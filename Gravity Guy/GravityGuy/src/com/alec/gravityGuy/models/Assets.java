package com.alec.gravityGuy.models;

import com.alec.gravityGuy.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;


public class Assets implements Disposable, AssetErrorListener {

	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();	// singleton
	private AssetManager assetManager;
	
	private Assets() {}
	
	public AssetFonts fonts;
	

	public AssetLevel level;
	public AssetPlayer player;
	public AssetGoldCoin goldCoin;
	public AssetEnemies enemies;	
	public AssetWeapons weapons;
	public AssetUI ui;
	
	public void init( AssetManager assetManager) {

		// establish the asset manager
		this.assetManager = assetManager;		
		assetManager.setErrorListener(this);		
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
		assetManager.finishLoading();
		
		// log all the assets there were loaded
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String asset : assetManager.getAssetNames()) {
			Gdx.app.debug(TAG, "asset: " + asset);
		}
		
		// load the texture atlas
		TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
		
		// enable texture filtering
		for (Texture texture : atlas.getTextures()) {
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		// create the game resources (inner Asset~ classes)
		fonts = new AssetFonts();
		level = new AssetLevel(atlas);
		player = new AssetPlayer(atlas);
		enemies = new AssetEnemies(atlas);
		goldCoin = new AssetGoldCoin(atlas);
		weapons = new AssetWeapons(atlas);
		ui = new AssetUI(atlas);
	}
		
	public class AssetUI {
		public AtlasRegion healthGaugeBorder, healthGaugeInfill, laserGaugeInfill;
		public AssetUI (TextureAtlas atlas) {
			healthGaugeBorder = atlas.findRegion("ui/healthGaugeBorder");
			healthGaugeInfill= atlas.findRegion("ui/healthGaugeInfill");
			laserGaugeInfill = atlas.findRegion("ui/laserGaugeInfill");			
		}
	}
	
	public class AssetWeapons {
		public final AtlasRegion laserBlue;
		public AssetWeapons(TextureAtlas atlas) {
			laserBlue = atlas.findRegion("weapons/laserBlue");
		}
	}
	
	public class AssetLevel {
		public final AtlasRegion wall, background, backgroundFloor;
		public AssetLevel (TextureAtlas atlas) {
			wall = atlas.findRegion("level/wall");
			background = atlas.findRegion("level/itsFullOfStars");
			backgroundFloor = atlas.findRegion("level/background_floor");
		}
	}
	
	public class AssetPlayer {
		public final AtlasRegion still;
		public final AtlasRegion jumping;
		public final Animation running;
		public AssetPlayer (TextureAtlas atlas) {
			still = atlas.findRegion("player/playerStanding");
			jumping = atlas.findRegion("player/playerJumping");
			
			Array<AtlasRegion> regions = null;
			regions = atlas.findRegions("player/playerRunning");
			running = new Animation(1.0f / 8.0f, regions,
					Animation.LOOP);
			
			//running = Gdx.audio.newSound(Gdx.files.internal("sounds/run.wav"));
			//jump = Gdx.audio.newSound(Gdx.files.internal("sounds/jump.wav"));
			//stand = Gdx.audio.newSound(Gdx.files.internal("sounds/stand.wav"));
		}
	}
	
	public class AssetEnemies {
		public final AtlasRegion lgm1Standing;
		public final Animation lgmWalking;
		public AssetEnemies (TextureAtlas atlas) {
			Array<AtlasRegion> regions = null;
			regions = atlas.findRegions("enemies/lgm1");
			lgm1Standing = regions.first();
			lgmWalking = new Animation(1.0f / 3.0f, regions,
					Animation.LOOP);
		}
	}
	
	public class AssetGoldCoin {
		public final AtlasRegion goldCoin;
		public AssetGoldCoin(TextureAtlas atlas) {
			goldCoin = atlas.findRegion("item_gold_coin");
		}
	}
	
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts () {
			defaultSmall = new BitmapFont(Gdx.files.internal("fonts/white16.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("fonts/white16.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("fonts/white16.fnt"), true);
			
			defaultSmall.setScale(.1f);
			defaultNormal.setScale(1.0f);
			defaultBig.setScale(2.0f);
			
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset: '" + asset.fileName + "' " + (Exception)throwable);
	}

	@Override
	public void dispose() {
		assetManager.dispose();
	}
	
}
