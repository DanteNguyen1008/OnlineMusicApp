//package com.bigcatfamily.myringtones;
//
//import java.util.HashMap;
//import java.util.Stack;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.ImageButton;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.actionbarsherlock.app.ActionBar;
//import com.bigcatfamily.myringtones.base.SlidingBaseFragmentActivity;
//import com.bigcatfamily.myringtones.contranst.AppConstant;
//import com.bigcatfamily.myringtones.fragment.DownloadableTrackListFragment;
//import com.bigcatfamily.myringtones.fragment.MyTracksFragment;
//import com.bigcatfamily.myringtones.fragment.SearchTrackFragment;
//import com.bigcatfamily.myringtones.fragment.SlideMenuFragment;
//import com.bigcatfamily.myringtones.fragment.StreamTrackListFragment;
//import com.bigcatfamily.myringtones.utility.Utility;
//import com.bigcatfamily.myringtones.widget.ActionExpandImageButton;
//import com.bigcatfamily.myringtones.widget.ActionExpandTextButton;
//import com.bigcatfamily.myringtones.widget.UiUtil;
//import com.slidingmenu.lib.SlidingMenu;
//import com.slidingmenu.lib.SlidingMenu.OnOpenListener;
//
//public class BackUpOfMainActivity extends SlidingBaseFragmentActivity {
//
//	private static final String TAG = SplashActivity.class.getSimpleName();
//	private HashMap<String, Stack<Fragment>> mStacks;
//	private Fragment mFrag;
//	private RelativeLayout headerView;
//	private ImageButton btHome;
//	private String currentMenu;
//	private TextView tvTitle;
//	private View mObject = null;
//	private ProgressDialog mLoadingDialog;
//
//	private FragmentManager mFragmentManager;
//	private SlidingMenu sm;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setBehindContentView(R.layout.main_left_menu);
//		mFragmentManager = getSupportFragmentManager();
//		if (savedInstanceState == null) {
//			FragmentTransaction t = mFragmentManager.beginTransaction();
//			mFrag = new SlideMenuFragment();
//			t.replace(R.id.menu_frame, mFrag);
//			t.commit();
//		} else
//			mFrag = (SlideMenuFragment) mFragmentManager.findFragmentById(R.id.menu_frame);
//
//		Utility.createFolder(getString(R.string.app_name));
//
//		// customize the SlidingMenu
//		sm = getSlidingMenu();
//		sm.setSlidingEnabled(false);
//		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
//		sm.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);
//		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
//		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//		sm.setFadeDegree(0.35f);
//		initActionbar();
//
//		sm.setOnOpenListener(new OnOpenListener() {
//			@Override
//			public void onOpen() {
//			}
//		});
//
//		setContentView(R.layout.activity_main);
//
//		mStacks = new HashMap<String, Stack<Fragment>>();
//		mStacks.put(AppConstant.SEARCH_LIST_MENU, new Stack<Fragment>());
//		mStacks.put(AppConstant.STREAMABLE_LIST_MENU, new Stack<Fragment>());
//		mStacks.put(AppConstant.DOWNLOADABLE_LIST_MENU, new Stack<Fragment>());
//		mStacks.put(AppConstant.MY_TRACK_LIST_MENU, new Stack<Fragment>());
//
//		mLoadingDialog = new ProgressDialog(this);
//		mLoadingDialog.setCancelable(false);
//		mLoadingDialog.setMessage(getString(R.string.loading));
//
//		Bundle bundle = getIntent().getExtras();
//		currentMenu = AppConstant.STREAMABLE_LIST_MENU;
//
//		if (bundle != null) {
//			currentMenu = bundle.getString(AppConstant.INTENT_AL_SCREEEN_NAME_EXTRA);
//		}
//
//		if (currentMenu != null) {
//			if (currentMenu.equals(AppConstant.STREAMABLE_LIST_MENU))
//				pushFragments(new StreamTrackListFragment(), false, true);
//			else if (currentMenu.equals(AppConstant.DOWNLOADABLE_LIST_MENU))
//				pushFragments(new DownloadableTrackListFragment(), false, true);
//			else if (currentMenu.equals(AppConstant.MY_TRACK_LIST_MENU))
//				pushFragments(new MyTracksFragment(), false, true);
//			else if (currentMenu.equals(AppConstant.SEARCH_LIST_MENU))
//				pushFragments(new SearchTrackFragment(), false, true);
//		}
//
//	}
//
//	/**
//	 * init the actionbar from the actionbar.xml
//	 */
//	private void initActionbar() {
//		final ActionBar ab = getSupportActionBar();
//		ab.setDisplayShowHomeEnabled(false);
//		ab.setDisplayShowTitleEnabled(false);
//
//		final LayoutInflater inflater = (LayoutInflater) getSystemService("layout_inflater");
//		headerView = (RelativeLayout) inflater.inflate(R.layout.header_sherlock, null);
//		headerView.bringToFront();
//		btHome = (ImageButton) headerView.findViewById(R.id.header_home);
//		btHome.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				hideKeyboard();
//				sm.toggle();
//			}
//		});
//		tvTitle = (TextView) headerView.findViewById(R.id.header_title);
//		headerView.findViewById(R.id.header_title);
//
//		ab.setCustomView(headerView);
//		ab.setDisplayShowCustomEnabled(true);
//	}
//
//	@Override
//	public String GetActivityID() {
//		return null;
//	}
//
//	@Override
//	protected void SetNullForCustomVariable() {
//
//	}
//
//	@Override
//	protected void SetContentView() {
//
//	}
//
//	@Override
//	public void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		Bundle bundle = intent.getExtras();
//		if (bundle != null) {
//			currentMenu = bundle.getString(AppConstant.INTENT_AL_SCREEEN_NAME_EXTRA);
//			if (currentMenu != null) {
//				if (currentMenu.equals(AppConstant.STREAMABLE_LIST_MENU))
//					pushFragments(new StreamTrackListFragment(), false, true);
//				else if (currentMenu.equals(AppConstant.DOWNLOADABLE_LIST_MENU))
//					pushFragments(new DownloadableTrackListFragment(), false, true);
//				else if (currentMenu.equals(AppConstant.MY_TRACK_LIST_MENU))
//					pushFragments(new MyTracksFragment(), false, true);
//				else if (currentMenu.equals(AppConstant.SEARCH_LIST_MENU))
//					pushFragments(new SearchTrackFragment(), false, true);
//			}
//		}
//	}
//
//	public void pushFragments(Fragment fragment, boolean shouldAnimate, boolean shouldAdd) {
//		pushFragments(currentMenu, fragment, shouldAnimate, shouldAdd);
//	}
//
//	private void pushFragments(String tag, Fragment fragment, boolean shouldAnimate, boolean shouldAdd) {
//		// popTutorialFragments();
//		if (shouldAdd)
//			mStacks.get(tag).push(fragment);
//		FragmentManager manager = getSupportFragmentManager();
//		FragmentTransaction ft = manager.beginTransaction();
//		if (shouldAnimate)
//			ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//		ft.replace(R.id.content_frame, fragment);
//		ft.commit();
//
//		removeActionExpandButton();
//	}
//
//	public void popFragments() {
//		Fragment fragment = mStacks.get(currentMenu).elementAt(mStacks.get(currentMenu).size() - 2);
//
//		mStacks.get(currentMenu).pop();
//		FragmentManager manager = getSupportFragmentManager();
//		FragmentTransaction ft = manager.beginTransaction();
//		ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
//		ft.replace(R.id.content_frame, fragment);
//		ft.commit();
//
//		removeActionExpandButton();
//	}
//
//	public void changeCurrentMenu(String menuName) {
//		sm.showContent();
//		if (!menuName.equals(currentMenu)) {
//			currentMenu = menuName;
//			getSlidingMenu().showContent();
//
//			if (mStacks.get(menuName).size() == 0) {
//				/*
//				 * We are adding a new fragment which is not present in stack.
//				 */
//				if (menuName.equals(AppConstant.STREAMABLE_LIST_MENU)) {
//					pushFragments(menuName, new StreamTrackListFragment(), false, true);
//				} else if (menuName.equals(AppConstant.DOWNLOADABLE_LIST_MENU)) {
//					pushFragments(menuName, new DownloadableTrackListFragment(), false, true);
//				} else if (menuName.equals(AppConstant.MY_TRACK_LIST_MENU)) {
//					pushFragments(menuName, new MyTracksFragment(), false, true);
//				} else if (menuName.equals(AppConstant.SEARCH_LIST_MENU)) {
//					pushFragments(menuName, new SearchTrackFragment(), false, true);
//				}
//			} else {
//				/*
//				 * We are switching tabs, and target tab is already has atleast
//				 * one fragment. No need of animation, no need of stack pushing.
//				 * Just show the target fragment
//				 */
//				// pushFragments(menuName, mStacks.get(menuName).lastElement(),
//				// false, false);
//				Fragment fragment = mStacks.get(menuName).firstElement();
//				mStacks.get(menuName).clear();
//				mStacks.get(menuName).add(fragment);
//				pushFragments(menuName, mStacks.get(menuName).firstElement(), false, false);
//			}
//		}
//	}
//
//	public void setTitle(String strTitle) {
//		tvTitle.setText(strTitle);
//	}
//
//	@Override
//	public void onBackPressed() {
//		if (mStacks.get(currentMenu).size() > 1) {
//			popFragments();
//		} else {
//			onexitNotify();
//		}
//	}
//
//	public void removeActionExpandButton() {
//		if (mObject != null) {
//			mObject.setOnClickListener(null);
//			headerView.removeView(mObject);
//			mObject = null;
//		}
//	}
//
//	public void addActionExpandButton(ActionExpandImageButton button) {
//		if (mObject != null) {
//			mObject.setOnClickListener(null);
//			headerView.removeView(mObject);
//			mObject = null;
//		}
//
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//		        LayoutParams.WRAP_CONTENT);
//		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		params.addRule(RelativeLayout.CENTER_VERTICAL);
//		headerView.addView((View) button, params);
//		mObject = button;
//	}
//
//	public void addActionExpandButton(ActionExpandTextButton button) {
//		if (mObject != null) {
//			mObject.setOnClickListener(null);
//			headerView.removeView(mObject);
//			mObject = null;
//		}
//
//		if (headerView != null) {
//			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
//			        UiUtil.dipToPixels(this, 30));
//			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//			params.addRule(RelativeLayout.CENTER_VERTICAL);
//			// params.leftMargin = UiUtil.dipToPixels(this, 4);
//			// params.rightMargin = UiUtil.dipToPixels(this, 4);
//			headerView.addView((View) button, params);
//			mObject = button;
//		}
//	}
//
//	public void ShowLoadingDialog() {
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				if (!mLoadingDialog.isShowing())
//					mLoadingDialog.show();
//			}
//		});
//	}
//
//	public void CloseLoadingDialog() {
//		runOnUiThread(new Runnable() {
//			@Override
//			public void run() {
//				if (mLoadingDialog.isShowing())
//					mLoadingDialog.cancel();
//			}
//		});
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//	}
//
//}
