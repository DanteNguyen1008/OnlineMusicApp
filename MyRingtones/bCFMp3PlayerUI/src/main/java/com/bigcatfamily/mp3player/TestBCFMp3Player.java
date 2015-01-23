package com.bigcatfamily.mp3player;

import android.app.Activity;
import android.os.Bundle;

public class TestBCFMp3Player extends Activity implements IMediaListener {

	private BCFMediaPlayer mMediaPlayer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_bcf_mp3_player);
		mMediaPlayer = (BCFMediaPlayer) findViewById(R.id.bcf_media_player);
		mMediaPlayer
		        .setUrl("https://api.soundcloud.com/tracks/155509352/stream?client_id=cd7760bb233646df5105e99b06a97dca");
		mMediaPlayer.setOnMediaListener(this);
	}

	@Override
	public void next() {

	}

	@Override
	public void previous() {

	}

}
