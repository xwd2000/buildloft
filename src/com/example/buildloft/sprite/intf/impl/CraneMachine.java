package com.example.buildloft.sprite.intf.impl;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.example.buildloft.adt.Direction;
import com.example.buildloft.consts.AppConst;
import com.example.buildloft.sprite.intf.AbstractPhysicsGameSprite;
import com.example.buildloft.sprite.intf.AbstractPhysicsGameSpriteBatch;
import com.example.buildloft.sprite.intf.IGameSprite;
import com.example.buildloft.sprite.intf.IPhysicsGameSprite;

public class CraneMachine extends AbstractPhysicsGameSpriteBatch implements IPhysicsGameSprite{
	private Crane crane;
	private Link link;
	private AbstractPhysicsGameSprite hungedObj;
	private Body ground;   //地面，机器相对于地面平行移动
	private Body limitTranslationLeftBody,limitTranslationRightBody;  //移动的左右边界
	private PrismaticJoint prismaticJoint;

	
	public static void loadResource(Context context, TextureManager textManager){
		Crane.loadResource(context, textManager);
		Link.loadResource(context, textManager);
	
	}
	
	public CraneMachine(Context context,PhysicsWorld pPhysicsWorld,Body ground,AbstractPhysicsGameSprite hungedObj){
		super(context,pPhysicsWorld);
		this.ground=ground;
		this.hungedObj=hungedObj;
	}
	
	public void pasteToSence(float pX,float pY,Scene scene){
		
		crane=new Crane(context,physicsWorld,BodyType.DynamicBody);

		crane.pasteToSence(pX,pY, scene);
		
		final PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
		prismaticJointDef.initialize(ground, (Body)crane.getPastedSprite().getUserData(), ground.getWorldCenter(),new Vector2(20F,0F).nor());
		prismaticJointDef.enableMotor = true;
		prismaticJointDef.motorSpeed = crane.getSpeed();
		prismaticJointDef.maxMotorForce = 1000.0f;
		prismaticJointDef.enableLimit=false;
		prismaticJoint=(PrismaticJoint)physicsWorld.createJoint(prismaticJointDef);
		
		physicsWorld.setContactListener(new ContactListener() {
			private Body bodyA;
			private Body bodyB;
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}
			
			@Override
			public void endContact(Contact contact) {
				
				if((bodyA.equals(limitTranslationLeftBody)&&bodyB.equals(crane.getPastedSprite().getUserData()))||
						(bodyA.equals(crane.getPastedSprite().getUserData())&&bodyB.equals(limitTranslationLeftBody))){
					prismaticJoint.setMotorSpeed(crane.getSpeed());
				}
				else 	if((bodyA.equals(limitTranslationRightBody)&&bodyB.equals(crane.getPastedSprite().getUserData()))||
						(bodyA.equals(crane.getPastedSprite().getUserData())&&bodyB.equals(limitTranslationRightBody))){
					prismaticJoint.setMotorSpeed(-crane.getSpeed());
				}
			} 
			
			@Override
			public void beginContact(Contact contact) {
				bodyA=contact.getFixtureA().getBody();
				bodyB=contact.getFixtureB().getBody();
			}
		});
		
		link=new Link(context,physicsWorld,crane);
		link.pasteToSence(pX+crane.getCraneWidth()/2, pY+crane.getCraneHeight(), scene);
		link.addHungedObj(scene, hungedObj);
	}
	
	
	
	public void dropHungedObj() {
		link.dropHungedObj();
		hungedObj=null;
	}

	public void hungObj(Scene scene,AbstractPhysicsGameSprite hungedObj) {
		link.addHungedObj(scene,hungedObj);
		this.hungedObj=hungedObj;
	}
	
	public void removeHungedObj(Scene scene){
		
		link.removeHungedObj(scene);
		hungedObj=null;
	}

	public AbstractPhysicsGameSprite getHungedObj() {
		return hungedObj;
	}

	public void setLimitTranslation(Body limitTranslationLeftBody,Body limitTranslationRightBody) {
		this.limitTranslationRightBody = limitTranslationRightBody;
		this.limitTranslationLeftBody = limitTranslationLeftBody;
	}

	@Override
	public void setBodyType(BodyType bodyType) {
		crane.setBodyType(bodyType);
		link.setBodyType(bodyType);
	}

	@Override
	public void setPhysics(boolean hasBody) {
		crane.setPhysics(hasBody);
		link.setPhysics(hasBody);
		
	}

	@Override
	public void remove(Scene scene) {
		crane.remove(scene);
		link.remove(scene);
	}
	
	
}
