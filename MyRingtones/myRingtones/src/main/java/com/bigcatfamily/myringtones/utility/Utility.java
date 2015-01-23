package com.bigcatfamily.myringtones.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bigcatfamily.myringtones.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class Utility {
	private static final String TAG = Utility.class.getSimpleName();

	/**
	 * Obtains character set of the entity, if known.
	 * 
	 * @param entity
	 *            must not be null
	 * @return the character set, or null if not found
	 * @throws ParseException
	 *             if header elements cannot be parsed
	 * @throws IllegalArgumentException
	 *             if entity is null
	 */
	public static String getContentCharSet(final HttpEntity entity) throws ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		String charset = null;

		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}

		return charset;
	}

	/**
	 * Get the entity content as a String, using the provided default character
	 * set if none is found in the entity. If defaultCharset is null, the
	 * default "ISO-8859-1" is used.
	 * 
	 * @param entity
	 *            must not be null
	 * @param defaultCharset
	 *            character set to be applied if none found in the entity
	 * @return the entity content as a String
	 * @throws ParseException
	 *             if header elements cannot be parsed
	 * @throws IllegalArgumentException
	 *             if entity is null or if content length > Integer.MAX_VALUE
	 * @throws IOException
	 *             if an error occurs reading the input stream
	 */
	public static String toString(final HttpEntity entity, final String defaultCharset) throws IOException,
	        ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}

		InputStream instream = entity.getContent();

		if (instream == null) {
			return "";
		}

		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("HTTP entity too large to be buffered in memory");
		}
		// Log.d( "HTTP", "entity.getContentLength( ) = " +
		// entity.getContentLength( ) );
		// Log.d( "HTTP", "instream.available( ) = " + instream.available( ) );
		// int i = ( int ) entity.getContentLength( );
		//
		// if ( i < 0 )
		// {
		// i = 4096;
		// }

		String charset = getContentCharSet(entity);

		if (charset == null) {
			charset = defaultCharset;
		}

		if (charset == null) {
			charset = HTTP.DEFAULT_CONTENT_CHARSET;
		}

		Reader reader = new InputStreamReader(instream, charset);

		StringBuilder buffer = new StringBuilder();

		try {
			char[] tmp = new char[1024];

			int l;

			while ((l = reader.read(tmp)) != -1) {
				// Log.d( "HTTP", "readerrrrrrrrrrrrrrrrrrrr l = " + l );
				buffer.append(tmp, 0, l);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			reader.close();
		}
		String str = buffer.toString();
		// Log.d( "HTTP", "readerrrrrrrrrrrrrrrrrrrr str = " + str );
		return str;
	}

	/**
	 * Read the contents of an entity and return it as a String. The content is
	 * converted using the character set from the entity (if any), failing that,
	 * "ISO-8859-1" is used.
	 * 
	 * @param entity
	 * @return String containing the content.
	 * @throws ParseException
	 *             if header elements cannot be parsed
	 * @throws IllegalArgumentException
	 *             if entity is null or if content length > Integer.MAX_VALUE
	 * @throws IOException
	 *             if an error occurs reading the input stream
	 */
	public static String toString(final HttpEntity entity) throws IOException, ParseException {
		return toString(entity, null);
	}

	/**
	 * Convert string to md5 string
	 * 
	 * @param s
	 * @return md5 string
	 */
	public static final String MD5(final String s) {
		if (!IsEmpty(s))
			try {
				// Create MD5 Hash
				MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
				digest.update(s.getBytes());
				byte messageDigest[] = digest.digest();

				// Create Hex String
				StringBuffer hexString = new StringBuffer();
				for (int i = 0; i < messageDigest.length; i++) {
					String h = Integer.toHexString(messageDigest[i] & 0xFF);
					while (h.length() < 2)
						h = "0" + h;
					hexString.append(h);
				}
				return hexString.toString();

			} catch (Exception e) {
				e.printStackTrace();
			}
		return "";
	}

	/**
	 * Check whether string is null/empty or not
	 * 
	 * @param str
	 * @return
	 */
	public static boolean IsEmpty(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * Check whether talklife email address is valid or not
	 * 
	 * @param str
	 * @return
	 */
	public static boolean IsTalkLifeEmailValid(String email_address) {
		if (IsEmpty(email_address))
			return false;

		if (!email_address.contains("@"))
			return false;
		String temp = email_address.replaceFirst("@", "");
		if (temp.contains("@"))
			return false;
		int str_len = email_address.length();
		int a_index = email_address.indexOf("@");
		if (a_index >= str_len - 3)
			return false;
		String domain = email_address.substring(a_index);
		if (!domain.contains("."))
			return false;
		int dot_index = domain.indexOf(".");
		if (dot_index >= domain.length() - 1)
			return false;
		if (domain.contains(".."))
			return false;

		return true;
	}

	public static String GetBirthday(String date) {
		if (IsEmpty(date))
			return "unidentified";

		int yr_year = 0, yr_month = 0, yr_date = 0;
		try {
			String[] str = date.split("-");
			yr_year = Integer.parseInt(str[0]);
			yr_month = Integer.parseInt(str[1]);
			yr_date = Integer.parseInt(str[2]);
		} catch (Exception e) {
			return "unidentified";
		}

		long time = System.currentTimeMillis();
		Calendar today = Calendar.getInstance();
		today.setTimeInMillis(time);

		Calendar birthDate = Calendar.getInstance();
		birthDate.set(yr_year, yr_month - 1, yr_date);

		if (birthDate.after(today)) {
			return "unidentified";
		}

		int year = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH) - birthDate.get(Calendar.MONTH);
		int day = today.get(Calendar.DAY_OF_MONTH) - birthDate.get(Calendar.DAY_OF_MONTH);
		if (month > 0) {
			year += 1;
		} else if (month == 0) {
			if (day >= 0)
				year += 1;
		}

		return String.valueOf(year);
	}

	@SuppressLint("InlinedApi")
	public static int DetectDiviceScreenKind(Context context) {
		Configuration config = context.getResources().getConfiguration();
		if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
			return Configuration.SCREENLAYOUT_SIZE_XLARGE;
		} else if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
			return Configuration.SCREENLAYOUT_SIZE_NORMAL;
		} else if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
			return Configuration.SCREENLAYOUT_SIZE_SMALL;
		} else if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
			return Configuration.SCREENLAYOUT_SIZE_LARGE;
		} else {
			return Configuration.SCREENLAYOUT_SIZE_UNDEFINED;
		}
	}

	public static float GetScaleForCurrentDiviceSize(Context context, int nBaseSize) {
		if (DetectDiviceScreenKind(context) == nBaseSize) {
			return 0;
		} else {
			switch (DetectDiviceScreenKind(context)) {
			case Configuration.SCREENLAYOUT_SIZE_XLARGE:

				break;

			default:
				break;
			}
		}

		return 0;
	}

	public static String GetStringWithLimit(String string, int limit) {
		String ellip = "...";
		if (string == null || string.length() <= limit || string.length() < ellip.length()) {
			return string;
		}
		return string.substring(0, limit - ellip.length()).concat(ellip);
	}

	public static int GetAge(String birthDate) throws java.text.ParseException {
		Date currentDate = new Date();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(birthDate);
		Calendar now = Calendar.getInstance();
		Calendar dob = Calendar.getInstance();
		dob.setTime(dateOfBirth);
		now.setTime(currentDate);
		// if (dob.after(now)) {
		// throw new IllegalArgumentException("Can't be born in the future");
		// }
		int year1 = now.get(Calendar.YEAR);
		int year2 = dob.get(Calendar.YEAR);
		int age = year1 - year2;
		int month1 = now.get(Calendar.MONTH);
		int month2 = dob.get(Calendar.MONTH);
		if (month2 > month1) {
			age--;
		} else if (month1 == month2) {
			int day1 = now.get(Calendar.DAY_OF_MONTH);
			int day2 = dob.get(Calendar.DAY_OF_MONTH);
			if (day2 > day1) {
				age--;
			}
		}

		return age;
	}

	// public static String GetTimeFromNowToDate(String date) {
	// String result = "";
	// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
	// try {
	// DateTime startDate = new DateTime(sdf.parse(date).getTime());
	// DateTime endDate = new DateTime(DateTimeZone.UTC);
	// result = GetTimeBetWeenDates(startDate, endDate);
	// } catch (java.text.ParseException e) {
	// e.printStackTrace();
	// }
	//
	// return result;
	// }

	public static Date GetTimeFromDateString(String date) {
		Date result = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
		try {
			result = new Date(sdf.parse(date).getTime());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String GetUTCStringTimeFromDate(Date date) {
		String result = "";

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss");
		result = sdf.format(date);

		return result;
	}

	// public static String GetTimeBetWeenDates(DateTime startDate, DateTime
	// endDate) {
	// Days days = Days.daysBetween(startDate, endDate);
	// Months months = Months.monthsBetween(startDate, endDate);
	// Years years = Years.yearsBetween(startDate, endDate);
	// Hours hours = Hours.hoursBetween(startDate, endDate);
	// Minutes minutes = Minutes.minutesBetween(startDate, endDate);
	// Seconds seconds = Seconds.secondsBetween(startDate, endDate);
	// // Period period = new Period(startDate, endDate);
	// String result = "Now";
	// if (years.getYears() != 0) {
	// if (years.getYears() > 1)
	// result = years.getYears() + " years";
	// else
	// result = years.getYears() + " year";
	// } else if (months.getMonths() != 0) {
	// if (months.getMonths() > 1)
	// result = months.getMonths() + " months";
	// else
	// result = months.getMonths() + " month";
	// } else if (months.getMonths() == 0 && years.getYears() == 0 &&
	// days.getDays() != 0) {
	// if (days.getDays() > 1)
	// result = days.getDays() + " days";
	// else
	// result = days.getDays() + " day";
	// } else if (hours.getHours() != 0 && days.getDays() == 0) {
	// if (hours.getHours() > 1)
	// result = hours.getHours() + " hours";
	// else
	// result = hours.getHours() + " hour";
	// } else if (minutes.getMinutes() != 0 && hours.getHours() == 0) {
	// if (minutes.getMinutes() > 1)
	// result = minutes.getMinutes() + " minute";
	// else
	// result = minutes.getMinutes() + " minutes";
	// } else if (seconds.getSeconds() != 0 && minutes.getMinutes() == 0) {
	// if (seconds.getSeconds() > 1)
	// result = seconds.getSeconds() + " second";
	// else
	// result = seconds.getSeconds() + " seconds";
	// }
	// return result + " ago";
	// }

	public static void ShowKeyBoard(Context context, EditText edittext) throws Exception {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(edittext,
		        InputMethodManager.SHOW_FORCED);
	}

	public static void HideKeyBoard(Activity activity) {
		if (activity.getCurrentFocus() != null) {
			InputMethodManager inputMethodManager = (InputMethodManager) activity
			        .getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		}
	}

	/* Capture and choose exising photo */
	public static final int CAMERA_PIC_REQUEST = 1336;
	public static final int GALLERY_PIC_REQUEST = 1337;
	public static final int PIC_CROP = 1338;

	public static void copyStream(InputStream input, OutputStream output) throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public static int GetRandomNumberInRange(int from, int to) {
		return from + (int) (Math.random() * ((to - from) + 1));
	}

	public static String getUTCGMTTime() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		f.setTimeZone(TimeZone.getTimeZone("UTC"));
		return f.format(new Date());
	}

	public static String getUTCGMTStringTimeFromDate(Date date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		f.setTimeZone(TimeZone.getTimeZone("UTC"));
		return f.format(date);
	}

	public static Date getUTCGMTDateFromString(String strDate) {
		try {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			f.setTimeZone(TimeZone.getTimeZone("UTC"));

			return f.parse(strDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getStrTimeFromDate() {
		return getStrTimeFromDate(new Date());
	}

	public static String getStrTimeFromDate(Date date) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return f.format(date);
	}

	public static Date getDateFromString(String strDate) {
		try {
			SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			return f.parse(strDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String getStrTimeFromSimpleDate() {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		return f.format(new Date());
	}

	public static Date getDateFromSimpleString(String strDate) {
		try {
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
			return f.parse(strDate);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static int GetNumberInsideString(String str) throws Exception {
		String result = "";

		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(str);

		// Find all matches
		while (matcher.find()) {
			result += matcher.group();
		}

		return Integer.parseInt(result);
	}

	// public static Date convertUTCToCurrentTime(long timeStamp) {
	// Date result = null;
	// try {
	// return new
	// Date(DateTimeZone.forTimeZone(Calendar.getInstance().getTimeZone()).convertUTCToLocal(timeStamp));
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return result;
	// }

	public static void writeToFile(String fileName, String data, Context context) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName,
			        Context.MODE_PRIVATE));
			outputStreamWriter.write(data);
			outputStreamWriter.close();
		} catch (IOException e) {
			Log.e("Exception", "File write failed: " + e.toString());
		}
	}

	public static String readFromFile(String fileName, Context context) {

		String ret = "";

		try {
			InputStream inputStream = context.openFileInput(fileName);

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}

				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	public static Drawable getDrawbleInAsset(Context context, String fileName) {
		try {
			// get input stream
			InputStream ims = context.getAssets().open(fileName);
			// load image as Drawable
			return Drawable.createFromStream(ims, null);
		} catch (IOException ex) {
			return null;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public static Date convertStringToDate(String str) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss +0000");
		try {
			return simpleDateFormat.parse(str);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressLint("SimpleDateFormat")
	public static Date convertStringToDateLocal(String str) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			return simpleDateFormat.parse(str);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * format like %d min, %d sec
	 * */

	public static String convertLongToMinutes(String format, long duration) {
		return String.format(
		        format,
		        TimeUnit.MILLISECONDS.toMinutes(duration),
		        TimeUnit.MILLISECONDS.toSeconds(duration)
		                - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
	}

	public static DisplayImageOptions getDisplayImageOptionWithDefaultImage() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		        .resetViewBeforeLoading(false)
		        // default
		        .delayBeforeLoading(100).cacheInMemory(true).showImageForEmptyUri(R.drawable.track_bg_default_image)
		        .showImageForEmptyUri(R.drawable.track_bg_default_image)
		        .showImageOnLoading(R.drawable.track_bg_default_image) // default
		        .cacheOnDisk(true) // default
		        .considerExifParams(false) // default
		        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
		        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
		        .displayer(new SimpleBitmapDisplayer()) // default
		        .handler(new Handler()) // default
		        .build();

		return options;
	}

	public static DisplayImageOptions getDisplayImageOptionWithRoundedImage(int mIAvatarCircleWidth) {
		return new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheOnDisk(true).cacheInMemory(true)
		        .displayer(new BCFRoundedBitmapDisplayer(mIAvatarCircleWidth))
		        .showImageForEmptyUri(R.drawable.icon_music_avatar).showImageForEmptyUri(R.drawable.icon_music_avatar)
		        .showImageOnLoading(R.drawable.icon_music_avatar).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	public static String fileNameFactory(String name) {
		return name.replaceAll(" ", "_").replaceAll("[-+.^:,]", "");
	}

	public static void createFolder(String folderName) {
		File rootPath = new File(Environment.getExternalStorageDirectory(), folderName);
		if (!rootPath.exists()) {
			rootPath.mkdirs();
		}
	}

	public static boolean checkFileExist(String strFilePath) {
		return new File(Environment.getExternalStorageDirectory(), strFilePath).exists();
	}

	public static void setDefaultRingtone(String songTitle, String filePath, String fileType, String singerName,
	        long duration, Context context, boolean isRingtone, boolean isSMS) {

		File file = new File(filePath);
		if (!file.exists()) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(MediaStore.MediaColumns.DATA, filePath);
		values.put(MediaStore.MediaColumns.TITLE, songTitle);
		values.put(MediaStore.MediaColumns.SIZE, file.length());
		values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/" + fileType);
		values.put(MediaStore.Audio.Media.ARTIST, singerName);
		values.put(MediaStore.Audio.Media.DURATION, duration);
		values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
		values.put(MediaStore.Audio.Media.IS_NOTIFICATION, true);
		values.put(MediaStore.Audio.Media.IS_ALARM, true);
		values.put(MediaStore.Audio.Media.IS_MUSIC, true);

		/* delete bug here */
		/* solution : store uri path after insert and update before insert */
		Uri uri = MediaStore.Audio.Media.getContentUriForPath(filePath);
		context.getContentResolver().delete(uri, MediaStore.MediaColumns.DATA + "=\"" + filePath + "\"", null);
		Uri newUri = context.getContentResolver().insert(uri, values);

		if (isRingtone)
			RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newUri);
		if (isSMS)
			RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION, newUri);
	}

	public static String getRealPathFromUri(Context context, Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public static boolean isContentResolverRsExist(File file, Context context) {
		final String track_id = MediaStore.Audio.Media._ID;
		final String track_no = MediaStore.Audio.Media.TRACK;
		final String track_name = MediaStore.Audio.Media.TITLE;
		final String artist = MediaStore.Audio.Media.ARTIST;
		final String duration = MediaStore.Audio.Media.DURATION;
		final String album = MediaStore.Audio.Media.ALBUM;
		final String composer = MediaStore.Audio.Media.COMPOSER;
		final String year = MediaStore.Audio.Media.YEAR;
		final String path = MediaStore.Audio.Media.DATA;
		final String[] columns = { track_id, track_no, artist, track_name, album, duration, path, year, composer };
		Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, columns, null,
		        null, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				Log.d(TAG, cursor.getString(cursor.getColumnIndexOrThrow(path)));
			}
			return false;
		} else
			return false;
	}
}
