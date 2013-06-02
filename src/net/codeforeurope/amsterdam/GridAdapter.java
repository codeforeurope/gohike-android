package net.codeforeurope.amsterdam;

import java.util.List;

import net.codeforeurope.amsterdam.model.BaseModelWithIcon;
import net.codeforeurope.amsterdam.util.StreamDrawable;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

	protected LayoutInflater inflater;

	protected List<? extends BaseModelWithIcon> profiles;

	protected Context context;

	public GridAdapter(Context context,
			List<? extends BaseModelWithIcon> profiles) {
		super();
		this.context = context;
		this.profiles = profiles;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final MainListHolder mHolder;
		GridView gv = (GridView) parent;
		View v = convertView;
		if (convertView == null) {
			mHolder = new MainListHolder();
			v = inflater.inflate(R.layout.profile_item, null);
			mHolder.icon = (ImageView) v.findViewById(R.id.gridIcon);
			v.setTag(mHolder);
		} else {
			mHolder = (MainListHolder) v.getTag();
		}

		final BaseModelWithIcon model = profiles.get(position);
		Bitmap bitmap = BitmapFactory.decodeFile(model.image.localPath);
//		BitmapDrawable bmd = new BitmapDrawable(context.getResources(),
//				model.image.localPath);
//		bmd
//		mHolder.icon.setBackgroundDrawable();
		
		StreamDrawable drawable = new StreamDrawable(bitmap, 6, 4);
		mHolder.icon.setImageDrawable(drawable);
		// v.setLayoutParams(new GridView.LayoutParams(130, 130));
		// mHolder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return v;

	}

	class MainListHolder {

		private ImageView icon;

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
