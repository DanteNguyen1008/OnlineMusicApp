package com.bigcatfamily.myringtones.contranst;

public class AppConstant {
	public static final String NAMESPACE = "com.bigcatfamily";
	public static final String ITUNE_TOP_HIT_MENU = "itune.top.hits.menu";
    public static final String SEARCH_TRACK_MENU = "itune.top.hits.menu";

    public static final int ITUNE_TOP_HIT_MENU_INDEX = 0;
	public static final int SEARCH_TRACK_MENU_INDEX = ITUNE_TOP_HIT_MENU_INDEX + 1;

	public static final String INTENT_AL_SCREEEN_NAME_EXTRA = "intent.al.screen.name.extra";
	public static final String PLAY_TRACK_HINT_TITLE = "You're listening %s";
	public static final int DOWNLOAD_LIMIT = 5;
	public static final int TOAST_DURATION = 2000;
	public static final String DOWNLOAD_TRACK_FOLDER = "/%s/";
	public static final String DB_FILE_NAME = "myringtones.db";
	public static final int DB_VERSION = 1;

	/* database */
	/* track table */
	public static final String TB_TRACK_NAME = "track";
	public static final String TB_TRACK_CL_ID = "id";
	public static final String TB_TRACK_CL_CREATED_DATE = "createdDate";
	public static final String TB_TRACK_CL_DURATION = "duration";
	public static final String TB_TRACK_CL_IS_STREAMABLE = "isStreamable";
	public static final String TB_TRACK_CL_IS_DOWNLOADABLE = "isDownloadable";
	public static final String TB_TRACK_CL_PURCHASE_URL = "purchaseUrl";
	public static final String TB_TRACK_CL_TITLE = "title";
	public static final String TB_TRACK_CL_DESCRIPTION = "description";
	public static final String TB_TRACK_CL_VIDEO_URL = "videoUrl";
	public static final String TB_TRACK_CL_TRACK_TYPE = "trackType";
	public static final String TB_TRACK_CL_RELEASE_YEAR = "releaseYear";
	public static final String TB_TRACK_CL_RELEASE_MONTH = "releaseMonth";
	public static final String TB_TRACK_CL_RELEASE_DAY = "releaseDay";
	public static final String TB_TRACK_CL_ORIGINAL_FORMAT = "originalFormat";
	public static final String TB_TRACK_CL_LISENCE = "lisence";
	public static final String TB_TRACK_CL_URL = "url";
	public static final String TB_TRACK_CL_PERMAL_LINK = "permaLink";
	public static final String TB_TRACK_CL_ART_WORK_URL = "artWorkUrl";
	public static final String TB_TRACK_CL_STREAM_URL = "streamUrl";
	public static final String TB_TRACK_CL_DOWNLOAD_URL = "downloadUrl";
	public static final String TB_TRACK_CL_PLAY_BACK_COUNT = "playBackCount";
	public static final String TB_TRACK_CL_SINGER_NAME = "singerName";
	public static final String TB_TRACK_CL_LOCAL_URL = "localUrl";

	public static final String[] TB_TRACK_CLS = { TB_TRACK_CL_ID, TB_TRACK_CL_CREATED_DATE, TB_TRACK_CL_DURATION,
	        TB_TRACK_CL_IS_STREAMABLE, TB_TRACK_CL_IS_DOWNLOADABLE, TB_TRACK_CL_PURCHASE_URL, TB_TRACK_CL_TITLE,
	        TB_TRACK_CL_DESCRIPTION, TB_TRACK_CL_VIDEO_URL, TB_TRACK_CL_TRACK_TYPE, TB_TRACK_CL_RELEASE_YEAR,
	        TB_TRACK_CL_RELEASE_MONTH, TB_TRACK_CL_RELEASE_DAY, TB_TRACK_CL_ORIGINAL_FORMAT, TB_TRACK_CL_LISENCE,
	        TB_TRACK_CL_URL, TB_TRACK_CL_PERMAL_LINK, TB_TRACK_CL_ART_WORK_URL, TB_TRACK_CL_STREAM_URL,
	        TB_TRACK_CL_DOWNLOAD_URL, TB_TRACK_CL_PLAY_BACK_COUNT, TB_TRACK_CL_SINGER_NAME, TB_TRACK_CL_LOCAL_URL };

	public static final int ITUNE_TOP_HIT_LIMIT = 100;
    public static final int SEARCH_TRACH_LIMIT = 20;

    public static final String BUNDLE_EXTRA_KEY_SEARCH_CONTENT = "bundle.extra.key.search.content";
}
