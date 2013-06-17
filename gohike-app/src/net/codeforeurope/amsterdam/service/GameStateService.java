package net.codeforeurope.amsterdam.service;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.PingResult;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;

public class GameStateService extends Service {

	private GameData gameData;

	private Profile currentProfile;

	private Route currentRoute;

	private Waypoint currentTarget;

	private final IBinder binder = new GameStateBinder();

	private BroadcastReceiver receiver;

	public class GameStateBinder extends Binder {
		public GameStateService getService() {
			return GameStateService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		return binder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		setupReceiver();
		ensureGameDataLoaded();
	}

	private void ensureGameDataLoaded() {
		if (gameData == null) {
			startService(new Intent(getBaseContext(), ContentService.class));
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	private void setupReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ApiConstants.ACTION_CONTENT_LOADED);
		filter.addAction(ApiConstants.ACTION_REMOTE_CONTENT_LOADED);
		filter.addAction(ApiConstants.ACTION_PING_COMPLETE);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (ApiConstants.ACTION_PING_COMPLETE.equals(action)) {
					processPingResponse(intent);
				} else {
					gameData = intent
							.getParcelableExtra(ApiConstants.GAME_DATA);

					if (ApiConstants.ACTION_REMOTE_CONTENT_LOADED
							.equals(action)) {
						notifyGameDataUpdated();
					} else {
						pingforChanges();
					}
				}

			}

		};
		registerReceiver(receiver, filter);
	}

	protected void pingforChanges() {
		Intent intent = new Intent(getBaseContext(), PingApiService.class);
		intent.putExtra(ApiConstants.CONTENT_VERSION, gameData.version);
		startService(intent);
	}

	public void doUpdateContent() {
		Intent intent = new Intent(getBaseContext(), ContentApiService.class);
		startService(intent);
	}

	protected void notifyGameDataUpdated() {
		Intent intent = new Intent(ApiConstants.ACTION_GAME_DATA_UPDATED);
		sendBroadcast(intent);
	}

	protected void processPingResponse(Intent intent) {
		if (intent.getBooleanExtra(ApiConstants.SUCCESS, false)) {
			PingResult result = intent
					.getParcelableExtra(ApiConstants.PING_RESULT);
			if (!"ok".equalsIgnoreCase(result.status)) {
				Intent promptIntent = new Intent(
						ApiConstants.ACTION_PROMPT_FOR_UPDATE);
				promptIntent.putExtra(ApiConstants.UPDATE_SIZE, result.size);
				sendBroadcast(promptIntent);
			} else {
				notifyGameDataUpdated();
			}
		} else {
			notifyGameDataUpdated();
		}

	}

	public ArrayList<Profile> getProfiles() {
		if (gameData != null) {
			return gameData.profiles;
		}
		return new ArrayList<Profile>();
	}
}
