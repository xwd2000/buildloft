package com.example.buildloft.sprite.intf.impl;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.sprite.intf.AbstractGameSprite;

public class LinkItem extends AbstractGameSprite{
	private float width=4f;
	private float height=16f;

	
	public LinkItem(Context context, PhysicsWorld pPhysicsWorld,
			TiledTextureRegion pTextureRegion) {
		super(context, pPhysicsWorld, pTextureRegion);
	}
	public LinkItem(Context context, PhysicsWorld pPhysicsWorld,
			TiledTextureRegion pTextureRegion,float width,float height) {
		super(context, pPhysicsWorld, pTextureRegion);
		this.width=width;
		this.height=height;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	
	@Override
	protected Body createPhysicsBody(BodyType bodyType,AnimatedSprite sprite) {
		return PhysicsFactory.createBoxBody(this.mPhysicsWorld, sprite, bodyType, FIXTURE_DEF);
	}

	@Override
	protected AnimatedSprite createAnimatedSprite(float pX, float pY, Engine engine) {
		 new AnimatedSprite(pX-width/2, pY-height/2,width,height, this.mTextureRegion, engine.getVertexBufferObjectManager());
		return null;
	}

}
