package com.att.kiwilauncher.util;

import com.google.android.exoplayer2.C;

import java.util.Formatter;
import java.util.Locale;

/**
 * Created by tienh on 6/15/2017.
 */

public class CheckLink {

    public int CheckLinkURL(String link) {
        int check = 0;

        if (link.indexOf(".jpg")>0) check =1;
        else if (link.indexOf(".mp4")>0) check=2;
        else if (link.indexOf(".m3u8")>0) check=3;

        return check;
    }

    public String stringForTime(int timeMs) {
      StringBuilder formatBuilder = new StringBuilder();
        Formatter formatter=new Formatter(formatBuilder, Locale.getDefault());
        if (timeMs == C.TIME_UNSET) {
            timeMs = 0;
        }
        long totalSeconds = (timeMs + 500) / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        formatBuilder.setLength(0);
        return hours > 0 ? formatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
                : formatter.format("%02d:%02d", minutes, seconds).toString();
    }
}
