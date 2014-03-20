package com.example.buildloft.sprite.impl;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.sprite.AbstractGameSprite;

public class Board extends AbstractGameSprite{
	private float mBoardWidth=130L;
	private float mBoardHeight=13L;
	
	public Board(Context context,PhysicsWorld pPhysicsWorld,TiledTextureRegion pTextureRegion) {
		super(context,pPhysicsWorld,pTextureRegion);
	}
	
	public Board(Context context,PhysicsWorld pPhysicsWorld,float pBoardWidth,float pBoardHeight,TiledTextureRegion pTextureRegion) {
		super(context,pPhysicsWorld,pTextureRegion);
		this.mBoardWidth=pBoardWidth;
		this.mBoardHeight=pBoardHeight;
	}


	
	@Override
	public Body createPhysicsBody() {
		return PhysicsFactory.createBoxBody(this.mPhysicsWorld, sprite, BodyType.DynamicBody, FIXTURE_DEF);
	}

	@Override
	public AnimatedSprite createAnimatedSprite(float pX,float pY,Engine engine) {
		return new AnimatedSprite(pX-mBoardWidth/2, pY-mBoardHeight/2,mBoardWidth,mBoardHeight, this.mTextureRegion, engine.getVertexBufferObjectManager());
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
		return mBoardWidth;
	}

	public void setmBoardWidth(float mBoardWidth) {
		this.mBoardWidth = mBoardWidth;
	}

	public float getmBoardHeight() {
		return mBoardHeight;
	}

	public void setmBoardHeight(float mBoardHeight) {
		this.mBoardHeight = mBoardHeight;
	}

	
	
}
