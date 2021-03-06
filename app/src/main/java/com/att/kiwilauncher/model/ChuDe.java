package com.att.kiwilauncher.model;

import java.io.Serializable;

/**
 * Created by mac on 5/23/17.
 */

public class ChuDe implements Serializable{
    String nameCate;
    // ảnh thể loại
    int drawCate;
    //id thể loại
    int indexCate;
    //link icon
    String iconLink;
    // chủ đề được chọn hay không ?
    boolean checkedCate;

    public ChuDe(String nameCate, int drawCate, int indexCate, boolean checkedCate) {
        this.nameCate = nameCate;
        this.drawCate = drawCate;
        this.indexCate = indexCate;
        this.checkedCate = checkedCate;

    }

    public ChuDe(String nameCate, int drawCate, int indexCate, String iconLink, boolean checkedCate) {
        this.nameCate = nameCate;
        this.drawCate = drawCate;
        this.indexCate = indexCate;
        this.iconLink = iconLink;
        this.checkedCate = checkedCate;
    }

    public ChuDe() {}

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
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
