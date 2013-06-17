package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.service.CheckinService;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RouteDetailActivity extends AbstractGameActivity implements
		OnClickListener {

	ImageView routeImage;

	TextView routeTitle;

	TextView routeDescription;

	LinearLayout waypointList;

	private LayoutInflater inflater;

	Button fakeIt;

	BroadcastReceiver receiver;

	private Route currentRoute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setupViewReferences();
		setupBroadcastReceivers();

	}

	private void setupBroadcastReceivers() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ApiConstants.ACTION_CHECKINS_LOADED);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				updateWaypointDisplay();
			}
		};
		registerReceiver(receiver, filter);
	}

	@Override
	public void onActionModeFinished(ActionMode mode) {
		// This should solve the exception of BroadcastReceiver leaking that
		// shows when debugging
		unregisterReceiver(receiver);
		super.onActionModeFinished(mode);
	}

	private void updateWaypointDisplay() {
		Route currentRoute = gameStateService.getCurrentRoute();
		int length = currentRoute.waypoints.size();
		for (int i = 0; i < length; i++) {
			Waypoint waypoint = currentRoute.waypoints.get(i);
			if (gameStateService.isWaypointCheckedIn(waypoint)) {
				RelativeLayout waypointItem = (RelativeLayout) waypointList
						.getChildAt(i);
				ImageView leftIcon = (ImageView) waypointItem
						.findViewById(R.id.waypoint_item_icon_left);

				ImageView rightIcon = (ImageView) waypointItem
						.findViewById(R.id.waypoint_item_icon_right);
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

			}
		}
	}

	private void openLocationDetail(Waypoint wp) {
		Intent intent = new Intent(this, LocationDetailActivity.class);
		// intent.putExtra(ApiConstants.GAME_DATA, gameData);
		// intent.putExtra(ApiConstants.CURRENT_PROFILE, currentProfile);
		// intent.putExtra(ApiConstants.CURRENT_ROUTE, currentRoute);
		// intent.putExtra(ApiConstants.CURRENT_WAYPOINT, wp);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
	}

	private void loadAndDisplayData() {
		Bitmap photo = BitmapFactory.decodeFile(currentRoute.image.localPath);
		routeImage.setImageBitmap(photo);
		routeTitle.setText(currentRoute.getLocalizedName());
		routeDescription.setText(currentRoute.getLocalizedDescription());
		int length = currentRoute.waypoints.size();
		for (int i = 0; i < length; i++) {
			Waypoint waypoint = currentRoute.waypoints.get(i);
			RelativeLayout waypointItem = (RelativeLayout) inflater.inflate(
					R.layout.waypoint_item, null);
			TextView itemTitle = (TextView) waypointItem
					.findViewById(R.id.waypoint_item_title);
			itemTitle.setText(waypoint.getLocalizedName());
			waypointItem.setTag(waypoint);
			if (i == 0) {
				waypointItem
						.setBackgroundResource(R.drawable.waypoint_item_first);
			}
			if (i == length - 1) {
				waypointItem
						.setBackgroundResource(R.drawable.waypoint_item_last);
			}

			waypointList.addView(waypointItem);

		}
	}

	private void setupViewReferences() {
		setContentView(R.layout.route_detail);
		routeImage = (ImageView) findViewById(R.id.route_detail_image);
		routeTitle = (TextView) findViewById(R.id.route_detail_title);
		routeDescription = (TextView) findViewById(R.id.route_detail_description);
		waypointList = (LinearLayout) findViewById(R.id.route_detail_waypoints);
		inflater = (LayoutInflater) getBaseContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		fakeIt = (Button) findViewById(R.id.route_detail_fake_checkin);
		fakeIt.setOnClickListener(this);
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
			Intent intent = new Intent(this, NavigateRouteActivity.class);
			// intent.putExtra(ApiConstants.GAME_DATA, gameData);
			// intent.putExtra(ApiConstants.CURRENT_PROFILE, currentProfile);
			// intent.putExtra(ApiConstants.CURRENT_ROUTE, currentRoute);
			// intent.putExtra(ApiConstants.CURRENT_TARGET, getNextTarget());
			startActivity(intent);
			overridePendingTransition(R.anim.enter_from_right,
					R.anim.leave_to_left);
			return true;
		case R.id.menu_view_reward:
			Intent intent1 = new Intent(this, RewardActivity.class);
			// intent1.putExtra(ApiConstants.GAME_DATA, gameData);
			// intent1.putExtra(ApiConstants.CURRENT_PROFILE, currentProfile);
			// intent1.putExtra(ApiConstants.CURRENT_ROUTE, currentRoute);
			startActivity(intent1);
			overridePendingTransition(R.anim.enter_from_right,
					R.anim.leave_to_left);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @SuppressWarnings("unused")
	// private void goUp() {
	// Intent intent = new Intent(this, RouteGridActivity.class);
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	// intent.putExtra(ApiConstants.GAME_DATA, gameData);
	// intent.putExtra(ApiConstants.CURRENT_PROFILE, currentProfile);
	// startActivity(intent);
	// overridePendingTransition(R.anim.enter_from_left, R.anim.leave_to_right);
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (isRouteFinished()) {
			inflater.inflate(R.menu.route_detail_finished, menu);
		} else {
			inflater.inflate(R.menu.route_detail, menu);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentRoute.getLocalizedName());
	}

	@Override
	public void onClick(View v) {
		Waypoint waypoint = gameStateService.getNextTarget();
		Intent checkinIntent = new Intent(getBaseContext(),
				CheckinService.class);
		checkinIntent.putExtra(ApiConstants.CURRENT_TARGET, waypoint);
		startService(checkinIntent);

	}

	private boolean isRouteFinished() {
		if (gameStateService.getNextTarget() == null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onGameStateServiceConnected() {
		// TODO Auto-generated method stub
		currentRoute = gameStateService.getCurrentRoute();

		loadAndDisplayData();
		setupActionBar();
	}

	@Override
	protected void onGameDataUpdated(Intent intent) {
		// TODO Auto-generated method stub
		onGameStateServiceConnected();
	}
}
