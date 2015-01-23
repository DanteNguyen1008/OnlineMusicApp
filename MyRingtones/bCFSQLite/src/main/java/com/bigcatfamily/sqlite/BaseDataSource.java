package com.bigcatfamily.sqlite;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public abstract class BaseDataSource {
	private static final String TAG = BaseDataSource.class.getSimpleName();
	public DatabaseHelper dbHelper;
	protected Context context;
	public SQLiteDatabase database;

	public BaseDataSource(Context context, String dbFileName, int dbVersion) {
		this.context = context;
		dbHelper = new DatabaseHelper(context, dbFileName, null, dbVersion, null);
		dbHelper.createDatabase();
	}

	public void open() {
		if (database == null || !database.isOpen())
			database = dbHelper.openDataBase();
	}

	public void close() {
		try {
			dbHelper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Object> getData(String tbName, String[] colums, String selection, String[] selectionArgs,
	        String groupBy, String having, String orderBy) {
		try {
			ArrayList<Object> object = new ArrayList<Object>();

			Cursor cursor = database.query(tbName, colums, selection, selectionArgs, groupBy, having, orderBy);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				object.add(cursorToData(cursor));
				cursor.moveToNext();
			}

			cursor.close();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public ArrayList<Object> getDataListByArgs(String tbName, String[] colums, String[] argsCol, Object[] argsValue) {
		try {
			ArrayList<Object> data = new ArrayList<Object>();

			String whereClause = "";
			for (int i = 0; i < argsCol.length; i++) {
				if (argsValue[i] instanceof String)
					whereClause += argsCol[i] + " = \"" + argsValue[i] + "\"";
				else
					whereClause += argsCol[i] + " = " + argsValue[i];
				if (i + 2 == argsCol.length)
					whereClause += " AND ";
			}

			Log.d(TAG, "Where clause of query " + whereClause);

			Cursor cursor = database.query(tbName, colums, whereClause, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				data.add(cursorToData(cursor));
				cursor.moveToNext();
			}

			cursor.close();
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Object getDataById(String tbName, String[] colums, String columsIdName, String id) {
		Object data = null;
		try {
			Cursor cursor = database.query(tbName, colums, columsIdName + "=" + id, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				data = cursorToData(cursor);
				cursor.moveToNext();
			}

			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	protected Cursor executeQuery(String query, String[] arguments) {
		try {
			Cursor cur = database.rawQuery(query, arguments);
			return cur;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public abstract Object insert(Object data);

	public abstract int update(Object data);

	public abstract int delete(Object data);

	public abstract ArrayList<Object> getAllData();

	public abstract Object cursorToData(Cursor cursor);

	public Object cursorToData(Cursor cursor, int i) {
		return null;
	}
}
