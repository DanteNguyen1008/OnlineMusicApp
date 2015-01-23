package com.example.androiddownloadmanager;

import java.io.File;

public interface INotificationDownloadListener {
	public void onDowloadFinished(File f,Object[] data);
}
