package com.example.androiddownloadmanager;

import java.io.File;

public interface IDownloadListener {
	public void onDownloadConnecting();

	public void onDownloadStarted(float fileSizeInKB);

	public void onDownLoadGoing(int progressPercent, float fileSize);

	public void onDownloadFinished(File f);

	public void onDownloadCancel();

	public void onDownloadError(int errorCode, String errorMsg);
}
