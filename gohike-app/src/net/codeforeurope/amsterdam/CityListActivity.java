package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.adapter.CityListAdapter;
import net.codeforeurope.amsterdam.model.City;
import net.codeforeurope.amsterdam.model.LocateData;
import net.codeforeurope.amsterdam.service.LocateApiService;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.location.LocationListener;

public class CityListActivity extends AbstractGameActivity implements
		LocationListener, OnItemClickListener {

	LocateData locateData;

	BroadcastReceiver locateDataReceiver;

	CityListAdapter listAdapter;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_list);

		listView = (ListView) findViewById(R.id.city_list);
		listAdapter = new CityListAdapter(this);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
	}

	private void setupReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ActionConstants.LOCATE_COMPLETE);
		locateDataReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				locateData = intent
						.getParcelableExtra(ApiConstants.LOCATE_DATA);
				progressDialog.dismiss();
				listAdapter.setCities(locateData);

			}
		};
		registerReceiver(locateDataReceiver, filter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		setupReceiver();
		locationClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (locationClient.isConnected()) {
			locationClient.removeLocationUpdates(this);
		}
		locationClient.disconnect();
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(locateDataReceiver);
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location.hasAccuracy() && location.getAccuracy() < 15
				&& locateData == null) {
			Intent intent = new Intent(getApplicationContext(),
					LocateApiService.class);
			intent.putExtra(DataConstants.LOCATE_CURRENT_LOCATION, location);
			startService(intent);
			progressDialog
					.setMessage(getString(R.string.city_list_loading_cities));
			if (locationClient.isConnected()) {
				locationClient.removeLocationUpdates(this);
			}
		}

	}

	@Override
	public void onConnected(Bundle bundle) {
		if (locateData == null) {
			locationClient.requestLocationUpdates(locationRequest, this);
			progressDialog.setMessage(getString(R.string.city_list_locating));
			progressDialog.show();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (id > 0) {
			City city = (City) listAdapter.getItem(position);
			saveSelectedCity(city);
			gotoContentGrid();
			overridePendingTransition(R.anim.enter_from_right,
					R.anim.leave_to_left);
		}

	}

}
