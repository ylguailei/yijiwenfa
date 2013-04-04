package com.japyijiwenfa;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.japyijiwenfa.R;
import com.japyijiwenfa.adapter.CatalogAdapter;
import com.japyijiwenfa.common.UpdateHelper;
import com.japyijiwenfa.manager.MenuManager;
import com.japyijiwenfa.util.Constant;
import com.japyijiwenfa.util.TextReader;
import com.japyijiwenfa.util.Utils;
import com.japyijiwenfa.view.BookViewFlipper;
import com.japyijiwenfa.view.HelpWinReader;
import com.japyijiwenfa.view.KanShuDialog;
import com.japyijiwenfa.view.Loading;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class JapyijiwenfaActivity extends Activity implements OnItemClickListener,
		GestureDetector.OnGestureListener
{
	private static final String TAG = "JinpinmeiActivity";

	public class WhatMessage
	{
		public static final int LOADING_VIEW = 1;
		public static final int MAIN_VIEW = 2;
		public static final int CATALOG_LIST_VIEW = 3;
		public static final int BOOK_VIEW = 4;
		public static final int ABOUT_VIEW = 5;
		public static final int POSTSCRIPT_VIEW = 6;
	}

	// 表示各个界面的安全类型枚举
	public enum Views
	{
		LOADING_VIEW, MAIN_VIEW, CATALOG_LIST_VIEW, BOOK_VIEW, ABOUT_VIEW, POSTSCRIPT_VIEW
	}

	private Queue<Views> mQueue = new LinkedList<Views>();
	// 各个view的缓存
	private Loading mLoading;
	private View mMainView;
	private View mCatalogView;
	private View mBookView;
	private View mAboutView;
	private View mPostscriptView;
	// 当前显示的view
	private Views mCurrView;
	// MainView的控件引用
	private Button mCatalogBtn;
	private Button mStartReadingBtn;
	private Button mBookmarkBtn;
	private Button mAboutBtn;
	private KanShuDialog mDialog;
	// Catalog listView的控件
	private ListView mListView;
	private Button mBackBtn;
	private TextView mBookName;

	private CatalogAdapter mCatalogAdapter;
	// BookView 中的控件
	private BookViewFlipper mViewFlipper;
	private GestureDetector mDetector;
	private SharedPreferences msp;
	// Postscript View的空间
	private TextView mTextView;
	private Button mBackToMain2;
	// about View
	private Button mBackToMain;
	AdView adView;
	private Handler checkUpdateHandler = new Handler();
	MenuManager menu;
	/**
	 * 添加google广告
	 * */
	private void AddGoogleAd(){
		adView = new AdView(JapyijiwenfaActivity.this, AdSize.BANNER,
				"a14f292df2c0535");
		LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayout01);
		layout.addView(adView);
		// getListView().addHeaderView(layout);
		adView.loadAd(new AdRequest());
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Utils.setFullScreen(this);
		// 先进入loading界面
		//sendMessage(WhatMessage.LOADING_VIEW);
		sendMessage(WhatMessage.LOADING_VIEW);
		
		// ===============此处为自动检查更新部分==================
				checkUpdateHandler.post(new Runnable()
				{
					@Override
					public void run()
					{
						// TODO Auto-generated method stub
						if (UpdateHelper.CheckNewVersion(JapyijiwenfaActivity.this))
						{
							UpdateHelper.ExcuteUpdateVersion(JapyijiwenfaActivity.this);
						}
					}
				});

		// ======================================================

	}

	public void sendMessage(int what)
	{
		Message msg = mHandler.obtainMessage(what);
		mHandler.sendMessage(msg);
	}

	public void sendMessage(int what, int chapter)
	{
		Message msg = mHandler.obtainMessage(what, chapter);
		mHandler.sendMessage(msg);
	}

	// 处理各个View传来的消息
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case WhatMessage.LOADING_VIEW:
				startLoading();
				break;
			case WhatMessage.MAIN_VIEW:
				mainView();
				break;
			case WhatMessage.CATALOG_LIST_VIEW:
				catalogListView();
				break;
			case WhatMessage.BOOK_VIEW:
				bookView(true);
				break;
			case WhatMessage.ABOUT_VIEW:
				aboutView();
				break;
			case WhatMessage.POSTSCRIPT_VIEW:
				postscriptView();
				break;
			}
		}

	};

	/**
	 * 开始的时候loading两张图片
	 * */
	private void startLoading()
	{
		if (mLoading == null)
		{
			mLoading = new Loading(this);
		}
		setContentView(mLoading);

		mCurrView = Views.LOADING_VIEW;
		mQueue.add(mCurrView);
	};

	/**
	 * 首页
	 * */
	private void mainView()
	{
		if (mMainView == null)
		{
			mMainView = getLayoutInflater().inflate(R.layout.main, null);
		}

		if (mCatalogBtn == null)
		{
			mCatalogBtn = (Button) mMainView.findViewById(R.id.catalog_btn);
			mCatalogBtn.setOnClickListener(btnListener);
		}
		if (mStartReadingBtn == null)
		{
			mStartReadingBtn = (Button) mMainView
					.findViewById(R.id.start_reading_btn);
			mStartReadingBtn.setOnClickListener(btnListener);
		}
		// if(mBookmarkBtn==null){
		// mBookmarkBtn = (Button)mMainView.findViewById(R.id.bookmark_btn);
		// mBookmarkBtn.setOnClickListener(btnListener);
		// }
		// if(mAboutBtn==null){
		// mAboutBtn = (Button)mMainView.findViewById(R.id.about_btn);
		// mAboutBtn.setOnClickListener(btnListener);
		// }

		setContentView(mMainView);

		mCurrView = Views.MAIN_VIEW;
		mQueue.add(mCurrView);
		

		AddGoogleAd();
	}

	private View.OnClickListener btnListener = new View.OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			switch (v.getId())
			{
			case R.id.catalog_btn:
				sendMessage(WhatMessage.CATALOG_LIST_VIEW);
				break;
			case R.id.start_reading_btn:
				sendMessage(WhatMessage.BOOK_VIEW);
				break;
			// case R.id.bookmark_btn:
			// sendMessage(WhatMessage.POSTSCRIPT_VIEW);
			// break;
			// case R.id.about_btn:
			// sendMessage(WhatMessage.ABOUT_VIEW);
			// break;
			case R.id.back_main_view:
			case R.id.back_btn:
			case R.id.back_btn2:
				sendMessage(WhatMessage.MAIN_VIEW);
				break;
			}
		}
	};

	/**
	 * 书籍目录列表
	 * */
	private void catalogListView()
	{
		if (mCatalogView == null)
		{
			mCatalogView = getLayoutInflater().inflate(R.layout.catalog_list,null);
		}
		if (mBackBtn == null)
		{
			mBackBtn = (Button) mCatalogView.findViewById(R.id.back_main_view);
			mBackBtn.setOnClickListener(btnListener);
		}
		if (mBookName == null)
		{
			mBookName = (TextView) mCatalogView.findViewById(R.id.book_name);
		}
		if (mListView == null)
		{
			mListView = (ListView) mCatalogView
					.findViewById(R.id.catalog_listview);
			mListView.setOnItemClickListener(this);
		}
		if (mCatalogAdapter == null)
		{
			List<String> list = TextReader.readCatalog(this);
			mCatalogAdapter = new CatalogAdapter(this, list);
		}
		mListView.setAdapter(mCatalogAdapter);
		setContentView(mCatalogView);

		mCurrView = Views.CATALOG_LIST_VIEW;
		mQueue.add(mCurrView);

		AddGoogleAd();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id)
	{
		sendMessage(WhatMessage.BOOK_VIEW);
		SharedPreferences sp = getSharedPreferences(Constant.BOOK_DB,Context.MODE_WORLD_WRITEABLE);
		int chapter = position + 1;
		// 本书上册 章节有50章
		if (chapter > 0 && chapter <= 215)
		{
			sp.edit().putInt(Constant.CHAPTER, position + 1).commit();
		} else
		{
			sp.edit().putInt(Constant.CHAPTER, 1).commit();
		}
		sp.edit().putInt(Constant.PAGE+chapter, 0).commit();
	}

	
	int ScreenHeight;
	int ScreenWidth;
	
	/**
	 * 书籍浏览页面
	 * */
	private void bookView(Boolean isShow)
	{
		 //if(mBookView == null)
		mBookView = getLayoutInflater().inflate(R.layout.book_view_layout, null);
		// if(mViewFlipper == null)
		mViewFlipper = (BookViewFlipper) mBookView.findViewById(R.id.viewFlipper);

		if (mDetector == null)
		{
			mDetector = new GestureDetector(this);
		}
		setContentView(mBookView);

		menu = new MenuManager(JapyijiwenfaActivity.this, mBookView);
		mCurrView = Views.BOOK_VIEW;
		mQueue.add(mCurrView);
		
		//mBookView.setOnCreateContextMenuListener(viewList);
		
		if(isShow){
			ScreenHeight = getWindowManager().getDefaultDisplay().getHeight();
			ScreenWidth = getWindowManager().getDefaultDisplay().getWidth();
			initReadHelp();
			if(IsShowHelp())
				ShowHelp();
		}
		AddGoogleAd();
	}
	
