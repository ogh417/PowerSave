package com.landoapps.powersave;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ToggleService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        // register receiver that handles screen on and screen off logic
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        if (screenOn) {
        	Log.i("SERVICE","**********service screen off...**************");
        	disableSettings();
        	
        } else {
        	Log.i("SERVICE","**********service screen on...**************");
        	enableSettings();
        }
    }
    
    private void configSetting(boolean enable){
    	SharedPreferences pref = getSharedPreferences("pref", 0);
    	if(enable){
    		//only enabled those service that were disabled
    		SharedPreferences settings = getSharedPreferences("setting_state",0);
    		if(pref.getBoolean(PowerSave.WIFI, false)){
    			//enable wifi
    			if(settings.getBoolean(PowerSave.WIFI, false)){
    				WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
    				wifi.setWifiEnabled(true);
    				Log.i("ToggleService","*******Enabling Wifi***********");
    			}
    		}
    		if(pref.getBoolean(PowerSave.DATA3G, false)){
    			//enable 3g
    			Log.i("ToggleService","**********Enable 3g**********");
    		}
    	}else
    	{
    		if(pref.getBoolean(PowerSave.WIFI, false)){
    			// disable wifi
    			WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
    			wifi.setWifiEnabled(false);
    			Log.i("ToggleService","*******Disabling Wifi***********");
    		}
    		if(pref.getBoolean(PowerSave.DATA3G, false)){
    			//TODO:disable 3g
    			Log.i("ToggleService","**********Disable 3g**********");
    		}
    	}
    }
    
    private void disableSettings(){
    	//check if wifi or 3g is enabled before disabled.
    	SharedPreferences pref = getSharedPreferences("setting_state",0);
    	SharedPreferences.Editor editor = pref.edit();
    	//TODO:check 3g
    	
    	//check wifi
    	WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
    	if(wifi.isWifiEnabled())
    	{
    		editor.putBoolean(PowerSave.WIFI, true);
    		Toast.makeText(this, "wifi was on", Toast.LENGTH_SHORT).show();
    	}
    	editor.commit();
    	configSetting(false);
    }

    private void enableSettings(){
    	configSetting(true);
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}


}
