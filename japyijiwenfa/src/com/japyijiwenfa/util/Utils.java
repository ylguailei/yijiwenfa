package com.japyijiwenfa.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class Utils {
	public static int getDeviceScreenWidth(Context context){
    	DisplayMetrics dm = context.getResources().getDisplayMetrics();
    	return dm.widthPixels;
    }
    public static int getDeviceScreenHeight(Context context){
    	DisplayMetrics dm = context.getResources().getDisplayMetrics();
    	return dm.heightPixels;
    }
    /**
     * ÉèÖÃÆÁÄ»È«ÆÁ
     * @param context
     */
    public static void setFullScreen(Activity context){
    	context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    			WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        context.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
