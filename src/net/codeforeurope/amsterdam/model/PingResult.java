package net.codeforeurope.amsterdam.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PingResult implements Parcelable {
	
	public String status;
	
	public int size;

	public PingResult(Parcel in) {
		status = in.readString();
		size = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.status);
		dest.writeInt(this.size);
	}

	public static final Parcelable.Creator<PingResult> CREATOR = new Parcelable.Creator<PingResult>() {
		public PingResult createFromParcel(Parcel in) {
			return new PingResult(in);
		}

		public PingResult[] newArray(int size) {
			return new PingResult[size];
		}
	};

}
