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
	 * 检查是否包含新版本
	 * @param context
	 * @return 是否返回新版本 true:有新版本;false:无新版本
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
	 * 检查服务器版本号
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
	 * 没有更新提示
	 * */
	public static void notNewVersionShow(Context context) {
        int verCode = VersionHelper.getVersionCode(context);
        String verName = VersionHelper.getVersionName(context);
        StringBuffer sb = new StringBuffer();
        sb.append("当前版本:");
        sb.append(verName);
        //sb.append(" Code:");
        //sb.append(verCode);
        sb.append(",\n已是最新版,无需更新!");
        Dialog dialog = new AlertDialog.Builder(context)
                        .setTitle("软件更新").setMessage(sb.toString())// 设置内容
                        .setPositiveButton("确定",// 设置确定按钮
                                        new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog,
                                                                int which) {
                                                	dialog.dismiss();
                                                }

                                        }).create();// 创建
        // 显示对话框
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
        //sb.append("当前版本:");
        //sb.append(verName);
        //sb.append(" Code:");
        //sb.append(verCode);
        sb.append("发现新版本:");
        sb.append(newVerName);
        sb.append("\n更新包总字节数:"+length);
        //sb.append(" Code:");
        //sb.append(newVerCode);
        sb.append("\n是否更新?");
        Dialog dialog = new AlertDialog.Builder(context)
                        .setTitle("软件更新")
                        .setMessage(sb.toString())
                        // 设置内容
                        .setPositiveButton("更新",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                            int which) {
                                    pBar = new ProgressDialog(context);
                                    pBar.setTitle("正在下载");
                                    pBar.setMessage("请稍候...");
                                    //pBar.setProgress(which * 100);
                                    pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    downFile(context,VersionHelper.UPDATE_SERVER
                                                    + VersionHelper.UPDATE_APKNAME+"?"+UUID.randomUUID());
                            }

                        })
                        .setNegativeButton("暂不更新",
                                        new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,
                                                                int whichButton) {
                                                        // 点击"取消"按钮之后退出程序
                                                        dialog.dismiss();
                                                }
                                        }).create();// 创建
        // 显示对话框
        dialog.show();
	}
	
	static int   fileSize;
	static int   downLoadFileSize;
	
	private static Handler handler = new Handler()
	  {
	    @Override
	    public void handleMessage(Message msg)
	    {//定义一个Handler，用于处理下载线程与UI间通讯
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
	            //Toast.makeText(.this, "文件下载完成", 1).show();
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
					fileSize = (int) length;//根据响应获取文件大小
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
							sendMsg(1);//更新进度条
							count += ch;
							if (length > 0) {
							}
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					
					sendContextMsg(2,context);//通知下载完成
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