package net.codeforeurope.amsterdam.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Route extends BaseModelWithIcon implements Parcelable {

	public ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
	public Reward reward = new Reward();

	public Route(Parcel in) {
		this.id = in.readInt();
		this.nameEn = in.readString();
		this.nameNl = in.readString();
		this.descriptionEn = in.readString();
		this.descriptionNl = in.readString();
		this.image = in.readParcelable(Image.class.getClassLoader());
		this.icon = in.readParcelable(Image.class.getClassLoader());
		in.readTypedList(this.waypoints, Waypoint.CREATOR);
		this.reward = in.readParcelable(Reward.class.getClassLoader());
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.id);
		dest.writeString(this.nameEn);
		dest.writeString(this.nameNl);
		dest.writeString(this.descriptionEn);
		dest.writeString(this.descriptionNl);
		dest.writeParcelable(this.image, 0);
		dest.writeParcelable(this.icon, 0);
		dest.writeTypedList(this.waypoints);
		dest.writeParcelable(this.reward, 0);

	}

	public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {
		public Route createFromParcel(Parcel in) {
			return new Route(in);
		}

		public Route[] newArray(int size) {
			return new Route[size];
		}
	};

}
