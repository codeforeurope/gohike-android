package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.PingResult;
import net.codeforeurope.amsterdam.service.ContentApiService;
import net.codeforeurope.amsterdam.service.ContentService;
import net.codeforeurope.amsterdam.service.PingApiService;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class NewSplashActivity extends Activity {

	private GameData gameData;

	private BroadcastReceiver receiver;

	private UpdateContentDialogFragment updateDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		updateDialog = new UpdateContentDialogFragment();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setupReceiver();

	}

	private void setupReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ApiConstants.ACTION_CONTENT_LOADED);
		filter.addAction(ApiConstants.ACTION_REMOTE_CONTENT_LOADED);
		filter.addAction(ApiConstants.ACTION_PING_COMPLETE);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (ApiConstants.ACTION_PING_COMPLETE.equals(action)) {
					if (intent.getBooleanExtra(ApiConstants.SUCCESS, false)) {
						PingResult result = intent
								.getParcelableExtra(ApiConstants.PING_RESULT);
						if (!"ok".equalsIgnoreCase(result.status)) {
							updateDialog.setContentSize(result.size);
							updateDialog.show(getFragmentManager(), "dialog");
						} else {
							gotoMainScreen();
						}
					} else {
						gotoMainScreen();
					}
				} else {
					gameData = intent
							.getParcelableExtra(ApiConstants.GAME_DATA);

					if (ApiConstants.ACTION_REMOTE_CONTENT_LOADED
							.equals(action)) {
						gotoMainScreen();
					} else {
						pingforChanges();
					}
				}

			}
		};
		registerReceiver(receiver, filter);
	}

	protected void gotoMainScreen() {
		Intent intent = new Intent(getBaseContext(), ProfileGridActivity.class);
		intent.putExtra(ApiConstants.GAME_DATA, gameData);
		startActivity(intent);
		finish();
	}

	protected void pingforChanges() {
		Intent intent = new Intent(getBaseContext(), PingApiService.class);
		intent.putExtra(ApiConstants.CONTENT_VERSION, gameData.version);
		startService(intent);
	}

	public void doUpdateContent() {
		Intent intent = new Intent(getBaseContext(), ContentApiService.class);
		startService(intent);
	}

	public void skipUpdate() {
		gotoMainScreen();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onResume() {
		super.onResume();
		startService(new Intent(getBaseContext(), ContentService.class));
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
