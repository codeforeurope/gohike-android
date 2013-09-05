package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.dialog.ErrorDialogFragment;
import net.codeforeurope.amsterdam.model.City;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public abstract class AbstractGameActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private final static int GOOGLE_PLAY_CONNECTION_FAILURE = 12345;

	protected ProgressDialog progressDialog;

	protected LocationClient locationClient;

	protected LocationRequest locationRequest;

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		locationClient = new LocationClient(this, this, this);
		locationRequest = LocationRequest.create();
		// Use high accuracy
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		locationRequest.setInterval(1000);
		// Set the fastest update interval to 1 second
		locationRequest.setFastestInterval(1000);

		progressDialog = new ProgressDialog(this);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onPause() {

		super.onPause();
		// unregisterReceiver(gameDataUpdateReceiver);
	}

	protected boolean isCitySelected() {
		return getApp().getSelectedCityId() > 0;
	}

	protected GoHikeApplication getApp() {
		return (GoHikeApplication) getApplication();
	}

	@Override
	protected void onResume() {
		// setupReceiver();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	protected void onGameDataUpdated(Intent intent) {

	}

	protected void gotoGrid() {
		Intent intent = new Intent(getBaseContext(), ContentGridActivity.class);
		startActivity(intent);
		finish();
	}

	protected void saveSelectedCity(City city) {
		getApp().setSelectedCity(city);
	}

	protected String getCurrentCityName() {
		// TODO Auto-generated method stub
		return getApp().getSelectedCityName();
	}

	/*
	 * Google Play setup crap
	 */

	private void showErrorDialog(int errorCode) {
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				this, GOOGLE_PLAY_CONNECTION_FAILURE);

		// If Google Play services can provide an error dialog
		if (errorDialog != null) {
			// Create a new DialogFragment for the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);
			// Show the error dialog in the DialogFragment
			errorFragment.show(getSupportFragmentManager(), "Location Updates");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.android.gms.common.GooglePlayServicesClient.
	 * OnConnectionFailedListener
	 * #onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						GOOGLE_PLAY_CONNECTION_FAILURE);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showErrorDialog(connectionResult.getErrorCode());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
	 * #onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle bundle) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
	 * #onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/**
	 * Moves the screen to city selection screen
	 * 
	 * @param finish
	 */
	public void gotoCityList(boolean finish) {
		Intent intent = new Intent(getBaseContext(), CityListActivity.class);
		startActivity(intent);
		if (finish) {
			finish();
		}
	}

}