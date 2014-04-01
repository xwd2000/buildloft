package com.example.buildloft.sprite.intf;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import android.content.Context;



public abstract class AbstractGameSpriteBatch implements InterfGameSprite{
	protected ArrayList<AbstractGameSprite> items;
	protected SpriteBatch spriteBatch;
	protected PhysicsWorld mPhysicsWorld;
	protected Context mContext;
	
	public AbstractGameSpriteBatch(Context context,PhysicsWorld pPhysicsWorld){
		this.mPhysicsWorld=pPhysicsWorld;
		this.mContext=context;
	}
	
	
	public Shape pasteToSence(float pX,float pY,Scene scene){
		
		scene.attachChild(spriteBatch);
		return spriteBatch;
	}
	public void setFree(){
		for(AbstractGameSprite sprite:items)
			sprite.setFree();
	}
	public void setGrabed(){
		for(AbstractGameSprite sprite:items)
			sprite.setGrabed();
	}
	public void removePhy(){
		for(AbstractGameSprite sprite:items)
			sprite.removePhy();
	}
	public void removeEntity(Scene scene){
		for(AbstractGameSprite sprite:items)
			sprite.removeEntity(scene);
	}
	

}
