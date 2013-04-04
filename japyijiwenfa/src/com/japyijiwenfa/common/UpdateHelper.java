package com.japyijiwenfa.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class UpdateHelper {
	public static ProgressDialog pBar;
	
	private static int newVerCode = 0;
	private static String newVerName = "";
	
	//private static Handler handler = new Handler();
	
	/**
	 * ����Ƿ�����°汾
	 * @param context
	 * @return �Ƿ񷵻��°汾 true:���°汾;false:���°汾
	 * */
	public static boolean CheckNewVersion(Context context){
		return getServerVerCode(context);
	}
	
	public static void ExcuteUpdateVersion(Context context) {
//		int vercode = VersionHelper.getVersionCode(context);
//		if (newVerCode > vercode) {
//			doNewVersionUpdate(context);
//		} else {
//			//notNewVersionShow(context);
//		}
		doNewVersionUpdate(context);
	}
	
	/**
	 * ���������汾��
	 * */
	public static boolean getServerVerCode(Context context) {
        try {
                String verjson = NetWorkTool.getContent(VersionHelper.UPDATE_SERVER + VersionHelper.UPDATE_VERJSON+"?"+UUID.randomUUID());
                JSONArray array = new JSONArray(verjson);
                if (array.length() > 0) {
                        JSONObject obj = array.getJSONObject(0);
                        try {
                                newVerCode = Integer.parseInt(obj.getString("verCode"));
                                newVerName = obj.getString("verName");
                        } catch (Exception e) {
                                newVerCode = -1;
                                newVerName = "";
                                return false;
                        }
                }
        } catch (Exception e) {
                return false;
        }
        int vercode = VersionHelper.getVersionCode(context);
        if (newVerCode > vercode)
        	return true;
        else
        	return false;
	}
	
	public static void CancelDialog(){
		pBar.cancel();
	}
	
	public static void ShowDialog(){
		pBar.show();
	}
	
	/**
	 * û�и�����ʾ
	 * */
	public static void notNewVersionShow(Context context) {
        int verCode = VersionHelper.getVersionCode(context);
        String verName = VersionHelper.getVersionName(context);
        StringBuffer sb = new StringBuffer();
        sb.append("��ǰ�汾:");
        sb.append(verName);
        //sb.append(" Code:");
        //sb.append(verCode);
        sb.append(",\n�������°�,�������!");
        Dialog dialog = new AlertDialog.Builder(context)
                        .setTitle("�������").setMessage(sb.toString())// ��������
                        .setPositiveButton("ȷ��",// ����ȷ����ť
                                        new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                int which) {
                                                	dialog.dismiss();
                                                }

                                        }).create();// ����
        // ��ʾ�Ի���
        dialog.show();
	}
	
	public static void doNewVersionUpdate(final Context context) {
        int verCode = VersionHelper.getVersionCode(context);
        String verName = VersionHelper.getVersionName(context);
        StringBuffer sb = new StringBuffer();
        HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(VersionHelper.UPDATE_SERVER
                + VersionHelper.UPDATE_APKNAME+"?"+UUID.randomUUID());
		HttpResponse response;
		long length=0;
		try {
			response = client.execute(get);
			HttpEntity entity = response.getEntity();
			length= entity.getContentLength();
		}catch (Exception e) {
			// TODO: handle exception
		}
        //sb.append("��ǰ�汾:");
        //sb.append(verName);
        //sb.append(" Code:");
        //sb.append(verCode);
        sb.append("�����°汾:");
        sb.append(newVerName);
        sb.append("\n���°����ֽ���:"+length);
        //sb.append(" Code:");
        //sb.append(newVerCode);
        sb.append("\n�Ƿ����?");
        Dialog dialog = new AlertDialog.Builder(context)
                        .setTitle("�������")
                        .setMessage(sb.toString())
                        // ��������
                        .setPositiveButton("����",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                            int which) {
                                    pBar = new ProgressDialog(context);
                                    pBar.setTitle("��������");
                                    pBar.setMessage("���Ժ�...");
                                    //pBar.setProgress(which * 100);
                                    pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    downFile(context,VersionHelper.UPDATE_SERVER
                                                    + VersionHelper.UPDATE_APKNAME+"?"+UUID.randomUUID());
                            }

                        })
                        .setNegativeButton("�ݲ�����",
                                        new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                int whichButton) {
                                                        // ���"ȡ��"��ť֮���˳�����
                                                        dialog.dismiss();
                                                }
                                        }).create();// ����
        // ��ʾ�Ի���
        dialog.show();
	}
	
	static int   fileSize;
	static int   downLoadFileSize;
	
	private static Handler handler = new Handler()
	  {
	    @Override
	    public void handleMessage(Message msg)
	    {//����һ��Handler�����ڴ��������߳���UI��ͨѶ
	      if (!Thread.currentThread().isInterrupted())
	      {
	        switch (msg.what)
	        {
	          case 0:
	            pBar.setMax(fileSize);
	          case 1:
	        	pBar.setProgress(downLoadFileSize);
	            //int result = downLoadFileSize * 100 / fileSize;
	            //tv.setText(result + "%");
	            break;
	          case 2:
	            //Toast.makeText(.this, "�ļ��������", 1).show();
	        	  pBar.cancel();
	        	  Context context = (Context)msg.obj;
	        	  update(context);
	            break;

	          case -1:
//	            String error = msg.getData().getString("error");
//	            Toast.makeText(main.this, error, 1).show();
	            break;
	        }
	      }
	      super.handleMessage(msg);
	    }
	  };
	  private static void sendMsg(int flag)
		{
		    Message msg = new Message();
		    msg.what = flag;
		    handler.sendMessage(msg);
		}
	  private static void sendContextMsg(int flag,Context context){
		  Message msgMessage = new Message();
		  msgMessage.what=flag;
		  msgMessage.obj=context;
		  handler.sendMessage(msgMessage);
	  }
	private static void downFile(final Context context,final String url) {
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					fileSize = (int) length;//������Ӧ��ȡ�ļ���С
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {

						File file = new File(Environment
								.getExternalStorageDirectory(),
								VersionHelper.UPDATE_SAVENAME);
						fileOutputStream = new FileOutputStream(file);
						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						downLoadFileSize=0;
						sendMsg(0);
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							downLoadFileSize += ch;
							sendMsg(1);//���½�����
							count += ch;
							if (length > 0) {
							}
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					
					sendContextMsg(2,context);//֪ͨ�������
					//down(context);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}.start();

	}

//	private static void down(final Context context) {
//		handler.post(new Runnable() {
//			public void run() {
//				pBar.cancel();
//				update(context);
//			}
//		});
//
//	}

	private static void update(Context context) {

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), VersionHelper.UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}