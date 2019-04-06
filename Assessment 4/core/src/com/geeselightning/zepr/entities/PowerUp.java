package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.world.FixtureType;

import com.geeselightning.zepr.util.Constant;

/**
 * Represents a power-up item that have effects on the player. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>class now extends {@link Entity} instead of {@link Sprite}</li>
 * <li>different power-up classes consolidated into one, with a Type sub-enum</li>
 * <li>removed overlapsPlayer() method (collision handled by box2d)</li>
 * <li>power-up effect handled by player class</li>
 * </ul>
 * @author Xzytl
 *
 * Assessment 4:
 * (F12) Added the Cure powerup, to 'cure' Zombies in an area around the player
 */
public class PowerUp extends Entity {
	
	public enum Type {
		HEAL("heal.png", 0),
		IMMUNITY("immunity.png", Constant.IMMUNITYTIME),
		RAPID_FIRE("rapidfire.png", Constant.RPDFIRETIME),
		SPEED("speed.png", Constant.SPEEDUPTIME),
		STRENGTH("strength.png", Constant.ATKUPTIME),
		CURE("cure.png", 0),
		STORYCURE("cure.png", 0);
		
		String textureName;
		float duration;
		
		Type(String textureName, float duration) {
			this.textureName = textureName;
			this.duration = duration;
		}
	}
	
	private final Type type;

	public PowerUp(Zepr parent, float bRadius, Vector2 initialPos, float initialRot, Type type) {
		super(parent, new Sprite(new Texture(type.textureName)), bRadius, initialPos, initialRot);
		this.type = type;
	}
	
	public Type getType() {
		return type;
	}
	
	public float getDuration() {
		return type.duration;
	}
	
	// Assessment 3: added defineBody() method required for box2d integration.
	@Override
	public void defineBody() {
		BodyDef bDef = new BodyDef();
		bDef.type = BodyDef.BodyType.StaticBody;
		bDef.position.set(initialPos);
		
		CircleShape shape = new CircleShape();
		shape.setRadius(this.bRadius);
		
		FixtureDef fDef = new FixtureDef();
		fDef.shape = shape;
		
		b2body = world.createBody(bDef);
		b2body.createFixture(fDef).setUserData(FixtureType.POWERUP);
		b2body.setUserData(this);
		
		shape.dispose();
	}

	@Override
	public void update(float delta) {}

}
