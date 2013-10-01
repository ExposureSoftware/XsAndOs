package com.exposuresoftware.xsandos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainMenu extends Activity {
	
	private static final String TAG = "XO - Main Menu";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
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
		getMenuInflater().inflate(R.menu.activity_main_menu, menu);
		return true;
	}
	
	public void startTwoPlayerGame(View button) {
		Log.v(TAG, "Two players selected.");
		Intent intentToStartGame = new Intent(this, Game.class);
		intentToStartGame.putExtra("solo", false);
		startActivity(intentToStartGame);
		return;
	}
	
	public void startOnePlayerGame(View button) {
		Log.v(TAG, "One player selected.");
		Intent intentToStartGame = new Intent(this, Game.class);
		intentToStartGame.putExtra("solo", true);
		startActivity(intentToStartGame);
		return;
	}

}
