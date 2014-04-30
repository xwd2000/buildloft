package com.example.buildloft;



import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.example.buildloft.adt.Direction;
import com.example.buildloft.consts.AppConst;
import com.example.buildloft.sprite.intf.AbstractSimpleGameSprite.AreaTouchCallBack;
import com.example.buildloft.sprite.intf.impl.AndButton;
import com.example.buildloft.sprite.intf.impl.Board;
import com.example.buildloft.sprite.intf.impl.Circle;
import com.example.buildloft.sprite.intf.impl.Crane;
import com.example.buildloft.sprite.intf.impl.CraneMachine;

/**
 * (c) 2010 Nicolas Gramlich
 * (c) 2011 Zynga
 *
 * @author Nicolas Gramlich implements IOnSceneTouchListener, 
 * @since 18:47:08 - 19.03.2010
 */
public class PhysicsExample2 extends SimpleBaseGameActivity implements IAccelerationListener, IOnSceneTouchListener ,IScrollDetectorListener, IPinchZoomDetectorListener, IOnAreaTouchListener{
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 720;
	private static final float MAX_ZOOMFACTOR = 2f;
	private static final float MIN_ZOOMFACTOR = 1f;
	
	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 1f, 0f);

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	
	private Font huakangwawatiFont;
	private static final int FONT_SIZE = 30;
	
	private PinchZoomDetector mPinchZoomDetector;
	private SurfaceScrollDetector mScrollDetector;
	private Scene mScene;

	private PhysicsWorld mPhysicsWorld;
	private PointF clickPoint;
	
	private float mGravityX;
	private float mGravityY;
	
	private ZoomCamera mZoomCamera;
	
	private float mPinchZoomStartedCameraZoomFactor;
	

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG).show();

		this.mZoomCamera = new ZoomCamera(0, 20, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mZoomCamera.setBounds(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		this.mZoomCamera.setBoundsEnabled(true);
		if(MultiTouch.isSupported(this)) {
			if(MultiTouch.isSupportedDistinct(this)) {
				Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.", Toast.LENGTH_LONG).show();
		}
		
		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mZoomCamera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 64, 128, TextureOptions.BILINEAR);
		//this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		//this.mTriangleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_triangle_tiled.png", 0, 64, 2, 1); // 64x32
		//this.mHexagonFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_hexagon_tiled.png", 0, 96, 2, 1); // 64x32
		this.mBitmapTextureAtlas.load();

		//创建字体
		final ITexture huakangwawatiFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		FontFactory.setAssetBasePath("font/");
		this.huakangwawatiFont = FontFactory.createFromAsset(this.getFontManager(), huakangwawatiFontTexture, this.getAssets(), "huakangwawati.ttf", FONT_SIZE, true, Color.WHITE);
		this.huakangwawatiFont.load();
		
		
		CraneMachine.loadResource(this, this.getTextureManager());
		Board.loadResource(this, this.getTextureManager());
		Circle.loadResource(this, this.getTextureManager());
		AndButton.loadResource(this, this.getTextureManager());
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		this.mScene = new Scene();
		this.mScene.setBackground(new Background(0, 0, 0));
		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setOnAreaTouchListener(this);
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		mPinchZoomDetector=new PinchZoomDetector(this);
		this.mScrollDetector = new SurfaceScrollDetector(this);
		
		final VertexBufferObjectManager vertexBufferObjectManager = this.getVertexBufferObjectManager();
		final Rectangle ground = new Rectangle(0, CAMERA_HEIGHT - 2, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle roof = new Rectangle(0, 0, CAMERA_WIDTH, 2, vertexBufferObjectManager);
		final Rectangle left = new Rectangle(0, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);
		final Rectangle right = new Rectangle(CAMERA_WIDTH - 2, 0, 2, CAMERA_HEIGHT, vertexBufferObjectManager);

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f,false,AppConst.CATEGORYBIT_BOUND,AppConst.MASK_BOUND,(short)0);
		Body groundBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		Body leftBody=PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		Body rightBody=PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

		this.mScene.attachChild(ground);
		this.mScene.attachChild(roof);
		this.mScene.attachChild(left);
		this.mScene.attachChild(right);
		
		
		final Body body;
		this.mScene.attachChild(new Text(5, 5, this.huakangwawatiFont, "xxoo哇奶奶的", vertexBufferObjectManager));
		
		Text myFontText=new Text(25, 390, this.huakangwawatiFont, "娃娃体", vertexBufferObjectManager);
		
		body=PhysicsFactory.createBoxBody(this.mPhysicsWorld,myFontText,BodyType.DynamicBody,  FIXTURE_DEF);
		
		this.mScene.registerTouchArea(myFontText);
		this.mScene.attachChild(myFontText);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(myFontText, body, true, true));
		
		
		this.mScene.registerUpdateHandler(this.mPhysicsWorld);
		
		final Board board=new Board(this,this.mPhysicsWorld);
		board.setAreaTouchCallBack(
				new Board.AreaTouchCallBack() {
			@Override
			public boolean onAreaTouched() {
				final Vector2 velocity = Vector2Pool.obtain(mGravityX * -50, mGravityY* -50);
				board.jumpFace(velocity);
				return false;
			}
		});
		board.pasteToSence(230, 230, mScene);
		Circle circle=new Circle(this,this.mPhysicsWorld);
		
		final CraneMachine can=new CraneMachine(this,this.mPhysicsWorld,groundBody,circle);
		can.setLimitTranslation(leftBody,rightBody);
		can.pasteToSence(50,50, mScene);
	
		float buttonWidth=80,buttonHeight=80;
		AndButton button1=new AndButton(this,buttonWidth,buttonHeight);
	
		button1.setAreaTouchCallBack(
				new AndButton.AreaTouchCallBack() {
					private int i=0;
					@Override
					public boolean onAreaTouched() {
						if(can.getHungedObj()==null){
							if(i%2==1)
								can.hungObj(mScene, new Circle(PhysicsExample2.this,mPhysicsWorld));
							else
								can.hungObj(mScene, new Board(PhysicsExample2.this,mPhysicsWorld));
							i++;
						}else
							can.dropHungedObj();
						return false;
					}
		});
		button1.pasteToSence(CAMERA_WIDTH-buttonWidth-5, CAMERA_HEIGHT-buttonHeight-5, mScene);
		
		//Crane crane = new Crane(this,mPhysicsWorld,BodyType.DynamicBody);
		//crane.pasteToSence(20, 100, mScene);
		
		return this.mScene;
	}


	@Override
	public void onAccelerationAccuracyChanged(final AccelerationData pAccelerationData) {

	}

	@Override
	public void onAccelerationChanged(final AccelerationData pAccelerationData) {
		this.mGravityX = pAccelerationData.getX();
		this.mGravityY = pAccelerationData.getY();
		final Vector2 gravity = Vector2Pool.obtain(pAccelerationData.getX(), pAccelerationData.getY());
		this.mPhysicsWorld.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public void onResumeGame() {
		super.onResumeGame();

		this.enableAccelerationSensor(this);
	}

	@Override
	public void onPauseGame() {
		super.onPauseGame();

		this.disableAccelerationSensor();
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
		System.out.println("==start");
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
		System.out.println("==onScroll");
	}
	
	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
		System.out.println("pDistanceX="+pDistanceX+",pDistanceY="+pDistanceY+",zoomFactor="+zoomFactor);
		System.out.println("==endScroll");
		
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mZoomCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, float pZoomFactor) {
		if(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor<MIN_ZOOMFACTOR)
			this.mZoomCamera.setZoomFactor(MIN_ZOOMFACTOR);
		else if(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor>MAX_ZOOMFACTOR)
			this.mZoomCamera.setZoomFactor(MAX_ZOOMFACTOR);
		else this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}


	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

		if(this.mPinchZoomDetector.isZooming()) {
			this.mScrollDetector.setEnabled(false);
		} else {
			if(pSceneTouchEvent.isActionDown()) {
				// 记录click点
				clickPoint=new PointF(pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
				this.mScrollDetector.setEnabled(true);
			}else if(pSceneTouchEvent.isActionUp()){
				if(clickPoint!=null&&Math.abs(pSceneTouchEvent.getX()-clickPoint.x)<3&&Math.abs(pSceneTouchEvent.getY()-clickPoint.y)<3)
				{
					if(this.mPhysicsWorld != null) {
						Board newboard=new Board(PhysicsExample2.this,mPhysicsWorld,100f, 100f);
						newboard.pasteToSence(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), pScene);
						
						newboard.setBodyType(BodyType.DynamicBody);
						return true;
					}
				}	
				this.mScrollDetector.setEnabled(false);
				clickPoint=null;
			}
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		
		if(pSceneTouchEvent.isActionDown()) {
			mScrollDetector.setEnabled(false);
			if(pTouchArea instanceof Text)
				return true;
			else if(pTouchArea instanceof AnimatedSprite){
				final AnimatedSprite broad = (AnimatedSprite) pTouchArea;
				return true;
			}
		}
		
		return true;
	}
	

	
	

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
}
