package com.example.iotapptest;

import android.R.string;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {
	
	public DbOpenHelper(Context context) {
		super(context, "Items.db", null, 1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//String sql="Create table books(_id Integer primary key,name text,author text,subject text,price Integer)";
		//db.execSQL(sql);
		//Log.i("book db", "created");
		
		//String sql1="insert into player values(?,?)";
		//db.execSQL(sql1);
		String sql="Create table if not exists items(name text,value text,consumed text,date text)";
		
		db.execSQL(sql,null);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		String sql="drop table items";
		db.execSQL(sql);
		Log.w("book db", "deleted");
		
	}

}
