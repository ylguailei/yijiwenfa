package com.japyijiwenfa.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public final class NetWorkTool {
	private final static String TAG="NetWorkTool";
	
	/**
	 * 判断是否能联网
	 * */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // 注意，这个判断一定要的哦，要不然会出错
			return networkInfo.isAvailable();
		}
		return false;
	}
	
	/**
	 * Http请求获取数据(Post方式)
	 * @param url
	 * @param arry
	 * @return
	 */
	public static String PostHttpRequest(String url) {
		try {
			String httpUrl = url;
			// HttpPost连接对象
			HttpPost httpRequest = new HttpPost(httpUrl);
			// 添加要传递的参数
			ArrayList<NameValuePair> arryArrayList = new ArrayList<NameValuePair>();
			arryArrayList.add(new BasicNameValuePair("1", "1"));
			// params.add(new BasicNameValuePair("par",
			// "HttpClient_android_Post"));
			// 设置字符集
			HttpEntity httpentity = new UrlEncodedFormEntity(arryArrayList, "gb2312");
			// 请求httpRequest
			httpRequest.setEntity(httpentity);
			// 取得默认的HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// 取得HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// HttpStatus.SC_OK表示连接成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				return strResult;
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v(TAG, e.getMessage());
		}
		return "";
	}
	
	/**
     * 获取网址内容
     * @param url
     * @return
     * @throws Exception
     */
     public static String getContent(String url) throws Exception{
         StringBuilder sb = new StringBuilder();
         
         HttpClient client = new DefaultHttpClient();
         HttpParams httpParams = client.getParams();
         //设置网络超时参数
         HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
         HttpConnectionParams.setSoTimeout(httpParams, 5000);
         HttpResponse response = client.execute(new HttpGet(url));
         HttpEntity entity = response.getEntity();
         if (entity != null) {
//             BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"), 8192);//UTF-8
//             
//             String line = null;
//             while ((line = reader.readLine())!= null){
//                 sb.append(line);
//             }
//             reader.close();
        	 String strResult = EntityUtils.toString(entity);
        	 sb.append(strResult);
         }
         return sb.toString();
     } 
     
     /**
 	 * Http请求获取数据(GET方式)
 	 * @param url
 	 * @return
 	 */
 	public static String GetHttpRequest(String url) {
 		try {
 			String httpUrl = url;
 			// HttpGet连接对象
 			HttpGet httpRequest = new HttpGet(httpUrl);
 			// 取得HttpClient对象
 			HttpClient httpclient = new DefaultHttpClient();
 			// 请求HttpClient，取得HttpResponse
 			HttpResponse httpResponse = httpclient.execute(httpRequest);
 			// 请求成功
 			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
 				// 取得返回的字符串
 				String strResult = EntityUtils.toString(httpResponse.getEntity());
 				return strResult;
 			}
 		} catch (Exception e) {
 			// TODO: handle exception
 			Log.v(TAG,e.getMessage());
 			//e.getStackTrace();
 		}
 		return "";
 	}
}