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
 * <p>{@link Screen} that the player enters the high scores
 * in, player gets here from the {@link GamePlayScreen}</p>
 * 
 * <p>Like a classic arcade high score screen, lets the player
 * enter three letters to be stored in a list of high scores</p> 
 * 
 * @author Andrew Rowell
 * @version 1.0
 */


public class EnterHighScoreScreen extends Screen{
	final float WORLD_WIDTH;
	final float WORLD_HEIGHT;
	BigFont bigfont;
	CoordinateConverter cc;
	
	GLGraphics glGraphics;

	Vector2 touchPos = new Vector2(); // Stores the position last touched
	Camera2D camera;
	SpriteBatcher batcher;
	Rectangle nextBounds, textBounds, nameBounds, scoreBounds, uparrow1bounds,
		uparrow2bounds, uparrow3bounds, downarrow1bounds, downarrow2bounds,
		downarrow3bounds;
	
	
	// How much we need to shift the background to give the appearance
	// of constantly scrolling grass
	float offset; 
		
	// Score earned in the game that was just finished
	int score;
			
	// Name that the player is entering
	String name;
	
	/**
	 * 
	 * @param game {@link Game} object passed from previous screen 
	 * @param score Score earned by player in GamePlayScreen
	 */
	public EnterHighScoreScreen(Game game, int score, float worldwidth, float worldheight) {
		super(game);
		WORLD_WIDTH = worldwidth;
		WORLD_HEIGHT = worldheight;
		this.score = score;
		glGraphics = ((GLGame)game).getGLGraphics();
		cc = new CoordinateConverter(worldwidth, worldheight);
		bigfont = new BigFont(WORLD_WIDTH, WORLD_HEIGHT);
		
		camera = new Camera2D(glGraphics, WORLD_WIDTH, WORLD_HEIGHT);
		batcher = new SpriteBatcher(glGraphics, 500);
		nextBounds = new Rectangle(cc.xcon(96), 0, cc.xcon(128), cc.ycon(64));
		textBounds = new Rectangle(cc.xcon(16), cc.ycon(320), cc.xcon(288), cc.ycon(128));
		nameBounds = new Rectangle(cc.xcon(92), cc.ycon(192), cc.xcon(144), cc.ycon(128));
		scoreBounds = new Rectangle(cc.xcon(40), cc.ycon(96), cc.xcon(240), cc.ycon(64));
		
		uparrow1bounds = new Rectangle(cc.xcon(96), cc.ycon(288), cc.xcon(32), cc.ycon(32));
		uparrow2bounds = new Rectangle(cc.xcon(144), cc.ycon(288), cc.xcon(32), cc.ycon(32));
		uparrow3bounds = new Rectangle(cc.xcon(192), cc.ycon(288), cc.xcon(32), cc.ycon(32));
		downarrow1bounds = new Rectangle(cc.xcon(96), cc.ycon(192), cc.xcon(32), cc.ycon(32));
		downarrow2bounds = new Rectangle(cc.xcon(144), cc.ycon(192), cc.xcon(32), cc.ycon(32));
		downarrow3bounds = new Rectangle(cc.xcon(192), cc.ycon(192), cc.xcon(32), cc.ycon(32));
		
		glGraphics.getGL().glClearColor(1,1,1,1);
		offset = 0;
		name = "AAA";
		
		// Psychological reward for player getting high score
		MainAssets.majorchord.play(1.0f);
	}
	
