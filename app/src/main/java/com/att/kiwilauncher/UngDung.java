package com.att.kiwilauncher;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by mac on 5/23/17.
 */

public class UngDung implements Serializable {
    CharSequence labelApp;
    CharSequence nameApp;
    Drawable iconApp;
    String icon;
    String id;

    public UngDung() {
    }

    public UngDung(CharSequence labelApp, CharSequence nameApp, Drawable iconApp, String icon, String id) {
        this.labelApp = labelApp;
        this.nameApp = nameApp;
        this.iconApp = iconApp;
        this.icon = icon;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

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
