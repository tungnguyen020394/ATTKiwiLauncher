package com.att.kiwilauncher.util;

/**
 * Created by admin on 7/3/2017.
 */

public class DateCompare {
    public static boolean compareDate(String checkDate, String currentDate) {

        String date1 = splitDateTime(checkDate, 1)[0];
        String time1 = splitDateTime(checkDate, 1)[1];
        String date2 = splitDateTime(currentDate, 1)[0];
        String time2 = splitDateTime(currentDate, 1)[1];

        if (Integer.parseInt(splitDateTime(date1, 2)[0]) < Integer.parseInt(splitDateTime(date2, 2)[0])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(date1, 2)[1]) < Integer.parseInt(splitDateTime(date2, 2)[1])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(date1, 2)[2]) < Integer.parseInt(splitDateTime(date2, 2)[2])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(time1, 3)[0]) < Integer.parseInt(splitDateTime(time2, 3)[0])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(time1, 3)[1]) < Integer.parseInt(splitDateTime(time2, 3)[1])) {
            return true;
        }
        if (Integer.parseInt(splitDateTime(time1, 3)[2]) <= Integer.parseInt(splitDateTime(time2, 3)[2])) {
            return true;
        }
        return false;
    }

    public static String[] splitDateTime(String dateTime, int type) {
        String[] kq;
        if (type == 1) {
            kq = dateTime.split(" ");
        } else if (type == 2) {
            kq = dateTime.split("-");
        } else {
            kq = dateTime.split(":");
        }
        return kq;
    }
}
