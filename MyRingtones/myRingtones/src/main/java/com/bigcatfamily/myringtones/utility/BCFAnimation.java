package com.bigcatfamily.myringtones.utility;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

public class BCFAnimation {
	final public static int LEFT = 0;// from left
	final public static int RIGHT = LEFT + 1;// from right
	final public static int TOP = RIGHT + 1;// from top
	final public static int BOTTOM = TOP + 1;// from bottom
	// use for alpha animation
	final public static int APPEAR = BOTTOM + 1;
	final public static int DISAPPEAR = APPEAR + 1;

	final public static int RELATIVE_PARENT = Animation.RELATIVE_TO_PARENT;
	final public static int RELATIVE_SELF = Animation.RELATIVE_TO_SELF;

	final public static long DURATION = 300;
	final public static long ALPHA_DURATION = 1000;// 1 second

	private static BCFAnimation m_instance;

	public static BCFAnimation GetInstance() {
		if (m_instance == null) {
			m_instance = new BCFAnimation();
		}

		return m_instance;
	}

	public Animation AnimateInFrom(int toDirection) {
		return AnimateInFrom(toDirection, DURATION);
	}

	public Animation AnimateInFrom(int direction, long duration) {
		return AnimateInFrom(direction, RELATIVE_PARENT, duration);
	}

	public Animation AnimateInFrom(int direction, int relative, long duration) {
		Animation inFrom = null;

		switch (direction) {
		case LEFT:
			inFrom = new TranslateAnimation(relative, -1.0f, relative, 0.0f, relative, 0.0f, relative, 0.0f);
			break;
		case RIGHT:
			inFrom = new TranslateAnimation(relative, +1.0f, relative, 0.0f, relative, 0.0f, relative, 0.0f);
			break;
		case TOP:
			inFrom = new TranslateAnimation(relative, 0.0f, relative, 0.0f, relative, -1.0f, relative, 0.0f);
			break;
		case BOTTOM:
			inFrom = new TranslateAnimation(relative, 0.0f, relative, 0.0f, relative, +1.0f, relative, 0.0f);
			break;
		}

		inFrom.setDuration(duration);
		inFrom.setInterpolator(new AccelerateInterpolator());
		return inFrom;
	}

	public Animation AnimateOutTo(int toDirection) {
		return AnimateOutTo(toDirection, DURATION);
	}

	public Animation AnimateOutTo(int toDirection, long duration) {
		return AnimateOutTo(toDirection, RELATIVE_PARENT, DURATION);
	}

	public Animation AnimateOutTo(int toDirection, int relative, long duration) {
		Animation outTo = null;

		switch (toDirection) {
		case LEFT:
			outTo = new TranslateAnimation(relative, 0.0f, relative, -1.0f, relative, 0.0f, relative, 0.0f);
			break;
		case RIGHT:
			outTo = new TranslateAnimation(relative, 0.0f, relative, +1.0f, relative, 0.0f, relative, 0.0f);
			break;
		case TOP:
			outTo = new TranslateAnimation(relative, 0.0f, relative, 0.0f, relative, 0.0f, relative, -1.0f);
			break;
		case BOTTOM:
			outTo = new TranslateAnimation(relative, 0.0f, relative, 0.0f, relative, 0.0f, relative, +1.0f);
			break;
		}

		outTo.setDuration(duration);
		outTo.setInterpolator(new AccelerateInterpolator());
		return outTo;
	}

	public Animation AppearAnimation(int alpha) {
		return AppearAnimation(alpha, ALPHA_DURATION);
	}

	public Animation AppearAnimation(int alpha, long duration) {
		Animation alphaAnim = null;

		if (alpha == APPEAR) {
			alphaAnim = new AlphaAnimation(0.0f, 1.0f);
		} else if (alpha == DISAPPEAR) {
			alphaAnim = new AlphaAnimation(1.0f, 0.0f);
		}
		alphaAnim.setDuration(duration);

		return alphaAnim;
	}

	public Animation ScaleFromEdge(float fromXScale, float toXScale, float edgeX, float fromYScale, float toYScale,
	        float edgeY, long duration, long startOffset, boolean repeatIndefinitely, boolean repeatReverse) {

		Animation scale = null;

		scale = new ScaleAnimation(fromXScale, toXScale, fromYScale, toYScale, edgeX, edgeY);
		scale.setStartOffset(startOffset);

		if (repeatIndefinitely)
			scale.setRepeatCount(Animation.INFINITE);
		if (repeatReverse)
			scale.setRepeatMode(Animation.REVERSE);

		scale.setInterpolator(new LinearInterpolator());
		scale.setDuration(duration);
		scale.setFillEnabled(true);

		return scale;
	}

