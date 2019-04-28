package com.geeselightning.zepr.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.geeselightning.zepr.KeyboardController;
import com.geeselightning.zepr.entities.BossZombie;
import com.geeselightning.zepr.entities.Entity;
import com.geeselightning.zepr.entities.Human;
import com.geeselightning.zepr.entities.Player;
import com.geeselightning.zepr.entities.PowerUp;
import com.geeselightning.zepr.entities.Zombie;
import com.geeselightning.zepr.stages.Hud;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.util.RandomEnum;
import com.geeselightning.zepr.world.Level;
import com.geeselightning.zepr.world.Wave;
import com.geeselightning.zepr.world.WorldContactListener;

import box2dLight.RayHandler;

//import box2dLights.RayHandler;

/**
 * Coordinator for the main game logic and rendering. <br/>
 * Implemented in assessment 3.
 * @author Xzytl
 */
public class GameManager implements Disposable {

	private final Zepr parent;
	// The instance of GameManager used by the game.
	public static GameManager instance;
	
	public boolean cureFound = false;

	// The preferences file that holds  data.
	private Preferences prefs;

	// Defines whether the game is currently running.
	private boolean gameRunning = false;
	// Defines whether a level is currently loaded.
	private boolean levelLoaded = false;

	// The furthest level reached.
	private int levelProgress = 0;

	/* GameScreen display objects */
	private OrthographicCamera gameCamera;
	private SpriteBatch batch;

	// The controller used to interact with the player character.
	private KeyboardController controller;

	// The map of waves in each level.
	private Map<Level.Location, Wave[]> waves = new HashMap<>();

	/* Level specific fields */
	// The box2d world the entities exist in.
	private World world;
	// The map renderer.
	private TiledMapRenderer tiledMapRenderer;
	private Box2DDebugRenderer debugRenderer;

	private RayHandler rayHandler;
	private Hud hud;
  
	private Player player;
	private Player.Type playerType;
	private Level level;
	private Level.Location location;
	// The current wave being attempted.
	private int waveProgress = 0;
	// The number of zombies queued for spawning.
	private int zombiesToSpawn = 0;
	// The time until the next set of zombies will be spawned.
	private float spawnCooldown = 0;
	// Defines whether there is a boss active in the current wave.
	private boolean activeBoss;

	// All the active entities - used for update and drawing.
	private ArrayList<Entity> entities;
	// All the active zombies.
	private ArrayList<Zombie> zombies;
	public ArrayList<Zombie> getZombies() {
		return zombies;
	}
	
	//all active humans
	private ArrayList<Human> humans;
	public ArrayList<Human> getHumans(){
		return humans;
	}
	
	// All the active power-ups.
	private ArrayList<PowerUp> powerUps;

	// Random generator for zombie types.
	private static RandomEnum<Zombie.Type> randomZombieType = new RandomEnum<Zombie.Type>(Zombie.Type.class);

	private GameManager(Zepr parent) {
		this.parent = parent;

		controller = new KeyboardController();
		
		prefs = Gdx.app.getPreferences("ZEPR Preferences");

		// Define the waves for each level
		waves.put(Level.Location.TOWN, new Wave[] { Wave.SMALL, Wave.MEDIUM });
		waves.put(Level.Location.HALIFAX, new Wave[] { Wave.SMALL, Wave.MEDIUM });
		waves.put(Level.Location.CENTRALHALL, new Wave[] { Wave.MEDIUM, Wave.LARGE, Wave.MINIBOSS });
		waves.put(Level.Location.COURTYARD, new Wave[] { Wave.MEDIUM, Wave.LARGE, Wave.MEDIUM });
		waves.put(Level.Location.LIBRARY, new Wave[] { Wave.SMALL, Wave.MEDIUM, Wave.MEDIUM });
		waves.put(Level.Location.RONCOOKE, new Wave[] { Wave.LARGE, Wave.LARGE, Wave.BOSS });
		waves.put(Level.Location.ZOMBIE1, new Wave[] { Wave.SMALL});
		waves.put(Level.Location.ZOMBIE2, new Wave[] { Wave.SMALL});
	}

