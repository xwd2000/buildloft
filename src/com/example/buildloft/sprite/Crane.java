package com.example.buildloft.sprite;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.adt.Direction;

public class Crane extends AnimatedSprite{
	private AnimatedSprite board;
	private float speed=100F;
	private Direction mDirection;
	private final PhysicsHandler mPhysicsHandler;
	private boolean running=false;
	private Context mContext;
	private ZoomCamera camera;
	// ===========================================================
	// Constructors
	// ===========================================================
	public Crane(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion pTiledTextureRegion,VertexBufferObjectManager vertexBufferObjectManager
			,Context context) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion,vertexBufferObjectManager
				);
		mDirection=Direction.RIGHT;
		this.mContext=context;
		 mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.mPhysicsHandler.setVelocity(speed, 0);
		camera=(ZoomCamera) ((BaseGameActivity)mContext).getEngine().getCamera();
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public AnimatedSprite getBoard() {
		return board;
	}


	public void setBoard(AnimatedSprite board) {
		this.board = board;
	}
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}



	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if(running==true) {
			if(this.mPhysicsHandler.getVelocityX()==0)
			{
				switch(mDirection){
				case LEFT:
					this.mPhysicsHandler.setVelocityX(-speed);
				case RIGHT:
					this.mPhysicsHandler.setVelocityX(speed);
					
				}
			}
			if(this.mX <0&&mDirection==Direction.LEFT){
				this.mPhysicsHandler.setVelocityX(speed);
				mDirection=Direction.RIGHT;
			} else if((this.mX+this.getWidth()  > camera.getWidthRaw())&&mDirection==Direction.RIGHT) {
				this.mPhysicsHandler.setVelocityX(-speed);
				mDirection=Direction.LEFT;
			} else{
				
			}
		}else {
			if(this.mPhysicsHandler.getVelocityX()!=0)
				this.mPhysicsHandler.setVelocityX(0);
		}

		super.onManagedUpdate(pSecondsElapsed);
	}
	
	
	// ===========================================================
	// other Methods
	// ===========================================================


	public void pasteToScene(Scene scene){
		scene.attachChild(this);
	}

	public void startRun(){
		running=true;
	}
	public void stopRun(){
		running=false;
	}

}
