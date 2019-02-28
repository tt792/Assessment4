package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;

/**
 * Abstract class that represents an object in the {@link World}. <br/>
 * Implemented in assessment 3.
 * @author Xzytl
 *
 */
public abstract class Entity {
	
	protected Zepr parent;
	// The active GameManager instance.
	protected GameManager gameManager;
	// The world the body exists in.
	protected World world;
	// The radius of the body's collision shape.
	protected float bRadius;
	// The entity's box2d body.
	protected Body b2body;
	// The entity's visual sprite.
	protected Sprite sprite;
	// The initial spawn location the entity appears at.
	protected Vector2 initialPos;
	// The initial rotation the entity has upon spawning.
	protected float initialRot;
	// Flag representing whether the entity is still 'alive'.
	// Dead entities are removed from the world.
	protected boolean alive = true;

	public Entity(Zepr parent, Sprite sprite, float bRadius, Vector2 initialPos, float initialRot) {
		this.parent = parent;
		this.sprite = sprite;
		this.bRadius = bRadius;
		this.initialPos = initialPos;
		this.initialRot = initialRot;
		sprite.setSize(bRadius * 2, bRadius * 2);
		sprite.setOrigin(bRadius, bRadius);
		gameManager = GameManager.getInstance(parent);
		world = gameManager.getWorld();
	}
	
	/* Accessor methods */
	public Body getBody() {
		return b2body;
	}
	
	public Vector2 getPos() {
		return b2body.getPosition();
	}
	
	public float getX() {
		return getPos().x;
	}
	
	public float getY() {
		return getPos().y;
	}
	
	public float getRot() {
		return (float) Math.toDegrees(b2body.getAngle());
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	/* Body transform methods */
	public void moveTo(float x, float y) {
		b2body.setTransform(x, y, b2body.getAngle());
	}
	
	public void setAngle(float angle) {
		b2body.setTransform(b2body.getPosition(), (float)Math.toRadians(angle));
	}
	
	public void setAngle(double angle) {
		b2body.setTransform(b2body.getPosition(), (float)Math.toRadians(angle));
	}
	
	/**
	 * Defines the {@link Fixture}'s that comprise the body of the entity.
	 */
	public abstract void defineBody();
	
	/**
	 * Draws the entity on the screen.
	 * @param batch	the {@link SpriteBatch} to use for rendering.
	 */
	public void draw(SpriteBatch batch) {
		float posX = b2body.getPosition().x - bRadius;
		float posY = b2body.getPosition().y - bRadius;
		float rotation = (float) Math.toDegrees(b2body.getAngle());
		sprite.setPosition(posX, posY);
		sprite.setRotation(rotation);
		sprite.draw(batch);
	}
	
	/**
	 * Defines the entity's activity every update cycle
	 * @param delta	the seconds since the last update
	 */
	public abstract void update(float delta);
	
	/**
	 * Removes the entity from the box2d world.
	 * Warning: it is best not to manually call this method; calling it while the world simulation
	 * step is occuring is likely to cause errors.
	 */
	public void delete() {
		world.destroyBody(b2body);
		b2body.setUserData(null);
		b2body = null;
	}
	
	public double distanceFrom(Entity entity) {
		return Math.sqrt(Math.pow(getX() - entity.getX(), 2) + Math.pow(getY() - entity.getY() ,2));
	}
	
	public Vector2 getVectorTo(Entity entity) {
		float dx = entity.getX() - getX();
		float dy = entity.getY() - getY();
		
		return new Vector2(dx, dy);
	}

}
