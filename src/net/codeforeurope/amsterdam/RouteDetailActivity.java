package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RouteDetailActivity extends Activity {
	GameData gameData;

	Profile currentProfile;

	Route currentRoute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
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
		case R.id.menu_start_hike:
			Intent intent = new Intent(this, NavigateRouteActivity.class);
			intent.putExtra(ApiConstants.GAME_DATA, gameData);
			intent.putExtra(ApiConstants.CURRENT_PROFILE, currentProfile);
			intent.putExtra(ApiConstants.CURRENT_ROUTE, currentRoute);
			startActivity(intent);
			overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void goUp() {
		Intent intent = new Intent(this, RouteGridActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ApiConstants.GAME_DATA, gameData);
		intent.putExtra(ApiConstants.CURRENT_PROFILE, currentProfile);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_left, R.anim.leave_to_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.route_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

}
