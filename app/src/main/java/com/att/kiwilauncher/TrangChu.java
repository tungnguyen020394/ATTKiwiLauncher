package com.att.kiwilauncher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.att.kiwilauncher.adapter.ChuDeAdapter;
import com.att.kiwilauncher.adapter.UngDungAdapter;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.model.TheLoai;
import com.att.kiwilauncher.model.ThoiTiet;
import com.att.kiwilauncher.util.CheckLink;
import com.att.kiwilauncher.util.Define;
import com.att.kiwilauncher.util.Volume;
import com.att.kiwilauncher.view.VideoFull;
import com.att.kiwilauncher.xuly.DuLieu;
import com.att.kiwilauncher.xuly.LunarCalendar;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.att.kiwilauncher.R.id.imgFull;
import static com.att.kiwilauncher.R.id.imgPlay;
import static com.att.kiwilauncher.R.id.relay2;

public class TrangChu extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    public final static String APIKEY = "1fd660e2a27afad8b71405f654997a62";
    int chieuDai, chieuRong, didIndex = 0, willIndex, indexChuDe = 0, mChieuDai, mChieuRong, main = 12;
    RelativeLayout reLay1, reLay2, reLay3, reLay4, reLay111, reLay112, reLay113, reLay11,
            reLay21, reLay22, reLay222, reLay211, reLay212, reLay213, reLay214, reLay215, reLay216, reLay13, reLay12;
    List<ChuDe> cates;
    ArrayList<View> listItem;
    TextView text, mNgayAmTxt, mNgayDuongTxt, mTxtTinh, mTxtNhietDo;
    RecyclerView rcCategory;
    static RecyclerView rcApp;
    static PackageManager manager;
    static List<UngDung> apps;
    private List<TheLoai> mListTheLoai;
    static List<List<UngDung>> listApps;
    ArrayList<String> listvideo;
    public static View.OnClickListener appClick;
    VideoView video;
    ImageView image1, image2, image3, image4, image5, image6,
            imageCaiDat, imageMinus, imagePlus;
    public static final int REQUEST_SETTINGS = 101;
    static int demdsApp = 0;
    UngDungAdapter listapp;
    private static final String TAG = "TrangChu";
    DatabaseHelper mDatabaseHelper;
    private ProgressDialog dialog;

    Volume volume;
    ImageView imgView, imgWeb;
    ImageButton ibtNext, ibtPlay, ibtBack, ibtVolumeOf, ibtVolumeOn, ibtPause, ibtFull;
    TextView tvTimeStart, tvTimeEnd;
    CheckLink checkLink;
    MediaPlayer mp;
    SimpleExoPlayerView exoPlayer;
    SimpleExoPlayer player;
    TrackSelector trackSelector;
    private long timePause = 0;
    private boolean dragging;
    Intent intent;
    Handler handler = new Handler();

    int indexVideo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        loadData();
        addNavigationItem();
        addClicks();
    }

    private void addClicks() {
        appClick = new AppClick(this);
    }

    private void loadData() {
        // Load Category
        cates = new ArrayList<ChuDe>();
        ChuDe cate1 = new ChuDe("Giải Trí", R.drawable.ic_giaitri, 0, true);
        cates.add(cate1);
        ChuDe cate2 = new ChuDe("Trò Chơi", R.drawable.ic_trochoi, 0, false);
        cates.add(cate2);
        ChuDe cate3 = new ChuDe("Giáo Dục & Sức Khoẻ", R.drawable.ic_suckhoe, 0, false);
        cates.add(cate3);
        ChuDe cate4 = new ChuDe("Tiện Ích", R.drawable.ic_tienich, 0, false);
        cates.add(cate4);

        rcCategory.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcCategory.setLayoutManager(layoutManager1);
        ChuDeAdapter categoryAdapter = new ChuDeAdapter(this, cates);
        rcCategory.setAdapter(categoryAdapter);

        // Load App
        manager = getPackageManager();
        apps = new ArrayList<UngDung>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        listApps = new ArrayList();

        int soUngDung = 0;
        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.labelApp = ri.loadLabel(manager);
            app.nameApp = ri.activityInfo.packageName;
            app.iconApp = ri.activityInfo.loadIcon(manager);
            apps.add(app);
            soUngDung++;
            if (soUngDung == 7) {
                listApps.add(apps);
                apps = new ArrayList<UngDung>();
                soUngDung = 0;
            }
        }

        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.labelApp = ri.loadLabel(manager);
            app.nameApp = ri.activityInfo.packageName;
            app.iconApp = ri.activityInfo.loadIcon(manager);
            apps.add(app);
            soUngDung++;
            if (soUngDung == 7) {
                listApps.add(apps);
                apps = new ArrayList<UngDung>();
                soUngDung = 0;
            }
        }

        if (apps.size() != 0) {
            listApps.add(apps);
        }

        rcApp.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcApp.setLayoutManager(layoutManager);
        listapp = new UngDungAdapter(this, listApps.get(demdsApp));
        rcApp.setAdapter(listapp);

