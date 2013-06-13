package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationDetailActivity extends Activity {

	GameData gameData;

	Profile currentProfile;

	Route currentRoute;
	
	Waypoint currentWaypoint;
	
	ImageView locationImage;

	TextView locationTitle;

	TextView locationDescription;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setupDataReferences();
		setupActionBar();
		setupViewReferences();
		loadAndDisplayData();

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
			//TODO Handle the tap on the Map button (if implemented)
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("unused")
	private void goUp() {
		Intent intent = new Intent(this, RouteDetailActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(ApiConstants.GAME_DATA, gameData);
		intent.putExtra(ApiConstants.CURRENT_PROFILE, currentProfile);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_left, R.anim.leave_to_right);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.route_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	private void setupDataReferences() {
		gameData = getIntent().getParcelableExtra(ApiConstants.GAME_DATA);
		currentProfile = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_PROFILE);
		currentRoute = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_ROUTE);
		currentWaypoint = getIntent().getParcelableExtra(ApiConstants.CURRENT_WAYPOINT);
	}
	
	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(currentWaypoint.getLocalizedName());
	}
	
	private void setupViewReferences() {
		setContentView(R.layout.location_detail);
		locationImage = (ImageView) findViewById(R.id.location_detail_image);
		locationTitle = (TextView) findViewById(R.id.location_detail_title);
		locationDescription = (TextView) findViewById(R.id.location_detail_description);

	}
	
	private void loadAndDisplayData() {
		Bitmap photo = BitmapFactory.decodeFile(currentWaypoint.image.localPath);
		locationImage.setImageBitmap(photo);
		locationTitle.setText(currentWaypoint.getLocalizedName());
		locationDescription.setText(currentWaypoint.getLocalizedDescription());
		
	}

	
}
