package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.adapter.GridAdapter;
import net.codeforeurope.amsterdam.model.BaseModel;
import net.codeforeurope.amsterdam.service.CatalogApiService;
import net.codeforeurope.amsterdam.service.ImageDownloadService;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ContentGridActivity extends AbstractGameActivity implements
		OnItemClickListener {

	GridView gridView;
	GridAdapter adapter;

	BroadcastReceiver receiver = new Receiver();

	IntentFilter receiverFilter = new IntentFilter();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		gridView = (GridView) findViewById(R.id.grid);
		gridView.setOnItemClickListener(this);
		setupActionBar();
		setupReceivers();

	}

	private void setupReceivers() {
		receiverFilter.addAction(ActionConstants.CATALOG_DOWNLOAD_COMPLETE);
		receiverFilter.addAction(ActionConstants.IMAGE_DOWNLOAD_COMPLETE);
		receiverFilter.addAction(ActionConstants.IMAGE_DOWNLOAD_PROGRESS);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getCurrentCityName());
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GridAdapter adapter = (GridAdapter) parent.getAdapter();
		BaseModel profile = (BaseModel) adapter.getItem(position);
		// gameStateService.setCurrentProfile(profile);
		// gameStateService.setCurrentRoute(profile.routes.get(0));

		Intent intent = new Intent(getBaseContext(), RouteDetailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerReceiver(receiver, receiverFilter);
		progressDialog
				.setMessage(getString(R.string.content_grid_loading_catalog));
		progressDialog.show();
		Intent intent = new Intent(getApplicationContext(),
				CatalogApiService.class);
		intent.putExtra(DataConstants.CITY_ID, getApp().getSelectedCityId());
		startService(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_select, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			gotoCityList(true);
			overridePendingTransition(R.anim.enter_from_left,
					R.anim.leave_to_right);
			return true;
		case R.id.menu_view_help:
			// show help here

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ActionConstants.CATALOG_DOWNLOAD_COMPLETE.equals(action)) {
				progressDialog.dismiss();
				progressDialog.setIndeterminate(false);
				progressDialog.setMax(100);
				progressDialog
						.setMessage(getString(R.string.content_grid_downloading_images));
				progressDialog
						.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();
				Intent downloadIntent = new Intent(getApplicationContext(),
						ImageDownloadService.class);
				downloadIntent
						.setAction(ActionConstants.CATALOG_DOWNLOAD_COMPLETE);
				downloadIntent
						.putExtra(
								DataConstants.CATALOG_PROFILES,
								intent.getParcelableArrayListExtra(DataConstants.CATALOG_PROFILES));
				startService(downloadIntent);

			} else if (ActionConstants.IMAGE_DOWNLOAD_PROGRESS.equals(action)) {
				progressDialog.setProgress(intent.getIntExtra(
						DataConstants.IMAGE_DOWNLOAD_PROGRESS, 0));
				progressDialog.setMax(intent.getIntExtra(
						DataConstants.IMAGE_DOWNLOAD_TARGET, 0));

			} else if (ActionConstants.IMAGE_DOWNLOAD_COMPLETE.equals(action)) {
				progressDialog.setIndeterminate(true);
				progressDialog.dismiss();
			}

		}

	}

}
