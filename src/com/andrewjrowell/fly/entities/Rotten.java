package com.andrewjrowell.fly.entities;

public class Rotten {
	public int type;
	
	public float x,y;
	
	public boolean eaten;
	
	public Rotten(float screenwidth, float screenheight){
		type = (int) (Math.random() * 16) + 1;
		eaten = false;
		x = (float) (Math.random() * screenwidth);
		y = screenheight;
		System.out.println("New Rotten at: (" + x + ',' + y + ")");
	}
	
	public void update(float deltaTime, int pace){
		y -= deltaTime * pace;
	}
}
