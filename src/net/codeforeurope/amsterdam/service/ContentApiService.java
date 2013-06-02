/**
 * 
 */
package net.codeforeurope.amsterdam.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;

import android.content.Intent;
import android.util.JsonReader;

/**
 * @author psteininger
 * 
 */
public class ContentApiService extends AbstractApiService {

	private static String NAME = "ContentApiService";

	public static String END_POINT = "content";

	public ContentApiService() {
		super(NAME);
	}

	@Override
	protected HttpRequestBase generateRequest(Intent intent)
			throws UnsupportedEncodingException, JSONException {
		final HttpGet request = new HttpGet(getEndpointUrl());
		request.addHeader("Content-Type", "application/json");
		return request;
	}

	@Override
	protected void processResponse(InputStream responseStream, Intent intent,
			Intent broadCastIntent) throws JSONException, ParseException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
		JsonReader reader = new JsonReader(new InputStreamReader(responseStream, "UTF-8"));
		
//		Log.i("API", responseBody.length() + "");
	}

	@Override
	protected String getEndpointUrl() {
		return String.format(API_URL, END_POINT);
	}

}
