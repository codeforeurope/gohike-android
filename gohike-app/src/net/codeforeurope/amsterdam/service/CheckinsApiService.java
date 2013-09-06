package net.codeforeurope.amsterdam.service;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.codeforeurope.amsterdam.model.Checkin;
import net.codeforeurope.amsterdam.model.gson.CheckinExclusionStrategy;
import net.codeforeurope.amsterdam.model.gson.DateTypeAdapter;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.AppSecret;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import android.content.Intent;
import android.provider.Settings.Secure;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CheckinsApiService extends AbstractApiService {

	private static String NAME = "CheckinApiService";

	public static String END_POINT = "checkin";

	public CheckinsApiService() {
		super(NAME);
	}

	@Override
	protected HttpRequestBase generateRequest(Intent intent)
			throws UnsupportedEncodingException, JSONException {
		ArrayList<Checkin> checkins = intent
				.getParcelableArrayListExtra(ApiConstants.OUTSTANDING_CHECKINS);
		String checkinsJson = convertCheckinsToJson(checkins);
		final HttpPost request = new HttpPost(getEndpointUrl());
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Take-A-Hike-Secret", AppSecret.TOKEN);
		HttpEntity entity = new StringEntity(checkinsJson);
		request.setEntity(entity);
		return request;
	}

	private String convertCheckinsToJson(List<Checkin> checkins) {
		Gson gson = makeGson();
		String deviceId = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		String json = "{\"identifier\": \"" + deviceId + "\", \"checkins\": "
				+ gson.toJson(checkins) + "}";
		return json;
	}

	private Gson makeGson() {
		Gson gson = new GsonBuilder()
				.setExclusionStrategies(new CheckinExclusionStrategy())
				.registerTypeAdapter(Date.class, new DateTypeAdapter())
				.create();
		return gson;
	}

	@Override
	protected void processResponse(InputStream responseStream,
			Header[] headers, Intent intent, Intent broadCastIntent)
			throws JSONException, ParseException, UnsupportedEncodingException {
		ArrayList<Checkin> checkins = intent
				.getParcelableArrayListExtra(ApiConstants.OUTSTANDING_CHECKINS);
		broadCastIntent.setAction(ActionConstants.CHECKINS_UPLOAD_COMPLETE);
		broadCastIntent.setClass(getBaseContext(), CheckinService.class);
		broadCastIntent.putParcelableArrayListExtra(
				ApiConstants.UPLOADED_CHECKINS, checkins);

	}

	@Override
	protected String getEndpointUrl() {
		return String.format(API_URL, END_POINT);
	}

	@Override
	public void sendBroadcast(Intent intent) {
		// here we want to start the service to update checkins
		startService(intent);
	}

}
