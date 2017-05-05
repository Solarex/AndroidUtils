package com.solarexsoft.androidutils.utils;

import android.content.Context;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 04/05/2017
 *    Desc:
 * </pre>
 */

public final class Utils {
    private static Context mContext;

    private Utils() {
        throw new UnsupportedOperationException("Can't instantiate directly");
    }

    public static void init(Context context) {
        mContext = context.getApplicationContext();
    }

    public static Context getContext() {
        if (mContext != null) {
            return mContext;
        }
        throw new NullPointerException("call init first!");
    }
}
