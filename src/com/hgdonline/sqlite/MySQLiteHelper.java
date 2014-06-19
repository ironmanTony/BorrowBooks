package com.hgdonline.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * �������Ҫ�Ǽ̳�SQLitehelper �����ﴴ����
 *
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
	

	//���ݿ���
	public static final  String DATABASE_NAME="hgdonline";
	//���ݿ�汾
	public static final int DATABASE_VERSION=1;
	//����
	public static final String TABLE_NAME="bookInfo";
	//���ĸ����ֶ�
	public static final String BOOK_NAME="book_name";//book name
	public static final String BOOK_ID="book_id";//book id
	public static final String SEARCH_ID = "search_id"; //book barcode
	public static final String  BORROW_DATE="borrow_date";
	public static final String 	BORROW_STATE="borrow_state";
	
  public MySQLiteHelper(Context context){
	  super(context,DATABASE_NAME,null,DATABASE_VERSION);
  }
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		String sql="CREATE TABLE if not exists  bookInfo (_id integer primary key autoincrement,book_name text,book_id text,search_id text,borrow_date date,borrow_state integer)";
		arg0.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists "+TABLE_NAME);
		onCreate(db);
	}
	
	

}
