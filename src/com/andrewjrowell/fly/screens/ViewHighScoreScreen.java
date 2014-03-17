package com.andrewjrowell.fly.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import com.andrewjrowell.fly.assets.MainAssets;
import com.andrewjrowell.fly.bitmapfonts.BigFont;
import com.andrewjrowell.framework.CoordinateConverter;
import com.andrewjrowell.framework.Game;
import com.andrewjrowell.framework.Screen;
import com.andrewjrowell.framework.diskio.HighScores;
import com.andrewjrowell.framework.gl.Camera2D;
import com.andrewjrowell.framework.gl.GLGame;
import com.andrewjrowell.framework.gl.GLGraphics;
import com.andrewjrowell.framework.gl.SpriteBatcher;
import com.andrewjrowell.framework.input.Input.TouchEvent;
import com.andrewjrowell.framework.math.OverlapTester;
import com.andrewjrowell.framework.math.Rectangle;
import com.andrewjrowell.framework.math.Vector2;

/**
 * <p>Displays the high scores to the player.</p>
 * 
 * @author Andrew Rowell
 * @version 1.0
*/

public class ViewHighScoreScreen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	BigFont bigfont;
	CoordinateConverter cc;
	
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds, scoreBounds, nameBounds;
	
	// How much we need to shift the background to give the
	// appearance of constantly scrolling grass
	float offset;
			
	public ViewHighScoreScreen(Game game, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		glGraphics = ((GLGame)game).getGLGraphics();
		bigfont = new BigFont(WORLD_WIDTH, WORLD_HEIGHT);
		cc = new CoordinateConverter(worldwidth, worldheight);
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(cc.xcon(96), 0, cc.xcon(128), cc.ycon(64));
		textBounds = new Rectangle(cc.xcon(16), cc.ycon(320), cc.xcon(288), cc.ycon(128));
		scoreBounds = new Rectangle(cc.xcon(32), cc.ycon(128), cc.xcon(160), cc.ycon(160));
		nameBounds = new Rectangle(cc.xcon(192), cc.ycon(128), cc.xcon(96), cc.ycon(160));
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		HighScores.load(game.getFileIO());
	}
	
	/**
	 * <p>Update status of various elements of the
	 * ViewHighScoreScreen.</p>
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
		
		// See if player touched button to return to MainScreen
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
	}
	
	/**
	 * <p>Render various elements of ViewHighScoreScreen</p>
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
		
		for(int j = 0; j < 4; j++){
			batcher.drawLLSprite(0, (int) cc.ycon(j * 320.0f - offset),
					cc.xcon(320),cc.ycon(320), MainAssets.background);
		}
		
		batcher.drawLLSprite((int) nextBounds.lowerLeft.x,(int) nextBounds.lowerLeft.y,
				(int)nextBounds.width,(int) nextBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) textBounds.lowerLeft.x,(int) textBounds.lowerLeft.y,
				(int)textBounds.width,(int) textBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) scoreBounds.lowerLeft.x,(int) scoreBounds.lowerLeft.y,
				(int)scoreBounds.width,(int) scoreBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) nameBounds.lowerLeft.x,(int) nameBounds.lowerLeft.y,
				(int)nameBounds.width,(int) nameBounds.height, MainAssets.tallred);

		bigfont.drawString(cc.xcon(64),cc.ycon(384), "HIGH", batcher);	
		bigfont.drawString(cc.xcon(16),cc.ycon(320), "SCORES", batcher);	
		
		for(int i = 0; i < 5; i++){
			drawScore(i,cc.xcon(32),cc.ycon(32 * (8-i)));
		}
		
		batcher.drawLLSprite(cc.xcon(96), 0, cc.xcon(128), cc.ycon(64), MainAssets.arrow);
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	
	/**
	 * <p>Renders a high score</p>
	 * 
	 * @param scoreindex which score to draw
	 * @param x X position to draw it at
	 * @param y Y position to draw it at
	 */
	private void drawScore(int scoreindex, int x, int y){
		int tscore = HighScores.scores[scoreindex];
		int digit1 = (int) Math.floor(tscore / 10000);
		tscore -= (digit1 * 10000);
		int digit2 = (int) Math.floor(tscore / 1000);
		tscore -= (digit2 * 1000);
		int digit3 = (int) Math.floor(tscore / 100);
		tscore -= (digit3 * 100);
		int digit4 = (int) Math.floor(tscore / 10);
		tscore -= (digit4 * 10);
		int digit5 = (int) Math.floor(tscore / 1);
		tscore -= (digit5 * 1);
		drawDigit(x,y,digit1);
		drawDigit(x + cc.xcon(32),y,digit2);
		drawDigit(x + cc.xcon(64),y,digit3);
		drawDigit(x + cc.xcon(96),y,digit4);
		drawDigit(x + cc.xcon(128),y,digit5);
		drawName(x + cc.xcon(160),y,HighScores.names[scoreindex]);
	}
	
	/**
	 * <p>Renders individual digits</p>
	 * 
	 * @param x X position of digit
	 * @param y Y position of digit
	 * @param digit digit between 0 and 9
	 */
	private void drawDigit(int x, int y, int digit){
		switch(digit){
		case 1: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.one); break;
		case 2: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.two); break;
		case 3: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.three); break;
		case 4: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.four); break;
		case 5: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.five); break;
		case 6: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.six); break;
		case 7: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.seven); break;
		case 8: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.eight); break;
		case 9: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.nine); break;
		case 0: batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.zero); break;
		}
	}
	
	/**
	 * <p>Renders initials</p>
	 * 
	 * @param x X position to render initials
	 * @param y Y position to render initials
	 * @param name three initials to draw
	 */
	private void drawName(int x, int y, String name){
		drawChar(x, y, name.charAt(0));
		drawChar(x + cc.xcon(32), y, name.charAt(1));
		drawChar(x + cc.xcon(64), y, name.charAt(2));
	}
	
	/**
	 * <p>renders individual characters</p>
	 * @param x X position to render character
	 * @param y Y position to render character
	 * @param c character to render
	 */
	private void drawChar(int x, int y, char c){
		switch(c){
		case 'A': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.a); break;
		case 'B': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.b); break;
		case 'C': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.c); break;
		case 'D': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.d); break;
		case 'E': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.e); break;
		case 'F': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.f); break;
		case 'G': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.g); break;
		case 'H': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.h); break;
		case 'I': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.i); break;
		case 'J': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.j); break;
		case 'K': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.k); break;
		case 'L': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.l); break;
		case 'M': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.m); break;
		case 'N': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.n); break;
		case 'O': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.o); break;
		case 'P': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.p); break;
		case 'Q': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.q); break;
		case 'R': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.r); break;
		case 'S': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.s); break;
		case 'T': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.t); break;
		case 'U': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.u); break;
		case 'V': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.v); break;
		case 'W': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.w); break;
		case 'X': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.x); break;
		case 'Y': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.y); break;
		case 'Z': batcher.drawLLSprite(x, y, cc.xcon(32), cc.ycon(32), MainAssets.z); break;
		}
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
