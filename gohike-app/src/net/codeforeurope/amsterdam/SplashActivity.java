package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

public class SplashActivity extends AbstractGameActivity {

	private GameData gameData;

	private BroadcastReceiver receiver;

	private UpdateContentDialogFragment updateDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		updateDialog = new UpdateContentDialogFragment();

	}

	private void setupReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(ApiConstants.ACTION_PROMPT_FOR_UPDATE);
		filter.addAction(ApiConstants.ACTION_GO_TO_PROFILES);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				if (ApiConstants.ACTION_PROMPT_FOR_UPDATE.equals(action)) {
					updateDialog.setContentSize(intent.getIntExtra(
							ApiConstants.UPDATE_SIZE, 0));
					updateDialog.show(getFragmentManager(), "dialog");
				}
			}
		};
		registerReceiver(receiver, filter);
	}

	protected void gotoMainScreen() {
		Intent intent = new Intent(getBaseContext(), ProfileGridActivity.class);
		startActivity(intent);
		finish();
	}

	// protected void pingforChanges() {
	// Intent intent = new Intent(getBaseContext(), PingApiService.class);
	// intent.putExtra(ApiConstants.CONTENT_VERSION, gameData.version);
	// startService(intent);
	// }

	public void doUpdateContent() {
		gameStateService.doUpdateContent();
	}

	@Override
	protected void onStart() {
		super.onStart();
		setupReceiver();
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	public void skipUpdate() {
		gotoMainScreen();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// startService(new Intent(getBaseContext(), ContentService.class));
	}

	@Override
	protected void onPause() {

		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	protected void onGameDataUpdated(Intent intent) {
		gotoMainScreen();

	}

	@Override
	protected void onGameStateServiceConnected() {
		// TODO Auto-generated method stub

	}

}
