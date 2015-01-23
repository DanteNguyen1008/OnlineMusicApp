package com.example.androiddownloadmanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.concurrent.RejectedExecutionException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

public class AndroidDownloader extends Observable {
	@SuppressWarnings("unused")
	private static final String TAG = AndroidDownloader.class.getSimpleName();

	public static final int MESSAGE_DOWNLOAD_STARTED = 1000;
	public static final int MESSAGE_DOWNLOAD_COMPLETE = 1001;
	public static final int MESSAGE_UPDATE_PROGRESS_BAR = 1002;
	public static final int MESSAGE_DOWNLOAD_CANCELED = 1003;
	public static final int MESSAGE_CONNECTING_STARTED = 1004;
	public static final int MESSAGE_ENCOUNTERED_ERROR = 1005;
	public static final int DOWNLOAD_BUFFER_SIZE = 11 * 1024;

	private IDownloadListener mDownloadListener;
	private String mFileName, mDownloadUrl;
	private Context context;

	private AndroidDownloader(Context context, IDownloadListener downloadListener, String downloadUrl, String fileName) {
		this.mFileName = fileName;
		this.mDownloadUrl = downloadUrl;
		this.mDownloadListener = downloadListener;
		this.context = context;
	}

	public static class AndroidDownloaderBulder {

		private IDownloadListener downloadListener;
		private String mUrl, mFileName;
		private Context context;

		public AndroidDownloaderBulder() {
		}

		public AndroidDownloaderBulder setDownloadListener(IDownloadListener downloadListener) {
			this.downloadListener = downloadListener;
			return this;
		}

		public AndroidDownloaderBulder setDownloadUrl(String downloadUrl) {
			this.mUrl = downloadUrl;
			return this;
		}

		public AndroidDownloaderBulder setFileName(String fileName) {
			this.mFileName = fileName;
			return this;
		}

		public AndroidDownloaderBulder setContext(Context context) {
			this.context = context;
			return this;
		}

		public AndroidDownloader build() {
			return new AndroidDownloader(context, downloadListener, mUrl, mFileName);
		}
	}

	class AndroidDownloaderAsync extends AsyncTask<Void, Void, File> {

		URL url;
		HttpURLConnection conn;
		String downloadUrl;
		String fileName;
		Context context;

		public AndroidDownloaderAsync(Context context, String fileName, String downloadUrl) {
			this.fileName = fileName;
			this.downloadUrl = downloadUrl;
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected File doInBackground(Void... params) {
			File f = null;
			float fileSize;
			InputStream input = null;
			OutputStream output = null;

			// we're going to connect now
			mDownloadListener.onDownloadConnecting();

			try {
				url = new URL(downloadUrl);
				conn = (HttpURLConnection) url.openConnection();
				conn.setUseCaches(false);
				conn.connect();

				if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					if (conn.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
						String redirectLocation = conn.getHeaderField("Location");
						if (redirectLocation != null && !redirectLocation.equals("")) {
							new AndroidDownloaderAsync(context, this.fileName, redirectLocation).run();
							if (conn != null)
								conn.disconnect();
							return f;
						}

					} else {
						if (mDownloadListener != null)
							mDownloadListener.onDownloadError(conn.getResponseCode(), conn.getResponseMessage());
					}
				} else {
					fileSize = conn.getContentLength();

					// notify download start
					if (mDownloadListener != null)
						mDownloadListener.onDownloadStarted(fileSize / 1024);

					// this will be useful to display download percentage
					// might be -1: server did not report the length

					// download the file
					input = conn.getInputStream();
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
						f = new File(context.getExternalFilesDir(null), fileName);
						f.createNewFile();
					} else {
						File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
						f = new File(path, fileName);
						path.mkdirs();
					}
					output = new FileOutputStream(f);
					byte data[] = new byte[4096];
					long total = 0;
					int count;
					long lastTime = System.currentTimeMillis();
					while ((count = input.read(data)) != -1) {
						// allow canceling with back button
						if (isCancelled()) {
							input.close();
							return f;
						}
						total += count;
						// publishing the progress....
						if (fileSize > 0 && (System.currentTimeMillis() - lastTime) >= 1000) {
							if (mDownloadListener != null)
								mDownloadListener.onDownLoadGoing((int) (total * 100 / fileSize), fileSize / 1024);
							lastTime = System.currentTimeMillis();
						}

						output.write(data, 0, count);
					}
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
				if (mDownloadListener != null)
					mDownloadListener.onDownloadError(0, e.getMessage());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				if (mDownloadListener != null)
					mDownloadListener.onDownloadError(0, e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				if (mDownloadListener != null)
					mDownloadListener.onDownloadError(0, e.getMessage());
			} finally {
				try {
					if (output != null)
						output.close();
					if (input != null)
						input.close();
				} catch (IOException ignored) {
				}

				if (conn != null)
					conn.disconnect();
			}

			return f;
		}

		@Override
		protected void onPostExecute(File f) {
			super.onPostExecute(f);
			if (f != null && mDownloadListener != null)
				mDownloadListener.onDownloadFinished(f);
		}

		@SuppressLint({ "InlinedApi", "NewApi" })
		public void run() {
			try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
				} else {
					execute((Void) null, (Void) null);
				}
			} catch (RejectedExecutionException ex) {
				ex.printStackTrace();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void run() {
		/* for testing */
		// new AndroidDownloaderAsync(mFileName,
		// "https://api.soundcloud.com/tracks/68861878/download?client_id=cd7760bb233646df5105e99b06a97dca",
		// context).run();
		if ((mFileName != null && !mFileName.equals("")) && (mDownloadUrl != null && !mDownloadUrl.equals("")))
			new AndroidDownloaderAsync(context, mFileName, mDownloadUrl).run();
	}
}
