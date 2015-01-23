package com.bigcatfamily.myringtones.apdater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigcatfamily.myringtones.OnItemClickListener;
import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.model.ItuneTrack;
import com.bigcatfamily.myringtones.utility.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class ItuneTrackAdapter extends RecyclerView.Adapter<ItuneTrackAdapter.ViewHolder> {

	private ArrayList<ItuneTrack> dataList;
	private Context mContext;
	protected LayoutInflater mInflater;
	protected int mIAvatarCircleWidth;
    private OnItemClickListener mOnItemClickListener;

	public ItuneTrackAdapter(ArrayList<ItuneTrack> dataList, Context mContext) {
		this.dataList = dataList;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(this.mContext);
		mIAvatarCircleWidth = (int) this.mContext.getResources().getDimension(R.dimen.avatar_radian);
	}

	public void notifyDataSetChanged(ArrayList<ItuneTrack> tracks) {
		dataList = tracks;
		notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ImageView photo;
		public TextView title, singer;
        public View parentView;

		public ViewHolder(View view) {
			super(view);

			photo = (ImageView) view.findViewById(R.id.track_photo);
			title = (TextView) view.findViewById(R.id.txt_song_name);
			singer = (TextView) view.findViewById(R.id.txt_singer_name);

            parentView = view.findViewById(R.id.track_parent);
		}

	}

	@Override
	public int getItemCount() {
		if (dataList != null)
			return dataList.size();
		return 0;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		ItuneTrack track = dataList.get(position);
		ImageLoader.getInstance().displayImage(track.imageLinks[2], holder.photo,
		        Utility.getDisplayImageOptionWithRoundedImage(mIAvatarCircleWidth));
		holder.singer.setText(track.singer);
		holder.title.setText(track.title);

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.OnItemClick(position);
            }
        });
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_itune_track, parent, false);
		return new ViewHolder(v);
	}

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
