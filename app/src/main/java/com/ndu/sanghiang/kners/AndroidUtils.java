package com.ndu.sanghiang.kners;

/**
 * Created by Nandang Duryat on 2/22/2018.
 */

import android.os.Build;

public final class AndroidUtils {
    public static boolean isLollipop() {
        return android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}