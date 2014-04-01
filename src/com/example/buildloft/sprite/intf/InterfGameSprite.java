package com.example.buildloft.sprite.intf;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;

public interface InterfGameSprite {
	public AnimatedSprite pasteToSence(float pX,float pY,Scene scene);
	public void setFree();
	public void setGrabed();
	public void removePhy();
	public void removeEntity(Scene scene);
}
