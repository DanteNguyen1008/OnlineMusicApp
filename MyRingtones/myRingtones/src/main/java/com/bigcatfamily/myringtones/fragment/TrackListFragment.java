package com.bigcatfamily.myringtones.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigcatfamily.mp3player.BCFMediaPlayer;
import com.bigcatfamily.mp3player.IMediaListener;
import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.apdater.TracksAdapter;
import com.bigcatfamily.myringtones.apdater.TracksAdapter.iTrackAdapterActionHandle;
import com.bigcatfamily.myringtones.base.BaseFragment;
import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.model.PlayList;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.networkhandler.MethodParams;
import com.bigcatfamily.myringtones.networkhandler.Urls;
import com.bigcatfamily.myringtones.utility.BCFAnimation;
import com.bigcatfamily.myringtones.utility.Utility;
import com.bigcatfamily.networkhandler.db.DataResult;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

public abstract class TrackListFragment extends BaseFragment implements OnClickListener {
	private static final String TAG = TrackListFragment.class.getSimpleName();
	protected PlayList mPlayList;
//	protected PullAndLoadSwipeListView mListView;
	protected TracksAdapter mAdapter;
	protected boolean pauseOnScroll = false; // or true
	protected boolean pauseOnFling = true; // or false
	protected ProgressBar mProgressBarLoading;
	protected static int TRACK_LIMIT = 20;
	protected int offset = 0;
	protected boolean isFirstTimeLoad = true;
	protected RelativeLayout mLoadMoreProcessBar, mRlPlayTrackHint, mRlPlayTrack;
	protected boolean m_bIsLoadMore = false;
	protected BCFMediaPlayer mMediaPlayer;
	protected ImageView mImgPlayTrackBG, mBtnClosePlayTrack;
	protected TextView mTxtPlayingTrackName, mTxtPlayTrackNameHint;

	protected iTrackAdapterActionHandle mTrackActionHandler;

	private IMediaListener mMediaListener = new IMediaListener() {

		@Override
		public void previous() {
			playTrack(mAdapter.getPrevTrack());
		}

		@Override
		public void next() {
			playTrack(mAdapter.getNextTrack());
		}

		@Override
		public void finish() {
			/* get next music */
			playTrack(mAdapter.getNextTrack());
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = setUpRootView(inflater, container);
		mProgressBarLoading = (ProgressBar) mRootView.findViewById(R.id.process_bar_loading);
		mMediaPlayer = (BCFMediaPlayer) mRootView.findViewById(R.id.bcf_media_player);
		mMediaPlayer.setOnMediaListener(mMediaListener);

		mImgPlayTrackBG = (ImageView) mRootView.findViewById(R.id.img_play_track_bg);
		mBtnClosePlayTrack = (ImageView) mRootView.findViewById(R.id.btn_close_play_track);

		mTxtPlayingTrackName = (TextView) mRootView.findViewById(R.id.txt_track_name);
		mTxtPlayTrackNameHint = (TextView) mRootView.findViewById(R.id.txt_track_name_hint);

		mRlPlayTrack = (RelativeLayout) mRootView.findViewById(R.id.rl_play_track);
		mRlPlayTrackHint = (RelativeLayout) mRootView.findViewById(R.id.rl_play_track_hint);
		mRlPlayTrackHint.setOnClickListener(this);
		mBtnClosePlayTrack.setOnClickListener(this);

		setUpListView(mRootView);

		mProgressBarLoading.setVisibility(View.VISIBLE);
//		mListView.setVisibility(View.GONE);

		OnRequestData();

		AdView adView = (AdView) mRootView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);

		m_bIsLoadMore = false;
		isFirstTimeLoad = true;
		return mRootView;
	}

	protected View setUpRootView(LayoutInflater inflater, ViewGroup container) {
		return (View) inflater.inflate(R.layout.fragment_track_list, container, false);
	}

	protected void LoadSwipListviewSettings() {
//		SwipeListviewManager settings = getSwipeOption();
//		mListView.getRefreshableView().setSwipeMode(settings.getSwipeMode());
//		mListView.getRefreshableView().setSwipeActionLeft(settings.getSwipeActionLeft());
//		mListView.getRefreshableView().setSwipeActionRight(settings.getSwipeActionRight());
//		mListView.getRefreshableView().setAnimationTime(settings.getSwipeAnimationTime());
//		mListView.getRefreshableView().setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
//		mListView.getRefreshableView().setmBIsRestrictedByOffset(settings.isSwipeRestrictedByOffset());
	}

	@SuppressLint("InflateParams")
	protected void setUpListView(View rootView) {
//		/* set up the list view */
//		mListView = (PullAndLoadSwipeListView) rootView.findViewById(R.id.listview_tracks);
		LoadSwipListviewSettings();
		mLoadMoreProcessBar = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.loading_view_item, null);
//		mListView.getRefreshableView().addFooterView(mLoadMoreProcessBar);

		setUpTrackAction();
		mAdapter = setUpAdapter();
//		mListView.setAdapter(mAdapter);

//		mListView
//		        .setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener<SwipeListView>() {
//
//			        @Override
//			        public void onRefresh(PullToRefreshBase<SwipeListView> refreshView) {
//				        offset = 0;
//				        OnRequestData();
//			        }
//
//		        });
//
//		mListView.setOnScrollListener(mListView.getRefreshableView().getTouchListener().makeScrollListener());

