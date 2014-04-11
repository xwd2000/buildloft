package com.example.buildloft.sprite.intf.impl;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.consts.AppConst;
import com.example.buildloft.sprite.intf.AbstractPhysicsGameSprite;

public class LinkItem extends AbstractPhysicsGameSprite{
	private float width=4f;
	private float height=16f;
	private static TiledTextureRegion textureRegion;
	public LinkItem(Context context, PhysicsWorld pPhysicsWorld) {
		super(context, pPhysicsWorld);
	}
	
	public LinkItem(Context context, PhysicsWorld pPhysicsWorld,BodyType bodyType) {
		super(context, pPhysicsWorld,bodyType);
	}
	
	public LinkItem(Context context, PhysicsWorld pPhysicsWorld,float width,float height) {
		super(context, pPhysicsWorld);
		this.width=width;
		this.height=height;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	@Override
	protected Body createPhysicsBody(BodyType bodyType,AnimatedSprite sprite) {
		setFixtureDef(PhysicsFactory.createFixtureDef(200f, 0.7f, 0.5f,false,AppConst.CATEGORYBIT_LINK,AppConst.MASK_LINK,(short)0));
		return PhysicsFactory.createBoxBody(this.physicsWorld, sprite, bodyType, fixtureDef);
	}

	@Override
	protected AnimatedSprite createAnimatedSprite(final float pX, final float pY, Engine engine) {
		return new AnimatedSprite(pX-width/2, pY-height/2,width,height, textureRegion, engine.getVertexBufferObjectManager());
		
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
