package com.japyijiwenfa.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBbase extends SQLiteOpenHelper {

	
	private static final String Create_ServerClass = "Create table ServerClass(" +
			"ServerClassID integer primary key,ServerClassName text,Description text,IsWebServer integer)";
	private static final String Create_ServerList="Create table ServerList(" +
			"id integer primary key,ServerID text,ServerClassName text,ServerClassID integer,ServerName text," +
			"ServerIP text, ServerIP2 text, ServerMac text,ServerMac2 text,ServerStatus integer,CommandList text,Description text)";
	private static final String Create_Version="Create table Version(id integer primary key,Version1 integer,LastUpdate text)";
	private static final String Create_Command="Create table ServerCommand(id integer primary key,CommandName text,Command text)";
	private static final String Create_User = "Create table User(UserName text,PassWord text,IsSave integer, IsAdmin integer)";
	/**
	 * 客户端监听数据
	 * */
	private static final String Create_MonitorData="Create table MonitorData("+
			"ManchineName nvarchar(50) , MData double,dataType text,CreateTime long)";
	public DBbase(Context paramContext) {
		super(paramContext, "QDCAWS.db", null, 13);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Create_ServerClass);
		db.execSQL(Create_ServerList);
		db.execSQL(Create_Version);
		db.execSQL(Create_MonitorData);
		db.execSQL(Create_Command);
		db.execSQL(Create_User);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table ServerClass");
		db.execSQL("drop table ServerList");
		db.execSQL("drop table Version");
		db.execSQL("drop table MonitorData");
		db.execSQL("drop table ServerCommand");
		db.execSQL("drop table User");
		onCreate(db);
	}
}
