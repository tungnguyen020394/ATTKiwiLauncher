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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.att.kiwilauncher.adapter.ChuDeAdapter;
import com.att.kiwilauncher.adapter.UngDungAdapter;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.model.QuangCao;
import com.att.kiwilauncher.model.TheLoaiUngDung;
import com.att.kiwilauncher.model.ThoiTiet;
import com.att.kiwilauncher.util.CheckLink;
import com.att.kiwilauncher.util.Define;
import com.att.kiwilauncher.util.Volume;
import com.att.kiwilauncher.xuly.DuLieu;
import com.att.kiwilauncher.xuly.LunarCalendar;
import com.att.kiwilauncher.xuly.RequestToServer;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.att.kiwilauncher.R.id.relay2;

public class TrangChu extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private RelativeLayout reLay1, reLay2, reLay3, reLay4, reLay111, reLay112, reLay113, reLay11,
            reLay21, reLay22, reLay222, reLay211, reLay212, reLay213, reLay214, reLay215, reLay216, reLay13, reLay12;
    private TextView text, mNgayAmTxt, mNgayDuongTxt, mTxtTinh, mTxtNhietDo, tvTimeStart, tvTimeEnd;
    private VideoView video;
    private ImageView image1, image2, image3, image4, image5, image6,
            imageCaiDat, imageMinus, imagePlus, imgView, imgWeb;
    private ImageButton ibtNext, ibtPlay, ibtBack, ibtVolume;
    private ProgressDialog dialog;
    private AlertDialog mNetworkConnectionNoticeDialog;
    private AlertDialog.Builder mNetworkConnectionNoticeDialogBuilder;
    private RecyclerView rcCategory;
    static RecyclerView rcApp;
    public static View.OnClickListener appClick;
    // adapter
    public static UngDungAdapter listapp;
    private ChuDeAdapter categoryAdapter;
    //list
    private HashMap<String, List> mAllListMap;
    static List<UngDung> apps;
    private List<ChuDe> cates;
    public static List<List<UngDung>> listApps;
    public static List<UngDung> listAppBottom;
    private List<QuangCao> mListQuangCao;
    public static List<UngDung> mListUngDung;
    public static List<TheLoaiUngDung> mListTheLoaiUngDung;
    private ArrayList<String> listvideo;
    private ArrayList<View> listItem;
    int chieuDai, chieuRong, didIndex = 0, willIndex, indexChuDe = 0, mChieuDai, mChieuRong, main = 12;
    public static final int REQUEST_SETTINGS = 101;
    public static int demdsApp = 0;
    private long timePause = 0;
    int indexVideo = 0;
    private static final String TAG = "TrangChu";
    private String mTextQC = "Hãy đến với các sản phẩm chất lượng nhất từ chúng tôi";
    private String todayFormated = "";
    private String idThoiTiet = "24";
    private DatabaseHelper mDatabaseHelper;
    private ThoiTiet mThoiTiet;
    private RequestQueue requestQueue;
    static PackageManager manager;
    private SharedPreferences mSharedPreferencesThoiTiet;
    private Volume volume;
    private CheckLink checkLink;
    private SimpleExoPlayerView exoPlayer;
    private SimpleExoPlayer player;
    private TrackSelector trackSelector;

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

        rcCategory.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcCategory.setLayoutManager(layoutManager1);
        categoryAdapter = new ChuDeAdapter(this, cates, mListTheLoaiUngDung, mListUngDung);
        rcCategory.setAdapter(categoryAdapter);

        // Load App
        manager = getPackageManager();
        apps = new ArrayList<>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        //   List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        listApps = new ArrayList();
        List<UngDung> checkedList = DuLieu.getListUngDungByTheLoaiId(cates.get(0).getIndexCate() + "", mListTheLoaiUngDung, mListUngDung);
        List<UngDung> tmpList = new ArrayList<>();
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
        listAppBottom.addAll(listApps.get(demdsApp));
        /*int soUngDung = 0;
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
        }*/


        /*for (ResolveInfo ri : availableActivities) {
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
        }*/

        /*if (apps.size() != 0) {
            listApps.add(apps);
        }*/

        rcApp.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcApp.setLayoutManager(layoutManager);
        listapp = new UngDungAdapter(this, listAppBottom);
        rcApp.setAdapter(listapp);

        //audio
        volume.MuteAudio(this);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi ứng dụng tải dữ liệu");
        // dialog.show();
        StringRequest stringRequest2 = RequestToServer.createRequestAndUpdate(dialog, mDatabaseHelper, mAllListMap,
                mListQuangCao, cates, categoryAdapter, text, TrangChu.this);
        requestQueue.add(stringRequest2);
    }

    @Override
    protected void onResume() {
        super.onResume();

        createPlayer(Define.URL_LINK_PLAY);
        exoPlayer.setOnTouchListener(this);
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

    private void initNetworkConnectDialog() {
        mNetworkConnectionNoticeDialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        mNetworkConnectionNoticeDialogBuilder.setTitle("Lỗi kết nối mạng");
        mNetworkConnectionNoticeDialogBuilder.setMessage("Vui lòng kiểm tra lại mạng kết nối ...");
        mNetworkConnectionNoticeDialogBuilder.setPositiveButton("Kiểm tra", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
    }

    private void addControls() {
        initNetworkConnectDialog();
        mSharedPreferencesThoiTiet = getSharedPreferences("thoitiet", MODE_PRIVATE);
        idThoiTiet = mSharedPreferencesThoiTiet.getString("idthoitiet", "24");
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
        cates = new ArrayList<>();
        mListUngDung = new ArrayList<>();
        mListQuangCao = new ArrayList<>();
        mListTheLoaiUngDung = new ArrayList<>();
        mAllListMap = new HashMap<>();
        mAllListMap = mDatabaseHelper.getAllList();
        mListQuangCao = mAllListMap.get("quangcao");
        cates = mAllListMap.get("theloai");
        mListUngDung = mAllListMap.get("ungdung");
        mListTheLoaiUngDung = mAllListMap.get("theloaiungdung");

        //   Toast.makeText(this, (String) mAllListMap.get("capnhat").get(0) + "s", Toast.LENGTH_SHORT).show();
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

        //video = (VideoView) findViewById(R.id.videoView);
        exoPlayer = (SimpleExoPlayerView) findViewById(R.id.videoView);
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
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image6.setOnClickListener(this);

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
        for (QuangCao quangCao : mListQuangCao) {
            if (quangCao.getLoaiQuangCao().equals("3")) {
                mTextQC = quangCao.getText();
                break;
            }
        }
        text.setText(mTextQC);

        imageCaiDat = (ImageView) findViewById(R.id.img_caidat);
        imageCaiDat.setOnClickListener(this);
        Map<String, String> today = LunarCalendar.getTodayInfo();
        mNgayDuongTxt.setText("Thứ " + today.get("thu") + ", " + today.get("daySolar") + "/" + today.get("monthSolar") + "/" + today.get("yearSolar"));
        mNgayAmTxt.setText(today.get("dayLunar") + "/" + today.get("monthLunar") + " " + today.get("can") + " " + today.get("chi"));
        todayFormated = today.get("yearSolar") + "-" + today.get("monthSolar") + "-" + today.get("daySolar") + " "
                + today.get("hour") + ":" + today.get("minute") + ":" + today.get("second");

        mThoiTiet = mDatabaseHelper.getThongTinThoiTiet(idThoiTiet);
        mTxtTinh.setText(mSharedPreferencesThoiTiet.getString("tinh", "Hà nội"));
        mTxtNhietDo.setText(mSharedPreferencesThoiTiet.getString("nhietdo", "25"));

        SharedPreferences.Editor editor = mSharedPreferencesThoiTiet.edit();
        editor.putString("tinh", mThoiTiet.getTen());
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = RequestToServer.createWeatherRequest(mThoiTiet, todayFormated, mTxtNhietDo, editor);
        requestQueue.add(stringRequest);
        mTxtTinh.setText(mThoiTiet.getTen());
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
            case R.id.imgPlay:

                break;
//            case R.id.btnFullOf:
//                btnFull.setVisibility(View.VISIBLE);
//                btnFullOf.setVisibility(View.GONE);
//
//                video.start();
//                break;
//            case R.id.btnFull:
////                btnFull.setVisibility(View.GONE);
////                btnFullOf.setVisibility(View.VISIBLE);
//
//                Intent intent = new Intent(getBaseContext(), VideoFull.class);
//                intent.putExtra("index", indexVideo);
//                intent.putExtra("list", listvideo);
//                String str = listvideo.get(indexVideo);
//
//                // độ dài video đang chạy
//                long timepause= video.getCurrentPosition();
//                intent.putExtra("timePause",timepause);
//
//                // đọ dài của video
//                MediaPlayer mp = MediaPlayer.create(this, Uri.parse(str));
//                int duration = mp.getDuration();
//                mp.release();
//
//
//                startActivity(intent);
//                break;
//            case R.id.btnOf:
//                btnOn.setVisibility(View.GONE);
//                btnOf.setVisibility(View.VISIBLE);
//                volume.MuteAudio(this);
//                break;
//
//            case R.id.btnOn:
//                btnOn.setVisibility(View.VISIBLE);
//                btnOf.setVisibility(View.GONE);
//                volume.UnMuteAudio(this);
//                break;
//
//            case R.id.btnPause:
//                btnPlay.setVisibility(View.VISIBLE);
//                btnPause.setVisibility(View.GONE);
//                video.pause();
//                break;
//            case R.id.btnPlay:
//                btnPlay.setVisibility(View.GONE);
//                btnPause.setVisibility(View.VISIBLE);
//                video.start();
//                break;
//
//            case R.id.btnNext:
//                if (indexVideo == (listvideo.size() - 1)) {
//                    indexVideo = 0;
//                } else indexVideo++;
//                video.setVideoPath(listvideo.get(indexVideo));
//                video.start();
//
//                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        indexVideo++;
//                        video.setVideoPath(listvideo.get(indexVideo));
//                        video.start();
//                    }
//                });
//                break;
//
//            case R.id.btnBack:
//                if (indexVideo == 0) {
//                    indexVideo = listvideo.size() - 1;
//                } else indexVideo--;
//
//                video.setVideoPath(listvideo.get(indexVideo));
//                video.start();
//
//                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        indexVideo++;
//                        video.setVideoPath(listvideo.get(indexVideo));
//                        video.start();
//                    }
//                });
//                break;

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
}
