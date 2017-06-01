package com.att.kiwilauncher;

import android.graphics.drawable.Drawable;

/**
 * Created by mac on 5/23/17.
 */

public class UngDung {
    CharSequence    labelApp;
    CharSequence    nameApp;
    Drawable        iconApp;

    public CharSequence getNameApp() {
        return nameApp;
    }

    public void setNameApp(CharSequence nameApp) {
        this.nameApp = nameApp;
    }

    public CharSequence getLabelApp() {
        return labelApp;
    }

    public void setLabelApp(CharSequence labelApp) {
        this.labelApp = labelApp;
    }

    public Drawable getIconApp() {
        return iconApp;
    }

    public void setIconApp(Drawable iconApp) {
        this.iconApp = iconApp;
    }
}
