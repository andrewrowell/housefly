package com.andrewjrowell.fly.entities;

/**
 * <p>Represents a predator in the {@link GamePlayScreen} that can kill the fly.</p>
 * 
* @author Andrew Rowell
* @version 1.0
*/


public class Predator {
	public int type;
	
	public float x,y; // Position of the center of the predator on screen
	
	public final float max_x; // Width of the screen
	
	public boolean direction; // True if moving right, false if moving left
	
	/**
	 * 
	 * @param screenwidth width of game world in pixels
	 * @param screenheight height of game world in pixels
	 */
	public Predator(float screenwidth, float screenheight){
		
		// Pick a type of predator.
		// 1 is a spider 32 pixels wide
		// 2 is a bat 64 pixels wide
		// 3 is a duck 96 pixels wide
		type = (int) (Math.random() * 100) + 1;
		if(type <= 90){
			type = 1;
		} else if(type > 90 && type < 98){
			type = 2;
		} else {
			type = 3;
		}
		
		// Pick a random starting point on the X axis
		x = (float) (Math.random() * screenwidth);
		
		// The next three if statements work in the same way,
		// so I'll only comment on the first one
		
		if(type == 1){ // If the predator is a spider
			
			// Put it far enough above the top of the screen (32 pixels above)
			// that it can't be seen until it starts to move down
			y = screenheight + 32;
			
			// correct the x-axis placement so that the entire predator is
			// on screen when it starts to scroll down
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
		
		// Pick a random direction for the predator to be moving
		if(Math.random() >= 0.5){
			direction = true;
		} else {
			direction = false;
		}
		System.out.println("New predator at: (" + x + ',' + y + ")");
	}
	
	/**
	 * Update the position of the Predator
	 * 
	 * @param deltaTime time since last update()
	 * @param pace modifies the speed that the predator moves
	 */
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
