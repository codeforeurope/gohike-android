package net.codeforeurope.amsterdam.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class LocateData implements Parcelable {

	public ArrayList<City> within = new ArrayList<City>();

	public ArrayList<City> other = new ArrayList<City>();

	public LocateData(Parcel in) {
		in.readTypedList(this.within, City.CREATOR);
		in.readTypedList(this.other, City.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(this.within);
		dest.writeTypedList(this.other);

	}

	public static final Parcelable.Creator<LocateData> CREATOR = new Parcelable.Creator<LocateData>() {
		public LocateData createFromParcel(Parcel in) {
			return new LocateData(in);
		}

		public LocateData[] newArray(int size) {
			return new LocateData[size];
		}
	};

}
