package com.geeselightning.zepr.tests;

import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.entities.Zombie;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

@RunWith(GdxTestRunner.class)
public class ZombieTest {

	Zombie fast, medium, slow;

	@Before
	public void init() {
		fast = new Zombie(null, 0.3f, new Vector2(7,7), 0, Zombie.Type.FAST);
		medium = new Zombie(null, 0.3f, new Vector2(7,7), 0, Zombie.Type.MEDIUM);
		slow = new Zombie(null, 0.3f, new Vector2(7,7), 0, Zombie.Type.SLOW);
	}
	
	@Test
	public void zombieSpeedHierarchy() {
		assertTrue("Slow zombie should be slower than medium, and medium should be slower than fast.",
				(fast.getSpeed() > medium.getSpeed() && (medium.getSpeed() ) > slow.getSpeed()));
	}
	
	@Test
	public void zombieHealthHierarchy() {
		assertTrue("Slow zombie should have more health than medium, who should have more than fast.",
				(slow.getHealth() > medium.getHealth()) && (medium.getHealth() > fast.getHealth()));
	}
	
	@Test
	public void zombieDamageHierarchy() {
		assertTrue("Medium zombie should deal more damage than fast and slow zombies",
				(medium.getAttackDamage() > slow.getAttackDamage()) 
				&& (slow.getAttackDamage() == fast.getAttackDamage()));
	}

}
