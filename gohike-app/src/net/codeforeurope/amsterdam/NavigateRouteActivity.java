package net.codeforeurope.amsterdam;

import java.util.List;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.R.integer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigateRouteActivity extends Activity implements
		SensorEventListener, LocationListener {
	private static final int ANIMATION_DURATION = 300;

	private static final int COMPASS_UPDATE_THRESHOLD = 500;
	
	private static final int CHECKIN_DISTANCE = 4000; //Change to 20 for production

	GameData gameData;

	Profile currentProfile;

	Route currentRoute;

	Waypoint currentTarget;

	SensorManager sensorManager;

	ImageView compassRose;

	ImageView compassTarget;

	TextView debug;

	float targetBearing = 0;

	private Sensor accelerometer;

	private Sensor magnetometer;

	LocationManager locationManager;
	
	boolean checkInWindowOnScreen = false; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setUpViewReferences();
		loadData();

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentRoute.nameEn);

	}

	private void loadData() {
		gameData = getIntent().getParcelableExtra(ApiConstants.GAME_DATA);
		currentProfile = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_PROFILE);
		currentRoute = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_ROUTE);

		currentTarget = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_TARGET);
	}

	private void setUpViewReferences() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.navigate);
		compassRose = (ImageView) findViewById(R.id.navigate_rose);
		compassTarget = (ImageView) findViewById(R.id.navigate_target);
		debug = (TextView) findViewById(R.id.debug);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_left, R.anim.leave_to_right);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			onBackPressed();
			return true;
		case R.id.menu_show_map:

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
		sensorManager.registerListener(this, accelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, magnetometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		List<String> providers = locationManager.getAllProviders();
		for (String provider : providers) {
			locationManager.requestLocationUpdates(provider, 400, 0, this);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
		locationManager.removeUpdates(this);
	}

	float[] mGravity;
	float[] mGeomagnetic;
	long timestamp = 0;
	float azimuth = 0;
	
	public void onSensorChanged(SensorEvent event) {
		
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values;

		if (mGravity != null && mGeomagnetic != null) {

			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);

			long difference = (event.timestamp - timestamp) / 1000000;

			if (success && event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD
					&& difference > COMPASS_UPDATE_THRESHOLD) {
				timestamp = event.timestamp;
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimuth = -1 * (float) Math.toDegrees(orientation[0]);

				Log.d("Navigate", "Sensor:" + event.sensor.getType() + " TS: "
						+ event.timestamp + " PREV: " + timestamp + " DIFF: "
						+ difference + " ACCY: " + event.accuracy);
				Log.d("Navigate", "Azimuth: " + azimuth);
				compassRose.animate().setDuration(ANIMATION_DURATION)
						.rotation(360 + azimuth);

				compassTarget.animate().setDuration(ANIMATION_DURATION)
						.rotation(360 + azimuth + targetBearing);

			}
		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		final float[] results = new float[3];
		Location.distanceBetween(location.getLatitude(),
				location.getLongitude(), currentTarget.latitude,
				currentTarget.longitude, results);
		final float bearing = results[1];
		String data = "Distance: " + results[0] + ", Bearing: " + bearing
				+ ", Final Bearing: " + results[2] + ", ACCY: "
				+ location.getAccuracy();
		Log.d("Navigate", data);
		debug.setText(data);
		targetBearing = results[2];
		// Toast.makeText(getBaseContext(),, Toast.LENGTH_SHORT).show();
		
		Log.d("navigating_to", currentTarget.nameEn);
		if(results[0] < CHECKIN_DISTANCE)
		{
			Log.d("Checkin", "You can check in!");
			if(checkInWindowOnScreen  != true)
			{
				checkInWindowOnScreen = true;
				CheckinDialogFragment c = new CheckinDialogFragment();
				c.show(getFragmentManager(), "checkin");
			}
		}
	}
	
	public void doNavigateToNextCheckin()
	{		
		int i = currentRoute.waypoints.indexOf(currentTarget) + 1;
		if(i<currentRoute.waypoints.size())
		{
			Waypoint nextTarget = currentRoute.waypoints.get(i+1);
			currentTarget = nextTarget;
		}
		else
		{
			//Route is finished
			finish();
			overridePendingTransition(R.anim.enter_from_left, R.anim.leave_to_right);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}
