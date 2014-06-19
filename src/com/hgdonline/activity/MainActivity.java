package com.hgdonline.activity;


import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hgdonline.entity.Message;
import com.hgdonline.net.ConnectNet;
import com.hgdonline.sqlite.HandleSharedPre;

public class MainActivity extends BaseActivity {
	
	public final static String IS_BORROWING = "is_borrowing";
	//借书
	private final static int SCANNIN_BORROW_CODE = 1;
	//还书
	private final static int SCANNIN_RETURN_CODE = 2;
	//扫描入库
	private final static int SCANNIN_ADD_BOOK = 3;

	//发送请求的时候会使用dialog让用户等待
	private ProgressDialog dialog;
	
	private Handler handler;
	private String userName;
	private String password;
	
	private Message message;
	
	private boolean isSuperUser = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		password = intent.getStringExtra("password");
		isSuperUser = intent.getBooleanExtra("isSuperUser", false);
		
		//点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		ImageButton buttonBorrow = (ImageButton) findViewById(R.id.button_borrow);
		buttonBorrow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_BORROW_CODE);
			}
		});
		ImageButton buttonReturn = (ImageButton)findViewById(R.id.button_return);
		buttonReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, MipcaActivityCapture.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(intent, SCANNIN_RETURN_CODE);
			}
		});
		
		ImageButton buttonScanAddBooks = (ImageButton) findViewById(R.id.button_scan_books);
		if(!isSuperUser){
			buttonScanAddBooks.setVisibility(View.INVISIBLE);
			buttonScanAddBooks.setClickable(false);
		}else{
			buttonScanAddBooks.setVisibility(View.VISIBLE);
			buttonScanAddBooks.setClickable(true);
			buttonScanAddBooks.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, MipcaActivityCapture.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivityForResult(intent, SCANNIN_ADD_BOOK);
				}
			});
		}
		handler = new Handler();
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			if(!"".equals(result.trim())){
				if(!isConnectWifi(MainActivity.this)){
					Toast.makeText(MainActivity.this, "请先连接办公室wifi！", Toast.LENGTH_LONG).show();
				}else{
					//如果是扫书入库，就直接跳转到另外一个界面
					if(requestCode == SCANNIN_ADD_BOOK){
						Intent intent = new Intent(MainActivity.this, SuperUserActivity.class);
						intent.putExtra("result", result);
						intent.putExtra("userName", userName);
						intent.putExtra("password", password);
						MainActivity.this.startActivity(intent);
					}else{
						dialog = ProgressDialog.show(MainActivity.this, "数据加载中", "数据加载中，请稍后。。。");
						HandlPost(result, requestCode);
					}
				}
			}
        }
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE,1,1,"当前借阅");
		menu.add(Menu.NONE,2,2,"借阅历史");
		menu.add(Menu.NONE,3,3,"注销账号");
		menu.add(Menu.NONE,4,4,"退出登陆");
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		//当前借阅
		case 1:
			Intent intent = new Intent(this, ShowBooksActivity.class);
			intent.putExtra(IS_BORROWING, "1");
			intent.putExtra("userName", userName);
			intent.putExtra("password",password);
			intent.putExtra("isSuperUser", isSuperUser);
			MainActivity.this.startActivity(intent);
			break;
		//借阅历史
		case 2:
			Intent intent1 = new Intent(this, ShowBooksActivity.class);
			intent1.putExtra(IS_BORROWING, "0");
			intent1.putExtra("userName", userName);
			intent1.putExtra("password",password);
			intent1.putExtra("isSuperUser", isSuperUser);
			MainActivity.this.startActivity(intent1);
			break;
		//注销账号
		case 3:
			HandleSharedPre sharedPre = new HandleSharedPre(MainActivity.this);
			sharedPre.deleteMessage();
			Intent intent2 = new Intent(this, LoginActivity.class);
			MainActivity.this.startActivity(intent2);
			this.finish();
			break;
		//退出
		case 4:
			System.exit(0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//handle net business
	/**
	 * 
	 * @param id 书本编号
	 * @param status 如果是借阅就为1，还书就为2
	 * @return
	 */
	private int HandlPost(final String id, final int status){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					ConnectNet conn = ConnectNet.getConnect();
						//如果是借书
						if(SCANNIN_BORROW_CODE == status){
							message = conn.borrowBooks(userName, password,id,isSuperUser);
						}else if(SCANNIN_RETURN_CODE == status){//还书
							message = conn.returnBooks(userName, password, id, isSuperUser);
						}
						//更新界面
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Toast.makeText(MainActivity.this, message.getInfo(), Toast.LENGTH_LONG).show();
								dialog.dismiss();
							}
						});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return 0;
	}
	
	

}
