package com.bigcatfamily.myringtones;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnCloseListener;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigcatfamily.myringtones.base.BaseActivity;
import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.fragment.SCTrackListFragment;
import com.bigcatfamily.myringtones.fragment.SlideMenuFragment;
import com.bigcatfamily.myringtones.fragment.TopHitFragment;
import com.bigcatfamily.myringtones.utility.Utility;

import java.util.HashMap;
import java.util.Stack;

public class MainActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private HashMap<String, Stack<Fragment>> mStacks;
    private Fragment mFrag;
    private RelativeLayout headerView;
    private ImageButton btHome;
    private String currentMenu;
    private TextView tvTitle;
    private View mObject = null;
    private ProgressDialog mLoadingDialog;

    private FragmentManager mFragmentManager;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private FrameLayout mMenuLayout;
    private String mTitle;
    private SearchView mSearchView;

    // private SlidingMenu sm;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            FragmentTransaction t = mFragmentManager.beginTransaction();
            mFrag = new SlideMenuFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        } else
            mFrag = (SlideMenuFragment) mFragmentManager.findFragmentById(R.id.menu_frame);

        Utility.createFolder(getString(R.string.app_name));

        initActionbar();

        mStacks = new HashMap<String, Stack<Fragment>>();
        mStacks.put(AppConstant.ITUNE_TOP_HIT_MENU, new Stack<Fragment>());
        mStacks.put(AppConstant.SEARCH_TRACK_MENU, new Stack<Fragment>());
