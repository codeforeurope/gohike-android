package net.codeforeurope.amsterdam.service;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.model.Checkin;
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

	public class GameStateBinder extends Binder {
		public GameStateService getService() {
			return GameStateService.this;
		}
	}

	private GameData gameData;

	private Profile currentProfile;

	private Route currentRoute;

	private Waypoint currentTarget;

	private ArrayList<Checkin> checkins = new ArrayList<Checkin>();

	private final IBinder binder = new GameStateBinder();

	private BroadcastReceiver receiver;

	public void doUpdateContent() {
		Intent intent = new Intent(getBaseContext(), ContentApiService.class);
		startService(intent);
	}

	private void ensureGameDataLoaded() {
		if (gameData == null) {
			startService(new Intent(getBaseContext(), ContentService.class));
		}
		if (checkins == null) {
			startService(new Intent(getBaseContext(), CheckinService.class)
					.setAction(ApiConstants.ACTION_LOAD_CHECKINS));
		}

	}

	public ArrayList<Checkin> getCheckins() {
		return checkins;
	}

	public Profile getCurrentProfile() {
		return currentProfile;
	}

	public Route getCurrentRoute() {
		return currentRoute;
	}

	public Waypoint getCurrentTarget() {
		return currentTarget;
	}

	public ArrayList<Profile> getProfiles() {
		if (gameData != null) {
			return gameData.profiles;
		}
		return new ArrayList<Profile>();
	}

	protected void notifyGameDataUpdated() {
		if (gameData != null && checkins != null) {
			Intent intent = new Intent(ApiConstants.ACTION_GAME_DATA_UPDATED);
			sendBroadcast(intent);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		return binder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setupReceiver();
		ensureGameDataLoaded();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	protected void pingforChanges() {
		Intent intent = new Intent(getBaseContext(), PingApiService.class);
		intent.putExtra(ApiConstants.CONTENT_VERSION, gameData.version);
		startService(intent);
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

	public void setCurrentProfile(Profile currentProfile) {
		this.currentProfile = currentProfile;
	}

	public void setCurrentRoute(Route currentRoute) {
		this.currentRoute = currentRoute;
	}

	public void setCurrentTarget(Waypoint currentTarget) {
		this.currentTarget = currentTarget;
	}

	private void setupReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ApiConstants.ACTION_CONTENT_LOADED);
		filter.addAction(ApiConstants.ACTION_REMOTE_CONTENT_LOADED);
		filter.addAction(ApiConstants.ACTION_PING_COMPLETE);
		filter.addAction(ApiConstants.ACTION_CHECKINS_LOADED);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (ApiConstants.ACTION_PING_COMPLETE.equals(action)) {
					processPingResponse(intent);
				} else {
					if (intent.hasExtra(ApiConstants.GAME_DATA)) {
						gameData = intent
								.getParcelableExtra(ApiConstants.GAME_DATA);
					} else if (intent.hasExtra(ApiConstants.LOCAL_CHECKINS)) {
						checkins = intent
								.getParcelableArrayListExtra(ApiConstants.LOCAL_CHECKINS);
					}

					if (ApiConstants.ACTION_REMOTE_CONTENT_LOADED
							.equals(action)
							|| ApiConstants.ACTION_CHECKINS_LOADED
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

	public boolean isWaypointCheckedIn(Waypoint waypoint) {
		for (Checkin checkin : checkins) {
			if (checkin.locationId == waypoint.id
					&& checkin.routeId == waypoint.routeId) {
				return true;
			}
		}
		return false;

	}

	public Waypoint getNextTarget() {
		// Returns the first not visited location
		int waypoints = currentRoute.waypoints.size();
		for (int i = 0; i < waypoints; i++) {
			Waypoint w = currentRoute.waypoints.get(i);
			if (isWaypointCheckedIn(w) != true) {
				return w;

			}
		}
		return null;
	}

}
