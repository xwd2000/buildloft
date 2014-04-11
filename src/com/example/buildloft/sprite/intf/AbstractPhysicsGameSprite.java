package com.example.buildloft.sprite.intf;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import org.andengine.engine.Engine;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public abstract class AbstractPhysicsGameSprite extends AbstractGameSprite implements IPhysicsGameSprite{
	protected PhysicsWorld physicsWorld;
	protected AnimatedSprite sprite;
	protected Body spriteBody;
	protected BodyType bodyType;
	
	protected AreaTouchCallBack areaTouchCallBack;

	protected FixtureDef fixtureDef = PhysicsFactory.createFixtureDef(200f, 0.7f, 0.5f);
	
	public AbstractPhysicsGameSprite(Context context,PhysicsWorld pPhysicsWorld){
		super(context);
		this.bodyType=BodyType.DynamicBody;
		this.physicsWorld=pPhysicsWorld;
	}
	
	public AbstractPhysicsGameSprite(Context context,PhysicsWorld pPhysicsWorld,BodyType bodyType){
		super(context);
		this.bodyType=bodyType;
		this.physicsWorld=pPhysicsWorld;
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
			spriteBody = createPhysicsBody(bodyType,sprite);
			sprite.setUserData(spriteBody);
			physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, spriteBody, true, true));
		}else{
			sprite.setPosition(pX, pY);
		}
	}
	
	
	
	public void setBodyType(BodyType bodyType){
		spriteBody.setType(bodyType);
	}
	public BodyType getBodyType(){
		return spriteBody.getType();
	}
	
	public void setPhysics(boolean hasBody){
		if(hasBody)
		{
			//添加body,有物理特性
			if(spriteBody==null){
				spriteBody = createPhysicsBody(bodyType,sprite);
				sprite.setUserData(spriteBody);
				physicsWorld.registerPhysicsConnector(new PhysicsConnector(sprite, spriteBody, true, true));
			}else{
				final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
				if(facePhysicsConnector==null)
					return ;//不考虑未连接情况
			}
				
		}else{
			//删除body，无物理特性
			if(spriteBody!=null){
				final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
				if(facePhysicsConnector==null)
					return;
				physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
				physicsWorld.destroyBody(facePhysicsConnector.getBody());
				spriteBody=null;
			}
		}
		
	}
	
	public void remove(Scene scene){
		final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(sprite);
		if(facePhysicsConnector!=null){
			physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
			physicsWorld.destroyBody(facePhysicsConnector.getBody());
		}
		scene.unregisterTouchArea(this.sprite);
		scene.detachChild(this.sprite);
		
		System.gc();
	
		//this.mScene.unregisterTouchArea(obj);
		//this.mScene.detachChild(obj);
	}

	
	public boolean collidesWith(AbstractPhysicsGameSprite sprite){
		return this.sprite.collidesWith(sprite.getPastedSprite());
	}
	
	public boolean collidesWith(final IShape sprite){
		return this.sprite.collidesWith(sprite);
	}
	// 将sprint用坐标转化为body用坐标
	protected float useBodyPixel(float spritePixel){
		return spritePixel/PIXEL_TO_METER_RATIO_DEFAULT;
	}
	
	public AnimatedSprite getPastedSprite() {
		return sprite;
	}

	public void setSprite(AnimatedSprite sprite) {
		this.sprite = sprite;
	}

	
	public AreaTouchCallBack getAreaTouchCallBack() {
		return areaTouchCallBack;
	}

	public void setAreaTouchCallBack(AreaTouchCallBack areaTouchCallBack) {
		this.areaTouchCallBack = areaTouchCallBack;
	}


	public void setFixtureDef(FixtureDef fixtureDef){
		this.fixtureDef=fixtureDef;
	}
	
	public FixtureDef getFixtureDef() {
		return fixtureDef;
	}


	protected abstract Body createPhysicsBody(BodyType bodyType,AnimatedSprite sprite);
	protected abstract AnimatedSprite createAnimatedSprite(float pX,float pY,Engine engine);

	//public abstract TiledTextureRegion loadResource();
	
	public interface AreaTouchCallBack{
		public boolean onAreaTouched();
	}

}
