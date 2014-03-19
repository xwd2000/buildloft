package com.example.buildloft.sprite.impl;

import org.andengine.engine.Engine;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.sprite.AbstractGameSprite;

public class Board extends AbstractGameSprite{
	public Board(PhysicsWorld pPhysicsWorld,TiledTextureRegion pTiledTextureRegion,Engine engine) {
		super(pPhysicsWorld,pTiledTextureRegion,engine,0L,0L);
	}

	private static final float BOARD_WIDTH=130L;
	private static final float BOARD_HEIGHT=13L;
	
	@Override
	public Body createPhysicsBody() {
		return PhysicsFactory.createBoxBody(this.mPhysicsWorld, sprite, BodyType.DynamicBody, FIXTURE_DEF);
	}

	@Override
	public AnimatedSprite createAnimatedSprite(float pX,float pY,Engine engine) {
		return new AnimatedSprite(pX-BOARD_WIDTH/2, pY-BOARD_HEIGHT/2,BOARD_WIDTH,BOARD_HEIGHT, this.mTextureRegion, engine.getVertexBufferObjectManager());
	}

}
