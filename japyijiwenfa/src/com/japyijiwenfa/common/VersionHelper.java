package com.japyijiwenfa.common;


import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.japyijiwenfa.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class VersionHelper {
	public static final String TAG="VersionHelper";
	public static final String UPDATE_SERVER = "http://app.localinfos.net/";//http://download.qidian.com/apk/
	public static final String UPDATE_APKNAME = "japyijiwenfa.apk";
	public static final String UPDATE_VERJSON = "japyijiwenfa.html";
	public static final String UPDATE_SAVENAME = "japyijiwenfa.apk";
	
	LocationManager locationManager;
	/**
	 * 获取版本号Code
	 * */
	public static int getVersionCode(Context context){
		int vCode=-1;
		try{
			vCode = context.getPackageManager().getPackageInfo(context.getString(R.string.pagename), 0).versionCode;  
		}catch (Exception e) {
			// TODO: handle exception
			Log.v(TAG,e.getMessage());
		}
		return vCode;
	}
	/**
	 * 获取版本号Name
	 * */
	public static String getVersionName(Context context){
		String vName="";
		try {
			vName = context.getPackageManager().getPackageInfo(context.getString(R.string.pagename), 0).versionName;
		} catch (Exception e) {
			// TODO: handle exception
			Log.v(TAG,e.getMessage());
		}
		return vName;
	}
	
	/**
	 * 获取应用程序名称
	 * */
	public static String getAppName(Context context) {
        String verName = context.getResources()
        .getText(R.string.app_name).toString();
        return verName;
	}
	
	public static byte[] LoadAsset(Context ctx, String path) {
		InputStream is;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] result = null;
		try {
			is = ctx.getResources().getAssets().open(path);
			byte[] charBuffer = new byte[1024 * 8];
			int readsize = 0;
			while ((readsize = is.read(charBuffer)) > 0) {
				output.write(charBuffer, 0, readsize);

			}
			output.flush();
			result = output.toByteArray();
			is.close();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 给服务端统计设备
	 * */
//	public void AddDeviceInfo(Context context) {
//		if (!NetWorkTool.isNetworkAvailable(context)) {
//			return;
//		}
//		if (locationManager == null) {
//			locationManager = (LocationManager) context
//					.getSystemService(Context.LOCATION_SERVICE);
//		}
//		double[] result = new double[2];
//		result[0] = 0;
//		result[1] = 1;
//		try {
//			Location location = locationManager
//					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//			if (location != null) {
//				result[0] = location.getLatitude();
//				result[1] = location.getLongitude();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		JsApi api = new JsApi(context);
//		String key = api.CurrentIMEI() + "|" + result[0] + "|" + result[1] + "|"
//				+ api.CurrentVersion() + "|" + api.CurrentWidth() + "|"
//				+ api.CurrentHeight() + "|" + new String(this.LoadAsset(context,"")) + "|" + api.CurrentSysVersion()
//				+ "|1|" + a;
//		key = Base64.encode(key.getBytes());
//		// key = android.util.Base64.encodeToString(key.getBytes(),
//		// android.util.Base64.DEFAULT);
//		String strURL = Consts.UrlR1 + "?r=" + key;
//		try {
//			// 模拟器测试时，请使用外网地址
//			URL url = new URL(strURL);
//			URLConnection con = url.openConnection();
//			HttpURLConnection httpconn = (HttpURLConnection) con;
//			httpconn.setConnectTimeout(3 * 1000);
//			httpconn.setReadTimeout(3 * 1000);
//			httpconn.setRequestProperty("Cookie", CookieAdapter.GetCookies());
//			int responseCode = httpconn.getResponseCode();
//			if (responseCode != 200)
//				return;
//			// HttpURLConnection.HTTP_OK
//			InputStream is = con.getInputStream();
//			StringBuilder builder = new StringBuilder();
//			BufferedReader bufferedReader = new BufferedReader(
//					new InputStreamReader(is));
//			for (String s = bufferedReader.readLine(); s != null; s = bufferedReader
//					.readLine()) {
//				builder.append(s);
//			}
//			is.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			ctx.GetAdapter().updateUserSetting(Consts.SettingIsAddDeviceInfo,
//					"1");
//		}
//	} 
}