package com.japyijiwenfa.view;


import com.japyijiwenfa.R;
import com.japyijiwenfa.BookText;
import com.japyijiwenfa.util.Constant;
import com.japyijiwenfa.util.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class BookViewFlipper extends ViewFlipper {
	private final static String TAG = "BookViewFlipper";

	private BookView mView1;
	private BookView mView2;
	private BookView mView3;

	private BookText mBookText;
	private int mPage = 0;
	private int mChapter;
	public BookViewFlipper(Context context) {
		super(context);
		init();
	}

	public BookViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public BookViewFlipper(Context context, AttributeSet attrs,int txtSize) {
		super(context, attrs);
		init(txtSize);
	}

	private void init() {
		mView1 = new BookView(getContext());
		mView2 = new BookView(getContext());
		mView3 = new BookView(getContext());

		addView(mView1);
		addView(mView2);
		addView(mView3);

		mBookText = new BookText(getContext());
		mPage = getPage();
		if(mPage > mBookText.getAllPage()){
			mPage=0;
			savePage();
		}
			
		setBookText();
	}
	
	private void init(int txtSize) {
		mView1 = new BookView(getContext());
		mView2 = new BookView(getContext());
		mView3 = new BookView(getContext());

		addView(mView1);
		addView(mView2);
		addView(mView3);

		mBookText = new BookText(getContext());
		mPage = getPage();
		if(mPage > mBookText.getAllPage()){
			mPage=0;
			savePage();
		}
		setBookText();
	}
	
	private int getPage(){
		SharedPreferences sp = getContext().getSharedPreferences(Constant.BOOK_DB, Context.MODE_WORLD_WRITEABLE);
		mChapter = mBookText.getChapter();
		return sp.getInt(Constant.PAGE+mChapter, 0);
		
	}
	private void setBookText() {
		//Log.i(TAG, "setBookText ");
		switch (mPage % 3) {
		case 0:
			Log.i(TAG, "mPage = " + mPage + ",i = 0");
			if(mPage==0){
				mView1.drawText(mBookText, mPage);// 当前页文字
				mView2.drawText(mBookText, mPage + 1);// 第二页文字
				mView3.drawText(mBookText, mPage + 2);// 第三页文字
			}else{
				mView1.drawText(mBookText, mPage);// 当前页文字
				mView2.drawText(mBookText, mPage + 1);// 第二页文字
				mView3.drawText(mBookText, mPage - 1);// 第三页文字
			}
			return;
		case 1:
			Log.i(TAG, "mPage = " + mPage + ",i = 1");
			mView1.drawText(mBookText, mPage - 1);// 前一页文字
			mView2.drawText(mBookText, mPage);// 当前页
			mView3.drawText(mBookText, mPage + 1);// 第二页文字
			return;
		case 2:
			Log.i(TAG, "mPage = " + mPage + ",i = 2");
			mView1.drawText(mBookText, mPage + 1);// 前两页文字
			mView2.drawText(mBookText, mPage - 1);// 前一页文字
			mView3.drawText(mBookText, mPage);// 当前页文字
		}
	}

	@Override
	public void showNext() {
		if(!isLastPage()){
			setInAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_right_in));
			setOutAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_left_out));
			super.showNext();
			mPage++;
			savePage();
			setBookText();
			Log.i(TAG, " showNext mPage = " + mPage);
		}
	}

	@Override
	public void showPrevious() {
		if (!isFirstPage()) {
			setInAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_left_in));
			setOutAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.push_right_out));
			super.showPrevious();
			mPage--;
			savePage();
			setBookText();
			Log.i(TAG, " showPrevious mPage = " + mPage);
		}
	}
	//将当前页写入数据库
	public void savePage(){
		SharedPreferences sp = getContext().getSharedPreferences(Constant.BOOK_DB, Context.MODE_WORLD_WRITEABLE);
		sp.edit().putInt(Constant.PAGE+mChapter, mPage).commit();
	}
	
	
	
	public boolean isLastPage() {
		if(mPage>=mBookText.getTotalPage()){
			mPage = mBookText.getTotalPage();
			return true;
		}
		return false;
	}

	public boolean isFirstPage() {
		if(mPage<=0){
			mPage=0;
			return true;
		}
		return false;
	}
	public int getCurrentPage(){
		isLastPage();
		isFirstPage();
		return mPage;
	}
}