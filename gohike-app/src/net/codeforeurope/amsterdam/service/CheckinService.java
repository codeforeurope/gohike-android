package net.codeforeurope.amsterdam.service;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.model.Checkin;
import net.codeforeurope.amsterdam.storage.CheckinsContract;
import net.codeforeurope.amsterdam.storage.CheckinsDbHelper;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

public class CheckinService extends IntentService {

	private static String NAME = "CheckinService";

	public CheckinService() {
		super(NAME);
	}

	private SQLiteDatabase db;

	@Override
	protected void onHandleIntent(Intent intent) {
		Intent broadcastIntent = new Intent();
		db = new CheckinsDbHelper(getBaseContext()).getWritableDatabase();

		if (ApiConstants.CHECKINS_UPLOADED.equals(intent.getAction())) {
			broadcastIntent.setAction(ApiConstants.ACTION_CHECKINS_UPLOADED);
			ArrayList<Checkin> checkinsUploaded = intent
					.getParcelableArrayListExtra(ApiConstants.UPLOADED_CHECKINS);
			updateCheckinsUploaded(checkinsUploaded);

		} else {

			Checkin checkin = intent.getParcelableExtra(ApiConstants.CHECKIN);
			broadcastIntent.setAction(ApiConstants.ACTION_CHECKIN_SAVED);
			broadcastIntent.putExtra(ApiConstants.CHECKIN, checkin);

			ArrayList<Checkin> checkinsToUpload = persistCheckin(checkin);
			Intent uploadIntent = new Intent(getBaseContext(),
					CheckinsApiService.class);
			uploadIntent.putParcelableArrayListExtra(
					ApiConstants.OUTSTANDING_CHECKINS, checkinsToUpload);
			startService(uploadIntent);
		}

		sendBroadcast(broadcastIntent);
	}

	private void updateCheckinsUploaded(ArrayList<Checkin> checkinsUploaded) {
		ContentValues updates = new ContentValues();
		updates.put(CheckinsContract.CheckinsEntry.COLUMN_NAME_UPLOADED, true);
		ArrayList<String> idsToUpdate = new ArrayList<String>();
		String selection = CheckinsContract.CheckinsEntry._ID + " IN (?)";
		for (Checkin checkin : checkinsUploaded) {
			idsToUpdate.add(checkin.id + "");
		}
		String[] selectionArgs = idsToUpdate.toArray(new String[idsToUpdate
				.size()]);
		db.update(CheckinsContract.CheckinsEntry.TABLE_NAME, updates,
				selection, selectionArgs);

	}

	@SuppressLint("SimpleDateFormat")
	private ArrayList<Checkin> persistCheckin(Checkin checkin) {
		ArrayList<Checkin> toUpload = new ArrayList<Checkin>();

		ContentValues values = CheckinsContract.CheckinsEntry
				.toContentValues(checkin);

		long newRowId = db.insert(CheckinsContract.CheckinsEntry.TABLE_NAME,
				"null", values);
		if (newRowId > -1) {
			Cursor c = db.query(CheckinsContract.CheckinsEntry.TABLE_NAME,
					CheckinsContract.CheckinsEntry.COLUMNS,
					CheckinsContract.CheckinsEntry.COLUMN_NAME_UPLOADED
							+ " = 1", null, null, null, null);
			int totalRows = c.getCount();

			for (int i = 0; i < totalRows; i++) {
				ContentValues map = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(c, map);
				Checkin cc = CheckinsContract.CheckinsEntry
						.fromContentValues(map);
				toUpload.add(cc);
			}
		}

		return toUpload;
	}

}
