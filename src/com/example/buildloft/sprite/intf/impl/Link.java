package com.example.buildloft.sprite.intf.impl;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.sprite.intf.AbstractGameSpriteBatch;
import com.example.buildloft.sprite.intf.InterfGameSprite;

import android.content.Context;

public class Link extends AbstractGameSpriteBatch{
	private ArrayList<LinkItem> linkItems;
	
	public Link(Context context, PhysicsWorld pPhysicsWorld) {
		super(context, pPhysicsWorld);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Shape pasteToSence(float pX, float pY, Scene scene) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setFree() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGrabed() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removePhy() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void removeEntity(Scene scene) {
		// TODO Auto-generated method stub
		
	}

	

	
	


	
	
}
