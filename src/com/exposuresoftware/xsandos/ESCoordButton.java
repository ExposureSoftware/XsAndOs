package com.exposuresoftware.xsandos;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

public class ESCoordButton extends Button {
	
	int x = 0;
	int y = 0;
	
	public ESCoordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public ESCoordButton(Context context, AttributeSet attrs, int id) {
		super(context, attrs, id);
		init(attrs);
	}
	
	public ESCoordButton(Context context) {
		super(context);
	}
	
	public ESCoordButton(Context context, int x, int y) {
		super(context);
		this.x = x;
		this.y = y;
	}
	
	private void init(AttributeSet attrs) {
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ESCoordButton);
		x = a.getInteger(R.styleable.ESCoordButton_x, x);
		y = a.getInteger(R.styleable.ESCoordButton_y, y);
	}

}
