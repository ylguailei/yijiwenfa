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
	//文本间距-英文
	private static int TEXT_SPACE_BETWEEN_EN = 8;
	//文本间距-中文
	private static int TEXT_SPACE_BETWEEN_CN = 16;
	/* screen width */
	private int mWidth;
	/* screen height */
	private int mHeight;
	/* 内容的宽,不包括行间距 */
	private int mContentWidth;
	/* 内容的高,不包括行间距 */
	private int mContentHeight;
	/* 上下边缘 */
	private int mMarginVertical = 15;
	/* 左右边缘 */
	private int mMarginHorizontal = 20;
	/**每页内容字数*/
	private int mPageWords;
	private int mPage;
	public int mPageCount;
	//行间距
	private float mLineSpacing;
	/*当前位置,读取字符时从此位置开始读取*/
	private int mNextPageStart = 0;
	/*文件大小*/
	private int mContentLength;
	/*每页显示的行数*/
	private int mRows;
	
	private static int mTextSize = 18;
	private int mTextColor = Color.BLACK;

	private String mCharsetName = "gbk";
	
	private Paint mPaint;
	
	private static Context mContext;
	
	private int mChapter = 1;
	/** 每行字数*/
	private int lWordCount;
	/** 每页行数*/
	private int pLineCount;
	
	public Integer getAllPage(){
		return mPageCount;
	}
	public BookText(Context context){
		mContext = context;
		mTextSize = GetTxtSize();
		
		mWidth = Utils.getDeviceScreenWidth(mContext);//屏幕的宽度
		mHeight = Utils.getDeviceScreenHeight(mContext);//屏幕的高度
		
		mContentWidth = mWidth - mMarginHorizontal * 2;//内容宽度
		mContentHeight = mHeight - mMarginVertical * 2;//30 为总的行间距,即所有行间距的和  内容的高度
		//加 0.8 和0.9是因为每页的字数绘完之后,还有空余的屏幕没有绘字
		//mPageWords = (int)(mContentWidth*1.0f / mTextSize + 0.8) * (int)(mContentHeight*1.0f / mTextSize + 0.9);//每页字数
		lWordCount = (mWidth - mMarginHorizontal)/mTextSize;
		pLineCount = (int)(mContentHeight/(mTextSize*1.5f));
		Log.i(TAG,"lWordCount="+lWordCount);
		Log.i(TAG,"pLineCount="+pLineCount);
		mPageWords = lWordCount*pLineCount;//(int)(mContentWidth*1.0f / mTextSize) * (int)(mContentHeight*1.0f / mTextSize);//每页字数
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
	 * 获取数据库中字体大小
	 * */
	private static int GetTxtSize(){
		SharedPreferences sp = mContext.getSharedPreferences(Constant.TXTSIZE, Context.MODE_WORLD_WRITEABLE);
		return sp.getInt(Constant.TXTSIZE, 18);
	}
	/**
	 * 绘每一页的文字
	 * @param canvas
	 * @param page 从0开始计数,即0为第一页
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
			int currRow=0;//当前绘第几行
			int currX=0;//当前字的横坐标
			int y = mMarginHorizontal*2;
			while(!finishFlag){
				if (txt != null) {
					if (index < txt.length()) {
						// 取一个字符
						char c = txt.charAt(index++);
						if (c == '\n') {// 如果是换行
							currRow++;
							currX = 0;
							y += mTextSize + mLineSpacing;
						} else if ((c <= 'z' && c >= 'a')
								|| (c <= 'Z' && c >= 'A')
								|| (c <= '9' && c >= '0')) {// 大小写字母或者是数字
							canvas.drawText(c + "", currX + mTextSize / 2, y,mPaint);
							// canvas.drawText(c+"", currX+mTextSize/2,
							// currRow*mTextSize+mTextSize, mPaint);
							currX += mTextSize;
						} else {// 中文
							canvas.drawText(c + "", currX + mTextSize / 2, y,mPaint);
							// canvas.drawText(c+"", currX+mTextSize/2,
							// currRow*mTextSize+mTextSize, mPaint);
							currX += mTextSize;
						}
						if (currX >= mContentWidth) {// 若超出可显示内容的宽度则换行
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
