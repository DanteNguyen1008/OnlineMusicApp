/**
 * 
 */
package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author annqd
 * 
 */
public class NLScrollLastListView extends ListView {
	private static final int MIN_HEIGHT_TO_CHANGE = 300;

	public NLScrollLastListView(Context context) {
		super(context);
	}

	public NLScrollLastListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NLScrollLastListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);

		if (yNew - yOld > MIN_HEIGHT_TO_CHANGE)
			setSelection(getCount());

	}
}
