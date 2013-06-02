package net.codeforeurope.amsterdam.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import net.codeforeurope.amsterdam.model.GameData;
import net.codeforeurope.amsterdam.model.Image;
import net.codeforeurope.amsterdam.model.ImageSerializer;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.ContentFileFilter;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class ContentService extends IntentService {

	private static String NAME = "ContentService";

	public ContentService() {
		super(NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final Intent broadCastIntent = new Intent();
		/*
		 * 1. check if there is a content file in files directory. 2. grab
		 * content from assets 2. get version from json 3.
		 */
		// TODO Auto-generated method stub
		try {
			File contentFile = ensureContentFilePresent();
			
			InputStream input = new FileInputStream(contentFile);
			Reader reader = new InputStreamReader(input, "UTF-8");
			Gson gson = buildGson(contentFile);

			long start = System.currentTimeMillis();
			GameData game = gson.fromJson(reader, GameData.class);
			long end = System.currentTimeMillis();
			Log.i(NAME, "Time to parse: " + (end - start) + "ms");
			broadCastIntent.setAction(ApiConstants.ACTION_DATA_LOADED);
			broadCastIntent.putExtra(ApiConstants.GAME_DATA, game);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			sendBroadcast(broadCastIntent);
		}

		// InputStream input = File.open(contentfiles[0]);
	}

	private Gson buildGson(File contentFile) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Image.class, new ImageSerializer(
				contentFile));
		Gson gson = gsonBuilder.create();
		return gson;
	}

	private File ensureContentFilePresent() throws IOException {
		// TODO Auto-generated method stub
		File contentDirectory = new File(getFilesDir(), "content");
		File contentFile = getContentFile(contentDirectory);

		if (contentFile == null) {
			contentFile = writeOutContentFile(contentDirectory);
		} else {
//			contentFile.delete();
//			File imagesDirectory = new File(contentFile.getParentFile(),"images");
//			imagesDirectory.delete();
		}
		return contentFile;

	}

	private File getContentFile(File contentDirectory) {
		File contentFile;
		if (!contentDirectory.exists()) {
			contentDirectory.mkdir();
		}
		File[] files = contentDirectory.listFiles(new ContentFileFilter());
		if (files.length > 0) {
			contentFile = files[0];
		} else {
			contentFile = null;
		}

		return contentFile;
	}

	private File writeOutContentFile(File contentDirectory)
			throws FileNotFoundException, IOException {

		InputStream input = getAssets().open("content.json");
		String version = parseContentVersion(input);
		File jsonFile = new File(contentDirectory, version + ".json");
		OutputStream output = new FileOutputStream(jsonFile);

		input.reset();

		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = input.read(buffer)) >= 0) {
			output.write(buffer, 0, len);
		}
		input.close();
		output.close();
		return jsonFile;
	}

	private String parseContentVersion(InputStream input)
			throws UnsupportedEncodingException, IOException {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(
				new InputStreamReader(input, "UTF-8"));
		reader.beginObject();
		reader.nextName();
		String version = gson.fromJson(reader, String.class);
		return version;
	}

}
