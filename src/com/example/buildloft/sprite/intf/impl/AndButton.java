package com.example.buildloft.sprite.intf.impl;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.content.Context;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.example.buildloft.sprite.intf.AbstractGameSprite;
import com.example.buildloft.sprite.intf.AbstractSimpleGameSprite;
import com.example.buildloft.sprite.intf.IGameSprite;

public class AndButton extends AbstractSimpleGameSprite implements IGameSprite{
	private static TextureRegion textureRegion;
	private float width;
	private float height;
	
	public AndButton(Context context) {
		super(context);
	}
	public AndButton(Context context,float width,float height) {
		super(context);
		this.width=width;
		this.height=height;
	}

	@Override
	protected Sprite createAnimatedSprite(float pX, float pY,
			Engine engine) {
		Sprite sprite=
		new Sprite(pX, pY,width,height, this.textureRegion, engine.getVertexBufferObjectManager()){
			//如果sprite要出发areaTouch事件，添加areaTouchCallBack，并注册如下代码
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(areaTouchCallBack!=null&&pSceneTouchEvent.isActionDown()){
					return areaTouchCallBack.onAreaTouched();
				}
				else
					return false;
			}
			
		};
		sprite.setZIndex(20);
		return sprite;
	}

	
	
	// ===========================================================
	// other Methods
	// ===========================================================
	public static TextureRegion loadResource(Context context,TextureManager textManager){
		BitmapTextureAtlas bitmapTextureAtlas = new BitmapTextureAtlas(textManager, 256, 128, TextureOptions.BILINEAR);
		textureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, context, "next.png", 0, 0);
		bitmapTextureAtlas.load();
		return textureRegion;
	}
	
}
