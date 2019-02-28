package com.geeselightning.zepr.minigame;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.geeselightning.zepr.KeyboardController;

/**
 * Logic for minigame. Implemented in assessment 3.
 * @author ljd546
 * @Xzytl
 *
 */
public class MiniGame {
	//Used by MiniGameScreen to implement controls
	public KeyboardController miniGameController;
	//Player related variables
	public int ammo;
	public int score;
	// MiniGame Progress
	public int wave;
	//If inactive, MiniGameScreen stops rendering
	public boolean active;
	//Used to buffer clicks
	private float timeSinceLastClick;
	//Used to determine score gained
	public long startTime;
	//The goose thats flying
	public Goose goose;
	//Used to change goose's direction at random
	public Random rand;
	
	public MiniGame() {
		
		
		//initialises game variables
		this.ammo = 3;
		this.score = 0;
		this.wave = 1;
		this.goose = new Goose(1/10f);
		
		this.miniGameController = new KeyboardController();
		this.timeSinceLastClick = 0;
		this.rand = new Random();
		this.startTime = System.currentTimeMillis();
		start();
		
	}
	/**
	 * Activates the game
	 */
	protected void start() {
		this.active = true;
	}
	
	/**
	 * Checks the game state at every delta and updates accordingly.
	 * @param delta
	 */
	public void update(float delta) {
		
		//implements a delay on click
		this.timeSinceLastClick = this.timeSinceLastClick + delta;
		
		if (active){
			//Checks if the player has run out of ammo
			if (ammo > 0) {
				this.goose.update(delta);
				
			} else {
				nextWave();
			}
			//Changing direction randomly
			if (rand.nextInt(100) > 90) {
				goose.changeDirection();
			}
			// if any goose has 'escaped'
			goose.update(delta);
			if ( goose.currentPos.y > 360) {
				nextWave();
			}else {
				//On Click and click has been buffered
				if (Gdx.input.justTouched() && timeSinceLastClick > 0.009) {
					if (goose.checkMouse() && !goose.isDead){
						goose.die();
						
						//Score is added based on the time it took to click the goose.
						score = score +(100 - (int)(System.currentTimeMillis()- this.startTime)/100);
						System.out.println("HIT");
						nextWave();
					}
					//Lose ammo on click
					ammo = ammo - 1;	
					System.out.println("ammo: " + String.valueOf(this.ammo));
				}
				///Reset click buffer
				timeSinceLastClick = 0;
			}	
		}						
					
		
	}

	/**
	 * Advancing the game. Ammo is reset, time since goose spawn is reset, Goose now moves faster
	 */
	private void nextWave() {
		this.startTime = System.currentTimeMillis();
		active = false;
		ammo = 3;
		wave = wave + 1;
		this.timeSinceLastClick = 0;
		active = true;
		if (wave < 8) {
			this.goose = new Goose(wave/10f);
		} else {
			this.goose = new Goose(0.8f);
		}
	}
	
}
