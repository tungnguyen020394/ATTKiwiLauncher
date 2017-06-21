package com.att.kiwilauncher;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.att.kiwilauncher.model.ThoiTiet;
import com.att.kiwilauncher.util.CheckLink;
import com.att.kiwilauncher.util.Define;
import com.att.kiwilauncher.util.Volume;
import com.att.kiwilauncher.view.VideoFull;
import com.att.kiwilauncher.xuly.DuLieu;
import com.att.kiwilauncher.xuly.LunarCalendar;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrangChu extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    public final static String APIKEY = "1fd660e2a27afad8b71405f654997a62";
    int chieuDai, chieuRong, didIndex = 0, willIndex, indexChuDe = 0, mChieuDai, mChieuRong, main = 12, position,bonusmain = 6;
    RelativeLayout reLay1, reLay2, reLay3, reLay4, reLay111, reLay112, reLay113, reLay11,
            reLay21, reLay22, reLay222, reLay211, reLay212, reLay213, reLay214, reLay215, reLay216, reLay13, reLay12,
            reLay2221,reLay121;
    ArrayList<View> listItem;
    TextView text, mNgayAmTxt, mNgayDuongTxt, mTxtTinh, mTxtNhietDo;
    VideoView video;
    ImageView image1, image2, image3, image4, image5, image6,
            imageCaiDat, imageMinus, imagePlus;
    private AlertDialog mNetworkConnectionNoticeDialog;
    private AlertDialog.Builder mNetworkConnectionNoticeDialogBuilder;
    RecyclerView rcCategory;
    static RecyclerView rcApp;
    public static UngDungAdapter listapp;
    static List<UngDung> apps;
    List<ChuDe> cates;
    public static List<List<UngDung>> listApps;
    public static List<UngDung> listAppBottom;
    ArrayList<String> listvideo;
    private RequestQueue requestQueue;
    static PackageManager manager;
    public static View.OnClickListener appClick;
    public static final int REQUEST_SETTINGS = 101;
    public static int demdsApp = 0;
    int indexVideo = 0;
    private static final String TAG = "TrangChu";
    DatabaseHelper mDatabaseHelper;
    private ProgressDialog dialog;
    LinearLayout linNear1;
    Volume volume;
    int intVolume;
    ImageView imgView, imgWeb;
    ImageButton ibtNext, ibtPlay, ibtBack, ibtVolumeOn, ibtFull;
    TextView tvTimeStart, tvTimeEnd, tvTime;
    CheckLink checkLink;
    MediaPlayer mp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorfull;
    private int timePause = 0;
    private boolean dragging, playing = true, mute = true;
    private AudioManager audioManager;
    Intent intent;
    Handler handler = new Handler();
    private int currentApiVersion;

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
        cates = mDatabaseHelper.getListChuDe();
        //   Toast.makeText(this, cates.size() + "", Toast.LENGTH_LONG).show();
        rcCategory.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcCategory.setLayoutManager(layoutManager1);
        final ChuDeAdapter categoryAdapter = new ChuDeAdapter(this, cates);
        rcCategory.setAdapter(categoryAdapter);

        // Load App
        manager = getPackageManager();
        apps = new ArrayList<>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        //   List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        listApps = new ArrayList();
        List<UngDung> checkedList = new ArrayList<>();
        checkedList = mDatabaseHelper.getListUngDung(cates.get(0));
        List<UngDung> tmpList = new ArrayList<>();
        //   listApps.add(mDatabaseHelper.getListUngDung(cates.get(0)));
        for (int j = 1; j <= checkedList.size(); j++) {
            UngDung ungDung = new UngDung();
            ungDung.setNameApp(checkedList.get(j - 1).getNameApp());
            ungDung.setIcon(checkedList.get(j - 1).getIcon());
            ungDung.setId(checkedList.get(j - 1).getId());
            tmpList.add(ungDung);
            if (j % 7 == 0 || j == checkedList.size()) {
                listApps.add(tmpList);
                tmpList = new ArrayList<>();
                tmpList.clear();
            }
        }
        //Toast.makeText(this, listApps.size() + "s" + listApps.get(0).size(), Toast.LENGTH_SHORT).show();
        listAppBottom.addAll(listApps.get(demdsApp));

        rcApp.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        rcApp.setLayoutManager(gridLayoutManager);
        listapp = new UngDungAdapter(this, listAppBottom);
        rcApp.setAdapter(listapp);

