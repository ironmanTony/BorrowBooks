package com.hgdonline.activity;


import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.hgdonline.entity.Book;
import com.hgdonline.sqlite.HandleSQLite;

public class ShowBooksActivity extends ListActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_books);
		
		Intent intent = getIntent();
		String isBorrowing = intent.getStringExtra(MainActivity.IS_BORROWING);
		System.out.println(isBorrowing);
		
		HandleSQLite handle = new HandleSQLite(this);
		//插入几组数据用来测试
		for(int i = 0;i<5;i++){
			Book book = new Book();
			book.setBookId(i+"");
			book.setBookName("NBbook0000"+i);
			book.setPublishingCompany("publish"+i);
			book.setIsBorrowing(0);
			handle.insertBook(book);
		}
		//1表示正在借的书
		Cursor cursor = handle.getCursor(isBorrowing);
		
		if(cursor == null){
			System.out.println("null");
		}

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.show_books_item, cursor, new String[]{"book_name","publishing_campany","borrow_date"}, new int[]{R.id.book_name,R.id.book_publishing_factory,R.id.borrow_date},CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
		
		//设置点击actionbar返回
		ActionBar actionBar  = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case android.R.id.home:
			this.finish();break;
			default:
		}
		return super.onOptionsItemSelected(item);
	}
	
	


}
