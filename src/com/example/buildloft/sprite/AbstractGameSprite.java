package com.example.buildloft.sprite;

import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class AbstractGameSprite extends Entity {
	protected AnimatedSprite sprite;
	protected boolean isGrabed;
	protected PhysicsWorld mPhysicsWorld;
	protected TiledTextureRegion mTextureRegion;
	protected Context mContext;
	
	protected static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 0.7f, 0.5f);
	
	public AbstractGameSprite(Context context,PhysicsWorld pPhysicsWorld,TiledTextureRegion pTextureRegion){
		this.mPhysicsWorld=pPhysicsWorld;
		this.mContext=context;
		this.mTextureRegion=pTextureRegion;
	}
	
	public AnimatedSprite pasteToSence(float pX,float pY,Scene scene){
		Engine currentEngine=((BaseGameActivity)mContext).getEngine();
		sprite=createAnimatedSprite(pX,pY,currentEngine);
		//Body body=createPhysicsBody();
		sprite.animate(200);
		//sprite.setUserData(body);
		
		scene.attachChild(sprite);			
		scene.registerTouchArea(sprite);
		Body body = createPhysicsBody(BodyType.StaticBody);
		sprite.setUserData(body);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
		isGrabed=true;
		return sprite;
		//this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
	}
	
	
	public void setFree(){
		if(isGrabed==true){
			final PhysicsConnector facePhysicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
			if(facePhysicsConnector==null)
			{
				Body body = createPhysicsBody(BodyType.DynamicBody);
				mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
			}
			else{
				facePhysicsConnector.getBody().setType(BodyType.DynamicBody);
			}
			isGrabed=false;
		}
	}
	
	public void setGrabed(){
		if(isGrabed==false){
			final PhysicsConnector facePhysicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
			if(facePhysicsConnector==null)
			{
				Body body = createPhysicsBody(BodyType.StaticBody);
				mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
			}
			else{
				facePhysicsConnector.getBody().setType(BodyType.StaticBody);
			}
			isGrabed=true;
		}
		//this.mScene.unregisterTouchArea(obj);
		//this.mScene.detachChild(obj);
	}
	
	public void removePhy(){
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
	
	public void removeEntity(Scene scene){
		if(isGrabed==false){
			final PhysicsConnector facePhysicsConnector = mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
			if(facePhysicsConnector==null)
				return;
			mPhysicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
			mPhysicsWorld.destroyBody(facePhysicsConnector.getBody());
			
			scene.unregisterTouchArea(this.sprite);
			scene.detachChild(this.sprite);
			
			System.gc();
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

	public abstract Body createPhysicsBody(BodyType bodyType);
	public abstract AnimatedSprite createAnimatedSprite(float pX,float pY,Engine engine);
	//public abstract TiledTextureRegion loadResource();
}
