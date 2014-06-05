package com.hgdonlie.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.qr_codescan.R;


public class LoginActivity extends Activity {
	private EditText editUserName;
	private EditText editPassword;
	private String userName;
	private String password;

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
				if(compareUserNameAndPass()){
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
				}
			}
		});
	}
	
	
	
	
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		//get message from sharedpreference, if the message is right ,just jump to another activity
		super.onStart();
	}






	//compare user name and password
	private boolean compareUserNameAndPass(){
		//compare user name and password
		return true;
	}
	
	/**
	 * get user name and password from sharedpreference
	 * @return 
	 * true get message successfully
	 * false get message failed
	 */
	private boolean getStoredUserMessage(){
		
		return true;
	}
	
	/**
	 * store user message to sharedpreference 
	 * @return
	 * true if stored successfully
	 * false stored failed
	 */
	
	private boolean storeUserMessage(String userName, String password){
		return true;
	}
		
}
