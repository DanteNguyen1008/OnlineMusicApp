package com.bigcatfamily.myringtones.model;

public class ItuneTrack {
	public String name;
	public String[] imageLinks;
	public String title;
	public String streamLink;
	public String type;
	public String singer;
	public String releaseDate;

	public ItuneTrack(String name, String[] imageLinks, String title, String streamLink, String type, String singer,
	        String releaseDate) {
		super();
		this.name = name;
		this.imageLinks = imageLinks;
		this.title = title;
		this.streamLink = streamLink;
		this.type = type;
		this.singer = singer;
		this.releaseDate = releaseDate;
	}
}
