package com.hgdonline.activity;


import com.hgdonline.sqlite.HandleSharedPre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public final static String IS_BORROWING = "is_borrowing";
	//借书
	private final static int SCANNIN_BORROW_CODE = 1;
	//还书
	private final static int SCANNIN_RETURN_CODE = 2;
	/**
	 * 显示扫描结果
	 */
	private TextView mTextView ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mTextView = (TextView) findViewById(R.id.result); 
//		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		
		//点击按钮跳转到二维码扫描界面，这里用的是startActivityForResult跳转
		//扫描完了之后调到该界面
		ImageButton mButton = (ImageButton) findViewById(R.id.button_borrow);
		mButton.setOnClickListener(new OnClickListener() {
			
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
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_BORROW_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				mTextView.setText("borrow "+bundle.getString("result"));
				//显示
//				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
			}
			break;
		case SCANNIN_RETURN_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//显示扫描到的内容
				mTextView.setText("return "+bundle.getString("result"));
				//显示
//				mImageView.setImageBitmap((Bitmap) data.getParcelableExtra("bitmap"));
			}
			break;
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
			MainActivity.this.startActivity(intent);
			break;
		//借阅历史
		case 2:
			Intent intent1 = new Intent(this, ShowBooksActivity.class);
			intent1.putExtra(IS_BORROWING, "0");
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
		//退出de
		case 4:
			System.exit(0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

}
