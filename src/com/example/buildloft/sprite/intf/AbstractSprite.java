package com.example.buildloft.sprite.intf;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsWorld;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.content.Context;

public abstract class  AbstractSprite implements InterfGameSprite{
	protected PhysicsWorld physicsWorld;
	protected Context context;
	public AbstractSprite( Context context,PhysicsWorld physicsWorld) {
		super();
		this.physicsWorld = physicsWorld;
		this.context = context;
	}

}
