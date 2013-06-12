package net.codeforeurope.amsterdam.service;

import java.util.ArrayList;

import net.codeforeurope.amsterdam.model.Checkin;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.storage.CheckinsContract;
import net.codeforeurope.amsterdam.storage.CheckinsDbHelper;
import net.codeforeurope.amsterdam.util.ApiConstants;
import net.codeforeurope.amsterdam.util.CheckinUtil;
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

		if (ApiConstants.ACTION_LOAD_CHECKINS.equals(intent.getAction())) {
			handleLoadCheckins(broadcastIntent);

		} else if (ApiConstants.CHECKINS_UPLOADED.equals(intent.getAction())) {
			handleCheckinsUploaded(intent, broadcastIntent);

		} else {
			handleSaveCheckins(intent, broadcastIntent);
		}

		sendBroadcast(broadcastIntent);
	}

	private void handleLoadCheckins(Intent broadcastIntent) {
		broadcastIntent.setAction(ApiConstants.ACTION_CHECKINS_LOADED);
		ArrayList<Checkin> localCheckins = loadLocalCheckins();
		broadcastIntent.putParcelableArrayListExtra(
				ApiConstants.LOCAL_CHECKINS, localCheckins);
	}

	private void handleSaveCheckins(Intent intent, Intent broadcastIntent) {
		Waypoint currentTarget = intent
				.getParcelableExtra(ApiConstants.CURRENT_TARGET);
		Checkin checkin = CheckinUtil.fromWaypoint(currentTarget);
		broadcastIntent.setAction(ApiConstants.ACTION_CHECKIN_SAVED);
		broadcastIntent.putExtra(ApiConstants.CHECKIN, checkin);

		ArrayList<Checkin> checkinsToUpload = persistCheckin(checkin);
		Intent uploadIntent = new Intent(getBaseContext(),
				CheckinsApiService.class);
		uploadIntent.putParcelableArrayListExtra(
				ApiConstants.OUTSTANDING_CHECKINS, checkinsToUpload);
		startService(uploadIntent);
	}

	private void handleCheckinsUploaded(Intent intent, Intent broadcastIntent) {
		broadcastIntent.setAction(ApiConstants.ACTION_CHECKINS_UPLOADED);
		ArrayList<Checkin> checkinsUploaded = intent
				.getParcelableArrayListExtra(ApiConstants.UPLOADED_CHECKINS);
		updateCheckinsUploaded(checkinsUploaded);
	}

	private ArrayList<Checkin> loadLocalCheckins() {

		ArrayList<Checkin> localCheckins = new ArrayList<Checkin>();

		Cursor c = db.query(CheckinsContract.CheckinsEntry.TABLE_NAME,
				CheckinsContract.CheckinsEntry.COLUMNS, null, null, null, null,
				null);

		while (c.moveToNext()) {
			ContentValues map = new ContentValues();
			DatabaseUtils.cursorRowToContentValues(c, map);
			Checkin cc = CheckinsContract.CheckinsEntry.fromContentValues(map);
			localCheckins.add(cc);
		}
		return localCheckins;
	}

	private void updateCheckinsUploaded(ArrayList<Checkin> checkinsUploaded) {
		if (checkinsUploaded.size() > 0) {
			ContentValues updates = makeUpdateValues();

			String selection = makeSelectionString(checkinsUploaded);

			String[] selectionArgs = makeSelectionArguments(checkinsUploaded);
			db.update(CheckinsContract.CheckinsEntry.TABLE_NAME, updates,
					selection, selectionArgs);
		}

	}

	private String[] makeSelectionArguments(ArrayList<Checkin> checkinsUploaded) {
		ArrayList<String> idsToUpdate = new ArrayList<String>();
		for (Checkin checkin : checkinsUploaded) {
			idsToUpdate.add(checkin.id + "");
		}

		String[] selectionArgs = idsToUpdate.toArray(new String[idsToUpdate
				.size()]);
		return selectionArgs;
	}

	private String makeSelectionString(ArrayList<Checkin> checkinsUploaded) {
		String selection = CheckinsContract.CheckinsEntry._ID + " IN ("
				+ makePlaceholders(checkinsUploaded.size()) + ")";
		return selection;
	}

	private ContentValues makeUpdateValues() {
		ContentValues updates = new ContentValues();
		updates.put(CheckinsContract.CheckinsEntry.COLUMN_NAME_UPLOADED, true);
		return updates;
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
							+ " = 0", null, null, null, null);

			while (c.moveToNext()) {
				ContentValues map = new ContentValues();
				DatabaseUtils.cursorRowToContentValues(c, map);
				Checkin cc = CheckinsContract.CheckinsEntry
						.fromContentValues(map);
				toUpload.add(cc);
			}
		}

		return toUpload;
	}

	private String makePlaceholders(int len) {

		StringBuilder sb = new StringBuilder(len * 2 - 1);
		sb.append("?");
		for (int i = 1; i < len; i++) {
			sb.append(",?");
		}
		return sb.toString();

	}
}
