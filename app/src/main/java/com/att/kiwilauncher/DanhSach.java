package com.att.kiwilauncher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

import com.att.kiwilauncher.adapter.ChuDeDsAdapter;
import com.att.kiwilauncher.adapter.UngDungDsAdapter;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.xuly.AppInfoHelper;
import com.att.kiwilauncher.xuly.AppInstallHelper;
import com.att.kiwilauncher.xuly.LunarCalendar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.att.kiwilauncher.TrangChu.REQUEST_SETTINGS;
import static com.att.kiwilauncher.TrangChu.demdsApp;
import static com.att.kiwilauncher.TrangChu.listApps;

public class DanhSach extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout reLay1, reLay2, reLay3, reLay13, reLay12, reLay113, reLay111, reLay112, reLay11, reLay121;
    static int didIndex = 0;
    static int main = 5;
    int indexChuDe;
    static int demDsApp = 0;
    static List<ChuDe> dsChuDe;
    private static List<List<UngDung>> listUngDungDaCai;
    public static List<UngDung> dsUngDung, ungDungDaCai;
    static RecyclerView rcChuDe;
    static RecyclerView rcUngDung, rcUngDung1;
    public static UngDungDsAdapter ungDungAdapter, listappDacai;
    private static PackageManager manager;
    private PackageManager manager1;
    ImageView imageCaidat, imageMinusDs, imagePlusDs;
    RelativeLayout imageKiwi;
    ArrayList<View> listItem;
    TextView text;
    Intent mIntentGetData;
    TextView mNgayDuongTxt, mNgayAmTxt, mTxtTinh, mTxtNhietDo;
    DatabaseHelper mDatabaseHelper;
    public static View.OnClickListener appClick, chuDeClick;
    static List<UngDung> listUngDungChan;
    static List<UngDung> listUngDungLe;
    static List<UngDung> listUngDungChung;
    static ChuDeDsAdapter categoryAdapter;
    TrangChu trangChu;
    static List<List<UngDung>> listAppsDs;
    int idTheLoai;

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

        imageKiwi = (RelativeLayout) findViewById(R.id.relay13_ds);
        imageKiwi.setBackgroundResource(R.drawable.border_pick);
        imageCaidat = (ImageView) findViewById(R.id.img_caidat_ds);
        imageCaidat.setOnClickListener(this);
        imageKiwi.setOnClickListener(this);

        text = (TextView) findViewById(R.id.text1_ds);
        text.setSelected(true);
        text.setText(mDatabaseHelper.getLinkTextQuangCao());
        appClick = new AppClick(getApplicationContext());
        chuDeClick = new ChuDeClick(getApplicationContext());

        rcUngDung1 = (RecyclerView) findViewById(R.id.recycler5_ds);
        imageMinusDs = (ImageView) findViewById(R.id.img_minus_ds);
        imagePlusDs = (ImageView) findViewById(R.id.img_plus_ds);
        imageMinusDs.setOnClickListener(this);
        imagePlusDs.setOnClickListener(this);
    }

    public void addLoadData() {
        demDsApp = 0;
        int dem = 0;
        manager1 = getPackageManager();
        ungDungDaCai = new ArrayList<UngDung>();
        listUngDungDaCai = new ArrayList<List<UngDung>>();
        Intent i1 = new Intent(Intent.ACTION_MAIN, null);
        i1.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager1.queryIntentActivities(i1, 0);
        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.labelApp = ri.loadLabel(manager1);
            app.nameApp = ri.activityInfo.packageName;
            app.iconApp = ri.activityInfo.loadIcon(manager1);
            app.icon = null;
            ungDungDaCai.add(app);
            dem++;
            if (dem == 7) {
                listUngDungDaCai.add(ungDungDaCai);
                dem = 0;
                ungDungDaCai = new ArrayList<UngDung>();
            }
        }

        if (ungDungDaCai.size() != 0) {
            listUngDungDaCai.add(ungDungDaCai);
        }

        Intent i = getIntent();
        String tenChuDe = i.getStringExtra("tenChuDe");
        idTheLoai = i.getIntExtra("idTheLoai", 3);
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
        listUngDungChung = mDatabaseHelper.getListUngDungById(idTheLoai);
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
        // dsUngDung.addAll(mDatabaseHelper.getListUngDung(mDatabaseHelper.getListChuDe().get(4)));
        dsUngDung.addAll(mDatabaseHelper.getListUngDungById(idTheLoai));

        rcUngDung.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 9);
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

        // relay 5
        rcUngDung1.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(this, 7);
        rcUngDung1.setLayoutManager(gridLayoutManager1);
        listappDacai = new UngDungDsAdapter(this, listUngDungDaCai.get(demDsApp));
        rcUngDung1.setAdapter(listappDacai);

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
                    if (dsUngDung.size() != 0) {
                        rcUngDung.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                    }
                } else if (didIndex > main - 1 + dsChuDe.size()
                        && didIndex <= main - 1 + dsChuDe.size() + dsUngDung.size() - 8
                        && dsUngDung.size() > 8) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = didIndex + 8;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                } else {
                    try {
                        rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                        didIndex = main + dsChuDe.size() + dsUngDung.size() + 1;
                        rcUngDung1.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                    } catch (Exception e) {
                    }
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
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size()
                        && dsUngDung.size() > 8) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex = didIndex - 8;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex > main + dsChuDe.size() + dsUngDung.size() - 1
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size() + listUngDungDaCai.get(demDsApp).size() + 1) {
                    if (didIndex == main + dsChuDe.size() + dsUngDung.size()
                            || didIndex == main + dsUngDung.size() + dsChuDe.size() + listUngDungDaCai.get(demDsApp).size() + 1) {
                        imageMinusDs.setImageResource(R.drawable.ic_minus1);
                        imagePlusDs.setImageResource(R.drawable.ic_plus1);
                    } else {
                        rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - 1).setBackgroundResource(R.drawable.none);
                    }
                    didIndex = main + dsChuDe.size() + dsUngDung.size() - 1;
                    if (dsUngDung.size() != 0 ) {
                        rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                    }

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
                        if (dsUngDung.size() != 0) {
                            rcUngDung.getChildAt(0).setBackgroundResource(R.drawable.none);
                        }
                    }
                    didIndex--;
                    rcChuDe.getChildAt(didIndex - main).callOnClick();
                } else if (didIndex > main + dsChuDe.size()
                        && didIndex < main + dsChuDe.size() + dsUngDung.size()) {
                    if (didIndex != main + dsChuDe.size() + dsUngDung.size()) {
                        rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main + 1 + dsChuDe.size() + dsUngDung.size()) {
                    rcUngDung1.getChildAt(0).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    imageMinusDs.setImageResource(R.drawable.ic_minus);
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size() + listUngDungDaCai.get(demDsApp).size() + 1) {
                    didIndex--;
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - 1).setBackgroundResource(R.drawable.border_pick);
                    imagePlusDs.setImageResource(R.drawable.ic_plus1);
                } else if (didIndex > main + dsChuDe.size() + dsUngDung.size() + 1
                        && didIndex <= main + dsChuDe.size() + dsUngDung.size() + listUngDungDaCai.get(demdsApp).size()) {
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - 1).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - 1).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size()) {
                    imageMinusDs.setImageResource(R.drawable.ic_minus1);
                    didIndex--;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
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
                } else if ((didIndex > main - 1 + dsChuDe.size())
                        && (didIndex < main - 1 + dsChuDe.size() + dsUngDung.size())) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main - 1 + dsChuDe.size() + dsUngDung.size()) {
                    rcUngDung.getChildAt(didIndex - main - dsChuDe.size()).setBackgroundResource(R.drawable.none);
                    rcUngDung1.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                    didIndex = main + dsChuDe.size() + dsUngDung.size() + 1;
                } else if (didIndex >= main + dsChuDe.size() + dsUngDung.size() + 1
                        && didIndex < main + dsChuDe.size() + dsUngDung.size() + listUngDungDaCai.get(demDsApp).size()) {
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - 1).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - 1).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size() + listUngDungDaCai.get(demDsApp).size()) {
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - 1).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    imagePlusDs.setImageResource(R.drawable.ic_plus);
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size()) {
                    imageMinusDs.setImageResource(R.drawable.ic_minus1);
                    didIndex++;
                    rcUngDung1.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
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
                    didIndex = 0;
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size()) {
                    imageMinusDs.callOnClick();
                } else if (didIndex == main + dsChuDe.size() + dsUngDung.size() + listUngDungDaCai.get(demDsApp).size() + 1) {
                    imagePlusDs.callOnClick();
                } else if (didIndex > main + dsChuDe.size() + dsUngDung.size()
                        && didIndex < main + dsChuDe.size() + dsUngDung.size() + listUngDungDaCai.get(demDsApp).size() + 1) {
                    rcUngDung1.getChildAt(didIndex - main - dsChuDe.size() - dsUngDung.size() - 1).callOnClick();
                    didIndex = 0;
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
            int parentID = ((View) v.getParent()).getId();
            if (parentID == rcUngDung.getId()) {
                int position = rcUngDung.getChildPosition(v);
             //   Toast.makeText(context,"t1",Toast.LENGTH_SHORT).show();
                if (AppInstallHelper.checkInstalledApplication(dsUngDung.get(position).getPackageName(), context)) {
                    AppInfoHelper.launchApp(dsUngDung.get(position).getPackageName(), context);
                } else {
                    Intent intent = manager.getLaunchIntentForPackage("com.example.tienh.kiwistore10");
                    intent.putExtra("idApp", dsUngDung.get(position).getId());
                    context.startActivity(intent);
                }
            } else {
              //  Toast.makeText(context,"t2",Toast.LENGTH_SHORT).show();
                int position = rcUngDung1.getChildPosition(v);
                if (AppInstallHelper.checkInstalledApplication(listUngDungDaCai.get(demDsApp).get(position).getPackageName(), context)) {
                    AppInfoHelper.launchApp(listUngDungDaCai.get(demDsApp).get(position).getPackageName(), context);
                } else {
                    Intent intent = manager.getLaunchIntentForPackage("com.example.tienh.kiwistore10");
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
            case R.id.relay13_ds:
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
                if (0 < demDsApp) {
                    demDsApp--;
                    listappDacai = new UngDungDsAdapter(this, listUngDungDaCai.get(demDsApp));
                    rcUngDung1.setAdapter(listappDacai);
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đang ở danh sách các ứng dụng đầu tiên ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_plus_ds:
                if (demDsApp < listUngDungDaCai.size() - 1) {
                    demDsApp++;
                    listappDacai = new UngDungDsAdapter(this, listUngDungDaCai.get(demDsApp));
                    rcUngDung1.setAdapter(listappDacai);
                    didIndex = main + dsUngDung.size() + dsChuDe.size() + listUngDungDaCai.get(demDsApp).size() + 1;
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đã ở cuối danh sách ứng dụng", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.relay113_ds:
                DanhSach.this.startActivityForResult(new Intent(Settings.ACTION_SETTINGS), REQUEST_SETTINGS);
                break;
        }

    }

    @Override
    protected void onStop() {
        finish();
        super.onStop();
    }
}
