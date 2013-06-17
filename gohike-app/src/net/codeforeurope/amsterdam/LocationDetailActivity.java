package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationDetailActivity extends AbstractGameActivity {

	Waypoint currentWaypoint;

	ImageView locationImage;

	TextView locationTitle;

	TextView locationDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setupViewReferences();

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
			// TODO Handle the tap on the Map button (if implemented)
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.route_detail, menu);
		return super.onCreateOptionsMenu(menu);
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
		Bitmap photo = BitmapFactory
				.decodeFile(currentWaypoint.image.localPath);
		locationImage.setImageBitmap(photo);
		locationTitle.setText(currentWaypoint.getLocalizedName());
		locationDescription.setText(currentWaypoint.getLocalizedDescription());

	}

	@Override
	protected void onGameStateServiceConnected() {
		currentWaypoint = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_WAYPOINT);

		setupActionBar();
		loadAndDisplayData();
	}

	@Override
	protected void onGameDataUpdated(Intent intent) {
		// TODO Auto-generated method stub

	}

}
