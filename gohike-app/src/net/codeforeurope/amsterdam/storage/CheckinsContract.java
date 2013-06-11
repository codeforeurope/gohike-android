package net.codeforeurope.amsterdam.storage;

import java.text.ParseException;

import net.codeforeurope.amsterdam.model.Checkin;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.content.ContentValues;
import android.provider.BaseColumns;

public class CheckinsContract {

	public static abstract class CheckinsEntry implements BaseColumns {
		public static final String TABLE_NAME = "checkin";
		public static final String COLUMN_NAME_LOCATION_ID = "location_id";
		public static final String COLUMN_NAME_ROUTE_ID = "route_id";
		public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
		public static final String COLUMN_NAME_UPLOADED = "uploaded";
		public static final String[] COLUMNS = new String[] { _ID,
				COLUMN_NAME_LOCATION_ID, COLUMN_NAME_ROUTE_ID,
				COLUMN_NAME_TIMESTAMP, COLUMN_NAME_UPLOADED };

		public static Checkin fromContentValues(ContentValues values) {
			Checkin checkin = new Checkin();
			checkin.id = values.getAsInteger(_ID);
			checkin.locationId = values.getAsInteger(COLUMN_NAME_LOCATION_ID);
			checkin.routeId = values.getAsInteger(COLUMN_NAME_ROUTE_ID);
			try {
				checkin.timestamp = ApiConstants.DATE_FORMAT.parse(values
						.getAsString(COLUMN_NAME_TIMESTAMP));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return checkin;
		}

		public static ContentValues toContentValues(Checkin checkin) {
			ContentValues values = new ContentValues();

			values.put(COLUMN_NAME_LOCATION_ID, checkin.locationId);
			values.put(COLUMN_NAME_ROUTE_ID, checkin.routeId);
			values.put(COLUMN_NAME_TIMESTAMP,
					ApiConstants.DATE_FORMAT.format(checkin.timestamp));
			values.put(COLUMN_NAME_UPLOADED, false);
			return values;

		}
	}

}
