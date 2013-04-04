package com.japyijiwenfa.manager;


import java.util.ArrayList;

import com.japyijiwenfa.R;
import com.japyijiwenfa.JapyijiwenfaActivity;

import android.app.Activity;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class MenuManager
{
	Activity mActivity;
	View parent;
	public int top;

	public MenuManager(Activity activity, View parentView) {
		mActivity = activity;
		parent = parentView;
	}

	public void SetParentView(View parentView) {
		parent = parentView;
	}

	ArrayList<PopupWindow> winList = new ArrayList<PopupWindow>();

	private PopupWindow createWin(final View subview) {
		if (mActivity instanceof JapyijiwenfaActivity) {
			Rect rect = new Rect();
			mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
			top = rect.top;
		}

		int height = parent.getHeight();
		height += top;
		final PopupWindow win = new PopupWindow(subview, parent.getWidth(),height);
		win.setFocusable(true);
		win.setBackgroundDrawable(new BitmapDrawable());
		win.setOutsideTouchable(false);
		win.setTouchInterceptor(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				LinearLayout layout = (LinearLayout) subview;
				int height = layout.getChildAt(0).getHeight();
				if (event.getY() < parent.getHeight() - height) {
					if (win.isShowing())
						win.dismiss();
				}
				return false;
			}
		});
		winList.add(win);
		return win;
	}

	public void CloseAllWin() {
		for (int i = 0; i < winList.size(); i++) {
			PopupWindow win = winList.get(i);
			if (win.isShowing()) {
				win.dismiss();
			}
		}
		winList.clear();
	}
	
	PopupWindow fontWin;
	FontSizeMenu fontSizeMenu;

	public void ShowFont(int size) {
		CloseAllWin();
		mActivity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		if (fontWin == null || fontSizeMenu == null) {
			fontSizeMenu = new FontSizeMenu(mActivity);
		}
		fontWin = createWin(fontSizeMenu);
		fontWin.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			}
		});
		fontSizeMenu.SetSize(size);
		fontWin.setAnimationStyle(R.style.PopupAnimationMove);
		fontWin.showAtLocation(parent, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
		fontWin.update();
	}
}
