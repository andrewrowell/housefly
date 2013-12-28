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
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	
	float rottencounter; // Timer for spawning another rotten
	
	ArrayList<Rotten> rottens;
	
	public RottenManager(float worldwidth, float worldheight){
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		rottens = new ArrayList<Rotten>();
		rottencounter = 0;
	}
	
	public void update(float deltaTime, int pace){
		rottencounter += deltaTime * pace;
		if(rottencounter >= 5){
			rottens.add(new Rotten(WORLD_WIDTH, WORLD_HEIGHT));
			rottencounter = 0;
		}
		
		for(Rotten r : rottens){
			r.update(deltaTime, pace);
		}
	}
	
	public int eaten(PlayerFly fly){
		int eaten = 0;
		ArrayList<Rotten> rottens2 = new ArrayList<Rotten>();
		for(Rotten r : rottens){
			if(OverlapTester.overlapRectangles(
					fly.getBounds(),
					new Rectangle(r.x - 16, r.y - 16, 32, 32))){
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
	
	public ArrayList<Rotten> getRottens(){
		return rottens;
	}
}
