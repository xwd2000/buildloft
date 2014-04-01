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
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

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
	private float mCraneWidth=200L;
	private float mCraneHeight=20L;
	
	private AbstractGameSprite mHoldObj;
	private float speed=100F;
	private Direction mDirection=Direction.RIGHT;
	private boolean running=false;
	private ZoomCamera mCamera;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public Crane(Context context, PhysicsWorld pPhysicsWorld,
			TiledTextureRegion pTextureRegion,AbstractGameSprite holdObj,ZoomCamera camera) {
		super(context, pPhysicsWorld, pTextureRegion);
		mHoldObj=holdObj;
		mCamera=camera;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================


	@Override
	protected Body createPhysicsBody(BodyType bodyType,AnimatedSprite sprite) {
		return PhysicsFactory.createBoxBody(this.mPhysicsWorld, sprite, bodyType, FIXTURE_DEF);
	}

	@Override
	protected AnimatedSprite createAnimatedSprite(float pX, float pY, Engine engine) {
		AnimatedSprite  sprite = new AnimatedSprite(pX-mCraneWidth/2, pY-mCraneHeight/2,mCraneWidth,mCraneHeight, this.mTextureRegion, engine.getVertexBufferObjectManager());
		return sprite;
	}
	
	
	
	// ===========================================================
	// other Methods
	// ===========================================================
	
	
	
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
