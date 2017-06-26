package com.att.kiwilauncher.xuly;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

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
import com.att.kiwilauncher.model.TheLoaiUngDung;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.att.kiwilauncher.TrangChu.demdsApp;
import static com.att.kiwilauncher.TrangChu.listAppBottom;
import static com.att.kiwilauncher.TrangChu.listApps;
import static com.att.kiwilauncher.TrangChu.listapp;
import static com.att.kiwilauncher.TrangChu.mListTheLoaiUngDung;
import static com.att.kiwilauncher.TrangChu.mListUngDung;

/**
 * Created by admin on 5/22/2017.
 */


public class DuLieu {

    //  public static String URL = "http://phonecase.890m.com";
    public static String URL_REQUEST = "http://phone.websumo.vn/first_request_store.php";
    public static String URL_IMAGE = "http://phone.websumo.vn/images";
    public static String URL_FILE = "http://phone.websumo.vn/files";


    public static List<ApplicationInfo> getListInstalledApplication(Context context) {
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        applicationInfoList = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        return applicationInfoList;
    }

    public static boolean checkInstalledApplication(String label, Context context) {
        List<ApplicationInfo> applicationInfoList = new ArrayList<>();
        applicationInfoList = getListInstalledApplication(context);
        for (int i = 0; i < applicationInfoList.size(); i++) {
            String appLabel = (String) context.getPackageManager().getApplicationLabel(applicationInfoList.get(i));
            if (appLabel.trim().toLowerCase().replace(" ", "").equals(label.trim().toLowerCase().replace(" ", ""))) {
                return true;
            }
        }
        return false;
    }

    public static boolean capNhatVersion(String packageName, int versionCode, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            if (pi.versionCode < versionCode) {
                return true;
            }
            return false;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getPackageName(String label, Context context) {
        String packageName = "";
        List<ApplicationInfo> listApplicationInfo = context.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        for (int i = 0; i < listApplicationInfo.size(); i++) {
            if (context.getPackageManager().getApplicationLabel(listApplicationInfo.get(i)).toString().trim().toLowerCase().replace(" ", "").equals(
                    label.trim().toLowerCase().replace(" ", ""))) {
                packageName = context.getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA).get(i).packageName;
                //Toast.makeText(context, "Ok-" + packageName, Toast.LENGTH_LONG).show();
                break;
            }
        }
        return packageName;
    }

