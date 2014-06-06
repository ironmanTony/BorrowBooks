package com.hgdonline.sqlite;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hgdonline.entity.Book;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 这个类主要是引用MySQLiteHelper来操作数据库，提供删除，插入，更新等等操作
 *
 */

public class HandleSQLite {
    private MySQLiteHelper helper;
    private SQLiteDatabase db;
    public HandleSQLite(Context context){
    	helper=new MySQLiteHelper(context);
    }
    //插入数据book
    public long insertBook(Book book){
    	if(helper!=null){
    		db=helper.getWritableDatabase();
    		ContentValues values=new ContentValues();
    		if(book.getBookName()!=null){
    			values.put(MySQLiteHelper.BOOK_NAME, book.getBookName());
    		}
    		if(book.getBookId()!=null){
    			values.put(MySQLiteHelper.BOOK_ID, book.getBookId());
    		}
    		if(book.getBorrowDate()!=null){
    			values.put(MySQLiteHelper.BORROW_DATE, book.getBorrowDate().toString());
    		}
    		if(book.getIsBorrowing()!=-1){
    			values.put(MySQLiteHelper.BORROW_STATE, book.getIsBorrowing());
    		}
    		long rowId=db.insert(MySQLiteHelper.TABLE_NAME, null, values);
    		return rowId;
    	}
    	return -1;
    }
    //删除数据book
    public int deleteBook(Book book){
    	if(helper!=null){
    		db=helper.getWritableDatabase();
    		String whereClause=MySQLiteHelper.BOOK_ID+"=?";
    		String[] whereArgs={book.getBookId()};
    		int rowID=db.delete(MySQLiteHelper.TABLE_NAME, whereClause, whereArgs);
    		return rowID;
    	}
    	return -1;
    }
    //借阅book历史信息
  public List<Book> selectBooks(){
	  List<Book> booklist=new ArrayList<Book>();
	  String[] columns={
			  MySQLiteHelper.BOOK_NAME,
			  MySQLiteHelper.BOOK_ID,                                                          
			  MySQLiteHelper.PUBLISHING_CAMPANY,
			  MySQLiteHelper.BORROW_DATE,
			  MySQLiteHelper.BORROW_STATE
	  };
	  String orderBy="_id desc";
	  if(helper!=null){
		  db=helper.getReadableDatabase();
		  Cursor cursor=db.query(MySQLiteHelper.TABLE_NAME, columns, null, null, null, null, orderBy);
		  int book_name=cursor.getColumnIndex(MySQLiteHelper.BOOK_NAME);
		  int book_id=cursor.getColumnIndex(MySQLiteHelper.BOOK_ID);
		  int publishing_campany=cursor.getColumnIndex(MySQLiteHelper.PUBLISHING_CAMPANY);
		  int borrow_date=cursor.getColumnIndex(MySQLiteHelper.BORROW_DATE);
		  int borrow_state=cursor.getColumnIndex(MySQLiteHelper.BORROW_STATE);
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()){
			  Book book=new Book();
			  book.setBookName(cursor.getString(book_name));
			  book.setBookId(cursor.getString(book_id));
			  book.setPublishingCompany(cursor.getString(publishing_campany));
			  book.setBorrowDate(strToDateLong(cursor.getString(borrow_date)));
			  book.setIsBorrowing(cursor.getInt(borrow_state));
			  booklist.add(book);
			  cursor.moveToNext();
		  }
	  }
	  return booklist;
  }
//  查询当前借阅的book信息
  public List<Book> selectCurrent(){
	  List<Book> booklist=new ArrayList<Book>();
	  String[] columns={
			  MySQLiteHelper.BOOK_NAME,
			  MySQLiteHelper.BOOK_ID,                                                          
			  MySQLiteHelper.PUBLISHING_CAMPANY,
			  MySQLiteHelper.BORROW_DATE,
			  MySQLiteHelper.BORROW_STATE
	  };
	  String orderBy=MySQLiteHelper.BORROW_DATE+" desc";
	  if(helper!=null){
		  db=helper.getReadableDatabase();
		  String selection=MySQLiteHelper.BORROW_STATE+"=?";
		  String[] selectArgs={"1"};
		  Cursor cursor=db.query(MySQLiteHelper.TABLE_NAME, columns, selection, selectArgs, null, null, orderBy);
		  int book_name=cursor.getColumnIndex(MySQLiteHelper.BOOK_NAME);
		  int book_id=cursor.getColumnIndex(MySQLiteHelper.BOOK_ID);
		  int publishing_campany=cursor.getColumnIndex(MySQLiteHelper.PUBLISHING_CAMPANY);
		  int borrow_date=cursor.getColumnIndex(MySQLiteHelper.BORROW_DATE);
		  int borrow_state=cursor.getColumnIndex(MySQLiteHelper.BORROW_STATE);
		  cursor.moveToFirst();
		  while(!cursor.isAfterLast()){
			  Book book=new Book();
			  book.setBookName(cursor.getString(book_name));
			  book.setBookId(cursor.getString(book_id));
			  book.setPublishingCompany(cursor.getString(publishing_campany));
			  book.setBorrowDate(strToDateLong(cursor.getString(borrow_date)));
			  book.setIsBorrowing(cursor.getInt(borrow_state));
			  booklist.add(book);
			  cursor.moveToNext();
		  }
	  }
	  return booklist;
  }
  //字符转换成日期的函数
  public static Date strToDateLong(String strDate) {

	    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    ParsePosition pos = new ParsePosition(0);
	    Date strtodate = formatter.parse(strDate, pos);
	    return strtodate;
	}
  
  public Cursor getCursor(String args){
	  if(helper != null){
		  String[] columns={
				  "_id",
				  MySQLiteHelper.BOOK_NAME,
				  MySQLiteHelper.PUBLISHING_CAMPANY,
				  MySQLiteHelper.BORROW_DATE,
				  MySQLiteHelper.BORROW_STATE
		  };
		  String selectTion = "borrow_state = ?";
		  String[] selectionArgs = {args};
		  String orderBy="_id desc ";
		  db = helper.getReadableDatabase();
		  Cursor cursor = db.query(MySQLiteHelper.TABLE_NAME,columns,selectTion,selectionArgs,null,null,orderBy);
		  return cursor;
	  }
	  return null;
  }
  

}