//        listvideo = mDatabaseHelper.getListVideoQuangCao();

        // video
//        if (!mDatabaseHelper.getLinkVideoQuangCao().equals("")) {
//        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                indexVideo++;
//                setVideoOrImager(listvideo.get(indexVideo));
////                    setVideoOrImager(Define.URL_LINK_PLAY);
//            }
//        });
        //video.setVideoPath(listvideo.get(indexVideo));
        setVideoOrImager(listvideo.get(indexVideo));
//        }

        //audio
        intVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_MUTE,0);
        volume.MuteAudio(this);


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
                cates.clear();
                cates.addAll(mDatabaseHelper.getListChuDe());
                categoryAdapter.notifyDataSetChanged();
                listAppBottom.clear();
                listApps.clear();
                List<UngDung> checkedList;
                checkedList = mDatabaseHelper.getListUngDung(cates.get(0));
                List<UngDung> tmpList = new ArrayList<>();
                //   listApps.add(mDatabaseHelper.getListUngDung(cates.get(0)));
                for (int j = 1; j <= checkedList.size(); j++) {
                    UngDung ungDung = new UngDung();
                    ungDung.setNameApp(checkedList.get(j - 1).getNameApp());
                    ungDung.setIcon(checkedList.get(j - 1).getIcon());
                    ungDung.setId(checkedList.get(j - 1).getId());
                    tmpList.add(ungDung);
                    if (j % 7 == 0 || j == checkedList.size()) {
                        listApps.add(tmpList);
                        tmpList = new ArrayList<>();
                        tmpList.clear();
                    }
                }
                demdsApp = 0;
                //    Toast.makeText(TrangChu.this, listApps.size() + "s"+listApps.get(0).size() , Toast.LENGTH_SHORT).show();
                listAppBottom.addAll(listApps.get(demdsApp));
                //listAppBottom.addAll(mDatabaseHelper.getListUngDung(cates.get(0)));
                listapp.notifyDataSetChanged();
