package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.view.NotVisitedDialogFragment;
import android.app.ActionBar;
import android.app.DialogFragment;
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

	Button goHikeButton;

	LinearLayout waypointList;

	private LayoutInflater inflater;

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
		filter.addAction(ApiConstants.ACTION_CHECKIN_SAVED);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {

				updateWaypointDisplay();
			}
		};
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		setupBroadcastReceivers();
		updateWaypointDisplay();
		updateButtonVisibility();
		invalidateOptionsMenu();
	}

	private void updateButtonVisibility() {
		if (gameStateService.isRouteFinished()) {
			goHikeButton.setVisibility(android.view.View.GONE);
		} else {
			goHikeButton.setVisibility(android.view.View.VISIBLE);
			goHikeButton.setOnClickListener(this);
		}
	}

	private void updateWaypointDisplay() {
		int length = currentRoute.waypoints.size();
		for (int i = 0; i < length; i++) {
			Waypoint waypoint = currentRoute.waypoints.get(i);
			RelativeLayout waypointItem = (RelativeLayout) waypointList
					.getChildAt(i);
			if (gameStateService.isWaypointCheckedIn(waypoint)) {

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
			} else {
				waypointItem.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// here we show the Dialog "Go Hike Now?"
						showNotFoundYetDialog();
					}
				});
			}
		}
	}

	private void showNotFoundYetDialog() {
		DialogFragment newFragment = new NotVisitedDialogFragment();
		newFragment.show(getFragmentManager(), "notvisited");
	}

	private void openLocationDetail(Waypoint wp) {
		Intent intent = new Intent(this, LocationDetailActivity.class);
		intent.putExtra(ApiConstants.CURRENT_WAYPOINT, wp);
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
		goHikeButton = (Button) findViewById(R.id.route_gohike_button);
		waypointList = (LinearLayout) findViewById(R.id.route_detail_waypoints);
		inflater = (LayoutInflater) getBaseContext().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
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
			startHike();
			return true;
		case R.id.menu_view_reward:
			Intent intent1 = new Intent(this, RewardActivity.class);
			startActivity(intent1);
			overridePendingTransition(R.anim.enter_from_right,
					R.anim.leave_to_left);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void startHike() {
		Intent intent = new Intent(this, NavigateRouteActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if (gameStateService != null && gameStateService.isRouteFinished()) {
			inflater.inflate(R.menu.route_detail_finished, menu);
		} else {
			inflater.inflate(R.menu.route_detail, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentRoute.getLocalizedName());
	}

	@Override
	protected void onGameStateServiceConnected() {
		currentRoute = gameStateService.getCurrentRoute();
		// gameStateService.
		loadAndDisplayData();
		setupActionBar();
		updateWaypointDisplay();
		updateButtonVisibility();
	}

	@Override
	protected void onGameDataUpdated(Intent intent) {
		// TODO Auto-generated method stub
		onGameStateServiceConnected();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		startHike();
	}
}
