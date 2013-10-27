package com.exposuresoftware.xsandos;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
	
	//private static String TAG = "Xs And Os - Settings";
	
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		getFragmentManager().beginTransaction().replace( android.R.id.content, new SettingsFragment() )
			.commit();
	}
	
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}
}
