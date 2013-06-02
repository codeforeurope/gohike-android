package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ProfileGridActivity extends Activity implements OnItemClickListener {
	
	
	GameData gameData;
	
	GridView gridView;
	GridAdapter adapter;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.grid);
		
		
		gameData = getIntent().getParcelableExtra(ApiConstants.GAME_DATA);
		adapter = new GridAdapter(this,gameData.profiles);
		
		gridView = (GridView) findViewById(R.id.grid);
		// setupReceiver();
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		GridAdapter adapter = (GridAdapter) parent.getAdapter();
		Profile profile = (Profile) adapter.getItem(position);
		
		Intent intent = new Intent(getBaseContext(), RouteGridActivity.class);
		intent.putExtra(ApiConstants.GAME_DATA, gameData);
		intent.putExtra(ApiConstants.CURRENT_PROFILE, profile);
		startActivity(intent);
	}
	
}
