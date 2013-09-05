package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.dialog.FirstLaunchDialogFragment;
import android.os.Bundle;

public class SplashActivity extends AbstractGameActivity {

	FirstLaunchDialogFragment dialog = new FirstLaunchDialogFragment();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isCitySelected()) {
			dialog.show(getSupportFragmentManager(), "first_launch");
		} else {
			gotoGrid();
		}
	}

}
