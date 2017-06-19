package com.att.kiwilauncher.model;

/**
 * Created by mac on 5/23/17.
 */

public class ChuDe {
    String nameCate;
    // ảnh thể loại
    int drawCate;
    //id thể loại
    int indexCate;
    // chủ đề được chọn hay không ?
    boolean checkedCate;

    public ChuDe(String nameCate, int drawCate, int indexCate, boolean checkedCate) {
        this.nameCate = nameCate;
        this.drawCate = drawCate;
        this.indexCate = indexCate;
        this.checkedCate = checkedCate;
    }

    public ChuDe() {
    }

    public boolean isCheckedCate() {
        return checkedCate;
    }

    public void setCheckedCate(boolean checkedCate) {
        this.checkedCate = checkedCate;
    }

    public String getNameCate() {
        return nameCate;
    }

    public void setNameCate(String nameCate) {
        this.nameCate = nameCate;
    }

    public int getDrawCate() {
        return drawCate;
    }

    public void setDrawCate(int drawCate) {
        this.drawCate = drawCate;
    }

    public int getIndexCate() {
        return indexCate;
    }

    public void setIndexCate(int indexCate) {
        this.indexCate = indexCate;
    }
}