//        mStacks.put(AppConstant.STREAMABLE_LIST_MENU, new Stack<Fragment>());
//        mStacks.put(AppConstant.DOWNLOADABLE_LIST_MENU, new Stack<Fragment>());
//        mStacks.put(AppConstant.MY_TRACK_LIST_MENU, new Stack<Fragment>());

        mLoadingDialog = new ProgressDialog(this);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setMessage(getString(R.string.loading));

        Bundle bundle = getIntent().getExtras();
        currentMenu = AppConstant.ITUNE_TOP_HIT_MENU;

        if (bundle != null) {
            currentMenu = bundle.getString(AppConstant.INTENT_AL_SCREEEN_NAME_EXTRA);
        }

        if (currentMenu != null) {
//            if (currentMenu.equals(AppConstant.STREAMABLE_LIST_MENU))
//                pushFragments(new StreamTrackListFragment(), false, true);
//            else if (currentMenu.equals(AppConstant.DOWNLOADABLE_LIST_MENU))
//                pushFragments(new DownloadableTrackListFragment(), false, true);
//            else if (currentMenu.equals(AppConstant.MY_TRACK_LIST_MENU))
//                pushFragments(new MyTracksFragment(), false, true);
//            else if (currentMenu.equals(AppConstant.SEARCH_LIST_MENU))
//                pushFragments(new SearchTrackFragment(), false, true);
//            else

            if (currentMenu.equals(AppConstant.ITUNE_TOP_HIT_MENU))
                pushFragments(new TopHitFragment(), false, true);
            else if (currentMenu.equals(AppConstant.SEARCH_TRACK_MENU))
                pushFragments(new SCTrackListFragment(), false, true);
        }

    }

    /**
     * init the actionbar from the actionbar.xml
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void initActionbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mMenuLayout = (FrameLayout) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_close, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            @SuppressLint("NewApi")
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            @SuppressLint("NewApi")
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.closeDrawer(mMenuLayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        // getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setIcon(R.drawable.icon_music_avatar);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        mSearchView = (SearchView) searchItem.getActionView();

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String newText) {
                Log.d(TAG, "on query text Submit " + newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "on query text change " + newText);
                return false;
            }
        });

        mSearchView.setOnCloseListener(new OnCloseListener() {

            @Override
            public boolean onClose() {
                return false;
            }
        });

        if (currentMenu != null) {
//			if (currentMenu.equals(AppConstant.STREAMABLE_LIST_MENU))
//				searchItem.setVisible(false);
//			else if (currentMenu.equals(AppConstant.DOWNLOADABLE_LIST_MENU))
//				searchItem.setVisible(false);
//			else if (currentMenu.equals(AppConstant.MY_TRACK_LIST_MENU))
//				searchItem.setVisible(false);
//			else if (currentMenu.equals(AppConstant.SEARCH_LIST_MENU))
//				searchItem.setVisible(true);
//			else

            if (currentMenu.equals(AppConstant.ITUNE_TOP_HIT_MENU))
                searchItem.setVisible(false);
            else if (currentMenu.equals(AppConstant.ITUNE_TOP_HIT_MENU))
                searchItem.setVisible(true);
        }

        return true;
    }

    private void showHideSearchIcon(boolean isVisible, MenuItem searchItem) {
        if (mSearchView != null)
            mSearchView.setVisibility(View.GONE);
        if (currentMenu != null) {
            if (currentMenu.equals(AppConstant.SEARCH_TRACK_MENU)) {
                searchItem.setVisible(isVisible);
                if (mSearchView != null) {
                    if (!isVisible)
                        mSearchView.setVisibility(View.GONE);
                    else
                        mSearchView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        // view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mMenuLayout);
        // menu.findItem(R.id.search).setVisible(!drawerOpen);
        showHideSearchIcon(!drawerOpen, menu.findItem(R.id.search));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    public void setTitle(String title) {
        mTitle = title;
        getSupportActionBar().setTitle(title);
    }

    @Override
    public String GetActivityID() {
        return null;
    }

    @Override
    protected void SetNullForCustomVariable() {

    }

    @Override
    protected void SetContentView() {

    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            currentMenu = bundle.getString(AppConstant.INTENT_AL_SCREEEN_NAME_EXTRA);
            if (currentMenu != null) {
//				if (currentMenu.equals(AppConstant.STREAMABLE_LIST_MENU))
//					pushFragments(new StreamTrackListFragment(), false, true);
//				else if (currentMenu.equals(AppConstant.DOWNLOADABLE_LIST_MENU))
//					pushFragments(new DownloadableTrackListFragment(), false, true);
//				else if (currentMenu.equals(AppConstant.MY_TRACK_LIST_MENU))
//					pushFragments(new MyTracksFragment(), false, true);
//				else if (currentMenu.equals(AppConstant.SEARCH_LIST_MENU))
//					pushFragments(new SearchTrackFragment(), false, true);
//				else

                if (currentMenu.equals(AppConstant.ITUNE_TOP_HIT_MENU))
                    pushFragments(new TopHitFragment(), false, true);
                else if (currentMenu.equals(AppConstant.SEARCH_TRACK_MENU))
                    pushFragments(new SCTrackListFragment(), false, true);
            }
        }
    }

    public void pushFragments(Fragment fragment, boolean shouldAnimate, boolean shouldAdd) {
        pushFragments(currentMenu, fragment, shouldAnimate, shouldAdd);
    }

    private void pushFragments(String tag, Fragment fragment, boolean shouldAnimate, boolean shouldAdd) {
        // popTutorialFragments();
        if (shouldAdd)
            mStacks.get(tag).push(fragment);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate)
            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        // removeActionExpandButton();
    }

    public void popFragments() {
        Fragment fragment = mStacks.get(currentMenu).elementAt(mStacks.get(currentMenu).size() - 2);

        mStacks.get(currentMenu).pop();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        // removeActionExpandButton();
    }

    public void changeCurrentMenu(String menuName) {
        if (!menuName.equals(currentMenu)) {
            mDrawerLayout.closeDrawer(mMenuLayout);
            currentMenu = menuName;
            if (mStacks.get(menuName).size() == 0) {
                /*
				 * We are adding a new fragment which is not present in stack.
				 */
