package com.example.buildloft.sprite.intf;

import java.util.ArrayList;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.util.adt.list.SmartList;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

import android.content.Context;



public abstract class AbstractPhysicsGameSpriteBatch extends AbstractGameSprite implements IPhysicsGameSprite{
	protected PhysicsWorld physicsWorld;
	public AbstractPhysicsGameSpriteBatch(Context context,PhysicsWorld pPhysicsWorld){
		super(context);
		this.physicsWorld=pPhysicsWorld;
	}

}
