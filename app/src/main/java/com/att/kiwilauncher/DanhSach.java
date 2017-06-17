package com.att.kiwilauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.att.kiwilauncher.adapter.ChuDeAdapter;
import com.att.kiwilauncher.adapter.UngDungDsAdapter;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.xuly.LunarCalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DanhSach extends AppCompatActivity {
    RelativeLayout reLay1, reLay2, reLay3, reLay13, reLay12, reLay113,reLay111,reLay112;
    int chieuDai, chieuRong, didIndex = 0, main = 5, indexChuDe;
    public static int mChieuDai, mChieuRong;
    List<ChuDe> dsChuDe;
    static List<UngDung> dsUngDung;
    RecyclerView rcChuDe;
    static RecyclerView rcUngDung;
    static PackageManager manager;
    ImageView imageKiwi;
    ArrayList<View> listItem;
    TextView text;
    TextView mNgayDuongTxt, mNgayAmTxt, mTxtTinh, mTxtNhietDo;
    DatabaseHelper mDatabaseHelper;
    public static View.OnClickListener appClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach);
        addControls();
        addLoadData();
        addMove();
    }

    public void addControls() {
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.checkDatabase(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        chieuDai = displayMetrics.widthPixels;
        chieuRong = displayMetrics.heightPixels;
        mChieuDai = chieuDai / 70;
        mChieuRong = chieuRong / 40;

        // layout
        reLay1 = (RelativeLayout) findViewById(R.id.relay1_ds);
        reLay2 = (RelativeLayout) findViewById(R.id.relay2_ds);
        reLay3 = (RelativeLayout) findViewById(R.id.relay3_ds);
        reLay1.setPadding(mChieuDai, 0, mChieuDai, mChieuRong * 34);
        reLay2.setPadding(mChieuDai * 2, mChieuRong * 7, mChieuDai, mChieuRong * 29);
        reLay3.setPadding(mChieuDai * 2, mChieuRong * 12, mChieuDai*2, mChieuRong);

        reLay13 = (RelativeLayout) findViewById(R.id.relay13_ds);
        reLay12 = (RelativeLayout) findViewById(R.id.relay12_ds);
        reLay13.setPadding(mChieuDai, 0, mChieuDai * 60, 0);
        reLay12.setPadding(mChieuDai * 10, mChieuRong, mChieuDai, mChieuRong * 2);
        reLay113 = (RelativeLayout) findViewById(R.id.relay113_ds);
        reLay111 = (RelativeLayout) findViewById(R.id.relay111_ds);
        reLay112 = (RelativeLayout) findViewById(R.id.relay112_ds);

        //end layout

        rcChuDe = (RecyclerView) findViewById(R.id.recycler1_ds);
        rcUngDung = (RecyclerView) findViewById(R.id.recycler2_ds);

        imageKiwi = (ImageView) findViewById(R.id.img1_ds);
        imageKiwi.setBackgroundResource(R.drawable.border_pick);

        text = (TextView) findViewById(R.id.text1_ds);
        text.setSelected(true);

        appClick = new AppClick(getApplicationContext());

    }

    public void addLoadData() {
        // Load Category
        dsChuDe = new ArrayList<ChuDe>();
        ChuDe cate1 = new ChuDe("Giải Trí", R.drawable.ic_giaitri, 0, true);
        dsChuDe.add(cate1);
        ChuDe cate2 = new ChuDe("Trò Chơi", R.drawable.ic_trochoi, 0, false);
        dsChuDe.add(cate2);
        ChuDe cate3 = new ChuDe("Giáo Dục & Sức Khoẻ", R.drawable.ic_suckhoe, 0, false);
        dsChuDe.add(cate3);
        ChuDe cate4 = new ChuDe("Tiện Ích", R.drawable.ic_tienich, 0, false);
        dsChuDe.add(cate4);

        rcChuDe.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcChuDe.setLayoutManager(layoutManager1);
        ChuDeAdapter categoryAdapter = new ChuDeAdapter(this, dsChuDe);
        rcChuDe.setAdapter(categoryAdapter);
        mNgayDuongTxt = (TextView) findViewById(R.id.txt_duonglich_ds);
        mNgayAmTxt = (TextView) findViewById(R.id.txt_amlich_ds);
        mTxtTinh = (TextView) findViewById(R.id.txt_thanhpho_ds);
        mTxtNhietDo = (TextView) findViewById(R.id.txt_nhietdo_ds);

        // Load App
        manager = getPackageManager();
        dsUngDung = new ArrayList<UngDung>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);

        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.labelApp = ri.loadLabel(manager);
            app.nameApp = ri.activityInfo.packageName;
            app.iconApp = ri.activityInfo.loadIcon(manager);
            dsUngDung.add(app);
        }

        rcUngDung.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        rcUngDung.setLayoutManager(gridLayoutManager);
        UngDungDsAdapter ungDungAdapter = new UngDungDsAdapter(this, dsUngDung);
        rcUngDung.setAdapter(ungDungAdapter);

        Map<String, String> today = LunarCalendar.getTodayInfo();
        mNgayDuongTxt.setText("Thứ " + today.get("thu") + ", " + today.get("daySolar") + "/" + today.get("monthSolar") + "/" + today.get("yearSolar"));
        mNgayAmTxt.setText(today.get("dayLunar") + "/" + today.get("monthLunar") + " " + today.get("can") + " " + today.get("chi"));
        final String todayFormated = today.get("yearSolar") + "-" + today.get("monthSolar") + "-" + today.get("daySolar") + " "
                + today.get("hour") + ":" + today.get("minute") + ":" + today.get("second");

        SharedPreferences sharedPreferencesThoiTiet = getSharedPreferences("thoitiet", MODE_PRIVATE);
        mTxtTinh.setText(sharedPreferencesThoiTiet.getString("tinh", "Hà nội"));
        mTxtNhietDo.setText(sharedPreferencesThoiTiet.getString("nhietdo", "25"));
    }

    public void addMove() {
        listItem = new ArrayList<>();
        listItem.add(imageKiwi);
        listItem.add(text);
        listItem.add(reLay111);
        listItem.add(reLay112);
        listItem.add(reLay113);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                text.setSelected(true);
                if (didIndex < main) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    rcChuDe.getChildAt(0).callOnClick();
                    didIndex = main;
                } else if ((didIndex >= main) && (didIndex < main + dsChuDe.size())) {
                    indexChuDe = didIndex;
                    didIndex = main + dsChuDe.size();
                    rcUngDung.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex > main - 1 + dsChuDe.size()
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size() - 7) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = didIndex + 7;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                text.setSelected(true);
                if (didIndex >= main && didIndex < main + dsChuDe.size()) {
                    didIndex = 0;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= main + dsChuDe.size()) && (didIndex < main + dsChuDe.size() + 7)) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = indexChuDe;
                    rcChuDe.getChildAt(didIndex - main).callOnClick();
                } else if (didIndex >= main + dsChuDe.size() + 7
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size()) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = didIndex - 7;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                text.setSelected(true);
                if (didIndex > 0 && didIndex <= main) {
                    if (didIndex != main) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    }

                    didIndex--;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex > main && didIndex <= main + dsChuDe.size()) {
                    if (didIndex == main + dsChuDe.size()) {
                        rcUngDung.getChildAt(0).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rcChuDe.getChildAt(didIndex - main).callOnClick();
                } else if (didIndex > main + dsChuDe.size() && didIndex < main + dsChuDe.size() + dsUngDung.size()) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                text.setSelected(true);
                if (didIndex < main - 1) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= main - 1) && (didIndex <= main - 1 + dsChuDe.size())) {
                    if (didIndex == main - 1) {
                        listItem.get(main - 1).setBackgroundResource(R.drawable.none);
                    }
                    if (didIndex == main - 1 + dsChuDe.size()) {
                        didIndex = main - 1;
                    }
                    didIndex++;
                    rcChuDe.getChildAt(didIndex - main).callOnClick();
                } else if ((didIndex > main - 1 + dsChuDe.size()) && (didIndex < main - 1 + dsChuDe.size() + dsUngDung.size())) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (didIndex < main - 1) {
                    listItem.get(didIndex).callOnClick();
                } else if (didIndex > main - 1 + dsChuDe.size()
                        && didIndex < main - 1 + dsChuDe.size() + dsUngDung.size()) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).callOnClick();
                }
                break;

            default:
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    public static class AppClick implements View.OnClickListener {

        private final Context context;

        public AppClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int position = rcUngDung.getChildPosition(v);
            Intent i = manager.getLaunchIntentForPackage(dsUngDung.get(position).getNameApp().toString());
            context.startActivity(i);
        }
    }
}
