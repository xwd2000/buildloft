package com.example.buildloft.sprite.intf.impl;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.PhysicsExample2;
import com.example.buildloft.sprite.intf.AbstractGameSprite;

public class Board extends AbstractGameSprite{
	private float boardWidth=130L;
	private float boardHeight=13L;
	private static TiledTextureRegion textureRegion;
	
	public Board(Context context,PhysicsWorld pPhysicsWorld) {
		super(context,pPhysicsWorld);
	}
	
	public Board(Context context,PhysicsWorld pPhysicsWorld,float pBoardWidth,float pBoardHeight) {
		super(context,pPhysicsWorld);
		this.boardWidth=pBoardWidth;
		this.boardHeight=pBoardHeight;
	}

	private void jumpFace(Vector2 velocity) {
		final Body faceBody = (Body)sprite.getUserData();
		faceBody.setLinearVelocity(velocity);
		Vector2Pool.recycle(velocity);
	}
	
	
	@Override
	protected Body createPhysicsBody(BodyType bodyType,AnimatedSprite sprite) {
		return PhysicsFactory.createBoxBody(this.physicsWorld, sprite, bodyType, FIXTURE_DEF);
	}

	@Override
	protected AnimatedSprite createAnimatedSprite(float pX,float pY,Engine engine) {
		return new AnimatedSprite(pX-boardWidth/2, pY-boardHeight/2,boardWidth,boardHeight, this.textureRegion, engine.getVertexBufferObjectManager()){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				final Vector2 velocity = Vector2Pool.obtain(((PhysicsExample2)Board.this.context).getGravityX() * -50, ((PhysicsExample2)Board.this.context).getGravityX()* -50);
				Board.this.jumpFace(velocity);
				return false;
			}
			
		};
	}

	

//	@Override
//	public TiledTextureRegion loadResource() {
//		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
//		BaseGameActivity baseGameActivity=(BaseGameActivity)mContext;
//		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(baseGameActivity.getTextureManager(), 64, 128, TextureOptions.BILINEAR);
//		bitmapTextureAtlas.load();
//		return BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, mContext, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
//		
//	}

	public float getmBoardWidth() {
		return boardWidth;
	}

	public void setmBoardWidth(float mBoardWidth) {
		this.boardWidth = mBoardWidth;
	}

	public float getmBoardHeight() {
		return boardHeight;
	}

	public void setmBoardHeight(float mBoardHeight) {
		this.boardHeight = mBoardHeight;
	}
	
	// ===========================================================
	// other Methods
	// ===========================================================
	public static TiledTextureRegion loadResource(Context context,TextureManager textManager){
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(textManager, 64, 128, TextureOptions.BILINEAR);
		textureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, context, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		bitmapTextureAtlas.load();
		return textureRegion;
	}
	
	
}
