package net.codeforeurope.amsterdam.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile extends BaseModel implements Parcelable {

	public ArrayList<Route> routes = new ArrayList<Route>();

	public Profile(Parcel in) {
		this.id = in.readLong();
		this.name = in.readParcelable(TranslatedString.class.getClassLoader());
		this.image = in.readParcelable(Image.class.getClassLoader());
		in.readTypedList(this.routes, Route.CREATOR);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeParcelable(this.name, 0);
		dest.writeParcelable(this.image, 0);
		dest.writeTypedList(this.routes);
	}

	public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
		public Profile createFromParcel(Parcel in) {
			return new Profile(in);
		}

		public Profile[] newArray(int size) {
			return new Profile[size];
		}
	};

	@Override
	public long getNumberOfImages() {
		long numberOfImages = super.getNumberOfImages();
		for (Route route : routes) {
			numberOfImages += route.getNumberOfImages();
		}
		return numberOfImages;
	}

}
