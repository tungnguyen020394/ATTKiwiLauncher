package com.att.kiwilauncher.util;

/**
 * Created by tienh on 6/14/2017.
 */

public class Define {
    public static final String DB_THELOAI = "theloai";
    public static final String DB_UNGDUNG = "ungdung";
    public static final String DB_ANHCHITIET = "anhchitiet";
    public static final String DB_THELOAIUNGDUNG = "theloai_ungdung";
    public static final String DB_LOAIQC = "loaiquangcao";
    public static final String DB_QUANGCAO = "quangcao";
    public static final String DB_NAME = "kiwistore.sqlite";

    public static final String URL_LLNK_WEATHER_API = "http://api.openweathermap.org/data/2.5/forecast?id=";
    public static final int NUMBER_RESULT_FULL = 50;
    public static final int NUMBER_RESULT_WEB = 40;

    public final static String URL_WEATHER = "http://api.openweathermap.org/data/2.5/forecast?id=";
    public final static String APIKEY = "1fd660e2a27afad8b71405f654997a62";
    //URL Json
    //http://kiwi.websumo.vn/request_json/theloai/$2y$10$xZHJxJ6lcDnwamJUpWTy..ub6oLZoP.qOR17bw6m10dkpZMTCEgGO
    public static final String IP_SERVER = "kiwi.websumo.vn/";
    public static final String API_KEY = "/$2y$10$xZHJxJ6lcDnwamJUpWTy..ub6oLZoP.qOR17bw6m10dkpZMTCEgGO";
    public static final String BASE_URL = "http://" + IP_SERVER + "request_json/";
    public static final String API_UNGDUNG = BASE_URL + DB_UNGDUNG + API_KEY;
    public static final String API_THELOAI = BASE_URL + DB_THELOAI + API_KEY;
    public static final String API_ANHCHITIET = BASE_URL + DB_ANHCHITIET + API_KEY;
    public static final String API_THELOAI_UNGDUNG = BASE_URL + DB_THELOAIUNGDUNG + API_KEY;
    public static final String API_QUANGCAO = BASE_URL + DB_QUANGCAO + API_KEY;
    public static final String API_LOAIQC = BASE_URL + DB_LOAIQC + API_KEY;
    public static final String URL_IMAGE = "http://" + IP_SERVER + "public/upload";
    public static final String URL_FILE = "http://" + IP_SERVER + "public/fileapp";
    public static final String URL_LINK_IMG02 = "http://1.bp.blogspot.com/-KwSC3miMEGY/VKs8xTyHQPI/AAAAAAAAASM/GYb7jPYZgaM/s1600/Cloud-Hosting-Service---banner.jpg";
    public static final String URL_LINK_IMG01 = "http://www.thietkelogo.pro.vn/upload/hinhchung/thietke/banner-quang-cao-xe-oto.jpg";
    public static final String URL_LINK_PLAY = "http://118.70.81.139:1969/live/bongda/bongda.m3u8";
    //http://clips.vorwaerts-gmbh.de/VfE_html5.mp4
//    public static final String URL_LINK_BACK="https://hls.mediacdn.vn/vtv/tojizlwz1plyh73dcccccccccccctp/2017/06/14/tin-hieu-lac-quan-dtvn-1497400347001-9434d_360p.mp4";
    public static final String URL_LINK_BACK = "https://dzbbmecpa0hd2.cloudfront.net/video/original/2016/12/24/06/1482562261_03fcd89265fa2030.mp4";
}
