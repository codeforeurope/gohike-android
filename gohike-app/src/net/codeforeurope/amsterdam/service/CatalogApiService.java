/**
 * 
 */
package net.codeforeurope.amsterdam.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;

import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ContentServicesHelper;
import net.codeforeurope.amsterdam.util.DataConstants;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;

import android.content.Intent;

/**
 * @author psteininger
 * 
 */
public class CatalogApiService extends AbstractApiService {

	private static String NAME = "CatalogApiService";

	public static String END_POINT = "catalog/%d";

	public CatalogApiService() {
		super(NAME);
	}

	@Override
	protected HttpRequestBase generateRequest(Intent intent)
			throws UnsupportedEncodingException, JSONException {

		long cityId = intent.getLongExtra(DataConstants.CITY_ID, 0);
		final HttpGet request = new HttpGet(String.format(getEndpointUrl(),
				cityId));
		request.addHeader("Content-Type", "application/json");
		return request;
	}

	@Override
	protected void processResponse(InputStream responseStream,
			Header[] headers, Intent intent, Intent broadCastIntent)
			throws JSONException, ParseException, UnsupportedEncodingException {
		try {

			ArrayList<Profile> profiles = ContentServicesHelper
					.parseCatalogResponse(responseStream);

			broadCastIntent
					.setAction(ActionConstants.CATALOG_DOWNLOAD_COMPLETE);
			broadCastIntent.putExtra(DataConstants.CATALOG_PROFILES, profiles);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	protected String getEndpointUrl() {
		return String.format(API_URL, END_POINT);
	}

}
