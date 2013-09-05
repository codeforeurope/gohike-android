package net.codeforeurope.amsterdam.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class City implements Parcelable {

	public long id = 0;

	public String name;

	@SerializedName("state_province")
	public String stateProvince;

	@SerializedName("country_code")
	public String countryCode;

	public City() {

	}

	public City(String name) {
		this.name = name;
	}

	public City(Parcel in) {
		id = in.readLong();
		name = in.readString();
		stateProvince = in.readString();
		countryCode = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.name);
		dest.writeString(this.stateProvince);
		dest.writeString(this.countryCode);
	}

	public static final Parcelable.Creator<City> CREATOR = new Parcelable.Creator<City>() {
		public City createFromParcel(Parcel in) {
			return new City(in);
		}

		public City[] newArray(int size) {
			return new City[size];
		}
	};

}
