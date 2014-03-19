package com.example.buildloft.sprite;

import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public abstract class AbstractGameSprite extends Entity {
	protected AnimatedSprite sprite;
	protected boolean isGrabed;
	protected PhysicsWorld mPhysicsWorld;
	protected TiledTextureRegion mTextureRegion;
	
	
	protected static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 1f, 0f);
	
	public AbstractGameSprite(PhysicsWorld pPhysicsWorld,TiledTextureRegion pTextureRegion,Engine engine,float pX,float pY){
		this.mTextureRegion=pTextureRegion;
		this.sprite=createAnimatedSprite(pX,pY,engine);
		
		this.mPhysicsWorld=pPhysicsWorld;
	}
	
	public void setFree(){
		if(isGrabed==true){
			
			Body body = createPhysicsBody();
			mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
			//this.mScene.unregisterTouchArea(obj);
			//this.mScene.detachChild(obj);
			isGrabed=false;
		}
	}
	
	public void setGrabed(){
		if(isGrabed==false){
			final PhysicsConnector facePhysicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
			if(facePhysicsConnector==null)
				return;
			mPhysicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
			mPhysicsWorld.destroyBody(facePhysicsConnector.getBody());
			isGrabed=true;
		}
		//this.mScene.unregisterTouchArea(obj);
		//this.mScene.detachChild(obj);
	}
	
	
	public AnimatedSprite getSprite() {
		return sprite;
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	public abstract Body createPhysicsBody();
	public abstract AnimatedSprite createAnimatedSprite(float pX,float pY,Engine engine);
}
