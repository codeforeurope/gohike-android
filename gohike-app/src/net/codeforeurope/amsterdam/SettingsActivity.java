package net.codeforeurope.amsterdam;


import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.LoginButton;

public class SettingsActivity extends AbstractGameActivity implements OnClickListener {

	private Button privacyButton;
	private Button termsButton;

	private LoginButton facebookButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		privacyButton = (Button) findViewById(R.id.settings_privacy_button);
		termsButton = (Button) findViewById(R.id.settings_tos_button);
		facebookButton = (LoginButton) findViewById(R.id.settings_login_button);
		privacyButton.setOnClickListener(this);
		termsButton.setOnClickListener(this);
		facebookButton.setReadPermissions(ApiConstants.FACEBOOK_READ_PERMISSIONS);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onFacebookStatusChange(Session session, SessionState state, Exception exception) {
		super.onFacebookStatusChange(session, state, exception);

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(getBaseContext(), DisclaimerActivity.class);
		switch (v.getId()) {
		case R.id.settings_privacy_button:
			intent.putExtra(DataConstants.DISCLAIMER_TITLE, getString(R.string.settings_privacy_policy));
			intent.putExtra(DataConstants.DISCLAIMER_URL,
					String.format(ApiConstants.WEB_BASE_URL, ApiConstants.PRIVACY_DOCUMENT_URI));
			startActivity(intent);
			break;
		case R.id.settings_tos_button:

			intent.putExtra(DataConstants.DISCLAIMER_TITLE, getString(R.string.settings_terms_of_service));
			intent.putExtra(DataConstants.DISCLAIMER_URL,
					String.format(ApiConstants.WEB_BASE_URL, ApiConstants.TOS_DOCUMENT_URI));
			startActivity(intent);
			break;
		}

	}

}
