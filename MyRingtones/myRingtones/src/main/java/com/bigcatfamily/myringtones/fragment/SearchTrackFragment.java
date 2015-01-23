package com.bigcatfamily.myringtones.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.networkhandler.DataHelper;
import com.bigcatfamily.myringtones.networkhandler.ParseData;
import com.bigcatfamily.myringtones.utility.Utility;

import java.io.UnsupportedEncodingException;

public class SearchTrackFragment extends DownloadableTrackListFragment {

	private String mStrSearchContent;
	private EditText mEdtSearchBox;
	private Button mBtnSearch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = super.onCreateView(inflater, container, savedInstanceState);
		mActivity.setTitle(getString(R.string.title_search_track_list));

		mEdtSearchBox = (EditText) mRootView.findViewById(R.id.txt_search_content);
		mBtnSearch = (Button) mRootView.findViewById(R.id.btn_search);

		mBtnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/* start search */
				if (mEdtSearchBox.getText() != null && !Utility.IsEmpty(mEdtSearchBox.getText().toString())) {
					mStrSearchContent = mEdtSearchBox.getText().toString().trim();
					OnRequestData();
					isFirstTimeLoad = true;
					mProgressBarLoading.setVisibility(View.VISIBLE);
//					mListView.setVisibility(View.GONE);
					mPlayList = null;
				}
			}
		});

		return mRootView;
	}

	@Override
	protected View setUpRootView(LayoutInflater inflater, ViewGroup container) {
		return (View) inflater.inflate(R.layout.fragment_search_track_list, container, false);
	}

//	@Override
//	protected OnLastItemVisibleListener setLastItemVisible() {
//		return new OnLastItemVisibleListener() {
//
//			@Override
//			public void onLastItemVisible() {
//				if (mLoadMoreProcessBar != null && !m_bIsLoadMore) {
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
	protected void OnRequestData() {
        try {
            DataHelper.getInstance(mActivity, ParseData.getInstance(mActivity)).getSearchStrackList(TRACK_LIMIT, offset,
                    mStrSearchContent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
