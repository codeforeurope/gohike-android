package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.adapter.GridAdapter;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ProfileGridActivity extends AbstractGameActivity implements
		OnItemClickListener {

	GridView gridView;
	GridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid);
		gridView = (GridView) findViewById(R.id.grid);
		gridView.setOnItemClickListener(this);
		
		SharedPreferences myPrefs = getSharedPreferences(ApiConstants.PREFERENCES_FILE, MODE_PRIVATE);
		int i = myPrefs.getInt(ApiConstants.PREFERENCES_HELPSHOWN, 0);
		if(i == 0)
		{
			SharedPreferences.Editor e = myPrefs.edit();
			e.putInt(ApiConstants.PREFERENCES_HELPSHOWN, 1); // add or overwrite helpShown
			e.commit(); // this saves to disk and notifies observers
			
			showHelp();
		}
		
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		GridAdapter adapter = (GridAdapter) parent.getAdapter();
		Profile profile = (Profile) adapter.getItem(position);
		gameStateService.setCurrentProfile(profile);
		gameStateService.setCurrentRoute(profile.routes.get(0));

		Intent intent = new Intent(getBaseContext(), RouteDetailActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.enter_from_right, R.anim.leave_to_left);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d("RESTART", "restarting");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onGameDataUpdated(Intent intent) {
		adapter = new GridAdapter(this, gameStateService.getProfiles());
		gridView.setAdapter(adapter);
	}

	@Override
	protected void onGameStateServiceConnected() {
		adapter = new GridAdapter(this, gameStateService.getProfiles());
		gridView.setAdapter(adapter);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.profile_select, menu);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			onBackPressed();
			return true;
		case R.id.menu_view_help:
			// show help here
			showHelp();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void showHelp()
	{
		Intent intent = new Intent(this, HelpFragmentActivity.class);
		startActivity(intent);
	}
}
