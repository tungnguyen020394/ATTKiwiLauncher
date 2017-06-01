package com.att.kiwilauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.att.kiwilauncher.Adapter.ChuDeAdapter;
import com.att.kiwilauncher.Adapter.UngDungAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.att.kiwilauncher.R.id.relay2;

public class TrangChu extends AppCompatActivity implements View.OnClickListener{
    int chieuDai,chieuRong,didIndex = 0,willIndex,indexChuDe;
    RelativeLayout reLay1,reLay2,reLay3,reLay4,reLay111,reLay112,reLay113,reLay22;
    List<ChuDe> cates;
    ArrayList<View>       listItem;
    RecyclerView          rcCategory;
    static RecyclerView    rcApp;
    static PackageManager  manager;
    static List<UngDung>   apps;
    public static   View.OnClickListener appClick;
    VideoView       video;
    TextView        text;
    ImageView       image1,image2,image3,image4,image5,image6,
                    imageCaiDat,imagePlus;
    public static final int REQUEST_SETTINGS = 101;
    private static final String TAG = "TrangChu";


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
        ChuDe cate1 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate1);
        ChuDe cate2 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate2);
        ChuDe cate3 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate3);
        ChuDe cate4 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate4);
        ChuDe cate5 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate5);
        ChuDe cate6 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate6);
        ChuDe cate7 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate7);
        ChuDe cate8 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate8);
        ChuDe cate9 = new ChuDe("Đối Tác",R.drawable.play,0,false);
        cates.add(cate9);
        rcCategory.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcCategory.setLayoutManager(layoutManager1);
        ChuDeAdapter categoryAdapter = new ChuDeAdapter(this,cates);
        rcCategory.setAdapter(categoryAdapter);

        // Load App
        manager = getPackageManager();
        apps = new ArrayList<UngDung>();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);

        int soUngDung = 0;
        for (ResolveInfo ri : availableActivities) {
            UngDung app = new UngDung();
            app.labelApp  = ri.loadLabel(manager);
            app.nameApp   = ri.activityInfo.packageName;
            app.iconApp   = ri.activityInfo.loadIcon(manager);
            apps.add(app);
            soUngDung++;
            if (soUngDung == 7) {
                break;
            }
        }

        rcApp.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcApp.setLayoutManager(layoutManager);
        UngDungAdapter listapp = new UngDungAdapter(this,apps);
        rcApp.setAdapter(listapp);

        // video
        //video.setVideoPath("https://www.viettelpost.com.vn/Video/gioithieu.mp4");
        //video.start();
    }

    private void addControls() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        chieuDai = displayMetrics.widthPixels;
        chieuRong = displayMetrics.heightPixels;

        listItem = new ArrayList<>();

        reLay1 = (RelativeLayout) findViewById(R.id.relay1);
        reLay1.setPadding(20,0,20,(chieuRong*21)/25);
        reLay2 = (RelativeLayout) findViewById(relay2);
        reLay2.setPadding(20,(chieuRong*4)/25,20,(chieuRong*13)/50);
        reLay3 = (RelativeLayout) findViewById(R.id.relay3);
        reLay3.setPadding(20,(chieuRong*18)/25,20,(chieuRong*3)/25);
        reLay4 = (RelativeLayout) findViewById(R.id.relay4);
        reLay4.setPadding(20,(chieuRong*39)/50,20,0);

        rcCategory = (RecyclerView) findViewById(R.id.recycler1);
        rcApp      = (RecyclerView) findViewById(R.id.recycler2);

        video = (VideoView) findViewById(R.id.videoView);
        reLay22 = (RelativeLayout) findViewById(R.id.relay22);

        text = (TextView) findViewById(R.id.text1);
        text.setSelected(true);


        reLay111 = (RelativeLayout) findViewById(R.id.relay111);
        reLay112 = (RelativeLayout) findViewById(R.id.relay112);
        reLay113 = (RelativeLayout) findViewById(R.id.relay113);

        reLay113.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageCaiDat.callOnClick();
            }
        });


        image1 = (ImageView) findViewById(R.id.img_tv);
        image2 = (ImageView) findViewById(R.id.img_phim);
        image3 = (ImageView) findViewById(R.id.img_nhac);
        image4 = (ImageView) findViewById(R.id.img_kara);
        image5 = (ImageView) findViewById(R.id.img_youtube);
        image6 = (ImageView) findViewById(R.id.img_store);

        imageCaiDat = (ImageView) findViewById(R.id.img_caidat);
        imageCaiDat.setOnClickListener(this);

        imagePlus = (ImageView) findViewById(R.id.img_plus);

        imagePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TrangChu.this,DanhSach.class);
                i.putExtra("ChuDe",indexChuDe);
                startActivity(i);
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.stopPlayback();
                Intent intent = new Intent(TrangChu.this,Viettel.class);
                intent.putExtra("link","https://www.viettelpost.com.vn");
                intent.putExtra("text","Truyền Thông ");
                startActivity(intent);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.stopPlayback();
                Intent intent = new Intent(TrangChu.this,Viettel.class);
                intent.putExtra("link","https://www.viettelpost.com.vn/Tracking");
                intent.putExtra("text","tra cứu vận đơn");
                startActivity(intent);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.stopPlayback();
                Intent intent = new Intent(TrangChu.this,Viettel.class);
                intent.putExtra("link","https://www.viettelpost.com.vn/Services");
                intent.putExtra("text","Thông Báo");
                startActivity(intent);
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChu.this,DanhSach.class);
                startActivity(intent);
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image5.clearAnimation();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                text.setSelected(true);
                if ((didIndex >= 4) && (didIndex < 12)) {
                    willIndex = didIndex - 4;
                    changeListItemBackGround(didIndex,willIndex);
                } else if ((didIndex >= 12) && (didIndex < 12 + cates.size())) {
                    rcCategory.getChildAt(didIndex - 12).callOnClick();
                    if ((didIndex - 4) < 12) {
                        didIndex = didIndex - 4;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    } else {
                        didIndex = 11;
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                    }
                } else if ((didIndex >= 12 + cates.size()) && (didIndex < 12 + cates.size() + apps.size())){
                    rcApp.getChildAt(didIndex - 12 - cates.size()).setBackgroundResource(R.drawable.none);
                    didIndex = 12;
                    rcCategory.getChildAt(0).callOnClick();
                } else if (didIndex == listItem.size() - 1) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex = 11 + cates.size();
                    rcCategory.getChildAt(didIndex - 12).callOnClick();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                text.setSelected(true);
                if ((didIndex >= 0) && (didIndex < 8)) {
                    willIndex = didIndex + 4;
                    changeListItemBackGround(didIndex,willIndex);
                } else if ((didIndex >= 8) && (didIndex < 12)) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex = 12;
                    rcCategory.getChildAt(0).callOnClick();
                } else if ((didIndex >=12) && (didIndex < 12 + cates.size())) {
                    indexChuDe = didIndex - 12;
                    didIndex = 12 + cates.size();
                    rcApp.getChildAt(0).setBackgroundResource(R.drawable.border_pick);
                }
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                text.setSelected(true);
                if ((didIndex > 0) && (didIndex < 13)) {
                    if (didIndex == 12) {
                        didIndex--;
                        rcCategory.getChildAt(0).setBackgroundResource(R.drawable.none);
                        listItem.get(didIndex).setBackgroundResource(R.drawable.border_pick);
                        return true;
                    }
                    willIndex = didIndex - 1;
                    changeListItemBackGround(didIndex, willIndex);
                } else if ((didIndex >= 13) && (didIndex < (13 + cates.size()))) {
                    if (didIndex == (12 + cates.size())) {
                        rcApp.getChildAt(0).setBackgroundResource(R.drawable.none);
                    }
                    didIndex--;
                    rcCategory.getChildAt(didIndex - 12).callOnClick();
                    indexChuDe = didIndex - 12;
                } else if ((didIndex >= (13 + cates.size())) && (didIndex < (12 + cates.size() + apps.size()))) {
                    rcApp.getChildAt(didIndex - 12 - cates.size()).setBackgroundResource(R.drawable.none);
                    listItem.get(listItem.size() - 1).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    rcApp.getChildAt(didIndex - 12 - cates.size()).setBackgroundResource(R.drawable.border_pick);
                    Log.e("hihi", didIndex + " ha ha ha ha");
                } else if (didIndex == (listItem.size() - 1)) {
                    listItem.get(didIndex).setBackgroundResource(R.drawable.none);
                    didIndex--;
                    rcApp.getChildAt(didIndex-12-cates.size()).setBackgroundResource(R.drawable.border_pick);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                text.setSelected(true);
                if (didIndex < 11 ) {
                    willIndex = didIndex + 1;
                    changeListItemBackGround(didIndex, willIndex);
                } else if (didIndex < (11 + cates.size())) {
                    listItem.get(11).setBackgroundResource(R.drawable.none);
                    didIndex++;
                    rcCategory.getChildAt(didIndex - 12).callOnClick();
                    indexChuDe = didIndex - 12;
                } else if (didIndex < (11 + cates.size() + apps.size())) {
                    if (didIndex != 11 + cates.size()) {
                        rcApp.getChildAt(didIndex - 11 - cates.size() - 1).setBackgroundResource(R.drawable.none);
                    }
                    didIndex++;
                    rcApp.getChildAt(didIndex - 12 - cates.size()).setBackgroundResource(R.drawable.border_pick);
                } else if(didIndex == (listItem.size() - 2)) {
                    rcApp.getChildAt(didIndex - 12 - cates.size()).setBackgroundResource(R.drawable.none);
                    listItem.get(listItem.size() - 1).setBackgroundResource(R.drawable.border_pick);
                    didIndex++;
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_CENTER:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
                if (didIndex < 12) {
                    listItem.get(didIndex).callOnClick();
                } else if (didIndex < (12 + cates.size())) {
                    rcCategory.getChildAt(didIndex - 12).callOnClick();
                } else if (didIndex == listItem.size() - 1) {
                    listItem.get(didIndex).callOnClick();
                } else {
                    rcApp.getChildAt(didIndex - 12 - cates.size()).callOnClick();
                }
            default:
                return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_caidat:
                TrangChu.this.startActivityForResult(new Intent(Settings.ACTION_SETTINGS),REQUEST_SETTINGS);
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

    public static class AppClick implements View.OnClickListener{

        private final Context context;

        public AppClick(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int position = rcApp.getChildPosition(v);
            Intent i = manager.getLaunchIntentForPackage(apps.get(position).getNameApp().toString());
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
        listItem.add(reLay22);
        listItem.add(image4);
        listItem.add(image5);
        listItem.add(image6);
        listItem.add(reLay22);

        int soChuDe = 0;
        for (ChuDe c : cates) {
            listItem.add(rcCategory.getChildAt(soChuDe));
            soChuDe ++;
        }

        int soUngDung = 0;
        for (UngDung app : apps ) {
            listItem.add(rcApp.getChildAt(soUngDung));
        }

        listItem.add(imagePlus);
    }

    public void changeListItemBackGround(int i,int j) {
        if (i < 12) {
            listItem.get(i).setBackgroundResource(R.drawable.none);
            listItem.get(j).setBackgroundResource(R.drawable.border_pick);
            didIndex = willIndex;
        }
    }
}
