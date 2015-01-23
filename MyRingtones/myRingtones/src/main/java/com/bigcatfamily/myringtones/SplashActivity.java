package com.bigcatfamily.myringtones;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.base.BaseActivity;

public class SplashActivity extends BaseActivity {
	ProgressDialog progress;
	Initilized loadingThread;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		if (!AppManager.getInstance(SplashActivity.this).IsFirstInitial()) {
			progress = ProgressDialog.show(this, "", getContext().getString(R.string.first_time_loading_msg), true);
			loadingThread = new Initilized();
			loadingThread.execute();
		} else
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					goToMainActivity();
				}
			}, 3000);

	}

	private void goToMainActivity() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onDestroy() {
		if (loadingThread != null) {
			loadingThread.cancel(true);
			loadingThread = null;
		}

		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		// don't do anything in splash activity
	}

	@Override
	public String GetActivityID() {
		return null;
	}

	private class Initilized extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					progress.dismiss();
					goToMainActivity();
				}
			});

			return null;
		}

		@SuppressLint("NewApi")
		public void execute() {
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
			} else {
				execute((Void) null);
			}
		}

	}

	@Override
	protected void SetNullForCustomVariable() {

	}

	@Override
	protected void SetContentView() {

	}
}
