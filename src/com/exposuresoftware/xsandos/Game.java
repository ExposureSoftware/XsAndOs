package com.exposuresoftware.xsandos;

import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.exposuresoftware.xsandos.GameBoard.Mark;
import com.google.analytics.tracking.android.EasyTracker;


public class Game extends Activity {
	
	private static final int SIZE = 3;
	private static final String TAG = "Xs And Os - Game";
	
	boolean solo_player = false; 
	Typeface chalkduster = null;
	int moves = 0;
	GameBoard.Mark player = GameBoard.Mark.X;
	GameBoard board = null;
	int[][] buttons = new int[3][3];
	
	private SensorManager sensorManager;
	private ShakeEventListener sensorListener;
	
	Toast toast_player_x = null, toast_player_o = null;
	
	boolean dialog_up = false;
	
	// TODO Add shake to clear
	// TODO Move switchPlayer() out of handleButtonForSpace(View)
	
	@SuppressLint("ShowToast")
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.d( TAG, "Starting Game Activity" );
		setContentView(R.layout.activity_game);
		solo_player = getIntent().getExtras().getBoolean("solo");
		chalkduster = Typeface.createFromAsset(getAssets(), 
        		"fonts/Chalkduster.ttf");
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    sensorListener = new ShakeEventListener();   

	    sensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

