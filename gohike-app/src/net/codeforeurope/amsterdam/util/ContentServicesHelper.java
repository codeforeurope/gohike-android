package net.codeforeurope.amsterdam.util;

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
import net.codeforeurope.amsterdam.model.gson.ImageTypeAdapter;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class ContentServicesHelper {

	public static File ensureContentDirectory(Context context) {
		File contentDirectory = new File(context.getFilesDir(), "content");
		if (!contentDirectory.exists()) {
			contentDirectory.mkdir();
		}
		return contentDirectory;
	}

	public static File getContentFile(File contentDirectory) {
		File contentFile = null;
		File[] files = contentDirectory.listFiles(new ContentFileFilter());
		if (files.length > 0) {
			contentFile = files[0];
		}
		return contentFile;
	}

	public static File writeOutContentFile(File contentDirectory,
			InputStream input, String version) throws FileNotFoundException,
			IOException {

		File jsonFile = new File(contentDirectory, version + ".json");
		OutputStream output = new FileOutputStream(jsonFile);

		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = input.read(buffer)) >= 0) {
			output.write(buffer, 0, len);
		}
		input.close();
		output.close();
		return jsonFile;
	}

	public static String parseContentVersion(InputStream input)
			throws UnsupportedEncodingException, IOException {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(
				new InputStreamReader(input, "UTF-8"));
		reader.beginObject();
		reader.nextName();
		String version = gson.fromJson(reader, String.class);
		reader.close();
		return version;
	}

	private static Gson buildGson(File contentDirectory) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(Image.class, new ImageTypeAdapter(
				contentDirectory));
		Gson gson = gsonBuilder.create();
		return gson;
	}

	public static GameData parseGameData(File contentFile)
			throws FileNotFoundException, UnsupportedEncodingException {
		InputStream contentFileInput = new FileInputStream(contentFile);
		Reader reader = new InputStreamReader(contentFileInput, "UTF-8");
		Gson gson = buildGson(contentFile.getParentFile());
		// long start = System.currentTimeMillis();
		GameData game = gson.fromJson(reader, GameData.class);
		// long end = System.currentTimeMillis();
		// Log.i("GameDataParser", "Time to parse: " + (end - start) + "ms");
		return game;
	}
}
