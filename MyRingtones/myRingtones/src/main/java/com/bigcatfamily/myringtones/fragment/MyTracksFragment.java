package com.bigcatfamily.myringtones.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.apdater.MyTracksAdapter;
import com.bigcatfamily.myringtones.apdater.TracksAdapter;
import com.bigcatfamily.myringtones.apdater.TracksAdapter.iTrackAdapterActionHandle;
import com.bigcatfamily.myringtones.db.TrackDataSource;
import com.bigcatfamily.myringtones.dialogs.RingtoneDialogFragment;
import com.bigcatfamily.myringtones.model.PlayList;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.utility.Utility;

import java.util.ArrayList;

public class MyTracksFragment extends TrackListFragment {
	private static final String TAG = MyTracksFragment.class.getSimpleName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = super.onCreateView(inflater, container, savedInstanceState);
		mActivity.setTitle(getString(R.string.title_my_tracks_list));

		return mRootView;
	}

	@Override
	protected void OnRequestData() {
		/* request database */
		TrackDataSource trackDS = new TrackDataSource(mActivity);
		trackDS.open();
		ArrayList<Track> tracks = trackDS.getTracksByPaging(offset, TRACK_LIMIT);
		trackDS.close();
		offset += TRACK_LIMIT;
		if (mPlayList == null)
			mPlayList = new PlayList("", 1, "", "", tracks.size(), TRACK_LIMIT, offset, tracks);
		else
			mPlayList.addTrack(tracks);

//		refreshList();
//		CompleteRefreshing();
	}

//	@Override
//	public SwipeListviewManager getSwipeOption() {
//		SwipeListviewManager option = SwipeListviewManager.getInstance();
////		option.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT);
//		option.setSwipeOffsetLeft(mActivity.getResources().getDimension(
//		        R.dimen.swip_listview_offset_padding_left_mytrack));
//		return option;
//	}

	@Override
	protected void LoadSwipListviewSettings() {
//		SwipeListviewManager settings = getSwipeOption();
//		mListView.getRefreshableView().setSwipeMode(settings.getSwipeMode());
//		mListView.getRefreshableView().setSwipeActionLeft(settings.getSwipeActionLeft());
//		mListView.getRefreshableView().setSwipeActionRight(settings.getSwipeActionRight());
//		mListView.getRefreshableView().setAnimationTime(settings.getSwipeAnimationTime());
//		mListView.getRefreshableView().setSwipeOpenOnLongPress(settings.isSwipeOpenOnLongPress());
//		mListView.getRefreshableView().setOffsetLeft(settings.getSwipeOffsetLeft());
//		mListView.getRefreshableView().setmBIsRestrictedByOffset(settings.isSwipeRestrictedByOffset());
	}

	@Override
	protected TracksAdapter setUpAdapter() {
		if (mPlayList != null && mPlayList.tracks != null)
			return new MyTracksAdapter(mPlayList.tracks, mActivity, mTrackActionHandler);
		return new MyTracksAdapter(null, mActivity, mTrackActionHandler);
	}

	@Override
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
				TrackDataSource trackDS = new TrackDataSource(mActivity);
				trackDS.open();
				trackDS.delete(track);
				trackDS.close();

				/* remove track from displaying list */
				for (int i = 0; i < mPlayList.tracks.size(); i++) {
					if (mPlayList.tracks.get(i).id.equals(track.id)) {
						mPlayList.tracks.remove(i);
					}
				}

				mActivity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mAdapter.notifyDataSetChanged(mPlayList.tracks);
					}
				});
			}

			@Override
			public void OnSetRingtone(final Track track) {
				/* show ringtone dialog */
				RingtoneDialogFragment ringtoneDialog = new RingtoneDialogFragment() {

					@Override
					public void OnDialogResult(Object object) {
						boolean isRingtone = false;
						boolean isSMS = false;
						eRingtoneType ringtoneType = (eRingtoneType) object;
						switch (ringtoneType) {
						case PHONE:
							isRingtone = true;
							break;
						case SMS:
							isSMS = true;
							break;
						default:
							break;
						}

						String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + track.localUrl;
						Utility.setDefaultRingtone(track.title, filePath, track.originalFormat, track.singerName,
						        track.duration, mActivity, isRingtone, isSMS);
					}
				};

				ringtoneDialog.show(mActivity.getSupportFragmentManager(), "setRingtone");
			}
		};
	}
}
