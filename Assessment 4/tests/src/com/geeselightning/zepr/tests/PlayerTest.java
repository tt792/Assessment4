package com.geeselightning.zepr.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.entities.Player;
import com.geeselightning.zepr.entities.PowerUp;
import com.geeselightning.zepr.entities.Zombie;

@RunWith(GdxTestRunner.class)
public class PlayerTest {
	
	private Player nerdy;
	private Player sporty;
	private Player heavy;
	
	@Before
	public void init() {
		nerdy = new Player(null, 0.3f, new Vector2(0,5), 0f, Player.Type.NERDY);
		sporty = new Player(null, 0.3f, new Vector2(5,0), 0f, Player.Type.SPORTY);
		heavy = new Player(null, 0.3f, new Vector2(-5,0), 0f, Player.Type.HEAVY);
	}
	
	@Test
	public void playerAcceptsDamage() {
		assertNotEquals("Player health should not be null", nerdy.getHealth(), null);
		int initialHealth = nerdy.getHealth();
		nerdy.takeDamage(5);
		assertEquals("Player should lose 5 health", nerdy.getHealth(), initialHealth - 5);
	}
	
	@Test
	public void nerdyPlayerHasHigherHealth() {
		assertTrue("Nerdy type should have higher health than sporty", nerdy.getHealth() > sporty.getHealth());
		assertTrue("Nerdy type should have higher health than heavy", nerdy.getHealth() > heavy.getHealth());
	}
	
	@Test
	public void sportyPlayerHasHigherSpeed() {
		assertTrue("Sporty type should have higher speed than nerdy", sporty.getSpeed() > nerdy.getSpeed());
		assertTrue("Sporty type should have higher speed than heavy", sporty.getSpeed() > heavy.getSpeed());
	}
	
	@Test
	public void heavyPlayerHasHigherDamage() {
		assertTrue("Heavy type should have higher damage than nerdy", heavy.getAttackDamage() > nerdy.getAttackDamage());
		assertTrue("Heavy type should have higher damage than sporty", heavy.getAttackDamage() > sporty.getAttackDamage());
	}
	
	@Test
	public void playerCollectsPowerUp() {
		nerdy.onPickup(new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.IMMUNITY));
		assertTrue("Player should have active power-up", nerdy.isPowerUpActive(PowerUp.Type.IMMUNITY));
	}

	@Test
	public void playerRegistersZombie() {
		Zombie zombie = new Zombie(null, 0.3f, new Vector2(7,7), 0, Zombie.Type.MEDIUM);
		nerdy.onMeleeRangeEntered(zombie);
		assertTrue("Player should have zombie in melee range", nerdy.getZombiesInRange().contains(zombie));
		nerdy.onMeleeRangeLeft(zombie);
		assertFalse("Player should not have zombie in melee range", nerdy.getZombiesInRange().contains(zombie));
	}

}