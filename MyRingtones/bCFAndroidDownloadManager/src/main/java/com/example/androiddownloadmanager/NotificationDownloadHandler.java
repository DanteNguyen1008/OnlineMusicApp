package com.example.androiddownloadmanager;

import java.io.File;
import java.util.Random;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

public class NotificationDownloadHandler extends BaseDownloaderHandler {
	private static final int MAX_RANDOM = 999999;
	private static final int MIN_RANDOM = 111111;
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	private int mNotifyId;
	private String mTitle;
	private INotificationDownloadListener mListener;
	private Object[] backDatas;

	public NotificationDownloadHandler(Context context, String title, int icon, Object[] backData,
	        INotificationDownloadListener listener) {
		this.mTitle = title;
		mNotifyId = autoGenerateNotifyId();
		mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder = new NotificationCompat.Builder(context);
		mBuilder.setContentTitle(mTitle).setSmallIcon(icon);
		this.mListener = listener;
		this.backDatas = backData;
	}

	@Override
	public void onDownloadConnecting() {
		mBuilder.setContentText("Download is connecting");
		mNotifyManager.notify(mNotifyId, mBuilder.build());
	}

	@Override
	public void onDownloadStarted(float fileSizeInKB) {
		mBuilder.setContentText("Download is started with file size ");
		mNotifyManager.notify(mNotifyId, mBuilder.build());
	}

	@Override
	public void onDownLoadGoing(int progressPercent, float fileSize) {
		mBuilder.setContentText("Download in progress " + progressPercent + "% / file size "
		        + String.format("%.2f", (fileSize / 1024)) + "mb");
		mNotifyManager.notify(mNotifyId, mBuilder.build());
	}

	@Override
	public void onDownloadCancel() {

	}

	@Override
	public void onDownloadError(int errorCode, String errorMsg) {

	}

	private int autoGenerateNotifyId() {
		Random rand = new Random();
		int randomNum = rand.nextInt((MAX_RANDOM - MIN_RANDOM) + 1) + MIN_RANDOM;
		return randomNum;
	}

	@Override
    public void onDownloadFinished(File f) {
		Log.d("BCF Downloader", "Download is finished");
		mBuilder.setContentText("Download is finished");
		mNotifyManager.notify(mNotifyId, mBuilder.build());
		if (mListener != null)
			mListener.onDowloadFinished(f, backDatas);
    }
}
