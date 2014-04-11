package com.example.buildloft.sprite.intf;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public interface IPhysicsGameSprite extends IGameSprite{
	public void setBodyType(BodyType bodyType);
	public void setPhysics(boolean hasBody);
}
