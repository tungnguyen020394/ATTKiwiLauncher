package com.att.kiwilauncher.xuly;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.att.kiwilauncher.TrangChu;
import com.att.kiwilauncher.UngDung;
import com.att.kiwilauncher.adapter.ChuDeAdapter;
import com.att.kiwilauncher.adapter.UngDungAdapter;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.model.QuangCao;
import com.att.kiwilauncher.model.ThoiTiet;
import com.att.kiwilauncher.util.DateCompare;
import com.att.kiwilauncher.util.Define;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.att.kiwilauncher.TrangChu.demdsApp;
import static com.att.kiwilauncher.TrangChu.listAppBottom;
import static com.att.kiwilauncher.TrangChu.listApps;
import static com.att.kiwilauncher.TrangChu.listapp;
import static com.att.kiwilauncher.TrangChu.mListTheLoaiUngDung;
import static com.att.kiwilauncher.TrangChu.mListUngDung;
import static com.att.kiwilauncher.xuly.DuLieu.checkInstalledApplication;

/**
 * Created by admin on 6/22/2017.
 */
public class RequestToServer {
    private DatabaseHelper databaseHelper;
    private boolean update = false;
    Context context;
    JSONArray root;
    SQLiteDatabase mDatabase;
    UpdateDataFromServer updateDataFromServer;
    UngDungAdapter ungDungAdapter;
    ProgressDialog dialog;
    List<QuangCao> mListQuangCao;
    List<ChuDe> cates;
    ChuDeAdapter categoryAdapter;
    TextView text;
    List<QuangCao> mListVideoAd;

    public RequestToServer(DatabaseHelper mDatabaseHelper, Context context, ProgressDialog dialog,
                           List<QuangCao> mListQuangCao, List<ChuDe> cates, ChuDeAdapter categoryAdapter,
                           TextView text, List<QuangCao> mListVideoAd) {
        this.databaseHelper = mDatabaseHelper;
        this.context = context;
        this.mDatabase = databaseHelper.getmDatabase();
        this.updateDataFromServer = new UpdateDataFromServer(mDatabase, context);
        this.dialog = dialog;
        this.mListQuangCao = mListQuangCao;
        this.cates = cates;
        this.categoryAdapter = categoryAdapter;
        this.text = text;
        this.mListVideoAd = mListVideoAd;
    }

