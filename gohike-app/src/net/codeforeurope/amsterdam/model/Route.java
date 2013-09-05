package net.codeforeurope.amsterdam.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Route extends BaseModel implements Parcelable {

	@SerializedName("profile_id")
	public long profileId;

	public TranslatedString description;
	@SerializedName("published_key")
	public String publishedKey;

	public Image icon;

	public ArrayList<Waypoint> waypoints = new ArrayList<Waypoint>();
	public Reward reward = new Reward();

	public Route(Parcel in) {
		this.id = in.readLong();
		this.profileId = in.readLong();
		this.name = in.readParcelable(TranslatedString.class.getClassLoader());
		this.description = in.readParcelable(TranslatedString.class
				.getClassLoader());
		this.publishedKey = in.readString();
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
		dest.writeLong(this.id);
		dest.writeLong(this.profileId);
		dest.writeParcelable(this.name, 0);
		dest.writeParcelable(this.description, 0);
		dest.writeString(this.publishedKey);
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

	@Override
	public long getNumberOfImages() {
		long numberOfImages = super.getNumberOfImages();
		if (this.icon != null && this.icon.url != null) {
			numberOfImages++;
		}
		if (reward != null) {
			numberOfImages += reward.getNumberOfImages();
		}
		for (Waypoint waypoint : waypoints) {
			numberOfImages += waypoint.getNumberOfImages();
		}
		return numberOfImages;
	}

}
