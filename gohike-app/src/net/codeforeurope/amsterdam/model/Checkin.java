package net.codeforeurope.amsterdam.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Checkin implements Parcelable {
	
	@SerializedName("location_id")
	public int locationId;
	
	@SerializedName("route_id")
	public int routeId;
	
	public Date timestamp;

	public Checkin(Parcel in) {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}
	public static final Parcelable.Creator<Checkin> CREATOR = new Parcelable.Creator<Checkin>() {
		public Checkin createFromParcel(Parcel in) {
			return new Checkin(in);
		}

		public Checkin[] newArray(int size) {
			return new Checkin[size];
		}
	};


}
