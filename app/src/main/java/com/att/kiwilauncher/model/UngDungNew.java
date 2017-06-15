package com.att.kiwilauncher.model;

import java.util.List;

/**
 * Created by admin on 5/25/2017.
 */

public class UngDungNew {
    String id;
    String name;
    boolean installed;
    String icon;
    List<String> anh;
    String des;
    String version;
    String linkCai;
    String luotCai;
    String rating;
    String versionCode;
    String update;

    public UngDungNew(String id, String name, boolean installed, String icon, List<String> anh, String des,
                      String version, String linkCai, String luotCai, String rating, String versionCode, String update) {
        this.id = id;
        this.name = name;
        this.installed = installed;
        this.icon = icon;
        this.anh = anh;
        this.des = des;
        this.version = version;
        this.linkCai = linkCai;
        this.luotCai = luotCai;
        this.rating = rating;
        this.versionCode = versionCode;
        this.update = update;
    }

    public String getUpdate() {
        return update;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLinkCai() {
        return linkCai;
    }

    public void setLinkCai(String linkCai) {
        this.linkCai = linkCai;
    }

    public String getLuotCai() {
        return luotCai;
    }

    public void setLuotCai(String luotCai) {
        this.luotCai = luotCai;
    }

    public UngDungNew() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInstalled() {
        return installed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getAnh() {
        return anh;
    }

    public void setAnh(List<String> anh) {
        this.anh = anh;
    }

}
