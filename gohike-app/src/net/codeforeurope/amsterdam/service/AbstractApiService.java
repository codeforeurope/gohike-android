package net.codeforeurope.amsterdam.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.HttpClient;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;

import android.app.IntentService;
import android.content.Intent;
import android.net.http.AndroidHttpClient;

public abstract class AbstractApiService extends IntentService {
	protected static String API_URL = "http://gohike.herokuapp.com/api/%s";

	public AbstractApiService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final Intent broadCastIntent = new Intent();
		final AndroidHttpClient client = HttpClient.newInstance();
		try {

			final HttpRequestBase request = generateRequest(intent);
			AndroidHttpClient.modifyRequestToAcceptGzipResponse(request);

			final HttpResponse response = client.execute(request);
			final int responseCode = response.getStatusLine().getStatusCode();

			if (responseCode < 200 || responseCode >= 300) {
				broadCastIntent.putExtra(ApiConstants.SUCCESS, false);
			} else {
				broadCastIntent.putExtra(ApiConstants.SUCCESS, true);
				final InputStream responseStream = AndroidHttpClient
						.getUngzippedContent(response.getEntity());

				processResponse(responseStream, response.getAllHeaders(),
						intent, broadCastIntent);
			}
		} catch (IOException e) {
			broadCastIntent
					.putExtra(ApiConstants.ERROR_MESSAGE, e.getMessage());
			processResponseError(intent, broadCastIntent, e);
		} catch (JSONException e) {
			processResponseError(intent, broadCastIntent, e);
			broadCastIntent
					.putExtra(ApiConstants.ERROR_MESSAGE, e.getMessage());
		} catch (Exception e) {
			processResponseError(intent, broadCastIntent, e);
			broadCastIntent.putExtra(ApiConstants.ERROR, ApiConstants.ERROR);
		} finally {
			client.close();

			sendBroadcast(broadCastIntent);
		}
	}

	protected void processResponseError(Intent intent, Intent broadCastIntent,
			Exception e) {
		// TODO Auto-generated method stub

	}

	protected abstract HttpRequestBase generateRequest(Intent intent)
			throws UnsupportedEncodingException, JSONException;

	protected abstract void processResponse(InputStream responseStream,
			Header[] headers, Intent intent, Intent broadCastIntent)
			throws JSONException, ParseException, UnsupportedEncodingException;

	protected abstract String getEndpointUrl();

}
