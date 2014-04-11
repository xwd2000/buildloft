package com.example.buildloft.sprite.intf.impl;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import java.util.ArrayList;
import java.util.Iterator;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.example.buildloft.sprite.intf.AbstractPhysicsGameSprite;
import com.example.buildloft.sprite.intf.AbstractPhysicsGameSpriteBatch;
import com.example.buildloft.sprite.intf.IGameSprite;

import android.content.Context;
import android.util.Log;

public class Link extends AbstractPhysicsGameSpriteBatch implements IGameSprite{
	private ArrayList<AbstractPhysicsGameSprite> items;
	private int linkItemNum=10;
	private float linkLength=200;
	private AbstractPhysicsGameSprite anchorSprite;
	private AbstractPhysicsGameSprite hungedObj;
	private Joint hungedObjRevoluteJoint;
	private Joint hungedObjRopeJoint;
	
	public Link(Context context, PhysicsWorld pPhysicsWorld,AbstractPhysicsGameSprite anchorSprite){
		super(context, pPhysicsWorld);
		this.anchorSprite=anchorSprite;
		this.hungedObj=null;
		items=new ArrayList<AbstractPhysicsGameSprite>();
	}
	
	public Link(Context context, PhysicsWorld pPhysicsWorld,AbstractPhysicsGameSprite anchorSprite,int linkItemNum,float linkLength){
		super(context, pPhysicsWorld);
		this.anchorSprite=anchorSprite;
		this.hungedObj=null;
		items=new ArrayList<AbstractPhysicsGameSprite>();
		this.linkItemNum=linkItemNum;
		this.linkLength=linkLength;
	}
	
	@Override
	public void pasteToSence(float pX, float pY, Scene scene) {
		float linkItemHeight=linkLength/linkItemNum;
		float linkStartX=pX;//(crane.getCraneWidth()/2+40)/PIXEL_TO_METER_RATIO_DEFAULT;
		float linkStartY=pY;//(crane.getCraneHeight()+50)/PIXEL_TO_METER_RATIO_DEFAULT;
		RevoluteJointDef jd=new RevoluteJointDef();
		jd.collideConnected = false;
	
		
		Body prevBody=(Body)anchorSprite.getPastedSprite().getUserData();
		//anchorSprite.setBodyType(BodyType.StaticBody);
		int N=linkItemNum;
		for (int i = 0; i < N; ++i)
		{
			AbstractPhysicsGameSprite gameSprite;
			LinkItem item = new LinkItem(context,physicsWorld,BodyType.DynamicBody);
			item.setHeight(linkItemHeight);//设置每节链子的长度
			gameSprite = item;
			//gameSprite.pasteToSence(linkStartX-item.getWidth()/2,linkStartY+i*item.getHeight(), scene);
			gameSprite.pasteToSence(linkStartX,linkStartY+item.getHeight()/2+i*item.getHeight(), scene);
			items.add(gameSprite);
			Body linkBody = (Body)gameSprite.getPastedSprite().getUserData();
			Vector2 anchor=new Vector2(useBodyPixel(linkStartX), useBodyPixel(linkStartY+i*linkItemHeight));
			jd.initialize(prevBody, linkBody, anchor); 
			physicsWorld.createJoint(jd);
			prevBody=linkBody;
		}
	}
	
	
	public void addHungedObj(Scene scene,AbstractPhysicsGameSprite hungedObj){
		if(this.hungedObj==null){
			LinkItem lastItem = (LinkItem)items.get(items.size()-1);
			float[] jointPoint=lastItem.getPastedSprite().convertLocalToSceneCoordinates(lastItem.getWidth()/2,lastItem.getHeight());
			float jointCoordinateX=jointPoint[0];
			float jointCoordinateY=jointPoint[1];
			Log.d("","=="+jointCoordinateX+","+jointCoordinateY);
			hungedObj.pasteToSence(jointCoordinateX, jointCoordinateY, scene);
			
			//旋转Joint
			RevoluteJointDef jd=new RevoluteJointDef();
			jd.collideConnected = false;
			Body linkBody = (Body)lastItem.getPastedSprite().getUserData();
			Vector2 anchor=new Vector2(useBodyPixel(jointCoordinateX), useBodyPixel(jointCoordinateY));
			jd.initialize((Body)hungedObj.getPastedSprite().getUserData(), linkBody, anchor); 
			hungedObjRevoluteJoint=physicsWorld.createJoint(jd);
			
			//绳子Joint
			RopeJointDef ropeJointDef=new RopeJointDef();
			ropeJointDef.localAnchorA.set(0f, 0f);
			ropeJointDef.localAnchorB.set(0f,0f);
	 
			ropeJointDef.maxLength = linkItemNum*useBodyPixel(lastItem.getHeight());
			ropeJointDef.bodyB = (Body)lastItem.getPastedSprite().getUserData();
			ropeJointDef.bodyA = (Body)anchorSprite.getPastedSprite().getUserData(); 
			ropeJointDef.collideConnected=true;
			hungedObjRopeJoint = (RopeJoint) physicsWorld.createJoint(ropeJointDef);
			
			this.hungedObj=hungedObj;
		}
	}

	public AbstractPhysicsGameSprite dropHungedObj(){
		if(this.hungedObj!=null){
			physicsWorld.destroyJoint(hungedObjRevoluteJoint);
			physicsWorld.destroyJoint(hungedObjRopeJoint);
			AbstractPhysicsGameSprite obj=this.hungedObj;
			this.hungedObj=null;
			return obj;
		}
		return null;
		
	}
	
	public void removeHungedObj(Scene scene){
		if(this.hungedObj!=null){
			final PhysicsConnector facePhysicsConnector = physicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(hungedObj.getPastedSprite());
			physicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
			physicsWorld.destroyBody(facePhysicsConnector.getBody());
			scene.unregisterTouchArea(hungedObj.getPastedSprite());
			scene.detachChild(hungedObj.getPastedSprite());
			hungedObj=null;
			System.gc();
		}
	}

	@Override
	public void remove(Scene scene) {
		for(AbstractPhysicsGameSprite sprite:items)
			sprite.remove(scene);
		
	}

	public static void loadResource(Context context,TextureManager textManager){
		LinkItem.loadResource(context, textManager);
	}
	
	// 将sprint用坐标转化为body用坐标
	protected float useBodyPixel(float spritePixel){
		return spritePixel/PIXEL_TO_METER_RATIO_DEFAULT;
	}


	@Override
	public void setPhysics(boolean hasBody) {
		for(AbstractPhysicsGameSprite sprite:items)
			sprite.setPhysics(hasBody);
		
	}


	@Override
	public void setBodyType(BodyType bodyType) {
		for(AbstractPhysicsGameSprite sprite:items)
			sprite.setBodyType(bodyType);
	}

	
	
	
	
}
