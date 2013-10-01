package com.exposuresoftware.xsandos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Game2 extends Activity {
	
	Marks player = Marks.X;
	boolean solo_player = false; 
	Typeface chalkduster = null;
	Marks[][] board = new Marks[3][3];
	int moves = 0;
	
	//private final static String TAG = "XO - Game";
	enum Marks{Blank, X, O};
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_game);
		solo_player = getIntent().getExtras().getBoolean("solo");
		chalkduster = Typeface.createFromAsset(getAssets(), 
        		"fonts/Chalkduster.ttf");
		//toast_player_one = Toast.makeText(this, R.string.msg_player_one, Toast.LENGTH_SHORT);
		//toast_player_two = Toast.makeText(this, R.string.msg_player_two, Toast.LENGTH_SHORT);
	}
	
	public void handleButtonForSpace(View space) {
		
		//TODO Extend Button to a custom view that has properties
		// for X, Y in order to remove silly switch()
		
		int x = 0, y = 0;
		((Button) space).setTypeface(chalkduster);
		space.setEnabled(false);
		if (player == Marks.X) {
			((Button) space).setText("X");
		} else {
			((Button) space).setText("O");
		}
		switch(space.getId()) {
			case R.id.button_0_0:
				x = 0;
				y = 0;
				break;
			case R.id.button_0_1:
				x = 0;
				y = 1;
				break;
			case R.id.button_0_2:
				x = 0;
				y = 2;
				break;
			case R.id.button_1_0:
				x = 1;
				y = 0;
				break;
			case R.id.button_1_1:
				x = 1;
				y = 1;
				break;
			case R.id.button_1_2:
				x = 1;
				y = 2;
				break;
			case R.id.button_2_0:
				x = 2;
				y = 0;
				break;
			case R.id.button_2_1:
				x = 2;
				y = 1;
				break;
			case R.id.button_2_2:
				x = 2;
				y = 2;
				break;
		}
		board[x][y] = player;
		checkForWin(x, y, player);
	}
	
	public void checkForWin(int x, int y, Marks mark) {
		
		boolean win = false;
		moves++;
		
		// Check for vertical win
		for (int i = 0; i < 3; i++)
		{
			if (board[x][i] != mark) {
				break;
			}
			if (2 == i) {
				win = true;
			}
		}
		
		// Check for horizontal win
		for (int i = 0; i < 3; i++)
		{
			if (board[i][y] != mark) {
				break;
			}
			if (2 == i) {
				win = true;
			}
		}
		
		// Check diagonal win
		if (x == y) {
			for (int i = 0; i < 3; i++) {
				if (board[i][i] != mark) {
					break;
				}
				if (2 == i) {
					win = true;
				}
			}
		}
		
		for (int i = 0; i < 3; i++) {
			if (board[i][2-i] != mark) {
				break;
			}
			if (2 == i) {
				win = true;
			}
		}
		
		if (win || 8 == moves) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.msg_new_game);
			builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intentToRestart = getIntent();
					startActivity(intentToRestart);
					finish();
				}
			});
			builder.setNegativeButton(R.string.return_to_title, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();					
				}
			});
			builder.setCancelable(false);
			if (win) {
				builder.setTitle("Player " + mark + " wins!");
			} else {
				builder.setTitle("It's a draw!");
			}
			builder.show();
		}
		
		if (false == solo_player) {
			if (player == Marks.X) {
				player = Marks.O;
			} else {
				player = Marks.X;
			}
		} else {
			computerPlayer();
		}
	}
	
	private void computerPlayer() {
		
		return;
	}
	

}
