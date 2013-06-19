package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.service.GameStateService;
import net.codeforeurope.amsterdam.service.GameStateService.GameStateBinder;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;

public abstract class AbstractGameActivity extends Activity {

	protected GameStateService gameStateService;

	protected BroadcastReceiver gameDataUpdateReceiver;

	private boolean mBound = false;

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		setupReceiver();
		bindToGameStateService();

	}

	@Override
	protected void onPause() {

		super.onPause();
		unregisterReceiver(gameDataUpdateReceiver);
	}

//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		setupReceiver();
//
//	}
	
	@Override
	protected void onResume() {
		setupReceiver();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Unbind from the service
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
		}
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			GameStateBinder binder = (GameStateBinder) service;
			gameStateService = binder.getService();
			mBound = true;
			onGameStateServiceConnected();
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	private void setupReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ApiConstants.ACTION_GAME_DATA_UPDATED);
		gameDataUpdateReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				onGameDataUpdated(intent);
			}
		};
		registerReceiver(gameDataUpdateReceiver, filter);
	}

	protected abstract void onGameStateServiceConnected();

	private void bindToGameStateService() {
		Intent intent = new Intent(this, GameStateService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	protected abstract void onGameDataUpdated(Intent intent);

}