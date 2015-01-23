package com.bigcatfamily.myringtones.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bigcatfamily.myringtones.MainActivity;
import com.bigcatfamily.myringtones.R;
import com.bigcatfamily.myringtones.base.BaseFragment;
import com.bigcatfamily.myringtones.contranst.AppConstant;
import com.bigcatfamily.myringtones.widget.MenuItem;
import com.bigcatfamily.networkhandler.db.DataResult;

@SuppressLint("ValidFragment")
public class SlideMenuFragment extends BaseFragment implements OnClickListener {
	@SuppressWarnings("unused")
	private int selectedMenu = AppConstant.ITUNE_TOP_HIT_MENU_INDEX;
	private MainActivity mActivity;

	public MenuItem menuSearchTrack, menuItuneTopHits;

	public SlideMenuFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_slide_menu, null);
//		menuStream = ((MenuItem) view.findViewById(R.id.mnu_streamable));
//		menuDownload = ((MenuItem) view.findViewById(R.id.mnu_downloadable));
//		menuMyTrack = ((MenuItem) view.findViewById(R.id.mnu_my_tracks));
		menuSearchTrack = ((MenuItem) view.findViewById(R.id.mnu_search));
		menuItuneTopHits = ((MenuItem) view.findViewById(R.id.mnu_itune_top_hits));

//		menuStream.setOnClickListener(this);
//		menuDownload.setOnClickListener(this);
//		menuMyTrack.setOnClickListener(this);
		menuSearchTrack.setOnClickListener(this);
		menuItuneTopHits.setOnClickListener(this);

		selectedMenu = AppConstant.ITUNE_TOP_HIT_MENU_INDEX;
        menuItuneTopHits.setCurrentMenu(true, 0);

		return view;
	}

	@Override
	public void onClick(View v) {
		mActivity.hideKeyboard();

//		if (v.equals(menuStream)) {
//			clearFocusMenu();
//			menuStream.setCurrentMenu(true, 0);
//			changeCurrentMenu(AppConstant.STREAMABLE_LIST_MENU);
//		} else if (v.equals(menuDownload)) {
//			clearFocusMenu();
//			menuDownload.setCurrentMenu(true, 0);
//			changeCurrentMenu(AppConstant.DOWNLOADABLE_LIST_MENU);
//		} else if (v.equals(menuMyTrack)) {
//			clearFocusMenu();
//			menuMyTrack.setCurrentMenu(true, 0);
//			changeCurrentMenu(AppConstant.MY_TRACK_LIST_MENU);
//		}

        if (v.equals(menuItuneTopHits)) {
			clearFocusMenu();
			menuSearchTrack.setCurrentMenu(true, 0);
			changeCurrentMenu(AppConstant.ITUNE_TOP_HIT_MENU);
		}else if (v.equals(menuSearchTrack)) {
            clearFocusMenu();
            menuSearchTrack.setCurrentMenu(true, 0);
            changeCurrentMenu(AppConstant.SEARCH_TRACK_MENU);
        }
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = (MainActivity) this.getActivity();
	}

	/**
	 * User Change the current focus menu. We call changeCurrentMenu() function
	 * of MainActivity
	 * 
	 * @param menuName
	 */
	private void changeCurrentMenu(String menuName) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity activity = (MainActivity) getActivity();
			activity.changeCurrentMenu(menuName);
		}
	}

	public void clearFocusMenu() {
//		menuStream.setCurrentMenu(false, 0);
//		menuDownload.setCurrentMenu(false, 0);
//		menuMyTrack.setCurrentMenu(false, 0);
		menuSearchTrack.setCurrentMenu(false, 0);
		menuItuneTopHits.setCurrentMenu(false, 0);

	}

	@SuppressWarnings("unused")
	private void expand() {
		// set Visible
		// mPanel.setVisibility(View.VISIBLE);
		//
		// final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
		// View.MeasureSpec.UNSPECIFIED);
		// mPanel.measure(widthSpec, heightSpec);
		//
		// ValueAnimator mAnimator = slideAnimator(0,
		// mPanel.getMeasuredHeight());
		// mAnimator.start();
	}

	@SuppressWarnings("unused")
	private void collapse() {
		// int finalHeight = mPanel.getHeight();
		//
		// ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
		// mAnimator.addListener(new AnimatorListener() {
		// @Override
		// public void onAnimationEnd(Animator animator) {
		// // Height = 0, but it set visibility to GONE
		// mPanel.setVisibility(View.GONE);
		// }
		//
		// @Override
		// public void onAnimationCancel(Animator animation) {
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animator animation) {
		// }
		//
		// @Override
		// public void onAnimationStart(Animator animation) {
		// }
		// });
		// mAnimator.start();
	}

	@SuppressWarnings("unused")
	private ValueAnimator slideAnimator(int start, int end) {
		// ValueAnimator animator = ValueAnimator.ofInt(start, end);
		// animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
		// {
		// @Override
		// public void onAnimationUpdate(ValueAnimator valueAnimator) {
		// // Update Height
		// int value = (Integer) valueAnimator.getAnimatedValue();
		// ViewGroup.LayoutParams layoutParams = mPanel.getLayoutParams();
		// layoutParams.height = value;
		// mPanel.setLayoutParams(layoutParams);
		// }
		// });
		// return animator;

		return null;
	}

	public void updateItemProfile() {
		// mUser = DHApplication.getInstance().getUserProfile();
		// menuProfile.setTextMenu(mUser.getFullName());
		// final ImageView iv = menuProfile.getIconMenu();
		// if (mUser.getAvatar_url() != null) {
		// // UrlImageViewHelper.setUrlDrawable(iv, mUser.getAvatar_url() +
		// // "?type=large", new UrlImageViewCallback() {
		// // @Override
		// // public void onLoaded(ImageView imageView, Bitmap loadedBitmap,
		// // String url, boolean loadedFromCache) {
		// // imageView.setScaleType(ScaleType.FIT_CENTER);
		// //
		// imageView.setImageBitmap(ImageHelper.getRoundedCornerBitmap(loadedBitmap,
		// // 8));
		// // }
		// // });
		// }
	}

	@Override
	public void ClearInstance() {

	}

	@Override
	public void update(DataResult result) {

	}

	// public void updatePushNotify(ManageNotification pushNotify) {
	// menuNotification.setBadgeCount(pushNotify.getUnread_notifications());
	// menuMessage.setBadgeCount(pushNotify.getUnread_messages());
	// }
}