//        listvideo = mDatabaseHelper.getListVideoQuangCao();

        // video
//        if (!mDatabaseHelper.getLinkVideoQuangCao().equals("")) {
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                indexVideo++;
                setVideoOrImager(listvideo.get(indexVideo));
//                    setVideoOrImager(Define.URL_LINK_PLAY);
            }
        });
        //video.setVideoPath(listvideo.get(indexVideo));
        setVideoOrImager(listvideo.get(indexVideo));
//            setVideoOrImager(Define.URL_LINK_BACK);
//        }

        //audio
        volume.MuteAudio(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        createPlayer(Define.URL_LINK_PLAY);
//        exoPlayer.setOnTouchListener(this);

        if (DuLieu.hasInternetConnection(TrangChu.this)) {
            setVideoOrImager(listvideo.get(indexVideo));
        } else {

            Toast.makeText(getApplicationContext(), "Mất kết nối mạng...", Toast.LENGTH_LONG).show();
        }
    }

    public void createPlayer(String link) {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        exoPlayer.setPlayer(player);
        exoPlayer.setControllerHideOnTouch(false);
        prepareVideo(link);
    }

    public void prepareVideo(String link) {
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "ShweVideo"), bandwidthMeter);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource videoSource = new ExtractorMediaSource(Uri.parse(link),
                dataSourceFactory, extractorsFactory, null, null);

        player.prepare(videoSource);
        player.seekTo(timePause);

    }

    private void killPlayer() {
        if (player != null) {
            player.release();
            timePause = player.getCurrentPosition();
            //  player = null;
            //   player.setPlayWhenReady(false);
        }
    }

    private void addControls() {
        SharedPreferences sharedPreferences = getSharedPreferences("thoitiet", MODE_PRIVATE);
        String idThoiTiet = sharedPreferences.getString("idthoitiet", "24");
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.checkDatabase(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        chieuDai = displayMetrics.widthPixels;
        chieuRong = displayMetrics.heightPixels;
        mChieuDai = chieuDai / 70;
        mChieuRong = chieuRong / 40;

        listItem = new ArrayList<>();
        mListTheLoai = new ArrayList<>();
        listvideo = new ArrayList<>();

        listvideo.add(Define.URL_LINK_PLAY);
        listvideo.add(Define.URL_LINK_IMG01);
//        listvideo.add(Define.URL_LINK_BACK);
        listvideo.add(Define.URL_LINK_IMG02);

        // reLaytive layout
        reLay1 = (RelativeLayout) findViewById(R.id.relay1);
        reLay1.setPadding(mChieuDai, 0, mChieuDai, mChieuRong * 33);
        reLay2 = (RelativeLayout) findViewById(relay2);
        reLay2.setPadding(mChieuDai, mChieuRong * 6, mChieuDai, mChieuRong * 12);
        reLay3 = (RelativeLayout) findViewById(R.id.relay3);
        reLay3.setPadding(mChieuDai * 2, mChieuRong * 27, mChieuDai, mChieuRong * 8);
        reLay4 = (RelativeLayout) findViewById(R.id.relay4);
        reLay4.setPadding(0, mChieuRong * 31, 0, 0);

        reLay13 = (RelativeLayout) findViewById(R.id.relay13);
        reLay12 = (RelativeLayout) findViewById(R.id.relay12);
        reLay11 = (RelativeLayout) findViewById(R.id.relay11);
        reLay111 = (RelativeLayout) findViewById(R.id.relay111);
        reLay112 = (RelativeLayout) findViewById(R.id.relay112);
        reLay113 = (RelativeLayout) findViewById(R.id.relay113);
        reLay113.setOnClickListener(this);
        reLay13.setPadding(mChieuDai, 0, mChieuDai * 60, 0);
        reLay12.setPadding(mChieuDai * 9, mChieuRong, 0, mChieuRong * 2);

        reLay21 = (RelativeLayout) findViewById(R.id.relay21);
        reLay22 = (RelativeLayout) findViewById(R.id.relay22);
        reLay21.setPadding(0, 0, mChieuDai * 34, 0);
        reLay22.setPadding(mChieuDai * 34, 0, 0, 0);
        reLay222 = (RelativeLayout) findViewById(R.id.relay222);
        reLay211 = (RelativeLayout) findViewById(R.id.relay211);
        reLay212 = (RelativeLayout) findViewById(R.id.relay212);
        reLay213 = (RelativeLayout) findViewById(R.id.relay213);
        reLay214 = (RelativeLayout) findViewById(R.id.relay214);
        reLay215 = (RelativeLayout) findViewById(R.id.relay215);
        reLay216 = (RelativeLayout) findViewById(R.id.relay216);
        reLay211.setPadding(mChieuDai, 0, mChieuDai * 23, mChieuRong * 11);
        reLay212.setPadding(mChieuDai * 12, 0, mChieuDai * 12, mChieuRong * 11);
        reLay213.setPadding(mChieuDai * 23, 0, mChieuDai * 1, mChieuRong * 11);
        reLay214.setPadding(mChieuDai, mChieuRong * 11, mChieuDai * 23, 0);
        reLay215.setPadding(mChieuDai * 12, mChieuRong * 11, mChieuDai * 12, 0);
        reLay216.setPadding(mChieuDai * 23, mChieuRong * 11, mChieuDai * 1, 0);

        //end Layout
        rcCategory = (RecyclerView) findViewById(R.id.recycler1);
        rcApp = (RecyclerView) findViewById(R.id.recycler2);

        video = (VideoView) findViewById(R.id.videoView);
//        exoPlayer = (SimpleExoPlayerView) findViewById(R.id.videoView);

        ibtNext = (ImageButton) findViewById(R.id.imgNext);
        ibtPlay = (ImageButton) findViewById(R.id.imgPlay);
        ibtBack = (ImageButton) findViewById(R.id.imgBack);
        ibtPause = (ImageButton) findViewById(R.id.imgPause);
        ibtVolumeOf = (ImageButton) findViewById(R.id.imgVolumeOf);
        ibtVolumeOn = (ImageButton) findViewById(R.id.imgVolumeOn);
        ibtFull = (ImageButton) findViewById(R.id.imgFull);

        ibtNext.setOnClickListener(this);
        ibtPlay.setOnClickListener(this);
        ibtBack.setOnClickListener(this);
        ibtFull.setOnClickListener(this);
        ibtVolumeOf.setOnClickListener(this);
        ibtVolumeOn.setOnClickListener(this);
        ibtPause.setOnClickListener(this);

        tvTimeStart = (TextView) findViewById(R.id.tvTimeBegin);
        tvTimeEnd = (TextView) findViewById(R.id.tvTimeEnd);

        volume = new Volume();
        checkLink = new CheckLink();
        ibtPlay = (ImageButton) findViewById(R.id.imgPlay);
        ibtPlay.setOnClickListener(this);

        text = (TextView) findViewById(R.id.text1);
        text.setSelected(true);

        image1 = (ImageView) findViewById(R.id.img_tv);
        image2 = (ImageView) findViewById(R.id.img_phim);
        image3 = (ImageView) findViewById(R.id.img_nhac);
        image4 = (ImageView) findViewById(R.id.img_kara);
        image5 = (ImageView) findViewById(R.id.img_youtube);
        image6 = (ImageView) findViewById(R.id.img_store);
        imgView = (ImageView) findViewById(R.id.imgView);
        imgWeb= (ImageView) findViewById(R.id.imgWeb);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image6.setOnClickListener(this);
        imgWeb.setOnClickListener(this);

        int mChieuDaia = (mChieuDai * 5) / 8;
        int mChieuRonga = (mChieuRong * 3) / 4;
        image1.setPadding(mChieuDaia, mChieuRonga, mChieuDaia, mChieuRonga);
        image2.setPadding(mChieuDaia, mChieuRonga, mChieuDaia, mChieuRonga);
        image3.setPadding(mChieuDaia, mChieuRonga, mChieuDaia, mChieuRonga);
        image4.setPadding(mChieuDaia, mChieuRonga, mChieuDaia, mChieuRonga);
        image5.setPadding(mChieuDaia, mChieuRonga, mChieuDaia, mChieuRonga);
        image6.setPadding(mChieuDaia, mChieuRonga, mChieuDaia, mChieuRonga);

        imageMinus = (ImageView) findViewById(R.id.img_minus);
        imagePlus = (ImageView) findViewById(R.id.img_plus);
        imageMinus.setOnClickListener(this);
        imagePlus.setOnClickListener(this);

        mNgayDuongTxt = (TextView) findViewById(R.id.txt_duonglich);
        mNgayAmTxt = (TextView) findViewById(R.id.txt_amlich);
        mTxtTinh = (TextView) findViewById(R.id.txt_thanhpho);
        mTxtNhietDo = (TextView) findViewById(R.id.txt_nhietdo);

        text.setSelected(true);
        text.setText(mDatabaseHelper.getLinkTextQuangCao());
        imageCaiDat = (ImageView) findViewById(R.id.img_caidat);
        imageCaiDat.setOnClickListener(this);
        Map<String, String> today = LunarCalendar.getTodayInfo();
        mNgayDuongTxt.setText("Thứ " + today.get("thu") + ", " + today.get("daySolar") + "/" + today.get("monthSolar") + "/" + today.get("yearSolar"));
        mNgayAmTxt.setText(today.get("dayLunar") + "/" + today.get("monthLunar") + " " + today.get("can") + " " + today.get("chi"));
        final String todayFormated = today.get("yearSolar") + "-" + today.get("monthSolar") + "-" + today.get("daySolar") + " "
                + today.get("hour") + ":" + today.get("minute") + ":" + today.get("second");

        ThoiTiet thoiTiet = mDatabaseHelper.getThongTinThoiTiet(idThoiTiet);

        SharedPreferences sharedPreferencesThoiTiet = getSharedPreferences("thoitiet", MODE_PRIVATE);
        mTxtTinh.setText(sharedPreferencesThoiTiet.getString("tinh", "Hà nội"));
        mTxtNhietDo.setText(sharedPreferencesThoiTiet.getString("nhietdo", "25"));

        final SharedPreferences.Editor editor = sharedPreferencesThoiTiet.edit();
        editor.putString("tinh", thoiTiet.getTen());
        String url = "http://api.openweathermap.org/data/2.5/forecast?id=" + thoiTiet.getMaThoiTiet() + "&APPID=" + APIKEY + "&&units=metric";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray listThoiTiet = root.getJSONArray("list");
                    int currentPos = 0;
                    for (int i = 0; i < listThoiTiet.length(); i++) {
                        JSONObject thoiTiet = listThoiTiet.getJSONObject(i);
                        String time = thoiTiet.getString("dt_txt");
                        // Toast.makeText(getApplicationContext(), todayFormated + "==" + time, Toast.LENGTH_SHORT).show();

                        if (DuLieu.compareDate(todayFormated, time)) {
                            if (i == 0) {
                                currentPos = 0;
                            } else {
                                currentPos = i - 1;
                            }
                            thoiTiet = listThoiTiet.getJSONObject(currentPos);
                            String nhietDo = thoiTiet.getJSONObject("main").getString("temp");
                            String trangThai = thoiTiet.getJSONArray("weather").getJSONObject(0).getString("main");
                            mTxtNhietDo.setText(Math.round(Double.parseDouble(nhietDo)) + "");
                            editor.putString("nhietdo", Math.round(Double.parseDouble(nhietDo)) + "");
                            editor.commit();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
        mTxtTinh.setText(thoiTiet.getTen());

        dialog = new ProgressDialog(this);
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi ứng dụng tải dữ liệu");

        String url2 = DuLieu.URL + "/first_request_store.php";
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray root = new JSONArray(response);
                    dialog.show();
                    for (int i = 0; i < root.length(); i++) {
                        JSONObject capnhat = root.getJSONObject(i);
                        String isCapNhat = capnhat.getString("is_cap_nhat");
                        if (isCapNhat.equals("0")) {
                            break;
                        } else {
                            String loaiCapNhat = capnhat.getString("loai");
                            switch (loaiCapNhat) {
                                case "quangcao":
                                    JSONArray rootQC = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteQuangCao();
                                    for (int j = 0; j < rootQC.length(); j++) {
                                        JSONObject app = rootQC.getJSONObject(j);
                                        mDatabaseHelper.insertQuangCao(app.getString("id"), app.getString("noidung"), app.getString("loaiquangcaoid"));
                                    }
                                    break;
                                case "ungdung":
                                    JSONArray rootApp = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteListApp();
                                    for (int j = 0; j < rootApp.length(); j++) {
                                        JSONObject app = rootApp.getJSONObject(j);
                                        int install, update;
                                        install = update = 0;
                                        if (DuLieu.checkInstalledApplication(app.getString("ten"), TrangChu.this)) {
                                            install = 1;
                                            if (DuLieu.capNhatVersion(DuLieu.getPackageName(app.getString("ten"), TrangChu.this), app.getInt("version_code"), TrangChu.this)) {
                                                update = 1;
                                            } else {
                                                update = 0;
                                            }
                                        }
                                        mDatabaseHelper.insertApp(app.getString("id"), app.getString("ten")
                                                , install, DuLieu.URL_IMAGE + "/" + app.getString("icon")
                                                , app.getString("luotcai"), app.getString("version")
                                                , app.getString("des"), DuLieu.URL_FILE + "/" + app.getString("linkcai")
                                                , app.getString("rating"), app.getString("version_code"), update);
                                    }


                                    //  Toast.makeText(getApplicationContext(), mDatabaseHelper.testInsertApp() + " == max app", Toast.LENGTH_SHORT).show();
                                    break;
                                case "luotcai":
                                    break;
                                case "anhchitiet":
                                    JSONArray rootAnhChiTiet = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteAnhChiTiet();
                                    for (int j = 0; j < rootAnhChiTiet.length(); j++) {
                                        JSONObject app = rootAnhChiTiet.getJSONObject(j);
                                        mDatabaseHelper.insertAnhChiTiet(app.getString("id"), app.getString("ungdungid"), DuLieu.URL_IMAGE +
                                                "/" + app.getString("ten"));
                                    }
                                    break;
                                case "theloai_ungdung":
                                    JSONArray rootTheLoaiUngDung = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteTheLoaiUngDung();
                                    for (int j = 0; j < rootTheLoaiUngDung.length(); j++) {
                                        JSONObject app = rootTheLoaiUngDung.getJSONObject(j);
                                        mDatabaseHelper.insertTheLoaiUngDung(app.getString("id"), app.getString("theloaiid"), app.getString("ungdungid"));
                                    }
                                    break;
                                case "theloai":
                                    JSONArray rootTheLoai = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteTheLoai();
                                    for (int j = 0; j < rootTheLoai.length(); j++) {
                                        JSONObject app = rootTheLoai.getJSONObject(j);
                                        mDatabaseHelper.insertTheLoai(app.getString("id"), app.getString("ten"), app.getString("soluong"), app.getString("icon"));
                                    }
                                    break;
                                case "capnhat":
                                    JSONArray rootCapNhat = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteCapNhat();
                                    JSONObject app = rootCapNhat.getJSONObject(0);
                                    mDatabaseHelper.insertCapNhat(app.getString("id"), app.getString("id"));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                text.setText(mDatabaseHelper.getLinkTextQuangCao());

                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        indexVideo++;
                        setVideoOrImager(listvideo.get(indexVideo));
//                        video.setVideoPath(DuLieu.splitLinkVideoWeb(mDatabaseHelper.getLinkVideoQuangCao())[0]);

                    }
                });
                setVideoOrImager(listvideo.get(indexVideo));


                //   Toast.makeText(getApplicationContext(), mDatabaseHelper.getListVideoQuangCao().size() + "", Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap<>();
                values.put("capnhatid", mDatabaseHelper.getIdCapNhat());
                return values;
            }
        };
        requestQueue.add(stringRequest2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                text.setSelected(true);
                if ((didIndex >= 4) && (didIndex < main)) {
                    willIndex = didIndex - 4;
                    changeListItemBackGround(didIndex, willIndex);
                } else if ((didIndex >= main) && (didIndex < main + cates.size())) {
                    rcCategory.getChildAt(didIndex - main).callOnClick();
                    if ((didIndex - 4) < main) {
                        didIndex = didIndex - main;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    } else {
                        didIndex = main - 1;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    }
                } else if ((didIndex >= main + 1 + cates.size()) && (didIndex < main + 1 + cates.size() + listApps.get(demdsApp).size())) {
                    rcApp.getChildAt(didIndex - main - 1 - cates.size()).setBackgroundResource(R.drawable.none);
                    didIndex = indexChuDe + main;
                } else if (didIndex == main + 1 + cates.size() + listApps.get(demdsApp).size()) {
                    imagePlus.setImageResource(R.drawable.ic_plus1);
                    didIndex = indexChuDe + main;
                    rcCategory.getChildAt(didIndex - main).callOnClick();
                } else if (didIndex == main + cates.size()) {
                    imageMinus.setImageResource(R.drawable.ic_minus1);
                    didIndex = indexChuDe + main;
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                text.setSelected(true);
                if ((didIndex >= 0) && (didIndex < main - 4)) {
                    willIndex = didIndex + 4;
                    changeListItemBackGround(didIndex, willIndex);
                } else if ((didIndex >= main - 4) && (didIndex < main)) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex = indexChuDe + main;
                    rcCategory.getChildAt(didIndex - main).callOnClick();
                } else if ((didIndex >= main) && (didIndex < main + cates.size())) {
                    indexChuDe = didIndex - main;
                    didIndex = main + 1 + cates.size();
                    rcApp.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                text.setSelected(true);
                if ((didIndex > 0) && (didIndex < main + 1)) {
                    if (didIndex == main) {
                        didIndex--;
                        rcCategory.getChildAt(0).setBackgroundResource(R.drawable.none);
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                        return true;
                    }
                    willIndex = didIndex - 1;
                    changeListItemBackGround(didIndex, willIndex);
                } else if ((didIndex >= main + 1) && (didIndex < (main + 1 + cates.size()))) {
                    if (didIndex == (main + cates.size())) {
                        imageMinus.setImageResource(R.drawable.ic_minus1);
                    }
                    didIndex--;
                    rcCategory.getChildAt(didIndex - main).callOnClick();
                    indexChuDe = didIndex - main;
                } else if (didIndex == main + 1 + cates.size()) {
                    imageMinus.setImageResource(R.drawable.ic_minus);
                    rcApp.getChildAt(0).setBackgroundResource(R.drawable.none);
                    didIndex--;
                } else if ((didIndex >= (main + 2 + cates.size())) && (didIndex < (main + 1 + cates.size() + listApps.get(demdsApp).size()))) {
                    rcApp.getChildAt(didIndex - main - 1 - cates.size()).setBackgroundResource(R.drawable.none);
                    imagePlus.setImageResource(R.drawable.ic_plus1);
                    didIndex--;
                    rcApp.getChildAt(didIndex - main - 1 - cates.size()).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main + 1 + cates.size() + listApps.get(demdsApp).size()) {
                    imagePlus.setImageResource(R.drawable.ic_plus1);
                    didIndex--;
                    rcApp.getChildAt(didIndex - main - 1 - cates.size()).setBackgroundResource(R.drawable.border_pick);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                text.setSelected(true);
                if (didIndex < main - 1) {
                    willIndex = didIndex + 1;
                    changeListItemBackGround(didIndex, willIndex);
                } else if (didIndex <= (main - 1 + cates.size())) {
                    if (didIndex == main - 1 + cates.size()) {
                        didIndex = main - 1;
                    }
                    listItem.get(main - 1).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    rcCategory.getChildAt(didIndex - main).callOnClick();
                    indexChuDe = didIndex - main;
                } else if (didIndex == main + cates.size()) {
                    imageMinus.setImageResource(R.drawable.ic_minus1);
                    didIndex++;
                    rcApp.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex < (main + cates.size() + listApps.get(demdsApp).size())) {
                    if (didIndex != main + cates.size()) {
                        rcApp.getChildAt(didIndex - main - cates.size() - 1).setBackgroundResource(R.drawable.none);
                    }
                    didIndex++;
                    rcApp.getChildAt(didIndex - main - 1 - cates.size()).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main + cates.size() + listApps.get(demdsApp).size()) {
                    rcApp.getChildAt(didIndex - main - 1 - cates.size()).setBackgroundResource(R.drawable.none);
                    imagePlus.setImageResource(R.drawable.ic_plus);
                    didIndex++;
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (didIndex < main) {
                    listItem.get(didIndex).callOnClick();
                } else if (didIndex < (main + cates.size())) {
                    rcCategory.getChildAt(didIndex - main).callOnClick();
                } else if (didIndex == main + 1 + cates.size() + listApps.get(demdsApp).size()) {
                    listItem.get(listItem.size() - 1).callOnClick();
                } else if (didIndex == main + cates.size()) {
                    listItem.get(main + cates.size()).callOnClick();
                } else {
                    rcApp.getChildAt(didIndex - main - 1 - cates.size()).callOnClick();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgWeb:
                Uri uri = Uri.parse("http://www.bongdaso.com/news.aspx");
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;

            case R.id.imgFull:
                intent = new Intent(getBaseContext(), VideoFull.class);
                intent.putExtra("index", indexVideo);
                intent.putExtra("list", listvideo);
                String str = listvideo.get(indexVideo);

                // độ dài video đang chạy
                int timepause = video.getCurrentPosition();
                intent.putExtra("timePause", timepause);
                startActivity(intent);
                break;
            case R.id.imgVolumeOf:
                ibtVolumeOn.setVisibility(View.VISIBLE);
                ibtVolumeOf.setVisibility(View.GONE);

                volume.MuteAudio(this);
                break;

            case R.id.imgVolumeOn:
                ibtVolumeOn.setVisibility(View.GONE);
                ibtVolumeOf.setVisibility(View.VISIBLE);
                volume.UnMuteAudio(this);

                break;
//
            case R.id.imgPause:
                ibtPlay.setVisibility(View.VISIBLE);
                ibtPause.setVisibility(View.GONE);
                video.pause();
                break;
            case R.id.imgPlay:
                ibtPlay.setVisibility(View.GONE);
                ibtPause.setVisibility(View.VISIBLE);
                video.start();
                break;

            case R.id.imgNext:
                if (indexVideo == (listvideo.size() - 1)) {
                    indexVideo = 0;
                } else indexVideo++;

                setVideoOrImager(listvideo.get(indexVideo));

                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        indexVideo++;
                        setVideoOrImager(listvideo.get(indexVideo));
                    }
                });
                break;
//
            case R.id.imgBack:
                if (indexVideo == 0) {
                    indexVideo = listvideo.size() - 1;
                } else indexVideo--;

                setVideoOrImager(listvideo.get(indexVideo));

                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        indexVideo++;
                        setVideoOrImager(listvideo.get(indexVideo));
                    }
                });
                break;

            case R.id.img_caidat:
                TrangChu.this.startActivityForResult(new Intent(Settings.ACTION_SETTINGS), REQUEST_SETTINGS);
                break;

            case R.id.img_tv:
                Intent i = new Intent(TrangChu.this, DanhSach.class);
                startActivity(i);
                break;

            case R.id.img_phim:
                Intent i1 = new Intent(TrangChu.this, DanhSach.class);
                startActivity(i1);
                break;

            case R.id.img_nhac:
                Intent i2 = new Intent(TrangChu.this, DanhSach.class);
                startActivity(i2);
                break;

            case R.id.img_kara:
                Intent i3 = new Intent(TrangChu.this, DanhSach.class);
                startActivity(i3);
                break;

            case R.id.img_youtube:
                Intent i4 = manager.getLaunchIntentForPackage("com.google.android.apps.youtube.kids");
                startActivity(i4);
                break;

            case R.id.img_store:
                /*Intent i5 = manager.getLaunchIntentForPackage("com.store.kiwi.kiwistore");
                startActivity(i5);*/
                launchApp("com.store.kiwi.kiwistore");
                break;

            case R.id.relay113:
                imageCaiDat.callOnClick();
                break;

            case R.id.img_plus:
                if (listApps.size() - 1 > demdsApp) {
                    imagePlus.setImageResource(R.drawable.ic_plus1);
                    demdsApp++;
                    listapp = new UngDungAdapter(getApplicationContext(), listApps.get(demdsApp));
                    rcApp.setAdapter(listapp);
                    didIndex = 12 + cates.size();
                    imageMinus.setImageResource(R.drawable.ic_minus);
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đã ở cuối danh sách ứng dụng", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_minus:
                if (0 < demdsApp) {
                    demdsApp--;
                    listapp = new UngDungAdapter(getApplicationContext(), listApps.get(demdsApp));
                    rcApp.setAdapter(listapp);
                    didIndex = 12 + cates.size();
                    imageMinus.setImageResource(R.drawable.ic_minus);
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đang ở danh sách các ứng dụng đầu tiên ", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SETTINGS:
                Log.d(TAG, "settings change");
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public static class AppClick implements View.OnClickListener {

        private final Context context;

        public AppClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int position = rcApp.getChildPosition(v);
            Intent i = manager.getLaunchIntentForPackage(listApps.get(demdsApp).get(position).getNameApp().toString());
            context.startActivity(i);
        }
    }

    public void addNavigationItem() {
        listItem.add(text);
        listItem.add(reLay111);
        listItem.add(reLay112);
        listItem.add(reLay113);
        listItem.add(image1);
        listItem.add(image2);
        listItem.add(image3);
        listItem.add(reLay222);
        listItem.add(image4);
        listItem.add(image5);
        listItem.add(image6);
        listItem.add(reLay222);

        int soChuDe = 0;
        for (ChuDe c : cates) {
            listItem.add(rcCategory.getChildAt(soChuDe));
            soChuDe++;
        }

        listItem.add(imageMinus);

        int soUngDung = 0;
        for (UngDung app : listApps.get(demdsApp)) {
            listItem.add(rcApp.getChildAt(soUngDung));
        }

        listItem.add(imagePlus);
    }

    public void changeListItemBackGround(int i, int j) {
        listItem.get(i).setBackgroundResource(R.drawable.none);
        listItem.get(j).setBackgroundResource(R.drawable.border_pick);
        didIndex = willIndex;
    }

    public void launchApp(String packageName) {
        Intent intent = new Intent();
        intent.setPackage(packageName);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));

        if (resolveInfos.size() > 0) {
            ResolveInfo launchable = resolveInfos.get(0);
            ActivityInfo activity = launchable.activityInfo;
            ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                    activity.name);
            Intent i = new Intent(Intent.ACTION_MAIN);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);

            startActivity(i);
        }
    }

    private void setVideoOrImager(String check) {

        int position = checkLink.CheckLinkURL(check);

        if (position == 1) {
            imgView.setVisibility(View.VISIBLE);
            video.setVisibility(View.GONE);

            Glide.with(this)
                    .load(listvideo.get(indexVideo))
                    .into(imgView);

        } else if (position == 2) {
            imgView.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);

//            handler.postDelayed(new ViewUpdater(checkLink.stringForTime(video.getCurrentPosition()),tvTimeEnd),1000);


            // đọ dài của video
            mp = MediaPlayer.create(this, Uri.parse(check));
            int duration = mp.getDuration();
            mp.release();

            tvTimeEnd.setText(checkLink.stringForTime(duration));
            video.setVideoPath(listvideo.get(indexVideo));
            video.start();

            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    indexVideo++;
                    setVideoOrImager(listvideo.get(indexVideo));
                }
            });
        } else if (position == 3) {
            imgView.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);

            MediaController mc = new MediaController(this);
            video.setMediaController(mc);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    video.setVideoURI(Uri.parse(listvideo.get(indexVideo)));
                    video.start();
                }
            });
        }
    }

    private class ViewUpdater implements Runnable {
        private String mString;
        private TextView mtv;


        public ViewUpdater(String string, TextView _mtv) {
            mString = string;
            mtv = _mtv;
        }

        @Override
        public void run() {
            mtv.setText(mString);
        }
    }

//
//    private final Runnable updateProgressAction = new Runnable() {
//        @Override
//        public void run() {
//            updateProgress();
//        }
//    };
//
//    private void updateProgress() {
//
//        long position = video == null ? 0 : video.getCurrentPosition();
//
//        if (!dragging) {
//            tvTimeStart.setText(checkLink.stringForTime(position));
//        }
//        postDelayed(updateProgressAction, 1000);
//    }
}
