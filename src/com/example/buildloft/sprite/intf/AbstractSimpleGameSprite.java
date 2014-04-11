package com.example.buildloft.sprite.intf;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.ui.activity.BaseGameActivity;

import com.example.buildloft.sprite.intf.AbstractPhysicsGameSprite.AreaTouchCallBack;

import android.content.Context;

public abstract class AbstractSimpleGameSprite extends AbstractGameSprite implements IGameSprite{
	protected Sprite sprite;
	protected AreaTouchCallBack areaTouchCallBack;
	
	public AbstractSimpleGameSprite(Context context) {
		super(context);
	}

	public void pasteToSence(float pX,float pY,Scene scene){
		if(sprite==null){
			Engine currentEngine=((BaseGameActivity)context).getEngine();
			sprite=createAnimatedSprite(pX,pY,currentEngine);
			if(areaTouchCallBack!=null)
				scene.registerTouchArea(sprite);
			scene.attachChild(sprite);
		}else{
			sprite.setPosition(pX, pY);
		}
	}
	public void remove(Scene scene){
		scene.detachChild(sprite);
	}
	
	public AreaTouchCallBack getAreaTouchCallBack() {
		return areaTouchCallBack;
	}

	public void setAreaTouchCallBack(AreaTouchCallBack areaTouchCallBack) {
		this.areaTouchCallBack = areaTouchCallBack;
	}

	
	protected abstract Sprite createAnimatedSprite(float pX,float pY,Engine engine);
	
	public interface AreaTouchCallBack{
		public boolean onAreaTouched();
	}
}
