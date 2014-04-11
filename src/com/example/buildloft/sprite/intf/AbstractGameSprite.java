package com.example.buildloft.sprite.intf;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.content.Context;

public abstract class  AbstractGameSprite implements IGameSprite{
	
	protected Context context;
	
	public AbstractGameSprite( Context context) {
		super();
		this.context = context;
	}
}
