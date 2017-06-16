package com.att.kiwilauncher.util;

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
}
