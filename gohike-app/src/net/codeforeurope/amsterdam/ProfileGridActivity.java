package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.adapter.GridAdapter;
import net.codeforeurope.amsterdam.model.Profile;
import android.content.Intent;
import android.os.Bundle;
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
	protected void onGameDataUpdated(Intent intent) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onGameStateServiceConnected() {
		adapter = new GridAdapter(this, gameStateService.getProfiles());
		gridView.setAdapter(adapter);
	}

}
