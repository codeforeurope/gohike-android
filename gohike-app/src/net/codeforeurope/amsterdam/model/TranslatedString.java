package net.codeforeurope.amsterdam.model;

import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("DefaultLocale")
public class TranslatedString implements Parcelable {

	private static String TRANSLATIONS = "translations";

	private HashMap<String, String> translations = new HashMap<String, String>();

	@SuppressWarnings("unchecked")
	public TranslatedString(Parcel in) {
		Bundle bundle = in.readBundle();
		translations = (HashMap<String, String>) bundle
				.getSerializable(TRANSLATIONS);
	}

	// to handle the catalog one
	public TranslatedString(String value) {
		Locale currentLocale = Locale.getDefault();
		translations.put(currentLocale.getLanguage(), value);
	}

	public TranslatedString(HashMap<String, String> map) {
		translations.putAll(map);
	}

	public String getLocalizedValue() {
		Locale currentLocale = Locale.getDefault();
		Locale englishLocale = Locale.ENGLISH;
		String localeString;
		if (translations.containsKey(currentLocale.getLanguage())) {
			localeString = currentLocale.getLanguage();
		} else if (translations.containsKey(englishLocale.getLanguage())) {
			localeString = englishLocale.getLanguage();
		} else {
			localeString = translations.keySet().iterator().next();
		}
		return translations.get(localeString);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(TRANSLATIONS, translations);
		dest.writeBundle(bundle);

	}

	public static final Parcelable.Creator<TranslatedString> CREATOR = new Parcelable.Creator<TranslatedString>() {
		public TranslatedString createFromParcel(Parcel in) {
			return new TranslatedString(in);
		}

		public TranslatedString[] newArray(int size) {
			return new TranslatedString[size];
		}
	};

}
