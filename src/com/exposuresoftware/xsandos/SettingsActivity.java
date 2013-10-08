package com.exposuresoftware.xsandos;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
	private static String TAG = "Xs And Os - Settings";
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		getFragmentManager().beginTransaction().replace( android.R.id.content, new SettingsFragment() )
			.commit();
	}
}
