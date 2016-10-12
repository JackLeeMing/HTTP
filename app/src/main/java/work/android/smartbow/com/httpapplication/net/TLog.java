/*
 * Copyright (c) 2016. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package work.android.smartbow.com.httpapplication.net;

import android.util.Log;

public class TLog {
	public static final String LOG_TAG = "Citybms_DEBUG";
	public static boolean DEBUG = true;

	public TLog() {
	}

	public static void analytics(String log) {
		//if (DEBUG)
			Log.d(LOG_TAG, log);
	}

	public static  void error(String log) {
		//if (DEBUG)
			Log.e(LOG_TAG, "" + log);
	}

	public static  void log(String log) {
		//if (DEBUG)
			Log.i(LOG_TAG, log);
	}

	public static void log(String tag, String log) {
		//if (DEBUG)
			Log.i(tag, log);
	}

	public static void logv(String log) {
		//if (DEBUG)
			Log.v(LOG_TAG, log);
	}

	public static void warn(String log) {
		//if (DEBUG)
			Log.w(LOG_TAG, log);
	}
}
