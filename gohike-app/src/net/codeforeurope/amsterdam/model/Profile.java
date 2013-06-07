package net.codeforeurope.amsterdam.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class Profile extends BaseModelWithIcon implements Parcelable {

	public List<Route> routes = new ArrayList<Route>();

	public Profile(Parcel in) {
		this.id = in.readInt();
		this.nameEn = in.readString();
		this.nameNl = in.readString();
		this.descriptionEn = in.readString();
		this.descriptionNl = in.readString();
		this.image = in.readParcelable(Image.class.getClassLoader());
		this.icon = in.readParcelable(Image.class.getClassLoader());
		in.readTypedList(this.routes, Route.CREATOR);
		
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

}
