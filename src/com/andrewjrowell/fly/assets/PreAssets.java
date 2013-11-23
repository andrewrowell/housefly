package com.andrewjrowell.fly.assets;

import com.andrewjrowell.framework.audio.Audio;
import com.andrewjrowell.framework.gl.GLGame;
import com.andrewjrowell.framework.gl.Texture;
import com.andrewjrowell.framework.gl.TextureRegion;

public class PreAssets {
	public static Audio audio;
	public static Texture imagemap;
	
	public static TextureRegion background;
	
	public static void load(GLGame game){
		audio = game.getAudio();
		imagemap = new Texture(game, "preassets.png");
		
		background = new TextureRegion(imagemap, 0, 0, 320, 480);
	}
	
	public static void reload(){
		imagemap.reload();
	}
	
	public static void reloadAudio(){
	}

	public static void reloadSpeedSound() {
		
	}
}
