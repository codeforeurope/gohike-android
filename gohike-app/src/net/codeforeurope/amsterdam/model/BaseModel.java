package net.codeforeurope.amsterdam.model;

public abstract class BaseModel {

	protected long id;

	public TranslatedString name;

	public Image image;

	public BaseModel() {
		super();
	}

	public long getNumberOfImages() {
		if (this.image != null && this.image.url != null) {
			return 1;
		}
		return 0;
	}

	public long getId() {
		return this.id;
	}

}