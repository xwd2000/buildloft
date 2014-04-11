package com.example.buildloft.sprite.intf.impl;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

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
import com.example.buildloft.consts.AppConst;
import com.example.buildloft.sprite.intf.AbstractPhysicsGameSprite;

public class Circle extends AbstractPhysicsGameSprite{
	private float radius=30;
	private static TiledTextureRegion textureRegion;
	
	public Circle(Context context,PhysicsWorld pPhysicsWorld,BodyType bodyType) {
		super(context,pPhysicsWorld,bodyType);
	}
	
	public Circle(Context context,PhysicsWorld pPhysicsWorld) {
		super(context,pPhysicsWorld);
	}
	
	public Circle(Context context,PhysicsWorld pPhysicsWorld,float radius) {
		super(context,pPhysicsWorld);
		this.radius=radius;
	
	}

	public void jumpFace(Vector2 velocity) {
		final Body faceBody = (Body)sprite.getUserData();
		faceBody.setLinearVelocity(velocity);
		Vector2Pool.recycle(velocity);
	}
	
	
	@Override
	protected Body createPhysicsBody(BodyType bodyType,AnimatedSprite sprite) {
		setFixtureDef(PhysicsFactory.createFixtureDef(200f, 0.7f, 0.5f,false,AppConst.CATEGORYBIT_OBJ,AppConst.MASK_OBJ,(short)0));
		return PhysicsFactory.createCircleBody(this.physicsWorld, sprite, bodyType, fixtureDef);
	}

	@Override
	protected AnimatedSprite createAnimatedSprite(float pX,float pY,Engine engine) {
		return new AnimatedSprite(pX-radius, pY-radius,2*radius,2*radius, this.textureRegion, engine.getVertexBufferObjectManager()){
			//如果sprite要出发areaTouch事件，添加areaTouchCallBack，并注册如下代码
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(areaTouchCallBack!=null&&pSceneTouchEvent.isActionDown()){
					return areaTouchCallBack.onAreaTouched();
				}
				else
					return false;
			}
			
		};
	}
	
	// ===========================================================
	// other Methods
	// ===========================================================
	public static TiledTextureRegion loadResource(Context context,TextureManager textManager){
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(textManager, 64, 128, TextureOptions.BILINEAR);
		textureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bitmapTextureAtlas, context, "face_circle_tiled.png", 0, 0, 2, 1); // 64x32
		bitmapTextureAtlas.load();
		return textureRegion;
	}
	
	
}
