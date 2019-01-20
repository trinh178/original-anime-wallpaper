package com.samuigroup.originalanimewallpaper.utils;

import android.util.Log;

public class Utils {
    public static String[] splitTags(String tags, String regex) {
        return tags.split(regex);
    }
    public static String joinTags(String[] tags, String regex) {
        String s = "";
        for (String tag: tags) {
            s += tag + regex;
        }
        return s;
    }
    public static void Log(String msg) {
        Log.d("TEST", msg);
    }
}
