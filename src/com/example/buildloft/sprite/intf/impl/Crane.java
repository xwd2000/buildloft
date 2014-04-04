package com.example.buildloft.sprite.intf.impl;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.adt.Direction;
import com.example.buildloft.sprite.intf.AbstractGameSprite;

public class Crane extends AbstractGameSprite{
	

	// ===========================================================
	// Constants
	// ===========================================================
	private static TiledTextureRegion tiledTextureRegion;
	private float mCraneWidth=200L;
	private float mCraneHeight=20L;
	private float speed=100F;
	private Direction mDirection=Direction.RIGHT;
	private boolean running=false;
	private ZoomCamera mCamera;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Crane(Context context, PhysicsWorld pPhysicsWorld,ZoomCamera camera) {
		super(context, pPhysicsWorld);
		mCamera=camera;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	@Override
	protected Body createPhysicsBody(BodyType bodyType,AnimatedSprite sprite) {
		return PhysicsFactory.createBoxBody(this.physicsWorld, sprite, bodyType, FIXTURE_DEF);
	}

	@Override
	protected AnimatedSprite createAnimatedSprite(float pX, float pY, Engine engine) {
		if(tiledTextureRegion==null)
			loadResource(context, ((SimpleBaseGameActivity)context).getTextureManager());
		AnimatedSprite  sprite = new AnimatedSprite(pX, pY,mCraneWidth,mCraneHeight, tiledTextureRegion, engine.getVertexBufferObjectManager());
		return sprite;
	}
	
	
	
	// ===========================================================
	// other Methods
	// ===========================================================
	public static TiledTextureRegion loadResource(Context context,TextureManager textManager){
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(textManager, 64, 128, TextureOptions.BILINEAR);
		tiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, context, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		bitmapTextureAtlas.load();
		return tiledTextureRegion;
	}
	
	
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	
	public float getSpeed() {
		return speed;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public void startRun(){
		running=true;
	}
	
	public void stopRun(){
		running=false;
	}

	public float getCraneWidth() {
		return mCraneWidth;
	}

	public void setCraneWidth(float mCraneWidth) {
		this.mCraneWidth = mCraneWidth;
	}

	public float getCraneHeight() {
		return mCraneHeight;
	}

	public void setCraneHeight(float mCraneHeight) {
		this.mCraneHeight = mCraneHeight;
	}

}