	      public void onShake() {
	    	  if ( !dialog_up && PreferenceManager.getDefaultSharedPreferences( Game.this )
	  				.getBoolean( "pref_key_shake_to_clear", true ) ) {
		    	  	Typeface chalkduster = Typeface.createFromAsset(getAssets(), 
			        		"fonts/Chalkduster.ttf");
					final Dialog dialog = new Dialog(Game.this, R.style.CleanDialog);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView( R.layout.dialog_base );
					dialog.setCancelable( false );			
					TextView title = (TextView) dialog.findViewById( R.id.dialog_header );
					title.setTypeface(chalkduster);
					TextView message = 	(TextView) dialog.findViewById(R.id.dialog_message);
					message.setTypeface(chalkduster);
					Button ok_button = (Button) dialog.findViewById(R.id.dialog_new_game);
					Button title_button = (Button) dialog.findViewById(R.id.dialog_title_screen);
					ok_button.setTypeface( chalkduster );
					title_button.setTypeface(chalkduster);
					// Now, change what they say!
					title.setText(R.string.dialog_shake_header);
					ok_button.setText(R.string.new_game);
					title_button.setText(R.string.cancel);
					message.setText(R.string.clear_board);
					ok_button.setOnClickListener( new OnClickListener() {
							public void onClick(View view) {
								Log.d( TAG, "Restarting game" );
								Intent intentToRestart = getIntent();
								startActivity(intentToRestart);
								dialog.dismiss();
								finish();
							}
						});
					title_button.setOnClickListener( new OnClickListener() {
						public void onClick(View view) {
							Log.d( TAG, "Shake cancelled" );
							dialog_up = false;
							dialog.dismiss();
						}
					});
					dialog.show();
					dialog_up = true;
	    	  }
	      }
	    });
		board = new GameBoard(SIZE);
		Log.d(TAG, "Game board created.");		
		this.toast_player_x = Toast.makeText(this, R.string.msg_player_one, Toast.LENGTH_SHORT);
		this.toast_player_o = Toast.makeText(this, R.string.msg_player_two, Toast.LENGTH_SHORT);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}
	
	public void onResume() {
		super.onResume();
		Log.d( TAG,  "Building button ID array" );
		buildButtonIds();
		// Register shake listener
		sensorManager.registerListener(sensorListener,
		        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
		        SensorManager.SENSOR_DELAY_UI);
		// TODO Make sure orientation change doesn't restart game with this!
		boolean random_start = PreferenceManager.getDefaultSharedPreferences( this )
				.getBoolean( "pref_key_random_start", true ); 
		Log.d( TAG, "Random starting player: " + Boolean.toString( random_start ) );
		if ( random_start ) {
			Random randomGen = new Random();
			if (randomGen.nextBoolean()) {
				player = GameBoard.Mark.X;
				Log.d(TAG, "Player X selected to go first.");
				toast_player_x.show();
			} else {
				Log.d(TAG, "Player O selected to go first.");
				toast_player_o.show();
				switchPlayer();
			}
		}
	}
	
	@Override
	public void onPause() {
		sensorManager.unregisterListener(sensorListener);
		super.onPause();
	}
	
	@Override
	public void onStop() {
		EasyTracker.getInstance(this).activityStop(this);
		super.onStop();
	}
	
	private void buildButtonIds() {
		buttons[0][0] = R.id.button_0_0;
		buttons[0][1] = R.id.button_0_1;
		buttons[0][2] = R.id.button_0_2;
		buttons[1][0] = R.id.button_1_0;
		buttons[1][1] = R.id.button_1_1;
		buttons[1][2] = R.id.button_1_2;
		buttons[2][0] = R.id.button_2_0;
		buttons[2][1] = R.id.button_2_1;
		buttons[2][2] = R.id.button_2_2;
	}
	
	public void handleButtonForSpace(View space) {
		
		int x = 0, y = 0;
		((Button) space).setTypeface(chalkduster);
		x = ((ESCoordButton) space).x;
		y = ((ESCoordButton) space).y;
		Log.d( TAG, "Button pressed at " + Integer.toString(x) + "," + Integer.toString(y) 
				+ " by player " + player.toString() );
		if (board.markSpace(x, y, player)) {
			Log.d( TAG, "Valid entry, marking space" );
			((Button) space).setText(player.toString());
			space.setEnabled(false);
		} else {
			Typeface chalkduster = Typeface.createFromAsset(getAssets(), 
	        		"fonts/Chalkduster.ttf");
			final Dialog dialog = new Dialog(this, R.style.CleanDialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView( R.layout.dialog_base );
			dialog.setCancelable( false );			
			TextView title = (TextView) dialog.findViewById( R.id.dialog_header );
			title.setTypeface(chalkduster);
			((TextView) dialog.findViewById(R.id.dialog_message)).setTypeface(chalkduster);
			Button ok_button = (Button) dialog.findViewById(R.id.dialog_new_game);
			Button title_button = (Button) dialog.findViewById(R.id.dialog_title_screen);
			ok_button.setTypeface( chalkduster );
			title_button.setTypeface(chalkduster);
			ok_button.setOnClickListener( new OnClickListener() {
					public void onClick(View view) {
						Log.d( TAG, "Restarting game" );
						Intent intentToRestart = getIntent();
						startActivity(intentToRestart);
						dialog.dismiss();
						finish();
					}
				});
			title_button.setOnClickListener( new OnClickListener() {
				public void onClick(View view) {
					Log.d( TAG, "Returning to title" );
					dialog.dismiss();
					finish();
				}
			});
			switch (board.gameState) {
				case WIN:
					title.setText("Player " + player.toString() + " wins!");
					dialog.show();
					break;
				case DRAW:
					title.setText("Game is a draw!");
					dialog.show();
					break;
				case IN_PROGRESS:
			}
		}
		if ( board.gameState == GameBoard.State.IN_PROGRESS ) {
			switchPlayer();
		}
	}
	
	private void switchPlayer() {
		Log.d(TAG, "Switching player");
		if (GameBoard.Mark.X == player) {
			Log.d(TAG, "Player now O");
			player = GameBoard.Mark.O;
			if (solo_player) {
				computerPlayer();
			}
		} else {
			player = GameBoard.Mark.X;
			Log.d(TAG, "Player now X");
		}
	}
		
	private void computerPlayer() {
		Log.d(TAG, "Computing move");
		Space first_choice = findWinningSpace();
		View button = null;
		if ( first_choice.winning || first_choice.counter ) {
			Log.d( TAG, "Marking first choice: " + first_choice.x + "," + first_choice.y );
			button = findViewById( buttons[first_choice.x][first_choice.y] );
		} else {
			/*
			 * Corners are crucial top-tier strategy. Once taken, the middle space
			 * is prioritized. After that, winning_space should contain a valid blank
			 * space to use, if no win condition is found here it likely the game will
			 * draw in any case.
			 */
			// BUG Not selecting winners first
			Log.d( TAG, "Trying scheduled spaces" );
			if ( findViewById( buttons[0][0] ).isEnabled() ) {
				button = findViewById( buttons[0][0] );
			} else if ( findViewById( buttons[0][2] ).isEnabled() ) {
				button = findViewById( buttons[0][2] );
			} else if ( findViewById( buttons[2][0] ).isEnabled() ) {
				button = findViewById( buttons[2][0] );
			} else if ( findViewById( buttons[2][2] ).isEnabled() ) {
				button = findViewById( buttons[2][2] );
			} else if ( findViewById( buttons[1][1] ).isEnabled() ) {
				button = findViewById( buttons[1][1] );
			} else {
				button = findViewById( buttons[first_choice.x][first_choice.y] );
			}
		}
		handleButtonForSpace( button );
	}
	
	private Space findWinningSpace() {
		Log.d(TAG, "Attempting to locate winning space");
		Space winning_space = new Space();
		Mark[][] current_board = board.board;
		// Check win conditions, and counter
		Log.d(TAG, "Checking columns");
		for (int y = 0; y < SIZE && ( !winning_space.winning && !winning_space.counter ); y++) {
			int x_count = 0, o_count = 0;
			for ( int x = 0; x < SIZE; x++ ) {
				Mark mark_in_space = current_board[x][y];
				switch ( mark_in_space ) {
					case X:
						x_count++;
						break;
					case O:
						o_count++;
						break;
					case BLANK:
						if ( !winning_space.winning && !winning_space.counter ) {
							winning_space.x = x;
							winning_space.y = y;
						}
					default:
						break;
				}
			}
			if ( o_count == SIZE - 1  && SIZE - o_count > x_count ) {
				Log.d( TAG, "Win found in column " + y );
				winning_space.winning = true;
			}
			if ( x_count == SIZE - 1 && SIZE - x_count > o_count ) {
				Log.d( TAG, "Counter found in column " + y );
				winning_space.counter = true;
			}
		}
		
		Log.d( TAG, "Checking rows" );
		for (int x = 0; x < SIZE && ( !winning_space.winning && !winning_space.counter ); x++)
		{
			int x_count = 0, o_count = 0;
			for ( int y = 0; y < SIZE; y++ ) {
				switch ( current_board[x][y] ) {
					case X:
						x_count++;
						break;
					case O:
						o_count++;
						break;
					case BLANK:
						if ( !winning_space.winning && !winning_space.counter ) {
							winning_space.x = x;
							winning_space.y = y;
						}
					default:
						break;
				}
			}
			if ( o_count == SIZE - 1 && SIZE - o_count > x_count ) {
				Log.d( TAG, "Win found in row " + x );
				winning_space.winning = true;
			}
			if ( x_count == SIZE - 1 && SIZE - x_count > o_count ) {
				Log.d( TAG, "Counter found in row " + x );
				winning_space.counter = true;
			}
		}
		
		Log.d( TAG, "Checking diagonals" );
		int x_count = 0, o_count = 0, y = 0, x = 0;
		for ( x = 0; x < SIZE && ( !winning_space.winning && !winning_space.counter ); x++ ) {
			switch ( current_board[x][y] ) {
			case X:
				x_count++;
				break;
			case O:
				o_count++;
				break;
			case BLANK:
				if ( !winning_space.winning && !winning_space.counter ) {
					winning_space.x = x;
					winning_space.y = y;
				}
			default:
				break;
			}
			y++;
		}
		if ( o_count == SIZE - 1 && SIZE - o_count > x_count ) {
			Log.d( TAG, "Win found in diagonal" );
			winning_space.winning = true;
		}
		if ( x_count == SIZE - 1 && SIZE - x_count > o_count ) {
			Log.d( TAG, "Counter found in diagonal" );
			winning_space.counter = true;
		}
				
		x_count = 0;
		o_count = 0;
		x = 0;
		for ( y = 0; y < SIZE && ( !winning_space.winning && !winning_space.counter ); y++ ) {
			switch ( current_board[(SIZE - 1) - y][y] ) {
			case X:
				x_count++;
				break;
			case O:
				o_count++;
				break;
			case BLANK:
				if ( !winning_space.winning && !winning_space.counter ) {
					winning_space.x = ( SIZE - 1 ) - y;
					winning_space.y = y;
				}
			default:
				break;
			}
		}
		if ( o_count == SIZE - 1 && SIZE - o_count > x_count ) {
			Log.d( TAG, "Win found in anti-diagonal" );
			winning_space.winning = true;
		}
		if ( x_count == SIZE - 1 && SIZE - x_count > o_count ) {
			Log.d( TAG, "Counter found in anti-diagonal" );
			winning_space.counter = true;
		}
		
		return winning_space;
	}

}
