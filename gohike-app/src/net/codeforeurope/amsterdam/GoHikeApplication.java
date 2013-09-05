package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.City;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GoHikeApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public long getSelectedCityId() {
		SharedPreferences settings = getSharedPreferences(
				ApiConstants.GOHIKE_SETTINGS, 0);
		return settings.getLong(ApiConstants.CURRENT_CITY_ID, 0);
	}

	public void setSelectedCity(City city) {
		SharedPreferences settings = getSharedPreferences(
				ApiConstants.GOHIKE_SETTINGS, 0);
		Editor editor = settings.edit();
		editor.putLong(ApiConstants.CURRENT_CITY_ID, city.id);
		editor.putString(ApiConstants.CURRENT_CITY_NAME, city.name);
		editor.commit();
	}

	public String getSelectedCityName() {
		SharedPreferences settings = getSharedPreferences(
				ApiConstants.GOHIKE_SETTINGS, 0);
		return settings.getString(ApiConstants.CURRENT_CITY_NAME, "");
	}
}
