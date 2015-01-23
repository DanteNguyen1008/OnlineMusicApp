package com.bigcatfamily.myringtones.dialogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bigcatfamily.myringtones.R;

public abstract class RingtoneDialogFragment extends DialogFragment {

	public enum eRingtoneType {
		PHONE, SMS
	}

	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		View view = inflater.inflate(R.layout.dialog_ringtone, container, false);

		view.findViewById(R.id.btn_phone_ringtone).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onItemClick(eRingtoneType.PHONE);
			}
		});

		view.findViewById(R.id.btn_sms_ringtone).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onItemClick(eRingtoneType.SMS);
			}
		});

		getDialog().setCancelable(true);
		getDialog().setCanceledOnTouchOutside(true);

		return view;
	}

	private void onItemClick(eRingtoneType ringtoneType) {
		dismiss();
		OnDialogResult(ringtoneType);
	}

	public abstract void OnDialogResult(Object object);
}
