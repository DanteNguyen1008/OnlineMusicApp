package com.bigcatfamily.myringtones.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;
import java.util.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;

import com.bigcatfamily.myringtones.MainActivity;
import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.networkhandler.DataHelper;
import com.bigcatfamily.myringtones.networkhandler.ParseData;
import com.bigcatfamily.networkhandler.Util;
import com.bigcatfamily.networkhandler.db.DataResult;

public abstract class BaseFragment extends Fragment implements Observer {
	public View mRootView = null;
	public MainActivity mActivity;
	public FragmentManager mFragmentManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (MainActivity) getActivity();
		mFragmentManager = getFragmentManager();
	}

	/**
	 * Detach from view.
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (getView() != null) {
			System.out.println("==> Unbinding drawbale to free memory from fragment");
			// unbindDrawables(getView());
			// ViewUnbindHelper.unbindReferences(mRootView);
			System.gc();
			System.out.println("==> Memory used - printMemInfo : ");
			printMemInfo();
		}
		System.out.println("==> " + getClassName(getClass()) + " destroyView");
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void onResume() {
		super.onResume();
		DataHelper.getInstance(mActivity, ParseData.getInstance(mActivity)).addObserver(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		DataHelper.getInstance(mActivity, ParseData.getInstance(mActivity)).deleteObserver(this);
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ClearInstance();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void printMemInfo() {
		System.out.println("==> Max heap size = " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
		System.out.println("==> Allocate heap size = " + android.os.Debug.getNativeHeapAllocatedSize() / 1024 + "K");

		Method _readProclines = null;
		try {
			Class procClass;
			procClass = Class.forName("android.os.Process");
			Class parameterTypes[] = new Class[] { String.class, String[].class, long[].class };
			_readProclines = procClass.getMethod("readProcLines", parameterTypes);
			Object arglist[] = new Object[3];
			final String[] mMemInfoFields = new String[] { "MemTotal:", "MemFree:", "Buffers:", "Cached:" };
			long[] mMemInfoSizes = new long[mMemInfoFields.length];
			mMemInfoSizes[0] = 30;
			mMemInfoSizes[1] = -30;
			arglist[0] = new String("/proc/meminfo");
			arglist[1] = mMemInfoFields;
			arglist[2] = mMemInfoSizes;
			if (_readProclines != null) {
				_readProclines.invoke(null, arglist);
				for (int i = 0; i < mMemInfoSizes.length; i++) {
					System.out.println("==> " + mMemInfoFields[i] + " = " + mMemInfoSizes[i] / 1024 + "M");
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	}

	// returns the class (without the package if any)
	@SuppressWarnings("rawtypes")
	public static String getClassName(Class c) {
		String FQClassName = c.getName();
		int firstChar;
		firstChar = FQClassName.lastIndexOf('.') + 1;
		if (firstChar > 0) {
			FQClassName = FQClassName.substring(firstChar);
		}
		return FQClassName;
	}

	public boolean onBackPressed() {
		return false;
	}

	public ActionBar getActionBar() {
		return mActivity.getSupportActionBar();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		Fragment fragment = (Fragment) getChildFragmentManager().findFragmentById(R.id.content_frame);
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, intent);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (data instanceof DataResult) {
			DataResult result = (DataResult) data;
			if (Util.IsEmpty(result.getErrorMessage())) {
				update(result);
			} else {
				/* general error handle place */
				Log.d(getTag(), result.getErrorMessage());
			}
		}
	}

	public abstract void update(DataResult result);

	public abstract void ClearInstance();
}
