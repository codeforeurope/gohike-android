package net.codeforeurope.amsterdam.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CheckinsDbHelper extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "checkins.db";

	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ CheckinsContract.CheckinsEntry.TABLE_NAME + " ("
			+ CheckinsContract.CheckinsEntry._ID + INTEGER_TYPE
			+ " PRIMARY KEY" + COMMA_SEP
			+ CheckinsContract.CheckinsEntry.COLUMN_NAME_LOCATION_ID
			+ INTEGER_TYPE + COMMA_SEP
			+ CheckinsContract.CheckinsEntry.COLUMN_NAME_ROUTE_ID
			+ INTEGER_TYPE + COMMA_SEP
			+ CheckinsContract.CheckinsEntry.COLUMN_NAME_TIMESTAMP
			+ INTEGER_TYPE + COMMA_SEP
			+ CheckinsContract.CheckinsEntry.COLUMN_NAME_UPLOADED
			+ INTEGER_TYPE + " ); CREATE INDEX `"
			+ CheckinsContract.CheckinsEntry.COLUMN_NAME_UPLOADED + "` ON `"
			+ CheckinsContract.CheckinsEntry.TABLE_NAME + "` (`"
			+ CheckinsContract.CheckinsEntry.COLUMN_NAME_UPLOADED + "`);";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ CheckinsContract.CheckinsEntry.TABLE_NAME;

	public CheckinsDbHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}

}
