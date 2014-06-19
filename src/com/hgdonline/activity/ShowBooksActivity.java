package com.hgdonline.activity;


import java.io.IOException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import com.hgdonline.entity.Book;
import com.hgdonline.net.ConnectNet;
import com.hgdonline.sqlite.HandleSQLite;

public class ShowBooksActivity extends ListActivity {
	
	private ProgressDialog dialog;
	private Handler handler;
	
	private String userName;
	private String pass;
	private boolean isSuperUser;
	private String isBorrowing;
	
	private SimpleCursorAdapter adapter;
	private HandleSQLite handle = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_books);
		
		Intent intent = getIntent();
		isBorrowing = intent.getStringExtra(MainActivity.IS_BORROWING);
		userName = intent.getStringExtra("userName");
		pass = intent.getStringExtra("password");
		isSuperUser = intent.getBooleanExtra("isSuperUser", false);
		
		//加载数据
		refreshBookList();
		
		handler = new Handler();
		//设置点击actionbar返回
		ActionBar actionBar  = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuItem add = menu.add(0,0,0,"保存");
		add.setIcon(android.R.drawable.ic_menu_upload);
		add.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case android.R.id.home:
				this.finish();break;
			case 0:
				handleRefresh();
				break;
			default:break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void handleRefresh(){
		dialog = ProgressDialog.show(ShowBooksActivity.this, "加载中", "加载中，请稍后。。。");
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ConnectNet conn = ConnectNet.getConnect();
				ArrayList<Book> books = null;
				if(conn != null){
					try {
						books = (ArrayList<Book>) conn.getBorrowBooks(userName, pass, isSuperUser,Integer.parseInt(isBorrowing));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(books != null){
						//insert books into sqlite
						if(handle != null){
							handle = new HandleSQLite(ShowBooksActivity.this);
						}
						handle.deleteDate(isBorrowing);
						for(int i = 0;i<books.size();i++){
							handle.insertBook(books.get(i));
						}
					}
				}
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						dialog.dismiss();
						refreshBookList();
					}
				});
			}
		}).start();
	}
	
	private void refreshBookList(){
		if(handle == null){
			handle = new HandleSQLite(ShowBooksActivity.this);
		}
		Cursor cursor = handle.getCursor(isBorrowing);
		if(cursor != null){
			adapter = new SimpleCursorAdapter(this, R.layout.show_books_item, cursor, new String[]{"book_name","search_id","borrow_date"}, new int[]{R.id.book_name,R.id.book_id,R.id.borrow_date},CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
			setListAdapter(adapter);
		}
	}
	
	


}
