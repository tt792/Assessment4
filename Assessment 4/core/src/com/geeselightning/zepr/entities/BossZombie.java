package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;
import com.geeselightning.zepr.world.FixtureType;
import com.geeselightning.zepr.world.WorldContactListener;

/**
 * A more powerful hostile computer-controlled character that will pursue and attempt to harm the 
 * player. <br/>
 * Implemented in assessment 3.
 * @author Xzytl
 *
 */
public class BossZombie extends Character {
	
	public enum Type {
		MINIBOSS("smallboss.png", 0.4f),
		BOSS("smallboss.png", 0.5f);
		
		String textureName;
		float bRadius;
		
		Type(String textureName, float bRadius) {
			this.textureName = textureName;
			this.bRadius = bRadius;
		}
	}
	
	// Determines the density of the zombie's box2d body.
	private int density;
	private float hitCooldown;
	
	// Determines whether the boss is able to attack the player
	private boolean inMeleeRange;
	
	private float stunTimer;
	
	private int attackDamage;

	public BossZombie(Zepr parent, Vector2 initialPos, float initialRot, Type type) {
		super(parent, new Sprite(new Texture(type.textureName)), type.bRadius, initialPos, initialRot);
		float speedModifier = 1.0f;
		float healthModifier = 1.0f;
		float damageModifier = 1.0f;
		switch(type) {
		case MINIBOSS:
			speedModifier = 1.2f;
			healthModifier = 1.0f;
			damageModifier = 1.5f;
			density = 15;
			hitCooldown = Constant.ZOMBIEHITCOOLDOWN / 0.75f;
			break;
		case BOSS:
			speedModifier = 0.8f;
			healthModifier = 2.0f;
			damageModifier = 2.0f;
			density = 20;
		default:
			break;
		}
		this.speed = (int) (Constant.ZOMBIESPEED * speedModifier);
		this.health = (int) (Constant.ZOMBIEMAXHP * healthModifier);
		this.attackDamage = (int) (Constant.ZOMBIEDMG * damageModifier);
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
		b2body.createFixture(fBodyDef).setUserData(FixtureType.BOSSZOMBIE);
		
		b2body.setUserData(this);
		shape.dispose();
		
		b2body.setLinearDamping(5f);
		b2body.setAngularDamping(5f);
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		
		if (stunTimer > 0) {
			stunTimer -= delta;
			return;
		}
		
		Player player = gameManager.getPlayer();
		
		Vector2 playerVector = getVectorTo(player);
		
		b2body.applyLinearImpulse(playerVector.nor(), getPos(), true);
		
		double angle = Math.toDegrees(Math.atan2(playerVector.y, playerVector.x)) - 90;
		
		this.setAngle(angle);
		
		if (inMeleeRange && hitRefresh > hitCooldown) {
			gameManager.getPlayer().takeDamage(this.attackDamage, this);
			hitRefresh = 0;
		} else {
			hitRefresh += delta;
		}
	}
	
	/**
	 * Called by {@link WorldContactListener} when the player is in contact.
	 */
	public void beginContact() {
		this.inMeleeRange = true;
	}
	
	/**
	 * Called by {@link WorldContactListener} when the player leaves contact.
	 */
	public void endContact() {
		this.inMeleeRange = false;
	}
	
	@Override
	public void takeDamage(int damage, Character attacker) {
		Vector2 impulse = getVectorTo(attacker);

		b2body.applyLinearImpulse(impulse.scl(-3f * b2body.getMass()), getPos(), true);
		
		stunTimer = 0.5f;
		
		if (health - damage >= 0) {
    		health -= damage;
    	} else {
    		health = 0;
    		this.alive = false;
    	}
	}

}
