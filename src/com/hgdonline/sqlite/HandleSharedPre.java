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
	public static final String IS_SUPER_USER = "is_super_user";
	
	public HandleSharedPre(Context context){
		sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
	}
	
	//insert message
	public boolean insertUserNameAndPassword(String name, String pass, boolean isSuperUser){
		if(sharedPreference != null){
			Editor editor = sharedPreference.edit();
			editor.putString(USER_NAME, name);
			editor.putString(PASSWORD, pass);
			editor.putBoolean(IS_SUPER_USER, isSuperUser);
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
			boolean isSuperUser = sharedPreference.getBoolean(IS_SUPER_USER, false);
			message.add(name);
			message.add(pass);
			message.add(""+isSuperUser);
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
			editor.putBoolean(IS_SUPER_USER, false);
			editor.commit();
			return true;
		}
		return false;
	}

}
