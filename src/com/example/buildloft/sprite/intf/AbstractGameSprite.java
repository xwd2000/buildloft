package com.example.buildloft.sprite.intf;

import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
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

public abstract class AbstractGameSprite implements InterfGameSprite{
	protected AnimatedSprite sprite;
	protected boolean isGrabed;
	protected PhysicsWorld physicsWorld;

	protected Context context;
	
	protected static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(200f, 0.7f, 0.5f);
	
	public AbstractGameSprite(Context context,PhysicsWorld pPhysicsWorld){
		this.physicsWorld=pPhysicsWorld;
		this.context=context;

	}
	
	public void pasteToSence(float pX,float pY,Scene scene){
		if(sprite==null){
			Engine currentEngine=((BaseGameActivity)context).getEngine();
			sprite=createAnimatedSprite(pX,pY,currentEngine);
			//Body body=createPhysicsBody();
			sprite.animate(200);
			//sprite.setUserData(body);
			
			scene.attachChild(sprite);			
			scene.registerTouchArea(sprite);
			Body body = createPhysicsBody(BodyType.StaticBody,sprite);
			sprite.setUserData(body);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
			isGrabed=true;
			//this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
		}else{
			sprite.setPosition(pX, pY);
		}
	}
	
	
	
	public void setFree(){
		if(isGrabed==true){
			final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
			if(facePhysicsConnector==null)
			{
				Body body = createPhysicsBody(BodyType.DynamicBody,sprite);
				sprite.setUserData(body);
				physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
			}
			else{
				facePhysicsConnector.getBody().setType(BodyType.DynamicBody);
			}
			isGrabed=false;
		}
	}
	
	public void setGrabed(){
		if(isGrabed==false){
			final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
			if(facePhysicsConnector==null)
			{
				Body body = createPhysicsBody(BodyType.StaticBody,sprite);
				sprite.setUserData(body);
				physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, body, true, true));
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
			final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
			if(facePhysicsConnector==null)
				return;
			physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
			physicsWorld.destroyBody(facePhysicsConnector.getBody());
			isGrabed=true;
		}
		//this.mScene.unregisterTouchArea(obj);
		//this.mScene.detachChild(obj);
	}
	
	public void removeEntity(Scene scene){
		if(isGrabed==false){
			final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
			if(facePhysicsConnector==null)
				return;
			physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
			physicsWorld.destroyBody(facePhysicsConnector.getBody());
			
			scene.unregisterTouchArea(this.sprite);
			scene.detachChild(this.sprite);
			
			System.gc();
			isGrabed=true;
		}
		//this.mScene.unregisterTouchArea(obj);
		//this.mScene.detachChild(obj);
	}

	
	public AnimatedSprite getPastedSprite() {
		return sprite;
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	protected abstract Body createPhysicsBody(BodyType bodyType,AnimatedSprite sprite);
	protected abstract AnimatedSprite createAnimatedSprite(float pX,float pY,Engine engine);
	//public abstract TiledTextureRegion loadResource();
}
