package com.japyijiwenfa.view;


import com.japyijiwenfa.R;
import com.japyijiwenfa.JapyijiwenfaActivity;
import com.japyijiwenfa.JapyijiwenfaActivity.WhatMessage;
import com.japyijiwenfa.util.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Loading extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "Loading";

	private JapyijiwenfaActivity mContext;

	private Paint mPaint;
	private int mCurrAlpha = 0;

	private Bitmap[] mLogos = new Bitmap[2];
	private Bitmap mCurrLogo;

	//logo ������screen�е�λ��
	private int mCurrX;
	private int mCurrY;

	private int mWidth;
	private int mHeight;

	public Loading(Context context) {
		super(context);
		init();
	}

	public Loading(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Loading(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mContext = (JapyijiwenfaActivity) getContext();
		// �����������ڻص��ӿڵ�ʵ����
		getHolder().addCallback(this);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		// ����ͼƬ
		mLogos[0] = BitmapFactory.decodeResource(getResources(),R.drawable.logo1);
		mLogos[1] = BitmapFactory.decodeResource(getResources(),R.drawable.logo3);

		mWidth = Utils.getDeviceScreenWidth(mContext);
		mHeight = Utils.getDeviceScreenHeight(mContext);
	}

	public void onDraw(Canvas canvas) {
		// ���ƺ��������屳��
		mPaint.setColor(Color.BLACK);// ���û�����ɫ
		mPaint.setAlpha(255);
		canvas.drawRect(0, 0, mWidth, mHeight, mPaint);
		// ����ƽ����ͼ
		if (mCurrLogo == null)
			return;
		mPaint.setAlpha(mCurrAlpha);
		canvas.drawBitmap(mCurrLogo, mCurrX, mCurrY, mPaint);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "surfaceCreated");
		new Thread(){
			public void run(){
				for(Bitmap logo : mLogos){
					mCurrLogo = logo;
					//����ͼƬλ��
					mCurrX = mWidth/2 - logo.getWidth()/2;
					mCurrY = mHeight/2 - logo.getHeight()/2;
					//��̬�ı�alphaֵ,���Ҳ�ͣ���ػ�ͼƬ
					for(int i=255;i>-10;i=i-10){
						mCurrAlpha = i;
						if(mCurrAlpha < 0){
							mCurrAlpha = 0;
						}
						SurfaceHolder holder = Loading.this.getHolder();
						//��ȡ����
						Canvas canvas = holder.lockCanvas();
						try{
							synchronized (holder) {
								onDraw(canvas);
							}
						}catch (Exception e) {
							e.printStackTrace();
						}finally{
							if(canvas!=null){
								holder.unlockCanvasAndPost(canvas);
							}
						}
						//��ʼһ����ͼƬ
						try{
							if(i==255){
								Thread.sleep(2000);
							}
							Thread.sleep(50);
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				mContext.sendMessage(WhatMessage.MAIN_VIEW);
			}
		}.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

	}

}
