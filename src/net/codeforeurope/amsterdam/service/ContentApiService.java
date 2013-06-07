/**
 * 
 */
package net.codeforeurope.amsterdam.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.ContentServicesHelper;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;

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
	protected void processResponse(InputStream responseStream,
			Header[] headers, Intent intent, Intent broadCastIntent)
			throws JSONException, ParseException, UnsupportedEncodingException {
		final Context context = getBaseContext();
		final String version = getVersionFromHeaders(headers);

		try {
			File contentDirectory = ContentServicesHelper
					.ensureContentDirectory(context);
			eraseCurrentContent(contentDirectory);

			File contentFile = ContentServicesHelper.writeOutContentFile(
					contentDirectory, responseStream, version);
			
			GameData game = ContentServicesHelper.parseGameData(contentFile);
			broadCastIntent.setAction(ApiConstants.ACTION_REMOTE_CONTENT_LOADED);
			broadCastIntent.putExtra(ApiConstants.GAME_DATA, game);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

	

	private String getVersionFromHeaders(Header[] headers) {
		for (Header header : headers) {
			if ("Content-Version".equalsIgnoreCase(header.getName())) {
				return header.getValue();
			}
		}
		return null;
	}

	private void eraseCurrentContent(File contentDirectory) {

		File[] files = contentDirectory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				File[] imageFiles = file.listFiles();
				for (File imageFile : imageFiles) {
					imageFile.delete();
				}
			}
			file.delete();
		}

	}

	@Override
	protected String getEndpointUrl() {
		return String.format(API_URL, END_POINT);
	}

}
