package net.codeforeurope.amsterdam.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Reward extends BaseModel implements Parcelable {

	@SerializedName("route_id")
	public long routeId;
	public TranslatedString description;

	public Reward() {
		// TODO Auto-generated constructor stub
	}

	public Reward(Parcel in) {
		// TODO Auto-generated constructor stub
		this.id = in.readLong();
		this.routeId = in.readLong();
		this.name = in.readParcelable(TranslatedString.class.getClassLoader());
		this.description = in.readParcelable(TranslatedString.class
				.getClassLoader());
		this.image = in.readParcelable(Image.class.getClassLoader());
	}

	public static final Parcelable.Creator<Reward> CREATOR = new Parcelable.Creator<Reward>() {
		public Reward createFromParcel(Parcel in) {
			return new Reward(in);
		}

		public Reward[] newArray(int size) {
			return new Reward[size];
		}
	};

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

	}

}
