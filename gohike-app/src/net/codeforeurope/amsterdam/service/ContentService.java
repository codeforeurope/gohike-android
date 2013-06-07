package net.codeforeurope.amsterdam.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.ContentServicesHelper;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class ContentService extends IntentService {

	private static String NAME = "ContentService";

	public ContentService() {
		super(NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final Intent broadCastIntent = new Intent();
		final Context context = getBaseContext();
		/*
		 * 1. check if there is a content file in files directory. 2. grab
		 * content from assets 2. get version from json 3.
		 */
		// TODO Auto-generated method stub
		try {
			File contentDirectory = ContentServicesHelper.ensureContentDirectory(context);
			File contentFile = ContentServicesHelper.getContentFile(contentDirectory);
			
			if (contentFile == null) {
				String version = ContentServicesHelper.parseContentVersion(getOriginalContentFile());
				contentFile = ContentServicesHelper.writeOutContentFile(contentDirectory, getOriginalContentFile(),
						version);
			}
			GameData game = ContentServicesHelper.parseGameData(contentFile);
			broadCastIntent.setAction(ApiConstants.ACTION_CONTENT_LOADED);
			broadCastIntent.putExtra(ApiConstants.GAME_DATA, game);
		} catch (IOException e) {
			broadCastIntent.setAction(ApiConstants.ACTION_CONTENT_LOAD_ERROR);
		} finally{
			sendBroadcast(broadCastIntent);
		}
	}

	private InputStream getOriginalContentFile() throws IOException {
		return getAssets().open("content.json");
	}

	

	

}
