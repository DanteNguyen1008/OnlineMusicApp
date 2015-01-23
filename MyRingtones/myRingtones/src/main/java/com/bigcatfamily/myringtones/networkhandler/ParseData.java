package com.bigcatfamily.myringtones.networkhandler;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.model.ItuneTrack;
import com.bigcatfamily.myringtones.model.PlayList;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.model.Track.eLicense;
import com.bigcatfamily.myringtones.model.Track.eTrackType;
import com.bigcatfamily.myringtones.utility.Utility;
import com.bigcatfamily.networkhandler.ParserDataHelperCore;
import com.bigcatfamily.networkhandler.db.BaseMethodParams;
import com.bigcatfamily.networkhandler.db.DataResult;

public class ParseData extends ParserDataHelperCore {
	private static final String TAG = ParseData.class.getSimpleName();
	private Context context;

	private ParseData(Context context) {
		this.context = context;
	}

	private volatile static ParseData uniqueInstance;

	public static ParseData getInstance(Context context) {
		if (uniqueInstance == null) {
			synchronized (DataHelper.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new ParseData(context);
				}
			}
		}
		return uniqueInstance;
	}

	@Override
	public DataResult parseData(JSONObject json, BaseMethodParams methodparam) {
		DataResult result = new DataResult();
		try {
			switch (((MethodParams) methodparam).getName()) {
			case GET_SOUND_LIST:
				result = parseSoundList(json);
				break;

			case GET_DOWNLOAD_SOUND_LIST:
				result = parseDownloadSoundList(json);
				break;

			case GET_SEARCH_TRACK_LIST:
				result = parseSearchSoundList(json);
				break;

			case GET_ITUNE_TOP_HIT:
				result = parseItuneTopHit(json);
				break;

			default:
				break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	private boolean isError(JSONObject json) {
		return !json.isNull("errors");
	}

	public DataResult parseSearchSoundList(JSONObject json) throws JSONException {
		DataResult dataResult = new DataResult();

		if (!isError(json)) {
			ArrayList<Track> tracks = new ArrayList<Track>();

			JSONArray tracksJSON = json.getJSONArray("collection");

			for (int i = 0; i < tracksJSON.length(); i++) {
				Track track = parseTrack(tracksJSON.getJSONObject(i));
				if (track.isStreamable)
					tracks.add(track);
			}

			PlayList playList = new PlayList("1111", 11111, "", "", tracks.size(), 0, 0, tracks);
			dataResult.setData(playList);
		} else {
			JSONArray errorsJSON = json.getJSONArray("errors");
			String error = errorsJSON.getJSONObject(0).getString("error_message");
			dataResult.setErrorMessage(error);
		}

		return dataResult;
	}

	public DataResult parseSoundList(JSONObject json) throws JSONException {
		DataResult dataResult = new DataResult();

		if (!isError(json)) {
			ArrayList<Track> tracks = new ArrayList<Track>();

			JSONArray tracksJSON = json.getJSONArray("tracks");

			for (int i = 0; i < tracksJSON.length(); i++) {
				Track track = parseTrack(tracksJSON.getJSONObject(i));
				if (track.isStreamable)
					tracks.add(track);
			}

			PlayList playList = parsePlayList(json, tracks);
			dataResult.setData(playList);
		} else {
			JSONArray errorsJSON = json.getJSONArray("errors");
			String error = errorsJSON.getJSONObject(0).getString("error_message");
			dataResult.setErrorMessage(error);
		}

		return dataResult;
	}

	public DataResult parseDownloadSoundList(JSONObject json) throws JSONException {
		DataResult dataResult = new DataResult();

		if (!isError(json)) {
			ArrayList<Track> tracks = new ArrayList<Track>();

			JSONArray tracksJSON = json.getJSONArray("tracks");

			for (int i = 0; i < tracksJSON.length(); i++) {
				Track track = parseTrack(tracksJSON.getJSONObject(i));
				if (track.isDownloadable && !Utility.IsEmpty(track.downloadUrl)) {
					tracks.add(track);
				}
			}

			PlayList playList = parsePlayList(json, tracks);
			dataResult.setData(playList);
		} else {
			JSONArray errorsJSON = json.getJSONArray("errors");
			String error = errorsJSON.getJSONObject(0).getString("error_message");
			dataResult.setErrorMessage(error);
		}

		return dataResult;
	}

	private PlayList parsePlayList(JSONObject playListObjJSON, ArrayList<Track> tracks) throws JSONException {
		String id = playListObjJSON.getString("id");
		long duration = playListObjJSON.getLong("duration");
		String permaLinkUrl = playListObjJSON.getString("permalink_url");
		String uri = playListObjJSON.getString("uri");
		int trackCount = playListObjJSON.getInt("id");

		return new PlayList(id, duration, permaLinkUrl, uri, trackCount, 0, 0, tracks);
	}

	private Track parseTrack(JSONObject trackObjJSON) throws JSONException {
		String id = getJSONString(trackObjJSON, "id");
		Date createdAt = Utility.convertStringToDate(getJSONString(trackObjJSON, "created_at"));
		long duration = getJSONLong(trackObjJSON, "duration");
		boolean isStreamable = getJSONBoolean(trackObjJSON, "streamable");
		boolean isDownloadable = getJSONBoolean(trackObjJSON, "downloadable");
		String purchaseUrl = trackObjJSON.getString("purchase_url");
		String title = getJSONString(trackObjJSON, "title");
		String description = getJSONString(trackObjJSON, "description");
		String videoUrl = getJSONString(trackObjJSON, "video_url");
		eTrackType trackType = eTrackType.getByName(getJSONString(trackObjJSON, "track_type"));
		int releaseYear = getJSONInt(trackObjJSON, "release_year");
		int releaseMonth = getJSONInt(trackObjJSON, "release_month");
		int releaseDay = getJSONInt(trackObjJSON, "release_day");
		String originalFormat = getJSONString(trackObjJSON, "original_format");
		eLicense license = eLicense.getByName(getJSONString(trackObjJSON, "license"));
		String uri = getJSONString(trackObjJSON, "uri");
		String permaLinkUrl = getJSONString(trackObjJSON, "permalink_url");
		String artWorkUrl = getJSONString(trackObjJSON, "artwork_url");
		String streamUrl = getJSONString(trackObjJSON, "stream_url");
		long playBackCount = getJSONLong(trackObjJSON, "playback_count");
		JSONObject userJSON = trackObjJSON.getJSONObject("user");
		String singerName = "";
		if (userJSON != null)
			singerName = getJSONString(userJSON, "username");
		String downloadUrl = getJSONString(trackObjJSON, "download_url");
		String localUrl = String.format(AppConstant.DOWNLOAD_TRACK_FOLDER, context.getString(R.string.app_name))
		        + Utility.fileNameFactory(title) + "." + originalFormat;
		return new Track(id, createdAt, duration, isStreamable, isDownloadable, purchaseUrl, title, description,
		        videoUrl, trackType, releaseYear, releaseMonth, releaseDay, originalFormat, license, uri, permaLinkUrl,
		        artWorkUrl, streamUrl, playBackCount, singerName, downloadUrl, localUrl);
	}

	private DataResult parseItuneTopHit(JSONObject json) throws JSONException {
		DataResult dataResult = new DataResult();

		if (!isError(json)) {
			ArrayList<ItuneTrack> tracks = new ArrayList<ItuneTrack>();

			JSONArray tracksJSON = json.getJSONObject("feed").getJSONArray("entry");

			for (int i = 0; i < tracksJSON.length(); i++) {
				ItuneTrack track = parseItuneTrack(tracksJSON.getJSONObject(i));
				tracks.add(track);
			}

			dataResult.setData(tracks);
		} else {
			JSONArray errorsJSON = json.getJSONArray("errors");
			String error = errorsJSON.getJSONObject(0).getString("error_message");
			dataResult.setErrorMessage(error);
		}

		return dataResult;
	}

	private ItuneTrack parseItuneTrack(JSONObject json) throws JSONException {
		JSONObject nameObject = json.getJSONObject("im:name");
		String name = getJSONString(nameObject, "label");
		JSONArray linkArrayJSON = json.getJSONArray("im:image");
		String[] imageLinks = new String[linkArrayJSON.length()];
		for (int i = 0; i < imageLinks.length; i++) {
			imageLinks[i] = getJSONString((JSONObject) linkArrayJSON.get(i), "label");
		}
		String title = getJSONString(json.getJSONObject("title"), "label");
		JSONObject attributeStreamLink = ((JSONObject) json.getJSONArray("link").get(1)).getJSONObject("attributes");
		String streamLink = getJSONString(attributeStreamLink, "href");
		String type = getJSONString(attributeStreamLink, "type");
		String singer = getJSONString(json.getJSONObject("im:artist"), "label");
		String releaseDate = getJSONString(json.getJSONObject("im:releaseDate"), "label");

		return new ItuneTrack(name, imageLinks, title, streamLink, type, singer, releaseDate);
	}
}
