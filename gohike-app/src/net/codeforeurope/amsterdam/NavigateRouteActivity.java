package net.codeforeurope.amsterdam;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.dialog.CheckinPromptDialogFragment;
import net.codeforeurope.amsterdam.dialog.NextTargetDialogFragment;
import net.codeforeurope.amsterdam.model.Checkin;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.service.CheckinService;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.LocationListener;

public class NavigateRouteActivity extends AbstractGameActivity implements LocationListener {

	private static final LayoutParams PROGRESS_IMAGE_LAYOUT_PARAMS = new ViewGroup.LayoutParams(
			ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

	private static final int ANIMATION_DURATION = 300;

	private static final int COMPASS_UPDATE_THRESHOLD = 500;

	private static final int CHECKIN_DISTANCE = 15000; // Change to 20 for

	private static final int SHOW_LOCATION_DETAIL_REQUEST_CODE = 1234;

	public boolean isCheckinInProgress = false;

	ViewHolder viewHolder = new ViewHolder();

	SensorHolder sensorHolder = new SensorHolder();

	BroadcastReceiver receiver = new Receiver();

	IntentFilter receiverFilter = new IntentFilter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewHolder.setUpViewReferences();
		sensorHolder.setupSensorReferences();
		receiverFilter.addAction(ActionConstants.CHECKIN_SAVE_COMPLETE);
	}

	@Override
	protected void setupActionBar() {
		super.setupActionBar();
		actionBar.setTitle(getCurrentRoute().name.getLocalizedValue());
	}

