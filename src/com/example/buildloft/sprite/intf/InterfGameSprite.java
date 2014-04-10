package com.example.buildloft.sprite.intf;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;


public interface InterfGameSprite {
	public void pasteToSence(float pX,float pY,Scene scene);
	public void setBodyType(BodyType bodyType);
	public void setPhysics(boolean hasBody);
	public void removeEntity(Scene scene);
}
