package net.codeforeurope.amsterdam;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.dialog.FirstLaunchDialogFragment;
import net.codeforeurope.amsterdam.model.Checkin;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.service.CheckinService;
import net.codeforeurope.amsterdam.service.ImageDownloadService;
import net.codeforeurope.amsterdam.service.LocalRoutesService;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class SplashActivity extends AbstractGameActivity {

	FirstLaunchDialogFragment dialog = new FirstLaunchDialogFragment();

	Receiver receiver = new Receiver();

	IntentFilter receiverFilter = new IntentFilter();

	boolean routesLoadComplete = false;
	boolean checkinsLoadComplete = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		receiverFilter.addAction(ActionConstants.ROUTES_LOAD_COMPLETE);
		receiverFilter.addAction(ActionConstants.CHECKINS_LOAD_COMPLETE);
		receiverFilter.addAction(ActionConstants.IMAGE_DOWNLOAD_COMPLETE);
		receiverFilter.addAction(ActionConstants.IMAGE_DOWNLOAD_PROGRESS);

	}

	@Override
	protected void onStart() {
		super.onStart();
		loadLocalCheckins();
		loadLocalRoutes();
	}

	private void loadLocalRoutes() {
		Intent loadRoutesIntent = new Intent(getBaseContext(), LocalRoutesService.class);
		loadRoutesIntent.setAction(ActionConstants.LOAD_ROUTES);
		startService(loadRoutesIntent);
	}

	private void loadLocalCheckins() {
		Intent loadCheckinsIntent = new Intent(getBaseContext(), CheckinService.class);
		loadCheckinsIntent.setAction(ActionConstants.LOAD_CHECKINS);
		startService(loadCheckinsIntent);

	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, receiverFilter);

	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ActionConstants.IMAGE_DOWNLOAD_COMPLETE.equals(action)) {
				ArrayList<Route> routes = intent.getParcelableArrayListExtra(DataConstants.LOCAL_ROUTES);
				getApp().storeLocalRoutes(routes);
				routesLoadComplete = true;
			} else if (ActionConstants.ROUTES_LOAD_COMPLETE.equals(action)) {
				Intent downloadIntent = new Intent(getApplicationContext(), ImageDownloadService.class);
				downloadIntent.setAction(ActionConstants.ROUTES_LOAD_COMPLETE);
				downloadIntent.putExtra(DataConstants.LOCAL_ROUTES,
						intent.getParcelableArrayListExtra(DataConstants.LOCAL_ROUTES));
				startService(downloadIntent);

			} else if (ActionConstants.CHECKINS_LOAD_COMPLETE.equals(action)) {
				ArrayList<Checkin> checkins = intent.getParcelableArrayListExtra(DataConstants.LOCAL_CHECKINS);
				getApp().storeLocalCheckins(checkins);
				checkinsLoadComplete = true;
			}
			if (routesLoadComplete && checkinsLoadComplete) {
				if (!isCitySelected()) {
					dialog.show(getSupportFragmentManager(), "first_launch");
				} else {
					gotoContentGrid();
				}
			}

		}

	}

}