	/** update status of various elements of the EnterHighScoreScreen 
	 * 
	 * @param deltaTime time since last update()
	 */
	public void update(float deltaTime) {
		offset += cc.ycon(32) * deltaTime;
		if(offset >= cc.ycon(320)){
			offset = 0;
		}
		
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		game.getInput().getKeyEvents();
		
		int len = touchEvents.size();
		for(int i = 0; i < len; i++){
			TouchEvent event = touchEvents.get(i);
			
			camera.touchToWorld(touchPos.set(event.x, event.y));
						
			if(event.type == TouchEvent.TOUCH_UP){
				if(OverlapTester.pointInRectangle(uparrow1bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[0] = upChar(namechars[0]);
					name = String.valueOf(namechars);
					MainAssets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(uparrow2bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[1] = upChar(namechars[1]);
					name = String.valueOf(namechars);
					MainAssets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(uparrow3bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[2] = upChar(namechars[2]);
					name = String.valueOf(namechars);
					MainAssets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(downarrow1bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[0] = downChar(namechars[0]);
					name = String.valueOf(namechars);
					MainAssets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(downarrow2bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[1] = downChar(namechars[1]);
					name = String.valueOf(namechars);
					MainAssets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(downarrow3bounds, touchPos)){
					char[] namechars = name.toCharArray();
					namechars[2] = downChar(namechars[2]);
					name = String.valueOf(namechars);
					MainAssets.click.play(1.0f);
				}
				if(OverlapTester.pointInRectangle(nextBounds, touchPos)){
					MainAssets.click.play(1.0f);
					HighScores.addHighScore(score, name);
					HighScores.save(game.getFileIO());
					game.setScreen(new ViewHighScoreScreen(game, WORLD_WIDTH, WORLD_HEIGHT));
				}
			}
		}
	}
	
	
	/**
	 * <p>Render various elements of EnterHighScoreScreen</p>
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
		
		//Draw the background
		for(int j = 0; j < 4; j++){
			batcher.drawLLSprite(0, (int) cc.ycon(j * 320.0f - offset),cc.xcon(320),cc.ycon(320), MainAssets.background);
		}
		
		batcher.drawLLSprite((int) nextBounds.lowerLeft.x,(int) nextBounds.lowerLeft.y,
				(int)nextBounds.width,(int) nextBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) textBounds.lowerLeft.x,(int) textBounds.lowerLeft.y,
				(int)textBounds.width,(int) textBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) nameBounds.lowerLeft.x,(int) nameBounds.lowerLeft.y,
				(int)nameBounds.width,(int) nameBounds.height, MainAssets.white);
		batcher.drawLLSprite((int) scoreBounds.lowerLeft.x,(int) scoreBounds.lowerLeft.y,
				(int)scoreBounds.width,(int) scoreBounds.height, MainAssets.white);
		
		
		bigfont.drawString(cc.xcon(64),cc.ycon(384), "HIGH", batcher);
		
		bigfont.drawString(cc.xcon(16),cc.ycon(320), "SCORE!", batcher);
		
		batcher.drawLLSprite(cc.xcon(96), cc.ycon(288), cc.xcon(32), cc.ycon(32), MainAssets.uparrow);
		batcher.drawLLSprite(cc.xcon(144), cc.ycon(288), cc.xcon(32), cc.ycon(32), MainAssets.uparrow);
		batcher.drawLLSprite(cc.xcon(192), cc.ycon(288), cc.xcon(32), cc.ycon(32), MainAssets.uparrow);
		batcher.drawLLSprite(cc.xcon(96), cc.ycon(192), cc.xcon(32), cc.ycon(32), MainAssets.downarrow);
		batcher.drawLLSprite(cc.xcon(144), cc.ycon(192), cc.xcon(32), cc.ycon(32), MainAssets.downarrow);
		batcher.drawLLSprite(cc.xcon(192), cc.ycon(192), cc.xcon(32), cc.ycon(32), MainAssets.downarrow);

		
		drawScore(score, cc.xcon(40), cc.ycon(96));
		drawName(cc.xcon(92), cc.ycon(224), name);
		
		batcher.drawLLSprite(cc.xcon(96), cc.ycon(0), cc.xcon(128), cc.ycon(64), MainAssets.arrow);
		
		batcher.endBatch();
		gl.glDisable(GL10.GL_BLEND);
	}
	
	
	/** <p> Called by present() to draw the score earned in the
	 * game the player just finished</p>
	 * 
	 * @param score score to be rendered
	 * @param x x position of score
	 * @param y y position of score
	 */
	private void drawScore(int score, int x, int y){
		int tscore = score;
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
		drawDigit(x + cc.xcon(48) * 1,y,digit2);
		drawDigit(x + cc.xcon(48) * 2,y,digit3);
		drawDigit(x + cc.xcon(48) * 3,y,digit4);
		drawDigit(x + cc.xcon(48) * 4,y,digit5);
	}
	
	/**	<p>Draws individual digits for drawScore()</p> 
	 * 
	 * @param x x position to draw digit
	 * @param y y position to draw digit
	 * @param digit single digit number to be drawn
	 */
	private void drawDigit(int x, int y, int digit){
		switch(digit){
		case 1: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.ONE); break;
		case 2: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.TWO); break;
		case 3: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.THREE); break;
		case 4: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.FOUR); break;
		case 5: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.FIVE); break;
		case 6: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.SIX); break;
		case 7: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.SEVEN); break;
		case 8: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.EIGHT); break;
		case 9: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.NINE); break;
		case 0: batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.ZERO); break;
		}
	}

	/** <p>Called by present() to draw player's initials</p>
	 * 
	 * @param x x position of name
	 * @param y y position of name
	 * @param name initials to be drawn. Expects 3 characters.
	 */
	private void drawName(int x, int y, String name){
		drawChar(x, y, name.charAt(0));
		drawChar(x + cc.xcon(48), y, name.charAt(1));
		drawChar(x + cc.xcon(96), y, name.charAt(2));
	}
	
	/** <p>Called by drawName() to draw individual characters
	 * 
	 * @param x x position of character
	 * @param y y position of character
	 * @param c character
	 */
	private void drawChar(int x, int y, char c){
		switch(c){
		case 'A': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.A); break;
		case 'B': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.B); break;
		case 'C': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.C); break;
		case 'D': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.D); break;
		case 'E': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.E); break;
		case 'F': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.F); break;
		case 'G': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.G); break;
		case 'H': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.H); break;
		case 'I': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.I); break;
		case 'J': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.J); break;
		case 'K': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.K); break;
		case 'L': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.L); break;
		case 'M': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.M); break;
		case 'N': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.N); break;
		case 'O': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.O); break;
		case 'P': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.P); break;
		case 'Q': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.Q); break;
		case 'R': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.R); break;
		case 'S': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.S); break;
		case 'T': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.T); break;
		case 'U': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.U); break;
		case 'V': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.V); break;
		case 'W': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.W); break;
		case 'X': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.X); break;
		case 'Y': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.Y); break;
		case 'Z': batcher.drawLLSprite(x, y, cc.xcon(48), cc.ycon(64), MainAssets.Z); break;
		}
	}
	
	/** <p>Finds the character before the one given.</p>
	 *   
	 * <p>Used when player presses up arrow above a letter in the name input.</p>
	 * 
	 * @param c character to be shifted
	 * @return character before the one inputted
	 */
	private char upChar(char c){
		switch(c){
		case 'A': return 'Z';
		case 'B': return 'A';
		case 'C': return 'B';
		case 'D': return 'C';
		case 'E': return 'D';
		case 'F': return 'E';
		case 'G': return 'F';
		case 'H': return 'G';
		case 'I': return 'H';
		case 'J': return 'I';
		case 'K': return 'J';
		case 'L': return 'K';
		case 'M': return 'L';
		case 'N': return 'M';
		case 'O': return 'N';
		case 'P': return 'O';
		case 'Q': return 'P';
		case 'R': return 'Q';
		case 'S': return 'R';
		case 'T': return 'S';
		case 'U': return 'T';
		case 'V': return 'U';
		case 'W': return 'V';
		case 'X': return 'W';
		case 'Y': return 'X';
		case 'Z': return 'Y';
		}
		return 'A';
	}
	
	/** <p>Finds the character after the one given.</p>
	 *   
	 * <p>Used when player presses down arrow below a letter in the name input.</p>
	 * 
	 * @param c character to be shifted
	 * @return character after the one inputted
	 */
	private char downChar(char c){
		switch(c){
		case 'A': return 'B';
		case 'B': return 'C';
		case 'C': return 'D';
		case 'D': return 'E';
		case 'E': return 'F';
		case 'F': return 'G';
		case 'G': return 'H';
		case 'H': return 'I';
		case 'I': return 'J';
		case 'J': return 'K';
		case 'K': return 'L';
		case 'L': return 'M';
		case 'M': return 'N';
		case 'N': return 'O';
		case 'O': return 'P';
		case 'P': return 'Q';
		case 'Q': return 'R';
		case 'R': return 'S';
		case 'S': return 'T';
		case 'T': return 'U';
		case 'U': return 'V';
		case 'V': return 'W';
		case 'W': return 'X';
		case 'X': return 'Y';
		case 'Y': return 'Z';
		case 'Z': return 'A';
		}
		return 'Z';
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
