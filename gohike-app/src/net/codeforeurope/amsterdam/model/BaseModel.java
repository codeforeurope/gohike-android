package net.codeforeurope.amsterdam.model;

import com.google.gson.annotations.SerializedName;

public class BaseModel {

	public int id;
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

	public BaseModel() {
		super();
	}

}