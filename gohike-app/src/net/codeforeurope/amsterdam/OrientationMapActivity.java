package net.codeforeurope.amsterdam;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class OrientationMapActivity extends AbstractGameActivity implements
		OnMyLocationChangeListener {

	private GoogleMap map;

	LatLng myPosition;

	private LatLng targetPosition;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.map);

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.orientation_map)).getMap();
		map.setMyLocationEnabled(true);
		map.setOnMyLocationChangeListener(this);
		setupActionBar();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_left, R.anim.leave_to_right);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setTitle(gameStateService.getCurrentRoute()
		// .getLocalizedName());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			onBackPressed();
			return true;
		case R.id.menu_show_map:
			Intent i = new Intent(getBaseContext(),
					OrientationMapActivity.class);
			startActivity(i);

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// protected void onGameStateServiceConnected() {
	// Waypoint currentTarget = gameStateService.getCurrentTarget();
	// targetPosition = new LatLng(currentTarget.latitude,
	// currentTarget.longitude);
	//
	// map.addMarker(new MarkerOptions()
	// .position(targetPosition)
	// .draggable(false)
	// .title(currentTarget.getLocalizedName())
	// .icon(BitmapDescriptorFactory
	// .fromResource(R.drawable.blue_map_marker)));
	//
	// // zoomIn();
	// }

	private void zoomIn() {

		if (targetPosition != null || myPosition != null) {
			// Calculate the markers to get their position
			LatLngBounds.Builder b = new LatLngBounds.Builder();
			if (targetPosition != null) {
				b.include(targetPosition);
			}
			if (myPosition != null) {
				b.include(myPosition);
			}

			LatLngBounds bounds = b.build();
			// Change the padding as per needed
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100);
			map.animateCamera(cu);
		}
	}

	@Override
	protected void onGameDataUpdated(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMyLocationChange(Location myLocation) {
		myPosition = new LatLng(myLocation.getLatitude(),
				myLocation.getLongitude());
		zoomIn();
	}

}
