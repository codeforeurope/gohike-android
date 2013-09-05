package net.codeforeurope.amsterdam.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	protected LayoutInflater inflater;

	protected ArrayList<? extends Parcelable> profiles;

	protected Context context;

	private int columnWidth = 0;

	public GridAdapter(Context context, ArrayList<? extends Parcelable> profiles) {
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
			// mHolder = new MainListHolder();
			// v = inflater.inflate(R.layout.grid_item, null);
			// mHolder.topText = (TextView) v.findViewById(R.id.grid_item_top);
			// mHolder.bottomText = (TextView) v
			// .findViewById(R.id.grid_item_bottom);
			// v.setTag(mHolder);
		} else {
			mHolder = (MainListHolder) v.getTag();

		}
		final Parcelable model = profiles.get(position);
		// LayerDrawable layerDrawable = getBackground(model, v);
		//
		// v.setBackgroundDrawable(layerDrawable);

		// mHolder.topText.setText(model.getLocalizedName());

		// mHolder.bottomText.setText(context.getResources().getQuantityString(
		// R.plurals.number_of_routes, model.getNumberOfChildren(),
		// model.getNumberOfChildren()));
		return v;

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
		return position;
		// return (long) profiles.get(position).id;
	}

}
