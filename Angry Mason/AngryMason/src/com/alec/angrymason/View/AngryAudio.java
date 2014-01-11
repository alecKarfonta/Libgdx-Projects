package com.alec.angrymason.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AngryAudio {
	private AngryAudio() {}
	
	public static Music song = Gdx.audio.newMusic(Gdx.files.internal("data/determination.mp3"));
	public static Sound shoot = Gdx.audio.newSound(Gdx.files.internal("data/shoot.wav"));
	public static Sound explosion = Gdx.audio.newSound(Gdx.files.internal("data/boom.wav"));
	
	public static void playMusic(boolean looping) {
		song.setLooping(looping);
		song.play();
	}
	
	public static void shoot() {
		shoot.play();
	}
	
	public static void explode() {
		explosion.play();
	}
	
	public static void stopMusic() {
		song.stop();
	}
	
	public static void pauseMusic() {
		song.pause();
	}
	
	public static void dispose() {
		song.dispose();
		shoot.dispose();
		explosion.dispose();
	}
}
