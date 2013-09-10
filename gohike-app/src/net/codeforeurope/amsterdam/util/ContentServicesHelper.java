package net.codeforeurope.amsterdam.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import net.codeforeurope.amsterdam.model.Image;
import net.codeforeurope.amsterdam.model.LocateData;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.TranslatedString;
import net.codeforeurope.amsterdam.model.gson.TranslatedStringTypeAdapter;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class ContentServicesHelper {

	public static File ensureRouteDirectory(Context context, Route route) {
		File routeDirectory = ensureSubDirectory(route.getId() + "", ensureImagesDirectory(context, route.getClass()));
		return routeDirectory;
	}

	public static <T> File ensureImagesDirectory(Context context, Class<T> clazz) {
		String imagesDirectoryName = clazz.getSimpleName().toLowerCase();
		File baseDirectory = context.getFilesDir();
		File imagesDirectory = ensureSubDirectory(imagesDirectoryName, baseDirectory);
		return imagesDirectory;
	}

	public static File ensureSubDirectory(String subDirectoryName, File baseDirectory) {
		File imagesDirectory = new File(baseDirectory, subDirectoryName);
		if (!imagesDirectory.exists()) {
			imagesDirectory.mkdir();
		}
		return imagesDirectory;
	}

	public static File getContentFile(File contentDirectory) {
		File contentFile = null;
		File[] files = contentDirectory.listFiles(new ContentFileFilter());
		if (files.length > 0) {
			contentFile = files[0];
		}
		return contentFile;
	}

	public static String writeOutImageFile(Image image, File target) throws FileNotFoundException, IOException {
		if (image.url != null) {
			final URL url = new URL(image.url);
			InputStream input = url.openConnection().getInputStream();

			OutputStream output = new FileOutputStream(target);

			byte[] buffer = new byte[8192];
			int len = 0;
			while ((len = input.read(buffer)) >= 0) {
				output.write(buffer, 0, len);
			}
			input.close();
			output.close();
			return target.getPath();
		} else {
			return null;
		}
	}

	public static File writeRouteStreamFile(InputStream input, File target) throws FileNotFoundException, IOException {

		OutputStream output = new FileOutputStream(target);

		byte[] buffer = new byte[8192];
		int len = 0;
		while ((len = input.read(buffer)) >= 0) {
			output.write(buffer, 0, len);
		}
		input.close();
		output.close();
		return target;
	}

	public static String parseContentVersion(InputStream input) throws UnsupportedEncodingException, IOException {
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new InputStreamReader(input, "UTF-8"));
		reader.beginObject();
		reader.nextName();
		String version = gson.fromJson(reader, String.class);
		reader.close();
		return version;
	}

	/**
	 * Builds a GSON object with additional TypeAdapters and exclusion
	 * strategies for parsing responses from APIs.
	 * 
	 * @return
	 */
	private static Gson buildResponseGson() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(TranslatedString.class, new TranslatedStringTypeAdapter());
		Gson gson = gsonBuilder.create();
		return gson;
	}

	/**
	 * @param responseStream
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static LocateData parseLocateResponse(InputStream responseStream) throws UnsupportedEncodingException {
		Reader reader = new InputStreamReader(responseStream, "UTF-8");
		Gson gson = new Gson();
		LocateData locateData = gson.fromJson(reader, LocateData.class);
		return locateData;
	}

	public static ArrayList<Profile> parseCatalogResponse(InputStream responseStream)
			throws UnsupportedEncodingException {
		Reader reader = new InputStreamReader(responseStream, "UTF-8");
		Type listType = new TypeToken<ArrayList<Profile>>() {
		}.getType();

		Gson gson = buildResponseGson();
		ArrayList<Profile> profiles = gson.fromJson(reader, listType);
		return profiles;
	}

	public static Route parseRouteDownloadResponse(InputStream responseStream) throws UnsupportedEncodingException {
		Reader reader = new InputStreamReader(responseStream, "UTF-8");
		Gson gson = buildResponseGson();
		Route route = gson.fromJson(reader, Route.class);
		return route;
	}
}
