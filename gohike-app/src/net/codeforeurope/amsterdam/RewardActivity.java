package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Reward;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class RewardActivity extends AbstractGameActivity {

	GameData gameData;

	Profile currentProfile;

	Route currentRoute;

	Reward reward;

	ImageView rewardImage;

	TextView rewardTitle;

	TextView rewardDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setupViewReferences();
	}

	private void loadAndDisplayData() {
		Bitmap photo = BitmapFactory.decodeFile(reward.image.localPath);
		rewardImage.setImageBitmap(photo);
		rewardTitle.setText(reward.getLocalizedName());
		rewardDescription.setText(reward.getLocalizedDescription());

	}

	private void setupViewReferences() {
		setContentView(R.layout.reward);
		rewardImage = (ImageView) findViewById(R.id.reward_image);
		rewardTitle = (TextView) findViewById(R.id.reward_title);
		rewardDescription = (TextView) findViewById(R.id.route_detail_description);

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
		case R.id.menu_share_badge:
			// share on Facebook

			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			Uri screenshotUri = Uri.parse(reward.image.localPath);
			// Bitmap photo = BitmapFactory.decodeFile(reward.image.localPath);
			sharingIntent.setType("image/png");
			sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
			startActivity(Intent.createChooser(sharingIntent,
					getString(R.string.share_reward_using)));
			return true;
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
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_share, menu);
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
		reward = currentRoute.reward;
		setupActionBar();

		loadAndDisplayData();

	}

	@Override
	protected void onGameDataUpdated(Intent intent) {
		// TODO Auto-generated method stub

	}
}
