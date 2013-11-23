package com.andrewjrowell.fly.entities;

public class Powerup {
	public final static int POWERUP_ID_SPEED = 1;
	public final static int POWERUP_DURATION = 10;
	
	public float x, y;
	public int type;
	public boolean remove;
	
	public Powerup(float WORLD_WIDTH, float WORLD_HEIGHT){
		type = 1;
		x = (int) (Math.random() * (WORLD_WIDTH - 32)) + 16;
		
		remove = false;
		y = WORLD_HEIGHT;
	}
	
	public void update(float deltaTime, float pace){
		y -= deltaTime * pace;
		if(y <= 0){
			remove = true;
		}
	}
}
