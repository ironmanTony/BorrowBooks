package com.hgdonline.activity;


import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hgdonline.net.ConnectNet;
import com.hgdonline.sqlite.HandleSharedPre;

public class MainActivity extends Activity {
	
	public final static String IS_BORROWING = "is_borrowing";
	//����
	private final static int SCANNIN_BORROW_CODE = 1;
	//����
	private final static int SCANNIN_RETURN_CODE = 2;
	/**
	 * ��ʾɨ����
	 */
	private TextView mTextView ;
	//���������ʱ���ʹ��progressdialog���û��ȴ�
	private ProgressDialog dialog;
	
	private Handler handler;
	private String userName;
	private String password;
	private int borrow_status;
	private int return_status;
	
	private boolean isSuperUser = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		password = intent.getStringExtra("password");
		isSuperUser = intent.getBooleanExtra("isSuperUser", false);
		
		mTextView = (TextView) findViewById(R.id.result); 
//		mImageView = (ImageView) findViewById(R.id.qrcode_bitmap);
		
		//�����ť��ת����ά��ɨ����棬�����õ���startActivityForResult��ת
		//ɨ������֮������ý���
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
		handler = new Handler();
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
			Bundle bundle = data.getExtras();
			String result = bundle.getString("result");
			mTextView.setText("return "+bundle.getString("result"));
			if(!"".equals(result.trim())){
				dialog = ProgressDialog.show(MainActivity.this, "���ݼ�����", "���ݼ����У����Ժ󡣡���");
				HandlPost(result, resultCode);
			}
        }
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(Menu.NONE,1,1,"��ǰ����");
		menu.add(Menu.NONE,2,2,"������ʷ");
		menu.add(Menu.NONE,3,3,"ע���˺�");
		menu.add(Menu.NONE,4,4,"�˳���½");
		return super.onCreateOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		//��ǰ����
		case 1:
			Intent intent = new Intent(this, ShowBooksActivity.class);
			intent.putExtra(IS_BORROWING, "1");
			MainActivity.this.startActivity(intent);
			break;
		//������ʷ
		case 2:
			Intent intent1 = new Intent(this, ShowBooksActivity.class);
			intent1.putExtra(IS_BORROWING, "0");
			MainActivity.this.startActivity(intent1);
			break;
		//ע���˺�
		case 3:
			HandleSharedPre sharedPre = new HandleSharedPre(MainActivity.this);
			sharedPre.deleteMessage();
			Intent intent2 = new Intent(this, LoginActivity.class);
			MainActivity.this.startActivity(intent2);
			this.finish();
			break;
		//�˳�de
		case 4:
			System.exit(0);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//handle net business
	/**
	 * 
	 * @param id �鱾���
	 * @param status ����ǽ��ľ�Ϊ1�������Ϊ2
	 * @return
	 */
	private int HandlPost(final String id, final int status){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					ConnectNet conn = ConnectNet.getConnect();
					int loginStatus = conn.login(userName, password,isSuperUser);
					if(ConnectNet.IP_ERROR == loginStatus){
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								dialog.dismiss();
								Toast.makeText(MainActivity.this, "ip����ȷ���������Ӱ칫��wifiȻ��ʹ�ã�", Toast.LENGTH_LONG).show();
							}
						});
					}else if(ConnectNet.CONN_OK == loginStatus){
						//login success
						//����ǽ���
						if(SCANNIN_BORROW_CODE == status){
							borrow_status = conn.borrowBooks(id);
						}else if(SCANNIN_RETURN_CODE == status){//����
							return_status = conn.returnBooks(id);
						}
						//���½���
						handler.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(SCANNIN_BORROW_CODE == status){
									if(1 == borrow_status){
										Toast.makeText(MainActivity.this, "����ɹ���", Toast.LENGTH_LONG).show();
									}else{
										Toast.makeText(MainActivity.this, "����ʧ�ܣ�", Toast.LENGTH_LONG).show();
									}
								}else if(SCANNIN_RETURN_CODE == status){
									if(1 == return_status){
										Toast.makeText(MainActivity.this, "����ɹ���", Toast.LENGTH_LONG).show();
									}else{
										Toast.makeText(MainActivity.this, "����ʧ�ܣ�", Toast.LENGTH_LONG).show();
									}
								}
								dialog.dismiss();
							}
						});
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return 0;
	}
	
	

}
