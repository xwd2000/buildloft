package com.example.buildloft.sprite.intf;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;


public abstract class AbstractGameSpriteBatch implements InterfGameSprite{
	private ArrayList<AbstractGameSprite> items;
	
	
	public AnimatedSprite pasteToSence(float pX,float pY,Scene scene){
		return null;
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
