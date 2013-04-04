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
	 * �ж��Ƿ�������
	 * */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager conManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
		if (networkInfo != null) { // ע�⣬����ж�һ��Ҫ��Ŷ��Ҫ��Ȼ�����
			return networkInfo.isAvailable();
		}
		return false;
	}
	
	/**
	 * Http�����ȡ����(Post��ʽ)
	 * @param url
	 * @param arry
	 * @return
	 */
	public static String PostHttpRequest(String url) {
		try {
			String httpUrl = url;
			// HttpPost���Ӷ���
			HttpPost httpRequest = new HttpPost(httpUrl);
			// ���Ҫ���ݵĲ���
			ArrayList<NameValuePair> arryArrayList = new ArrayList<NameValuePair>();
			arryArrayList.add(new BasicNameValuePair("1", "1"));
			// params.add(new BasicNameValuePair("par",
			// "HttpClient_android_Post"));
			// �����ַ���
			HttpEntity httpentity = new UrlEncodedFormEntity(arryArrayList, "gb2312");
			// ����httpRequest
			httpRequest.setEntity(httpentity);
			// ȡ��Ĭ�ϵ�HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// ȡ��HttpResponse
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			// HttpStatus.SC_OK��ʾ���ӳɹ�
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// ȡ�÷��ص��ַ���
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
     * ��ȡ��ַ����
     * @param url
     * @return
     * @throws Exception
     */
     public static String getContent(String url) throws Exception{
         StringBuilder sb = new StringBuilder();
         
         HttpClient client = new DefaultHttpClient();
         HttpParams httpParams = client.getParams();
         //�������糬ʱ����
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
 	 * Http�����ȡ����(GET��ʽ)
 	 * @param url
 	 * @return
 	 */
 	public static String GetHttpRequest(String url) {
 		try {
 			String httpUrl = url;
 			// HttpGet���Ӷ���
 			HttpGet httpRequest = new HttpGet(httpUrl);
 			// ȡ��HttpClient����
 			HttpClient httpclient = new DefaultHttpClient();
 			// ����HttpClient��ȡ��HttpResponse
 			HttpResponse httpResponse = httpclient.execute(httpRequest);
 			// ����ɹ�
 			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
 				// ȡ�÷��ص��ַ���
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