//                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        indexVideo++;
//                      //  video.setVideoPath(listvideo.get(indexVideo));
//                        video.setVideoPath(DuLieu.splitLinkVideoWeb(mDatabaseHelper.getLinkVideoQuangCao())[0]);
//                        video.start();
//                    }
//                });

                //  video.start();

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
    protected void onResume() {
        super.onResume();

        if (DuLieu.hasInternetConnection(TrangChu.this)) {
            setVideoOrImager(listvideo.get(indexVideo));
        } else {

            Toast.makeText(getApplicationContext(), "Mất kết nối mạng...", Toast.LENGTH_LONG).show();
        }
    }

    private void addControls() {
        mNetworkConnectionNoticeDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        mNetworkConnectionNoticeDialogBuilder.setTitle("Lỗi kết nối mạng");
        mNetworkConnectionNoticeDialogBuilder.setMessage("Vui lòng kiểm tra lại mạng kết nối ...");
        mNetworkConnectionNoticeDialogBuilder.setPositiveButton("Kiểm tra", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                /*Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                startActivity(intent);*/
                /*Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                ComponentName cName = new ComponentName("com.android.phone","com.android.phone.Settings");
                intent.setComponent(cName);
                startActivity(intent);*/
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        mNetworkConnectionNoticeDialog = mNetworkConnectionNoticeDialogBuilder.create();
        if (!DuLieu.hasInternetConnection(this)) {
            mNetworkConnectionNoticeDialog.show();
        }

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
        listvideo = new ArrayList<>();
        listAppBottom = new ArrayList<>();

        listvideo.add(Define.URL_LINK_PLAY);
        listvideo.add(Define.URL_LINK_IMG01);
        listvideo.add(Define.URL_LINK_BACK);
        listvideo.add(Define.URL_LINK_IMG02);

        // reLaytive layout
        reLay1 = (RelativeLayout) findViewById(R.id.relay1);
        reLay2 = (RelativeLayout) findViewById(R.id.relay2);
        reLay3 = (RelativeLayout) findViewById(R.id.relay3);
        reLay4 = (RelativeLayout) findViewById(R.id.relay4);

        reLay13 = (RelativeLayout) findViewById(R.id.relay13);
        reLay12 = (RelativeLayout) findViewById(R.id.relay12);
        reLay11 = (RelativeLayout) findViewById(R.id.relay11);
        reLay111 = (RelativeLayout) findViewById(R.id.relay111);
        reLay112 = (RelativeLayout) findViewById(R.id.relay112);
        reLay113 = (RelativeLayout) findViewById(R.id.relay113);
        reLay113.setOnClickListener(this);
        reLay121 = (RelativeLayout) findViewById(R.id.relay121);

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
        reLay211.setPadding(0, 0, mChieuDai * 23, mChieuRong * 12);
        reLay212.setPadding(mChieuDai * 11, 0, mChieuDai * 12, mChieuRong * 12);
        reLay213.setPadding(mChieuDai * 22, 0, mChieuDai * 1, mChieuRong * 12);
        reLay214.setPadding(0, mChieuRong * 12, mChieuDai * 23, 0);
        reLay215.setPadding(mChieuDai * 11, mChieuRong * 12, mChieuDai * 12, 0);
        reLay216.setPadding(mChieuDai * 22, mChieuRong * 12, mChieuDai * 1, 0);
        reLay2221 = (RelativeLayout) findViewById(R.id.relay2221);
        reLay2221.setOnClickListener(this);

        linNear1 = (LinearLayout) findViewById(R.id.linear1);

        //end Layout
        rcCategory = (RecyclerView) findViewById(R.id.recycler1);
        rcApp = (RecyclerView) findViewById(R.id.recycler2);

        video = (VideoView) findViewById(R.id.videoView);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


//        exoPlayer = (SimpleExoPlayerView) findViewById(R.id.videoView);

        ibtNext = (ImageButton) findViewById(R.id.imgNext);
        ibtPlay = (ImageButton) findViewById(R.id.imgPlay);
        ibtBack = (ImageButton) findViewById(R.id.imgBack);
        ibtVolumeOn = (ImageButton) findViewById(R.id.imgVolumeOn);
        ibtFull = (ImageButton) findViewById(R.id.imgFull);

        ibtNext.setOnClickListener(this);
        ibtPlay.setOnClickListener(this);
        ibtBack.setOnClickListener(this);
        ibtFull.setOnClickListener(this);
        ibtVolumeOn.setOnClickListener(this);

        tvTimeStart = (TextView) findViewById(R.id.tvTimeBegin);
        tvTimeEnd = (TextView) findViewById(R.id.tvTimeEnd);
        tvTime = (TextView) findViewById(R.id.tvTime);

        volume = new Volume();
        checkLink = new CheckLink();
        sharedPreferences=getSharedPreferences("volume",MODE_PRIVATE);
        editorfull =sharedPreferences.edit();

        text = (TextView) findViewById(R.id.text1);
        text.setSelected(true);

        image1 = (ImageView) findViewById(R.id.img_tv);
        image2 = (ImageView) findViewById(R.id.img_phim);
        image3 = (ImageView) findViewById(R.id.img_nhac);
        image4 = (ImageView) findViewById(R.id.img_kara);
        image5 = (ImageView) findViewById(R.id.img_youtube);
        image6 = (ImageView) findViewById(R.id.img_store);
        imgView = (ImageView) findViewById(R.id.imgView);
        imgWeb = (ImageView) findViewById(R.id.imgWeb);
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
        requestQueue = Volley.newRequestQueue(this);
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

//                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        if (indexVideo == listvideo.size() - 1) {
//                            indexVideo = -1;
//                        }
//                        indexVideo++;
//                        setVideoOrImager(listvideo.get(indexVideo));
//                    }
//                });
                setVideoOrImager(listvideo.get(indexVideo));
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
                    if (didIndex == 7 || didIndex == 11) {
                        listItem.get(didIndex).callOnClick();
                    }
                } else if (didIndex >= main && didIndex < main + bonusmain) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex = 1;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                }else if ((didIndex >= main + bonusmain) && (didIndex < main + bonusmain + cates.size())) {
                    rcCategory.getChildAt(didIndex - main - bonusmain).callOnClick();
                    if ((didIndex - 4) < main + bonusmain) {
                        didIndex = didIndex - cates.size() - bonusmain;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                        if (didIndex == 7 || didIndex == 11) {
                            listItem.get(didIndex).setBackgroundResource(R.drawable.border_video);
                        }
                    } else {
                        didIndex = main - 4;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    }
                } else if ((didIndex >= main + 1 + cates.size() + bonusmain) && (didIndex < main + bonusmain + 1 + cates.size() + listApps.get(demdsApp).size())) {
                    rcApp.getChildAt(didIndex - main - 1 - cates.size() - bonusmain).setBackgroundResource(R.drawable.none);
                    didIndex = indexChuDe + main + bonusmain;
                } else if (didIndex == main + bonusmain + 1 + cates.size() + listApps.get(demdsApp).size()) {
                    imagePlus.setImageResource(R.drawable.ic_plus1);
                    didIndex = indexChuDe + main + bonusmain;
                    rcCategory.getChildAt(indexChuDe).callOnClick();
                } else if (didIndex == main + cates.size() + bonusmain) {
                    imageMinus.setImageResource(R.drawable.ic_minus1);
                    didIndex = indexChuDe + main + bonusmain;
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                text.setSelected(true);
                if ((didIndex >= 0) && (didIndex < main - 4)) {
                    willIndex = didIndex + 4;
                    changeListItemBackGround(didIndex, willIndex);
                    if (didIndex == 7 || didIndex == 11) {
                        listItem.get(didIndex).callOnClick();
                    }
                } else if ((didIndex >= main - 4) && (didIndex < main + bonusmain)) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    if (didIndex == main - 1 + bonusmain) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_video);
                    }
                    didIndex = indexChuDe + main + bonusmain;
                    rcCategory.getChildAt(indexChuDe).callOnClick();
                } else if ((didIndex >= main + bonusmain) && (didIndex < main + bonusmain + cates.size())) {
                    indexChuDe = didIndex - main - bonusmain;
                    didIndex = main + 1 + cates.size() + bonusmain;
                    rcApp.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                text.setSelected(true);
                if ((didIndex > 0) && (didIndex < main + 1)) {
                    if (didIndex == main) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                        didIndex--;
                    } else if ( didIndex == 8) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                        didIndex = 7;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    }
                    willIndex = didIndex - 1;
                    changeListItemBackGround(didIndex, willIndex);
                    if (didIndex == 7 || didIndex == 11) {
                        listItem.get(didIndex).callOnClick();
                    }
                } else if (didIndex >= main
                        && didIndex < main + bonusmain) {
                    if (didIndex != main) {
                        if (listItem.get(didIndex) instanceof ImageButton) {
                            ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorWhite));
                        }
                        if (didIndex == main + 4 && position == 1) {
                            didIndex--;
                        }
                        didIndex--;
                        if (listItem.get(didIndex) instanceof ImageButton) {
                            ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorcatenew));
                        }
                        if (didIndex == 0 && listItem.get(didIndex) instanceof ImageButton) ((ImageButton) listItem.get(didIndex)).setImageResource(R.drawable.ic_website);
                    } else {
                        if (didIndex == 0 && listItem.get(didIndex) instanceof ImageButton) ((ImageButton) listItem.get(didIndex)).setImageResource(R.drawable.ic_website);
                        didIndex = 7;
                    }
                } else if ((didIndex >= main + 1 + bonusmain) && (didIndex < (main + 1 + cates.size() + bonusmain))) {
                    if (didIndex == (main + cates.size() + bonusmain)) {
                        imageMinus.setImageResource(R.drawable.ic_minus1);
                    }
                    didIndex--;
                    rcCategory.getChildAt(didIndex - main - bonusmain).callOnClick();
                    indexChuDe = didIndex - main - bonusmain;
                } else if ( didIndex == main + bonusmain){
                    indexChuDe = 0;
                    didIndex = 10;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main + 1 + cates.size() + bonusmain) {
                    imageMinus.setImageResource(R.drawable.ic_minus);
                    rcApp.getChildAt(0).setBackgroundResource(R.drawable.none);
                    didIndex--;
                } else if ((didIndex >= (main + 2 + cates.size() + bonusmain)) && (didIndex < (main + bonusmain + 1 + cates.size() + listApps.get(demdsApp).size()))) {
                    rcApp.getChildAt(didIndex - main - 1 - cates.size() - bonusmain).setBackgroundResource(R.drawable.none);
                    imagePlus.setImageResource(R.drawable.ic_plus1);
                    didIndex--;
                    rcApp.getChildAt(didIndex - main - 1 - cates.size() - bonusmain).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main + cates.size() + listApps.get(demdsApp).size() + 1 + bonusmain) {
                    imagePlus.setImageResource(R.drawable.ic_plus1);
                    didIndex--;
                    rcApp.getChildAt(didIndex - main - 1 - cates.size() - bonusmain).setBackgroundResource(R.drawable.border_pick);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                text.setSelected(true);
                if (didIndex < main - 1) {
                    willIndex = didIndex + 1;
                    changeListItemBackGround(didIndex, willIndex);
                    if (didIndex == 7 || didIndex == 11) {
                        listItem.get(didIndex).callOnClick();
                    }
                } else if (didIndex >= main
                        && didIndex < main + bonusmain) {
                    if (didIndex != main + bonusmain - 1) {
                        if (listItem.get(didIndex) instanceof ImageButton) {
                            ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorWhite));
                        }

                        if (didIndex == 0 && listItem.get(didIndex) instanceof ImageButton) ((ImageButton) listItem.get(didIndex)).setImageResource(R.drawable.ic_web);

                        if (didIndex == main + 2 && position == 1) {
                            didIndex++;
                        }
                        if (didIndex == main + 4 && position == 1) {
                            listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                            didIndex = 8;
                            return true;
                        }
                        didIndex++;
                        if (listItem.get(didIndex) instanceof ImageButton) {
                            ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorcatenew));
                        }
                    } else {
                        if (listItem.get(didIndex) instanceof ImageButton) {
                            ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorWhite));
                        }
                        didIndex = 8;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    }
                } else if (didIndex <= (main - 1 + cates.size() + bonusmain)) {
                    if (didIndex == main - 1 + cates.size() + bonusmain) {
                        didIndex = main - 1 + bonusmain;
                    }
                    listItem.get(main - 1).setBackgroundResource(R.drawable.border_video);
                    didIndex++;
                    rcCategory.getChildAt(didIndex - main - bonusmain).callOnClick();
                    indexChuDe = didIndex - main - bonusmain;
                } else if (didIndex == main + cates.size() + bonusmain) {
                    imageMinus.setImageResource(R.drawable.ic_minus1);
                    didIndex++;
                    rcApp.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex < (main + cates.size() + listApps.get(demdsApp).size() + bonusmain)) {
                    if (didIndex != main + cates.size() + bonusmain) {
                        rcApp.getChildAt(didIndex - main - cates.size() - 1 - bonusmain).setBackgroundResource(R.drawable.none);
                    }
                    didIndex++;
                    rcApp.getChildAt(didIndex - main - 1 - cates.size() - bonusmain).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main + cates.size() + listApps.get(demdsApp).size() + bonusmain) {
                    rcApp.getChildAt(didIndex - main - cates.size() - 1 - bonusmain).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    imagePlus.setImageResource(R.drawable.ic_plus);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (didIndex < main + bonusmain) {
                    listItem.get(didIndex).callOnClick();
                } else if (didIndex < (main + cates.size() + bonusmain)) {
                    rcCategory.getChildAt(didIndex - main - bonusmain).callOnClick();
                } else if (didIndex == main + 1 + cates.size() + listApps.get(demdsApp).size() + bonusmain) {
                    listItem.get(listItem.size() - 1).callOnClick();
                } else if (didIndex == main + cates.size() + bonusmain) {
                    listItem.get(main + cates.size() + bonusmain).callOnClick();
                } else {
                    rcApp.getChildAt(didIndex - main - 1 - cates.size() - bonusmain).callOnClick();
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
                intent.putExtra("mute",mute);
                editorfull.putInt("volume",intVolume);
                editorfull.commit();
                startActivity(intent);
                break;

            case R.id.imgVolumeOn:
                if (mute == true) {
                    ibtVolumeOn.setImageResource(R.drawable.ic_volumeon);

                    volume.UnMuteAudio(this,intVolume);
//                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC,false);
                    mute = false;
                    editorfull.putInt("volume",intVolume);
                } else {

                    mute = true;
//                    volume.MuteAudio(this);
//                    audioManager.setStreamMute(AudioManager.STREAM_MUSIC,true);

                    editorfull.putInt("volume",intVolume);
                }
                editorfull.commit();
                break;

            case R.id.imgPlay:
                if (playing == false) {
                    ibtPlay.setImageResource(R.drawable.ic_pause);
                    video.start();
                    playing = true;
                } else {
                    ibtPlay.setImageResource(R.drawable.ic_playvideo);
                    video.pause();
                    playing = false;
                }
                break;

            case R.id.imgNext:
                handler.removeCallbacks(nextvideo);
                if (indexVideo == listvideo.size() - 1) indexVideo = 0;
                else indexVideo++;
                setVideoOrImager(listvideo.get(indexVideo));

                break;
//
            case R.id.imgBack:
                handler.removeCallbacks(nextvideo);
                if (indexVideo == 0) indexVideo = (listvideo.size() - 1);
                else indexVideo--;
                setVideoOrImager(listvideo.get(indexVideo));
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
                Toast.makeText(getApplicationContext(),chieuDai + " -" + chieuRong,Toast.LENGTH_SHORT).show();
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

            case R.id.relay2221:
                didIndex = 12;
                if (listItem.get(didIndex) instanceof ImageView) ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_web);
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
            try {
                context.startActivity(i);
            } catch (Exception e) {
                Intent intent = manager.getLaunchIntentForPackage("com.store.kiwi.kiwistore");
                context.startActivity(intent);
            }
        }
    }

    public void addNavigationItem() {
        listItem.add(reLay121);
        listItem.add(reLay111);
        listItem.add(reLay112);
        listItem.add(reLay113);
        listItem.add(image1);
        listItem.add(image2);
        listItem.add(image3);
        listItem.add(reLay2221);
        listItem.add(image4);
        listItem.add(image5);
        listItem.add(image6);
        listItem.add(reLay2221);

        listItem.add(imgWeb);
        listItem.add(ibtFull);
        listItem.add(ibtBack);
        listItem.add(ibtPlay);
        listItem.add(ibtNext);
        listItem.add(ibtVolumeOn);

        int soChuDe = 0;
        for (ChuDe c : cates) {
            listItem.add(rcCategory.getChildAt(soChuDe));
            soChuDe++;
        }

        listItem.add(imageMinus);

        int soUngDung = 0;
        for (UngDung app : listApps.get(0)) {
            listItem.add(rcApp.getChildAt(soUngDung));
        }

        listItem.add(imagePlus);
    }

    public void changeListItemBackGround(int i, int j) {
        listItem.get(i).setBackgroundResource(R.drawable.none);
        if (i == 0) listItem.get(i).setBackgroundResource(R.drawable.border_text);
        if (i == 7 || i == 11) {
            listItem.get(i).setBackgroundResource(R.drawable.border_video);
        }
        listItem.get(j).setBackgroundResource(R.drawable.border_pick);
        if (j == 7 || j == 11) {
            listItem.get(j).setBackgroundResource(R.drawable.border_video);
        }
        if (j == 0) listItem.get(j).setBackgroundResource(R.drawable.border_textpick);
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

        position = checkLink.CheckLinkURL(check);

        if (position == 1) {
            imgView.setVisibility(View.VISIBLE);
            ibtPlay.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            ibtVolumeOn.setVisibility(View.GONE);
            tvTimeStart.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);

            tvTimeEnd.setText("   ");
            Glide.with(this)
                    .load(listvideo.get(indexVideo))
                    .into(imgView);

            handler.postDelayed(nextvideo,5000);
        } else if (position == 2) {
            imgView.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            ibtPlay.setVisibility(View.VISIBLE);
            ibtVolumeOn.setVisibility(View.VISIBLE);
            tvTimeStart.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);

            // đọ dài của video
            mp = MediaPlayer.create(this, Uri.parse(check));
            long duration = mp.getDuration();
            mp.release();


//            if (mute == true) {
//                ibtVolumeOn.setImageResource(R.drawable.ic_volumeon);
//                volume.UnMuteAudio(this);
//
//                mute = false;
//                editorfull.putBoolean("volume",!mute);
//            } else {
//                ibtVolumeOn.setImageResource(R.drawable.ic_volumeoff);
//                mute = true;
//                volume.MuteAudio(this);
//                editorfull.putBoolean("volume",!mute);
//            }

            tvTimeEnd.setText(checkLink.stringForTime(duration));
            updateTime(tvTimeStart);

            video.setVideoPath(listvideo.get(indexVideo));
            video.start();
//            video.seekTo(timePause);

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

    private void updateTime(final TextView tv) {
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                tv.setText(checkLink.stringForTime(video.getCurrentPosition()));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    private Runnable nextvideo = new Runnable() {
        @Override
        public void run() {
            if (indexVideo == listvideo.size() - 1) indexVideo = 0;
            else indexVideo++;
            setVideoOrImager(listvideo.get(indexVideo));
        }
    };

}