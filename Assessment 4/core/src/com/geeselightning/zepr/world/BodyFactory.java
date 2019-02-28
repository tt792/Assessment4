package com.geeselightning.zepr.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Utility class for generating box2d fixtures. Implemented in assessment 3.
 * @author Xzytl
 *
 */
public class BodyFactory {
	
	public static void makeMeleeSensor(Body body, int points, float angle, float radius) {
		if (points < 2 || points > 7) {
			throw new IllegalArgumentException("Points must be between 2 and 7 (inclusive)!");
		}
		FixtureDef fDef = new FixtureDef();
		PolygonShape polyShape = new PolygonShape();
		Vector2[] vertices = new Vector2[points + 1];
		
		vertices[0] = new Vector2(0,0);
		float startAngle;
		float subAngle = angle / (float)points;
		if (points % 2 == 0) {
			startAngle = (subAngle / 2) + (((points - 2) / 2) * subAngle) + 90;
		} else {
			startAngle = subAngle * ((points - 1) / 2) + 90;
		}
		for (int i = 0; i < points; i++) {
			double radAngle = Math.toRadians(startAngle);
			vertices[i+1] = new Vector2(radius * (float)Math.cos(radAngle), radius * (float)Math.sin(radAngle));
			startAngle -= subAngle;
		}
		polyShape.set(vertices);
		fDef.shape = polyShape;
		fDef.isSensor = true;
		body.createFixture(fDef).setUserData(FixtureType.MELEE_SENSOR);;
		polyShape.dispose();
	}

}
