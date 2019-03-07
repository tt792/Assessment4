package com.geeselightning.zepr.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.util.Constant;

public class Human extends Character{
	
	private final float hitCooldown = Constant.HUMANHITCOOLDOWN;
	private float healthMulti = Constant.HUMANMAXHP;
	private float speedMulti = Constant.HUMANSPEED;
	private float damageMulti = Constant.HUMANDMG;
	
	private int density = 10;
	
	public boolean inMeleeRnage;
	
	public float stunTimer;
	
	public Human(Zepr parent, float bRadius, Vector2 initialPos, float initialRot) {
		super(parent, new Sprite(new Texture("HumanSprite.png")), bRadius, initialPos, initialRot);
		
	}

	@Override
	public void takeDamage(int dmg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void defineBody() {
		// TODO Auto-generated method stub
		
	}
	

}