//	OnCreateContextMenuListener viewList = new OnCreateContextMenuListener()
//	{
//		
//		@Override
//		public void onCreateContextMenu(ContextMenu menu, View v,
//				ContextMenuInfo menuInfo)
//		{
//			// TODO Auto-generated method stub
//			if(v == mBookView)
//			menu.add(0, 1, 1, "字体大小").setIcon(R.drawable.icon_fontsize);
//		}
//		
//		
//	};
	
	public void SetFont(int size){
		//menu.ShowFont(size);
		bookView(false);
		//mBookView.postInvalidate();
	}
	
	PopupWindow helpWin;
	HelpWinReader readHelp;
	
	public void ShowHelp() {
		menu.CloseAllWin();
		if (IsShowHelp()) {
			mBookView.post(new Runnable() {
				@Override
				public void run() {
					helpWin.showAtLocation(mBookView, Gravity.CENTER| Gravity.BOTTOM, 0, 0);
				}
			});
		}
	}

	public void initReadHelp() {
		if (readHelp == null) {
			readHelp = new HelpWinReader(this);
			helpWin = new PopupWindow(readHelp, this.ScreenWidth,
					this.ScreenHeight);
			helpWin.setFocusable(true);
			helpWin.setBackgroundDrawable(new BitmapDrawable());
			helpWin.setOutsideTouchable(false);
			helpWin.setTouchInterceptor(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					int x = (int) event.getX();
					int y = (int) event.getY();
					Rect rect = new Rect();
					readHelp.help2_close.getHitRect(rect);
					// Rect rect = new Rect(ScreenWidth / 3, ScreenHeight / 3,
					// ScreenWidth,
					// ScreenHeight / 3 * 2);
					if (rect.contains(x, y)) {
						return false;
					} else {
						helpWin.dismiss();
					}
					return false;
				}
			});
		}
	}
	
	/**
	 * 是否显示帮助页面
	 * */
	private Boolean IsShowHelp(){
		SharedPreferences sp = getSharedPreferences(Constant.HELP,Context.MODE_WORLD_WRITEABLE);
		return sp.getBoolean(Constant.HELP, true);
	}

	public void CloseHelp() {
		if (helpWin != null && helpWin.isShowing()) {
			helpWin.dismiss();
		}
	}

	/**
	 * 关于
	 * */
	private void aboutView()
	{
		if (mAboutView == null)
		{
			mAboutView = getLayoutInflater().inflate(R.layout.about, null);
		}
		if (mBackToMain == null)
		{
			mBackToMain = (Button) mAboutView.findViewById(R.id.back_btn);
		}
		mBackToMain.setOnClickListener(btnListener);
		setContentView(mAboutView);
		mCurrView = Views.ABOUT_VIEW;
	}

	/**
	 * 序跋
	 */
	private void postscriptView()
	{
		if (mPostscriptView == null)
		{
			mPostscriptView = View.inflate(this, R.layout.postscript, null);
		}
		if (mTextView == null)
		{
			mTextView = (TextView) mPostscriptView
					.findViewById(R.id.postscript);
		}
		if (mBackToMain2 == null)
		{
			mBackToMain2 = (Button) mPostscriptView
					.findViewById(R.id.back_btn2);
		}
		mBackToMain2.setOnClickListener(btnListener);
		String postscript = TextReader.readPostscript(this);
		mTextView.setText(postscript);

		setContentView(mPostscriptView);

		mCurrView = Views.POSTSCRIPT_VIEW;

		AddGoogleAd();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		Log.i(TAG, "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			mQueue.poll();
			Views v = mQueue.poll();
			/*
			 * if(v==Views.BOOK_VIEW){
			 * 
			 * sendMessage(WhatMessage.BOOK_VIEW); return true; }else if(v ==
			 * Views.CATALOG_LIST_VIEW){
			 * 
			 * sendMessage(WhatMessage.CATALOG_LIST_VIEW); return true;
			 * 
			 * }else if(v == Views.MAIN_VIEW){
			 * 
			 * sendMessage(WhatMessage.MAIN_VIEW); return true;
			 * 
			 * }else{ return true; }
			 */
			if (mCurrView == Views.MAIN_VIEW)
			{
				exitApp();
			} else
			{

				sendMessage(WhatMessage.MAIN_VIEW);
			}
			return true;

		}
		return super.onKeyDown(keyCode, event);
		//return true;
	}

	/*
	 * 弹出Alert框_Yes or No
	 */
	public static void AlertDialog_YesorNo(Context context, String title,
			String content, String Ok_Msg, String Cancel_Msg,
			OnClickListener finishListener)
	{
		new AlertDialog.Builder(context)
				.setTitle(title)
				.setMessage(content)
				.setPositiveButton(Ok_Msg, finishListener)
				.setNegativeButton(Cancel_Msg,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int which)
							{
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
	}

	
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu)
//	{
//		// TODO Auto-generated method stub
//		if(mCurrView == Views.BOOK_VIEW){
//			menu.add(0, 1, 1, "字体大小").setIcon(R.drawable.icon_fontsize);
//		}
//		return true;
//	}
	
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// TODO Auto-generated method stub
		if(mCurrView == Views.BOOK_VIEW){
			menu.clear();
			menu.add(0, 1, 1, "フォントサイズセッティング").setIcon(R.drawable.icon_fontsize);
		}
		else
			menu.clear();
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * 获取当前值
	 * */
	private int GetSize(){
		SharedPreferences sp = this.getSharedPreferences(Constant.TXTSIZE, Context.MODE_WORLD_WRITEABLE);
		//sp.edit().putInt(Constant.TXTSIZE, size).commit();
		return sp.getInt(Constant.TXTSIZE,22);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// TODO Auto-generated method stub
		if(mCurrView == Views.BOOK_VIEW){
			switch (item.getItemId())
			{
				case 1://调整字体大小
					menu.ShowFont(GetSize());
					//bookView();
					break;
				default:
					break;
			}
		}
		return super.onOptionsItemSelected(item);
		//return false;
	}

