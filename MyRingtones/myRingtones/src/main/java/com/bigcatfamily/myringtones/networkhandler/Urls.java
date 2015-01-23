package com.bigcatfamily.myringtones.networkhandler;

/**
 * Urls.java
 * 
 * <br>
 * <b>Purpose</b> :
 * 
 * <br>
 * <b>Optional info</b> :
 * 
 * <br>
 * <b>author</b> :
 * 
 * <br>
 * <b>date</b> : Jul 23, 2013
 * 
 * <br>
 * <b>lastChangedRevision</b> :
 * 
 * <br>
 * <b>lastChangedDate</b> :
 * 
 */
public interface Urls {
	public final String SOUNDCLOUD_ID = "cd7760bb233646df5105e99b06a97dca";
	public final String PLAY_LIST_STREAM_ID = "52220346";
	public final String PLAY_LIST_DOWNLOAD_ID = "52214847";

	public final String MAIN_URL = "http://api.soundcloud.com/playlists/%s.json?client_id=" + SOUNDCLOUD_ID;
	public final String SEARCH_URL = "https://api.soundcloud.com/search/sounds.json?client_id=" + SOUNDCLOUD_ID
	        + "&q=%s";
	public final String STREAM_URL = "%s?client_id=" + SOUNDCLOUD_ID;
	public final String DOWNLOAD_URL = "%s?client_id=" + SOUNDCLOUD_ID;
	public final String ITUNE_TOP_HIT_LINK = "https://itunes.apple.com/us/rss/topsongs/limit=%s/json";
}
