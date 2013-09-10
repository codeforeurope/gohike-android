package net.codeforeurope.amsterdam.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Waypoint extends BaseModel implements Parcelable {

	@Override
	public boolean equals(Object o) {
		return this.uniqueKey().equals(((Waypoint) o).uniqueKey());
	}

	@SerializedName("location_id")
	protected long id;

	@SerializedName("route_id")
	public long routeId;

	public TranslatedString description;

	public double latitude;

	public double longitude;

	public int rank;

	public Waypoint(Parcel in) {
		this.id = in.readLong();
		this.routeId = in.readLong();
		this.name = in.readParcelable(TranslatedString.class.getClassLoader());
		this.description = in.readParcelable(TranslatedString.class.getClassLoader());
		this.image = in.readParcelable(Image.class.getClassLoader());
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
		this.rank = in.readInt();

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeLong(this.routeId);
		dest.writeParcelable(this.name, 0);
		dest.writeParcelable(this.description, 0);
		dest.writeParcelable(this.image, 0);
		dest.writeDouble(this.latitude);
		dest.writeDouble(this.longitude);
		dest.writeInt(this.rank);

	}

	public static final Parcelable.Creator<Waypoint> CREATOR = new Parcelable.Creator<Waypoint>() {
		public Waypoint createFromParcel(Parcel in) {
			return new Waypoint(in);
		}

		public Waypoint[] newArray(int size) {
			return new Waypoint[size];
		}
	};

	public String uniqueKey() {
		return id + "-" + routeId;
	}

	public long getId() {
		return this.id;
	}
}
