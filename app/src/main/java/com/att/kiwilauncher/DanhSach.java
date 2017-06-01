package com.att.kiwilauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.att.kiwilauncher.Adapter.ChuDeAdapter;
import com.att.kiwilauncher.Adapter.UngDungAdapter;

import java.util.ArrayList;
import java.util.List;

public class DanhSach extends AppCompatActivity {
    RelativeLayout reLayDs1,reLayDs2,reLayDs3,reLayDs4,reLayDs5;
    RecyclerView   rc_Ds1,rc_Ds2,rc_Ds3,rc_Ds4;
    List<ChuDe>    dsChude;
    List<UngDung>  dsUngDung,dsUngDungDaCai;
    ArrayList<View> listItem;
    int chieuDai,chieuRong;
    EditText    editTxt;
    PackageManager  manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach);
        addControls();
        loadData();
        addNavigationItem();
    }

    private void addControls() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        chieuDai = displayMetrics.widthPixels;
        chieuRong = displayMetrics.heightPixels;

        // Layout
        reLayDs1 = (RelativeLayout) findViewById(R.id.relayds1);
        reLayDs1.setPadding(5,0,5,(chieuRong*21)/25);
        reLayDs2 = (RelativeLayout) findViewById(R.id.relayds2);
        reLayDs2.setPadding(20,(chieuRong*4)/25,20,(chieuRong*18)/25);
        reLayDs3 = (RelativeLayout) findViewById(R.id.relayds3);
        reLayDs3.setPadding(20,(chieuRong*11)/50,20,(chieuRong*13)/25);
        reLayDs4 = (RelativeLayout) findViewById(R.id.relayds4);
        reLayDs4.setPadding(20,(chieuRong*11)/25,20,(chieuRong*11)/25);
        reLayDs5 = (RelativeLayout) findViewById(R.id.relayds5);
        reLayDs5.setPadding(20,(chieuRong*25)/50,20,(chieuRong*7)/25);

        // RecyclerView
        rc_Ds1 = (RecyclerView) findViewById(R.id.rc_ds1);
        rc_Ds2 = (RecyclerView) findViewById(R.id.rc_ds2);
        rc_Ds3 = (RecyclerView) findViewById(R.id.rc_ds3);
        rc_Ds4 = (RecyclerView) findViewById(R.id.rc_ds4);
    }

    private void loadData() {
        // Chu de dua ve dang list
        dsChude = new ArrayList<ChuDe>();
        ChuDe cate1 = new ChuDe(" Giải Trí",R.drawable.play,0,true);
        dsChude.add(cate1);
        ChuDe cate2 = new ChuDe(" Giáo Dục ",R.drawable.school,1,false);
        dsChude.add(cate2);
        ChuDe cate3 = new ChuDe("Tìm kiếm ",R.drawable.search,2,false);
        dsChude.add(cate3);


        // Load App
        manager = getPackageManager();
        dsUngDung = new ArrayList<UngDung>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);

        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.nameApp  = ri.activityInfo.packageName;
            app.iconApp  = ri.activityInfo.loadIcon(manager);
            dsUngDung.add(app);
        }
        rc_Ds2.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_Ds2.setLayoutManager(layoutManager2);
        UngDungAdapter listapp = new UngDungAdapter(this,dsUngDung);
        rc_Ds2.setAdapter(listapp);
        rc_Ds4.setHasFixedSize(true);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_Ds4.setLayoutManager(layoutManager4);
        UngDungAdapter listapp1 = new UngDungAdapter(this,dsUngDung);
        rc_Ds4.setAdapter(listapp);

        // Rc 1 va 3
        rc_Ds1.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_Ds1.setLayoutManager(layoutManager);
        ChuDeAdapter categoryAdapter = new ChuDeAdapter(this,dsChude);
        rc_Ds1.setAdapter(categoryAdapter);
        rc_Ds3.setHasFixedSize(true);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_Ds3.setLayoutManager(layoutManager3);
        ChuDeAdapter categoryAdapter1 = new ChuDeAdapter(this,dsChude);
        rc_Ds3.setAdapter(categoryAdapter1);

    }

    private void addNavigationItem() {
        listItem = new ArrayList<>();
    }
}
