package net.codeforeurope.amsterdam.adapter;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.R;
import net.codeforeurope.amsterdam.model.City;
import net.codeforeurope.amsterdam.model.LocateData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityListAdapter extends BaseAdapter {

	private Context context;

	private ArrayList<City> cities = new ArrayList<City>();

	private LayoutInflater inflater;

	public CityListAdapter(Context context) {
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return cities.size();
	}

	@Override
	public Object getItem(int position) {
		return cities.get(position);
	}

	@Override
	public long getItemId(int position) {
		return ((City) cities.get(position)).id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder mHolder;
		View v = convertView;
		if (convertView == null) {
			mHolder = new ViewHolder();
			v = inflater.inflate(R.layout.city_list_item, null);
			mHolder.header = (TextView) v.findViewById(R.id.separator);
			mHolder.title = (TextView) v.findViewById(R.id.title);
			v.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) v.getTag();
		}
		final City city = (City) getItem(position);

		mHolder.header.setText(city.name);
		mHolder.title.setText(city.name);
		if (city.id == 0) {
			mHolder.header.setVisibility(View.VISIBLE);
			mHolder.title.setVisibility(View.GONE);
			v.setEnabled(false);
		}
		return v;

	}

	public void setCities(LocateData locateData) {
		cities.clear();
		City within = new City(
				context.getString(R.string.city_list_section_within));
		cities.add(within);
		for (City city : locateData.within) {
			cities.add(city);
		}
		City other = new City(
				context.getString(R.string.city_list_section_other));
		cities.add(other);
		for (City city : locateData.other) {
			cities.add(city);
		}
		notifyDataSetChanged();
	}

	class ViewHolder {

		private TextView header;
		private TextView title;
	}
}
