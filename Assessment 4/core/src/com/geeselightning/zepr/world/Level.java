package com.geeselightning.zepr.world;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;

/**
 * Represents a distinct game level, with a related TiledMap. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>level is no longer responsible for rendering (screen-related items moved to GameScreen)</li>
 * <li>level is no longer responsible for game logic (logic-related items moved to GameManager)</li>
 * <li>wave design moved to Wave class</li>
 * <li>level is no longer responsible for HUD (moved to Hud class)</li>
 * <li>all level code is now contained in one class, instead of each level possessing its own child class</li>
 * </ul>
 * @author Xzytl
 *
 */
public class Level {
	
	public enum Location {
		TOWN("Town", "townmap", 0),
		HALIFAX("Halifax College", "halifaxmap", 1),
		CENTRALHALL("Central Hall", "centralhallmap", 2),
		COURTYARD("Courtyard", "courtyard", 3),
		LIBRARY("Library", "library", 4),
		RONCOOKE("Ron Cooke Hub", "roncooke", 5);
		
		// Human-readable level name
		String name;
		// File name of the TiledMap for the level
		String mapFileName;
		// The order of the levels
		int num;
		
		Location(String name, String mapFileName, int num) {
			this.name= name;
			this.mapFileName = mapFileName;
			this.num = num;
		}
		
		public int getNum() {
			return num;
		}
	}
	
	/** The active {@link GameManager} instance. **/
	private GameManager gameManager;
	
	private Location location;
	private TiledMap tiledMap;
	// The background layers of the TiledMap.
	private int[] backgroundLayers = {0};
	// The foreground levels of the TiledMap.
	private int[] foregroundLayers = {1};
	
	// The position of the player spawn point.
	private Vector2 playerSpawn;
	// The positions of the zombie spawn points.
	private List<Vector2> zombieSpawnPoints;

	public Level(Zepr parent, Location location) {
		this.location = location;
		this.gameManager = GameManager.getInstance(parent);
		zombieSpawnPoints = new ArrayList<>();
	}
	
	/**
	 * Loads the TiledMap from file, retrieves spawn point objects and builds collision walls.
	 * @return	the TiledMap for the level location
	 */
	public TiledMap load() {
		tiledMap = new TmxMapLoader().load("maps/" + location.mapFileName + ".tmx");
		
		MapLayer spawnLayer = tiledMap.getLayers().get("Spawns");
		// Fetch spawn points from the spawn object layer.
		MapObjects spawnPoints = spawnLayer.getObjects();
		// Parse the spawn points into world coordinates and retrieve the type
		spawnPoints.forEach(sp -> {
			MapProperties props = sp.getProperties();
			String type = props.get("type", String.class);
			float x = props.get("x", Float.class) / Constant.PPT;
			float y = props.get("y", Float.class) / Constant.PPT;
			if (type == null) {
				return;
			}
			if (type.equals("player")) {
				playerSpawn = new Vector2(x,y);
			} else if (type.equals("zombie")) {
				zombieSpawnPoints.add(new Vector2(x,y));
			} else {
				System.out.println("Unrecognised spawnpoint type '" + type + "'.");
			}
		});
		
		// Use the MapBodyBuilder utility class to build walls from the TiledMap
		MapBodyBuilder.buildBodies(tiledMap, gameManager.getWorld());
		
		return tiledMap;
	}
	
	public TiledMap getTiledMap() {
		return tiledMap;
	}
	
	public int[] getBackgroundLayers() {
		return backgroundLayers;
	}
	
	public int[] getForegroundLayers() {
		return foregroundLayers;
	}
	
	public Vector2 getPlayerSpawn() {
		return playerSpawn;
	}
	
	public List<Vector2> getZombieSpawns() {
		return zombieSpawnPoints;
	}

}
