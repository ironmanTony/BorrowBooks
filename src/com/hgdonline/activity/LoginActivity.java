package com.hgdonline.activity;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		Button button = (Button) findViewById(R.id.button_login);
		editUserName = (EditText) findViewById(R.id.edit_user_name);
		editPassword = (EditText) findViewById(R.id.edit_password);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				compareUserNameAndPass();
			}
		});
		
		handler = new Handler();
		//初始化sharedPreference
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
			dialog = ProgressDialog.show(this, "login", "玩命登陆中。。。");
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ConnectNet conn = new ConnectNet(userName,password);
					conn.login();
					handler.post(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							storeUserMessage(userName, password);
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(intent);
							LoginActivity.this.finish();
							dialog.dismiss();
						}
					});
				}
			});
			thread.start();
		}else{
			Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
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
	private boolean storeUserMessage(String userName, String password){
		if(sharePre != null){
			if(sharePre.insertUserNameAndPassword(userName, password)){
				return true;
			}
		}
		return false;
	}
		
}
