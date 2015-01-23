package com.bigcatfamily.myringtones.apdater;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.model.Track;
import com.bigcatfamily.myringtones.utility.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MyTracksAdapter extends TracksAdapter {

	public MyTracksAdapter(ArrayList<Track> dataList, Context context, iTrackAdapterActionHandle actionHandler) {
		super(dataList, context, actionHandler);
	}

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
			holder.btnRemove = (TextView) view.findViewById(R.id.btn_delete_track);
			holder.btnSetRingtone = (TextView) view.findViewById(R.id.btn_set_ringtone);

			holder.btnDownload.setVisibility(View.GONE);
			holder.btnSetRingtone.setVisibility(View.VISIBLE);
			holder.btnRemove.setVisibility(View.VISIBLE);

			holder.rlFront = (RelativeLayout) view.findViewById(R.id.sub_front);

			holder.txtStreamable = (TextView) view.findViewById(R.id.txt_streamable);
			holder.txtDownloadable = (TextView) view.findViewById(R.id.txt_downloadable);
			holder.txtDownloaded = (TextView) view.findViewById(R.id.txt_downloaded);

			holder.txtDownloadable.setVisibility(View.GONE);
			holder.txtStreamable.setVisibility(View.GONE);
			holder.txtDownloaded.setVisibility(View.VISIBLE);

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

		holder.btnRemove.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mActionHandler != null)
					mActionHandler.OnRemoveClick(track);
			}
		});

		holder.btnSetRingtone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mActionHandler != null)
					mActionHandler.OnSetRingtone(track);
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
}
