package com.att.kiwilauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.att.kiwilauncher.adapter.ChuDeAdapter;
import com.att.kiwilauncher.adapter.UngDungAdapter;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.xuly.LunarCalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DanhSach extends AppCompatActivity {
    RelativeLayout reLay1, reLay2, reLay3, reLay13, reLay12, reLay113;
    int chieuDai, chieuRong, mChieuDai, mChieuRong, didIndex = 0, main = 4, indexChuDe;
    List<ChuDe> dsChuDe;
    List<UngDung> dsUngDung;
    RecyclerView rcChuDe, rcUngDung;
    PackageManager manager;
    ImageView imageKiwi, imageSearch;
    ArrayList<View> listItem;
    EditText editText;
    TextView mNgayDuongTxt, mNgayAmTxt, mTxtTinh, mTxtNhietDo;
    DatabaseHelper mDatabaseHelper;

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
        reLay3.setPadding(mChieuDai * 2, mChieuRong * 10, mChieuDai, mChieuRong);

        reLay13 = (RelativeLayout) findViewById(R.id.relay13_ds);
        reLay12 = (RelativeLayout) findViewById(R.id.relay12_ds);
        reLay13.setPadding(mChieuDai, 0, mChieuDai * 60, 0);
        reLay12.setPadding(mChieuDai * 10, mChieuRong, mChieuDai, mChieuRong * 2);
        reLay113 = (RelativeLayout) findViewById(R.id.relay113_ds);

        //end layout
        rcChuDe = (RecyclerView) findViewById(R.id.recycler1_ds);
        rcUngDung = (RecyclerView) findViewById(R.id.recycler2_ds);

        imageKiwi = (ImageView) findViewById(R.id.img1_ds);
        imageKiwi.setBackgroundResource(R.drawable.border_pick);
        imageSearch = (ImageView) findViewById(R.id.imgsearch_ds);
        editText = (EditText) findViewById(R.id.edittxt_ds);

    }

    public void addLoadData() {
        // Load Category
        dsChuDe = new ArrayList<ChuDe>();
        ChuDe cate1 = new ChuDe("Giải Trí", R.drawable.play, 0, false);
        dsChuDe.add(cate1);
        ChuDe cate2 = new ChuDe("Trò Chơi", R.drawable.ic_games, 0, false);
        dsChuDe.add(cate2);
        ChuDe cate3 = new ChuDe("Giáo Dục", R.drawable.school, 0, false);
        dsChuDe.add(cate3);

        rcChuDe.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcChuDe.setLayoutManager(layoutManager1);
        ChuDeAdapter categoryAdapter = new ChuDeAdapter(this, dsChuDe);
        rcChuDe.setAdapter(categoryAdapter);
        mNgayDuongTxt = (TextView) findViewById(R.id.txt_duonglich);
        mNgayAmTxt = (TextView) findViewById(R.id.txt_amlich);
        mTxtTinh = (TextView) findViewById(R.id.txt_thanhpho);
        mTxtNhietDo = (TextView) findViewById(R.id.txt_nhietdo);

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
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcUngDung.setLayoutManager(layoutManager2);
        UngDungAdapter ungDungAdapter = new UngDungAdapter(this, dsUngDung);
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
        listItem.add(editText);
        listItem.add(imageSearch);
        listItem.add(reLay113);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (didIndex < main) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    rcChuDe.getChildAt(0).callOnClick();
                    didIndex = 4;
                } else if ((didIndex >= main) && (didIndex < main + dsChuDe.size())) {
                    indexChuDe = didIndex;
                    didIndex = 7;
                    rcUngDung.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                if (didIndex >= main && didIndex < main + dsChuDe.size()) {
                    didIndex = 0;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= main + dsChuDe.size()) && (didIndex < main + dsChuDe.size() + dsUngDung.size())) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = indexChuDe;
                    rcChuDe.getChildAt(didIndex - main).callOnClick();
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
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

            default:
                return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
