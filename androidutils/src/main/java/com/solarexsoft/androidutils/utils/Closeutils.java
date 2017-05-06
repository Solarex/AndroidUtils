package com.solarexsoft.androidutils.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * <pre>
 *    Author: houruhou
 *    Project: https://solarex.github.io/projects
 *    CreatAt: 05/05/2017
 *    Desc:
 * </pre>
 */

public final class Closeutils {
    private Closeutils() {
        throw new UnsupportedOperationException("Can't instantiate directly");
    }

    public static void closeIO(Closeable... closeables) {
        if (closeables == null)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void closeIOQuietly(Closeable... closeables) {
        if (closeables == null)
            return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
