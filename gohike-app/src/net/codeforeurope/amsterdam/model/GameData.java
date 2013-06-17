package net.codeforeurope.amsterdam.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class GameData implements Parcelable {

	public String version;

	public ArrayList<Profile> profiles = new ArrayList<Profile>();

	public GameData(Parcel in) {
		this.version = in.readString();
		in.readTypedList(this.profiles, Profile.CREATOR);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.version);
		dest.writeTypedList(this.profiles);
	}

	public static final Parcelable.Creator<GameData> CREATOR = new Parcelable.Creator<GameData>() {
		public GameData createFromParcel(Parcel in) {
			return new GameData(in);
		}

		public GameData[] newArray(int size) {
			return new GameData[size];
		}
	};

}
