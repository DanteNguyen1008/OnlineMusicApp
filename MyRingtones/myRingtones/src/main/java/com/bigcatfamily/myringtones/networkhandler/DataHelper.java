package com.bigcatfamily.myringtones.networkhandler;

import android.content.Context;

import com.bigcatfamily.networkhandler.DataHelperCore;
import com.bigcatfamily.networkhandler.ParserDataHelperCore;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/*extended observable as well*/
public class DataHelper extends DataHelperCore {
	protected DataHelper(Context context, ParserDataHelperCore parseHelper) {
		super(context, parseHelper);
	}

	private volatile static DataHelper uniqueInstance;

	public static DataHelper getInstance(Context context, ParserDataHelperCore parseHelper) {
		if (uniqueInstance == null) {
			synchronized (DataHelper.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new DataHelper(context, parseHelper);
				}
			}
		}
		return uniqueInstance;
	}

	public void getSoundList(int limit, int offset, String playListId) throws UnsupportedEncodingException,
	        JSONException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("limit", limit + "");
		params.put("offset", offset + "");
		String url = String.format(Urls.MAIN_URL, playListId) + "&" + getParamsString(params);
		excute(url, GET, "", null, new MethodParams(Methods.GET_SOUND_LIST), "GET_SOUND_LIST", true);
	}

	public void getDownloadSoundList(int limit, int offset, String playListId) throws UnsupportedEncodingException,
	        JSONException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("limit", limit + "");
		params.put("offset", offset + "");
		String url = String.format(Urls.MAIN_URL, playListId) + "&" + getParamsString(params);
		excute(url, GET, "", null, new MethodParams(Methods.GET_DOWNLOAD_SOUND_LIST), "GET_DOWNLOAD_SOUND_LIST", true);
	}

	public void getSearchStrackList(int limit, int offset, String searchContent) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("limit", limit + "");
		params.put("offset", offset + "");
		String url = String.format(Urls.SEARCH_URL, URLEncoder.encode(searchContent, "UTF-8")) + "&" + getParamsString(params);
		excute(url, GET, "", null, new MethodParams(Methods.GET_SEARCH_TRACK_LIST), "GET_SEARCH_TRACK_LIST", true);
	}

	public void getItuneTopHit(int limit) {
		// Map<String, String> params = new HashMap<String, String>();
		// params.put("limit", limit + "");
		// params.put("offset", offset + "");
		String url = String.format(Urls.ITUNE_TOP_HIT_LINK, limit);
		excute(url, GET, "", null, new MethodParams(Methods.GET_ITUNE_TOP_HIT), "GET_ITUNE_TOP_HIT", true);
	}
}
