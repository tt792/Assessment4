package com.geeselightning.zepr.world;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.geeselightning.zepr.util.Constant;

/**
 * Converts map objects into Box2d objects.
 * Inspiration from
 * https://gamedev.stackexchange.com/questions/66924/how-can-i-convert-a-tilemap-to-a-box2d-world
 * with small modifications.
 * 
 * Implemented in assessment 3.
 * 
 * @author daemonaka (https://gamedev.stackexchange.com/users/41604/daemonaka)
 * @author Xzytl
 *
 */
public class MapBodyBuilder {

	public static Array<Body> buildBodies(TiledMap map, World world) {
		MapObjects objs = map.getLayers().get("Walls").getObjects();

		Array<Body> bodies = new Array<>();

		objs.forEach(o -> {
			if (o instanceof TextureMapObject) {
				System.out.println("Ignoring texture map object.");
				return;
			}

			Shape shape;

			if (o instanceof RectangleMapObject) {
				shape = getRectangle((RectangleMapObject) o);
			} else if (o instanceof PolygonMapObject) {
				shape = getPolygon((PolygonMapObject) o);
			} else if (o instanceof PolylineMapObject) {
				shape = getPolyline((PolylineMapObject) o);
			} else if (o instanceof CircleMapObject) {
				shape = getCircle((CircleMapObject) o);
			} else if (o instanceof EllipseMapObject) {
				shape = getEllipse((EllipseMapObject) o);
			} else {
				return;
			}

			BodyDef bDef = new BodyDef();
			bDef.type = BodyType.StaticBody;
			Body body = world.createBody(bDef);
			body.createFixture(shape, 1);

			bodies.add(body);
			shape.dispose();
		});
		
		return bodies;
	}

	private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
		Rectangle rectangle = rectangleObject.getRectangle();
		PolygonShape polygon = new PolygonShape();
		Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / Constant.PPT,
				(rectangle.y + rectangle.height * 0.5f) / Constant.PPT);
		polygon.setAsBox(rectangle.width * 0.5f / Constant.PPT, rectangle.height * 0.5f / Constant.PPT, size, 0.0f);
		return polygon;
	}

	private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
		PolygonShape polygon = new PolygonShape();
		float[] vertices = polygonObject.getPolygon().getTransformedVertices();

		float[] worldVertices = new float[vertices.length];

		for (int i = 0; i < vertices.length; ++i) {
			worldVertices[i] = vertices[i] / Constant.PPT;
		}

		polygon.set(worldVertices);
		return polygon;
	}

	private static ChainShape getPolyline(PolylineMapObject polylineObject) {
		float[] vertices = polylineObject.getPolyline().getTransformedVertices();
		Vector2[] worldVertices = new Vector2[vertices.length / 2];

		for (int i = 0; i < vertices.length / 2; ++i) {
			worldVertices[i] = new Vector2();
			worldVertices[i].x = vertices[i * 2] / Constant.PPT;
			worldVertices[i].y = vertices[i * 2 + 1] / Constant.PPT;
		}

		ChainShape chain = new ChainShape();
		chain.createChain(worldVertices);
		return chain;
	}

	private static CircleShape getCircle(CircleMapObject circleObject) {
		Circle circle = circleObject.getCircle();
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(circle.radius / Constant.PPT);
		circleShape.setPosition(new Vector2(circle.x / Constant.PPT, circle.y / Constant.PPT));
		return circleShape;
	}
	
	private static ChainShape getEllipse(EllipseMapObject ellipseObject) {
		Ellipse ellipse = ellipseObject.getEllipse();
		float width = ellipse.width / (2 * Constant.PPT);
		float height = ellipse.height / (2 * Constant.PPT);
		float x = (ellipse.x / Constant.PPT) + width;
		float y = (ellipse.y / Constant.PPT) + height;
		ChainShape chainEllipse = new ChainShape();
		Vector2[] vertices = new Vector2[64];
		for (int i = 0; i < 64; i++) {
			float t = (float)(i*2*Math.PI) / 64;
			vertices[i] = new Vector2((width * (float)Math.cos(t) + x), (height * (float)Math.sin(t) + y));
		}
		
		chainEllipse.createLoop(vertices);
		return chainEllipse;
	}

}
