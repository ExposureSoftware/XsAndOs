package com.exposuresoftware.xsandos;

import java.lang.reflect.Array;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.exposuresoftware.xsandos.GameBoard.Mark;

public class Game extends Activity {
	
	private static final int SIZE = 3;
	private static final String TAG = "Xs And Os - Game";
	
	boolean solo_player = false; 
	Typeface chalkduster = null;
	int moves = 0;
	GameBoard.Mark player = GameBoard.Mark.X;
	GameBoard board = null;
	int[][] buttons = new int[3][3];
	
	// TODO Add shake to clear
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_game);
		solo_player = getIntent().getExtras().getBoolean("solo");
		chalkduster = Typeface.createFromAsset(getAssets(), 
        		"fonts/Chalkduster.ttf");
		// TODO Show dialog to say what player starts.
		board = new GameBoard(SIZE);
		Log.d(TAG, "Game board created.");
		// TODO Add setting for random or X starts
		/*
		Random randomGen = new Random();
		if (randomGen.nextBoolean()) {
			player = GameBoard.Mark.X;
			Log.d(TAG, "Player X selected to go first.");
		} else {
			player = GameBoard.Mark.O;
			Log.d(TAG, "Player O selected to go first.");
			if (solo_player) {
				computerPlayer();
			}
		}
		*/
		player = GameBoard.Mark.X;
	}
	
	public void onResume() {
		super.onResume();
		Log.d( TAG,  "Building button ID array" );
		buildButtonIds();
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
			AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
			aBuilder.setMessage(R.string.msg_new_game);
			aBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intentToRestart = getIntent();
					startActivity(intentToRestart);
					finish();
				}
			});
			aBuilder.setNegativeButton(R.string.return_to_title, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();					
				}
			});
			switch (board.gameState) {
				case WIN:
					aBuilder.setTitle("Player " + player.toString() + " wins!");
					aBuilder.show();
					break;
				case DRAW:
					aBuilder.setTitle("Game is a draw!");
					aBuilder.show();
					break;
				case IN_PROGRESS:
			}
		}
		switchPlayer();
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
			//handleButtonForSpace( button );
			//board.markSpace(first_choice.x, first_choice.y, player);
		} else {
			/*
			 * Corners are crucial top-tier strategy. Once taken, the middle space
			 * is prioritized. After that, winning_space should contain a valid blank
			 * space to use, if no win condition is found here it likely the game will
			 * draw in any case.
			 */
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
