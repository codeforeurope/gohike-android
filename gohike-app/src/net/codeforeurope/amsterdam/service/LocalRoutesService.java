package net.codeforeurope.amsterdam.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.ContentServicesHelper;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

public class LocalRoutesService extends IntentService {

	private static String NAME = "LocalRoutesService";

	public LocalRoutesService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final Intent broadCastIntent = new Intent();
		final Context context = getBaseContext();
		try {

			ArrayList<Route> routes = new ArrayList<Route>();
			File routesDirectory = ContentServicesHelper.ensureImagesDirectory(context, Route.class);
			for (File routeDirectory : routesDirectory.listFiles()) {
				File routeContentFile = new File(routeDirectory, ApiConstants.ROUTE_FILE_NAME);

				if (routeContentFile.exists() && routeContentFile.length() > 0) {
					InputStream responseStream = new FileInputStream(routeContentFile);
					Route route = ContentServicesHelper.parseRouteDownloadResponse(responseStream);
					routes.add(route);
				}
			}
			broadCastIntent.setAction(ActionConstants.ROUTES_LOAD_COMPLETE);
			broadCastIntent.putExtra(DataConstants.LOCAL_ROUTES, routes);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			sendBroadcast(broadCastIntent);
		}
	}

}
