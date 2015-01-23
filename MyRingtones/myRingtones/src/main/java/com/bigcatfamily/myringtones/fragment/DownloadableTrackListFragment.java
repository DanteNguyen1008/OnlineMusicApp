package com.bigcatfamily.myringtones.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.apdater.TracksAdapter.iTrackAdapterActionHandle;
import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.contranst.CLog;
import com.bigcatfamily.myringtones.db.TrackDataSource;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.networkhandler.DataHelper;
import com.bigcatfamily.myringtones.networkhandler.ParseData;
import com.bigcatfamily.myringtones.networkhandler.Urls;
import com.example.androiddownloadmanager.AndroidDownloader;
import com.example.androiddownloadmanager.INotificationDownloadListener;
import com.example.androiddownloadmanager.NotificationDownloadHandler;

import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

//import com.fortysevendeg.swipelistview.SwipeListView;

public class DownloadableTrackListFragment extends TrackListFragment {
	private static final String TAG = DownloadableTrackListFragment.class.getSimpleName();
	private HashMap<String, AndroidDownloader> mDownloaderList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = super.onCreateView(inflater, container, savedInstanceState);
		mActivity.setTitle(getString(R.string.title_downloadable_list));

		mDownloaderList = new HashMap<String, AndroidDownloader>();

		return mRootView;
	}

	@Override
	protected void OnRequestData() {
		try {
			((DataHelper) DataHelper.getInstance(mActivity, ParseData.getInstance(mActivity))).getDownloadSoundList(
			        TRACK_LIMIT, offset, Urls.PLAY_LIST_DOWNLOAD_ID);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public SwipeListviewManager getSwipeOption() {
//		SwipeListviewManager option = SwipeListviewManager.getInstance();
////		option.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
//		return option;
//	}

	@Override
	protected void setUpTrackAction() {
		mTrackActionHandler = new iTrackAdapterActionHandle() {

			@Override
			public void OnTrackClick(Track track) {
				// hidePlayTrack();
				showPlayTrack();
				playTrack(track);
			}

			@Override
			public void OnDownloadClick(Track track) {
				/* download */
				downloodTrack(track);
			}

			@Override
			public void OnRemoveClick(Track track) {

			}

			@Override
			public void OnSetRingtone(Track track) {

			}
		};
	}

	@SuppressLint("ShowToast")
	private void downloodTrack(final Track track) {
		if (!track.isDownloadable) {
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					Toast.makeText(mActivity, mActivity.getString(R.string.downloaded_stream_track_error),
//					        AppConstant.TOAST_DURATION).show();
				}
			});
			return;
		}

		/* check the track exist in database */
		TrackDataSource trackDS = new TrackDataSource(mActivity);
		trackDS.open();
		if (trackDS.getTrackById(track.id) != null) {
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					Toast.makeText(mActivity, mActivity.getString(R.string.downloaded_file_error),
//					        AppConstant.TOAST_DURATION).show();
				}
			});
			return;
		}
		trackDS.close();
		trackDS = null;

		if (mDownloaderList.containsKey(track.id)) {
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					Toast.makeText(mActivity, mActivity.getString(R.string.download_file_ongrogess),
//					        AppConstant.TOAST_DURATION).show();
				}
			});

			return;
		}

		if (mDownloaderList.size() <= AppConstant.DOWNLOAD_LIMIT) {
			AndroidDownloader downloader = new AndroidDownloader.AndroidDownloaderBulder()
			        .setFileName(track.localUrl)
			        .setDownloadUrl(String.format(Urls.DOWNLOAD_URL, track.downloadUrl))
			        .setDownloadListener(
			                new NotificationDownloadHandler(mActivity, "Download track " + track.title,
			                        R.drawable.track_bg_default_image, new Object[] { track.id },
			                        new INotificationDownloadListener() {

				                        @Override
				                        public void onDowloadFinished(File f, Object[] data) {
					                        try {
						                        CLog.d(TAG, "On Download finished " + data[0]);
						                        /* insert track */
						                        TrackDataSource trackDS = new TrackDataSource(mActivity);
						                        trackDS.open();
						                        trackDS.insert(track);
						                        trackDS.close();
						                        trackDS = null;
						                        mDownloaderList.remove(data[0]);
					                        } catch (Exception e) {
						                        e.printStackTrace();
					                        }
				                        }
			                        })).build();

			mDownloaderList.put(track.id, downloader);
			downloader.run();
		} else {
			mActivity.runOnUiThread(new Runnable() {

				@SuppressLint("ShowToast")
				@Override
				public void run() {
//					Toast.makeText(
//					        mActivity,
//					        String.format(mActivity.getString(R.string.download_file_on_limit),
//					                AppConstant.DOWNLOAD_LIMIT), AppConstant.TOAST_DURATION).show();
				}
			});
		}
	}
}
