package com.exposuresoftware.xsandos;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeEventListener implements SensorEventListener {
	
	private static final String TAG = "Xs And Os - Shake";
	
	private static final int MIN_FORCE = 40;
	private static final int MIN_DIRECTION_CHANGE = 2;
	private static final int MAX_PAUSE_BETWEEN_DIRECTION_CHANGE = 200;
	private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;
	private long firstDirectionChangeTime = 0;
	private long lastDirectionChangeTime;
	private int directionChangeCount = 0;
	private float lastX = 0;
	private float lastY = 0;
	private float lastZ = 0;
	private OnShakeListener shakeListener;
  
	public interface OnShakeListener {
		void onShake();
	}
	
	public void setOnShakeListener(OnShakeListener listener) {
		shakeListener = listener;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// Get our position
		float x = event.values[SensorManager.DATA_X];
		float y = event.values[SensorManager.DATA_Y];
		float z = event.values[SensorManager.DATA_Z];
		//Log.d( TAG, "New position: " + x + ", " + y + ", " + z );
		
		// Get the movement
		// TODO Break out paper and see why this works!
		float moved = Math.abs(x + y + z - lastX - lastY - lastZ);
		
		// If moved more then our threshold
		if ( moved > MIN_FORCE ) {
			// Note the time for durations
			long eventOccuredTime = System.currentTimeMillis();
			
			// Is this the first movement?
			if ( 0 == firstDirectionChangeTime ) {
				// Yes, set our comparison values
				firstDirectionChangeTime = eventOccuredTime;
				lastDirectionChangeTime = eventOccuredTime;
			}
			
			long changeFrequency = eventOccuredTime - lastDirectionChangeTime;
			if ( MAX_PAUSE_BETWEEN_DIRECTION_CHANGE > changeFrequency ) {
				lastDirectionChangeTime = eventOccuredTime;
				directionChangeCount++;
				lastX = x;
				lastY = y;
				lastZ = z;
				if ( MIN_DIRECTION_CHANGE <= directionChangeCount ) {
					// How long has this been going on?
					long totalDuration = eventOccuredTime - firstDirectionChangeTime;
					if ( MAX_TOTAL_DURATION_OF_SHAKE > totalDuration ) {
						// We have a shake!
						Log.d( TAG, "Shake detected" );
						shakeListener.onShake();
						resetShakes();
					}
				}
			} else {
				resetShakes();
			}
		}
	}
	
	private void resetShakes() {
		firstDirectionChangeTime = 0;
	    directionChangeCount = 0;
	    lastDirectionChangeTime = 0;
	    lastX = 0;
	    lastY = 0;
	    lastZ = 0;
	}
	
}
