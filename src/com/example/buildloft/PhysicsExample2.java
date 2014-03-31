package com.example.buildloft;



import static org.andengine.extension.physics.box2d.util.constants.PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;

import org.andengine.engine.camera.ZoomCamera;
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
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.list.SmartList;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJoint;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.example.buildloft.sprite.AbstractGameSprite;
import com.example.buildloft.sprite.Crane;
import com.example.buildloft.sprite.impl.Board;

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

	private static final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(10f, 1f, 0f);

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private BitmapTextureAtlas mBitmapTextureAtlasButton;
	
	private TiledTextureRegion mBoxFaceTextureRegion;
	private TiledTextureRegion mCircleFaceTextureRegion;
	private TiledTextureRegion mTriangleFaceTextureRegion;
	private TiledTextureRegion mHexagonFaceTextureRegion;

	private ITextureRegion mNextTextureRegion;   //按钮
	
	private Font huakangwawatiFont;
	private static final int FONT_SIZE = 30;
	
	private PinchZoomDetector mPinchZoomDetector;
	private SurfaceScrollDetector mScrollDetector;
	private Scene mScene;

	private PhysicsWorld mPhysicsWorld;
	private int mFaceCount = 0;
	private PointF clickPoint;
	
	private float mGravityX;
	private float mGravityY;
	
	private ZoomCamera mZoomCamera;
	
	private float mPinchZoomStartedCameraZoomFactor;
	
	private SmartList<AbstractGameSprite> gameSpriteList=new SmartList<AbstractGameSprite>();
	
	private Crane crane;
	// ===========================================================
	// Constructors
	// ===========================================================



	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Touch the screen to add objects.", Toast.LENGTH_LONG).show();

		this.mZoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		
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
		this.mBoxFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_box_tiled.png", 0, 0, 2, 1); // 64x32
		//this.mCircleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_circle_tiled.png", 0, 32, 2, 1); // 64x32
		//this.mTriangleFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_triangle_tiled.png", 0, 64, 2, 1); // 64x32
		//this.mHexagonFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "face_hexagon_tiled.png", 0, 96, 2, 1); // 64x32
		this.mBitmapTextureAtlas.load();

		//创建字体
		final ITexture huakangwawatiFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		FontFactory.setAssetBasePath("font/");
		this.huakangwawatiFont = FontFactory.createFromAsset(this.getFontManager(), huakangwawatiFontTexture, this.getAssets(), "huakangwawati.ttf", FONT_SIZE, true, Color.WHITE);
		this.huakangwawatiFont.load();
		
		//创建按钮
		this.mBitmapTextureAtlasButton = new BitmapTextureAtlas(this.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		this.mNextTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlasButton, this, "next.png", 0, 0);
		this.mBitmapTextureAtlasButton.load();
		
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

		final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(0, 0.5f, 0.5f);
		Body groundBody=PhysicsFactory.createBoxBody(this.mPhysicsWorld, ground, BodyType.StaticBody, wallFixtureDef);
		Body roofBody=PhysicsFactory.createBoxBody(this.mPhysicsWorld, roof, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, left, BodyType.StaticBody, wallFixtureDef);
		PhysicsFactory.createBoxBody(this.mPhysicsWorld, right, BodyType.StaticBody, wallFixtureDef);

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

		
		final Board board=new Board(this,mPhysicsWorld,mBoxFaceTextureRegion);
		board.pasteToSence(board.getmBoardWidth(), board.getmBoardHeight(),this.mScene);
		board.setFree();
		
		crane = new Crane(this,mPhysicsWorld,mBoxFaceTextureRegion,board,mZoomCamera);
		crane.pasteToSence(crane.getCraneWidth()/2+40,crane.getCraneHeight()/2+50,this.mScene);
		crane.setFree();
		
		final PrismaticJointDef prismaticJointDef = new PrismaticJointDef();
		prismaticJointDef.initialize(groundBody, (Body)crane.getSprite().getUserData(), groundBody.getWorldCenter(),new Vector2(20F,0F).nor());
		prismaticJointDef.enableMotor = true;
		prismaticJointDef.motorSpeed = crane.getSpeed();
		prismaticJointDef.maxMotorForce = 1000.0f;
		prismaticJointDef.lowerTranslation = -100f;
		prismaticJointDef.upperTranslation = CAMERA_WIDTH-300;
		prismaticJointDef.enableLimit=true;
		final PrismaticJoint prismaticJoint=(PrismaticJoint)this.mPhysicsWorld.createJoint(prismaticJointDef);
		
		
		float linkItemWidth=0.125f;
		float linkItemHeight=0.5f;
		float linkStartX=(crane.getCraneWidth()/2+40)/PIXEL_TO_METER_RATIO_DEFAULT;
		float linkStartY=(crane.getCraneHeight()+50)/PIXEL_TO_METER_RATIO_DEFAULT;
		Log.d("main","linkItemWidth="+linkItemWidth+";linkItemHeight="+linkItemHeight+";linkStartX="+linkStartX+";linkStartY="+linkStartY);
		FixtureDef fd=new FixtureDef();
		final PolygonShape shape = new PolygonShape();
		shape.setAsBox(linkItemWidth/2, linkItemHeight/2);
		fd.shape=shape;
		fd.density = 10.0f;
		fd.friction = 0.2f;
		fd.filter.categoryBits = 0x0003;
		fd.filter.maskBits = (short)(0xFFFF & ~0x0002);
		
		RevoluteJointDef jd=new RevoluteJointDef();
		jd.collideConnected = false;
	
		RopeJointDef ropeJointDef=new RopeJointDef();
		ropeJointDef.localAnchorA.set(0, 0);
		Body prevBody=(Body)crane.getSprite().getUserData();
		int N=20;
		for (int i = 0; i < N; ++i)
		{
			BodyDef bd=new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(linkStartX, linkStartY+(1/2+i)*linkItemHeight);
			Log.d("main","bodyPosition=("+linkStartX+","+(linkStartY+(1/2+i)*linkItemHeight)+")");
			AnimatedSprite sprite;
			sprite=new AnimatedSprite(exchangePixel(linkStartX-linkItemWidth/2), exchangePixel(linkStartY+i*linkItemHeight),
					exchangePixel(linkItemWidth),exchangePixel(linkItemHeight), this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
			Log.d("main","SpritePosition=("+exchangePixel(linkStartX-linkItemWidth/2)+","+exchangePixel(linkStartY+i*linkItemHeight)+")");
			if (i == N - 1)
			{
				shape.setAsBox(1.5f, 1.5f);
				fd.shape=shape;
				sprite=new AnimatedSprite(exchangePixel(linkStartX-1.5f), exchangePixel(linkStartY),
						exchangePixel(1.5f*2),exchangePixel(1.5f*2), this.mBoxFaceTextureRegion, this.getVertexBufferObjectManager());
				fd.density = 50.0f;
				fd.filter.categoryBits = 0x0002;
				bd.position.set(linkStartX , linkStartY+i*linkItemHeight);
				bd.angularDamping = 0.4f;
			}
			mPhysicsWorld.createBody(bd);
			
			Body linkBody = this.mPhysicsWorld.createBody(bd);
			linkBody.createFixture(fd);
			this.mScene.attachChild(sprite);
			this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(
			sprite, linkBody, true, true));
			Vector2 anchor=new Vector2(linkStartX, linkStartY+i*linkItemHeight);
			jd.initialize(prevBody, linkBody, anchor); 
			mPhysicsWorld.createJoint(jd);
			prevBody=linkBody;
		}
		ropeJointDef.localAnchorB.set(0f,0f);
 
		float extraLength = 0.01f;
		ropeJointDef.maxLength = N*linkItemHeight - 1.0f + extraLength;
		ropeJointDef.bodyB = prevBody;
		
		ropeJointDef.bodyA = (Body)crane.getSprite().getUserData(); 
		RopeJoint ropeJoint= (RopeJoint) mPhysicsWorld.createJoint(ropeJointDef);
		
		
		final Sprite nextSprite = new Sprite(CAMERA_WIDTH /20,CAMERA_HEIGHT-100, this.mNextTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				
				if(pSceneTouchEvent.isActionDown()) {
					board.setGrabed();
					prismaticJoint.setMotorSpeed(-prismaticJoint.getMotorSpeed());
				}
				return true;
			};
		};
		this.mScene.attachChild(nextSprite);
		mScene.registerTouchArea(nextSprite);
		
		return this.mScene;
	}