//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//
//		int keyCode = event.getKeyCode();
//		if (keyCode == KeyEvent.KEYCODE_MENU) {
//			if (event.getAction() == KeyEvent.ACTION_UP)
//				return true;
//			menu.CloseAllWin();
//			return true;
//		}
//		return false;
//	}
	
	/**
	 * 退出程序
	 * */
	private void exitApp()
	{
		
		AlertDialog_YesorNo(this, getString(R.string.exit_dialog_title),
				getString(R.string.exit_dialog_content),
				getString(R.string.ok_btn), getString(R.string.cancel_btn),
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// TODO Auto-generated method stub
						finish();
					}
				});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (mCurrView == Views.BOOK_VIEW)
		{
			return mDetector.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e)
	{
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY)
	{
		if (mCurrView == Views.BOOK_VIEW)
		{
			if (e1.getX() > e2.getX())
			{
				if (mViewFlipper.isLastPage())
				{
					SharedPreferences sp = getSharedPreferences(Constant.BOOK_DB, Context.MODE_WORLD_WRITEABLE);

					int chapter = sp.getInt(Constant.CHAPTER, 1);
					if (chapter >= 215)
					{
						Toast.makeText(this, R.string.is_last_page,Toast.LENGTH_SHORT).show();
					} else
					{
						sp.edit().putInt(Constant.CHAPTER, chapter + 1).commit();
						sendMessage(WhatMessage.BOOK_VIEW);
					}
				} else
				{
					mViewFlipper.showNext();
				}
			} else if (e1.getX() < e2.getX()){
				if (mViewFlipper.isFirstPage())
				{
					SharedPreferences sp = getSharedPreferences(Constant.BOOK_DB, Context.MODE_WORLD_WRITEABLE);
					int chapter = sp.getInt(Constant.CHAPTER, 1);
					if (chapter <= 1)
					{
						Toast.makeText(this, R.string.is_first_page,Toast.LENGTH_SHORT).show();

					} else
					{
						sp.edit().putInt(Constant.CHAPTER, chapter - 1).commit();
						sendMessage(WhatMessage.BOOK_VIEW);
					}
				} else
				{
					mViewFlipper.showPrevious();
				}
			} else
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e)
	{

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e)
	{

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e)
	{
		return false;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		destroyLast();
	}

	private void destroyLast()
	{
		mLoading = null;
		mMainView = null;
		mCatalogView = null;
		mBookView = null;
		mAboutView = null;
		mPostscriptView = null;

		mCatalogBtn = null;
		mStartReadingBtn = null;
		mBookmarkBtn = null;
		mAboutBtn = null;
		mDialog = null;

		mCatalogView = null;
		mListView = null;
		mBackBtn = null;
		mBookName = null;

		mViewFlipper = null;
		mDetector = null;
		msp = null;

		mCatalogAdapter = null;
		// BookView 中的控件
		mViewFlipper = null;
		mDetector = null;
		msp = null;
		// Postscript View的空间
		mTextView = null;
		mBackToMain2 = null;
		// about View
		mBackToMain = null;

	}
}
