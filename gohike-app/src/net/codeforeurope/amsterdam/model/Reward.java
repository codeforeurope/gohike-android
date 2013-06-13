package net.codeforeurope.amsterdam.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Reward extends BaseModelWithIcon implements Parcelable {

	public Reward() {
		// TODO Auto-generated constructor stub
	}

	public Reward(Parcel in) {
		// TODO Auto-generated constructor stub
		this.id = in.readInt();
		this.nameEn = in.readString();
		this.nameNl = in.readString();
		this.descriptionEn = in.readString();
		this.descriptionNl = in.readString();
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
		// TODO Auto-generated method stub
		dest.writeInt(this.id);
		dest.writeString(this.nameEn);
		dest.writeString(this.nameNl);
		dest.writeString(this.descriptionEn);
		dest.writeString(this.descriptionNl);
		dest.writeParcelable(this.image, 0);
		
	}

	
}
