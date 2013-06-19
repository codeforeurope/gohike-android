package net.codeforeurope.amsterdam.model;

import com.google.gson.annotations.SerializedName;

public abstract class BaseModelWithIcon extends BaseModel {

	@SerializedName("icon_data")
	public Image icon;

	public abstract int getNumberOfChildren();

}