	/**
	 * Fetches the current instance of GameManager, or creates a new one if none currently exist.
	 * @param parent	the instance of the game to use
	 * @return	a GameManager instance
	 */
	public static GameManager getInstance(Zepr parent) {
		if (instance == null) {
			instance = new GameManager(parent);
		}
		return instance;
	}

	/* Accessor methods */
	public Preferences getPrefs() {
		return prefs;
	}
	
	public boolean isGameRunning() {
		return gameRunning;
	}

	public void setGameRunning(boolean running) {
		this.gameRunning = running;
	}

	public boolean isLevelLoaded() {
		return levelLoaded;
	}

	public void setLevelLoaded(boolean levelLoaded) {
		this.levelLoaded = levelLoaded;
	}

	public OrthographicCamera getGameCamera() {
		return gameCamera;
	}

	public void setGameCamera(OrthographicCamera gameCamera) {
		this.gameCamera = gameCamera;
	}

	public SpriteBatch getSpriteBatch() {
		return batch;
	}

	public void setSpriteBatch(SpriteBatch batch) {
		this.batch = batch;
	}

	public KeyboardController getController() {
		return controller;
	}

	public int getLevelProgress() {
		return levelProgress;
	}

	public void setLevelProgress(int levelProgress) {
		this.levelProgress = levelProgress;
	}

	public Wave getWave(Level.Location location, int wave) {
		if (waves.containsKey(location)) {
			return waves.get(location)[wave];
		} else {
			return Wave.SMALL;
		}
	}

	public World getWorld() {
		return world;
	}

	public Player getPlayer() {
		return player;
	}

	public Player.Type getPlayerType() {
		return playerType;
	}

	public void setPlayerType(Player.Type playerType) {
		this.playerType = playerType;
	}

	public Level.Location getLocation() {
		return location;
	}

	public void setLocation(Level.Location location) {
		this.location = location;
	}

	public Level getLevel() {
		return level;
	}

	public int getWaveProgress() {
		return waveProgress;
	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		this.entities.remove(entity);
	}	
	
	public void addHuman(Human human) {
		this.humans.add(human);
		this.entities.add(human);
	}	
	
	public void removeHuman(Human human) {
		this.humans.remove(human);
		this.entities.remove(human);
	}

	public void addZombie(Zombie zombie) {
		this.zombies.add(zombie);
		this.entities.add(zombie);
	}

	public void removeZombie(Zombie zombie) {
		this.zombies.remove(zombie);
		this.entities.remove(zombie);
	}

	public void addPowerUp(PowerUp powerUp) {
		this.powerUps.add(powerUp);
		this.entities.add(powerUp);
	}

	public void removePowerUp(PowerUp powerUp) {
		this.powerUps.remove(powerUp);
		this.entities.remove(powerUp);
	}

	/**
	 * Gets the mouse position in screen coordinates (origin top-left).
	 * 
	 * @return a {@link Vector2} representing the mouse's position on the screen
	 */
	public Vector2 getMouseScreenPos() {
		return new Vector2(Gdx.input.getX(), Gdx.input.getY());
	}

	/**
	 * Gets the mouse position in world coordinates (origin center).
	 * 
	 * @return a {@link Vector2} representing the mouse's position in the world
	 */
	public Vector2 getMouseWorldPos() {
		Vector3 mousePos3D = gameCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector2 mousePos = new Vector2(mousePos3D.x, mousePos3D.y);
		return mousePos;
	}

	/**
	 * Loads the {@link Level} defined by the location variable and sets level-specific fields.
	 * Assessment 4:
	 * (F12) When in the last Zombie level spawns the cure to heal the player
	 */
	public void loadLevel() {
		System.out.println("Beginning level loading...");
		// Create a new box2d world.
		world = new World(Vector2.Zero, true);
		world.setContactListener(new WorldContactListener());

		debugRenderer = new Box2DDebugRenderer();

		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0.7f);

