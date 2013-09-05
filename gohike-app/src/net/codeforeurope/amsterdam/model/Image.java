package net.codeforeurope.amsterdam.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable {

	public String localPath;

	public String md5;

	public String url;

	public Image() {

	}

	public Image(Parcel in) {
		this.localPath = in.readString();
		this.md5 = in.readString();
		this.url = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.localPath);
		dest.writeString(this.md5);
		dest.writeString(this.url);

	}

	public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
		public Image createFromParcel(Parcel in) {
			return new Image(in);
		}

		public Image[] newArray(int size) {
			return new Image[size];
		}
	};

}
