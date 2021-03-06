package com.andrewjrowell.fly.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.fly.bitmapfonts.BigFont;
import com.andrewjrowell.framework.CoordinateConverter;
import com.andrewjrowell.framework.Game;
import com.andrewjrowell.framework.Screen;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.GLGame;
import com.andrewjrowell.framework.gl.GLGraphics;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.input.Input.TouchEvent;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;

/**
 * <p> Third help screen, player gets here from the {@link Help2Screen}.</p>
 * 
 * <p> Displays a rotten moving down towards a stationary demo fly, and
 * then disappearing once it goes underneath the fly (to represent the
 * fly eating it). Then, the fly grows, to show that eating the rottens
 * is a good thing.</p>
 * 
 * <p> Player can move on to the main menu ({@link MainScreen}) by
 * clicking on an arrow button at the bottom of the screen.</p>
 * 
 * @author Andrew Rowell
 * @version 1.0
 */

public class Help3Screen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	BigFont bigfont;
	CoordinateConverter cc;
	
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds, textBounds2;
	
	// How much we need to shift the background to give the
	// appearance of constantly scrolling grass
	float offset;
	
	// Y value of the rotten's position
	float food_y;
	
	// Counter to reset the rotten to original position and
	// show the fly-eating-rotten animation all over again
	// ---
	// Is a float because resetcountdown's unit is seconds
	float resetcountdown;
			
	public Help3Screen(Game game, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		bigfont = new BigFont(WORLD_WIDTH, WORLD_HEIGHT);
		cc = new CoordinateConverter(WORLD_WIDTH, WORLD_HEIGHT);
		
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(cc.xcon(96),
				0, cc.xcon(128),
				cc.ycon(64));
		textBounds = new Rectangle(cc.xcon(88),
				cc.ycon(384),
				cc.xcon(144),
				cc.ycon(64));
		textBounds2 = new Rectangle(cc.xcon(64),
				cc.ycon(96),
				cc.xcon(192),
				cc.ycon(64));
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		food_y = 320;
		resetcountdown = 3; // 3 seconds
	}
	
	/** update status of various elements of the Help3Screen
	 * 
	 * @param deltaTime time since last update()
	 */
	@Override
	public void update(float deltaTime) {
		
		// Shift Background
		offset += 32.0 * deltaTime;
		if(offset >= 320){
			offset = 0;
		}
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		// See if player touched the "next screen" button
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			camera.touchToWorld(touchPos.set(event.x, event.y));
						
			if(event.type == TouchEvent.TOUCH_UP){
				if(OverlapTester.pointInRectangle(nextBounds, touchPos)){
					MainAssets.click.play(1.0f);
					game.setScreen(new MainScreen(game, WORLD_WIDTH, WORLD_HEIGHT));
				}
			}
		}
		
		// Shift the rotten
		food_y -= 32.0 * deltaTime;
		
		// If the rotten has moved down to the fly's position
		// Don't move it any further, and subtract from the
		// countdown timer.
		if(food_y <= 192){
			food_y = 192;
			resetcountdown -= deltaTime;
		}
		
		// If countdown has reached zero, reset the rotten to
		// its original position and reset the timer
		if(resetcountdown <= 0){
			food_y = 320;
			resetcountdown = 3;
			//TODO use a constant/static variable for resetting
		}
	}
	
	
	/** 
	 * <p>Render various elements of Help3Screen</p>
	 * 
	 * @param deltaTime time since last present()
	 */
	@Override
	public void present(float deltaTime) {
		GL10 gl = glGraphics.getGL();
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.setViewportAndMatrices();
		
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		batcher.beginBatch(MainAssets.imagemap);
		
		// Draw the background
		for(int j = 0; j < 4; j++){
			batcher.drawLLSprite(0, (int) cc.ycon((j * 320.0f - offset)),
			(int)WORLD_WIDTH, cc.ycon(320), MainAssets.background);
		}
		
		batcher.drawLLSprite((int) nextBounds.lowerLeft.x,(int) nextBounds.lowerLeft.y,
				(int)nextBounds.width,(int) nextBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) textBounds.lowerLeft.x,(int) textBounds.lowerLeft.y,
				(int)textBounds.width,(int) textBounds.height, MainAssets.white);
		
		bigfont.drawString(cc.xcon(88),
				cc.ycon(384), "EAT", batcher);
		
		// Don't draw the rotten if it has reached the fly
		if(food_y > (192.0 + 32.0)){
			batcher.drawSprite(cc.xcon(160),
					cc.ycon(food_y),
					cc.xcon(32),
					cc.ycon(32),
					MainAssets.rotten1);
			batcher.drawSprite(cc.xcon(160),
					cc.ycon(192),
					cc.xcon(32),
					cc.ycon(32),
					MainAssets.fly);
		} else {
			batcher.drawLLSprite((int) textBounds2.lowerLeft.x,(int) textBounds2.lowerLeft.y,
					(int)textBounds2.width,(int) textBounds2.height, MainAssets.white);
			bigfont.drawString(cc.xcon(64),
					cc.ycon(96), "GROW", batcher);
			batcher.drawSprite(cc.xcon(160),
					cc.ycon(192),
					(int) cc.xcon((32.0 + (32.0 * (3.0 - resetcountdown) / 3.0))),
					(int) cc.ycon((32.0 + (32.0 * (3.0 - resetcountdown) / 3.0))),
					MainAssets.fly);
		}
		
		batcher.drawLLSprite(cc.xcon(96),
				0, cc.xcon(128),
				cc.ycon(64), MainAssets.arrow);
		
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	@Override
	public void pause() {}
	@Override
	public void resume() {
		glGraphics.getGL().glClearColor(1,1,1,1);
	}
	@Override
	public void dispose() {}
}
