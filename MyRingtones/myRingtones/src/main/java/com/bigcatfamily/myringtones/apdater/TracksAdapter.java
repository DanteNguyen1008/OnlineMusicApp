package com.bigcatfamily.myringtones.apdater;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.utility.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TracksAdapter extends BaseAdapter {

	private ArrayList<Track> dataList;
	protected Context context;
	protected LayoutInflater mInflater;
	protected iTrackAdapterActionHandle mActionHandler;
	protected int mIAvatarCircleWidth;
	protected int mPlayInTrackPosition;

	public TracksAdapter(ArrayList<Track> dataList, Context context, iTrackAdapterActionHandle actionHandler) {
		super();
		this.dataList = dataList;
		this.context = context;
		mInflater = LayoutInflater.from(this.context);
		this.mActionHandler = actionHandler;
		mIAvatarCircleWidth = (int) this.context.getResources().getDimension(R.dimen.avatar_radian);

	}

	@Override
	public int getCount() {
		if (dataList == null)
			return 0;

		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		if (dataList == null)
			return null;

		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();

			view = (View) mInflater.inflate(R.layout.item_track, null);
			holder.imgPhoto = (ImageView) view.findViewById(R.id.track_photo);
			holder.txtSingerName = (TextView) view.findViewById(R.id.txt_singer_name);
			holder.txtSongName = (TextView) view.findViewById(R.id.txt_song_name);
			holder.txtPlayCount = (TextView) view.findViewById(R.id.txt_play_back_count);
			holder.txtTime = (TextView) view.findViewById(R.id.txt_song_time);
			holder.btnDownload = (TextView) view.findViewById(R.id.btn_download);

			holder.rlFront = (RelativeLayout) view.findViewById(R.id.sub_front);

			holder.txtStreamable = (TextView) view.findViewById(R.id.txt_streamable);
			holder.txtDownloadable = (TextView) view.findViewById(R.id.txt_downloadable);
			holder.txtDownloaded = (TextView) view.findViewById(R.id.txt_downloaded);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		final Track track = (Track) getItem(position);
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

		holder.btnDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mActionHandler != null)
					mActionHandler.OnDownloadClick(track);
				mPlayInTrackPosition = position;
			}
		});

		holder.rlFront.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mActionHandler != null)
					mActionHandler.OnTrackClick(track);
				mPlayInTrackPosition = position;
			}
		});

		return view;
	}

	public Track getNextTrack() {
		if (mPlayInTrackPosition + 1 < dataList.size()) {
			mPlayInTrackPosition++;
			return (Track) getItem(mPlayInTrackPosition);
		}

		return null;
	}

	public Track getPrevTrack() {
		if (mPlayInTrackPosition > 0) {
			mPlayInTrackPosition--;
			return (Track) getItem(mPlayInTrackPosition);
		}

		return null;
	}

	public void notifyDataSetChanged(ArrayList<Track> tracks) {
		dataList = tracks;
		notifyDataSetChanged();
	}

	public interface iTrackAdapterActionHandle {
		public void OnTrackClick(Track track);

		public void OnDownloadClick(Track track);

		public void OnRemoveClick(Track track);

		public void OnSetRingtone(Track track);
	}

	public class ViewHolder {
		ImageView imgPhoto;
		TextView txtSingerName, txtSongName, txtPlayCount, txtTime, btnDownload, btnRemove, btnSetRingtone;
		TextView txtStreamable, txtDownloadable, txtDownloaded;
		RelativeLayout rlFront;
	}
}
