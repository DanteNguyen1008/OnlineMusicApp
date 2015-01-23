package com.bigcatfamily.myringtones.model;

import java.io.Serializable;
import java.util.Date;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.utility.Utility;

public class Track implements Serializable {

	public enum eTrackType {
		original, remix, live, recording, spoken, podcast, demo, in_progress, stem, loop, sound_effect, sample, other;

		public static eTrackType getByName(String name) {
			if (name.equals("original"))
				return original;
			if (name.equals("remix"))
				return remix;
			if (name.equals("live"))
				return live;
			if (name.equals("recording"))
				return recording;
			if (name.equals("spoken"))
				return spoken;
			if (name.equals("podcast"))
				return podcast;
			if (name.equals("demo"))
				return demo;
			if (name.equals("in progress"))
				return in_progress;
			if (name.equals("stem"))
				return stem;
			if (name.equals("loop"))
				return loop;
			if (name.equals("sound effect"))
				return sound_effect;
			if (name.equals("sample"))
				return sample;

			return other;
		}
	}

	public enum eLicense {
		no_rights_reserved, all_rights_reserved, cc_by, cc_by_nc, cc_by_nd, cc_by_sa, cc_by_nc_nd, cc_by_nc_sa;

		public static eLicense getByName(String name) {
			if (name.equals("no_rights_reserved"))
				return no_rights_reserved;
			if (name.equals("cc-by"))
				return cc_by;
			if (name.equals("cc-by-nc"))
				return cc_by_nc;
			if (name.equals("cc-by-nd"))
				return cc_by_nd;
			if (name.equals("cc-by-sa"))
				return cc_by_sa;
			if (name.equals("cc-by-nc-nd"))
				return cc_by_nc_nd;
			if (name.equals("cc-by-nc-sa"))
				return cc_by_nc_sa;

			return all_rights_reserved;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -7256190322676926646L;

	public String id;
	public Date createdAt;
	public long duration;
	public boolean isStreamable;
	public boolean isDownloadable;
	public String purchaseUrl;
	public String title;
	public String description;
	public String videoUrl;
	public eTrackType trackType;
	public int releaseYear, releaseMonth, releaseDay;
	public String originalFormat;
	public eLicense license;
	public String uri;
	public String permaLinkUrl;
	public String artWorkUrl;
	public String streamUrl;
	public String downloadUrl;
	public long playBackCount;
	public String singerName;
	public String localUrl;

	public Track(String id, Date createdAt, long duration, boolean isStreamable, boolean isDownloadable,
	        String purchaseUrl, String title, String description, String videoUrl, eTrackType trackType,
	        int releaseYear, int releaseMonth, int releaseDay, String originalFormat, eLicense license, String uri,
	        String permaLinkUrl, String artWorkUrl, String streamUrl, long playBackCount, String singerName, String downloadUrl, String localUrl) {
		super();
		this.id = id;
		this.createdAt = createdAt;
		this.duration = duration;
		this.isStreamable = isStreamable;
		this.isDownloadable = isDownloadable;
		this.purchaseUrl = purchaseUrl;
		this.title = title;
		this.description = description;
		this.videoUrl = videoUrl;
		this.trackType = trackType;
		this.releaseYear = releaseYear;
		this.releaseMonth = releaseMonth;
		this.releaseDay = releaseDay;
		this.originalFormat = originalFormat;
		this.license = license;
		this.uri = uri;
		this.permaLinkUrl = permaLinkUrl;
		this.artWorkUrl = artWorkUrl;
		this.streamUrl = streamUrl;
		this.playBackCount = playBackCount;
		this.singerName = singerName;
		this.downloadUrl = downloadUrl;
		this.localUrl = localUrl;
	}
}
