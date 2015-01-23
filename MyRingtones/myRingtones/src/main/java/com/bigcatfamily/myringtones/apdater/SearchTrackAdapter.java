package com.bigcatfamily.myringtones.apdater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigcatfamily.myringtones.OnItemClickListener;
import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.utility.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by annguyenquocduy on 1/19/15.
 */


public class SearchTrackAdapter extends RecyclerView.Adapter<SearchTrackAdapter.ViewHolder> {

    private ArrayList<Track> mDataList;
    protected int mIAvatarCircleWidth;
    protected int mPlayInTrackPosition;
    private OnItemClickListener mOnItemClickListener;

    public SearchTrackAdapter(Context context) {
        mIAvatarCircleWidth = (int) context.getResources().getDimension(R.dimen.avatar_radian);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Track track = mDataList.get(position);

        ImageLoader.getInstance().displayImage(track.artWorkUrl, holder.imgPhoto,
                Utility.getDisplayImageOptionWithRoundedImage(mIAvatarCircleWidth));

        holder.txtSingerName.setText(track.singerName);
        holder.txtSongName.setText(track.title);
        holder.txtPlayCount.setText(track.playBackCount + "");
        holder.txtTime.setText(Utility.convertLongToMinutes("%d:%d", track.duration));

        if (track.isDownloadable) {
            if (holder.txtDownloadable.getVisibility() == View.GONE)
                holder.txtDownloadable.setVisibility(View.VISIBLE);
            if (holder.txtStreamable.getVisibility() == View.VISIBLE)
                holder.txtStreamable.setVisibility(View.GONE);
        } else {
            if (holder.txtStreamable.getVisibility() == View.GONE)
                holder.txtStreamable.setVisibility(View.VISIBLE);
            if (holder.txtDownloadable.getVisibility() == View.VISIBLE)
                holder.txtDownloadable.setVisibility(View.GONE);
        }

        holder.rlFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataList == null)
            return 0;

        return mDataList.size();
    }

    public Track getNextTrack() {
        if (mPlayInTrackPosition + 1 < mDataList.size()) {
            mPlayInTrackPosition++;
            return mDataList.get(mPlayInTrackPosition);
        }

        return null;
    }

    public Track getPrevTrack() {
        if (mPlayInTrackPosition > 0) {
            mPlayInTrackPosition--;
            return mDataList.get(mPlayInTrackPosition);
        }

        return null;
    }

    public void notifyDataSetChanged(ArrayList<Track> tracks) {
        mDataList = tracks;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgPhoto;
        TextView txtSingerName, txtSongName, txtPlayCount, txtTime, btnDownload;
        TextView txtStreamable, txtDownloadable, txtDownloaded;
        RelativeLayout rlFront;

        public ViewHolder(View view) {
            super(view);

            imgPhoto = (ImageView) view.findViewById(R.id.track_photo);
            txtSingerName = (TextView) view.findViewById(R.id.txt_singer_name);
            txtSongName = (TextView) view.findViewById(R.id.txt_song_name);
            txtPlayCount = (TextView) view.findViewById(R.id.txt_play_back_count);
            txtTime = (TextView) view.findViewById(R.id.txt_song_time);
            btnDownload = (TextView) view.findViewById(R.id.btn_download);

            rlFront = (RelativeLayout) view.findViewById(R.id.sub_front);

            txtStreamable = (TextView) view.findViewById(R.id.txt_streamable);
            txtDownloadable = (TextView) view.findViewById(R.id.txt_downloadable);
            txtDownloaded = (TextView) view.findViewById(R.id.txt_downloaded);
        }
    }
}
