package com.japyijiwenfa.util;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class TextReader {
	
	/**
	 * 读取标题目录
	 * @param context
	 * @return
	 */
	public static List<String> readCatalog(Context context){
		List<String> list = new ArrayList<String>();
		InputStream in = null;
		BufferedReader reader = null;
		try {
			in = context.getAssets().open("catalog.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while((line = reader.readLine())!=null){
				list.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	public static String readChapter(Context context,int chapter){
		String chapterName = "chapter"+chapter+".txt";
		String text = null;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			in = context.getAssets().open(chapterName);
			reader = new BufferedReader(new InputStreamReader(in));
			int len = in.available();
			char[] buf = new char[len];
			int l = reader.read(buf);
			text = new String(buf,0,l);
			text = text.replaceAll("\\r\\n","\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}
	/**
	 * 读取一个片段
	 * @param context
	 * @param start
	 * @param end
	 * @param chapter
	 * @return
	 */
	public static String readFragment(Context context,int start,int len,int chapter){
		String chapterName = "chapter"+chapter+".txt";
		String text = null;
		InputStream in = null;
		BufferedReader reader = null;
		try {
			in = context.getAssets().open(chapterName);
			reader = new BufferedReader(new InputStreamReader(in));
			reader.skip(start);
			char[] buf = new char[len];
			int l = reader.read(buf);
			text = new String(buf,0,l);
			text = text.replaceAll("\\r\\n","\n");
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text;
	}
	public static int getContentLength(Context context,int chapter){
		String chapterName = "chapter"+chapter+".txt";
		InputStream in = null;
		BufferedReader reader = null;
		int contentLength = -1;
		try{
			in = context.getAssets().open(chapterName);
			reader = new BufferedReader(new InputStreamReader(in));
			int len = in.available();
			char[] buf = new char[len];
			contentLength = reader.read(buf);
			String txt = new String(buf,0,contentLength);
			txt = txt.replaceAll("\\r\\n","\n");
			contentLength = txt.length();
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return contentLength;
	}
	public static String readPostscript(Context context){
		InputStream in = null;
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try{
			in = context.getAssets().open("postscript.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = null;
			while((line = reader.readLine())!=null){
				sb.append(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}