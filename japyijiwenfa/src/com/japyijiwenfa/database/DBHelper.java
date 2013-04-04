package com.japyijiwenfa.database;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {

	private static String T_ServerClass = "ServerClass";
	private static String T_ServerList = "ServerList";
	private static String T_Version = "Version";
	private static String T_MonitorData="MonitorData";
	private static String T_ServerCommand="ServerCommand";
	private static String T_UserString = "User";
	
	private Context mContext;
	private DBbase mDBbase;
	private SQLiteDatabase mDB;

	public void open() throws SQLException {
		this.mDB = this.mDBbase.getWritableDatabase();
	}

	public DBHelper(Context paramContext) {
		this.mContext = paramContext;
		this.mDBbase = new DBbase(this.mContext);
		open();
	}

	public void finalize() {
		if (this.mDB.isOpen())
			this.mDB.close();
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void BeginTransaction() {
		this.mDB.beginTransaction();
	}

	public void SetTransactionSuccessful() {
		this.mDB.setTransactionSuccessful();
	}

	public void EndTransaction() {
		this.mDB.endTransaction();
	}

}
