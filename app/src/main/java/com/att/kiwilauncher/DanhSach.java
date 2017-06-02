package com.att.kiwilauncher;

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

import com.att.kiwilauncher.Adapter.ChuDeAdapter;
import com.att.kiwilauncher.Adapter.UngDungAdapter;

import java.util.ArrayList;
import java.util.List;

public class DanhSach extends AppCompatActivity {
    RelativeLayout reLayDs1,reLayDs2,reLayDs3,reLayDs4,reLayDs5,reLayDs111,reLayDs112,reLayDs113;
    RecyclerView   rc_Ds1,rc_Ds2,rc_Ds3,rc_Ds4;
    List<ChuDe>    dsChude;
    List<UngDung>  dsUngDung,dsUngDungDaCai;
    ArrayList<View> listItem;
    int chieuDai,chieuRong,didIndex = 0,willIndex,mainRelay = 6;
    EditText    editTxt;
    ImageView   imageSearch,imgKiwi;
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
        listItem.add(imgKiwi);
        listItem.add(editTxt);
        listItem.add(imageSearch);
        listItem.add(reLayDs111);
        listItem.add(reLayDs112);
        listItem.add(reLayDs113);

        for (ChuDe c : dsChude) {
            listItem.add(rc_Ds1.getChildAt(0));
        }

        for (UngDung s : dsUngDung) {
            listItem.add(rc_Ds2.getChildAt(0));
        }

        for (ChuDe c: dsChude) {
            listItem.add(rc_Ds3.getChildAt(0));
        }

        for (UngDung s : dsUngDung) {
            listItem.add(rc_Ds4.getChildAt(0));
        }
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
                    && (didIndex < mainRelay + dsChude.size() + dsUngDung.size())) {
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size()).setBackgroundResource(R.drawable.none);
                    didIndex = mainRelay;
                    rc_Ds1.getChildAt(0).callOnClick();
                } else if ((didIndex >= mainRelay + dsChude.size() + dsUngDung.size())
                        && (didIndex < mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size())) {
                    rc_Ds3.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size()).setBackgroundResource(R.drawable.none);
                    didIndex = mainRelay + dsChude.size();
                    rc_Ds2.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size())
                        && (didIndex < mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size() + dsUngDung.size())) {
                    rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size() - dsChude.size()).setBackgroundResource(R.drawable.none);
                    didIndex = mainRelay + dsChude.size() + dsUngDung.size();
                    rc_Ds3.getChildAt(0).callOnClick();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (didIndex < mainRelay) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex = mainRelay;
                    rc_Ds1.getChildAt(0).callOnClick();
                } else if ((didIndex >= mainRelay) && (didIndex < mainRelay + dsChude.size())) {
                    didIndex = mainRelay + dsChude.size();
                    rc_Ds2.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= mainRelay + dsChude.size())
                        && (didIndex < mainRelay + dsChude.size() + dsUngDung.size())) {
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size()).setBackgroundResource(R.drawable.none);
                    didIndex = mainRelay + dsChude.size() + dsUngDung.size();
                    rc_Ds3.getChildAt(0).callOnClick();
                } else if ((didIndex >= mainRelay + dsChude.size() + dsUngDung.size())
                        && (didIndex < mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size())) {
                    didIndex = mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size();
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
                        rc_Ds2.getChildAt(0).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rc_Ds1.getChildAt(didIndex - mainRelay).callOnClick();
                } else if ((didIndex > mainRelay + dsChude.size())
                        && (didIndex <= mainRelay + dsChude.size() + dsUngDung.size())) {
                    if (didIndex != mainRelay + dsChude.size() + dsUngDung.size()) {
                        rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size()).setBackgroundResource(R.drawable.none);
                    }
                    rc_Ds3.getChildAt(0).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size()).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex > mainRelay + dsChude.size() + dsUngDung.size())
                        && (didIndex <= mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size())) {
                    if (didIndex == mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size()) {
                        rc_Ds4.getChildAt(0).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rc_Ds3.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size()).callOnClick();
                } else if ((didIndex > mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size())
                        && (didIndex <= mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size() + dsUngDung.size())) {
                    if ( didIndex != mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size() + dsUngDung.size()) {
                        rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size() - dsChude.size()).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size() - dsChude.size()).setBackgroundResource(R.drawable.border_pick);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (didIndex < (mainRelay - 1) ) {
                    willIndex = didIndex + 1;
                    changeListItemBackGround(didIndex, willIndex);
                } else if ((didIndex >= (mainRelay - 1 )) && (didIndex < (mainRelay - 1 ) + dsChude.size())) {
                    listItem.get((mainRelay - 1 )).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    rc_Ds1.getChildAt(didIndex - mainRelay).callOnClick();
                } else if ((didIndex >= (mainRelay - 1 ) + dsChude.size()) && (didIndex < (mainRelay - 1 ) + dsChude.size() + dsUngDung.size())) {
                    if (didIndex != (mainRelay - 1 ) + dsChude.size()) {
                        rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size()).setBackgroundResource(R.drawable.none);
                    }
                    didIndex++;
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size()).setBackgroundResource(R.drawable.border_pick);
                } else if ((didIndex >= (mainRelay - 1 ) + dsChude.size() + dsUngDung.size())
                        && (didIndex < (mainRelay - 1 ) + dsChude.size() + dsUngDung.size() + dsChude.size())) {
                    if (didIndex == (mainRelay - 1 ) + dsChude.size() + dsUngDung.size()) {
                        rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size()).setBackgroundResource(R.drawable.none);
                    }
                    didIndex++;
                    rc_Ds3.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size()).callOnClick();
                } else if ((didIndex >= (mainRelay - 1 ) + dsChude.size() + dsUngDung.size() + dsChude.size())
                        && (didIndex < (mainRelay - 1 ) + dsChude.size() + dsUngDung.size() + dsChude.size() + dsUngDung.size())) {
                    if (didIndex != (mainRelay - 1 ) + dsChude.size() + dsUngDung.size() + dsChude.size()) {
                        rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size() - dsChude.size()).setBackgroundResource(R.drawable.none);
                    }
                    didIndex++;
                    rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size() - dsChude.size()).setBackgroundResource(R.drawable.border_pick);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (didIndex < mainRelay) {
                    listItem.get(didIndex).callOnClick();
                } else if ((didIndex >= mainRelay + dsChude.size())
                        && (didIndex < mainRelay + dsChude.size() + dsUngDung.size())){
                    rc_Ds2.getChildAt(didIndex - mainRelay - dsChude.size()).callOnClick();
                } else if ((didIndex >= mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size())
                        && (didIndex < mainRelay + dsChude.size() + dsUngDung.size() + dsChude.size() + dsUngDung.size())) {
                    rc_Ds4.getChildAt(didIndex - mainRelay - dsChude.size() - dsUngDung.size() - dsChude.size()).callOnClick();
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
}
