package cn.carbs.autozoominimageview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

@SuppressLint("NewApi") 
public class AutoZoomInImageView extends ImageView{
	
	private static final String TAG = "wang";
	
	private Drawable mDrawable;
	private int mDrawableW;
	private int mDrawableH;
	
	private int mImageViewW;
	private int mImageViewH;
	
	private Matrix mMatrix;
	private float[] mValues = new float[9]; 

	public AutoZoomInImageView(Context context) {
		this(context, null);
	}
	
	public AutoZoomInImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public AutoZoomInImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setScaleType(ScaleType.MATRIX);
	}
	
	/**
	 * 在view解析出来之后进行使用，以确保可以将图片置于中间,推荐在post中使用
	 */
	public void init(){
		initInternalValues();
		initPicturePosition();
	}
	
	/**
	 * 在view解析出来之后进行使用，以确保可以将图片置于中间,推荐在post中使用
	 */
	public void init(Drawable drawable){
		initInternalValues(drawable);
		initPicturePosition();
	}
	/**
	 * 初始化成员变量，用于调整matrix的值
	 */
	private void initInternalValues(){
		mDrawable = getDrawable();
		
		if(mDrawable == null){
			throw new IllegalArgumentException("please set source of this ImageView");
		}
		
		mDrawableW = mDrawable.getIntrinsicWidth();
		mDrawableH = mDrawable.getIntrinsicHeight();
		
		mImageViewW = getMeasuredWidth();
		mImageViewH = getMeasuredHeight();
	
		mMatrix = getImageMatrix();
		mMatrix.getValues(mValues);
	}
	
	private void initInternalValues(Drawable drawable){
		mDrawable = drawable;
		
		if(mDrawable == null){
			throw new IllegalArgumentException("please set source of this ImageView");
		}
		
		mDrawableW = mDrawable.getIntrinsicWidth();
		mDrawableH = mDrawable.getIntrinsicHeight();
		
		mImageViewW = getMeasuredWidth();
		mImageViewH = getMeasuredHeight();
	
		mMatrix = getImageMatrix();
		mMatrix.getValues(mValues);
	}
	
	/**
	 * 根据图片大小以及imageview的大小，初始化图片的显示位置
	 */
	private void initPicturePosition(){
		updateMatrixValuesOrigin(mMatrix, mValues, mDrawableW, mDrawableH, mImageViewW, mImageViewH);
		setImageMatrix(mMatrix);
	}
	
	/**
	 * 开始自动放大效果
	 * @param scaleSpan
	 * @param duration
	 */
	public void startZoomInBySpan(float scaleSpan, long duration){
		
		final float oriScaleX = mValues[0];
		final float oriScaleY = mValues[4];
		
		ValueAnimator va = ValueAnimator.ofFloat(0, scaleSpan);
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				
				Float span = (Float)animation.getAnimatedValue();
				updateMatrixValuesSpan(mValues, mDrawableW, mDrawableH, mImageViewW, mImageViewH, oriScaleX, oriScaleY, span);
				mMatrix.setValues(mValues);
				setImageMatrix(mMatrix);
			}
		});
		va.setDuration(duration);
		va.start();
	}
	
	/**
	 * 在delayed毫秒后，开始自动放大效果
	 * @param scaleSpan
	 * @param duration
	 * @param delayed
	 */
	public void startZoomInBySpanDelayed(final float scaleSpan, final long duration, long delayed){
		
		postDelayed(new Runnable() {
			@Override
			public void run() {
				startZoomInBySpan(scaleSpan, duration);
			}
		}, delayed);
	}
	
	//初始化位置
	private void updateMatrixValuesOrigin(Matrix outMatrix, float[] outValues, float drawW, float drawH, float imageW, float imageH){
		
		if(outMatrix == null || outValues == null){
			throw new IllegalArgumentException("please set source of this ImageView's matrix and values");
		}
		
		outMatrix.reset();
		
		if((imageH * drawW > drawH * imageW)){
			//如果iv更细高
			float scale1 = ((float)imageH)/((float)drawH);
			float offset1 = (drawW * scale1 - (float)imageW)/2;
			
			outMatrix.postScale(scale1, scale1);
			outMatrix.postTranslate(-offset1, 0);
					
		}else{
			//如果iv更宽扁
			float scale2 = ((float)imageW)/((float)drawW);
			float offset2 = (drawH * scale2 - (float)imageH)/2;

			outMatrix.postScale(scale2, scale2);
			outMatrix.postTranslate(0, -offset2);
		}
		outMatrix.getValues(outValues);
	}
	
	private void updateMatrixValuesSpan(float[] outValues, float drawW, float drawH, float imageW, float imageH, float oriScaleX, float oriScaleY, float span){
		//根据四个参数：图片的宽高、控件的宽高，动态的计算出输出的矩阵（float数组）的值
		outValues[0] = oriScaleX + span;
		//y轴放大
		outValues[4] = oriScaleY + span;
		//x轴平移
		float offsetwidth = (drawW * outValues[0] - (float)imageW)/2;
		outValues[2] = - offsetwidth;
		//y轴平移
		float offsetHeight = (drawH * outValues[0] - (float)imageH)/2;
		outValues[5] = - offsetHeight;
	}

	//function for log
	public String printMyMatrix(Matrix m){
		float[] valueFloat = new float[9];
		m.getValues(valueFloat);
		
		String s = "";
		for(int i = 0; i < 9; i++){
			s = s + " [ " + valueFloat[i] + " ] ";
		}
		return s;
	}
		
	//function for log
	public String printMyValue(float[] valueFloat){
		String s = "";
		for(int i = 0; i < 9; i++){
			s = s + " [ " + valueFloat[i] + " ] ";
		}
		return s;
	}
	
	/**
	 * 暂时无效的方法
	 * @param outValues
	 * @param drawW
	 * @param drawH
	 * @param imageW
	 * @param imageH
	 * @param delta
	 */
	@Deprecated
	private void updateMatrixValuesDelta(float[] outValues, float drawW, float drawH, float imageW, float imageH, float delta){
		//根据四个参数：图片的宽高、控件的宽高，动态的计算出输出的矩阵（float数组）的值
		outValues[0] = outValues[0] + delta;
		//y轴放大
		outValues[4] = outValues[4] + delta;
		//x轴平移
		float offsetwidth = (drawW * outValues[0] - (float)imageW)/2;
		outValues[2] = - offsetwidth;
		//y轴平移
		float offsetHeight = (drawH * outValues[0] - (float)imageH)/2;
		outValues[5] = - offsetHeight;
	}
	
	/**
	 * 暂时无效的方法
	 * @param outValues
	 * @param drawW
	 * @param drawH
	 * @param imageW
	 * @param imageH
	 * @param span
	 */
	@Deprecated
	private void updateMatrixValuesSpan(float[] outValues, float drawW, float drawH, float imageW, float imageH, float span){
		//根据四个参数：图片的宽高、控件的宽高，动态的计算出输出的矩阵（float数组）的值
		outValues[0] = span;
		//y轴放大
		outValues[4] = span;
		//x轴平移
		float offsetwidth = (drawW * outValues[0] - (float)imageW)/2;
		outValues[2] = - offsetwidth;
		//y轴平移
		float offsetHeight = (drawH * outValues[0] - (float)imageH)/2;
		outValues[5] = - offsetHeight;
	}
	
}