	public Animation expandHorizontal(final View v, final boolean expand, int duration) {

		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
			m.setAccessible(true);
			m.invoke(v, MeasureSpec.makeMeasureSpec(((View) v.getParent()).getMeasuredWidth(), MeasureSpec.AT_MOST),
			        MeasureSpec.makeMeasureSpec(((View) v.getParent()).getMeasuredHeight(), MeasureSpec.AT_MOST));
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int initialWidth = v.getMeasuredWidth();

		if (expand) {
			v.getLayoutParams().width = 0;
		} else {
			v.getLayoutParams().width = initialWidth;
		}

		v.setVisibility(View.VISIBLE);

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				int newWidth = 0;
				if (expand) {
					newWidth = (int) (initialWidth * interpolatedTime);
				} else {
					newWidth = (int) (initialWidth * (1 - interpolatedTime));
				}
				v.getLayoutParams().width = newWidth;
				v.requestLayout();

				if (interpolatedTime == 1 && !expand)
					v.setVisibility(View.GONE);
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(duration);
		return a;
	}

	public Animation expandVertical(final View v, final boolean expand, int duration) {
		try {
			Method m = v.getClass().getDeclaredMethod("onMeasure", int.class, int.class);
			m.setAccessible(true);
			m.invoke(v, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
			        MeasureSpec.makeMeasureSpec(((View) v.getParent()).getMeasuredWidth(), MeasureSpec.AT_MOST));
		} catch (Exception e) {
			e.printStackTrace();
		}

		final int initialHeight = v.getMeasuredHeight();

		if (expand) {
			v.getLayoutParams().height = 0;
		} else {
			v.getLayoutParams().height = initialHeight;
		}

		v.setVisibility(View.VISIBLE);

		Animation a = new Animation() {
			@Override
			protected void applyTransformation(float interpolatedTime, Transformation t) {
				int newHeight = 0;
				if (expand) {
					newHeight = (int) (initialHeight * interpolatedTime);
				} else {
					newHeight = (int) (initialHeight * (1 - interpolatedTime));
				}
				v.getLayoutParams().height = newHeight;
				v.requestLayout();

				if (interpolatedTime == 1 && !expand)
					v.setVisibility(View.GONE);
			}

			@Override
			public boolean willChangeBounds() {
				return true;
			}
		};
		a.setDuration(duration);
		return a;
	}

	public Animation ScaleFromCenter(float fromXScale, float toXScale, float percentX, float fromYScale,
	        float toYScale, float percentY, long duration, long startOffset, boolean repeatIndefinitely,
	        boolean repeatReverse) {

		Animation scale = null;

		scale = new ScaleAnimation(fromXScale, toXScale, fromYScale, toYScale, Animation.RELATIVE_TO_SELF, percentX,
		        Animation.RELATIVE_TO_SELF, percentY);
		scale.setStartOffset(startOffset);

		if (repeatIndefinitely)
			scale.setRepeatCount(Animation.INFINITE);
		if (repeatReverse)
			scale.setRepeatMode(Animation.REVERSE);

		scale.setInterpolator(new LinearInterpolator());
		scale.setDuration(duration);
		scale.setFillEnabled(true);

		return scale;
	}

	public Animation RotateWithDistance(float fromDegrees, float toDegrees, float distX, float distY, long duration,
	        boolean repeatIndefinitely, boolean repeatReverse) {
		Animation rotate = null;

		rotate = new RotateAnimation(fromDegrees, toDegrees, distX, distY);

		if (repeatIndefinitely)
			rotate.setRepeatCount(Animation.INFINITE);

		if (repeatReverse)
			rotate.setRepeatMode(Animation.REVERSE);

		rotate.setInterpolator(new LinearInterpolator());
		rotate.setDuration(duration);
		rotate.setFillEnabled(true);

		return rotate;
	}

	public Animation RotateWithPercentObject(float fromDegrees, float toDegrees, float percentX, float percentY,
	        long duration, boolean repeatIndefinitely, boolean repeatReverse) {

		Animation rotate = null;

		rotate = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, percentX,
		        Animation.RELATIVE_TO_SELF, percentY);

		if (repeatIndefinitely)
			rotate.setRepeatCount(Animation.INFINITE);

		if (repeatReverse)
			rotate.setRepeatMode(Animation.REVERSE);

		rotate.setInterpolator(new LinearInterpolator());
		rotate.setDuration(duration);
		rotate.setFillEnabled(true);

		return rotate;
	}

	/**
	 * Create AnimationDrawable from an drawable resource id with duration
	 * 
	 * @param context
	 * @param drawableID
	 *            An drawable resource ID
	 * @param duration
	 *            Duration for animation(in ms)
	 * @param onceShot
	 *            Sets whether the animation should play once or repeat
	 * @return AnimationDrawable
	 */
	public AnimationDrawable CreateAnimationDrawable(Context context, int[] drawableID, int duration, boolean onceShot) {
		AnimationDrawable anim = new AnimationDrawable();
		for (int i = 0; i < drawableID.length; i++) {
			anim.addFrame(context.getResources().getDrawable(drawableID[i]), duration);
		}
		anim.setOneShot(onceShot);

		return anim;
	}

	public Animation TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta,
	        long durationMillis, int repeatCount, int repeatMode) {
		TranslateAnimation animation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);

		animation.setDuration(durationMillis);
		animation.setRepeatCount(repeatCount);
		if (repeatCount > 0 || repeatCount == TranslateAnimation.INFINITE)
			animation.setRepeatMode(repeatMode);

		return animation;
	}
}
