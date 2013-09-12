package net.codeforeurope.amsterdam;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.codeforeurope.amsterdam.model.Checkin;
import net.codeforeurope.amsterdam.model.City;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GoHikeApplication extends Application {
	public static final long CATALOG_THRESHOD = 24 * 60 * 60 * 1000;

	private Date catalogUpdatedAt;

	private HashMap<Long, ArrayList<Profile>> catalogs = new HashMap<Long, ArrayList<Profile>>();

	private HashMap<Long, Route> downloadedRoutes = new HashMap<Long, Route>();

	private HashMap<Long, Route> completedRoutes = new HashMap<Long, Route>();

	private HashMap<String, Checkin> localCheckins = new HashMap<String, Checkin>();

	private Route currentRoute;

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	/**
	 * @return long current cityId
	 */
	public long getSelectedCityId() {
		SharedPreferences settings = getSharedPreferences(ApiConstants.GOHIKE_SETTINGS, 0);
		return settings.getLong(ApiConstants.CURRENT_CITY_ID, 0);
	}

	/**
	 * Saves the selected city id and name in shared preferences.
	 * 
	 * @param city
	 */
	public void setSelectedCity(City city) {
		SharedPreferences settings = getSharedPreferences(ApiConstants.GOHIKE_SETTINGS, 0);
		Editor editor = settings.edit();
		editor.putLong(ApiConstants.CURRENT_CITY_ID, city.id);
		editor.putString(ApiConstants.CURRENT_CITY_NAME, city.name);
		editor.commit();
	}

	/**
	 * @return String
	 */
	public String getSelectedCityName() {
		SharedPreferences settings = getSharedPreferences(ApiConstants.GOHIKE_SETTINGS, 0);
		return settings.getString(ApiConstants.CURRENT_CITY_NAME, "");
	}

	/**
	 * Responsible for setting the correct route object into the application
	 * state.
	 * 
	 * If there is a downloaded route that route will be used, rather than the
	 * preview.
	 * 
	 * @param route
	 */
	public void setCurrentRoute(Route route) {
		if (downloadedRoutes.containsKey(route.getId())) {
			this.currentRoute = downloadedRoutes.get(route.getId());
		} else {
			this.currentRoute = route;
		}
	}

	/**
	 * Determines if the catalog should be refreshed or not.
	 * 
	 * @return boolean
	 */
	public boolean shouldRequestCatalog() {
		if (catalogs.get(getSelectedCityId()) != null && catalogUpdatedAt != null
				&& new Date().getTime() - catalogUpdatedAt.getTime() < CATALOG_THRESHOD) {
			return false;
		}
		return true;
	}

	/**
	 * @return Route current route
	 */
	public Route getCurrentRoute() {
		return currentRoute;
	}

	/**
	 * @return ArrayList<Profile> Current catalog
	 */
	public ArrayList<Profile> getCurrentCatalog() {
		return catalogs.get(getSelectedCityId());
	}

	public void storeCatalog(ArrayList<Profile> profiles) {
		catalogs.put(getSelectedCityId(), profiles);
		analyzeRoutesForUpdates(profiles);
		catalogUpdatedAt = new Date();
	}

	private void analyzeRoutesForUpdates(ArrayList<Profile> profiles) {
		for (Profile profile : profiles) {
			for (Route catalogRoute : profile.routes) {
				if (downloadedRoutes.containsKey(catalogRoute.getId())) {
					Route downloadedRoute = downloadedRoutes.get(catalogRoute.getId());
					if (!downloadedRoute.publishedKey.equals(catalogRoute.publishedKey)) {
						downloadedRoute.setUpdateAvailable(true);
						catalogRoute.setUpdateAvailable(true);
					} else {
						downloadedRoute.setUpdateAvailable(false);
						catalogRoute.setUpdateAvailable(false);
					}
				}
			}
		}
	}

	public void storeDownloadedRoute(Route route) {
		downloadedRoutes.put(route.getId(), route);
		setCurrentRoute(route);
		analyzeRoutesForUpdates(getCurrentCatalog());
	}

	public void storeLocalRoutes(ArrayList<Route> routes) {
		downloadedRoutes.clear();
		for (Route route : routes) {
			downloadedRoutes.put(route.getId(), route);
		}

	}

	public Route getLocalRoute(long id) {
		return downloadedRoutes.get(id);
	}

	/**
	 * Stores local checkins in app state.
	 * 
	 * @param checkins
	 */
	public void storeLocalCheckins(ArrayList<Checkin> checkins) {
		localCheckins.clear();
		for (Checkin checkin : checkins) {
			localCheckins.put(checkin.uniqueKey(), checkin);
		}
	}

	/**
	 * Checks if there are any local checkins for that waypoint.
	 * 
	 * @param waypoint
	 * @return boolean
	 */
	public boolean isWaypointCheckedIn(Waypoint waypoint) {
		return localCheckins.containsKey(waypoint.uniqueKey());
	}

	/**
	 * Determines if the route had been completed by the user. If it is, the
	 * answer is cached.
	 * 
	 * @param route
	 * @return
	 */
	public boolean isRouteFinished(Route route) {
		if (!completedRoutes.containsKey(route.getId())) {
			int checkedIn = 0;
			for (Waypoint waypoint : route.waypoints) {
				if (isWaypointCheckedIn(waypoint)) {
					checkedIn++;
				}
			}
			if (checkedIn >= route.waypoints.size() && route.waypoints.size() > 0) {
				completedRoutes.put(route.getId(), route);
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Add a checkin that was just created (stored in local DB)
	 * 
	 * @param localCheckin
	 */
	public void addLocalCheckin(Checkin localCheckin) {
		if (!localCheckins.containsKey(localCheckin.uniqueKey())) {
			localCheckins.put(localCheckin.uniqueKey(), localCheckin);
		}

	}
}
