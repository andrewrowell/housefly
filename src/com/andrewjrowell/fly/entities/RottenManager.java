package com.andrewjrowell.fly.entities;

import java.util.ArrayList;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Rectangle;

/**
 * <p>Encapsulates the code that manages the rottens away from
 * the GamePlayScreen</p>
 * 
 * @author andrew
 *
 */

public class RottenManager {
	final float WORLD_WIDTH = 320; // width of game world in pixels
	final float WORLD_HEIGHT = 480; // height of game world in pixels
	
	float rottencounter; // Timer for spawning another rotten
	
	ArrayList<Rotten> rottens;
	
	public RottenManager(){
		rottens = new ArrayList<Rotten>();
		rottencounter = 0;
	}
	
	/**
	 * <p>updates the positions of the rottens and adds a
	 * new one if necessary</p>
	 * 
	 * @param deltaTime time since last update
	 * @param pace speed modifier
	 */
	public void update(float deltaTime, float pace){
		rottencounter += deltaTime * pace / 48.0;
		// Divide by 48 so at normal pace, counter
		// represents time in seconds
		
		if(rottencounter >= 5){
			rottens.add(new Rotten(WORLD_WIDTH, WORLD_HEIGHT));
			rottencounter = 0;
		}
		
		for(Rotten r : rottens){
			r.update(deltaTime, pace);
		}
	}
	
	/**
	 * <p>Does a bounds check to see if the fly has hit
	 * any rottens</p>
	 * 
	 * @param fly Player's Fly
	 * @return number of rottens eaten
	 */
	public int eaten(PlayerFly fly){
		int eaten = 0;
		ArrayList<Rotten> rottens2 = new ArrayList<Rotten>();
		for(Rotten r : rottens){
			if(OverlapTester.overlapRectangles(
					fly.getBounds(),
					new Rectangle(r.x - (int)(16.0 * WORLD_WIDTH/320.0), r.y - (int)(16.0 * WORLD_HEIGHT/480.0),
							(int)(32.0 * WORLD_WIDTH/320.0), (int)(32.0 * WORLD_HEIGHT/480.0)))){
				r.eaten = true;
				fly.grow();
				MainAssets.slurp.play(1.0f);
				eaten += 1;
			} else {
				rottens2.add(r);
			}
		}
		rottens = rottens2;
		return eaten;
	}
	
	/**
	 * <p> lets rendering code access individual rottens
	 * @return ArrayList of the individual Rottens
	 */
	public ArrayList<Rotten> getRottens(){
		return rottens;
	}
}
