package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.service.ImageDownloadService;
import net.codeforeurope.amsterdam.service.RouteApiService;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.DataConstants;
import net.codeforeurope.amsterdam.view.NotVisitedDialogFragment;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;

public class RouteDetailActivity extends AbstractGameActivity implements OnClickListener {

	ImageView routeImage;

	TextView routeTitle;

	TextView routeDescription;

	Button goHikeButton;

	LinearLayout waypointList;

	private LayoutInflater inflater;

	BroadcastReceiver receiver = new Receiver();

	IntentFilter receiverFilter = new IntentFilter();

	private Button downloadButton;

	private Button facebookButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setupViewReferences();
		setupBroadcastReceivers();

	}

	private void setupBroadcastReceivers() {
		receiverFilter.addAction(ApiConstants.ACTION_CHECKINS_LOADED);
		receiverFilter.addAction(ApiConstants.ACTION_CHECKIN_SAVED);
		receiverFilter.addAction(ActionConstants.ROUTE_DOWNLOAD_COMPLETE);
		receiverFilter.addAction(ActionConstants.IMAGE_DOWNLOAD_COMPLETE);
		receiverFilter.addAction(ActionConstants.IMAGE_DOWNLOAD_PROGRESS);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, receiverFilter);
		onRouteDownloadComplete();
	}

	private void onRouteDownloadComplete() {
		updateWaypointDisplay();
		updateButtonVisibility();
	}

	@Override
	protected void onRestart() {
		super.onRestart();

		invalidateOptionsMenu();
	}

	private void updateButtonVisibility() {

		if (getCurrentRoute().isDownloaded()) {
			// We assume that if a route was downloaded that at some point
			// Facebook must have been connected. We will not prevent someone
			// from enjoying a hike if the session is not valid
			downloadButton.setVisibility(android.view.View.GONE);
			facebookButton.setVisibility(android.view.View.GONE);
			if (isRouteFinished()) {
				goHikeButton.setVisibility(android.view.View.GONE);
			} else {
				goHikeButton.setVisibility(android.view.View.VISIBLE);
				goHikeButton.setOnClickListener(this);
			}
		} else {
			Session session = Session.getActiveSession();
			if (session != null && (session.isOpened() || session.isClosed())) {
				downloadButton.setVisibility(android.view.View.GONE);
				goHikeButton.setVisibility(android.view.View.GONE);
				facebookButton.setVisibility(android.view.View.VISIBLE);
				facebookButton.setOnClickListener(this);
			} else {
				goHikeButton.setVisibility(android.view.View.GONE);
				facebookButton.setVisibility(android.view.View.GONE);
				downloadButton.setVisibility(android.view.View.VISIBLE);
				downloadButton.setOnClickListener(this);
			}
		}
	}

	private void updateWaypointDisplay() {
		Route currentRoute = getCurrentRoute();
		int length = currentRoute.waypoints.size();
		for (int i = 0; i < length; i++) {
			Waypoint waypoint = currentRoute.waypoints.get(i);
			RelativeLayout waypointItem = (RelativeLayout) waypointList.getChildAt(i);
			if (isWaypointCheckedIn(waypoint)) {

				ImageView leftIcon = (ImageView) waypointItem.findViewById(R.id.waypoint_item_icon_left);

				ImageView rightIcon = (ImageView) waypointItem.findViewById(R.id.waypoint_item_icon_right);
				rightIcon.setVisibility(View.VISIBLE);
				leftIcon.getDrawable().setLevel(1);
				waypointItem.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Waypoint wp = (Waypoint) v.getTag();
						// here we show the Location Detail
						openLocationDetail(wp);
					}
				});
			} else {
				waypointItem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// here we show the Dialog "Go Hike Now?"
						showNotFoundYetDialog((Waypoint) v.getTag());
					}
				});
			}
		}
	}

	private void showNotFoundYetDialog(Waypoint waypoint) {
		Bundle arguments = new Bundle();
		arguments.putParcelable(DataConstants.WAYPOINT, waypoint);
		DialogFragment newFragment = new NotVisitedDialogFragment();
		newFragment.setArguments(arguments);
		newFragment.show(getFragmentManager(), "notvisited");
	}

	private void openLocationDetail(Waypoint wp) {
		Intent intent = new Intent(this, LocationDetailActivity.class);
		intent.putExtra(ApiConstants.CURRENT_WAYPOINT, wp);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
	}

	private void loadAndDisplayData() {
		Route currentRoute = getCurrentRoute();
		Bitmap photo = BitmapFactory.decodeFile(currentRoute.image.localPath);
		routeImage.setImageBitmap(photo);
		routeTitle.setText(currentRoute.name.getLocalizedValue());
		routeDescription.setText(currentRoute.description.getLocalizedValue());

		int length = currentRoute.waypoints.size();
		for (int i = 0; i < length; i++) {
			Waypoint waypoint = currentRoute.waypoints.get(i);
			RelativeLayout waypointItem = (RelativeLayout) inflater.inflate(R.layout.waypoint_item, null);
			TextView itemTitle = (TextView) waypointItem.findViewById(R.id.waypoint_item_title);
			itemTitle.setText(waypoint.name.getLocalizedValue());
			waypointItem.setTag(waypoint);
			if (i == 0) {
				waypointItem.setBackgroundResource(R.drawable.waypoint_item_first);
			}
			if (i == length - 1) {
				waypointItem.setBackgroundResource(R.drawable.waypoint_item_last);
			}
			waypointList.addView(waypointItem);
		}
	}

	private void setupViewReferences() {
		setContentView(R.layout.route_detail);
		routeImage = (ImageView) findViewById(R.id.route_detail_image);
		routeTitle = (TextView) findViewById(R.id.route_detail_title);
		routeDescription = (TextView) findViewById(R.id.route_detail_description);
		goHikeButton = (Button) findViewById(R.id.route_gohike_button);
		downloadButton = (Button) findViewById(R.id.route_download_button);
		facebookButton = (Button) findViewById(R.id.route_facebook_button);
		waypointList = (LinearLayout) findViewById(R.id.route_detail_waypoints);
		inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		case R.id.menu_start_hike:
			startHike(null);
			return true;
		case R.id.menu_view_reward:
			Intent intent1 = new Intent(this, RewardActivity.class);
			startActivity(intent1);
			overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void startHike(Waypoint waypoint) {
		Intent intent = new Intent(this, NavigateRouteActivity.class);
		if (waypoint != null) {
			intent.putExtra(DataConstants.WAYPOINT, waypoint);
		}
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// if (gameStateService != null && gameStateService.isRouteFinished()) {
		// inflater.inflate(R.menu.route_detail_finished, menu);
		// } else {
		// inflater.inflate(R.menu.route_detail, menu);
		// }
		return super.onCreateOptionsMenu(menu);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getCurrentRoute().name.getLocalizedValue());
	}

	@Override
	protected void onStart() {
		super.onStart();
		loadAndDisplayData();
		setupActionBar();

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.route_gohike_button:
			startHike(null);
			break;
		case R.id.route_download_button:
			Intent intent = new Intent(getBaseContext(), RouteApiService.class);
			intent.putExtra(DataConstants.ROUTE, getCurrentRoute());
			startService(intent);
			progressDialog.setCancelable(false);
			progressDialog.setMessage(getString(R.string.route_detail_downloading_route));
			progressDialog.show();
			break;
		case R.id.route_facebook_button:
			break;

		}

	}

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ActionConstants.ROUTE_DOWNLOAD_COMPLETE.equals(action)) {
				progressDialog.dismiss();
				progressDialog.setIndeterminate(false);
				progressDialog.setMax(100);
				progressDialog.setMessage(getString(R.string.content_grid_downloading_images));
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progressDialog.show();
				Intent downloadIntent = new Intent(getApplicationContext(), ImageDownloadService.class);
				downloadIntent.setAction(ActionConstants.ROUTE_DOWNLOAD_COMPLETE);
				downloadIntent.putExtra(DataConstants.DOWNLOADED_ROUTE,
						intent.getParcelableExtra(DataConstants.DOWNLOADED_ROUTE));
				startService(downloadIntent);

			} else if (ActionConstants.IMAGE_DOWNLOAD_PROGRESS.equals(action)) {
				progressDialog.setProgress(intent.getIntExtra(DataConstants.IMAGE_DOWNLOAD_PROGRESS, 0));
				progressDialog.setMax(intent.getIntExtra(DataConstants.IMAGE_DOWNLOAD_TARGET, 0));

			} else if (ActionConstants.IMAGE_DOWNLOAD_COMPLETE.equals(action)) {
				Route route = intent.getParcelableExtra(DataConstants.DOWNLOADED_ROUTE);
				progressDialog.setIndeterminate(true);

				progressDialog.dismiss();
				getApp().storeDownloadedRoute(route);
				loadAndDisplayData();
				onRouteDownloadComplete();
			}
		}
	}
}
