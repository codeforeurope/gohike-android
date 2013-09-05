/**
 * 
 */
package net.codeforeurope.amsterdam.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import net.codeforeurope.amsterdam.model.LocateData;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ContentServicesHelper;
import net.codeforeurope.amsterdam.util.DataConstants;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;

import android.content.Intent;
import android.location.Location;

/**
 * @author psteininger
 * 
 */
public class LocateApiService extends AbstractApiService {

	private static String NAME = "LocateApiService";

	public static String END_POINT = "locate";

	public LocateApiService() {
		super(NAME);
	}

	@Override
	protected HttpRequestBase generateRequest(Intent intent)
			throws UnsupportedEncodingException, JSONException {
		final HttpPost request = new HttpPost(getEndpointUrl());
		request.addHeader("Content-Type", "application/json");
		Location location = intent
				.getParcelableExtra(DataConstants.LOCATE_CURRENT_LOCATION);
		HttpEntity entity = new StringEntity("{\"latitude\": "
				+ location.getLatitude() + ", \"longitude\": "
				+ location.getLongitude() + "}");
		request.setEntity(entity);
		return request;
	}

	@Override
	protected void processResponse(InputStream responseStream,
			Header[] headers, Intent intent, Intent broadCastIntent)
			throws JSONException, ParseException, UnsupportedEncodingException {

		try {
			LocateData game = ContentServicesHelper
					.parseLocateResponse(responseStream);
			broadCastIntent.setAction(ActionConstants.LOCATE_COMPLETE);
			broadCastIntent.putExtra(DataConstants.LOCATE_RETURN_DATA, game);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected String getEndpointUrl() {
		return String.format(API_URL, END_POINT);
	}

}
