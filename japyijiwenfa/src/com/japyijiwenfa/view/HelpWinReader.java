package com.japyijiwenfa.view;

import com.japyijiwenfa.R;
import com.japyijiwenfa.JapyijiwenfaActivity;
import com.japyijiwenfa.util.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class HelpWinReader extends LinearLayout {
	
	public ImageView help2_close;
	JapyijiwenfaActivity ctx;

	public HelpWinReader(Context context) {
		super(context);
		ctx = (JapyijiwenfaActivity)context;
		RelativeLayout.LayoutParams thisParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		this.setLayoutParams(thisParams);
		
		final LayoutInflater inflater = LayoutInflater.from(context);

		// 初始化当前LinearLayout
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.viewhelp, null);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		this.addView(layout, layoutParams);
		
		help2_close = (ImageView) layout.findViewById(R.id.help2_close);
		help2_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sp = ctx.getSharedPreferences(Constant.HELP,Context.MODE_WORLD_WRITEABLE);
				sp.edit().putBoolean(Constant.HELP, false).commit();
				ctx.CloseHelp();
			}
		});
	}
}
