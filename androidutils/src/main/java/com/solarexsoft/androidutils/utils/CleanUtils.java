package com.solarexsoft.androidutils.utils;

import java.io.File;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 06/05/2017
 *    Desc:
 * </pre>
 */

public final class CleanUtils {
    private CleanUtils() {
        throw new UnsupportedOperationException("Can't instantiate directly");
    }

    public static boolean cleanInternalCache() {
        return false;
    }

    public static boolean cleanInternalDbs() {
        return false;
    }

    public static boolean cleanInternalSP() {
        return false;
    }

    public static boolean cleanInternalFiles() {
        return false;
    }

    public static boolean cleanExternalCache() {
        return false;
    }

    public static boolean cleanCustomCache(File dir) {
        return false;
    }
}
