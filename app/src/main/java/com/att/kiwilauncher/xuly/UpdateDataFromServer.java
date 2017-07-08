package com.att.kiwilauncher.xuly;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.att.kiwilauncher.util.Define;

/**
 * Created by admin on 7/3/2017.
 */

public class UpdateDataFromServer {
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private ContentValues mValues;

    public UpdateDataFromServer(SQLiteDatabase mDatabase, Context mContext) {
        this.mContext = mContext;
        this.mDatabase = mDatabase;
    }
    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(Define.DB_NAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }
    public void insertQuangCao(String id, String noiDung, String loaiQuangCaoId) {
        openDatabase();
        mValues = new ContentValues();
        mValues.put("id", Integer.parseInt(id));
        mValues.put("noidung", noiDung);
        mValues.put("loaiquangcao", Integer.parseInt(loaiQuangCaoId));
        mDatabase.insert("quangcao", null, mValues);
        closeDatabase();
    }

    public void deleteQuangCao() {
        openDatabase();
        mDatabase.delete("quangcao", "1", null);
        closeDatabase();
    }

    public void insertApp(String id, String ten, int installed, String icon, String luotcai, String versions, String des,
                          String linkcai, String rating, String versionCode, int update, String packageName) {
        openDatabase();
        mValues = new ContentValues();
        mValues.put("id", Integer.parseInt(id));
        mValues.put("ten", ten);
        mValues.put("installed", installed);
        mValues.put("icon", icon);
        mValues.put("luotcai", luotcai);
        mValues.put("version", versions);
        mValues.put("des", des);
        mValues.put("linkcai", linkcai);
        mValues.put("rating", rating);
        mValues.put("version_code", versionCode);
        mValues.put("capnhat", update);
        mValues.put("packageName", packageName);
        mDatabase.insert("ungdung", null, mValues);
        closeDatabase();
    }

    public void updateApp(int installed, String id) {
        ContentValues values = new ContentValues();
        values.put("installed", installed);
        mDatabase.update("ungdung", values, "id = " + id, null);
    }

    public void deleteApp() {
        openDatabase();
        mDatabase.delete("ungdung", "1", null);
        closeDatabase();

    }

    public void insertTheLoaiUngDung(String id, String theloaiid, String ungdungid) {
        openDatabase();
        mValues = new ContentValues();
        mValues.put("id", Integer.parseInt(id));
        mValues.put("theloaiid", theloaiid);
        mValues.put("ungdungid", ungdungid);
        mDatabase.insert("theloai_ungdung", null, mValues);
        closeDatabase();
    }

    public void deleteTheLoaiUngDung() {
        openDatabase();
        mDatabase.delete("theloai_ungdung", "1", null);
        closeDatabase();
    }

    public void insertTheLoai(String id, String ten, String soLuong, String icon) {
        openDatabase();
        mValues = new ContentValues();
        mValues.put("id", Integer.parseInt(id));
        mValues.put("ten", ten);
        mValues.put("soLuong", Integer.parseInt(soLuong));
        mValues.put("icon", icon);
        mDatabase.insert("theloai", null, mValues);
    }

    public void deleteTheLoai() {
        openDatabase();
        mDatabase.delete("theloai", "1", null);
        closeDatabase();
    }

    public void insertAnhChiTiet(String id, String ungdungid, String ten) {
        openDatabase();
        mValues = new ContentValues();
        mValues.put("id", Integer.parseInt(id));
        mValues.put("ungdungid", ungdungid);
        mValues.put("ten", ten);
        mDatabase.insert("anhchitiet", null, mValues);
        closeDatabase();
    }

    public void deleteAnhChiTiet() {
        openDatabase();
        mDatabase.delete("anhchitiet", "1", null);
        closeDatabase();
    }

    public void insertCapNhat(String id, String value) {
        openDatabase();
        mValues = new ContentValues();
        mValues.put("id", Integer.parseInt(id));
        mValues.put("value", value);
        mDatabase.insert("capnhat", null, mValues);
        closeDatabase();
    }

    public void deleteCapNhat() {
        openDatabase();
        mDatabase.delete("capnhat", "1", null);
        closeDatabase();
    }
}
