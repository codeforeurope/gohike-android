package net.codeforeurope.amsterdam.util;

import android.net.http.AndroidHttpClient;

public class HttpClient {

	private static String USER_AGENT = "GoHike 1.0";

	public static AndroidHttpClient newInstance() {

		AndroidHttpClient client = AndroidHttpClient.newInstance(USER_AGENT);

		return client;

	}
}