		final PauseOnScrollListener pauseOnscrollListener = new PauseOnScrollListener(ImageLoader.getInstance(),
		        pauseOnScroll, pauseOnFling);

//		mListView.getRefreshableView().getTouchListener().setmNlOnScrollListener(new NLOnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				switch (scrollState) {
//				case OnScrollListener.SCROLL_STATE_IDLE:
//					break;
//				case OnScrollListener.SCROLL_STATE_FLING:
//					break;
//
//				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
//					break;
//				default:
//					break;
//				}
//				pauseOnscrollListener.onScrollStateChanged(view, scrollState);
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//				pauseOnscrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//			}
//		});

		/* implement later */

//		mListView.setOnLastItemVisibleListener(setLastItemVisible());

	}

//	protected OnLastItemVisibleListener setLastItemVisible() {
//		return new OnLastItemVisibleListener() {
//
//			@Override
//			public void onLastItemVisible() {
//				if (mLoadMoreProcessBar != null && !m_bIsLoadMore && offset <= mPlayList.trackCount) {
//					offset += TRACK_LIMIT;
//					OnRequestData();
//
//					if (mLoadMoreProcessBar.getVisibility() == View.GONE)
//						mLoadMoreProcessBar.setVisibility(View.VISIBLE);
//					m_bIsLoadMore = true;
//				}
//			}
//		};
//	}

	@Override
	public void ClearInstance() {
		mMediaPlayer.clearInstance();
	}

	protected abstract void OnRequestData();

//	public void CompleteRefreshing() {
//		if (!m_bIsLoadMore) {
//			mListView.onRefreshComplete();
//			if (isFirstTimeLoad) {
//				mProgressBarLoading.setVisibility(View.GONE);
//				mListView.setVisibility(View.VISIBLE);
//				isFirstTimeLoad = false;
//			}
//		} else {
//			m_bIsLoadMore = false;
//			if (mLoadMoreProcessBar.getVisibility() == View.VISIBLE)
//				mLoadMoreProcessBar.setVisibility(View.GONE);
//		}
//	}

	protected void playTrack(Track track) {
		if (track != null) {
			if (Utility.IsEmpty(track.localUrl) || !Utility.checkFileExist(track.localUrl))
				mMediaPlayer.setUrl(String.format(Urls.STREAM_URL, track.streamUrl));
			else
				mMediaPlayer.setUrl(Environment.getExternalStorageDirectory().getAbsolutePath() + track.localUrl);

			mMediaPlayer.play();
			ImageLoader.getInstance().displayImage(track.artWorkUrl, mImgPlayTrackBG,
			        Utility.getDisplayImageOptionWithDefaultImage());
			mTxtPlayingTrackName.setText(track.title);
			mTxtPlayTrackNameHint.setText(String.format(AppConstant.PLAY_TRACK_HINT_TITLE, track.title));
		}
	}

//	protected void refreshList() {
//		if (mPlayList != null && mPlayList.tracks.size() > 0) {
//			if (mAdapter == null) {
//				mAdapter = new TracksAdapter(mPlayList.tracks, mActivity, mTrackActionHandler);
//				mListView.setAdapter(mAdapter);
//			} else {
//				mAdapter.notifyDataSetChanged(mPlayList.tracks);
//			}
//		}
//	}

	@Override
	public void update(DataResult result) {
		switch (((MethodParams) result.getMethodName()).getName()) {
		case GET_SOUND_LIST:
		case GET_DOWNLOAD_SOUND_LIST:
		case GET_SEARCH_TRACK_LIST:
			Log.d(TAG, "Received Data");
			PlayList newPlayList = (PlayList) result.getData();
			if (newPlayList != null) {
				newPlayList.offset = this.offset;
				newPlayList.limit = TRACK_LIMIT;
				if (mPlayList == null)
					mPlayList = newPlayList;
				else
					mPlayList.mergePlayLists(newPlayList);
			}

			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					refreshList();
//					CompleteRefreshing();
				}
			});

			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.equals(mRlPlayTrackHint)) {
			showPlayTrack();
		} else if (v.equals(mBtnClosePlayTrack)) {
			hidePlayTrack();
		}
	}

	protected void showPlayTrack() {
		if (mRlPlayTrack.getVisibility() == View.GONE) {
			mRlPlayTrack.setVisibility(View.VISIBLE);
			Animation animation = BCFAnimation.GetInstance().AnimateInFrom(BCFAnimation.BOTTOM);
			mRlPlayTrack.startAnimation(animation);
		}
	}

	protected void hidePlayTrack() {
		if (mRlPlayTrack.getVisibility() == View.VISIBLE) {
			Animation animation = BCFAnimation.GetInstance().AnimateOutTo(BCFAnimation.BOTTOM);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					mRlPlayTrack.setVisibility(View.GONE);
				}
			});
			mRlPlayTrack.startAnimation(animation);
		}
	}

	protected TracksAdapter setUpAdapter() {
		if (mPlayList != null && mPlayList.tracks != null)
			return new TracksAdapter(mPlayList.tracks, mActivity, mTrackActionHandler);
		return new TracksAdapter(null, mActivity, mTrackActionHandler);
	}

	protected void setUpTrackAction() {
		mTrackActionHandler = new iTrackAdapterActionHandle() {

			@Override
			public void OnTrackClick(Track track) {
				showPlayTrack();
				playTrack(track);
			}

			@Override
			public void OnDownloadClick(Track track) {

			}

			@Override
			public void OnRemoveClick(Track track) {

			}

			@Override
			public void OnSetRingtone(Track track) {

			}
		};
	}

//	public abstract SwipeListviewManager getSwipeOption();
}
