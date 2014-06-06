package com.hgdonline.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class HandleSharedPre {
	
	private SharedPreferences sharedPreference;
	private static final String PREFERENCE_NAME = "message";
	public static final String USER_NAME = "name";
	public static final String PASSWORD = "password";
	
	public HandleSharedPre(Context context){
		sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	}
	
	//insert message
	public boolean insertUserNameAndPassword(String name, String pass){
		if(sharedPreference != null){
			Editor editor = sharedPreference.edit();
			editor.putString(USER_NAME, name);
			editor.putString(PASSWORD, pass);
			editor.commit();
			return true;
		}
		return false;
	}
	
	public List<String> getMessage(){
		if(sharedPreference !=  null){
			ArrayList<String> message = new ArrayList<String>();
			String name = sharedPreference.getString(USER_NAME, "");
			String pass = sharedPreference.getString(PASSWORD, "");
			message.add(name);
			message.add(pass);
			return message;
		}
		return null;
	}
	
	//delete message,just set pass and user name ""
	public boolean deleteMessage(){
		if(sharedPreference != null){
			Editor editor = sharedPreference.edit();
			editor.putString(USER_NAME, "");
			editor.putString(PASSWORD, "");
			editor.commit();
			return true;
		}
		return false;
	}

}
