package net.codeforeurope.amsterdam;

import java.util.Arrays;
import java.util.List;

import net.codeforeurope.amsterdam.model.Reward;
import net.codeforeurope.amsterdam.service.ConnectApiService;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

public class RewardActivity extends AbstractGameActivity {

	private static final String PUBLISH_ACTIONS = "publish_actions";

	TextView rewardDescription;

	ImageView rewardImage;

	TextView rewardTitle;

	boolean shouldShareBadge = false;

	boolean permissionsRequested = false;

	BroadcastReceiver receiver = new Receiver();

	IntentFilter receiverFilter = new IntentFilter();

	@Override
	protected void onFacebookStatusChange(Session session, SessionState state, Exception exception) {
		super.onFacebookStatusChange(session, state, exception);
		if (shouldShareBadge && session.isOpened()) {
			if (facebookHasPublishPermissions(session)) {
				ensureUserAccountExistsAndShareReward();
			} else {
				if (!permissionsRequested) {
					Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
							RewardActivity.this, Arrays.asList(PUBLISH_ACTIONS));
					session.requestNewPublishPermissions(newPermissionsRequest);
					permissionsRequested = true;
				}
			}

		}
	}

	private void ensureUserAccountExistsAndShareReward() {
		if (getApp().getPlayerId() > 0) {
			performShareReward();
		} else {
			Request request = Request.newMeRequest(Session.getActiveSession(), new Request.GraphUserCallback() {

				@Override
				public void onCompleted(GraphUser user, Response response) {
					Bundle connectParams = new Bundle();
					connectParams.putString(DataConstants.FACEBOOK_USER_NAME, user.getFirstName());
					connectParams.putString(DataConstants.FACEBOOK_USER_ID, user.getId());
					connectParams.putString(DataConstants.FACEBOOK_USER_EMAIL, (String) user.getProperty("email"));
					connectParams.putString(DataConstants.FACEBOOK_ACCESS_TOKEN, Session.getActiveSession()
							.getAccessToken());
					connectParams.putLong(DataConstants.FACEBOOK_ACCESS_TOKEN_EXPIRATION, Session.getActiveSession()
							.getExpirationDate().getTime() / 1000);

					Intent intent = new Intent(getBaseContext(), ConnectApiService.class);
					intent.putExtra(DataConstants.CONNECT_PARAMS, connectParams);
					startService(intent);

				}
			});
			request.executeAsync();
		}

	}

	private void performShareReward() {

		Bundle postParams = new Bundle();
		final Reward reward = getCurrentRoute().reward;

		postParams.putString("name", reward.name.getLocalizedValue());
		postParams.putString("description", reward.description.getLocalizedValue());
		postParams.putString("link", String.format(ApiConstants.WEB_BASE_URL, "rewards/" + reward.getId()));
		postParams.putString("message", getString(R.string.earned_reward, reward.name.getLocalizedValue()));
		postParams.putString("picture", reward.image.url);

		Request request = new Request(Session.getActiveSession(), "me/feed", postParams, HttpMethod.POST,
				new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						showPublishResult(reward.name.getLocalizedValue(), response);
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
		Bitmap photo = BitmapFactory.decodeFile(getCurrentRoute().reward.image.localPath);
		rewardImage.setImageBitmap(photo);
		rewardTitle.setText(getCurrentRoute().reward.name.getLocalizedValue());
		rewardDescription.setText(getCurrentRoute().reward.description.getLocalizedValue());

	}

	private void onClickPostPhoto() {
		Session facebookSession = Session.getActiveSession();
		if (facebookSession.isOpened()) {
			if (facebookHasPublishPermissions(facebookSession)) {
				ensureUserAccountExistsAndShareReward();
			}
		} else {
			OpenRequest openRequest = new OpenRequest(this).setCallback(facebookStatusCallback).setPermissions(
					ApiConstants.FACEBOOK_READ_PERMISSIONS);
			Session.setActiveSession(facebookSession);
			facebookSession.openForRead(openRequest);
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
		loadAndDisplayData();
		receiverFilter.addAction(ActionConstants.CONNECT_COMPLETE);
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

	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(receiver, receiverFilter);

	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ActionConstants.CONNECT_COMPLETE.equals(action)) {
				getApp().setPlayerId(intent.getLongExtra(DataConstants.PLAYER_ID, 0));
				performShareReward();
			}

		}
	}
}
