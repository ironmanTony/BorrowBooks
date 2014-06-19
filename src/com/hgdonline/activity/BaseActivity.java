package com.hgdonline.activity;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public abstract class BaseActivity extends Activity{
	
	public boolean isConnectWifi(Context inContext){
		
		
		 WifiManager mWifiManager = (WifiManager) inContext  
		         .getSystemService(Context.WIFI_SERVICE);  
		         WifiInfo wifiInfo = mWifiManager.getConnectionInfo();  
		         int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();  
		         if (mWifiManager.isWifiEnabled() && ipAddress != 0) {  
		             return true;  
		         } else {  
		            return false;     
		        }  
	}

}
