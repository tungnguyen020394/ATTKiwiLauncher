package com.att.kiwilauncher.model;

import java.io.Serializable;

/**
 * Created by admin on 6/21/2017.
 */

public class QuangCao implements Serializable{
    //1-video web || 3-text || 4-image web
    String loaiQuangCao;
    String noiDung;
    String text;
    String linkWeb;
    String linkVideo;
    String linkImage;
    String time;

    public String getLinkVideo() {
        return linkVideo;
    }

    public void setLinkVideo(String linkVideo) {
        this.linkVideo = linkVideo;
    }

    public String getLinkImage() {
        return linkImage;
    }

    public void setLinkImage(String linkImage) {
        this.linkImage = linkImage;
    }

    public QuangCao() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLinkWeb() {
        return linkWeb;
    }

    public void setLinkWeb(String linkWeb) {
        this.linkWeb = linkWeb;
    }



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLoaiQuangCao() {
        return loaiQuangCao;
    }

    public void setLoaiQuangCao(String loaiQuangCao) {
        this.loaiQuangCao = loaiQuangCao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
}