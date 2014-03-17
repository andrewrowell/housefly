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
 * <p> First help screen, player gets here from the {@link MainScreen}.</p>
 * 
 * <p> Displays a demonstration fly, and a phone. The image of a phone is
 * tilted to the right and the demonstration fly (which the player cannot control)
 * is moved to the right to show the player that there is a relationship between the
 * physical orientation of the phone and the direction the fly travels. Once
 * the fly gets a certain distance to the right, the phone image is rotated
 * to the left and the demonstration fly begins to move to the left.</p>
 * 
 * <p> Player can move on to the next help screen ({@link Help2Screen}) by
 * clicking on an arrow button at the bottom of the screen.</p>
 * 
 * @author Andrew Rowell
 * @version 1.0
 */


public class Help1Screen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	BigFont bigfont;
	CoordinateConverter cc;
	
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds;
	
	// How much we need to shift the background to give the
	// appearance of constantly scrolling grass
	float offset;
	
	// If true, the demonstration fly is moving right and the phone that is
	// being drawn on the screen is also being tilted to the right
	boolean tiltright = true;
	
	float fly_x; // X position of the demonstrationnstration fly
			
	/**
	 * 
	 * @param game {@link Game} object passed from {@link MainScreen}
	 */
	public Help1Screen(Game game, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		bigfont = new BigFont(WORLD_WIDTH, WORLD_HEIGHT);
		cc = new CoordinateConverter(worldwidth, worldheight);
		
		glGraphics = ((GLGame)game).getGLGraphics();
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(cc.xcon(96), 0, cc.xcon(128),cc.ycon(64));
		textBounds = new Rectangle(cc.xcon(64),cc.ycon(384),cc.xcon(192),cc.ycon(64));
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		fly_x = 320 / 2; // Place demonstration fly in screen's center
	}
	
	/** update status of various elements of the Help1Screen
	 * 
	 * @param deltaTime time since last update()
	 */
	public void update(float deltaTime) {
		
		// Shift background
		offset += 32.0 * deltaTime;
		if(offset >= 320){
			offset = 0;
		}
		
		// Change the direction of the demonstration fly
		// if it has reached the edge of the screen
		if(fly_x >= 320 && tiltright == true){
			fly_x = 320 - 1;
			tiltright = false;
			MainAssets.tiltleft.play(1.0f);
		}
		if(fly_x <= 0 && tiltright == false){
			fly_x = 1;
			tiltright = true;
			MainAssets.tiltright.play(1.0f);
		}
		
		// Move the fly based on the direction
		if(tiltright){
			fly_x += 64.0 * deltaTime;
		} else {
			fly_x -= 64.0 * deltaTime;
		}
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			camera.touchToWorld(touchPos.set(event.x, event.y));
						
			if(event.type == TouchEvent.TOUCH_UP){
				
				// See if player touched the "next screen" button
				if(OverlapTester.pointInRectangle(nextBounds, touchPos)){
					MainAssets.click.play(1.0f);
					game.setScreen(new Help2Screen(game, WORLD_WIDTH, WORLD_HEIGHT));
				}
			}
		}
	}
	
	
	/** 
	 * <p>Render various elements of Help1Screen</p>
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
			batcher.drawLLSprite(0, (int) cc.ycon(j * 320.0f - offset),
					(int) WORLD_WIDTH, cc.ycon(320), MainAssets.background);
		}
		
		// Draw text backgrounds
		batcher.drawLLSprite((int) nextBounds.lowerLeft.x,(int) nextBounds.lowerLeft.y,
				(int)nextBounds.width,(int) nextBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) textBounds.lowerLeft.x,(int) textBounds.lowerLeft.y,
				(int)textBounds.width,(int) textBounds.height, MainAssets.white);
		
		// Draw "TILT"
		bigfont.drawString(cc.xcon(64),cc.ycon(384), "TILT", batcher);
		
		// Draw next screen arrow
		batcher.drawLLSprite(cc.xcon(96),0,cc.xcon(128), cc.ycon(64), MainAssets.arrow);
		
		// Draw tilted phone
		/*if(tiltright == true){
			//batcher.drawSprite(cc.xcon(160), cc.ycon(256),
			//		cc.xcon(128), cc.ycon(192), 350, MainAssets.phone);
			batcher.drawSprite(cc.xcon(160), cc.ycon(256),
					cc.xcon(128), cc.ycon(192), 360 - (10 * (fly_x / 320)), MainAssets.phone);
		} else {
			//batcher.drawSprite(cc.xcon(160), cc.ycon(256),
			//		cc.xcon(128), cc.ycon(192), 10, MainAssets.phone);
			batcher.drawSprite(cc.xcon(160), cc.ycon(256),
					cc.xcon(128), cc.ycon(192), 10 - (10 * (fly_x) / 320), MainAssets.phone);
		}*/
		
		batcher.drawSprite(cc.xcon(160), cc.ycon(256),
				cc.xcon(128), cc.ycon(192), 20 * ((160 - fly_x)/160), MainAssets.phone);
		
		batcher.drawSprite(cc.xcon(fly_x), cc.ycon(96), cc.xcon(64), cc.ycon(64), MainAssets.fly);
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
