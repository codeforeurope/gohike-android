package net.codeforeurope.amsterdam.model;

import java.text.ParseException;
import java.util.Date;

import net.codeforeurope.amsterdam.util.ApiConstants;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Checkin implements Parcelable {

	public int id = 0;

	@SerializedName("location_id")
	public int locationId;

	@SerializedName("route_id")
	public int routeId;

	public Date timestamp;

	public Checkin() {

	}

	public Checkin(Parcel in) {
		id = in.readInt();
		locationId = in.readInt();
		routeId = in.readInt();
		try {
			timestamp = ApiConstants.DATE_FORMAT.parse(in.readString());
		} catch (ParseException e) {
			timestamp = new Date();
		}

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeInt(locationId);
		dest.writeInt(routeId);
		dest.writeString(ApiConstants.DATE_FORMAT.format(timestamp));

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
