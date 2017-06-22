package com.att.kiwilauncher.model;

/**
 * Created by admin on 6/22/2017.
 */

public class TheLoaiUngDung {
    String idTheLoai;
    String idUngDung;

    public TheLoaiUngDung() {
    }

    public TheLoaiUngDung(String idTheLoai, String idUngDung) {
        this.idTheLoai = idTheLoai;
        this.idUngDung = idUngDung;
    }

    public String getIdTheLoai() {
        return idTheLoai;
    }

    public void setIdTheLoai(String idTheLoai) {
        this.idTheLoai = idTheLoai;
    }

    public String getIdUngDung() {
        return idUngDung;
    }

    public void setIdUngDung(String idUngDung) {
        this.idUngDung = idUngDung;
    }
}
