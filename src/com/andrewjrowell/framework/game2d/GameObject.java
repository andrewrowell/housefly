package com.andrewjrowell.framework.game2d;

import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;

public abstract class GameObject {
	public final Vector2 position;
	public final Rectangle bounds;
	
	public GameObject(float x, float y, float width, float height){
		this.position = new Vector2(x, y);
		this.bounds = new Rectangle(x-width/2, y-height/2,width,height);
	}
}