		level = new Level(parent, location);
		
		// Loads the level and creates the renderer for the TiledMap.
		tiledMapRenderer = new OrthogonalTiledMapRenderer(level.load(), 1 / (float) Constant.PPT);
		tiledMapRenderer.setView(gameCamera);

		hud = new Hud(parent);

		// Instantiates entity lists.
		this.entities = new ArrayList<>();
		this.zombies = new ArrayList<>();
		this.humans = new ArrayList<>();
		this.powerUps = new ArrayList<>();

		spawnPlayer();

		hud.setHealthLabel(player.getHealth());

		// Initialises/resets the wave progress to the start.
		waveProgress = 0;

		levelLoaded = true;
		
		//Assessment 4: Spawn a cure to become human again
		if(levelProgress == 11) {
			PowerUp powerUp = new PowerUp(parent, 0.2f, new Vector2(level.getPlayerSpawn().x + 10.f, level.getPlayerSpawn().y + 10.f), 0, PowerUp.Type.STORYCURE);
			powerUp.defineBody();
			addPowerUp(powerUp);
		}

		loadWave();

		System.out.println("Finished level loading");
	}

	/**
	 * Creates the player character and places it in the world.
	 */
	public void spawnPlayer() {
		player = new Player(parent, 0.3f, level.getPlayerSpawn(), 0f, playerType);
		player.defineBody();
		addEntity(player);
	}

	/**
	 * Determines number of zombies to spawn based on wave parameter.
	 * Spawns a power-up if a wave has just been completed.
	 */
	public void loadWave() {
		System.out.println("Beginning wave loading...");
		boolean spawnBoss = false;
		activeBoss = false;
		List<Vector2> zombieSpawns = level.getZombieSpawns();
		switch (getWave(this.location, waveProgress)) {
		case LARGE:
			zombiesToSpawn = 4 * zombieSpawns.size();
			break;
		case MEDIUM:
			zombiesToSpawn = 3 * zombieSpawns.size();
			break;
		case SMALL:
			zombiesToSpawn = 2 * zombieSpawns.size();
			break;
		case MINIBOSS:
		case BOSS:
			zombiesToSpawn = 3 * zombieSpawns.size();
			spawnBoss = true;
			break;
		default:
			break;
		}
		hud.setProgressLabel(waveProgress + 1, zombiesToSpawn);
		spawnCooldown = 0;
		System.out.println("Zombies to spawn: " + zombiesToSpawn);
		
		//Assessment 4 Added human spawning at the start of a level
		if(levelProgress < 9) {
			for (int i = 0; i < 5; i++) {
				Human human = new Human(parent, 0.3f, level.getPlayerSpawn(), 0); //create the human at the same place as the player
				human.defineBody();
				addHuman(human);
			}
		}
		
		if (waveProgress > 0) {
			PowerUp powerUp = new PowerUp(parent, 0.2f, level.getPlayerSpawn(), 0, /*randomPowerUpType.getRandom()*/PowerUp.Type.CURE);
			powerUp.defineBody();
			addPowerUp(powerUp);
		}

		if (spawnBoss) {
			spawnBoss(zombieSpawns.get(0));
		}
		System.out.println("Finished wave loading");
	}
	
	/**
	 * Spawns a boss in the level.
	 * @param spawnLocation	the world location to spawn the boss
	 */
	public void spawnBoss(Vector2 spawnLocation) {
		BossZombie.Type type;
		Wave wave = getWave(this.location, waveProgress);
		if (wave.equals(Wave.MINIBOSS)) {
			type = BossZombie.Type.MINIBOSS;
		} else if (wave.equals(Wave.BOSS)) {
			type = BossZombie.Type.BOSS;
		} else {
			return;
		}
		BossZombie zombie = new BossZombie(parent, spawnLocation, 0, type);
		zombie.defineBody();
		addEntity(zombie);
		activeBoss = true;
	}

	/**
	 * Assessment 4:
	 * <li>(F11) When in the last Zombie level spawns the cure to heal the player</li></br>
	 * Spawns a set of Zombies in the level.
	 * @param delta	the seconds since the last update cycle
	 */
	public void spawnZombies(float delta) {
		if (spawnCooldown <= 0) {
			List<Vector2> zombieSpawns = level.getZombieSpawns();
			
			if(getLevelProgress() > 9) {
				zombieSpawns.forEach(sp -> {
					Zombie zombie = new Zombie(parent, 0.3f, sp, 0,
							Zombie.Type.HUMAN); //spawns humans against the player in the Zombie Levels
					zombie.defineBody();
					addZombie(zombie);
					zombiesToSpawn -= 1;
				});
			}
			else {
				zombieSpawns.forEach(sp -> {
					Zombie zombie = new Zombie(parent, 0.3f, sp, 0,
							randomZombieType.getRandom());
					zombie.defineBody();
					addZombie(zombie);
					zombiesToSpawn -= 1;
				});
			}
			spawnCooldown = 3f;
		} else {
			spawnCooldown -= delta;
		}
	}

	/**
	 * Advances to the next wave if one exists, or ends the level if not.
	 */
	public void waveComplete() {
		System.out.println("Wave complete!");
		this.waveProgress += 1;

		if (waveProgress >= waves.get(location).length) {
			levelComplete();
		} else {
			loadWave();
		}
	}

	/**
	 * Unlocks the next level and changes to the level complete screen. </br>
	 * Assessment 4:
	 * <li>(F11) Changed to update level progress when in Zombie levels</li>
	 */
	public void levelComplete() {
		if (location.getNum() + 1 > levelProgress) {
			levelProgress += 1;
		}
		levelLoaded = false;
		gameRunning = false;
		if(levelProgress == 11)
			parent.changeScreen(Zepr.ZOMBIE2);
		else
			parent.changeScreen(Zepr.LEVEL_COMPLETE);
	}

	//Assessment 4: Stop playing as a zombie and return to where you where
	void returnToNormalGame() {
		setLevelProgress(player.levelToReturn);
		player.SetHitsTaken(0);
		player.beenZombie = true;
		parent.changeScreen(Zepr.ZOMBIE3);
	}
	
	/**
	 * <p>Assessment 4:
	 * <li>(F12) Changed so that it activates the cure when the player presses the button E</li>
	 * </p>
	 * Runs update logic for each entity, processes player input and updates the camera.
	 * @param delta	the seconds since the last update cycle
	 */
	public void update(float delta) {
		if (!gameRunning)
			return;
		if (!levelLoaded)
			return;
		if (gameCamera == null || batch == null)
			return;

		// Check if the player has been killed
		if (player.getHealth() <= 0) {
			entities.forEach(e -> e.delete());
			this.entities.clear();
			this.zombies.clear();
			this.humans.clear();
			this.powerUps.clear();
			spawnPlayer();
			loadWave();
		}
		
		//Assessment 4: Use the cure to turn zombies into humans
		if (player.hasCure && Gdx.input.isKeyPressed(Input.Keys.E)) {
			cure();
		}

		// Check if the wave has been completed
		if (levelProgress == 11) {
			if (cureFound) {
				returnToNormalGame();
			}
		}
		else if (zombies.size() + zombiesToSpawn == 0 && !activeBoss) {
			waveComplete();
		}
		
		hud.setBossLabel(activeBoss);

		// Resolve user input
		handleInput();

		// Re-centre the camera on the player after movement
		gameCamera.position.x = player.getX();
		gameCamera.position.y = player.getY();
		gameCamera.update();

		// Change the position of the map
		tiledMapRenderer.setView(gameCamera);

		// Spawns more zombies if necessary.
		if (zombiesToSpawn > 0) {
			spawnZombies(delta);
		}
		

		// Removes dead entities from the world.
		List<Entity> deadEntities = entities.stream().filter(e -> (!e.isAlive() && !(e instanceof Player)))
				.collect(Collectors.toList());
		deadEntities.forEach(e -> {
			e.delete();
			if (e instanceof Zombie) {
				zombies.remove(e);
			} else if (e instanceof PowerUp) {
				powerUps.remove(e);
			} else if (e instanceof BossZombie) {
				activeBoss = false;
			} else if (e instanceof Human) {
				humans.remove(e);
			}
			entities.remove(e);
		});
		entities.forEach(e -> e.update(delta));

		int zombiesAlive = activeBoss ? zombies.size() + zombiesToSpawn + 1: zombies.size() + zombiesToSpawn;
		hud.setProgressLabel(waveProgress + 1, zombiesAlive);
		hud.setHealthLabel(player.getHealth());

		// Displays the power-up available.
		if (powerUps.size() > 0) {
			hud.setPowerUpLabel(powerUps.get(0).getType());
		} else {
			hud.setPowerUpLabel(null);
		}

		// Renders entities on the screen.
		draw();

		// Step through the physics world simulation
		world.step(1 / 60f, 6, 2);
	}

	/**
	 * Assessment 4:
	 * (F13) Implements the cure 'curing' zombies in an area around the player
	 */
	public void cure() {
		for (Iterator<Zombie> zl = getZombies().iterator(); zl.hasNext(); ) { //loops through each zombie
			Zombie zombie = zl.next();
			double distance = player.distanceFrom(zombie);
			if (distance <= Constant.CURERANGE) { //if a zombie is within the range
				Human human = new Human(parent, 0.3f, zombie.getPos(), 0); //create a human at that zombies location
				human.defineBody();
				addHuman(human);
				zombie.takeDamage(1000, null); //kill the zombie in question
			}
		}
	}
	
	/**
	 * Retrieves user input from the {@link InputProcessor} and moves the player
	 * character accordingly.
	 */
	public void handleInput() {

		float modifier = 1f;
		float speed = player.getSpeed();

		if (player.isPowerUpActive(PowerUp.Type.SPEED)) {
			modifier = 2f;
		}

		speed *= modifier;

		if (controller.left) {
			player.setLinearVelocityX(-1 * speed);
		}
		if (controller.right) {
			player.setLinearVelocityX(speed);
		}
		if (controller.up) {
			player.setLinearVelocityY(speed);
		}
		if (controller.down) {
			player.setLinearVelocityY(-1 * speed);
		}
		if (controller.isMouse1Down) {
			player.setAttacking(true);
		} else {
			player.setAttacking(false);
		}

		// If both buttons for an axis are held down, or neither are, set velocity in
		// that axis to 0
		if ((!controller.up && !controller.down) || (controller.up && controller.down)) {
			player.setLinearVelocityY(0);
		}
		if ((!controller.left && !controller.right) || (controller.left && controller.right)) {
			player.setLinearVelocityX(0);
		}

	}

	/**
	 * Calls the draw() method for every entity, and renders the map.
	 */
	public void draw() {
		tiledMapRenderer.render(level.getBackgroundLayers());
		batch.setProjectionMatrix(gameCamera.combined);
		batch.begin();
		entities.forEach(entity -> entity.draw(batch));
		batch.end();
		tiledMapRenderer.render(level.getForegroundLayers());
		rayHandler.setCombinedMatrix(gameCamera);
		rayHandler.updateAndRender();
		// If dev mode is enabled, show the debug renderer for Box2D
		if (Zepr.devMode)
			debugRenderer.render(world, gameCamera.combined);
		hud.stage.draw();
	}

	@Override
	public void dispose() {
//		rayHandler.dispose();
		debugRenderer.dispose();
		world.dispose();
	}

}
