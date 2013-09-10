/**
 * 
 */
package net.codeforeurope.amsterdam.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ApiConstants;
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
public class RouteApiService extends AbstractApiService {

	private static String NAME = "RouteApiService";

	public static String END_POINT = "/route/%d";

	public RouteApiService() {
		super(NAME);
	}

	@Override
	protected HttpRequestBase generateRequest(Intent intent) throws UnsupportedEncodingException, JSONException {

		Route route = intent.getParcelableExtra(DataConstants.ROUTE);
		final HttpGet request = new HttpGet(String.format(getEndpointUrl(), route.getId()));
		request.addHeader("Content-Type", "application/json");
		return request;
	}

	@Override
	protected void processResponse(InputStream responseStream, Header[] headers, Intent intent, Intent broadCastIntent)
			throws JSONException, ParseException, UnsupportedEncodingException {
		try {
			Route routePreview = intent.getParcelableExtra(DataConstants.ROUTE);
			File routeDirectory = ContentServicesHelper.ensureRouteDirectory(getBaseContext(), routePreview);
			File routeFile = ContentServicesHelper.writeRouteStreamFile(responseStream, new File(routeDirectory,
					ApiConstants.ROUTE_FILE_NAME));
			Route route = ContentServicesHelper.parseRouteDownloadResponse(new FileInputStream(routeFile));

			broadCastIntent.setAction(ActionConstants.ROUTE_DOWNLOAD_COMPLETE);
			broadCastIntent.putExtra(DataConstants.DOWNLOADED_ROUTE, route);

		} catch (IOException e) {
			broadCastIntent.setAction(ActionConstants.ROUTE_DOWNLOAD_COMPLETE);
			broadCastIntent.putExtra(DataConstants.API_ERROR, e);
		}

	}

	@Override
	protected String getEndpointUrl() {
		return String.format(API_URL, END_POINT);
	}

}
