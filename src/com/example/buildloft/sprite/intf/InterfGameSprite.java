package com.example.buildloft.sprite.intf;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;


public interface InterfGameSprite {
	public void pasteToSence(float pX,float pY,Scene scene);
	public void setFree();
	public void setGrabed();
	public void removePhy();
	public void removeEntity(Scene scene);
}
