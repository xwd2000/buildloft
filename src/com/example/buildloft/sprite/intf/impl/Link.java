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

public class Link extends AbstractGameSpriteBatch implements InterfGameSprite{
	private ArrayList<AbstractGameSprite> items;
	private int length=10;
	private AbstractGameSprite anchorSprite;
	private AbstractGameSprite movingSprite;
	
	public Link(Context context, PhysicsWorld pPhysicsWorld,AbstractGameSprite anchorSprite,AbstractGameSprite movingSprite){
		super(context, pPhysicsWorld);
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
	
		
		Body prevBody=(Body)anchorSprite.getPastedSprite().getUserData();
		//anchorSprite.setBodyType(BodyType.StaticBody);
		int N=length;
		for (int i = 0; i < N; ++i)
		{
			Log.d("main","bodyPosition=("+linkStartX+","+(linkStartY+(1/2+i)*linkItemHeight)+")");
			//AnimatedSprite sprite;
			
			LinkItem item = new LinkItem(context,physicsWorld,BodyType.DynamicBody);
			AbstractGameSprite gameSprite;
			items.add(item);
			if(i!=N-1){
				gameSprite=(AbstractGameSprite)item;
				item.pasteToSence(linkStartX-item.getWidth()/2,linkStartY+i*item.getHeight(), scene);
				Log.d("main","SpritePosition=("+(linkStartX-linkItemWidth/2)+","+(linkStartY+i*linkItemHeight)+")");
			}
			else
			{	
				gameSprite=movingSprite;
				gameSprite.pasteToSence(linkStartX, linkStartY+i*linkItemHeight, scene);
				//gameSprite.setBodyType(BodyType.DynamicBody);
			}
	
			
			Body linkBody = (Body)gameSprite.getPastedSprite().getUserData();
	
			Vector2 anchor=new Vector2(useBodyPixel(linkStartX), useBodyPixel(linkStartY+i*linkItemHeight));
			jd.initialize(prevBody, linkBody, anchor); 
			physicsWorld.createJoint(jd);
			prevBody=linkBody;
		}
		
		RopeJointDef ropeJointDef=new RopeJointDef();
		ropeJointDef.localAnchorA.set(0f, 0f);
		ropeJointDef.localAnchorB.set(0f,0f);
 
		float extraLength = 0.01f;
		ropeJointDef.maxLength = N*useBodyPixel(linkItemHeight) + extraLength;
		ropeJointDef.bodyB = prevBody;
		
		ropeJointDef.bodyA = (Body)anchorSprite.getPastedSprite().getUserData(); 
		ropeJointDef.collideConnected=true;
		RopeJoint ropeJoint= (RopeJoint) physicsWorld.createJoint(ropeJointDef);
	}



	@Override
	public void removeEntity(Scene scene) {
		for(AbstractGameSprite sprite:items)
			sprite.removeEntity(scene);
		
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
		for(AbstractGameSprite sprite:items)
			sprite.setPhysics(hasBody);
		
	}


	@Override
	public void setBodyType(BodyType bodyType) {
		for(AbstractGameSprite sprite:items)
			sprite.setBodyType(bodyType);
	}

	
	
}
