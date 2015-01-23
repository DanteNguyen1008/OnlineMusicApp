package com.bigcatfamily.myringtones.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.networkhandler.DataHelper;
import com.bigcatfamily.myringtones.networkhandler.ParseData;
import com.bigcatfamily.myringtones.networkhandler.Urls;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public class StreamTrackListFragment extends TrackListFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = super.onCreateView(inflater, container, savedInstanceState);
		mActivity.setTitle(getString(R.string.title_track_list));

		return mRootView;
	}
	
	@Override
	protected void OnRequestData() {
		try {
			((DataHelper) DataHelper.getInstance(mActivity, ParseData.getInstance(mActivity))).getSoundList(TRACK_LIMIT, offset,
			        Urls.PLAY_LIST_STREAM_ID);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

//	@Override
//	public SwipeListviewManager getSwipeOption() {
//		SwipeListviewManager option = SwipeListviewManager.getInstance();
////		option.setSwipeMode(SwipeListView.SWIPE_MODE_NONE);
//		return option;
//	}
}
