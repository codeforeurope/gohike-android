package net.codeforeurope.amsterdam;

import java.util.List;

import net.codeforeurope.amsterdam.model.BaseModel;
import net.codeforeurope.amsterdam.model.Reward;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.AlertDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;

public class RewardActivity extends AbstractGameActivity {

	private static final String PUBLISH_ACTIONS = "publish_actions";

	BaseModel currentProfile;

	Route currentRoute;

	Reward reward;

	TextView rewardDescription;

	ImageView rewardImage;

	TextView rewardTitle;

	boolean shouldShareBadge = false;

	private void doShareReward() {
		// Bitmap image = BitmapFactory.decodeFile(reward.image.localPath);

		Bundle postParams = new Bundle();
		postParams.putString("name", reward.name.getLocalizedValue());
		postParams.putString("description", reward.description.getLocalizedValue());
		postParams.putString("link", String.format(ApiConstants.WEB_BASE_URL, "rewards/" + reward.getId()));
		postParams.putString("message", getString(R.string.earned_reward, reward.name.getLocalizedValue()));

		Request request = new Request(Session.getActiveSession(), "me/feed", postParams, HttpMethod.POST,
				new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						// TODO Auto-generated method stub
						// showPublishResult(reward.getLocalizedName(),
						// response);
					}
				});

		RequestAsyncTask task = new RequestAsyncTask(request);
		task.execute();
		shouldShareBadge = false;
	}

	protected boolean facebookHasPublishPermissions(Session session) {
		List<String> permissions = session.getPermissions();
		if (permissions.contains(PUBLISH_ACTIONS)) {
			return true;
		}
		return false;
	}

	private void loadAndDisplayData() {
		Bitmap photo = BitmapFactory.decodeFile(reward.image.localPath);
		rewardImage.setImageBitmap(photo);
		// rewardTitle.setText(reward.getLocalizedName());
		// rewardDescription.setText(reward.getLocalizedDescription());

	}

	private void onClickPostPhoto() {
		Session facebookSession = Session.getActiveSession();
		if (facebookSession.isOpened() && facebookHasPublishPermissions(facebookSession)) {
			doShareReward();
		} else {
			Session.openActiveSession(this, true, facebookStatusCallback);
			shouldShareBadge = true;

		}
	}

	protected void setupActionBar() {
		super.setupActionBar();
		actionBar.setTitle(getCurrentRoute().name.getLocalizedValue());
	}

	private void setupViewReferences() {
		setContentView(R.layout.reward);
		rewardImage = (ImageView) findViewById(R.id.reward_image);
		rewardTitle = (TextView) findViewById(R.id.reward_title);
		rewardDescription = (TextView) findViewById(R.id.route_detail_description);

	}

	private void showPublishResult(String message, Response response) {
		String title = null;
		String alertMessage = null;
		if (response.getError() == null) {
			title = getString(R.string.share_reward_success);
			// String id =
			// response.getGraphObject().cast(GraphObjectWithId.class)
			// .getId();
			alertMessage = getString(R.string.share_reward_success_message);
		} else {
			title = getString(R.string.share_reward_error);
			alertMessage = response.getError().getErrorMessage();
		}

		new AlertDialog.Builder(this).setTitle(title).setMessage(alertMessage).setPositiveButton("OK", null).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setupViewReferences();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_share, menu);
		return super.onCreateOptionsMenu(menu);
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

			onClickPostPhoto();

			// Following code is to share using ACTION_SEND
			// Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			// Uri screenshotUri = Uri.parse(reward.image.localPath);
			// // Bitmap photo =
			// BitmapFactory.decodeFile(reward.image.localPath);
			// sharingIntent.setType("image/png");
			// sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
			// startActivity(Intent.createChooser(sharingIntent,
			// getString(R.string.share_reward_using)));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		facebookUiHelper.onSaveInstanceState(outState);
	}

}
