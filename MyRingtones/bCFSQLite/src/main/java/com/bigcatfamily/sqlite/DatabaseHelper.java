package com.bigcatfamily.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	public String dbPath;
	private Context mContext;
	private String dbName;
	private String[] createTablesQuery;
	private String[] tablesName;
	private SQLiteDatabase database;

	public DatabaseHelper(Context context, String name, CursorFactory factory, int version, String[] createTablesQuery,
	        String[] tablesName) {
		super(context, name, factory, version);
		this.tablesName = tablesName;
		this.dbName = name;
		this.createTablesQuery = createTablesQuery;
		dbPath = context.getApplicationInfo().dataDir + "/databases/";
		this.mContext = context;
	}

	public DatabaseHelper(Context context, String name, CursorFactory factory, int version, String[] tablesName) {
		super(context, name, factory, version);
		this.dbName = name;
		this.tablesName = tablesName;
		dbPath = context.getApplicationInfo().dataDir + "/databases/";
		this.mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (createTablesQuery != null && createTablesQuery.length > 0) {
			for (String command : createTablesQuery) {
				db.execSQL(command);
			}
		}
	}

	public void createDatabase() {
		// If database not exists copy it from the assets
		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				// Copy the database from assests
				copyDataBase();
				Log.e(TAG, "createDatabase database created");
			} catch (IOException e) {
				Log.e(TAG, "ErrorCopyingDataBase");
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Copy the database from assets
	private void copyDataBase() throws IOException, Exception {
		InputStream mInput = mContext.getAssets().open(dbName);
		String outFileName = dbPath + dbName;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	// Check that the database exists here: /data/data/your
	// package/databases/Da
	// Name
	private boolean checkDataBase() {
		File dbFile = new File(dbPath + dbName);
		return dbFile.exists();
	}

	private boolean clearDatabase() {
		File dbFile = new File(dbPath + dbName);
		return dbFile.delete();
	}

	@Override
	public synchronized void close() {
		try {
			if (database != null)
				database.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.close();

	}

	// Open the database, so we can query it
	public SQLiteDatabase openDataBase() throws SQLException {
		String mPath = dbPath + dbName;
		database = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS
		        | SQLiteDatabase.CREATE_IF_NECESSARY);
		return database;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (tablesName != null && tablesName.length > 0) {
			for (String tableName : tablesName) {
				db.execSQL("DROP TABLE IF EXISTS " + tableName);
			}
			onCreate(db);
		} else {
			try {
				clearDatabase();
				this.getReadableDatabase();
				this.close();
				copyDataBase();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void resetDatabase() {
		try {
			if (database != null) {
				if (tablesName != null && tablesName.length > 0) {
					for (String tableName : tablesName) {
						database.execSQL("DROP TABLE IF EXISTS " + tableName);
					}
				}
				clearDatabase();
				createDatabase();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
