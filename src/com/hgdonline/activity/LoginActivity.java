package com.hgdonline.activity;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hgdonline.net.ConnectNet;
import com.hgdonline.sqlite.HandleSharedPre;


public class LoginActivity extends Activity {
	private EditText editUserName;
	private EditText editPassword;
	private String userName;
	private String password;
	private HandleSharedPre sharePre;
	private Handler handler;
	private ProgressDialog dialog;
	//��ȡ��½״ֵ̬
	private int status = -1;
	//�Ƿ񳬼�����Ա
	private boolean isSuperUser = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		Button button = (Button) findViewById(R.id.button_login);
		editUserName = (EditText) findViewById(R.id.edit_user_name);
		editPassword = (EditText) findViewById(R.id.edit_password);
		
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,new String[]{"��ͨ�û���½","����Ա��½"});
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setVisibility(View.VISIBLE);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:break;
				case 1:isSuperUser = true;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				compareUserNameAndPass();
			}
		});
		
		handler = new Handler();
		//��ʼ��sharedPreference
		sharePre = new HandleSharedPre(this);
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		//get message from sharedpreference, if the message is right ,just jump to another activity
		if(getStoredUserMessage()){
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			intent.putExtra("userName", userName);
			intent.putExtra("password",password);
			intent.putExtra("isSuperUser", isSuperUser);
			startActivity(intent);
			LoginActivity.this.finish();
		}
		super.onStart();
	}


	//compare user name and password
	private boolean compareUserNameAndPass(){
		//compare user name and password
		userName = editUserName.getText().toString();
		password = editPassword.getText().toString();
		if(!(userName.equals("")|| password.equals(""))){
			dialog = ProgressDialog.show(this, "login", "������½�С�����");
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ConnectNet conn = ConnectNet.getConnect();
					try{
						status = conn.login(userName,password,isSuperUser);
					}catch(IOException e){
						e.printStackTrace();
					}
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							//�����½�ɹ�
							if(ConnectNet.CONN_OK== status){
								storeUserMessage(userName, password,isSuperUser);
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								intent.putExtra("userName", userName);
								intent.putExtra("password",password);
								intent.putExtra("isSuperUser",isSuperUser);
								startActivity(intent);
								LoginActivity.this.finish();
							}else if(ConnectNet.IP_ERROR == status){//ip����
								Toast.makeText(LoginActivity.this, "ip����ȷ���������Ӱ칫��wifiȻ���½��", Toast.LENGTH_LONG).show();
							}else if(ConnectNet.CONN_WRONG == status){//�û������������
								Toast.makeText(LoginActivity.this, "�û��������벻��ȷ��", Toast.LENGTH_LONG).show();
							}
							dialog.dismiss();
						}
					});
				}
			});
			thread.start();
		}else{
			Toast.makeText(this, "�û���������Ϊ��", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	/**
	 * get user name and password from sharedpreference
	 * @return 
	 * true get message successfully
	 * false get message failed
	 */
	private boolean getStoredUserMessage(){
		if(sharePre != null){
			List<String> message = sharePre.getMessage();
			if((!message.get(0).equals(""))&&(!message.get(1).equals(""))){
				userName = message.get(0);
				password = message.get(1);
				isSuperUser = message.get(2).equals("false")? false : true;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * store user message to sharedpreference 
	 * @return
	 * true if stored successfully
	 * false stored failed
	 */
	private boolean storeUserMessage(String userName, String password,boolean isSuperUser){
		if(sharePre != null){
			if(sharePre.insertUserNameAndPassword(userName, password,isSuperUser)){
				return true;
			}
		}
		return false;
	}
	
	//�ж������Ƿ�����
	private boolean isConnectWiFy(){
		
		return false;
	}
		
}