	private void loadData() {
		if (isRouteFinished()) {
			isCheckinInProgress = false;
			Intent intent = new Intent(this, RewardActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
		} else {
			Waypoint currentTarget = getCurrentTarget();
			viewHolder.targetName.setText(currentTarget.name.getLocalizedValue());
			viewHolder.distanceText.setText("...");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			onBackPressed();
			return true;
		case R.id.menu_show_map:
			Intent i = new Intent(getBaseContext(), OrientationMapActivity.class);
			startActivity(i);
			overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.route_navigate, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onResume() {
		super.onResume();
		sensorHolder.startListeners();
		locationClient.connect();
		loadData();
		updateProgress();
		registerReceiver(receiver, receiverFilter);

	}

	@Override
	public void onPause() {
		super.onPause();
		sensorHolder.stopListeners();
		if (locationClient.isConnected()) {
			locationClient.removeLocationUpdates(this);
		}
		locationClient.disconnect();
		unregisterReceiver(receiver);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (!isCheckinInProgress && !isRouteFinished()) {
			Waypoint currentTarget = getCurrentTarget();
			final float[] results = new float[3];
			Location.distanceBetween(location.getLatitude(), location.getLongitude(), currentTarget.latitude,
					currentTarget.longitude, results);
			updateDistance(results[0]);
			sensorHolder.targetBearing = results[2];
			if (results[0] < CHECKIN_DISTANCE) {
				Bundle dialogArgs = new Bundle();
				dialogArgs.putParcelable(DataConstants.CURRENT_TARGET, currentTarget);
				viewHolder.checkinDialog.setArguments(dialogArgs);
				FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
				transaction.add(viewHolder.checkinDialog, "checkin");
				transaction.commitAllowingStateLoss();

				isCheckinInProgress = true;
			}
		}

	}

	private void updateDistance(float rawDistance) {
		String distance;
		if (rawDistance > 1000) {
			distance = getString(R.string.target_distance_km, rawDistance / 1000);
		} else {
			distance = getString(R.string.target_distance_m, rawDistance);
		}

		viewHolder.distanceText.setText(distance);

	}

	public void doCheckin() {
		Intent checkinIntent = new Intent(getBaseContext(), CheckinService.class);
		checkinIntent.putExtra(DataConstants.CURRENT_TARGET, getCurrentTarget());
		startService(checkinIntent);

	}

	public void doDismissTargetHint() {
		// set that the check-in process has finished
		isCheckinInProgress = false;

	}

	private void showLocationDetail() {
		Intent intent = new Intent(getBaseContext(), LocationDetailActivity.class);
		intent.putExtra(DataConstants.CURRENT_TARGET, getCurrentTarget());
		intent.putExtra(ApiConstants.JUST_FOUND, true);
		startActivityForResult(intent, SHOW_LOCATION_DETAIL_REQUEST_CODE);
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SHOW_LOCATION_DETAIL_REQUEST_CODE) {
			advanceToNextTarget();
			showTargetHintDialog();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void advanceToNextTarget() {
		Waypoint currentTarget = getCurrentTarget();
		ArrayList<Waypoint> allWaypoints = getCurrentRoute().waypoints;
		int currentTargetIndex = allWaypoints.indexOf(currentTarget);
		int numberOfWaypoints = allWaypoints.size();
		int limit = currentTargetIndex + numberOfWaypoints;
		for (int i = currentTargetIndex; i < limit; i++) {
			int index = i;
			if (i >= numberOfWaypoints) {
				index = i - numberOfWaypoints;
			}
			if (!isWaypointCheckedIn(allWaypoints.get(index))) {
				getIntent().putExtra(DataConstants.CURRENT_TARGET, allWaypoints.get(index));
				loadData();
				updateProgress();
				break;
			}
		}

	}

	private void updateProgress() {

		ArrayList<Waypoint> waypoints = getCurrentRoute().waypoints;
		int numberOfWaypoints = waypoints.size();
		if (viewHolder.progressView.getChildCount() != numberOfWaypoints) {
			viewHolder.progressView.removeAllViews();
		}
		for (int i = 0; i < numberOfWaypoints; i++) {
			Waypoint waypoint = waypoints.get(i);
			ImageView progressImage = (ImageView) viewHolder.progressView.getChildAt(i);
			if (progressImage == null) {
				progressImage = new ImageView(getBaseContext());
				progressImage.setLayoutParams(PROGRESS_IMAGE_LAYOUT_PARAMS);
				viewHolder.progressView.addView(progressImage);
			}
			if (isWaypointCheckedIn(waypoint)) {
				progressImage.setImageResource(R.drawable.progress_check);
			} else {
				progressImage.setImageResource(R.drawable.progress_target);
			}

		}

	}

	private void showTargetHintDialog() {
		Bundle dialogArgs = new Bundle();
		dialogArgs.putParcelable(DataConstants.CURRENT_TARGET, getCurrentTarget());

		viewHolder.targetHintDialog.setArguments(dialogArgs);
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(viewHolder.targetHintDialog, "found");
		transaction.commitAllowingStateLoss();
		isCheckinInProgress = true;
	}

	@Override
	public void onConnected(Bundle bundle) {
		locationClient.requestLocationUpdates(locationRequest, this);

	}

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ActionConstants.CHECKIN_SAVE_COMPLETE.equals(action)) {
				Checkin localCheckin = intent.getParcelableExtra(DataConstants.CHECKIN);
				getApp().addLocalCheckin(localCheckin);
				showLocationDetail();
			}
		}
	}

	class ViewHolder {

		ImageView compassRose;
		ImageView compassTarget;
		TextView distanceText;
		TextView targetName;
		LinearLayout progressView;

		CheckinPromptDialogFragment checkinDialog;
		NextTargetDialogFragment targetHintDialog;

		public ViewHolder() {
			checkinDialog = new CheckinPromptDialogFragment();
			targetHintDialog = new NextTargetDialogFragment();
		}

		void setUpViewReferences() {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setContentView(R.layout.navigate);
			compassRose = (ImageView) findViewById(R.id.navigate_rose);
			compassTarget = (ImageView) findViewById(R.id.navigate_target);
			distanceText = (TextView) findViewById(R.id.navigate_overlay_target_distance);
			targetName = (TextView) findViewById(R.id.navigate_overlay_target_text);
			progressView = (LinearLayout) findViewById(R.id.navigate_progress);
		}

	}

	class SensorHolder implements SensorEventListener {

		SensorManager sensorManager;
		float targetBearing = 0;
		Sensor accelerometer;
		Sensor magnetometer;
		float[] gravityMatrix;
		float[] geomagneticMatrix;
		long timestamp = 0;
		float azimuth = 0;

		void setupSensorReferences() {
			sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
			accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		}

		public void stopListeners() {
			sensorManager.unregisterListener(this);
		}

		public void startListeners() {
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

			// Do nothing
		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				gravityMatrix = event.values;
			}
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				geomagneticMatrix = event.values;
			}
			if (gravityMatrix != null && geomagneticMatrix != null) {

				float R[] = new float[9];
				float I[] = new float[9];
				boolean success = SensorManager.getRotationMatrix(R, I, gravityMatrix, geomagneticMatrix);

				long difference = (event.timestamp - timestamp) / 1000000;

				if (success && event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
						&& difference > COMPASS_UPDATE_THRESHOLD) {
					timestamp = event.timestamp;
					float orientation[] = new float[3];
					SensorManager.getOrientation(R, orientation);
					azimuth = -1 * (float) Math.toDegrees(orientation[0]);
					viewHolder.compassRose.animate().setDuration(ANIMATION_DURATION).rotation(360 + azimuth);
					viewHolder.compassTarget.animate().setDuration(ANIMATION_DURATION)
							.rotation(360 + azimuth + targetBearing);

				}
			}

		}
	}
}
