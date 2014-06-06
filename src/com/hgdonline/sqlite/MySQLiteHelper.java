package com.hgdonline.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgdonline.entity.Book;

/**
 * 这个类主要是继承SQLitehelper 在这里创建表，
 * 
 *
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
	

	//数据库名
	public static final  String DATABASE_NAME="hgdonline.db";
	//数据库版本
	public static final int DATABASE_VERSION=1;
	//表名
	public static final String TABLE_NAME="bookInfo";
	//数的各种字段
	public static final String BOOK_NAME="book_name";//book name
	public static final String BOOK_ID="book_id";//book id
	public static final String PUBLISHING_CAMPANY="publishing_campany";
	public static final String  BORROW_DATE="borrow_date";
	public static final String 	BORROW_STATE="borrow_state";
	
	
	
	//创建表的sql
	public static final String CREATE_DATABASE="CREATE TABLE "+TABLE_NAME+"(_id integer primary key autoincrement,"
			+ BOOK_NAME+" text,"
			+BOOK_ID+" integer,"
			+PUBLISHING_CAMPANY+" text,"
			+BORROW_DATE+" date,"
			+BORROW_STATE+" integer)";
			
	//
  public MySQLiteHelper(Context context){
	  super(context,DATABASE_NAME,null,DATABASE_VERSION);
  }
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.execSQL(CREATE_DATABASE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists "+TABLE_NAME);
		onCreate(db);
	}
	
	

}
