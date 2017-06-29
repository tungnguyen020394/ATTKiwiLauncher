package com.att.kiwilauncher;

import android.app.AlertDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.att.kiwilauncher.view.VideoFull;
import com.att.kiwilauncher.xuly.DuLieu;
import com.att.kiwilauncher.xuly.LunarCalendar;
import com.att.kiwilauncher.xuly.RequestToServer;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrangChu extends AppCompatActivity implements View.OnClickListener {
    public final static String APIKEY = "1fd660e2a27afad8b71405f654997a62";
    int didIndex = 0, willIndex, indexChuDe = 0, main = 12, position, bonusmain = 6, indexVideo = 0;
    RelativeLayout reLay1, reLay2, reLay3, reLay4, reLay111, reLay112, reLay113, reLay11, reLay22, reLay211,
            reLay212, reLay213, reLay214, reLay215, reLay216, reLay13, reLay12, reLay2221, reLay121, reLay21;
    String linkQuangCao;
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
    static List<ChuDe> cates;
    public static List<List<UngDung>> listApps;
    public static List<UngDung> listAppBottom;
    //  ArrayList<String> listvideo;
    public static View.OnClickListener appClick;
    public static final int REQUEST_SETTINGS = 101;
    public static int demdsApp = 0;
    private static final String TAG = "TrangChu";
    private ProgressDialog dialog;
    private ChuDeAdapter categoryAdapter;
    private HashMap<String, List> mAllListMap;
    private List<QuangCao> mListQuangCao;
    private List<QuangCao> mListVideoAd;
    public static List<UngDung> mListUngDung;
    public static List<TheLoaiUngDung> mListTheLoaiUngDung;
    LinearLayout linNear1;
    //    Volume volume;
    int intVolume = 15;
    ImageView imgView, imgWeb;
    ImageButton ibtNext, ibtPlay, ibtBack, ibtVolumeOn, ibtFull;
    TextView tvTimeStart, tvTimeEnd, tvTime;
    MediaPlayer mp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editorfull;
    private int timePause = 0;
    private boolean dragging, playing = true, mute = true;
    private AudioManager audioManager;
    Intent intent;
    Handler handler = new Handler();
    private int currentApiVersion;
    private int hidetabbar = 1;
    private String mTextQC = "Hãy đến với các sản phẩm chất lượng nhất từ chúng tôi";
    private String todayFormated = "";
    private String idThoiTiet = "24";
    private String mIdCapNhat = "0";
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
    private StringRequest mRequestToServer;
    private Runnable mRunableCheckInterNet = new Runnable() {
        @Override
        public void run() {
            if (DuLieu.hasInternetConnection(TrangChu.this) && mListVideoAd.size() > 0) {
                setVideoOrImager(mListVideoAd.get(indexVideo));
                handler.removeCallbacks(mRunableCheckInterNet);
            } else {
                handler.postDelayed(mRunableCheckInterNet, 10000);
            }
        }
    };;

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

        // Layout Click
        reLay113.setOnClickListener(this);
        reLay121.setOnClickListener(this);
        reLay111.setOnClickListener(this);
        reLay112.setOnClickListener(this);
        reLay2221.setOnClickListener(this);

        // Video Click
        ibtNext.setOnClickListener(this);
        ibtPlay.setOnClickListener(this);
        ibtBack.setOnClickListener(this);
        ibtFull.setOnClickListener(this);
        ibtVolumeOn.setOnClickListener(this);
        imgWeb.setOnClickListener(this);

        // Main Click
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image6.setOnClickListener(this);

        // Loadmore App click
        imageMinus.setOnClickListener(this);
        imagePlus.setOnClickListener(this);
    }

    private void loadData() {
        mIdCapNhat = mDatabaseHelper.getIdCapNhat();
        // Load Category
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
        listAppBottom.addAll(listApps.get(demdsApp));

        rcApp.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        rcApp.setLayoutManager(gridLayoutManager);
        listapp = new UngDungAdapter(this, listAppBottom);
        rcApp.setAdapter(listapp);

        dialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        dialog.setTitle("Đang tải");
        dialog.setMessage("Vui lòng đợi ứng dụng tải dữ liệu");
        mRequestToServer = RequestToServer.createRequestAndUpdate(dialog, mDatabaseHelper, mAllListMap,
                mListQuangCao, cates, categoryAdapter, text, TrangChu.this, mIdCapNhat, mListVideoAd);
        requestQueue.add(mRequestToServer);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initNetworkConnectDialog();
        ibtPlay.setImageResource(R.drawable.ic_pause);
        playing = true;
        try {
            ibtVolumeOn.setImageResource(R.drawable.ic_volumeoff);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        } catch (Exception e) {

        }
        if (DuLieu.hasInternetConnection(TrangChu.this) && mListVideoAd.size() > 0) {
            setVideoOrImager(mListVideoAd.get(indexVideo));
        } else {
            video.setVisibility(View.GONE);
            imgView.setVisibility(View.VISIBLE);
            imgView.setImageResource(R.drawable.img);
            handler.post(mRunableCheckInterNet);
        }
    }

    private void addControls() {
        mSharedPreferencesThoiTiet = getSharedPreferences("thoitiet", MODE_PRIVATE);
        idThoiTiet = mSharedPreferencesThoiTiet.getString("idthoitiet", "24");
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.checkDatabase(this);

        listItem = new ArrayList<>();
        //    listvideo = new ArrayList<>();
        mListVideoAd = new ArrayList<>();
        listAppBottom = new ArrayList<>();

        cates = new ArrayList<>();
        mListUngDung = new ArrayList<>();
        mListQuangCao = new ArrayList<>();
        mListTheLoaiUngDung = new ArrayList<>();
        //  mAllListMap = new HashMap<>();

       /* mListQuangCao = mAllListMap.get("quangcao");
        cates = mAllListMap.get("theloai");
        mListUngDung = mAllListMap.get("ungdung");
        mListTheLoaiUngDung = mAllListMap.get("theloaiungdung");*/

        mListTheLoaiUngDung = mDatabaseHelper.getListTheLoaiUngDung();
        mListUngDung = mDatabaseHelper.getListUngDungV2();
        mListQuangCao = mDatabaseHelper.getListQuangCao();
        cates = mDatabaseHelper.getListChuDe();

        mListVideoAd.addAll(DuLieu.getAdVideoFromList(mListQuangCao));
       /* listvideo.add(Define.URL_LINK_IMG02);
        listvideo.add(Define.URL_LINK_BACK);*/
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
        reLay121 = (RelativeLayout) findViewById(R.id.relay121);
        reLay21 = (RelativeLayout) findViewById(R.id.relay21);
        reLay22 = (RelativeLayout) findViewById(R.id.relay22);
        reLay211 = (RelativeLayout) findViewById(R.id.relay211);
        reLay212 = (RelativeLayout) findViewById(R.id.relay212);
        reLay213 = (RelativeLayout) findViewById(R.id.relay213);
        reLay214 = (RelativeLayout) findViewById(R.id.relay214);
        reLay215 = (RelativeLayout) findViewById(R.id.relay215);
        reLay216 = (RelativeLayout) findViewById(R.id.relay216);
        reLay2221 = (RelativeLayout) findViewById(R.id.relay2221);

        //end Layout

        rcCategory = (RecyclerView) findViewById(R.id.recycler1);
        rcApp = (RecyclerView) findViewById(R.id.recycler2);

        // Video controls
        video = (VideoView) findViewById(R.id.videoView);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ibtNext = (ImageButton) findViewById(R.id.imgNext);
        ibtPlay = (ImageButton) findViewById(R.id.imgPlay);
        ibtBack = (ImageButton) findViewById(R.id.imgBack);
        ibtVolumeOn = (ImageButton) findViewById(R.id.imgVolumeOn);
        ibtFull = (ImageButton) findViewById(R.id.imgFull);
        tvTimeStart = (TextView) findViewById(R.id.tvTimeBegin);
        tvTimeEnd = (TextView) findViewById(R.id.tvTimeEnd);
        tvTime = (TextView) findViewById(R.id.tvTime);
        imgWeb = (ImageView) findViewById(R.id.imgWeb);
        imgView = (ImageView) findViewById(R.id.imgView);

        checkLink = new CheckLink();
        sharedPreferences = getSharedPreferences("volume", MODE_PRIVATE);
        editorfull = sharedPreferences.edit();

        text = (TextView) findViewById(R.id.text1);
        text.setSelected(true);

        // Main Contorls
        image1 = (ImageView) findViewById(R.id.img_tv);
        image2 = (ImageView) findViewById(R.id.img_phim);
        image3 = (ImageView) findViewById(R.id.img_nhac);
        image4 = (ImageView) findViewById(R.id.img_kara);
        image5 = (ImageView) findViewById(R.id.img_youtube);
        image6 = (ImageView) findViewById(R.id.img_store);

        imageMinus = (ImageView) findViewById(R.id.img_minus);
        imagePlus = (ImageView) findViewById(R.id.img_plus);

        mNgayDuongTxt = (TextView) findViewById(R.id.txt_duonglich);
        mNgayAmTxt = (TextView) findViewById(R.id.txt_amlich);
        mTxtTinh = (TextView) findViewById(R.id.txt_thanhpho);
        mTxtNhietDo = (TextView) findViewById(R.id.txt_nhietdo);

        text.setSelected(true);
        if (!DuLieu.getAdTextFromList(mListQuangCao).equals("")) {
            mTextQC = DuLieu.getAdTextFromList(mListQuangCao);
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
        mRequestToServer = RequestToServer.createWeatherRequest(mThoiTiet, todayFormated, mTxtNhietDo, editor);
        requestQueue.add(mRequestToServer);
        mTxtTinh.setText(mThoiTiet.getTen());
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
                    if (didIndex == 12 && listItem.get(didIndex) instanceof ImageView)
                        ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_website);
                    if (listItem.get(didIndex) instanceof ImageButton) {
                        ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorWhite));
                    }
                    didIndex = 1;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= main + bonusmain) && (didIndex < main + bonusmain + cates.size())) {
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

                    if (didIndex == 12 && listItem.get(didIndex) instanceof ImageView)
                        ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_website);
                    if (listItem.get(didIndex) instanceof ImageButton) {
                        ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorWhite));
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
                        if (didIndex == 12 && listItem.get(didIndex) instanceof ImageView)
                            ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_website);
                        didIndex--;
                    } else if (didIndex == 8) {
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
                        didIndex--;
                        if (listItem.get(didIndex) instanceof ImageButton) {
                            ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorcatenew));
                        }
                        if (didIndex == 12 && listItem.get(didIndex) instanceof ImageView)
                            ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_web);
                    } else {
                        if (didIndex == 12 && listItem.get(didIndex) instanceof ImageView)
                            ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_website);
                        didIndex = 7;
                    }
                } else if ((didIndex >= main + 1 + bonusmain) && (didIndex < (main + 1 + cates.size() + bonusmain))) {
                    if (didIndex == (main + cates.size() + bonusmain)) {
                        imageMinus.setImageResource(R.drawable.ic_minus1);
                    }
                    didIndex--;
                    rcCategory.getChildAt(didIndex - main - bonusmain).callOnClick();
                    indexChuDe = didIndex - main - bonusmain;
                } else if (didIndex == main + bonusmain) {
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

                        if (didIndex == 12 && listItem.get(didIndex) instanceof ImageView)
                            ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_website);

                        if (position == 1 && didIndex == main + 1) {
                            didIndex = 8;
                            listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
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
                    imageMinus.callOnClick();
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
            // video click
            case R.id.imgWeb:
                // Uri uri = Uri.parse("http://www.bongdaso.com/news.aspx");
                Uri uri = Uri.parse(mListVideoAd.get(indexVideo).getLinkWeb());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                editorfull.putInt("index", indexVideo);
                editorfull.putInt("timePause", timePause);
                editorfull.commit();
                startActivity(intent);
                break;

            case R.id.imgFull:
                intent = new Intent(getBaseContext(), VideoFull.class);
//                intent.putExtra("index", indexVideo);
                //     intent.putExtra("list", listvideo);
                intent.putExtra("video", (Serializable) mListVideoAd);

                // độ dài video đang chạy
                timePause = video.getCurrentPosition();
                intent.putExtra("timePause", timePause);
                intent.putExtra("mute", mute);
                intent.putExtra("index", indexVideo);

                startActivityForResult(intent, Define.NUMBER_RESULT_FULL);
                break;

            case R.id.imgVolumeOn:
                if (mute == true) {
                    ibtVolumeOn.setImageResource(R.drawable.ic_volumeon);

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, intVolume, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    mute = false;
                } else {
                    ibtVolumeOn.setImageResource(R.drawable.ic_volumeoff);
                    mute = true;
                    try {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                    } catch (Exception e) {
                    }
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
                if (indexVideo == mListVideoAd.size() - 1) indexVideo = 0;
                else indexVideo++;
                setVideoOrImager(mListVideoAd.get(indexVideo));
                break;

            case R.id.imgBack:
                handler.removeCallbacks(nextvideo);
                if (indexVideo == 0) indexVideo = (mListVideoAd.size() - 1);
                else indexVideo--;
                setVideoOrImager(mListVideoAd.get(indexVideo));
                break;

            // main click
            case R.id.img_caidat:
                TrangChu.this.startActivityForResult(new Intent(Settings.ACTION_SETTINGS), REQUEST_SETTINGS);
                break;

            case R.id.img_tv:
                Intent i = new Intent(TrangChu.this, DanhSach.class);
                i.putExtra("listChuDe", (Serializable) mListUngDung);
                startActivity(i);
                break;

            case R.id.img_phim:
                Toast.makeText(getApplicationContext(), "Tính năng sẽ được cập nhật trong thời gian tới", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_nhac:
                Toast.makeText(getApplicationContext(), "Tính năng sẽ được cập nhật trong thời gian tới", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_kara:
                Toast.makeText(getApplicationContext(), "Tính năng sẽ được cập nhật trong thời gian tới", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_youtube:
                try {
                    /*Intent i4 = manager.getLaunchIntentForPackage("com.google.android.apps.youtube.kids");
                    startActivity(i4);*/
                    Intent i4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCfkjXDdMNlo879ba7QOAxAQ"));
                    startActivity(i4);
                } catch (Exception e) {
                    launchApp("com.store.kiwi.kiwistore");
                }
                break;

            case R.id.img_store:
                launchApp("com.store.kiwi.kiwistore");
                break;

            case R.id.relay113:
                imageCaiDat.callOnClick();
                break;

            // load more App
            case R.id.img_plus:
                if (listApps.size() - 1 > demdsApp) {
                    imagePlus.setImageResource(R.drawable.ic_plus1);
                    demdsApp++;
                    listAppBottom.clear();
                    listAppBottom.addAll(listApps.get(demdsApp));
                    listapp.notifyDataSetChanged();
                    /*listapp = new UngDungAdapter(getApplicationContext(), listApps.get(demdsApp));
                    rcApp.setAdapter(listapp);*/
                    didIndex = 12 + cates.size() + bonusmain;
                    imageMinus.setImageResource(R.drawable.ic_minus);
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đã ở cuối danh sách ứng dụng", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_minus:
                if (0 < demdsApp) {
                    demdsApp--;
                    /*listapp = new UngDungAdapter(getApplicationContext(), listApps.get(demdsApp));
                    rcApp.setAdapter(listapp);*/
                    listAppBottom.clear();
                    listAppBottom.addAll(listApps.get(demdsApp));
                    listapp.notifyDataSetChanged();

                    didIndex = 12 + cates.size() + bonusmain;
                    imageMinus.setImageResource(R.drawable.ic_minus);
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đang ở danh sách các ứng dụng đầu tiên ", Toast.LENGTH_SHORT).show();
                }
                break;

            // layout 1 Click
            case R.id.relay2221:
                didIndex = 12;
                if (listItem.get(didIndex) instanceof ImageView)
                    ((ImageView) listItem.get(didIndex)).setImageResource(R.drawable.ic_web);
                break;

            case R.id.relay121:
                Toast.makeText(getApplicationContext(), text.getText(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.relay111:
                Toast.makeText(getApplicationContext(), mNgayDuongTxt.getText(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), mNgayAmTxt.getText(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.relay112:
                break;

            case R.id.text1:
                try {
                    Uri uri1 = Uri.parse(linkQuangCao);
                    intent = new Intent(Intent.ACTION_VIEW, uri1);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Khong ton tai link", Toast.LENGTH_SHORT).show();
                    break;
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
        switch (resultCode) {
            case RESULT_OK:
                if (requestCode == Define.NUMBER_RESULT_FULL) {
                    timePause = data.getIntExtra("timePause", 0);
                    indexVideo = data.getIntExtra("index", 0);
                }
                break;
        }
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

    private void setVideoOrImager(QuangCao quangCao) {
        // position = checkLink.CheckLinkURL(check);
        if (quangCao.getLoaiQuangCao().equals("1")) {
            position = 2;
        } else {
            position = 1;
        }
        // image type
        if (position == 1) {
            if (didIndex > main && didIndex < main + bonusmain) {
                ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorWhite));
                didIndex = main + 1;
                ((ImageButton) listItem.get(didIndex)).setColorFilter(getResources().getColor(R.color.colorcatenew));
            }
            imgView.setVisibility(View.VISIBLE);
            ibtPlay.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            ibtVolumeOn.setVisibility(View.GONE);
            tvTimeStart.setVisibility(View.GONE);
            tvTime.setVisibility(View.GONE);
            ibtNext.setVisibility(View.GONE);
            ibtBack.setVisibility(View.GONE);

            tvTimeEnd.setText("   ");
            Glide.with(this)
                    .load(mListVideoAd.get(indexVideo).getLinkImage())
                    .into(imgView);

            handler.postDelayed(nextvideo, Integer.parseInt(quangCao.getTime()));
        } else if (position == 2) {
            //video type
            try {
                playing = true;
                ibtPlay.setImageResource(R.drawable.ic_pause);
                imgView.setVisibility(View.GONE);
                video.setVisibility(View.VISIBLE);
                ibtPlay.setVisibility(View.VISIBLE);
                ibtVolumeOn.setVisibility(View.VISIBLE);
                tvTimeStart.setVisibility(View.VISIBLE);
                tvTime.setVisibility(View.VISIBLE);
                ibtNext.setVisibility(View.VISIBLE);
                ibtBack.setVisibility(View.VISIBLE);

                // đọ dài của video
                mp = MediaPlayer.create(this, Uri.parse(quangCao.getLinkVideo()));
                long duration = mp.getDuration();
                mp.release();

                tvTimeEnd.setText(checkLink.stringForTime(duration));
                updateTime(tvTimeStart);
                video.setVideoPath(mListVideoAd.get(indexVideo).getLinkVideo());

                video.start();
                video.seekTo(timePause);
                timePause = 0;
                video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (indexVideo == mListVideoAd.size() - 1) indexVideo = 0;
                        else indexVideo++;
                        setVideoOrImager(mListVideoAd.get(indexVideo));
                        video.clearFocus();
                    }
                });
            } catch (Exception e) {

                e.printStackTrace();
                if (indexVideo == mListVideoAd.size() - 1) indexVideo = 0;
                else indexVideo++;
                setVideoOrImager(mListVideoAd.get(indexVideo));
            }

        } else if (position == 3) {
            imgView.setVisibility(View.GONE);
            video.setVisibility(View.VISIBLE);
            ibtPlay.setVisibility(View.VISIBLE);
            ibtVolumeOn.setVisibility(View.VISIBLE);
            tvTimeStart.setVisibility(View.VISIBLE);
            tvTime.setVisibility(View.VISIBLE);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (indexVideo == mListVideoAd.size() - 1) indexVideo = 0;
                    else indexVideo++;
                    video.setVideoURI(Uri.parse(mListVideoAd.get(indexVideo).getLinkVideo()));
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
            if (indexVideo == mListVideoAd.size() - 1) indexVideo = 0;
            else indexVideo++;
            setVideoOrImager(mListVideoAd.get(indexVideo));
        }
    };


}