package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.adapter.GridAdapter;
import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class RouteGridActivity extends Activity implements OnItemClickListener {

	GameData gameData;

	GridView gridView;
	GridAdapter adapter;

	Profile currentProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.grid);

		setupDataReferences();
		setupActionBar();
		setupViewReferences();

	}

	private void setupViewReferences() {
		// adapter = new GridAdapter(this, currentProfile.routes);
		// gridView = (GridView) findViewById(R.id.grid);
		// gridView.setAdapter(adapter);
		// gridView.setOnItemClickListener(this);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentProfile.getLocalizedName());
	}

	private void setupDataReferences() {
		gameData = getIntent().getParcelableExtra(ApiConstants.GAME_DATA);
		currentProfile = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_PROFILE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		GridAdapter adapter = (GridAdapter) parent.getAdapter();
		Route route = (Route) adapter.getItem(position);

		Intent intent = new Intent(getBaseContext(), RouteDetailActivity.class);
		intent.putExtra(ApiConstants.GAME_DATA, gameData);
		intent.putExtra(ApiConstants.CURRENT_PROFILE, currentProfile);
		intent.putExtra(ApiConstants.CURRENT_ROUTE, route);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			// Intent intent = new Intent(this, ProfileGridActivity.class);
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// intent.putExtra(ApiConstants.GAME_DATA, gameData);
			//
			// startActivity(intent);
			// overridePendingTransition(R.anim.enter_from_left,
			// R.anim.leave_to_right);
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_left, R.anim.leave_to_right);
	}
}
