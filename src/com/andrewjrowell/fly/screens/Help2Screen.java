package com.andrewjrowell.fly.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
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
 * <p> Second help screen, player gets here from the {@link Help1Screen}.</p>
 * 
 * <p> Displays a horizontally scrolling list of predators that the player
 * must avoid.</p>
 * 
 * <p> Player can move on to the next help screen ({@link Help2Screen}) by
 * clicking on an arrow button at the bottom of the screen.</p>
 * 
 * @author Andrew Rowell
 * @version 1.0
 */


public class Help2Screen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	final static int TEXTX = 48; // Width of the bitmap font
	final static int TEXTY = 64; // Height of the bitmap font
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds;
	
	// How much we need to shift the background to give the
	// appearance of constantly scrolling grass
	float offset;
	
	float spider_x, lizard_x, duck_x; // X positions of predators being displayed
			
	public Help2Screen(Game game, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(96, 0, 128, 64);
		textBounds = new Rectangle(40, 384, 240, 64);
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		
		// This next section makes the creatures scroll in from the right side,
		// one at a time
		spider_x = WORLD_WIDTH; // Put spider on right edge of world
		lizard_x = spider_x + WORLD_WIDTH / 2; // Put lizard a bit further to
											   // the right off screen
		//TODO rename lizard to bat
		duck_x = spider_x + WORLD_WIDTH; // Put duck even further to the right
	}
	
	/** update status of various elements of the Help2Screen
	 * 
	 * @param deltaTime time since last update()
	 */
	@Override
	public void update(float deltaTime) {
		
		// Shift Background
		offset += 32 * deltaTime;
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
					game.setScreen(new Help3Screen(game,WORLD_WIDTH, WORLD_HEIGHT));
				}
			}
		}
		
		// Shift predators
		spider_x -= 64 * deltaTime;
		lizard_x -= 64 * deltaTime;
		duck_x -= 64 * deltaTime;
		
		// If a predator has gone off of the screen,
		// put them on the other side
		if(spider_x < -96){
			spider_x = WORLD_WIDTH + 96;
		}
		if(lizard_x < -96){
			lizard_x = WORLD_WIDTH + 96;
		}
		if(duck_x < -96){
			duck_x = WORLD_WIDTH + 96;
		}
	}
	
	/** 
	 * <p>Render various elements of Help2Screen</p>
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
			batcher.drawLLSprite(0, (int) (j * 320.0f - offset),320,320, MainAssets.background);
		}
		
		batcher.drawLLSprite((int) nextBounds.lowerLeft.x,(int) nextBounds.lowerLeft.y,
				(int)nextBounds.width,(int) nextBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) textBounds.lowerLeft.x,(int) textBounds.lowerLeft.y,
				(int)textBounds.width,(int) textBounds.height, MainAssets.white);
		
		batcher.drawLLSprite(40, 384, TEXTX, TEXTY, MainAssets.A);
		batcher.drawLLSprite(40 + TEXTX, 384, TEXTX, TEXTY, MainAssets.V);
		batcher.drawLLSprite(40 + 2 * TEXTX, 384, TEXTX, TEXTY, MainAssets.O);
		batcher.drawLLSprite(40 + 3 * TEXTX, 384, TEXTX, TEXTY, MainAssets.I);
		batcher.drawLLSprite(40 + 4 * TEXTX, 384, TEXTX, TEXTY, MainAssets.D);
		
		batcher.drawSprite(spider_x, 224, 64, 64, MainAssets.spider);
		batcher.drawSprite(lizard_x, 224, 128, 128, MainAssets.lizard);
		batcher.drawSprite(duck_x, 224, 192, 192, MainAssets.duck);
		
		batcher.drawLLSprite(96, 0, 128, 64, MainAssets.arrow);
		
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
