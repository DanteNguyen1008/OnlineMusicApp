package com.example.androiddownloadmanager;

import java.io.File;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

public class Utility {
	public static File getFile(Context context, String filePath) {
		File f = null;
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				f = new File(context.getExternalFilesDir(null), filePath);
				f.createNewFile();
			} else {
				File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				f = new File(path, filePath);
				path.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f;
	}
}
