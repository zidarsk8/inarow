package com.psywerx.utils;

import android.util.Log;

/**
 * 
 * Wraps android.util.Log
 * 
 * @author smotko
 * 
 */
public class L {

	private static final String TAG = "psywerx";
	private static final boolean DEBUG = true;

	/**
	 * INFORMATIVE: This level of logging should used be to note that something
	 * interesting to most people happened, i.e. when a situation is detected
	 * that is likely to have widespread impact, though isn't necessarily an
	 * error. Such a condition should only be logged by a module that reasonably
	 * believes that it is the most authoritative in that domain (to avoid
	 * duplicate logging by non-authoritative components). This level is always
	 * logged.
	 * 
	 * @param msg
	 */
	public static void i(String msg) {
		if(DEBUG) Log.i(TAG, msg);
	}

	/**
	 * DEBUG: This level of logging should be used to further note what is
	 * happening on the device that could be relevant to investigate and debug
	 * unexpected behaviors. You should log only what is needed to gather enough
	 * information about what is going on about your component. If your debug
	 * logs are dominating the log then you probably should be using verbose
	 * logging.
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if(DEBUG) Log.d(TAG, msg);
	}

	/**
	 * VERBOSE: This level of logging should be used for everything else. This
	 * level will only be logged on debug builds and should be surrounded by an
	 * if (LOCAL_LOGV) block (or equivalent) so that it can be compiled out by
	 * default. Any string building will be stripped out of release builds and
	 * needs to appear inside the if (LOCAL_LOGV) block.
	 * 
	 * @param msg
	 */
	public static void v(String msg) {
		if(DEBUG) Log.w(TAG, msg);
	}

	/**
	 * WARNING: This level of logging should used when something serious and
	 * unexpected happened, i.e. something that will have user-visible
	 * consequences but is likely to be recoverable without data loss by
	 * performing some explicit action, ranging from waiting or restarting an
	 * app all the way to re-downloading a new version of an application or
	 * rebooting the device. This level is always logged. Issues that justify
	 * some logging at the WARNING level might also be considered for reporting
	 * to a statistics-gathering server.
	 * 
	 * @param msg
	 */
	public static void w(String msg) {
		if(DEBUG) Log.v(TAG, msg);
	}

	/**
	 * ERROR: This level of logging should be used when something fatal has
	 * happened, i.e. something that will have user-visible consequences and
	 * won't be recoverable without explicitly deleting some data, uninstalling
	 * applications, wiping the data partitions or reflashing the entire phone
	 * (or worse). This level is always logged. Issues that justify some logging
	 * at the ERROR level are typically good candidates to be reported to a
	 * statistics-gathering server.
	 * 
	 * @param msg
	 */
	public static void e(String msg) {
		if(DEBUG) Log.e(TAG, msg);
	}

    public static void wtf(String msg) {
        if(DEBUG) Log.wtf(TAG, msg);
    }

}
