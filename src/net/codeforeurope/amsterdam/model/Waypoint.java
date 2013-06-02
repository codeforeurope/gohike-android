package net.codeforeurope.amsterdam.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Waypoint implements Parcelable {

	@SerializedName("location_id")
	public int id;

	@SerializedName("route_id")
	public int routeId;

	@SerializedName("name_en")
	public String nameEn;

	@SerializedName("name_nl")
	public String nameNl;

	@SerializedName("description_en")
	public String descriptionEn;

	@SerializedName("description_nl")
	public String descriptionNl;

	@SerializedName("image_data")
	public Image image;

	public double latitude;
	
	public double longitude;
	
	public int rank;

	public Waypoint(Parcel in) {
		this.id = in.readInt();
		this.routeId = in.readInt();
		this.nameEn = in.readString();
		this.nameNl = in.readString();
		this.descriptionEn = in.readString();
		this.descriptionNl = in.readString();
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
		dest.writeInt(this.id);
		dest.writeInt(this.routeId);
		dest.writeString(this.nameEn);
		dest.writeString(this.nameNl);
		dest.writeString(this.descriptionEn);
		dest.writeString(this.descriptionNl);
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

}
