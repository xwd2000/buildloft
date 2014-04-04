package com.example.buildloft.sprite.intf;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.list.SmartList;

import android.content.Context;



public abstract class AbstractGameSpriteBatch extends AbstractGameSprite implements InterfGameSprite{
	protected ArrayList<AbstractGameSprite> items;
	protected PhysicsWorld mPhysicsWorld;
	protected Context mContext;
	public AbstractGameSpriteBatch(Context context,PhysicsWorld pPhysicsWorld){
		super(context,pPhysicsWorld);
		this.mPhysicsWorld=pPhysicsWorld;
		this.mContext=context;
	}
	
	
	public void pasteToSence(float pX,float pY,Scene scene){
		for(AbstractGameSprite ags:items)
			ags.pasteToSence(pX, pY, scene);
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
	
	public ArrayList getSpriteItems(){
		return items;
	}

}
