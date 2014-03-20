package com.example.buildloft.sprite;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.vbo.ITiledSpriteVertexBufferObject;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.example.buildloft.adt.Direction;

public class Crane extends AnimatedSprite{
	private AnimatedSprite board;
	private int speed;
	private Direction mDirection;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public Crane(float pX, float pY, float pWidth, float pHeight,
			ITiledTextureRegion pTiledTextureRegion,
			ITiledSpriteVertexBufferObject pTiledSpriteVertexBufferObject) {
		super(pX, pY, pWidth, pHeight, pTiledTextureRegion,
				pTiledSpriteVertexBufferObject);
		mDirection=Direction.RIGHT;
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public AnimatedSprite getBoard() {
		return board;
	}


	public void setBoard(AnimatedSprite board) {
		this.board = board;
	}


	public int getSpeed() {
		return speed;
	}


	public void setSpeed(int speed) {
		this.speed = speed;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	
	// ===========================================================
	// other Methods
	// ===========================================================

	public void startRun(){
		
	}
	public void stopRun(){
		
	}

}
