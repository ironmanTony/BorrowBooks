package com.hgdonline.activity;
import java.io.IOException;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hgdonline.entity.Book;
import com.hgdonline.entity.Message;
import com.hgdonline.net.ConnectNet;

public class SuperUserActivity extends BaseActivity {
	
	private String userName;
	private String pass;
	private ProgressDialog dialog;
	private Handler handler;
	
	private EditText editSearchCode;
	private EditText editDonator;
	private Message message;
	
	private Book book;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_super_user);
		Intent intent = getIntent();
		String code = intent.getStringExtra("result");
		book = new Book();
		book.setBookId(code);
		userName = intent.getStringExtra("userName");
		pass = intent.getStringExtra("password");
		TextView textView = (TextView)findViewById(R.id.show_book_id);
		textView.setText(code);
		
		editSearchCode = (EditText) findViewById(R.id.search_book_id);
		editDonator = (EditText) findViewById(R.id.donator);
		
		
		handler = new Handler();
		
		Button buttonCancel = (Button) findViewById(R.id.button_cancel);
		buttonCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SuperUserActivity.this.finish();
			}
		});
		
		Button buttonOK = (Button) findViewById(R.id.button_ok);
		buttonOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String searchCode = editSearchCode.getText().toString().trim();
				String donator = editDonator.getText().toString().trim();
				book.setSearchId(searchCode);
				book.setDonator(donator);
				dialog = ProgressDialog.show(SuperUserActivity.this, "添加中", "玩命添加中，请稍后。。。");
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
							try {
								ConnectNet conn = ConnectNet.getConnect();
								if(conn != null){
								message = conn.addBooks(userName, pass, book);
								}
								handler.post(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										dialog.dismiss();
										Toast.makeText(SuperUserActivity.this, message.getInfo() , Toast.LENGTH_SHORT).show();
										SuperUserActivity.this.finish();
									}
								});
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
				}).start();;
			}
		});
		
		//设置点击actionbar返回
		ActionBar actionBar  = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();break;
		}
		return true;
		
	}


}
