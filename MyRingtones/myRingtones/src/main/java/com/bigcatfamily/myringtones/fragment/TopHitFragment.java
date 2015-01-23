package com.bigcatfamily.myringtones.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigcatfamily.myringtones.OnItemClickListener;
import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.apdater.ItuneTrackAdapter;
import com.bigcatfamily.myringtones.base.BaseFragment;
import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.model.ItuneTrack;
import com.bigcatfamily.myringtones.networkhandler.DataHelper;
import com.bigcatfamily.myringtones.networkhandler.MethodParams;
import com.bigcatfamily.myringtones.networkhandler.ParseData;
import com.bigcatfamily.networkhandler.db.DataResult;

import java.util.ArrayList;

public class TopHitFragment extends BaseFragment {
	private static final String TAG = TopHitFragment.class.getSimpleName();
	private ArrayList<ItuneTrack> mDataList;
	private ItuneTrackAdapter mAdapter;
	private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLayoutManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.fragment_itune_track_list, container, false);
		mActivity.setTitle(getString(R.string.title_tophit_tracks_list));

		mAdapter = new ItuneTrackAdapter(mDataList, mActivity);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                /*move to search on SC*/
                if(position >= 0) {
                    ItuneTrack track = mDataList.get(position);

                    SCTrackListFragment nextFragment = new SCTrackListFragment();
                    Bundle args = new Bundle();
                    args.putString(AppConstant.BUNDLE_EXTRA_KEY_SEARCH_CONTENT, track.name);

                    nextFragment.setArguments(args);

                    mActivity.pushFragments(nextFragment, true, true);
                }
            }
        });

		mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.listview_tracks);
        mLayoutManager = new LinearLayoutManager(mActivity);
		mRecyclerView.setLayoutManager(mLayoutManager);

		mRecyclerView.setAdapter(mAdapter);

        requestTopHitData();

        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_container);

        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                requestTopHitData();
            }
        });

        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                boolean enable = false;
                if(mLayoutManager != null && mLayoutManager.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mLayoutManager.findFirstVisibleItemPosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mLayoutManager.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mRefreshLayout.setEnabled(enable);

            }
        });


        return mRootView;
	}

    private void requestTopHitData() {
        DataHelper.getInstance(mActivity, ParseData.getInstance(mActivity)).getItuneTopHit(
                AppConstant.ITUNE_TOP_HIT_LIMIT);
    }

	@SuppressWarnings("unchecked")
	@Override
	public void update(DataResult result) {
		switch (((MethodParams) result.getMethodName()).getName()) {
		case GET_ITUNE_TOP_HIT:
			Log.d(TAG, "Received Data");
			mDataList = (ArrayList<ItuneTrack>) result.getData();
			mAdapter.notifyDataSetChanged(mDataList);
			break;

		default:
			break;
		}
	}

	@Override
	public void ClearInstance() {
		if (mDataList != null) {
			mDataList.clear();
			mDataList = null;
		}
	}

}
