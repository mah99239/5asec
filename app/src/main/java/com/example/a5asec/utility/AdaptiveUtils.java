package com.example.a5asec.utility;



import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.window.layout.WindowMetrics;
import androidx.window.layout.WindowMetricsCalculator;

public class AdaptiveUtils
    {
    static final int MEDIUM_SCREEN_WIDTH_SIZE = 600;
    static final int LARGE_SCREEN_WIDTH_SIZE = 1240;
    public enum WindowSizeClass
        {COMPACT, MEDIUM, EXPANDED}

    private AdaptiveUtils()
        {
        }

    /** Determines whether the device has a compact screen. **/
    public static boolean compactScreen(Activity activity) {
    WindowMetrics screenMetrics = WindowMetricsCalculator
            .getOrCreate()
            .computeMaximumWindowMetrics(activity);
    int shortSide = Math.min(screenMetrics.getBounds().width(),
            screenMetrics.getBounds().height());
    return shortSide / activity.getResources().getDisplayMetrics().density < MEDIUM_SCREEN_WIDTH_SIZE;
    }
    }
