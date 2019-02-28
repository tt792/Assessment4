package com.geeselightning.zepr.minigame;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
<<<<<<< HEAD
 * The goose entity for the minigame. Implemented in assessment 3.
 * @author ljd546
 */
public class Goose {
		//Position Variables
		float startX;
		float endX;
		public float speed;
		Vector2 velocity;
		public Vector2 currentPos;
		//Used to end the wave in Minigame
		public boolean isDead;
		//Used to animate the goose
		boolean flapping;
		//Used in click detection
		public Rectangle bounds;
		Sprite sprite;
		
		/*
		 * @param speed: increases based on the wave number in MiniGame
		 */
		protected Goose(float speed) {
			this.sprite = new Sprite(new Texture("goose.png"));
			this.velocity = new Vector2();
			this.currentPos = new Vector2();
			//Starts off alive
			this.isDead = false;
			//Starts off in initial sprite
			this.flapping = false;
			//Speed is set low to start off with
			this.speed = (speed + 0.1f) / 10f;
			
			Random rand = new Random();
			//Generates a random start location for the goose
			startX = rand.nextInt((Gdx.graphics.getWidth() + 1)) - Gdx.graphics.getWidth() /2;
			this.sprite.setFlip(true,false);
			
			//Generates a random but appropriate end location for the goose
			endX = rand.nextInt((Gdx.graphics.getWidth() + 1) - Gdx.graphics.getWidth()/2);
			if (startX > 0) {
				endX = endX * -1;
				//Sprite is flipped based on direction
				this.sprite.setFlip(false,false);
			}
			//Defining initial velocity, initial position
			velocity.x = (endX - startX);
			velocity.y = 720;
			currentPos = new Vector2(startX,-1 * Gdx.graphics.getHeight() /2 );
			System.out.println("Goose outs at: " + String.valueOf(endX));
		}
		
		/*
		 * Checks if the mouse cursor is within the bounds of the goose.
		 */
		public boolean checkMouse() {
			
			Rectangle bounds = new Rectangle(this.getSprite().getX(),this.getSprite().getY(),this.getSprite().getWidth(),this.getSprite().getHeight());
			if (bounds.contains(new Vector2(Gdx.input.getX() - Gdx.graphics.getWidth()/2,-1 * Gdx.input.getY() + Gdx.graphics.getHeight()/2))) {
				System.out.println("True");
				return true;
			}
			
			return false;
		}
		
		/**
		 * Takes a rendering delta to update the position of the goose
		 * @param delta
		 */
		public void update(float delta) {
			
			this.currentPos.x = this.currentPos.x +  (this.velocity.x * delta * speed);
			this.currentPos.y = this.currentPos.y + (this.velocity.y * delta * speed);
			
			//randomly flaps the gooses wings
			if ((int)this.currentPos.y % 10 == 0) {
				flap();
			}
		}
		/*
		 * returns the sprite
		 */
		public Sprite getSprite() {
			return this.sprite;
		}
		/**
		 * Draws the sprite at the position, chooses sprite if the goose is 'flapping' its wings.
		 * @param batch
		 */
		public void draw(SpriteBatch batch) {
			if (flapping) {
				this.sprite.setTexture(new Texture("gooseflap.png"));
			} else {
				this.sprite.setTexture(new Texture("goose.png"));
			}
			sprite.setPosition(this.currentPos.x, this.currentPos.y);
			sprite.draw(batch);
		}
		/*
		 * animates the sprite
		 */
		public void flap() {
			if (flapping) {	
				flapping = false;
			} else {
				flapping = true;
			}
		}
		/**
		 * Changes isDead to true;
		 */
		public void die() {
				this.isDead = true;
				System.out.println("Goose has died");
			
		}
		/**
		 * Changes the goose's direction randomly 
		 */
		public void changeDirection() {
			System.out.println("Changed Direction");
			Random rand = new Random();
			endX = rand.nextInt((Gdx.graphics.getWidth() + 1) - Gdx.graphics.getWidth()/2);
			this.sprite.setFlip(true, false);
			if (currentPos.x > 0) {
				endX = endX * -1;
				this.sprite.setFlip(false,false);
			}
			velocity.x = endX - currentPos.x;
		}
		
		

		
}
