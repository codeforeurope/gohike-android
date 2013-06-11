/**
 * 
 */
package net.codeforeurope.amsterdam.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import net.codeforeurope.amsterdam.model.PingResult;
import net.codeforeurope.amsterdam.util.ApiConstants;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author psteininger
 * 
 */
public class PingApiService extends AbstractApiService {

	private static String NAME = "PingApiService";

	public static String END_POINT = "ping";

	public PingApiService() {
		super(NAME);
	}

	@Override
	protected HttpRequestBase generateRequest(Intent intent)
			throws UnsupportedEncodingException, JSONException {
		String currentVersion = intent
				.getStringExtra(ApiConstants.CONTENT_VERSION);
		final HttpPost request = new HttpPost(getEndpointUrl());
		request.addHeader("Content-Type", "application/json");
		applyTimeoutSettings();
		JSONObject j = new JSONObject();
		j.put(ApiConstants.CURRENT_VERSION, currentVersion);
		HttpEntity entity = new StringEntity(j.toString());
		request.setEntity(entity);
		return request;
	}

	private void applyTimeoutSettings() {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 3000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 5000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
	}

	@Override
	protected void processResponse(InputStream responseStream,
			Header[] headers, Intent intent, Intent broadCastIntent)
			throws JSONException, ParseException, UnsupportedEncodingException {
		Gson gson = buildGson();
		Reader reader = new InputStreamReader(responseStream, "UTF-8");
		long start = System.currentTimeMillis();
		PingResult result = gson.fromJson(reader, PingResult.class);
		long end = System.currentTimeMillis();
		Log.i(NAME, "Time to parse: " + (end - start) + "ms");
		broadCastIntent.setAction(ApiConstants.ACTION_PING_COMPLETE);
		broadCastIntent.putExtra(ApiConstants.PING_RESULT, result);

	}

	@Override
	protected String getEndpointUrl() {
		return String.format(API_URL, END_POINT);
	}

	public static Gson buildGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		return gson;
	}
}
