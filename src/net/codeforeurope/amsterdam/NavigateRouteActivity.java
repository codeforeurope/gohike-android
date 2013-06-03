package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NavigateRouteActivity extends Activity implements
		SensorEventListener {
	GameData gameData;

	Profile currentProfile;

	Route currentRoute;

	SensorManager sensorManager;

	Sensor orientation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		orientation = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		// orientation = sensorManager.getOrientation(R, values);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.grid);

		gameData = getIntent().getParcelableExtra(ApiConstants.GAME_DATA);
		currentProfile = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_PROFILE);
		currentRoute = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_ROUTE);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentRoute.nameEn);

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
		inflater.inflate(R.menu.route_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(this, orientation,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		float azimuth_angle = event.values[0];
		float pitch_angle = event.values[1];
		float roll_angle = event.values[2];
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
}
