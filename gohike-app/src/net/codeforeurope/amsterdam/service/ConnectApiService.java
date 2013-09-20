/**
 * 
 */
package net.codeforeurope.amsterdam.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import net.codeforeurope.amsterdam.model.ConnectParams;
import net.codeforeurope.amsterdam.model.ConnectResponse;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.AppSecret;
import net.codeforeurope.amsterdam.util.ContentServicesHelper;
import net.codeforeurope.amsterdam.util.DataConstants;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;

import com.google.gson.Gson;

/**
 * @author psteininger
 * 
 */
public class ConnectApiService extends AbstractApiService {

	private static String NAME = "ConnectApiService";

	public static String END_POINT = "connect";

	public ConnectApiService() {
		super(NAME);
	}

	@Override
	protected HttpRequestBase generateRequest(Intent intent) throws UnsupportedEncodingException, JSONException {
		final HttpPost request = new HttpPost(getEndpointUrl());
		request.addHeader("Content-Type", "application/json");
		request.addHeader("Take-A-Hike-Secret", AppSecret.TOKEN);
		Bundle connectParams = intent.getBundleExtra(DataConstants.CONNECT_PARAMS);
		ConnectParams params = new ConnectParams(connectParams, Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID));
		HttpEntity entity = new StringEntity(new Gson().toJson(params, ConnectParams.class));
		request.setEntity(entity);
		return request;
	}

	@Override
	protected void processResponse(InputStream responseStream, Header[] headers, Intent intent, Intent broadCastIntent)
			throws JSONException, ParseException, UnsupportedEncodingException {

		try {
			ConnectResponse response = ContentServicesHelper.parseConnectResponse(responseStream);
			broadCastIntent.setAction(ActionConstants.CONNECT_COMPLETE);
			broadCastIntent.putExtra(DataConstants.PLAYER_ID, response.id);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected String getEndpointUrl() {
		return String.format(API_URL, END_POINT);
	}

}
