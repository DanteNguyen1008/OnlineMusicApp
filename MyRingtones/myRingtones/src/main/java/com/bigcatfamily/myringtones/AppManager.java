package com.bigcatfamily.myringtones;

import com.bigcatfamily.myringtones.contranst.AppConstant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppManager {
	public static final String APP_MANAGER_PREFEREENCES = AppConstant.NAMESPACE + ".app_manager";
	public static final String INITIAL_CHECKING = AppConstant.NAMESPACE + ".application.";

	private static AppManager _instance = null;
	private Context m_context;

	private AppManager(Context context) {
		this.m_context = context;
	}

	public static AppManager getInstance(Context context) {
		if (_instance == null)
			_instance = new AppManager(context);
		return _instance;
	}

	public boolean IsFirstInitial() {
		SharedPreferences prefs = m_context.getSharedPreferences(APP_MANAGER_PREFEREENCES, Context.MODE_PRIVATE);
		if (prefs != null) {
			return prefs.getBoolean(INITIAL_CHECKING, false);
		}
		return false;
	}

	public void firstInitial() {
		SharedPreferences prefs = m_context.getSharedPreferences(APP_MANAGER_PREFEREENCES, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putBoolean(INITIAL_CHECKING, true);
		editor.commit();
		editor.apply();
	}
}
