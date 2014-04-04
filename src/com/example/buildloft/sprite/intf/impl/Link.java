package com.example.buildloft.sprite.intf.impl;

import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import java.util.ArrayList;

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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.example.buildloft.sprite.intf.AbstractGameSprite;
import com.example.buildloft.sprite.intf.AbstractGameSpriteBatch;
import com.example.buildloft.sprite.intf.InterfGameSprite;

import android.content.Context;
import android.util.Log;

public class Link implements InterfGameSprite{
	private ArrayList<AbstractGameSprite> items;
	private int length=10;
	private PhysicsWorld physicsWorld;
	private Context context;
	private AbstractGameSprite anchorSprite;
	private AbstractGameSprite movingSprite;
	
	public Link(Context context, PhysicsWorld pPhysicsWorld,AbstractGameSprite anchorSprite,AbstractGameSprite movingSprite){
		this.physicsWorld=pPhysicsWorld;
		this.context=context;
		this.anchorSprite=anchorSprite;
		this.movingSprite=movingSprite;
		items=new ArrayList<AbstractGameSprite>();
	}
	
	
	@Override
	public void pasteToSence(float pX, float pY, Scene scene) {
		
		float linkItemWidth=0.125f*PIXEL_TO_METER_RATIO_DEFAULT;
		float linkItemHeight=0.5f*PIXEL_TO_METER_RATIO_DEFAULT;
		float linkStartX=pX;//(crane.getCraneWidth()/2+40)/PIXEL_TO_METER_RATIO_DEFAULT;
		float linkStartY=pY;//(crane.getCraneHeight()+50)/PIXEL_TO_METER_RATIO_DEFAULT;
		Log.d("main","linkItemWidth="+linkItemWidth+";linkItemHeight="+linkItemHeight+";linkStartX="+linkStartX+";linkStartY="+linkStartY);
		
		RevoluteJointDef jd=new RevoluteJointDef();
		jd.collideConnected = false;
	
		RopeJointDef ropeJointDef=new RopeJointDef();
		ropeJointDef.localAnchorA.set(0, 0);
		Body prevBody=(Body)anchorSprite.getPastedSprite().getUserData();
		anchorSprite.setGrabed();
		int N=length;
		for (int i = 0; i < N; ++i)
		{
			Log.d("main","bodyPosition=("+linkStartX+","+(linkStartY+(1/2+i)*linkItemHeight)+")");
			//AnimatedSprite sprite;
			
			LinkItem item = new LinkItem(context,physicsWorld);
			AbstractGameSprite gameSprite=(AbstractGameSprite)item;
			items.add(item);
			item.pasteToSence(linkStartX-item.getWidth()/2,linkStartY+i*item.getHeight(), scene);
			item.setFree();
			Log.d("main","SpritePosition=("+(linkStartX-linkItemWidth/2)+","+(linkStartY+i*linkItemHeight)+")");
			if (i == N - 1)
			{	
				gameSprite=movingSprite;
				gameSprite.pasteToSence(linkStartX, linkStartY+i*linkItemHeight, scene);
				gameSprite.setFree();
			}
	
			
			Body linkBody = (Body)gameSprite.getPastedSprite().getUserData();
			Vector2 anchor=new Vector2(useBodyPixel(linkStartX), useBodyPixel(linkStartY+i*linkItemHeight));
			jd.initialize(prevBody, linkBody, anchor); 
			physicsWorld.createJoint(jd);
			prevBody=linkBody;
		}
		ropeJointDef.localAnchorB.set(0f,0f);
 
		float extraLength = 0.01f;
		ropeJointDef.maxLength = N*linkItemHeight - 1.0f + extraLength;
		ropeJointDef.bodyB = prevBody;
		
		ropeJointDef.bodyA = (Body)anchorSprite.getPastedSprite().getUserData(); 
		RopeJoint ropeJoint= (RopeJoint) physicsWorld.createJoint(ropeJointDef);
	}
	
	
	private float useBodyPixel(float spritePixel){
		return spritePixel/PIXEL_TO_METER_RATIO_DEFAULT;
	}

	@Override
	public void setFree() {
		
	}

	@Override
	public void setGrabed() {
		
	}

	@Override
	public void removePhy() {
		
	}

	@Override
	public void removeEntity(Scene scene) {
		// TODO Auto-generated method stub
		
	}

	public static void loadResource(Context context,TextureManager textManager){
		LinkItem.loadResource(context, textManager);
	}
	


	
	
}
