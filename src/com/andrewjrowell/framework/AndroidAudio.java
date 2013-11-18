package com.andrewjrowell.framework;

import java.io.IOException;

import com.andrewjrowell.framework.interfaces.Audio;
import com.andrewjrowell.framework.interfaces.Music;
import com.andrewjrowell.framework.interfaces.Sound;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class AndroidAudio implements Audio {

	AssetManager assets;
	SoundPool soundPool;
	
	public AndroidAudio(Activity activity){
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		this.assets = activity.getAssets();
		this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
	}
	
	@Override
	public Music newMusic(String fileName) {
		try{
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			return new AndroidMusic(assetDescriptor);
		} catch (IOException e){
			throw new RuntimeException("Couldn't load music'" + fileName + "'");
		}
	}

	@Override
	public Sound newSound(String fileName) {
		try{
			AssetFileDescriptor assetDescriptor = assets.openFd(fileName);
			int soundId = soundPool.load(assetDescriptor, 0);
			return new AndroidSound(soundPool, soundId);
		} catch (IOException e){
			throw new RuntimeException("Couldn't load sound'" + fileName + "'");
		}
	}

}
