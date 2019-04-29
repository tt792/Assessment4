package com.geeselightning.zepr.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.entities.Player;
import com.geeselightning.zepr.entities.PowerUp;
import com.geeselightning.zepr.util.Constant;

@RunWith(GdxTestRunner.class)
//Assessment 4- added the testing numbers to each of tests
public class PowerUpTest {

    @Test
    //W2.1
    public void powerUpHealthDoesntAddHPToPlayerAtMaxHP() {
        Player player = new Player(null, 0.3f, new Vector2(0,5), 0f, Player.Type.NERDY);
        PowerUp heal = new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.HEAL);
        int damagedHealth = player.getHealth();
        player.onPickup(heal);
        player.update(1f);
        assertEquals("Heal power-up should give the player more hit points",
                damagedHealth , player.getHealth(), 0.1);
    }
    
    @Test
    //W2.2
    public void powerUpImmunityStopsDamage() {
    	Player player = new Player(null, 0.3f, new Vector2(0,5), 0f, Player.Type.NERDY);
        PowerUp immunity = new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.IMMUNITY);
        player.onPickup(immunity);
        int initialHealth = player.getHealth();
        player.takeDamage(10, null);
        assertEquals("Immunity power-up should prevent damage", initialHealth, player.getHealth());
    }
    
    @Test
    //W2.3
    public void powerUpExpires() {
    	Player player = new Player(null, 0.3f, new Vector2(0,5), 0f, Player.Type.NERDY);
    	PowerUp immunity = new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.IMMUNITY);
    	PowerUp heal = new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.HEAL);
    	PowerUp damage = new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.STRENGTH);
    	PowerUp speed = new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.SPEED);
    	PowerUp attackSpeed = new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.RAPID_FIRE);
    	player.onPickup(immunity);
    	player.onPickup(heal);
    	player.onPickup(damage);
    	player.onPickup(speed);
    	player.onPickup(attackSpeed);
    	
    	player.update(0.5f);
    	assertFalse("Heal power-up should have expired", player.isPowerUpActive(PowerUp.Type.HEAL));
    	
    	player.update(4.5f);
    	assertFalse("Immunity power-up should have expired", player.isPowerUpActive(PowerUp.Type.IMMUNITY));
    	
    	player.update(5f);
    	assertFalse("Damage power-up should have expired", player.isPowerUpActive(PowerUp.Type.STRENGTH));
    	assertFalse("Speed power-up should have expired", player.isPowerUpActive(PowerUp.Type.SPEED));
    	assertFalse("Rapid fire power-up should have expired", player.isPowerUpActive(PowerUp.Type.SPEED));
    }
    @Test
    //W2.4
    public void CureExist() {
    	PowerUp cure = new PowerUp(null, 0.2f, new Vector2(7,7), 0, PowerUp.Type.CURE);
    	assertEquals(cure.getType(), PowerUp.Type.CURE);
    }
   
    

}
