package com.helper;



import com.example.iotapptest.DbOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbHelper {
	Context context;
	SQLiteDatabase mDb;
	public static DbHelper dbHandle = null;
	
	public synchronized static DbHelper getInstance(Context context)
	{
		if (dbHandle == null)
		{
			dbHandle = new DbHelper(context);
		}
		return dbHandle;
		
	}
	private DbHelper(Context context) {
		
		this.context = context;
		this.mDb=null;
	}
	public void open(){
		if(mDb==null){
			DbOpenHelper openHelper=new DbOpenHelper(context);
			mDb=openHelper.getWritableDatabase();
		}
	}
	public void close(){
		if(mDb!=null && mDb.isOpen()){
			mDb.close();
		}
	}
	public void execSQl(String sql,Object[] args){
		if(args==null){
			mDb.execSQL(sql);
		}
		else
			mDb.execSQL(sql, args);
	}
	public Cursor rawQuery(String sql,String[] args){
		Cursor cursor=mDb.rawQuery(sql, args);
		return cursor;
	}
	
	
	
}
