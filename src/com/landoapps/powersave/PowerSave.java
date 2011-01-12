package com.landoapps.powersave;

import com.landoapps.powersave.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class PowerSave extends Activity implements OnClickListener, OnCheckedChangeListener {
	static final String WIFI = "wifi";
	static final String DATA3G = "3g";
	private Button mBtnSave;
	private Button mBtnExit;
	private CheckBox mChkWifi;
	private CheckBox mChk3g;
	private boolean mWifiChecked;
	private boolean m3gChecked;
	BroadcastReceiver mReceiver = new ScreenReceiver();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SharedPreferences pref = getSharedPreferences("pref",0);
        
        // Obtain handles to UI objects
        mChkWifi = (CheckBox)findViewById(R.id.chkWifi);
        mWifiChecked = pref.getBoolean(WIFI, false);
        mChkWifi.setChecked(mWifiChecked);
        mChkWifi.setOnCheckedChangeListener(this);
        
        mChk3g = (CheckBox)findViewById(R.id.chk3g);
        m3gChecked = pref.getBoolean(DATA3G, false);
        mChk3g.setChecked(m3gChecked);
        mChk3g.setOnCheckedChangeListener(this);
        
        mBtnSave = (Button)findViewById(R.id.btnSave);
        mBtnSave.setOnClickListener(this);
        
        mBtnExit = (Button)findViewById(R.id.btnExit);
        mBtnExit.setOnClickListener(this);
		// register receiver
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        //filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mReceiver, filter);
        Log.i("POWERSAVE","*******register receiver on action*******");
      }

	@Override
	public void onClick(View v) {
		if(mBtnSave.equals(v)){
			// save preferences
			SharedPreferences pref = getSharedPreferences("pref",0);
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean(WIFI, mWifiChecked);
			editor.putBoolean(DATA3G, m3gChecked);
			editor.commit();
			Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show();
			finish();
		}else if(mBtnExit.equals(v)) { //kill everything
			Toast.makeText(this, "finish()", Toast.LENGTH_SHORT).show();
			try {
				unregisterReceiver(mReceiver);
			} catch (IllegalArgumentException e) {
				Log.i("POWERSAVE","Catch IllegalArgumentException e on btnExit");
			}finally{
				System.exit(0);
			}
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean isChecked) {
		 if (mChkWifi.equals(v)){
				mWifiChecked = isChecked; 
			}
			else if(mChk3g.equals(v)){
				m3gChecked = isChecked;
			}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			unregisterReceiver(mReceiver);
		} catch (IllegalArgumentException e) {
			Log.i("POWERSAVE","Catch IllegalArgumentException e onDestroy");
		}
	}
    
    
}