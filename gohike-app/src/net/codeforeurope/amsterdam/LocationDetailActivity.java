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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationDetailActivity extends AbstractGameActivity implements
		OnClickListener {

	Waypoint currentWaypoint;

	ImageView locationImage;

	TextView locationTitle;

	TextView locationDescription;

	Button goHikeButton;

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

		case R.id.menu_continue_hike:
			continueHike();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void continueHike() {
		super.onBackPressed();
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (getIntent().getBooleanExtra(ApiConstants.JUST_FOUND, false)) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.location_found, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		// actionBar.setTitle(currentWaypoint.getLocalizedName());
	}

	private void setupViewReferences() {
		setContentView(R.layout.location_detail);
		locationImage = (ImageView) findViewById(R.id.location_detail_image);
		locationTitle = (TextView) findViewById(R.id.location_detail_title);
		locationDescription = (TextView) findViewById(R.id.location_detail_description);
		goHikeButton = (Button) findViewById(R.id.route_gohike_button);

	}

	private void loadAndDisplayData() {
		Bitmap photo = BitmapFactory
				.decodeFile(currentWaypoint.image.localPath);
		locationImage.setImageBitmap(photo);
		// locationTitle.setText(currentWaypoint.getLocalizedName());
		// locationDescription.setText(currentWaypoint.getLocalizedDescription());
		if (getIntent().getBooleanExtra(ApiConstants.JUST_FOUND, false)) {
			goHikeButton.setVisibility(android.view.View.VISIBLE);
			goHikeButton.setOnClickListener(this);
			Toast.makeText(getBaseContext(), R.string.found_it,
					Toast.LENGTH_SHORT).show();
		}

	}

	// @Override
	// protected void onGameStateServiceConnected() {
	// currentWaypoint = getIntent().getParcelableExtra(
	// ApiConstants.CURRENT_WAYPOINT);
	//
	// setupActionBar();
	// loadAndDisplayData();
	// }

	@Override
	protected void onGameDataUpdated(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		continueHike();
	}

}
