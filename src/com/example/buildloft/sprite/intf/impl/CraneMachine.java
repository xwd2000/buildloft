package com.example.buildloft.sprite.intf.impl;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.sprite.intf.AbstractGameSprite;

public class CraneMachine {
	private Crane crane;
	private Link link;
	private AbstractGameSprite hungedObj;
	private Context context;
	private PhysicsWorld physicsWorld;
	private ZoomCamera camera;
	
	public static void loadResource(Context context, TextureManager textManager){
		Crane.loadResource(context, textManager);
		Link.loadResource(context, textManager);
	
	}
	
	public CraneMachine(Context context,PhysicsWorld pPhysicsWorld,ZoomCamera camera,AbstractGameSprite hungedObj){
		this.context=context;
		this.physicsWorld=pPhysicsWorld;
		this.camera=camera;
		this.hungedObj=hungedObj;
	}
	
	public void pasteToSence(float pX,float pY,Scene scene){
		crane=new Crane(context,physicsWorld,camera);
		crane.pasteToSence(pX,pY, scene);
		crane.setFree();
		link=new Link(context,physicsWorld,crane,hungedObj);
		link.pasteToSence(pX+crane.getCraneWidth()/2, pY+crane.getCraneHeight(), scene);
	}
	
	
	
}
