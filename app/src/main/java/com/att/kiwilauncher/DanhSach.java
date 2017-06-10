package com.att.kiwilauncher;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.att.kiwilauncher.Adapter.ChuDeAdapter;
import com.att.kiwilauncher.Adapter.UngDungDsAdapter;

import java.util.ArrayList;
import java.util.List;

public class DanhSach extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout reLayDs1,reLayDs2,reLayDs3,reLayDs4,reLayDs5,reLayDs111,reLayDs112,reLayDs113,rltxt;
    RecyclerView   rc_Ds1,rc_Ds3;
    static RecyclerView     rc_Ds2,rc_Ds4;
    List<ChuDe>             dsChude;
    static List<UngDung>  dsUngDung,dsUngDungDaCai;
    static List<List<UngDung>>   listDsUngDung,listDsUngDungDaCai;
    static int                   demListUD = 0,demListDaCai = 0;
    ArrayList<View> listItem;
    int chieuDai,chieuRong,didIndex = 0,willIndex,mainRelay = 6;
    EditText    editTxt;
    ImageView   imageSearch,imgKiwi;
    static PackageManager  manager;
    ImageView   imgMinus1,imgMinus2,imgPlus1,imgPlus2;
    public static   View.OnClickListener appClick;
    UngDungDsAdapter listapp,listapp1;
    static boolean chooseRl2 = true;

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

        appClick = new AppClick(getApplicationContext());

        //listUD
        listDsUngDung = new ArrayList<>();
        listDsUngDungDaCai = new ArrayList<>();

        //img Minus,plus
        imgMinus1   = (ImageView) findViewById(R.id.img_minus_1_ds);
        imgMinus2   = (ImageView) findViewById(R.id.img_minus_2_ds);
        imgPlus1    = (ImageView) findViewById(R.id.img_plus_1_ds);
        imgPlus2    = (ImageView) findViewById(R.id.img_plus_2_ds);
        imgMinus1.setOnClickListener(this);
        imgMinus2.setOnClickListener(this);
        imgPlus1.setOnClickListener(this);
        imgPlus2.setOnClickListener(this);

        // Layout
        reLayDs1 = (RelativeLayout) findViewById(R.id.relayds1);
        reLayDs1.setPadding(5,0,5,(chieuRong*21)/25);
        reLayDs2 = (RelativeLayout) findViewById(R.id.relayds2);
        reLayDs2.setPadding(20,(chieuRong*4)/25,20,(chieuRong*18)/25);
        reLayDs3 = (RelativeLayout) findViewById(R.id.relayds3);
        reLayDs3.setPadding(20,(chieuRong*11)/50,20,(chieuRong*13)/25);
        reLayDs4 = (RelativeLayout) findViewById(R.id.relayds4);
        reLayDs4.setPadding(20,(chieuRong*14)/25,20,(chieuRong*8)/25);
        reLayDs5 = (RelativeLayout) findViewById(R.id.relayds5);
        reLayDs5.setPadding(20,(chieuRong*31)/50,20,(chieuRong*3)/25);

        rltxt = (RelativeLayout) findViewById(R.id.rltxt);
        rltxt.setPadding(20,(chieuRong*11)/25,20,(chieuRong*11)/25);

        editTxt = (EditText) findViewById(R.id.edtxtds1);
        imageSearch = (ImageView) findViewById(R.id.imgdsSearch);
        imgKiwi = (ImageView) findViewById(R.id.imgds1);

        reLayDs111 = (RelativeLayout) findViewById(R.id.relayds111);
        reLayDs112 = (RelativeLayout) findViewById(R.id.relayds112);
        reLayDs113 = (RelativeLayout) findViewById(R.id.relayds113);

        // RecyclerView
        rc_Ds1 = (RecyclerView) findViewById(R.id.rc_ds1);
        rc_Ds2 = (RecyclerView) findViewById(R.id.rc_ds2);
        rc_Ds3 = (RecyclerView) findViewById(R.id.rc_ds3);
        rc_Ds4 = (RecyclerView) findViewById(R.id.rc_ds4);

        imgKiwi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DanhSach.this,TrangChu.class);
                startActivity(i);
            }
        });
    }

    private void loadData() {
        // Chu de dua ve dang list
        dsChude = new ArrayList<ChuDe>();
        ChuDe cate1 = new ChuDe(" Giải Trí",R.drawable.play,0,false);
        dsChude.add(cate1);
        ChuDe cate2 = new ChuDe(" Giáo Dục ",R.drawable.school,1,false);
        dsChude.add(cate2);
        ChuDe cate3 = new ChuDe("Tìm kiếm ",R.drawable.search,2,false);
        dsChude.add(cate3);


        // Load App
        manager = getPackageManager();
        dsUngDung = new ArrayList<UngDung>();
        dsUngDungDaCai = new ArrayList<UngDung>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);

        int soungdung = 0;
        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.nameApp  = ri.activityInfo.packageName;
            app.iconApp  = ri.activityInfo.loadIcon(manager);
            dsUngDung.add(app);
            soungdung ++;
            if (soungdung == 7) {
                listDsUngDung.add(dsUngDung);
                dsUngDung = new ArrayList<UngDung>();
                soungdung = 0;
            }
        }

        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.nameApp  = ri.activityInfo.packageName;
            app.iconApp  = ri.activityInfo.loadIcon(manager);
            dsUngDung.add(app);
            soungdung ++;
            if (soungdung == 7) {
                listDsUngDung.add(dsUngDung);
                dsUngDung = new ArrayList<UngDung>();
                soungdung = 0;
            }
        }

        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.nameApp  = ri.activityInfo.packageName;
            app.iconApp  = ri.activityInfo.loadIcon(manager);
            dsUngDung.add(app);
            soungdung ++;
            if (soungdung == 7) {
                listDsUngDung.add(dsUngDung);
                dsUngDung = new ArrayList<UngDung>();
                soungdung = 0;
            }
        }

        if (dsUngDung.size() != 0) {
            listDsUngDung.add(dsUngDung);
        }
        rc_Ds2.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_Ds2.setLayoutManager(layoutManager2);
        listapp = new UngDungDsAdapter(this,listDsUngDung.get(demListUD));
        rc_Ds2.setAdapter(listapp);


        int soungdung1 = 0;
        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.nameApp  = ri.activityInfo.packageName;
            app.iconApp  = ri.activityInfo.loadIcon(manager);
            dsUngDungDaCai.add(app);
            soungdung1 ++;
            if (soungdung1 == 7) {
                listDsUngDungDaCai.add(dsUngDungDaCai);
                dsUngDungDaCai = new ArrayList<>();
                soungdung1 = 0;
            }
        }

        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.nameApp  = ri.activityInfo.packageName;
            app.iconApp  = ri.activityInfo.loadIcon(manager);
            dsUngDungDaCai.add(app);
            soungdung1 ++;
            if (soungdung1 == 7) {
                listDsUngDungDaCai.add(dsUngDungDaCai);
                dsUngDungDaCai = new ArrayList<>();
                soungdung1 = 0;
            }
        }

        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.nameApp  = ri.activityInfo.packageName;
            app.iconApp  = ri.activityInfo.loadIcon(manager);
            dsUngDungDaCai.add(app);
            soungdung1 ++;
            if (soungdung1 == 7) {
                listDsUngDungDaCai.add(dsUngDungDaCai);
                dsUngDungDaCai = new ArrayList<>();
                soungdung1 = 0;
            }
        }

        if (dsUngDungDaCai.size() != 0) {
            listDsUngDungDaCai.add(dsUngDungDaCai);
        }
        rc_Ds4.setHasFixedSize(true);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rc_Ds4.setLayoutManager(layoutManager4);
        listapp1 = new UngDungDsAdapter(this,listDsUngDungDaCai.get(demListDaCai));
        rc_Ds4.setAdapter(listapp1);

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
        listItem.add(imgKiwi);
        listItem.add(editTxt);
        listItem.add(imageSearch);
        listItem.add(reLayDs111);
        listItem.add(reLayDs112);
        listItem.add(reLayDs113);

        for (ChuDe c : dsChude) {
            listItem.add(rc_Ds1.getChildAt(0));
        }

        listItem.add(imgMinus1);

        for (UngDung s : listDsUngDung.get(demListUD)) {
            listItem.add(rc_Ds2.getChildAt(0));
        }

        listItem.add(imgPlus1);

        for (ChuDe c: dsChude) {
            listItem.add(rc_Ds3.getChildAt(0));
        }

        listItem.add(imgMinus2);

        for (UngDung s : listDsUngDungDaCai.get(demListDaCai)) {
            listItem.add(rc_Ds4.getChildAt(0));
        }

        listItem.add(imgPlus2);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                if ((didIndex >= mainRelay)
                        && (didIndex < mainRelay + dsChude.size())) {
                    rc_Ds1.getChildAt(didIndex - mainRelay).setBackgroundResource(R.drawable.none);
                    didIndex = 0;
                    listItem.get(0).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= mainRelay + dsChude.size())
                    && (didIndex < mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 2)) {
                    if (didIndex == mainRelay + dsChude.size()) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 1) {
                        imgPlus1.setBackgroundResource(R.drawable.none);
                    } else {
                        rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size() - 1).setBackgroundResource(R.drawable.none);
                    }
                    didIndex = mainRelay;
                    rc_Ds1.getChildAt(0).callOnClick();
                } else if ((didIndex >= mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 2)
                        && (didIndex < mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2)) {
                    rc_Ds3.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - 2).setBackgroundResource(R.drawable.none);
                    didIndex = mainRelay + dsChude.size() + 1;
                    rc_Ds2.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2)
                        && (didIndex < mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + listDsUngDungDaCai.get(demListDaCai).size() + 4)) {
                    chooseRl2 = true;
                    if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2) {
                        imgMinus2.setBackgroundResource(R.drawable.none);
                    } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + listDsUngDungDaCai.get(demListDaCai).size() + 3) {
                        imgPlus2.setBackgroundResource(R.drawable.none);
                    } else {
                        rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - dsChude.size() - 3).setBackgroundResource(R.drawable.none);
                    }
                    didIndex = mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 2;
                    rc_Ds3.getChildAt(0).callOnClick();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (didIndex < mainRelay) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex = mainRelay;
                    rc_Ds1.getChildAt(0).callOnClick();
                } else if ((didIndex >= mainRelay) && (didIndex < mainRelay + dsChude.size())) {
                    didIndex = mainRelay + dsChude.size() + 1;
                    rc_Ds2.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= mainRelay + dsChude.size())
                        && (didIndex < mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 2)) {
                    if (didIndex == mainRelay + dsChude.size()) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 1) {
                        imgPlus1.setBackgroundResource(R.drawable.none);
                    } else {
                        rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size() - 1).setBackgroundResource(R.drawable.none);
                    }
                    didIndex = mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 2;
                    rc_Ds3.getChildAt(0).callOnClick();
                } else if ((didIndex >= mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 2)
                        && (didIndex < mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2)) {
                    chooseRl2 = false;
                    didIndex = mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 3;
                    rc_Ds4.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if ((didIndex > 0) && (didIndex <= mainRelay)) {
                    if (didIndex == mainRelay) {
                        rc_Ds1.getChildAt(0).setBackgroundResource(R.drawable.none);
                        didIndex--;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                        return true;
                    }
                    willIndex = didIndex - 1;
                    changeListItemBackGround(didIndex, willIndex);
                } else if ((didIndex > mainRelay) && (didIndex <= mainRelay + dsChude.size())) {
                    if ( didIndex == mainRelay + dsChude.size()) {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rc_Ds1.getChildAt(didIndex - mainRelay).callOnClick();
                } else if (didIndex == mainRelay + dsChude.size() + 1) {
                    rc_Ds2.getChildAt(0).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex > mainRelay + dsChude.size() + 1)
                        && (didIndex <= mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 1)) {
                    if (didIndex != mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 1) {
                        rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size() - 1).setBackgroundResource(R.drawable.none);
                    } else {
                        imgPlus1.setBackgroundResource(R.drawable.none);
                    }
                    chooseRl2 = true;
                    didIndex--;
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size() - 1).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 2) {
                    rc_Ds3.getChildAt(0).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    imgPlus1.setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex > mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 2)
                        && (didIndex <= mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2)) {
                    if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2) {
                        imgMinus2.setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rc_Ds3.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - 2).callOnClick();
                } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 3) {
                    rc_Ds4.getChildAt(0).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    imgMinus2.setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex > mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 3)
                        && (didIndex <= mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + listDsUngDungDaCai.get(demListDaCai).size() + 3 )) {
                    if ( didIndex != mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + listDsUngDungDaCai.get(demListDaCai).size() + 3) {
                        rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - dsChude.size() - 3).setBackgroundResource(R.drawable.none);
                    } else {
                        imgPlus2.setBackgroundResource(R.drawable.none);
                    }
                    chooseRl2 = false;
                    didIndex--;
                    rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - dsChude.size() - 3).setBackgroundResource(R.drawable.border_pick);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (didIndex < (mainRelay - 1) ) {
                    willIndex = didIndex + 1;
                    changeListItemBackGround(didIndex, willIndex);
                } else if ((didIndex >= (mainRelay - 1 )) && (didIndex <= (mainRelay - 1 ) + dsChude.size())) {
                    if (didIndex == mainRelay - 1 + dsChude.size()) {
                        didIndex = mainRelay - 1;
                    }
                    listItem.get((mainRelay - 1 )).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    rc_Ds1.getChildAt(didIndex - mainRelay).callOnClick();
                } else if (didIndex == mainRelay + dsChude.size()) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    rc_Ds2.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= mainRelay+ dsChude.size()) && (didIndex < (mainRelay - 1 ) + dsChude.size() + 1 + listDsUngDung.get(demListUD).size())) {
                    if (didIndex != (mainRelay - 1 ) + dsChude.size() + 1) {
                        rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size() - 1).setBackgroundResource(R.drawable.none);
                    } else {
                        listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    }
                    chooseRl2 = true;
                    didIndex++;
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size() - 1).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size()) {
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size() - 1).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    imgPlus1.setBackgroundResource(R.drawable.border_pick);
                }else if ((didIndex >= (mainRelay - 1 ) + dsChude.size() + listDsUngDung.get(demListUD).size() + 2)
                        && (didIndex <= (mainRelay - 1 ) + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2)) {
                    if (didIndex == (mainRelay - 1 ) + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2) {
                        didIndex = (mainRelay - 1) + dsChude.size() + listDsUngDung.get(demListUD).size() + 2;
                    }
                    if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 1) {
                        imgPlus1.setBackgroundResource(R.drawable.none);
                    }
                    didIndex++;
                    rc_Ds3.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - 2).callOnClick();
                } else if (didIndex == (mainRelay - 1) + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2){
                    didIndex++;
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= (mainRelay - 1 ) + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 3 )
                        && (didIndex < (mainRelay - 1 ) + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + listDsUngDungDaCai.get(demListDaCai).size() + 3)) {
                    if (didIndex != (mainRelay - 1 ) + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 3) {
                        rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - dsChude.size() - 3).setBackgroundResource(R.drawable.none);
                    } else {
                        imgMinus2.setBackgroundResource(R.drawable.none);
                    }
                    chooseRl2 = false;
                    didIndex++;
                    rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - dsChude.size() - 3).setBackgroundResource(R.drawable.border_pick);
                } else if (didIndex == (mainRelay - 1) + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + listDsUngDungDaCai.get(demListDaCai).size() + 3) {
                    rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - dsChude.size() - 3).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    imgPlus2.setBackgroundResource(R.drawable.border_pick);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (didIndex < mainRelay) {
                    listItem.get(didIndex).callOnClick();
                } else if ((didIndex > mainRelay + dsChude.size())
                        && (didIndex < mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 1)){
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size() - 1).callOnClick();
                } else if (didIndex == mainRelay + dsChude.size()) {
                    listItem.get(didIndex).callOnClick();
                } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + 1) {
                    imgPlus1.callOnClick();
                } else if ((didIndex > mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2)
                        && (didIndex < mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + listDsUngDungDaCai.get(demListDaCai).size() + 3)) {
                    rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - listDsUngDung.get(demListUD).size() - dsChude.size() - 3).callOnClick();
                } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2) {
                    imgMinus2.callOnClick();
                } else if (didIndex == mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + listDsUngDungDaCai.get(demListDaCai).size() + 3) {
                    imgPlus2.callOnClick();
                }
                return true;
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    public void changeListItemBackGround(int i,int j) {
        listItem.get(i).setBackgroundResource(R.drawable.none);
        listItem.get(j).setBackgroundResource(R.drawable.border_pick);
        if ( j == 2 ) {
            listItem.get(j).setBackgroundResource(R.color.bordervideo);
        }
        didIndex = willIndex;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_minus_1_ds:
                if (0 < demListUD) {
                    demListUD--;
                    listapp = new UngDungDsAdapter(getApplicationContext(), listDsUngDung.get(demListUD));
                    rc_Ds2.setAdapter(listapp);
                    didIndex = mainRelay + dsChude.size();
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else {
                    Toast.makeText(getApplicationContext(),"Bạn đang ở danh sách các ứng dụng đầu tiên ",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_minus_2_ds:
                if (0 < demListDaCai) {
                    demListDaCai--;
                    listapp1 = new UngDungDsAdapter(getApplicationContext(), listDsUngDungDaCai.get(demListDaCai));
                    rc_Ds4.setAdapter(listapp1);
                    didIndex = mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2;
                    imgMinus2.setBackgroundResource(R.drawable.border_pick);
                } else {
                    Toast.makeText(getApplicationContext(), "Bạn đang ở danh sách các ứng dụng đầu tiên ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_plus_1_ds:
                if (listDsUngDung.size() - 1 > demListUD) {
                    imgPlus1.setBackgroundResource(R.drawable.none);
                    demListUD++;
                    listapp = new UngDungDsAdapter(getApplicationContext(), listDsUngDung.get(demListUD));
                    rc_Ds2.setAdapter(listapp);
                    didIndex = mainRelay + dsChude.size();
                    listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                } else {
                    Toast.makeText(getApplicationContext(),"Bạn đã ở cuối danh sách ứng dụng",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_plus_2_ds:
                if (listDsUngDungDaCai.size() - 1 > demListDaCai) {
                    imgPlus2.setBackgroundResource(R.drawable.none);
                    demListDaCai++;
                    listapp1 = new UngDungDsAdapter(getApplicationContext(), listDsUngDungDaCai.get(demListDaCai));
                    rc_Ds4.setAdapter(listapp1);
                    didIndex = mainRelay + dsChude.size() + listDsUngDung.get(demListUD).size() + dsChude.size() + 2;
                    imgMinus2.setBackgroundResource(R.drawable.border_pick);
                } else {
                    Toast.makeText(getApplicationContext(),"Bạn đã ở cuối danh sách ứng dụng",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public static class AppClick implements View.OnClickListener{

        private final Context context;

        public AppClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            if (chooseRl2 == true) {
                int position = rc_Ds2.getChildPosition(v);
                Intent i = manager.getLaunchIntentForPackage(listDsUngDung.get(demListUD).get(position).getNameApp().toString());
                context.startActivity(i);
            } else {
                int position = rc_Ds4.getChildPosition(v);
                Intent i = manager.getLaunchIntentForPackage(listDsUngDungDaCai.get(demListDaCai).get(position).getNameApp().toString());
                context.startActivity(i);
            }
        }
    }
}
