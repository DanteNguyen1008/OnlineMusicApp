package com.bigcatfamily.myringtones.model;

import java.util.ArrayList;

public class PlayList {
	public String id;
	public long duration;
	public String permaLinkUrl;
	public String uri;
	public int trackCount;
	public int limit, offset;

	public ArrayList<Track> tracks;

	public PlayList(String id, long duration, String permaLinkUrl, String uri, int trackCount, int limit, int offset,
	        ArrayList<Track> tracks) {
		super();
		this.id = id;
		this.duration = duration;
		this.permaLinkUrl = permaLinkUrl;
		this.uri = uri;
		this.trackCount = trackCount;
		this.limit = limit;
		this.offset = offset;
		this.tracks = tracks;
	}
	
	public void addTrack(ArrayList<Track> tracks) {
		this.tracks.addAll(tracks);
		this.trackCount += tracks.size();
	}

	public void mergePlayLists(PlayList anotherPLayList) {
		if (id.equals(anotherPLayList.id)) {
			if (offset < anotherPLayList.offset) {
				tracks.addAll(anotherPLayList.tracks);
			}
		}
	}

}