    public StringRequest UpdateTheLoaiRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, Define.API_THELOAI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(context,response,Toast.LENGTH_SHORT).show();
                try {
                    root = new JSONArray(response);
                    updateDataFromServer.deleteTheLoai();
                    for (int j = 0; j < root.length(); j++) {
                        JSONObject app = root.getJSONObject(j);
                        // updateDataFromServer.insertTheLoai(app.getString("PkTheLoaiId"), app.getString("Ten"), app.getString("soluong"), app.getString("Icon"));
                        updateDataFromServer.insertTheLoai(app.getString("id"), app.getString("ten"),
                                app.getString("dem_ung_dung"), app.getString("icon"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*theLoais.clear();
                theLoais.addAll(databaseHelper.getAllTheLoai());*/
                //   Toast.makeText(context, theLoais.size() + "tl s2", Toast.LENGTH_SHORT).show();
                uploadData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        return request;
    }

    public StringRequest UpdateUngDungRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, Define.API_UNGDUNG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    root = new JSONArray(response);
                    updateDataFromServer.deleteApp();
                    for (int j = 0; j < root.length(); j++) {
                        JSONObject app = root.getJSONObject(j);
                        int install, update;
                        install = update = 0;
                        if (checkInstalledApplication(app.getString("packageName"), context)) {
                            install = 1;
                            if (AppInfoHelper.capNhatVersion(app.getString("packageName"), app.getInt("versioncode"), context)) {
                                update = 1;
                            } else {
                                update = 0;
                            }
                        }
                        // updateDataFromServer.insertTheLoai(app.getString("PkTheLoaiId"), app.getString("Ten"), app.getString("soluong"), app.getString("Icon"));
                        updateDataFromServer.insertApp(app.getString("id"), app.getString("ten")
                                , install, Define.URL_IMAGE + "/" + app.getString("icon")
                                , app.getString("luotcai"), app.getString("version")
                                , app.getString("des"), Define.URL_FILE + "/" + app.getString("linkcai")
                                , app.getString("rating"), app.getString("versioncode"), update, app.getString("packageName"));

                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
               /* ungDungs.clear();
                ungDungs.addAll(databaseHelper.getAllUngDung());*/
                //   Toast.makeText(context, ungDungs.size() + "ud s2", Toast.LENGTH_SHORT).show();
                uploadData();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        });
        return request;
    }

    public StringRequest UpdateQuangCaoRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, Define.API_QUANGCAO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    root = new JSONArray(response);
                    updateDataFromServer.deleteQuangCao();
                    for (int j = 0; j < root.length(); j++) {
                        JSONObject app = root.getJSONObject(j);
                        // updateDataFromServer.insertTheLoai(app.getString("PkTheLoaiId"), app.getString("Ten"), app.getString("soluong"), app.getString("Icon"));
                        updateDataFromServer.insertQuangCao(app.getString("id"), app.getString("noidung"), app.getString("loaiquangcaoid"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                uploadData();
                //   Toast.makeText(context,databaseHelper.getLinkAnhQuangCao(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        });
        return request;
    }

    public StringRequest UpdateTheLoaiUngDungRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, Define.API_THELOAI_UNGDUNG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    root = new JSONArray(response);
                    updateDataFromServer.deleteTheLoaiUngDung();
                    for (int j = 0; j < root.length(); j++) {
                        JSONObject app = root.getJSONObject(j);
                        // updateDataFromServer.insertTheLoai(app.getString("PkTheLoaiId"), app.getString("Ten"), app.getString("soluong"), app.getString("Icon"));
                        updateDataFromServer.insertTheLoaiUngDung(app.getString("id"),
                                app.getString("theloaiid"), app.getString("ungdungid"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
          /*      ungDung_theLoais.clear();
                ungDung_theLoais.addAll(databaseHelper.getAllTheLoaiUngDung());*/
                //    Toast.makeText(context, ungDung_theLoais.size() + "udtl s2", Toast.LENGTH_SHORT).show();
                uploadData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        });
        return request;
    }

    public StringRequest UpdateAnhChiTietRequest() {
        StringRequest request = new StringRequest(Request.Method.GET, Define.API_ANHCHITIET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    root = new JSONArray(response);
                    updateDataFromServer.deleteAnhChiTiet();
                    for (int j = 0; j < root.length(); j++) {
                        JSONObject app = root.getJSONObject(j);
                        // updateDataFromServer.insertTheLoai(app.getString("PkTheLoaiId"), app.getString("Ten"), app.getString("soluong"), app.getString("Icon"));
                        updateDataFromServer.insertAnhChiTiet(app.getString("id"), app.getString("ungdungid"), Define.URL_IMAGE +
                                "/" + app.getString("ten"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
   /*             imagerViewApps.clear();
                imagerViewApps.addAll(databaseHelper.getAllImagerViewApp());*/
                //    Toast.makeText(context, imagerViewApps.size() + "anh s2", Toast.LENGTH_SHORT).show();
                uploadData();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.toString());
            }
        });
        return request;
    }

    public StringRequest RequestForWeather(final String idThoiTiet, final String todayFormated, final TextView mTxtNhietDo, final Context context) {
        StringRequest request = new StringRequest(Request.Method.GET, Define.URL_WEATHER + idThoiTiet
                + "&APPID=" + Define.APIKEY + "&&units=metric", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("URL", Define.URL_WEATHER + idThoiTiet
                            + "&APPID=" + Define.APIKEY + "&&units=metric");
                    JSONObject root = new JSONObject(response);
                    JSONArray listThoiTiet = root.getJSONArray("list");
                    int currentPos = 0;
                    for (int i = 0; i < listThoiTiet.length(); i++) {
                        JSONObject thoiTiet = listThoiTiet.getJSONObject(i);
                        String time = thoiTiet.getString("dt_txt");
                        if (DateCompare.compareDate(todayFormated, time)) {
                            if (i == 0) {
                                currentPos = 0;
                            } else {
                                currentPos = i - 1;
                            }
                            thoiTiet = listThoiTiet.getJSONObject(currentPos);
                            String nhietDo = thoiTiet.getJSONObject("main").getString("temp");
                            // String trangThai = thoiTiet.getJSONArray("weather").getJSONObject(0).getString("main");
                            mTxtNhietDo.setText(Math.round(Double.parseDouble(nhietDo)) + " ° C");
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Lỗi kết nối mạng" + error.toString(), Toast.LENGTH_SHORT).show();
                Log.e("Error", error.toString());
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
            }
        });
        return request;
    }

    private void uploadData() {
        if (TrangChu.requestNum < 4) {
            TrangChu.requestNum++;

        } else {
            TrangChu.requestNum = 0;
            dialog.dismiss();
          /*  theLoaiAdapter.notifyDataSetChanged();
            ungDungAdapter.notifyDataSetChanged();*/
        }
    }

    /* public StringRequest UpdateDataRequest(final Context context,
                                            final String mIdCapNhat, final AlertDialog dialog,
                                            final ArrayList<TheLoai> theLoais, final ArrayList<UngDung> ungDungs,
                                            final ArrayList<UngDung_TheLoai> ungDung_theLoais,
                                            final ArrayList<ImagerViewApp> imagerViewApps, final TheLoaiAdapter theLoaiAdapter,
                                            final UngDungAdapter ungDungAdapter) {
         StringRequest request = new StringRequest(Request.Method.POST, Define.URL, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 try {
                     databaseHelper.checkDatabase(context);
                     JSONArray root = new JSONArray(response);
                     databaseHelper.openDatabase();
                     SQLiteDatabase mDatabase = databaseHelper.getmDatabase();
                     UpdateDataFromServer updateDataFromServer = new UpdateDataFromServer(mDatabase, context);
                     for (int i = 0; i < root.length(); i++) {
                         JSONObject capnhat = root.getJSONObject(i);
                         String isCapNhat = capnhat.getString("is_cap_nhat");
                         if (isCapNhat.equals("0")) {
                             break;
                         } else {
                             update = true;
                             String loaiCapNhat = capnhat.getString("loai");
                             switch (loaiCapNhat) {
                                 case "quangcao":
                                     JSONArray rootQC = capnhat.getJSONArray("value");
                                     updateDataFromServer.deleteQuangCao();
                                     for (int j = 0; j < rootQC.length(); j++) {
                                         JSONObject app = rootQC.getJSONObject(j);
                                         updateDataFromServer.insertQuangCao(app.getString("id"), app.getString("noidung"), app.getString("loaiquangcaoid"));
                                     }
                                    *//* Glide.with(MainActivity.this).load(DuLieu.URL_IMAGE + "/" + mDatabaseHelper.getLinkAnhQuangCao()).asBitmap().into(new SimpleTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                            Drawable ad = new BitmapDrawable(resource);
                                            mAnhQuangCao.setBackground(ad);
                                        }
                                    });*//*
                                    //    Toast.makeText(getApplicationContext(), mDatabaseHelper.getMaxQuangCaoId() + " == max quang cao", Toast.LENGTH_SHORT).show();

                                    break;
                                case "ungdung":
                                    JSONArray rootApp = capnhat.getJSONArray("value");
                                    updateDataFromServer.deleteApp();
                                    for (int j = 0; j < rootApp.length(); j++) {
                                        JSONObject app = rootApp.getJSONObject(j);
                                        int install, update;
                                        install = update = 0;
                                        if (checkInstalledApplication(app.getString("package_name"), context)) {
                                            install = 1;
                                            if (AppInfoHelper.capNhatVersion(app.getString("package_name"), app.getInt("version_code"), context)) {
                                                update = 1;
                                            } else {
                                                update = 0;
                                            }
                                        }
                                        updateDataFromServer.insertApp(app.getString("id"), app.getString("ten")
                                                , install, Define.URL_IMAGE + "/" + app.getString("icon")
                                                , app.getString("luotcai"), app.getString("version")
                                                , app.getString("des"), Define.URL_FILE + "/" + app.getString("linkcai")
                                                , app.getString("rating"), app.getString("version_code"), update, app.getString("package_name"));
                                    }
                                  *//*  mListUngDung.clear();
                                    mListUngDung.addAll(mDatabaseHelper.getListUngDung(mListTheLoai.get(0)));
                                    mUngDungAdapter.notifyDataSetChanged();*//*
                                    //  Toast.makeText(getApplicationContext(), mDatabaseHelper.testInsertApp() + " == max app", Toast.LENGTH_SHORT).show();
                                    break;
                                case "luotcai":
                                    break;
                                case "anhchitiet":
                                    JSONArray rootAnhChiTiet = capnhat.getJSONArray("value");
                                    updateDataFromServer.deleteAnhChiTiet();
                                    for (int j = 0; j < rootAnhChiTiet.length(); j++) {
                                        JSONObject app = rootAnhChiTiet.getJSONObject(j);
                                        updateDataFromServer.insertAnhChiTiet(app.getString("id"), app.getString("ungdungid"), Define.URL_IMAGE +
                                                "/" + app.getString("ten"));
                                    }
                                    //mDatabaseHelper.getListAnhChiTietUngDung(new UngDung("2", "", false, "", new ArrayList<String>(), "", "", "", "",""));
                                    *//*Toast.makeText(getApplicationContext(),mDatabaseHelper.getListAnhChiTietUngDung(new UngDung("2","",false,"",
                                            new ArrayList<String>(),"","","","")).size()+"anh" , Toast.LENGTH_LONG).show();*//*
                                    //Toast.makeText(getApplicationContext(), mDatabaseHelper.testAnhChiTiet() + " == max anh chi tiet ", Toast.LENGTH_LONG).show();
                                    break;
                                case "theloai_ungdung":
                                    JSONArray rootTheLoaiUngDung = capnhat.getJSONArray("value");
                                    updateDataFromServer.deleteTheLoaiUngDung();
                                    for (int j = 0; j < rootTheLoaiUngDung.length(); j++) {
                                        JSONObject app = rootTheLoaiUngDung.getJSONObject(j);
                                        updateDataFromServer.insertTheLoaiUngDung(app.getString("id"), app.getString("theloaiid"), app.getString("ungdungid"));
                                    }
                                    // Toast.makeText(getApplicationContext(), mDatabaseHelper.testTheLoaiUngDung() + " == max the loai ung dung", Toast.LENGTH_SHORT).show();
                                    break;
                                case "theloai":
                                    JSONArray rootTheLoai = capnhat.getJSONArray("value");
                                    updateDataFromServer.deleteTheLoai();
                                    for (int j = 0; j < rootTheLoai.length(); j++) {
                                        JSONObject app = rootTheLoai.getJSONObject(j);
                                        updateDataFromServer.insertTheLoai(app.getString("id"), app.getString("ten"), app.getString("soluong"), app.getString("icon"));
                                    }
                                    //  Toast.makeText(getApplicationContext(), mDatabaseHelper.testTheLoai() + " == max the loai ", Toast.LENGTH_SHORT).show();
                                  *//*  mListTheLoai.clear();
                                    mListTheLoai.addAll(mDatabaseHelper.getListTheLoai());
                                    mTheLoaiAdapter.notifyDataSetChanged();*//*
                                    break;
                                case "capnhat":
                                    JSONArray rootCapNhat = capnhat.getJSONArray("value");
                                    updateDataFromServer.deleteCapNhat();
                                    JSONObject app = rootCapNhat.getJSONObject(0);
                                    updateDataFromServer.insertCapNhat(app.getString("id"), app.getString("id"));
                                    // Toast.makeText(getApplicationContext(), mDatabaseHelper.testCapNhat() + " == test cap nhat ", Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (update == true) {
                    theLoais.clear();
                    ungDungs.clear();
                    ungDung_theLoais.clear();
                    imagerViewApps.clear();
                    theLoais.addAll(databaseHelper.getAllTheLoai());
                    ungDungs.addAll(databaseHelper.getAllUngDung());
                    imagerViewApps.addAll(databaseHelper.getAllImagerViewApp());
                    ungDung_theLoais.addAll(databaseHelper.getAllTheLoaiUngDung());
                    theLoaiAdapter.notifyDataSetChanged();
                    ungDungAdapter.notifyDataSetChanged();
                }
                databaseHelper.closeDatabase();
                dialog.dismiss();
                //   updateData();
                //  mRecyclerViewUngDung.getChildAt(4).callOnClick();
                //   Toast.makeText(MainActivity.this,"sl"+mRecyclerViewUngDung.getChildCount(),Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                        // Now you can use any deserializer to make sense of data
                        JSONObject obj = new JSONObject(res);
                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    } catch (JSONException e2) {
                        // returned data is not JSONObject?
                        e2.printStackTrace();
                    }
                }
                //   updateData();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap<>();
                values.put("capnhatid", mIdCapNhat);
                return values;
            }
        };
        return request;
    }*/
    public static StringRequest createRequestAndUpdate(final ProgressDialog dialog, final DatabaseHelper mDatabaseHelper, final HashMap<String, List> mAllListMap,
                                                       final List<QuangCao> mListQuangCao, final List<ChuDe> cates, final ChuDeAdapter categoryAdapter,
                                                       final TextView text, final Context context, final String idCapNhat, final List<QuangCao> mListVideoAd) {

        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, DuLieu.URL_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray root = new JSONArray(response);
                    for (int i = 0; i < root.length(); i++) {
                        JSONObject capnhat = root.getJSONObject(i);
                        String isCapNhat = capnhat.getString("is_cap_nhat");
                        if (isCapNhat.equals("0")) {
                            break;
                        } else {
                            String loaiCapNhat = capnhat.getString("loai");
                            switch (loaiCapNhat) {
                                case "quangcao":
                                    JSONArray rootQC = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteQuangCao();
                                    for (int j = 0; j < rootQC.length(); j++) {
                                        JSONObject app = rootQC.getJSONObject(j);
                                        mDatabaseHelper.insertQuangCao(app.getString("id"), app.getString("noidung"), app.getString("loaiquangcaoid"));
                                    }
                                    break;
                                case "ungdung":
                                    JSONArray rootApp = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteListApp();
                                    for (int j = 0; j < rootApp.length(); j++) {
                                        JSONObject app = rootApp.getJSONObject(j);
                                        int install, update;
                                        install = update = 0;
                                        if (checkInstalledApplication(app.getString("ten"), context)) {
                                            install = 1;
                                            if (DuLieu.capNhatVersion(DuLieu.getPackageName(app.getString("ten"), context), app.getInt("version_code"), context)) {
                                                update = 1;
                                            } else {
                                                update = 0;
                                            }
                                        }
                                        mDatabaseHelper.insertApp(app.getString("id"), app.getString("ten")
                                                , install, DuLieu.URL_IMAGE + "/" + app.getString("icon")
                                                , app.getString("luotcai"), app.getString("version")
                                                , app.getString("des"), DuLieu.URL_FILE + "/" + app.getString("linkcai")
                                                , app.getString("rating"), app.getString("version_code"), update, app.getString("packageName"));
                                    }
                                    //  Toast.makeText(getApplicationContext(), mDatabaseHelper.testInsertApp() + " == max app", Toast.LENGTH_SHORT).show();
                                    break;
                                case "luotcai":
                                    break;
                                case "anhchitiet":
                                    JSONArray rootAnhChiTiet = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteAnhChiTiet();
                                    for (int j = 0; j < rootAnhChiTiet.length(); j++) {
                                        JSONObject app = rootAnhChiTiet.getJSONObject(j);
                                        mDatabaseHelper.insertAnhChiTiet(app.getString("id"), app.getString("ungdungid"), DuLieu.URL_IMAGE +
                                                "/" + app.getString("ten"));
                                    }
                                    break;
                                case "theloai_ungdung":
                                    JSONArray rootTheLoaiUngDung = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteTheLoaiUngDung();
                                    for (int j = 0; j < rootTheLoaiUngDung.length(); j++) {
                                        JSONObject app = rootTheLoaiUngDung.getJSONObject(j);
                                        mDatabaseHelper.insertTheLoaiUngDung(app.getString("id"), app.getString("theloaiid"), app.getString("ungdungid"));
                                    }
                                    break;
                                case "theloai":
                                    JSONArray rootTheLoai = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteTheLoai();
                                    for (int j = 0; j < rootTheLoai.length(); j++) {
                                        JSONObject app = rootTheLoai.getJSONObject(j);
                                        mDatabaseHelper.insertTheLoai(app.getString("id"), app.getString("ten"), app.getString("soluong"), app.getString("icon"));
                                    }
                                    break;
                                case "capnhat":
                                    JSONArray rootCapNhat = capnhat.getJSONArray("value");
                                    mDatabaseHelper.deleteCapNhat();
                                    JSONObject app = rootCapNhat.getJSONObject(0);
                                    mDatabaseHelper.insertCapNhat(app.getString("id"), app.getString("id"));
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                /*mAllListMap.clear();
                mAllListMap.putAll(mDatabaseHelper.getAllList());*/
                mListQuangCao.clear();
                //  mListQuangCao.addAll(mAllListMap.get("quangcao"));
                mListQuangCao.addAll(mDatabaseHelper.getListQuangCao());
                mListUngDung.clear();
                // mListUngDung.addAll(mAllListMap.get("ungdung"));
                mListUngDung.addAll(mDatabaseHelper.getListUngDungV2());
                mListTheLoaiUngDung.clear();
                // mListTheLoaiUngDung.addAll(mAllListMap.get("theloaiungdung"));
                mListTheLoaiUngDung.addAll(mDatabaseHelper.getListTheLoaiUngDung());
                cates.clear();
                // cates.addAll(mAllListMap.get("theloai"));
                cates.addAll(mDatabaseHelper.getListChuDe());
                dialog.dismiss();
                if (!DuLieu.getAdTextFromList(mListQuangCao).equals("")) {
                    text.setText(DuLieu.getAdTextFromList(mListQuangCao));
                }
                categoryAdapter.notifyDataSetChanged();
                listAppBottom.clear();
                listApps.clear();
                List<UngDung> checkedList;
                checkedList = mDatabaseHelper.getListUngDung(cates.get(0));
                List<UngDung> tmpList = new ArrayList<>();
                mListVideoAd.clear();
                mListVideoAd.addAll(DuLieu.getAdVideoFromList(mListQuangCao));
                // Toast.makeText(context,mListVideoAd.size()+"s",Toast.LENGTH_SHORT).show();
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
                demdsApp = 0;
                listAppBottom.addAll(listApps.get(demdsApp));
                listapp.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> values = new HashMap<>();
                //    values.put("capnhatid", (String) mAllListMap.get("capnhat").get(0));
                values.put("capnhatid", idCapNhat);
                return values;
            }
        };
        return request;
    }

    public static StringRequest createWeatherRequest(ThoiTiet mThoiTiet, final String todayFormated, final TextView mTxtNhietDo, final SharedPreferences.Editor editor) {
        String url = Define.URL_LLNK_WEATHER_API + mThoiTiet.getMaThoiTiet() + "&APPID=" + Define.APIKEY + "&&units=metric";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray listThoiTiet = root.getJSONArray("list");
                    int currentPos = 0;
                    for (int i = 0; i < listThoiTiet.length(); i++) {
                        JSONObject thoiTiet = listThoiTiet.getJSONObject(i);
                        String time = thoiTiet.getString("dt_txt");

                        if (DuLieu.compareDate(todayFormated, time)) {
                            if (i == 0) {
                                currentPos = 0;
                            } else {
                                currentPos = i - 1;
                            }
                            thoiTiet = listThoiTiet.getJSONObject(currentPos);
                            String nhietDo = thoiTiet.getJSONObject("main").getString("temp");
                            String trangThai = thoiTiet.getJSONArray("weather").getJSONObject(0).getString("main");
                            mTxtNhietDo.setText(Math.round(Double.parseDouble(nhietDo)) + " ° C");
                            editor.putString("nhietdo", Math.round(Double.parseDouble(nhietDo)) + "");
                            editor.commit();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        return request;
    }
}