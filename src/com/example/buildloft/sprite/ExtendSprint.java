package com.example.buildloft.sprite;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class ExtendSprint extends AnimatedSprite{
	private boolean isGrabed;
	public static final float BOARD_WIDTH=130;
	public static final float BOARD_HEIGHT=13;
	private PhysicsWorld physicsWorld;
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 1f, 0f);
	
	public ExtendSprint(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion pTiledTextureRegion,
			ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion,
				pTiledSpriteVertexBufferObject);
	}
	
	public void setFree(PhysicsWorld pPhysicsWorld){
		isGrabed=false;
		Body body = PhysicsFactory.createBoxBody(pPhysicsWorld, this, BodyType.DynamicBody, FIXTURE_DEF);
		pPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, true));
		//this.mScene.unregisterTouchArea(obj);
		//this.mScene.detachChild(obj);
	}
	
	public void setGrabed(PhysicsWorld pPhysicsWorld){
		isGrabed=true;
		
		final PhysicsConnector facePhysicsConnector = pPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(this);
		if(facePhysicsConnector==null)
			return;
		pPhysicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
		pPhysicsWorld.destroyBody(facePhysicsConnector.getBody());

		//this.mScene.unregisterTouchArea(obj);
		//this.mScene.detachChild(obj);
	}
	
	
	


	public boolean isGrabed() {
		return isGrabed;
	}





}
