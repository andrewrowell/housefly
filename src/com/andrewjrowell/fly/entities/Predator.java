package com.andrewjrowell.fly.entities;

public class Predator {
	public int type;
	
	public float x,y;
	
	public final float max_x;
	
	public boolean direction;
	
	public boolean eaten;
	
	public Predator(float screenwidth, float screenheight){
		type = (int) (Math.random() * 100) + 1;
		if(type <= 90){
			type = 1;
		} else if(type > 90 && type < 98){
			type = 2;
		} else {
			type = 3;
		}
		eaten = false;
		x = (float) (Math.random() * screenwidth);
		if(type == 1){
			y = screenheight + 32;
			if(x < 16){
				x = 16;
			}
			if(x > screenwidth - 16){
				x = screenwidth - 16;
			}
		}
		if(type == 2){
			y = screenheight + 64;
			if(x < 32){
				x = 32;
			}
			if(x > screenwidth - 32){
				x = screenwidth - 32;
			}
		}
		if(type == 3){
			y = screenheight + 96;
			if(x < 48){
				x = 48;
			}
			if(x > screenwidth - 48){
				x = screenwidth - 48;
			}
		}
		max_x = screenwidth;
		if(Math.random() >= 0.5){
			direction = true;
		} else {
			direction = false;
		}
		System.out.println("New predator at: (" + x + ',' + y + ")");
	}
	
	public void update(float deltaTime, float pace){
		y -= deltaTime * pace;
		if(type == 1){
			if(x <= 0){
				direction = true;
			}
			if(x >= max_x){
				direction = false;
			}
		
			if(direction){
				x += deltaTime * 32;
			} else {
				x -= deltaTime * 32;
			}
		}
		if(type == 2){
			if(x <= 0){
				direction = true;
			}
			if(x >= max_x){
				direction = false;
			}
		
			if(direction){
				x += deltaTime * 32 / 2;
			} else {
				x -= deltaTime * 32 / 2;
			}
		}
		if(type == 3){
			if(x <= 0){
				direction = true;
			}
			if(x >= max_x){
				direction = false;
			}
		
			if(direction){
				x += deltaTime * 32 / 4;
			} else {
				x -= deltaTime * 32 / 4;
			}
		}
	}
}
