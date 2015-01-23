/**
 * Methods.java
 * 
 * Purpose              : List all methods
 * 
 * Optional info        : 
 *
 * @author              : Hoang Viet 
 * 
 * @date                : Apr 9, 2012
 * 
 * @lastChangedRevision : 
 *
 * @lastChangedDate     :
 *
 */
package com.bigcatfamily.myringtones.networkhandler;

import java.io.Serializable;

import android.content.Intent;

/**
 * Methods.java
 * 
 * <br>
 * <b>Purpose</b> :
 * 
 * <br>
 * <b>Optional info</b> :
 * 
 * <br>
 * <b>author</b> :
 * 
 * <br>
 * <b>date</b> : Jul 23, 2013
 * 
 * <br>
 * <b>lastChangedRevision</b> :
 * 
 * <br>
 * <b>lastChangedDate</b> :
 * 
 */
public enum Methods implements Serializable {
	GET_SOUND_LIST, GET_DOWNLOAD_SOUND_LIST, GET_SEARCH_TRACK_LIST, GET_ITUNE_TOP_HIT;
	private static final String name = Methods.class.getName();

	public void attachTo(Intent intent) {
		intent.putExtra(name, ordinal());
	}

	public static Methods detachFrom(Intent intent) {
		if (!intent.hasExtra(name))
			throw new IllegalStateException();
		return values()[intent.getIntExtra(name, -1)];
	}
}