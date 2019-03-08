package com.geeselightning.zepr.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.FixtureType;

/**
 * A human computer-controlled character that will persue zombies and attempt to attack zombies
 */
public class Human extends Character{
	
	private final float hitCooldown = Constant.HUMANHITCOOLDOWN;
	private float healthMulti = Constant.HUMANMAXHP;
	private float speedMulti = Constant.HUMANSPEED;
	private float damageMulti = Constant.HUMANDMG;
	
	private int density = 10;
	
	public boolean inMeleeRange;
	
	public float stunTimer;
	
	public Human(Zepr parent, float bRadius, Vector2 initialPos, float initialRot) {
		super(parent, new Sprite(new Texture("player03.png")), bRadius, initialPos, initialRot);
		this.speed = (int) speedMulti;
		this.health = (int) healthMulti;
		this.attackDamage = (int) damageMulti;
	}

	@Override
	public void defineBody() {
		BodyDef bDef = new BodyDef();
		bDef.type = BodyDef.BodyType.DynamicBody;
		bDef.position.set(initialPos);
		
		FixtureDef fBodyDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(this.bRadius);
		fBodyDef.shape = shape;
		fBodyDef.density = density;
		
		b2body = world.createBody(bDef);
		b2body.createFixture(fBodyDef).setUserData(FixtureType.HUMAN);
		
		b2body.setUserData(this);
		shape.dispose();
		
		b2body.setLinearDamping(5f);
		b2body.setAngularDamping(5f);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if(stunTimer > 0) {
			stunTimer -= delta;
			return;
		}
		
		/*
		 * need to get list of the zombies here
		 */
		Zombie zombie = closestZombie();
		Vector2 zombieVector = getVectorTo(zombie);
		b2body.applyLinearImpulse(zombieVector.nor().scl(speedMulti), getPos(), true);
		
		double angle = Math.toDegrees(Math.atan2(zombieVector.y,  zombieVector.x)) - 90;
		
		this.setAngle(angle);
		
		if (inMeleeRange && hitRefresh > hitCooldown) {
			//damage the zombie in question
			hitRefresh = 0;
		} else {
			hitRefresh += delta;
		}
	}
	
	public void beginContact() {
		this.inMeleeRange = true;
	}
	
	public void endContact() {
		this.inMeleeRange = false;
	}


	@Override
	public void takeDamage(int damage) {
		//zombie reference
		Zombie zombie = new Zombie(parent, damageMulti, initialPos, damageMulti, null); //replace with ref to closest zombie
		Vector2 impulse = getVectorTo(zombie).nor();
		
		b2body.applyLinearImpulse(impulse.scl(-8f * b2body.getMass()), getPos(), true);
		
		stunTimer = 0.5f;
		
		if (health - damage >= 0) {
			health -= damage;
		} else {
			health = 0;
			this.alive = false;
		}
	}
	
	/**
	 * Function to return the closest zombie to this human
	 */
	private Zombie closestZombie() {
		ArrayList<Zombie> zombieList = gameManager.getZombies();
		Zombie closestZombie = zombieList.get(0);
		for (Zombie zombie : zombieList) {
			if (zombie != closestZombie) {
				double temp1 = distanceFrom(closestZombie);
				double temp2 = distanceFrom(zombie);
				if (temp2 > temp1) {
					closestZombie = zombie;
				}
			}
		}
		return closestZombie;	
	}

}