//	@Override
//	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
//		if(this.mPhysicsWorld != null) {
//			if(pSceneTouchEvent.isActionDown()) {
//				this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
//				return true;
//			}
//		}
//		return false;
//	}

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

	private float exchangePixel(float param){
		
		return param*PIXEL_TO_METER_RATIO_DEFAULT;
	}
	
	
	private AbstractGameSprite addFace(final float pX, final float pY) {
		Board board=new Board(this, mPhysicsWorld, mBoxFaceTextureRegion);
		board.pasteToSence(pX, pY, mScene);
		gameSpriteList.add(board);
		return board;
	}
	


	
	private void removePhysicsObj(final IShape obj) {
		final PhysicsConnector facePhysicsConnector = this.mPhysicsWorld.getPhysicsConnectorManager().findPhysicsConnectorByShape(obj);

		this.mPhysicsWorld.unregisterPhysicsConnector(facePhysicsConnector);
		this.mPhysicsWorld.destroyBody(facePhysicsConnector.getBody());

		this.mScene.unregisterTouchArea(obj);
		this.mScene.detachChild(obj);
		
		System.gc();
	}
	/**
	 * Creates a {@link Body} based on a {@link PolygonShape} in the form of a triangle:
	 * <pre>
	 *  /\
	 * /__\
	 * </pre>
	 */
	private static Body createTriangleBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		/* Remember that the vertices are relative to the center-coordinates of the Shape. */
		final float halfWidth = pAreaShape.getWidthScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = pAreaShape.getHeightScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;

		final float top = -halfHeight;
		final float bottom = halfHeight;
		final float left = -halfHeight;
		final float centerX = 0;
		final float right = halfWidth;

		final Vector2[] vertices = {
				new Vector2(centerX, top),
				new Vector2(right, bottom),
				new Vector2(left, bottom)
		};

		return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices, pBodyType, pFixtureDef);
	}

	/**
	 * Creates a {@link Body} based on a {@link PolygonShape} in the form of a hexagon:
	 * <pre>
	 *  /\
	 * /  \
	 * |  |
	 * |  |
	 * \  /
	 *  \/
	 * </pre>
	 */
	private static Body createHexagonBody(final PhysicsWorld pPhysicsWorld, final IAreaShape pAreaShape, final BodyType pBodyType, final FixtureDef pFixtureDef) {
		/* Remember that the vertices are relative to the center-coordinates of the Shape. */
		final float halfWidth = pAreaShape.getWidthScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float halfHeight = pAreaShape.getHeightScaled() * 0.5f / PIXEL_TO_METER_RATIO_DEFAULT;

		/* The top and bottom vertex of the hexagon are on the bottom and top of hexagon-sprite. */
		final float top = -halfHeight;
		final float bottom = halfHeight;

		final float centerX = 0;

		/* The left and right vertices of the heaxgon are not on the edge of the hexagon-sprite, so we need to inset them a little. */
		final float left = -halfWidth + 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float right = halfWidth - 2.5f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float higher = top + 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;
		final float lower = bottom - 8.25f / PIXEL_TO_METER_RATIO_DEFAULT;

		final Vector2[] vertices = {
				new Vector2(centerX, top),
				new Vector2(right, higher),
				new Vector2(right, lower),
				new Vector2(centerX, bottom),
				new Vector2(left, lower),
				new Vector2(left, higher)
		};

		return PhysicsFactory.createPolygonBody(pPhysicsWorld, pAreaShape, vertices, pBodyType, pFixtureDef);
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
	
	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}
	
	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
		
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
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
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
				if(Math.abs(pSceneTouchEvent.getX()-clickPoint.x)<3&&Math.abs(pSceneTouchEvent.getY()-clickPoint.y)<3)
				{
					if(this.mPhysicsWorld != null) {
						AbstractGameSprite gameSprite=this.addFace(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						gameSprite.setFree();
						crane.startRun();
						return true;
					}
				}	
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
			if(pTouchArea instanceof Text)
				return true;
			else if(pTouchArea instanceof AnimatedSprite){
				final AnimatedSprite broad = (AnimatedSprite) pTouchArea;
				this.jumpFace(broad);
			}
			return true;
		}
		return false;
	}
	
	private void jumpFace(final AnimatedSprite face) {
		final Body faceBody = (Body)face.getUserData();
		final Vector2 velocity = Vector2Pool.obtain(this.mGravityX * -50, this.mGravityY * -50);
		faceBody.setLinearVelocity(velocity);
		Vector2Pool.recycle(velocity);
	}

	public float getGravityX() {
		return mGravityX;
	}


	public float getGravityY() {
		return mGravityY;
	}


	
	
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
}
