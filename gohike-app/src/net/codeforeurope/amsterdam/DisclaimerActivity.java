package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.util.DataConstants;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.webkit.WebView;

public class DisclaimerActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.disclaimer);
		WebView webView = (WebView) findViewById(R.id.disclaimer_webview);
		webView.loadUrl(getIntent().getStringExtra(DataConstants.DISCLAIMER_URL));
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getIntent().getStringExtra(DataConstants.DISCLAIMER_TITLE));

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			overridePendingTransition(R.anim.enter_from_left, R.anim.leave_to_right);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
