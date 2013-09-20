package net.codeforeurope.amsterdam.model;

import net.codeforeurope.amsterdam.util.DataConstants;
import android.os.Build;
import android.os.Bundle;

public class ConnectParams {

	String user_name;
	String user_email;

	String fb_id;
	String fb_token;
	long fb_expires_at;

	String device_id;
	String device_platform;
	String device_version;

	public ConnectParams(Bundle params, String deviceId) {
		user_name = params.getString(DataConstants.FACEBOOK_USER_NAME);
		user_email = params.getString(DataConstants.FACEBOOK_USER_EMAIL);
		fb_id = params.getString(DataConstants.FACEBOOK_USER_ID);
		fb_token = params.getString(DataConstants.FACEBOOK_ACCESS_TOKEN);
		fb_expires_at = params.getLong(DataConstants.FACEBOOK_ACCESS_TOKEN_EXPIRATION);
		device_id = deviceId;
		device_platform = "android";
		device_version = Build.VERSION.RELEASE;
	}

}
