/*
 * NLBaseActivity
 *
 * Abstract class for inheritant activities. Ex: Splash, Login, SignUp,...
 *
 * @author 
 * @date 
 * @lastChangedRevision: 1.0
 * @lastChangedDate: 2011/08/12
 */
package com.bigcatfamily.myringtones.base;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.utility.Utility;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public abstract class BaseActivity extends ActionBarActivity {
	private Toast toast;
	private long lastBackPressTime = 0;
	private boolean isVisible;

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
		SetContentView();
	}

	@Override
	protected void onPause() {
		super.onPause();
		isVisible = false;
	}

	protected void onResume() {
		super.onResume();
		isVisible = true;
	}

	@Override
	protected void onStart() {
		super.onStart();

		// FlurryAgent.onStartSession(this, "7BP39YN4VC6JCZ7FZ4BT");
		// ActivityUtil.SaveOpenActivities(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		// FlurryAgent.onEndSession(this);
	}

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// System.out.println("==> " + GetActivityID());
	// SherlockFragmentActivity dd = this;
	// if (dd instanceof MainActivity) {
	// MainActivity mainActivity = (MainActivity)dd;
	// if(mainActivity.checkBackToExit()){
	// onexitNotify();
	// }else{
	// return super.onKeyDown(keyCode, event);
	// }
	// return true;
	// }
	// else
	// return super.onKeyDown(keyCode, event);
	// } else if (keyCode == KeyEvent.KEYCODE_HOME) {
	// finish();
	// return true;
	// }
	//
	// return super.onKeyDown(keyCode, event);
	// }

	/**
	 * Prevent accidental app exit by requiring users to press back twice when
	 * the app is exiting w/in 8sec
	 */
	public void onexitNotify() {
		if (this.lastBackPressTime < System.currentTimeMillis() - 1000) {
			toast = Toast.makeText(this, getString(R.string.str_press_back_exit), Toast.LENGTH_LONG);
			toast.show();
			this.lastBackPressTime = System.currentTimeMillis();
		} else {
			if (toast != null) {
				toast.cancel();
			}
			super.onBackPressed();
		}
	}

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent event) {
	// View view = getCurrentFocus();
	// boolean ret = super.dispatchTouchEvent(event);
	//
	// if (view instanceof EditText) {
	// View w = getCurrentFocus();
	// int scrcoords[] = new int[2];
	// w.getLocationOnScreen(scrcoords);
	// float x = event.getRawX() + w.getLeft() - scrcoords[0];
	// float y = event.getRawY() + w.getTop() - scrcoords[1];
	//
	// if (event.getAction() == MotionEvent.ACTION_UP
	// && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y >
	// w.getBottom())) {
	// InputMethodManager imm = (InputMethodManager)
	// getSystemService(Context.INPUT_METHOD_SERVICE);
	// imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(),
	// 0);
	// }
	// }
	// return ret;
	// }

	@Override
	protected void onDestroy() {
		// ActivityUtil.RemoveOpenActivities(this);
		isVisible = false;
		MyGarbageCollection();
		super.onDestroy();
	}

	public boolean isActivityVisible() {
		return isVisible;
	}

	public Context getContext() {
		return BaseActivity.this;
	}

	// protected int GetRootViewId() {
	// return R.id.rootview;
	// }

	public void hideKeyboard() {
		System.out.println("==> hideKeyboard");
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null && getCurrentFocus() != null)
				// imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,
				// InputMethodManager.RESULT_UNCHANGED_SHOWN);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
				        InputMethodManager.RESULT_UNCHANGED_SHOWN);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void ShowArletDialog(final ArletDialog nlDialog) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
		if (!Utility.IsEmpty(nlDialog.getTitle())) {
			dialog.setTitle(nlDialog.getTitle());
		}
		dialog.setMessage(nlDialog.getMessage());
		if (!Utility.IsEmpty(nlDialog.getOKString())) {
			dialog.setPositiveButton(nlDialog.getOKString(), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					if (nlDialog.GetINLDialogActionListener() != null) {
						nlDialog.GetINLDialogActionListener().OnOKButtonClick();
					}
				}
			});
		}

		if (!Utility.IsEmpty(nlDialog.getCancelString())) {
			dialog.setNegativeButton(nlDialog.getCancelString(), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					if (nlDialog.GetINLDialogActionListener() != null) {
						nlDialog.GetINLDialogActionListener().OnCancelButtonClick();
					}
				}
			});
		}

		Dialog _dialog = dialog.create();
		_dialog.setCanceledOnTouchOutside(false);
		_dialog.setCancelable(false);
		_dialog.show();
	}

	public void MyGarbageCollection() {
		try {
			SetNullForCustomVariable();
			// UnbindReferences(findViewById(GetRootViewId()));
		} catch (Exception e) {
			Log.e("ERROR", "Unbind References" + e.toString());
		}
	}

	@SuppressWarnings("unused")
	private void UnbindReferences(View view) {
		try {
			if (view != null) {
				UnbindReferences(view);
				if (view instanceof ViewGroup)
					UnbindViewGroupReferences((ViewGroup) view);
			}
			System.gc();
		} catch (Throwable e) {
			// whatever exception is thrown just ignore it because a crash is
			// always worse than this method not doing what it's supposed to do
		}
	}

	private void UnbindViewGroupReferences(ViewGroup viewGroup) {
		if (viewGroup == null)
			return;

		int nrOfChildren = viewGroup.getChildCount();
		for (int i = 0; i < nrOfChildren; i++) {
			View view = viewGroup.getChildAt(i);
			UnbindViewReferences(view);
			if (view instanceof ViewGroup)
				UnbindViewGroupReferences((ViewGroup) view);
		}
		try {
			viewGroup.removeAllViews();
		} catch (Throwable mayHappen) {
			// AdapterViews, ListViews and potentially other ViewGroups don't
			// support the removeAllViews operation
		}
	}
	
	@SuppressWarnings("deprecation")
	private void UnbindViewReferences(View view) {
		if (view == null)
			return;
		// set all listeners to null (not every view and not every API level
		// supports the methods)
		try {
			view.setOnClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnCreateContextMenuListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnFocusChangeListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnKeyListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnLongClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;

		// set background to null
		Drawable d = view.getBackground();
		if (d != null)
			d.setCallback(null);
		if (view instanceof ImageView) {
			ImageView imageView = (ImageView) view;
			d = imageView.getDrawable();
			if (d != null)
				d.setCallback(null);
			imageView.setImageDrawable(null);
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
				imageView.setBackgroundDrawable(null);
			} else {
				imageView.setBackground(null);
			}
		}

		// destroy webview
		if (view instanceof WebView) {
			((WebView) view).destroyDrawingCache();
			((WebView) view).destroy();
		}
	}

	public abstract void onBackPressed();

	public abstract String GetActivityID();

	protected abstract void SetNullForCustomVariable();

	protected abstract void SetContentView();
}