    public static String unzip(String filePath) {
        String apkFileName = "";
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zEntry = null;
            while ((zEntry = zipStream.getNextEntry()) != null) {
                Log.d("Unzip", "Unzipping " + zEntry.getName() + " at "
                        + filePath);

                if (zEntry.isDirectory()) {
                    // cử lý trong trường hợp file nến chứa thư mục : hanldeDirectory(zEntry.getName());

                } else {
                    //unzip file ra thư mục con download trong kiwistore
                    FileOutputStream fout = new FileOutputStream(
                            Environment.getExternalStorageDirectory() + "/KiwiStore/download/" + zEntry.getName());
                    BufferedOutputStream bufout = new BufferedOutputStream(fout);
                    apkFileName = zEntry.getName();
                    byte[] buffer = new byte[1024];
                    int read = 0;
                    while ((read = zipStream.read(buffer)) != -1) {
                        bufout.write(buffer, 0, read);
                    }

                    zipStream.closeEntry();
                    bufout.close();
                    fout.close();
                }
            }
            zipStream.close();
            Log.d("Unzip", "Unzipping complete. path :  " + filePath);
        } catch (Exception e) {
            apkFileName = "";
            Log.d("Unzip", "Unzipping failed");
            e.printStackTrace();
        }
        return apkFileName;
    }

    public static void InstallAPK(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            try {
                String command;
                command = "pm install -r " + filename;
                Process proc = Runtime.getRuntime().exec(new String[]{"su", "-c", command});
                proc.waitFor();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void installApp(Context context, String apkFileName) {
        String pathToDownloadFolder = "/KiwiStore/download/";
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        install.setDataAndType(
                Uri.fromFile(new File(Environment.getExternalStorageDirectory() + pathToDownloadFolder + apkFileName)),
                "application/vnd.android.package-archive");
        context.startActivity(install);
    }

    public static void uninstallApp(String packageName, Context context) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String getFileZipName(String url) {
        String zipName = "";
        int i = url.lastIndexOf('/');
        zipName = url.substring(i + 1, url.length());
        return zipName;
    }

    public static boolean compareDate(String checkDate, String currentDate) {

        String date1 = splitDateTime(checkDate, 1)[0];
        String time1 = splitDateTime(checkDate, 1)[1];
        String date2 = splitDateTime(currentDate, 1)[0];
        String time2 = splitDateTime(currentDate, 1)[1];

        String year1 = splitDateTime(date1, 2)[0];
        String year2 = splitDateTime(date2, 2)[0];
        if (Integer.parseInt(year1) < Integer.parseInt(year2)) {
            return true;
        }
        String month1 = splitDateTime(date1, 2)[1];
        String month2 = splitDateTime(date2, 2)[1];
        if (Integer.parseInt(month1) < Integer.parseInt(month2)) {
            return true;
        }
        String day1 = splitDateTime(date1, 2)[2];
        String day2 = splitDateTime(date2, 2)[2];
        if (Integer.parseInt(day1) < Integer.parseInt(day2)) {
            return true;
        }
        String hour1 = splitDateTime(time1, 3)[0];
        String hour2 = splitDateTime(time2, 3)[0];
        if (Integer.parseInt(hour1) < Integer.parseInt(hour2)) {
            return true;
        }
        String minute1 = splitDateTime(time1, 3)[1];
        String minute2 = splitDateTime(time2, 3)[1];
        if (Integer.parseInt(minute1) < Integer.parseInt(minute2)) {
            return true;
        }
        String second1 = splitDateTime(time1, 3)[2];
        String second2 = splitDateTime(time2, 3)[2];
        if (Integer.parseInt(second1) <= Integer.parseInt(second2)) {
            return true;
        }
        return false;
    }

    public static String[] splitDateTime(String dateTime, int type) {
        String[] kq;
        if (type == 1) {
            kq = dateTime.split(" ");
        } else if (type == 2) {
            kq = dateTime.split("-");
        } else {
            kq = dateTime.split(":");
        }
        return kq;
    }

    public static String[] splitLinkVideoWeb(String linkVideoWeb) {
        // phần tử 1 là link video, phần tử 2 là link web
        String[] mangVideoWeb = linkVideoWeb.split(";");
        return mangVideoWeb;
    }
    public static String[] splitLinkImageWeb(String linkImageWeb) {
        // phần tử 1 là link video, phần tử 2 là link web
        String[] mangImageWeb = linkImageWeb.split(";");
        return mangImageWeb;
    }
    public static boolean hasInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static List<UngDung> getListUngDungByTheLoaiId(String theLoaiId, List<TheLoaiUngDung> theLoaiUngDungList, List<UngDung> ungDungList) {
        List<UngDung> ungDungListByTheLoaiId = new ArrayList<>();
        List<String> ungDungIdList = new ArrayList<>();
        for (TheLoaiUngDung theLoaiUngDung : theLoaiUngDungList) {
            if (theLoaiUngDung.getIdTheLoai().equals(theLoaiId)) {
                ungDungIdList.add(theLoaiUngDung.getIdUngDung());
            }
        }
        for (String ungDungId : ungDungIdList) {
            for (UngDung ungDung : ungDungList) {
                if (ungDung.getId().equals(ungDungId)) {
                    ungDungListByTheLoaiId.add(ungDung);
                    break;
                }
            }
        }
        return ungDungListByTheLoaiId;
    }

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
    /*public void hanldeDirectory(String dir) {
        File f = new File(this.destination + dir);
        if (!f.isDirectory()) {
            f.mkdirs();
        }
    }*/
    /*public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            *//* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            *//*
            }
        } finally {
            zis.close();
        }
    }*/
}
