
package com.att.kiwilauncher.model;

/**
 * Created by admin on 6/3/2017.
 */

public class ThoiTiet {
    String id;
    String ten;
    String maThoiTiet;

    public ThoiTiet() {
    }

    public ThoiTiet(String id, String ten, String maThoiTiet) {
        this.id = id;
        this.ten = ten;
        this.maThoiTiet = maThoiTiet;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMaThoiTiet() {
        return maThoiTiet;
    }

    public void setMaThoiTiet(String maThoiTiet) {
        this.maThoiTiet = maThoiTiet;
    }
}

