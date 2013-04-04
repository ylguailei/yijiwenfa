package com.japyijiwenfa;


import com.japyijiwenfa.util.Constant;
import com.japyijiwenfa.util.TextReader;
import com.japyijiwenfa.util.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class BookText {
	private static final String TAG = "BookText";
	//�ı����-Ӣ��
	private static int TEXT_SPACE_BETWEEN_EN = 8;
	//�ı����-����
	private static int TEXT_SPACE_BETWEEN_CN = 16;
	/* screen width */
	private int mWidth;
	/* screen height */
	private int mHeight;
	/* ���ݵĿ�,�������м�� */
	private int mContentWidth;
	/* ���ݵĸ�,�������м�� */
	private int mContentHeight;
	/* ���±�Ե */
	private int mMarginVertical = 15;
	/* ���ұ�Ե */
	private int mMarginHorizontal = 20;
	/**ÿҳ��������*/
	private int mPageWords;
	private int mPage;
	public int mPageCount;
	//�м��
	private float mLineSpacing;
	/*��ǰλ��,��ȡ�ַ�ʱ�Ӵ�λ�ÿ�ʼ��ȡ*/
	private int mNextPageStart = 0;
	/*�ļ���С*/
	private int mContentLength;
	/*ÿҳ��ʾ������*/
	private int mRows;
	
	private static int mTextSize = 18;
	private int mTextColor = Color.BLACK;

	private String mCharsetName = "gbk";
	
	private Paint mPaint;
	
	private static Context mContext;
	
	private int mChapter = 1;
	/** ÿ������*/
	private int lWordCount;
	/** ÿҳ����*/
	private int pLineCount;
	
	public Integer getAllPage(){
		return mPageCount;
	}
	public BookText(Context context){
		mContext = context;
		mTextSize = GetTxtSize();
		
		mWidth = Utils.getDeviceScreenWidth(mContext);//��Ļ�Ŀ��
		mHeight = Utils.getDeviceScreenHeight(mContext);//��Ļ�ĸ߶�
		
		mContentWidth = mWidth - mMarginHorizontal * 2;//���ݿ��
		mContentHeight = mHeight - mMarginVertical * 2;//30 Ϊ�ܵ��м��,�������м��ĺ�  ���ݵĸ߶�
		//�� 0.8 ��0.9����Ϊÿҳ����������֮��,���п������Ļû�л���
		//mPageWords = (int)(mContentWidth*1.0f / mTextSize + 0.8) * (int)(mContentHeight*1.0f / mTextSize + 0.9);//ÿҳ����
		lWordCount = (mWidth - mMarginHorizontal)/mTextSize;
		pLineCount = (int)(mContentHeight/(mTextSize*1.5f));
		Log.i(TAG,"lWordCount="+lWordCount);
		Log.i(TAG,"pLineCount="+pLineCount);
		mPageWords = lWordCount*pLineCount;//(int)(mContentWidth*1.0f / mTextSize) * (int)(mContentHeight*1.0f / mTextSize);//ÿҳ����
		Log.i(TAG, "mPageWords = " + mPageWords);
		mRows = mContentHeight / mTextSize;
		//mLineSpacing = 30.0f / mRows;
		mLineSpacing = 30.0f / pLineCount;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextSize(mTextSize);
		mPaint.setColor(mTextColor);
		
		SharedPreferences sp = mContext.getSharedPreferences(Constant.BOOK_DB, Context.MODE_WORLD_WRITEABLE);
		mChapter = sp.getInt(Constant.CHAPTER, 1);
		//mPage = sp.getInt("page", 0);
		//Log.i(TAG, "mChapter = " + mChapter);
		mContentLength = TextReader.getContentLength(mContext, mChapter);
		
		mPageCount = (mContentLength % mPageWords)==0 ? mContentLength / mPageWords : mContentLength / mPageWords;
//		if(mPageCount > mPage)
//			mPageCount=1;
		Log.i(TAG, "mPageCount = " + mPageCount);
	}
	
	/**
	 * ��ȡ���ݿ��������С
	 * */
	private static int GetTxtSize(){
		SharedPreferences sp = mContext.getSharedPreferences(Constant.TXTSIZE, Context.MODE_WORLD_WRITEABLE);
		return sp.getInt(Constant.TXTSIZE, 18);
	}
	/**
	 * ��ÿһҳ������
	 * @param canvas
	 * @param page ��0��ʼ����,��0Ϊ��һҳ
	 */
	public void drawText(Canvas canvas,int page){
		setPage(page);
		synchronized(mPaint){
			String txt = null;
			if(mPageWords < mContentLength){
				txt = TextReader.readFragment(mContext, mPage * mPageWords, mPageWords,mChapter);
			}else{
				txt = TextReader.readFragment(mContext, mPage * mPageWords, mContentLength,mChapter);
			}
			int index=0;
			boolean finishFlag=false;
			int currRow=0;//��ǰ��ڼ���
			int currX=0;//��ǰ�ֵĺ�����
			int y = mMarginHorizontal*2;
			while(!finishFlag){
				if (txt != null) {
					if (index < txt.length()) {
						// ȡһ���ַ�
						char c = txt.charAt(index++);
						if (c == '\n') {// ����ǻ���
							currRow++;
							currX = 0;
							y += mTextSize + mLineSpacing;
						} else if ((c <= 'z' && c >= 'a')
								|| (c <= 'Z' && c >= 'A')
								|| (c <= '9' && c >= '0')) {// ��Сд��ĸ����������
							canvas.drawText(c + "", currX + mTextSize / 2, y,mPaint);
							// canvas.drawText(c+"", currX+mTextSize/2,
							// currRow*mTextSize+mTextSize, mPaint);
							currX += mTextSize;
						} else {// ����
							canvas.drawText(c + "", currX + mTextSize / 2, y,mPaint);
							// canvas.drawText(c+"", currX+mTextSize/2,
							// currRow*mTextSize+mTextSize, mPaint);
							currX += mTextSize;
						}
						if (currX >= mContentWidth) {// ����������ʾ���ݵĿ������
							currRow++;
							currX = 0;
							y += mTextSize + mLineSpacing;
						}

					} else {
						finishFlag = true;
						break;
					}

				}else{
					finishFlag = true;
					break;
				}
			}
		}
	}
	
	public int getCurrentPage(){
		return mPage;
	}
	public int getTotalPage(){
		return mPageCount;
	}
	
	public void setPage(int page){
		if(page<=0){
			mPage = 0;
		}else if(page>=mPageCount){
			mPage = mPageCount;
		}else{
			mPage = page;
		}
	}
	public int getWidth(){
		return mWidth;
	}
	public int getHeight(){
		return mHeight;
	}
	public int getChapter(){
		return mChapter;
	}
}
