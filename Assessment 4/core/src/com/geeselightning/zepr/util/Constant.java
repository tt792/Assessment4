package com.geeselightning.zepr.util;

import com.badlogic.gdx.math.Vector2;

/**
 * Defines constants used in other classes in the program. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>added different multipliers for different zombie types</li>
 * <li>added definition of PPM (pixels-per-metre) and PPT (pixels-per-tile)</li>
 * <li>tweaked speed to fit impulse-based movement</li>
 * </ul>
 * @author Xzytl
 * 
 */
public final class Constant {
	
	public static final int PPM = 50;
	public static final int PPT = 25;
	
	public static final Vector2 ORIGIN = new Vector2(0, 0);
	public static final float PLAYERSPEED = 6;
	public static final int PLAYERMAXHP = 100;
	public static final int PLAYERDMG = 100000;//20;
	public static final int PLAYERRANGE = 50;
	public static final float PLAYERHITCOOLDOWN = 0.2f;
	public static final int PLAYERTURNHITS = 4; //Number of hits before player has a chance to turn
	public static final double PLAYERTURNCHANCE = 1; //Chance of the player to turn when hit
	public static final double PLAYERTURNCHANCE2 = 0.01; //Chance to turn after having turned once

	public static final float ZOMBIESPEED = 4;
	public static final int ZOMBIEMAXHP = 100;
	public static final int ZOMBIEDMG = 10;
	public static final int ZOMBIERANGE = 20;
	public static final float ZOMBIEHITCOOLDOWN = 1;
	
	public static final int HUMANMAXHP = 100;
	public static final int HUMANDMG = 10;
	public static final float HUMANSPEED = 3;
	public static final float HUMANHITCOOLDOWN = 1;
	public static final int HUMANRANGE = 20;

	public static final float SLOWSPEEDMULT = 0.5f;
	public static final float SLOWHPMULT = 2f;
	public static final float SLOWDMGMULT = 1;

	public static final float MEDSPEEDMULT = 1;
	public static final float MEDHPMULT = 1.5f;
	public static final float MEDDMGMULT = 2f;

	public static final float FASTSPEEDMULT = 2f;
	public static final float FASTHPMULT = 1;
	public static final float FASTDMGMULT = 1;

	public static final int HEALUP = 50;
	public static final float SPEEDUPTIME = 10;
	public static final float IMMUNITYTIME = 5;
	public static final float ATKUPTIME = 10;
	public static final float RPDFIRETIME = 10;
	public static final double CURERANGE = 10;
	
}
