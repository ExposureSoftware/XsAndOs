package com.exposuresoftware.xsandos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
	
	private static final String TAG = "Xs and Os - Main Menu";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "Starting Main Menu");
		setContentView(R.layout.activity_main_menu);
		PreferenceManager.setDefaultValues( this, R.xml.preferences, false );
		Button button_one_player = (Button) findViewById(R.id.button_one_player);
		Button button_two_player = (Button) findViewById(R.id.button_two_player);
		Typeface chalkduster = Typeface.createFromAsset(getAssets(), 
        		"fonts/Chalkduster.ttf");
		button_one_player.setTypeface(chalkduster);
		button_two_player.setTypeface(chalkduster);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.d( TAG, "Creating options menu" );
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}
	
	public void startSettings(MenuItem item) {
		Log.d( TAG, "Settings button pressed" );
		startActivity( new Intent( this, SettingsActivity.class ) );
	}
	
	public void startSettings(View view) {
		Log.d( TAG, "Settings button pressed" );
		startActivity( new Intent( this, SettingsActivity.class ) );
	}
	
	public void startTwoPlayerGame(View button) {
		Log.d(TAG, "Two players selected.");
		Intent intentToStartGame = new Intent(this, Game.class);
		intentToStartGame.putExtra("solo", false);
		startActivity(intentToStartGame);
		return;
	}
	
	public void startOnePlayerGame(View button) {
		Log.d(TAG, "One player selected.");
		Intent intentToStartGame = new Intent(this, Game.class);
		intentToStartGame.putExtra("solo", true);
		startActivity(intentToStartGame);
		return;
	}

}
