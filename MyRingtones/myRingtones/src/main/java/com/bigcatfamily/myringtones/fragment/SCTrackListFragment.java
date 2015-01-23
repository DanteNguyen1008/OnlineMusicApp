package com.bigcatfamily.myringtones.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigcatfamily.mp3player.BCFMediaPlayer;
import com.bigcatfamily.mp3player.IMediaListener;
import com.bigcatfamily.myringtones.OnItemClickListener;
import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.apdater.SearchTrackAdapter;
import com.bigcatfamily.myringtones.base.BaseFragment;
import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.model.PlayList;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.networkhandler.DataHelper;
import com.bigcatfamily.myringtones.networkhandler.MethodParams;
import com.bigcatfamily.myringtones.networkhandler.ParseData;
import com.bigcatfamily.myringtones.networkhandler.Urls;
import com.bigcatfamily.myringtones.utility.BCFAnimation;
import com.bigcatfamily.myringtones.utility.Utility;
import com.bigcatfamily.networkhandler.db.DataResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.UnsupportedEncodingException;

/**
 * Created by annguyenquocduy on 1/19/15.
 */


public class SCTrackListFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG = "SCTrackList";
    protected PlayList mPlayList;

    private SearchTrackAdapter mAdapter;
    private RecyclerView mListView;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private String m_sSearchContent;
    protected int offset = 0;

    protected BCFMediaPlayer mMediaPlayer;
    protected ImageView mImgPlayTrackBG, mBtnClosePlayTrack;
    protected TextView mTxtPlayingTrackName, mTxtPlayTrackNameHint;
    protected RelativeLayout mRlPlayTrackHint, mRlPlayTrack;


    private int mCurrentPage;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

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

    //private int preLast;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_itune_track_list, container, false);
        mActivity.setTitle(getString(R.string.title_search_track_list));

        if (getArguments() != null)
            m_sSearchContent = getArguments().getString(AppConstant.BUNDLE_EXTRA_KEY_SEARCH_CONTENT);

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

        mAdapter = new SearchTrackAdapter(mActivity);

        mListView = (RecyclerView) mRootView.findViewById(R.id.listview_tracks);

        mLayoutManager = new LinearLayoutManager(mActivity);
        mListView.setLayoutManager(mLayoutManager);
        mListView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                boolean enable = false;
                if (mLayoutManager != null && mLayoutManager.getChildCount() > 0) {
                    // check if the first item of the list is visible
                    boolean firstItemVisible = mLayoutManager.findFirstVisibleItemPosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = mLayoutManager.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                mRefreshLayout.setEnabled(enable);

                if (loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = false;
                        getSearchTrack();
                        mCurrentPage++;
                    }
                }
            }
        });

        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_container);

        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mCurrentPage = 0;
                getSearchTrack();
            }
        });


        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                showPlayTrack();
                playTrack(mPlayList.tracks.get(position));
            }
        });


        mListView.setAdapter(mAdapter);

        getSearchTrack();

        return mRootView;
    }

    private void getSearchTrack() {
        try {
            DataHelper.getInstance(mActivity, ParseData.getInstance(mActivity))
                    .getSearchStrackList(AppConstant.SEARCH_TRACH_LIMIT,
                            this.offset, m_sSearchContent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(DataResult result) {
        switch (((MethodParams) result.getMethodName()).getName()) {
            case GET_SEARCH_TRACK_LIST:
                Log.d(TAG, "Received Data");
                PlayList newPlayList = (PlayList) result.getData();
                if (newPlayList != null) {
                    newPlayList.offset = this.offset;
                    newPlayList.limit = AppConstant.SEARCH_TRACH_LIMIT;
                    if (mPlayList == null)
                        mPlayList = newPlayList;
                    else
                        mPlayList.mergePlayLists(newPlayList);
                }

                mAdapter.notifyDataSetChanged(mPlayList.tracks);

                loading = false;

                mRefreshLayout.setRefreshing(false);

                break;

            default:
                break;
        }
    }

    private void playTrack(Track track) {
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

    @Override
    public void ClearInstance() {

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
            animation.setInterpolator(new OvershootInterpolator(1.f));
            mRlPlayTrack.startAnimation(animation);
        }
    }

    protected void hidePlayTrack() {
        if (mRlPlayTrack.getVisibility() == View.VISIBLE) {
            Animation animation = BCFAnimation.GetInstance().AnimateOutTo(BCFAnimation.BOTTOM);
            animation.setAnimationListener(new Animation.AnimationListener() {

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
}
