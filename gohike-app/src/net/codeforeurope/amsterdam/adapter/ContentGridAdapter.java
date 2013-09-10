package net.codeforeurope.amsterdam.adapter;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.R;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Route;
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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContentGridAdapter extends BaseAdapter implements View.OnClickListener {

	protected LayoutInflater inflater;

	protected ArrayList<Profile> profiles = new ArrayList<Profile>();

	protected Context context;

	public static interface OnGridItemClickListener {
		public void onGridItemClicked(Route route);

	}

	private OnGridItemClickListener listener = null;

	public ContentGridAdapter(Context context) {
		super();
		this.context = context;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setProfiles(ArrayList<Profile> profiles) {
		this.profiles.clear();
		this.profiles.addAll(profiles);
		notifyDataSetChanged();
	}

	@Override
	public boolean hasStableIds() {
		return true;
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
		return profiles.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Profile profile = (Profile) getItem(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.content_grid_group, null);
		}
		TextView headerLabel = (TextView) convertView.findViewById(R.id.content_grid_header_label);
		headerLabel.setText(profile.name.getLocalizedValue());
		ImageView headerIcon = (ImageView) convertView.findViewById(R.id.content_grid_header_icon);

		Bitmap icon = BitmapFactory.decodeFile(profile.image.localPath);
		StreamDrawable iconDrawable = new StreamDrawable(context.getResources(), icon, 24, 0);
		headerIcon.setImageDrawable(iconDrawable);

		GridLayout routeGridLayout = (GridLayout) convertView.findViewById(R.id.content_grid_items);
		routeGridLayout.removeAllViews();

		RelativeLayout.LayoutParams params = generateItemLayoutParams(parent, routeGridLayout);

		for (Route route : profile.routes) {

			ViewGroup gridItemLayout = (ViewGroup) inflater.inflate(R.layout.content_grid_item, null);
			ViewGroup gridItemWrapper = (ViewGroup) gridItemLayout.findViewById(R.id.content_grid_item_wrapper);

			TextView topText = (TextView) gridItemWrapper.findViewById(R.id.content_grid_item_top);
			topText.setText(route.name.getLocalizedValue());

			LayerDrawable background = getGridItemBackground(route, gridItemLayout, params.width);
			gridItemWrapper.setBackgroundDrawable(background);

			routeGridLayout.addView(gridItemLayout, params);

			GridViewHolder holder = new GridViewHolder();
			holder.route = route;
			gridItemLayout.setTag(holder);
			gridItemLayout.setOnClickListener(this);

		}

		return convertView;
	}

	private RelativeLayout.LayoutParams generateItemLayoutParams(ViewGroup parent, GridLayout routeGridLayout) {
		int columnWidth = (parent.getMeasuredWidth() - parent.getPaddingLeft() - parent.getPaddingRight())
				/ routeGridLayout.getColumnCount();

		int width = columnWidth - ((routeGridLayout.getPaddingLeft() + routeGridLayout.getPaddingRight()) / 2);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
		params.setMargins(routeGridLayout.getPaddingLeft(), 0, routeGridLayout.getPaddingRight(), 0);

		return params;
	}

	private LayerDrawable getGridItemBackground(final Route model, View v, int columnWidth) {
		Bitmap bitmap = BitmapFactory.decodeFile(model.icon.localPath);
		if (v.getMeasuredWidth() > 0) {
			columnWidth = v.getMeasuredWidth();

		}
		if (columnWidth > 0) {
			float ratio = (float) bitmap.getWidth() / (float) bitmap.getHeight();

			bitmap = Bitmap.createScaledBitmap(bitmap, (int) (columnWidth * ratio), columnWidth, false);
		}
		StreamDrawable drawable = new StreamDrawable(context.getResources(), bitmap, 6, 2);

		Drawable drawable2 = context.getResources().getDrawable(R.drawable.grid_item);

		LayerDrawable layerDrawable = new LayerDrawable(new Drawable[] { drawable, drawable2 });
		return layerDrawable;
	}

	@Override
	public void onClick(View v) {
		if (this.listener != null) {
			GridViewHolder holder = (GridViewHolder) v.getTag();
			this.listener.onGridItemClicked(holder.route);
		}

	}

	public void setListener(OnGridItemClickListener listener) {
		this.listener = listener;
	}

	public static class GridViewHolder {
		Route route;

	}

}
