package com.japyijiwenfa.view;


import com.japyijiwenfa.BookText;
import com.japyijiwenfa.util.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BookView extends View {
	private static final String TAG = "BookView";
	
	private BookText mBookText;
	private int mPage;
	
	public BookView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();

	}

	public BookView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setDrawingCacheEnabled(true);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		SharedPreferences sp = getContext().getSharedPreferences(Constant.BOOK_DB, Context.MODE_WORLD_WRITEABLE);
		int mChapter = mBookText.getChapter();
		int curPage = sp.getInt(Constant.PAGE+mChapter, 0);
		if(curPage == (mPage+1)){
			//Log.i(TAG, "mPage="+mPage);
			//mBookText.drawText(canvas,mPage);
			//标题
			//页数
			//canvas.drawText(("第"+mBookText.getChapter()+"回  " )+(mPage+1)+"/"+(mBookText.getTotalPage()+1), mBookText.getWidth()-75, mBookText.getHeight()-55, new Paint());
			mPage = curPage;
		}
		else if(curPage > (mPage+1)){
			mPage = 0;
			//SharedPreferences sp = mContext.getSharedPreferences(Constant.BOOK_DB, Context.MODE_WORLD_WRITEABLE);
			sp.edit().putInt(Constant.CHAPTER, 1).commit();
			sp.edit().putInt(Constant.PAGE+mChapter, 0).commit();
		}
		mBookText.drawText(canvas,mPage);
		//标题
		//页数
		canvas.drawText(("第"+mBookText.getChapter()+"回  " )+(mPage+1)+"/"+(mBookText.getTotalPage()+1), mBookText.getWidth()-75, mBookText.getHeight()-65, new Paint());
	}
	public void drawText(BookText bookText,int page){
		mBookText = bookText;
		mPage = page;
		postInvalidate();
	}
	
}
