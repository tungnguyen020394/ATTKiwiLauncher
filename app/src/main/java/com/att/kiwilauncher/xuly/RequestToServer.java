package com.att.kiwilauncher.xuly;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.att.kiwilauncher.UngDung;
import com.att.kiwilauncher.adapter.ChuDeAdapter;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.model.QuangCao;
import com.att.kiwilauncher.model.ThoiTiet;
import com.att.kiwilauncher.util.Define;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

/**
 * Created by admin on 6/22/2017.
 */

public class RequestToServer {
    public static StringRequest createRequestAndUpdate(final ProgressDialog dialog, final DatabaseHelper mDatabaseHelper, final HashMap<String, List> mAllListMap,
                                                       final List<QuangCao> mListQuangCao, final List<ChuDe> cates, final ChuDeAdapter categoryAdapter,
                                                       final TextView text, final Context context) {

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
                                        if (DuLieu.checkInstalledApplication(app.getString("ten"), context)) {
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
                                                , app.getString("rating"), app.getString("version_code"), update);
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
                mAllListMap.clear();
                mAllListMap.putAll(mDatabaseHelper.getAllList());
                mListQuangCao.clear();
                mListQuangCao.addAll(mAllListMap.get("quangcao"));
                mListUngDung.clear();
                mListUngDung.addAll(mAllListMap.get("ungdung"));
                mListTheLoaiUngDung.clear();
                mListTheLoaiUngDung.addAll(mAllListMap.get("theloaiungdung"));
                cates.clear();
                cates.addAll(mAllListMap.get("theloai"));
                dialog.dismiss();

                for (QuangCao quangCao : mListQuangCao) {
                    if (quangCao.getLoaiQuangCao().equals("3")) {
                        text.setText(quangCao.getText());
                        break;
                    }
                }

                categoryAdapter.notifyDataSetChanged();
                listAppBottom.clear();
                listApps.clear();
                List<UngDung> checkedList;
                checkedList = mDatabaseHelper.getListUngDung(cates.get(0));
                List<UngDung> tmpList = new ArrayList<>();
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
                values.put("capnhatid", (String) mAllListMap.get("capnhat").get(0));
                return values;
            }
        };
        return request;
    }

    public static StringRequest createWeatherRequest(ThoiTiet mThoiTiet, final String todayFormated, final TextView mTxtNhietDo, final SharedPreferences.Editor editor){
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
                            mTxtNhietDo.setText(Math.round(Double.parseDouble(nhietDo)) + "");
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