package com.att.kiwilauncher.model;

/**
 * Created by admin on 5/25/2017.
 */

public class TheLoai {
    private String id;
    private String ten;
    private String soLuong;
    private int icon;
    private  boolean checked;

    public TheLoai() {
    }

    public TheLoai(String ten, String soLuong, int icon, String id, boolean checked) {
        this.ten = ten;
        this.soLuong = soLuong;
        this.icon = icon;
        this.id = id;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(String soLuong) {
        this.soLuong = soLuong;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