//				if (menuName.equals(AppConstant.STREAMABLE_LIST_MENU)) {
//					pushFragments(menuName, new StreamTrackListFragment(), false, true);
//				} else if (menuName.equals(AppConstant.DOWNLOADABLE_LIST_MENU)) {
//					pushFragments(menuName, new DownloadableTrackListFragment(), false, true);
//				} else if (menuName.equals(AppConstant.MY_TRACK_LIST_MENU)) {
//					pushFragments(menuName, new MyTracksFragment(), false, true);
//				} else if (menuName.equals(AppConstant.SEARCH_LIST_MENU)) {
//					pushFragments(menuName, new SearchTrackFragment(), false, true);
//				} else if (currentMenu.equals(AppConstant.ITUNE_TOP_HIT_MENU))
//					pushFragments(new TopHitFragment(), false, true);

                if (currentMenu.equals(AppConstant.ITUNE_TOP_HIT_MENU))
                    pushFragments(new TopHitFragment(), false, true);
                else if (currentMenu.equals(AppConstant.SEARCH_TRACK_MENU))
                    pushFragments(new SCTrackListFragment(), false, true);
            } else {
				/*
				 * We are switching tabs, and target tab is already has atleast
				 * one fragment. No need of animation, no need of stack pushing.
				 * Just show the target fragment
				 */
                // pushFragments(menuName, mStacks.get(menuName).lastElement(),
                // false, false);
                Fragment fragment = mStacks.get(menuName).firstElement();
                mStacks.get(menuName).clear();
                mStacks.get(menuName).add(fragment);
                pushFragments(menuName, mStacks.get(menuName).firstElement(), false, false);
            }
        }
    }

    // public void setTitle(String strTitle) {
    // tvTitle.setText(strTitle);
    // }

    @Override
    public void onBackPressed() {
        if (mStacks.get(currentMenu).size() > 1) {
            popFragments();
        } else {
            onexitNotify();
        }
    }

    // public void removeActionExpandButton() {
    // if (mObject != null) {
    // mObject.setOnClickListener(null);
    // headerView.removeView(mObject);
    // mObject = null;
    // }
    // }
    //
    // public void addActionExpandButton(ActionExpandImageButton button) {
    // if (mObject != null) {
    // mObject.setOnClickListener(null);
    // headerView.removeView(mObject);
    // mObject = null;
    // }
    //
    // RelativeLayout.LayoutParams params = new
    // RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
    // LayoutParams.WRAP_CONTENT);
    // params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    // params.addRule(RelativeLayout.CENTER_VERTICAL);
    // headerView.addView((View) button, params);
    // mObject = button;
    // }
    //
    // public void addActionExpandButton(ActionExpandTextButton button) {
    // if (mObject != null) {
    // mObject.setOnClickListener(null);
    // headerView.removeView(mObject);
    // mObject = null;
    // }
    //
    // if (headerView != null) {
    // RelativeLayout.LayoutParams params = new
    // RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
    // UiUtil.dipToPixels(this, 30));
    // params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    // params.addRule(RelativeLayout.CENTER_VERTICAL);
    // // params.leftMargin = UiUtil.dipToPixels(this, 4);
    // // params.rightMargin = UiUtil.dipToPixels(this, 4);
    // headerView.addView((View) button, params);
    // mObject = button;
    // }
    // }

    // @SuppressLint("NewApi")
    // public void setSearchViewVisible(boolean isVisible) {
    // if (mSearchView != null)
    // if (isVisible) {
    // mSearchView.setVisibility(View.VISIBLE);
    // } else {
    // mSearchView.setVisibility(View.GONE);
    // }
    //
    // if(mMenu != null) {
    // mMenu.findItem(R.id.search).setVisible(isVisible);
    // invalidateOptionsMenu();
    // }
    // }

    public void ShowLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!mLoadingDialog.isShowing())
                    mLoadingDialog.show();
            }
        });
    }

    public void CloseLoadingDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mLoadingDialog.isShowing())
                    mLoadingDialog.cancel();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
