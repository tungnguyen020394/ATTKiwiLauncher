package com.att.kiwilauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.att.kiwilauncher.adapter.ChuDeAdapter;
import com.att.kiwilauncher.adapter.ChuDeDsAdapter;
import com.att.kiwilauncher.adapter.UngDungDsAdapter;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.xuly.LunarCalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.att.kiwilauncher.TrangChu.REQUEST_SETTINGS;
import static com.att.kiwilauncher.TrangChu.demdsApp;
import static com.att.kiwilauncher.TrangChu.listAppBottom;
import static com.att.kiwilauncher.TrangChu.listApps;
import static com.att.kiwilauncher.TrangChu.listapp;

public class DanhSach extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout reLay1, reLay2, reLay3, reLay13, reLay12, reLay113, reLay111, reLay112, reLay11, reLay121;
    int didIndex = 0;
    int main = 5;
    int indexChuDe;
    static int demDsApp = 0;
    static List<ChuDe> dsChuDe;
    public static List<UngDung> dsUngDung;
    static RecyclerView rcChuDe,rcChuDe1;
    static RecyclerView rcUngDung,rcUngDung1;
    public static UngDungDsAdapter ungDungAdapter;
    static PackageManager manager;
    ImageView imageKiwi, imageCaidat,imageMinusDs,imagePlusDs;
    ArrayList<View> listItem;
    TextView text;
    Intent mIntentGetData;
    TextView mNgayDuongTxt, mNgayAmTxt, mTxtTinh, mTxtNhietDo;
    DatabaseHelper mDatabaseHelper;
    public static View.OnClickListener appClick,chuDeClick;
    static List<UngDung> listUngDungChan;
    static List<UngDung> listUngDungLe;
    static List<UngDung> listUngDungChung;
    static ChuDeDsAdapter categoryAdapter;
    static ChuDeAdapter categoryAdapter1;
    List<ChuDe> cates1;
    TrangChu trangChu;
    static List<List<UngDung>> listAppsDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach);
        addControls();
        addLoadData();
        addMove();
    }

    public void addControls() {
        trangChu.demdsApp = 0;
        mDatabaseHelper = new DatabaseHelper(this);
        mDatabaseHelper.checkDatabase(this);

        mNgayDuongTxt = (TextView) findViewById(R.id.txt_duonglich_ds);
        mNgayAmTxt = (TextView) findViewById(R.id.txt_amlich_ds);
        mTxtTinh = (TextView) findViewById(R.id.txt_thanhpho_ds);
        mTxtNhietDo = (TextView) findViewById(R.id.txt_nhietdo_ds);
        // layout
        reLay1 = (RelativeLayout) findViewById(R.id.relay1_ds);
        reLay2 = (RelativeLayout) findViewById(R.id.relay2_ds);
        reLay3 = (RelativeLayout) findViewById(R.id.relay3_ds);
        reLay13 = (RelativeLayout) findViewById(R.id.relay13_ds);
        reLay12 = (RelativeLayout) findViewById(R.id.relay12_ds);
        reLay11 = (RelativeLayout) findViewById(R.id.relay11_ds);
        reLay121 = (RelativeLayout) findViewById(R.id.relay121_ds);
        reLay113 = (RelativeLayout) findViewById(R.id.relay113_ds);
        reLay111 = (RelativeLayout) findViewById(R.id.relay111_ds);
        reLay112 = (RelativeLayout) findViewById(R.id.relay112_ds);
        reLay113.setOnClickListener(this);
        reLay121.setOnClickListener(this);
        reLay111.setOnClickListener(this);

        //end layout
        rcChuDe = (RecyclerView) findViewById(R.id.recycler1_ds);
        rcUngDung = (RecyclerView) findViewById(R.id.recycler2_ds);

        imageKiwi = (ImageView) findViewById(R.id.img1_ds);
        imageKiwi.setBackgroundResource(R.drawable.border_pick);
        imageCaidat = (ImageView) findViewById(R.id.img_caidat_ds);
        imageCaidat.setOnClickListener(this);
        imageKiwi.setOnClickListener(this);

        text = (TextView) findViewById(R.id.text1_ds);
        text.setSelected(true);
        text.setText(mDatabaseHelper.getLinkTextQuangCao());
        appClick = new AppClick(getApplicationContext());
        chuDeClick = new ChuDeClick(getApplicationContext());

        rcChuDe1 = (RecyclerView) findViewById(R.id.recycler4_ds);
        rcUngDung1 = (RecyclerView) findViewById(R.id.recycler5_ds);
        imageMinusDs = (ImageView) findViewById(R.id.img_minus_ds);
        imagePlusDs  = (ImageView) findViewById(R.id.img_plus_ds);
        imageMinusDs.setOnClickListener(this);
        imagePlusDs.setOnClickListener(this);
    }

    public void addLoadData() {
        Intent i = getIntent();
        String tenChuDe = i.getStringExtra("tenChuDe");
        // Load Category
        dsChuDe = new ArrayList<ChuDe>();
        ChuDe cate1 = new ChuDe(tenChuDe, R.drawable.ic_giaitri, 0, true);
        dsChuDe.add(cate1);
        ChuDe cate2 = new ChuDe("Miễn Phí", R.drawable.ic_trochoi, 0, false);
        dsChuDe.add(cate2);
        ChuDe cate3 = new ChuDe("Trả Phí", R.drawable.ic_suckhoe, 0, false);
        dsChuDe.add(cate3);
        listUngDungChan = new ArrayList<UngDung>();
        listUngDungLe = new ArrayList<UngDung>();
        //listUngDungChung = (List<UngDung>) mIntentGetData.getSerializableExtra("listChuDe");
        listUngDungChung = mDatabaseHelper.getListUngDung(mDatabaseHelper.getListChuDe().get(4));
        rcChuDe.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcChuDe.setLayoutManager(layoutManager1);

        text.setText(mDatabaseHelper.getLinkTextQuangCao());

        // Load App
        manager = getPackageManager();
        dsUngDung = new ArrayList<UngDung>();
        Intent getIntent = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        int soUngDung = 0;
        dsUngDung.addAll(mDatabaseHelper.getListUngDung(mDatabaseHelper.getListChuDe().get(4)));

        rcUngDung.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 8);
        rcUngDung.setLayoutManager(gridLayoutManager);
        ungDungAdapter = new UngDungDsAdapter(this, dsUngDung);
        rcUngDung.setAdapter(ungDungAdapter);
        categoryAdapter = new ChuDeDsAdapter(this, dsChuDe, ungDungAdapter, dsUngDung);
        rcChuDe.setAdapter(categoryAdapter);

        Map<String, String> today = LunarCalendar.getTodayInfo();
        mNgayDuongTxt.setText("Thứ " + today.get("thu") + ", " + today.get("daySolar") + "/" + today.get("monthSolar") + "/" + today.get("yearSolar"));
        mNgayAmTxt.setText(today.get("dayLunar") + "/" + today.get("monthLunar") + " " + today.get("can") + " " + today.get("chi"));
        final String todayFormated = today.get("yearSolar") + "-" + today.get("monthSolar") + "-" + today.get("daySolar") + " "
                + today.get("hour") + ":" + today.get("minute") + ":" + today.get("second");

        SharedPreferences sharedPreferencesThoiTiet = getSharedPreferences("thoitiet", MODE_PRIVATE);
        mTxtTinh.setText(sharedPreferencesThoiTiet.getString("tinh", "Hà nội"));
        mTxtNhietDo.setText(sharedPreferencesThoiTiet.getString("nhietdo", "25") + " ° C");

        // relay 4 + relay 5
        cates1 = trangChu.getCates();
        rcChuDe1.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        rcChuDe1.setLayoutManager(linearLayoutManager);
        categoryAdapter1 = new ChuDeAdapter(this,cates1,trangChu.getmListTheLoaiUngDung(),trangChu.getmListUngDung());
        rcChuDe1.setAdapter(categoryAdapter1);

        rcUngDung1.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this,7);
        rcUngDung1.setLayoutManager(gridLayoutManager1);
        UngDungDsAdapter listapp = new UngDungDsAdapter(this,trangChu.getListAppBottom());
        rcUngDung1.setAdapter(listapp);

        listAppsDs = new ArrayList<>();
        listAppsDs = listApps;
    }

    public void addMove() {
        listItem = new ArrayList<>();
        listItem.add(imageKiwi);
        listItem.add(reLay121);
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
                    if (didIndex == 1) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_text);
                    }
                    rcChuDe.getChildAt(0).callOnClick();
                    didIndex = main;
                } else if ((didIndex >= main) && (didIndex < main + dsChuDe.size())) {
                    indexChuDe = didIndex;
                    didIndex = main + dsChuDe.size();
                    rcUngDung.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex > main - 1 + dsChuDe.size()
                        && didIndex <= main - 1 + dsChuDe.size() + dsUngDung.size() - 8
                        && dsUngDung.size() > 8) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = didIndex + 8;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                } else if ( didIndex <= main - 1 + dsChuDe.size() + dsUngDung.size()) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = main + dsChuDe.size() + dsUngDung.size();
                    rcChuDe1.getChildAt(0).callOnClick();
                } else if (didIndex > main - 1 + dsChuDe.size() + dsUngDung.size()
                        && didIndex <= main - 1 + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                    didIndex = main + dsChuDe.size() + dsUngDung.size() + cates1.size() + 1;
                    rcUngDung1.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_UP:
                text.setSelected(true);
                if (didIndex >= main && didIndex < main + dsChuDe.size()) {
                    didIndex = 0;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_textpick);
                } else if ((didIndex >= main + dsChuDe.size())
                        && (didIndex < main + dsChuDe.size() + dsUngDung.size())
                        && dsUngDung.size() < 8) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = indexChuDe;
                    rcChuDe.getChildAt(didIndex - main).callOnClick();
                } else if ((didIndex >= main + dsChuDe.size())
                        && (didIndex < main + dsChuDe.size() + 8)
                        && dsUngDung.size() > 8) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = indexChuDe;
                    rcChuDe.getChildAt(didIndex - main).callOnClick();
                } else if (didIndex >= main + dsChuDe.size() + 8
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size()) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = didIndex - 8;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex > main - 1 + dsChuDe.size() + dsUngDung.size()
                        && didIndex <= main - 1 + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                    rcUngDung.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                    didIndex = main + dsChuDe.size();
                } else if (didIndex > main + dsChuDe.size() + dsUngDung.size() + cates1.size()
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size() + cates1.size() + listAppsDs.get(demdsApp).size()) {
                    rcUngDung1.getChildAt(didIndex - main - 1 - dsChuDe.size() - dsUngDung.size() - cates1.size()).setBackgroundResource(R.drawable.none);
                    didIndex = main - 1 + dsChuDe.size() + dsUngDung.size();
                    rcChuDe1.getChildAt(0).callOnClick();
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                text.setSelected(true);
                if (didIndex > 0 && didIndex <= main) {
                    if (didIndex != main) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                        if (didIndex == 1)
                            listItem.get(didIndex).setBackgroundResource(R.drawable.border_text);
                    }
                    didIndex--;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    if (didIndex == 1)
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_textpick);
                } else if (didIndex > main && didIndex <= main + dsChuDe.size()) {
                    if (didIndex == main + dsChuDe.size()) {
                        rcUngDung.getChildAt(0).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rcChuDe.getChildAt(didIndex - main).callOnClick();
                } else if (didIndex > main + dsChuDe.size() && didIndex <= main + dsChuDe.size() + dsUngDung.size()) {
                    if ( didIndex != main + dsChuDe.size() + dsUngDung.size()) {
                        rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex > main + dsChuDe.size() + dsUngDung.size()
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                    if (didIndex == main + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                        imageMinusDs.setImageResource(R.drawable.ic_minus1);
                    }
                    didIndex--;
                    rcChuDe1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size()).callOnClick();
                } else if (didIndex > main + dsChuDe.size() + dsUngDung.size() + cates1.size()
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size() + cates1.size() + listAppsDs.get(demdsApp).size()) {
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - cates1.size() - 1).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    if (didIndex != main + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                        rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - cates1.size() - 1).setBackgroundResource(R.drawable.border_pick);
                    } else {
                        imageMinusDs.setImageResource(R.drawable.ic_minus);
                    }
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size() + cates1.size() + listAppsDs.get(demdsApp).size() + 1) {
                    imagePlusDs.setImageResource(R.drawable.ic_plus1);
                    didIndex--;
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - cates1.size() - 1).setBackgroundResource(R.drawable.border_pick);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                text.setSelected(true);
                if (didIndex < main - 1) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    if (didIndex == 1)
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_text);
                    didIndex++;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    if (didIndex == 1)
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_textpick);
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
                } else if (didIndex >= main - 1 + dsChuDe.size() + dsUngDung.size()
                        && didIndex <= main - 1 + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                    if (didIndex == main - 1 + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                       didIndex = main - 1 + dsChuDe.size() + dsUngDung.size();
                    }
                    if (didIndex == main - 1 + dsChuDe.size() + dsUngDung.size()) {
                        rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    }
                    didIndex++;
                    rcChuDe1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size()).callOnClick();
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                    imageMinusDs.setImageResource(R.drawable.ic_minus1);
                    didIndex++;
                    rcUngDung1.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex >= main + 1 + dsChuDe.size() + dsUngDung.size() + cates1.size()
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size() + cates1.size() + listAppsDs.get(demdsApp).size()) {
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - cates1.size() - 1).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    if ( didIndex != main + dsChuDe.size() + dsUngDung.size() + cates1.size() + listAppsDs.get(demdsApp).size() + 1) {
                        rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - cates1.size() - 1).setBackgroundResource(R.drawable.border_pick);
                    } else {
                        imagePlusDs.setImageResource(R.drawable.ic_plus);
                    }
                }
                break;

            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (didIndex <= main - 1) {
                    listItem.get(didIndex).callOnClick();
                } else if (didIndex > main - 1 + dsChuDe.size()
                        && didIndex <= main - 1 + dsChuDe.size() + dsUngDung.size()) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).callOnClick();
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size() + cates1.size()) {
                    imageMinusDs.callOnClick();
                } else if (didIndex >= main + 1 + dsChuDe.size() + dsUngDung.size() + cates1.size()
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size() + cates1.size() + listAppsDs.get(demdsApp).size()) {
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - cates1.size() - 1).callOnClick();
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size() + cates1.size() + listAppsDs.get(demdsApp).size() + 1) {
                    imagePlusDs.callOnClick();
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
            if (rcUngDung.callOnClick()) {
                int position = rcUngDung.getChildPosition(v);
                try {
                    Intent i = manager.getLaunchIntentForPackage(dsUngDung.get(position).getNameApp().toString());
                    context.startActivity(i);
                } catch (Exception e) {
                    Intent intent = manager.getLaunchIntentForPackage("com.store.kiwi.kiwistore");
                    context.startActivity(intent);
                }
            } else {
                int position = rcUngDung1.getChildPosition(v);
                try {
                    Intent i = manager.getLaunchIntentForPackage(listAppsDs.get(demDsApp).get(position).getNameApp().toString());
                    context.startActivity(i);
                } catch (Exception e) {
                    Intent intent = manager.getLaunchIntentForPackage("com.store.kiwi.kiwistore");
                    context.startActivity(intent);
                }
            }
        }
    }

    public static class ChuDeClick implements View.OnClickListener {
        private final Context context;

        public ChuDeClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int position = rcChuDe.getChildPosition(v);
            for (ChuDe cate : dsChuDe) {
                if (cate.isCheckedCate()) {
                    cate.setCheckedCate(false);
                    break;
                }
            }
            ChuDe cate = dsChuDe.get(position);
            cate.setCheckedCate(true);
            categoryAdapter.notifyDataSetChanged();
            listUngDungChan.clear();
            listUngDungLe.clear();
            UngDung ungDung;
            for (int i = 0; i < listUngDungChung.size(); i++) {
                if (i % 2 == 0) {
                    ungDung = listUngDungChung.get(i);
                    listUngDungChan.add(ungDung);
                } else {
                    ungDung = listUngDungChung.get(i);
                    listUngDungLe.add(ungDung);
                }
            }
            dsUngDung.clear();
            if (dsChuDe.get(position).getDrawCate() == R.drawable.ic_giaitri) {
                dsUngDung.clear();
                dsUngDung.addAll(listUngDungChung);
            } else if (dsChuDe.get(position).getDrawCate() == R.drawable.ic_trochoi) {
                dsUngDung.clear();
                dsUngDung.addAll(listUngDungChan);
            } else if (dsChuDe.get(position).getDrawCate() == R.drawable.ic_suckhoe) {
                dsUngDung.clear();
                dsUngDung.addAll(listUngDungLe);
            }
            ungDungAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img1_ds:
                onBackPressed();
                break;

            case R.id.img_caidat_ds:
                DanhSach.this.startActivityForResult(new Intent(Settings.ACTION_SETTINGS), REQUEST_SETTINGS);
                break;

            case R.id.relay121_ds:
                Toast.makeText(getApplicationContext(), text.getText(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.relay111_ds:
                Toast.makeText(getApplicationContext(), mNgayDuongTxt.getText(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), mNgayAmTxt.getText(), Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_minus_ds:
                if (0 < demdsApp) {
                    demDsApp--;
                    demdsApp--;
                    /*listapp = new UngDungAdapter(getApplicationContext(), listApps.get(demdsApp));
                    rcApp.setAdapter(listapp);*/
                    listAppBottom.clear();
                    listAppBottom.addAll(listAppsDs.get(demdsApp));
                    listapp.notifyDataSetChanged();

                    didIndex = main + dsChuDe.size() + dsUngDung.size() + cates1.size();
                    imageMinusDs.setImageResource(R.drawable.ic_minus);
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đang ở danh sách các ứng dụng đầu tiên ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_plus_ds:
                Toast.makeText(getApplicationContext(), "Bạn đã ở cuối danh sách ứng dụng", Toast.LENGTH_SHORT).show();
                break;

            case R.id.relay113_ds:
                DanhSach.this.startActivityForResult(new Intent(Settings.ACTION_SETTINGS), REQUEST_SETTINGS);
                break;
        }

    }

}
