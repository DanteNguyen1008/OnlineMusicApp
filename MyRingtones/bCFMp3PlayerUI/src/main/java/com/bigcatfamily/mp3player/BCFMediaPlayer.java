package com.bigcatfamily.mp3player;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class BCFMediaPlayer extends RelativeLayout implements OnClickListener, OnTouchListener, OnCompletionListener,
        OnBufferingUpdateListener, OnPreparedListener {

	private ImageView mBtnNext, mBtnPrev, mBtnPause, mBtnPlay;
	private SeekBar mSeekBar;
	private MediaPlayer mMediaPlayer;
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler();
	private Runnable notification;
	private int mediaFileLengthInMilliseconds; // this value contains the song
	                                           // duration in milliseconds. Look
	                                           // at getDuration() method in
	                                           // MediaPlayer class
	private String m_strUrl;
	private IMediaListener mMediaListener;
	private boolean mBIsDataSourceSet = false;

	public BCFMediaPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public BCFMediaPlayer(Context context) {
		super(context);
		init(context, null);
	}

	public BCFMediaPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		/* inflate layout */
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.bcf_mp3_player, this);

		mBtnNext = (ImageView) findViewById(R.id.btn_next);
		mBtnPrev = (ImageView) findViewById(R.id.btn_prev);
		mBtnPause = (ImageView) findViewById(R.id.btn_pause);
		mBtnPlay = (ImageView) findViewById(R.id.btn_play);

		mSeekBar = (SeekBar) findViewById(R.id.sb_play);
		mSeekBar.setMax(99);
		mSeekBar.setOnTouchListener(this);

		mBtnNext.setOnClickListener(this);
		mBtnPrev.setOnClickListener(this);
		mBtnPause.setOnClickListener(this);
		mBtnPlay.setOnClickListener(this);

		/* setup attrs - later */
		// if (attrs != null) {
		// TypedArray style = getContext().obtainStyledAttributes(attrs,
		// R.styleable.NLNotificationButton);
		// mNotificationColor =
		// style.getColor(R.styleable.NLNotificationButton_notification_color,
		// Color.RED);
		// mNotificationTextColor =
		// style.getColor(R.styleable.NLNotificationButton_notification_text_color,
		// Color.WHITE);
		//
		// style.recycle();
		// }

		mMediaPlayer = new MediaPlayer();
		// mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnBufferingUpdateListener(this);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnPreparedListener(this);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		mSeekBar.setSecondaryProgress(percent);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mBtnPlay.setVisibility(View.VISIBLE);
		mBtnPause.setVisibility(View.GONE);
		mBIsDataSourceSet = false;
		mMediaListener.finish();
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v.getId() == R.id.sb_play) {
			/**
			 * Seekbar onTouch event handler. Method which seeks MediaPlayer to
			 * seekBar primary progress position
			 */
			if (mMediaPlayer.isPlaying()) {
				int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * mSeekBar.getProgress();
				mMediaPlayer.seekTo(playPositionInMillisecconds);
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mBtnNext)) {
			if (mMediaListener != null) {
				stop();
				mMediaListener.next();
			}
		} else if (v.equals(mBtnPrev)) {
			if (mMediaListener != null) {
				stop();
				mMediaListener.previous();
			}
		} else if (v.equals(mBtnPlay)) {
			play();
		} else if (v.equals(mBtnPause)) {
			pause();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mediaFileLengthInMilliseconds = mp.getDuration();
		mBIsDataSourceSet = true;
		mMediaPlayer.start();
		mBtnPlay.setVisibility(View.GONE);
		mBtnPause.setVisibility(View.VISIBLE);
		primarySeekBarProgressUpdater();
	}

	private void pause() {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.pause();
			mBtnPlay.setVisibility(View.VISIBLE);
			mBtnPause.setVisibility(View.GONE);
		}
	}

	public void stop() {
		if (mMediaPlayer.isPlaying()) {
			mMediaPlayer.stop();
			mMediaPlayer.reset();
		}
	}

	/**
	 * You have to invoke setUrl to set stream url before this method
	 * */
	public void play() {
		if (m_strUrl != null && !m_strUrl.equals("")) {
			if (!mBIsDataSourceSet) {
				try {
					mMediaPlayer.setDataSource(m_strUrl);
					mMediaPlayer.prepareAsync();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (!mMediaPlayer.isPlaying()) {
					mMediaPlayer.start();
					mBtnPlay.setVisibility(View.GONE);
					mBtnPause.setVisibility(View.VISIBLE);
					primarySeekBarProgressUpdater();
				}
			}

		}
	}

	/**
	 * Method which updates the SeekBar primary progress by current song playing
	 * position
	 */
	private void primarySeekBarProgressUpdater() {
		mSeekBar.setProgress((int) (((float) mMediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This
		                                                                                                                 // math
		                                                                                                                 // construction
		                                                                                                                 // give
		                                                                                                                 // a
		                                                                                                                 // percentage
		                                                                                                                 // of
		                                                                                                                 // "was playing"/"song length"
		if (mMediaPlayer.isPlaying()) {
			if (notification == null)
				notification = new Runnable() {
					public void run() {
						primarySeekBarProgressUpdater();
					}
				};
			handler.postDelayed(notification, 1000);
		}
	}

	/* Have to set methods */
	public void setOnMediaListener(IMediaListener listener) {
		mMediaListener = listener;
	}

	public void setUrl(String url) {
		this.m_strUrl = url;
		mBIsDataSourceSet = false;
		try {
			mMediaPlayer.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearInstance() {
		mMediaPlayer.reset();
		mMediaPlayer.release();
		mMediaPlayer = null;
		handler.removeCallbacks(notification);
	}
}
