package net.codeforeurope.amsterdam.adapter;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.R;
import net.codeforeurope.amsterdam.model.BaseModelWithIcon;
import net.codeforeurope.amsterdam.util.StreamDrawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	protected LayoutInflater inflater;

	protected ArrayList<? extends BaseModelWithIcon> profiles;

	protected Context context;

	private int columnWidth = 0;

	public GridAdapter(Context context,
			ArrayList<? extends BaseModelWithIcon> profiles) {
		super();
		this.context = context;
		this.profiles = profiles;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MainListHolder mHolder;

		View v = convertView;

		if (convertView == null) {
			mHolder = new MainListHolder();
			v = inflater.inflate(R.layout.grid_item, null);
			mHolder.topText = (TextView) v.findViewById(R.id.grid_item_top);
			mHolder.bottomText = (TextView) v
					.findViewById(R.id.grid_item_bottom);
			v.setTag(mHolder);
		} else {
			mHolder = (MainListHolder) v.getTag();

		}
		final BaseModelWithIcon model = profiles.get(position);
		LayerDrawable layerDrawable = getBackground(model, v);

		v.setBackgroundDrawable(layerDrawable);

		mHolder.topText.setText(model.getLocalizedName());

		mHolder.bottomText.setText(context.getResources().getQuantityString(
				R.plurals.number_of_routes, model.getNumberOfChildren(),
				model.getNumberOfChildren()));
		return v;

	}

	private LayerDrawable getBackground(final BaseModelWithIcon model, View v) {
		Bitmap bitmap = BitmapFactory.decodeFile(model.image.localPath);
		if (v.getMeasuredWidth() > 0) {
			columnWidth = v.getMeasuredWidth();

		}
		if (columnWidth > 0) {
			float ratio = (float) bitmap.getWidth()
					/ (float) bitmap.getHeight();

			bitmap = Bitmap.createScaledBitmap(bitmap,
					(int) (columnWidth * ratio), columnWidth, false);
		}
		StreamDrawable drawable = new StreamDrawable(bitmap, 6, 2);

		Drawable drawable2 = context.getResources().getDrawable(
				R.drawable.grid_item);

		LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] {
				drawable, drawable2 });
		return layerDrawable;
	}

	class MainListHolder {

		private TextView topText;
		private TextView bottomText;

	}

	@Override
	public int getCount() {
		return profiles.size();
	}

	@Override
	public Object getItem(int position) {
		return profiles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return (long) profiles.get(position).id;
	}

}
