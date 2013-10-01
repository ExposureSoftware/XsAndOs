package com.exposuresoftware.xsandos;

import android.util.Log;

// Handles the win condition checks, and marks for each space.

public class GameBoard {
	
	private static final String TAG = "Xs and Os - GameBoard";
	
	enum Mark{BLANK, X, O};
	enum State{IN_PROGRESS, WIN, DRAW};
	public Mark[][] board = null;
	private int size = 0;
	State gameState = State.IN_PROGRESS;
	Mark winner = Mark.BLANK;
	private int moves = 0;
	
	public GameBoard(int size) {
		this.board = new Mark[size][size];
		this.size = size;
		// TODO Find a better way to initialize to blanks
		for ( int x = 0; x < size; x++ ) {
			for ( int y = 0; y < size; y++ ) {
				this.board[x][y] = Mark.BLANK;
			}
		}
	}
	
	public boolean markSpace(int x, int y, Mark mark) {
		// Checks for space availability and marks if possible
		// Then calls checkboard
		
		boolean marked = false;
		if (Mark.X != board[x][y] && Mark.O != board[x][y]) {
			board[x][y] = mark;
			marked = checkBoard(x, y, mark);
			Log.d(TAG, "Player " + mark.toString() + " marks " + Integer.toString(x)
					+ "," + Integer.toString(y));
		} else {
			Log.d(TAG, "Attempted to mark used space " + x + "," + y);
		}
		return marked;
	}
	
	public boolean checkBoard(int x, int y, Mark mark) {
		// Checks for end game scenarios
		
		boolean marked = true;
		
		// Check for vertical win
		for (int i = 0; i < size; i++) {
			if (board[x][i] != mark) {
				break;
			}
			if (i == size - 1) {
				marked = false;
				gameState = State.WIN;
				winner = mark;
			}
		}
		
		// Check for horizontal win
		for (int i = 0; i < size; i++)
		{
			if (board[i][y] != mark) {
				break;
			}
			if (i == size - 1) {
				marked = false;
				gameState = State.WIN;
				winner = mark;
			}
		}
		
		// Check diagonal win
		if (x == y) {
			for (int i = 0; i < size; i++) {
				if (board[i][i] != mark) {
					break;
				}
				if (i == size - 1) {
					marked = false;
					gameState = State.WIN;
					winner = mark;
				}
			}
		}
		
		// Check the other diagonal
		for (int i = 0; i < size; i++) {
			if (board[i][2 - i] != mark) {
				break;
			}
			if (i == size - 1) {
				marked = false;
				gameState = State.WIN;
				winner = mark;
			}
		}
		
		//Check for draw
		Log.d( TAG, moves + " moves made of " + Integer.toString(size*size-1) + " before draw." );
		moves++;
		if (moves == (size*size - 1)) {
			marked = false;
			gameState = State.DRAW;
		}
		
		return marked;
	}
	
	public Mark getMarkForSpace(int x, int y) {
		Mark space = null;
		if (null != board[x][y]) {
			space = board[x][y];
		} else {
			space = Mark.BLANK;
		}
		return space;
	}
	
}
