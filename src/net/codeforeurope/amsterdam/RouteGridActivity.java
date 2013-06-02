package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.GridView;

public class RouteGridActivity extends Activity {

	GameData gameData;

	GridView gridView;
	GridAdapter adapter;

	private Profile currentProfile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.grid);
		
		gameData = getIntent().getParcelableExtra(ApiConstants.GAME_DATA);
		currentProfile = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_PROFILE);
		adapter = new GridAdapter(this, currentProfile.routes);
		
		
		gridView = (GridView) findViewById(R.id.grid);
		// setupReceiver();
		gridView.setAdapter(adapter);

	}
}
