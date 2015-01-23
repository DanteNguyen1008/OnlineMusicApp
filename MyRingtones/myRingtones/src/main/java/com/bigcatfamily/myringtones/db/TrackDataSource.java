package com.bigcatfamily.myringtones.db;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.model.Track.eLicense;
import com.bigcatfamily.myringtones.model.Track.eTrackType;
import com.bigcatfamily.myringtones.utility.Utility;
import com.bigcatfamily.sqlite.BaseDataSource;

public class TrackDataSource extends BaseDataSource {

	public TrackDataSource(Context context) {
		super(context, AppConstant.DB_FILE_NAME, AppConstant.DB_VERSION);
	}

	@Override
	public Object insert(Object data) {
		try {
			Track track = (Track) data;

			/* check team existed */
			Track _track = (Track) getDataById(AppConstant.TB_TRACK_NAME, AppConstant.TB_TRACK_CLS,
			        AppConstant.TB_TRACK_CL_ID, track.id);

			if (_track != null)
				return null;

			ContentValues values = new ContentValues();
			values.put(AppConstant.TB_TRACK_CL_ID, track.id);
			// values.put(AppConstant.TB_TRACK_CL_CREATED_DATE,
			// Utility.getStrTimeFromDate(track.createdAt));
			values.put(AppConstant.TB_TRACK_CL_CREATED_DATE, Utility.getStrTimeFromDate(new Date()));
			values.put(AppConstant.TB_TRACK_CL_DURATION, track.duration);
			values.put(AppConstant.TB_TRACK_CL_IS_STREAMABLE, track.isStreamable);
			values.put(AppConstant.TB_TRACK_CL_IS_DOWNLOADABLE, track.isDownloadable);
			values.put(AppConstant.TB_TRACK_CL_PURCHASE_URL, track.purchaseUrl);
			values.put(AppConstant.TB_TRACK_CL_TITLE, track.title);
			values.put(AppConstant.TB_TRACK_CL_DESCRIPTION, track.description);
			values.put(AppConstant.TB_TRACK_CL_VIDEO_URL, track.videoUrl);
			values.put(AppConstant.TB_TRACK_CL_TRACK_TYPE, track.trackType.ordinal());
			values.put(AppConstant.TB_TRACK_CL_RELEASE_YEAR, track.releaseYear);
			values.put(AppConstant.TB_TRACK_CL_RELEASE_MONTH, track.releaseMonth);
			values.put(AppConstant.TB_TRACK_CL_RELEASE_DAY, track.releaseDay);
			values.put(AppConstant.TB_TRACK_CL_ORIGINAL_FORMAT, track.originalFormat);
			values.put(AppConstant.TB_TRACK_CL_LISENCE, track.license.ordinal());
			values.put(AppConstant.TB_TRACK_CL_URL, track.uri);
			values.put(AppConstant.TB_TRACK_CL_PERMAL_LINK, track.permaLinkUrl);
			values.put(AppConstant.TB_TRACK_CL_ART_WORK_URL, track.artWorkUrl);
			values.put(AppConstant.TB_TRACK_CL_STREAM_URL, track.streamUrl);
			values.put(AppConstant.TB_TRACK_CL_DOWNLOAD_URL, track.downloadUrl);
			values.put(AppConstant.TB_TRACK_CL_PLAY_BACK_COUNT, track.playBackCount);
			values.put(AppConstant.TB_TRACK_CL_SINGER_NAME, track.singerName);
			values.put(AppConstant.TB_TRACK_CL_LOCAL_URL, track.localUrl);

			database.insert(AppConstant.TB_TRACK_NAME, null, values);

			return track;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public int update(Object data) {
		return 0;
	}

	@Override
	public int delete(Object data) {
		if (data != null) {
			String whereClause = AppConstant.TB_TRACK_CL_ID + " = " + ((Track) data).id;
			return database.delete(AppConstant.TB_TRACK_NAME, whereClause, null);
		}

		return 0;
	}

	public ArrayList<Track> getTracksByPaging(int offset, int limit) {
		ArrayList<Track> tracks = new ArrayList<Track>();

		String query = "SELECT * FROM " + AppConstant.TB_TRACK_NAME + " ORDER BY "
		        + AppConstant.TB_TRACK_CL_CREATED_DATE + " DESC LIMIT " + limit + " OFFSET " + offset;

		Cursor cursor = executeQuery(query, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			tracks.add((Track) cursorToData(cursor));
			cursor.moveToNext();
		}

		cursor.close();

		return tracks;
	}

	@Override
	public ArrayList<Object> getAllData() {
		return getData(AppConstant.TB_TRACK_NAME, AppConstant.TB_TRACK_CLS, null, null, null, null, null);
	}

	public Track getTrackById(String id) {
		return (Track) getDataById(AppConstant.TB_TRACK_NAME, AppConstant.TB_TRACK_CLS, AppConstant.TB_TRACK_CL_ID, id);
	}

	@Override
	public Object cursorToData(Cursor cursor) {

		String id = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_ID));
		Date createdAt = Utility.convertStringToDateLocal(cursor.getString(cursor
		        .getColumnIndex(AppConstant.TB_TRACK_CL_CREATED_DATE)));
		long duration = cursor.getLong(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_DURATION));
		boolean isStreamable = cursor.getInt(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_IS_STREAMABLE)) == 1 ? true
		        : false;
		boolean isDownloadable = cursor.getInt(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_IS_DOWNLOADABLE)) == 1 ? true
		        : false;
		String purchaseUrl = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_PURCHASE_URL));
		String title = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_TITLE));
		String description = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_DESCRIPTION));
		String videoUrl = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_VIDEO_URL));
		eTrackType trackType = eTrackType.values()[cursor.getInt(cursor
		        .getColumnIndex(AppConstant.TB_TRACK_CL_TRACK_TYPE))];
		int releaseYear = cursor.getInt(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_RELEASE_YEAR));
		int releaseMonth = cursor.getInt(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_RELEASE_MONTH));
		int releaseDay = cursor.getInt(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_RELEASE_DAY));
		String originalFormat = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_ORIGINAL_FORMAT));
		eLicense license = eLicense.values()[cursor.getInt(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_LISENCE))];
		String uri = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_URL));
		String permaLinkUrl = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_PERMAL_LINK));
		String artWorkUrl = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_ART_WORK_URL));
		String streamUrl = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_STREAM_URL));
		String downloadUrl = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_DOWNLOAD_URL));
		long playBackCount = cursor.getLong(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_PLAY_BACK_COUNT));
		String singerName = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_SINGER_NAME));
		String localUrl = cursor.getString(cursor.getColumnIndex(AppConstant.TB_TRACK_CL_LOCAL_URL));

		return new Track(id, createdAt, duration, isStreamable, isDownloadable, purchaseUrl, title, description,
		        videoUrl, trackType, releaseYear, releaseMonth, releaseDay, originalFormat, license, uri, permaLinkUrl,
		        artWorkUrl, streamUrl, playBackCount, singerName, downloadUrl, localUrl);
	}
}
