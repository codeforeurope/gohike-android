package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
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
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		gameData = getIntent().getParcelableExtra(ApiConstants.GAME_DATA);
		currentProfile = getIntent().getParcelableExtra(
				ApiConstants.CURRENT_PROFILE);
		adapter = new GridAdapter(this, currentProfile.routes);

		gridView = (GridView) findViewById(R.id.grid);
		// setupReceiver();
		gridView.setAdapter(adapter);

	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            Intent intent = new Intent(this, ProfileGridActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            intent.putExtra(ApiConstants.GAME_DATA, gameData);
	            startActivity(intent);
	            overridePendingTransition(R.anim.enter_from_left,R.anim.leave_to_right); 
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
