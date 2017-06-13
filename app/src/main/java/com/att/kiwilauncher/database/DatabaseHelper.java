package com.att.kiwilauncher.database;

/**
 * Created by mac on 5/5/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.model.TheLoai;
import com.att.kiwilauncher.model.ThoiTiet;
import com.att.kiwilauncher.model.UngDung;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 4/28/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "kiwistore.sqlite";
    public static final String DB_FOLDER_PATH = "/data/data/com.att.kiwilauncher/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;
    private Map<String, Integer> listIcon = new HashMap<>();
    private Map<String, Integer> list = new HashMap<>();

    public SQLiteDatabase getmDatabase() {
        return mDatabase;
    }

    public void setmDatabase(SQLiteDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        listIcon.put("ic_tatca", R.drawable.ic_tatca);
        listIcon.put("ic_giaitri", R.drawable.ic_giaitri);
        listIcon.put("ic_giaoduc", R.drawable.ic_giaoduc);
        listIcon.put("ic_trochoi", R.drawable.ic_trochoi);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DB_NAME).getPath();
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

    //kiểm tra database có tồn tại không
    public void checkDatabase(Context context) {
        File database = context.getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME);
        if (database.exists() == false) {
            this.getReadableDatabase();
            if (copyDatabase(context)) {
                //Toast.makeText(context.getApplicationContext(), "Tải thành công", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context.getApplicationContext(), "Tải thất bại", Toast.LENGTH_LONG).show();
            }
        }
    }

    //copy dữ liệu khi không có database
    public boolean copyDatabase(Context context) {
        try {
            InputStream inputStream = context.getAssets().open(DatabaseHelper.DB_NAME);
            String outputFilePath = DatabaseHelper.DB_FOLDER_PATH + DatabaseHelper.DB_NAME;
            OutputStream outputStream = new FileOutputStream(outputFilePath);
            byte[] buff = new byte[1024];
            int length = 0;
            length = inputStream.read(buff);
            while (length > 0) {
                outputStream.write(buff);
                length = inputStream.read(buff);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // lấy danh sách tất cả các thể loại cửa ứng dụng
    public List<TheLoai> getListTheLoai() {
        TheLoai theLoai;
        List<TheLoai> listTheLoai = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM theloai", null);
        if (cursor.moveToFirst()) {
            theLoai = new TheLoai();
            theLoai.setId(cursor.getString(0));
            theLoai.setTen(cursor.getString(1));
            theLoai.setSoLuong(cursor.getString(2));
            theLoai.setIcon(listIcon.get(cursor.getString(3)));
            theLoai.setChecked(true);
            listTheLoai.add(theLoai);
        }
        while (cursor.moveToNext()) {
            theLoai = new TheLoai();
            theLoai.setId(cursor.getString(0));
            theLoai.setTen(cursor.getString(1));
            theLoai.setSoLuong(cursor.getString(2));
            theLoai.setIcon(listIcon.get(cursor.getString(3)));
            theLoai.setChecked(false);
            listTheLoai.add(theLoai);
        }
        cursor.close();
        closeDatabase();
        return listTheLoai;
    }

    // lấy danh sách tất cả các ứng dụng của thẻ loại xyz
    public List<UngDung> getListUngDung(TheLoai theLoai) {
        List<UngDung> listUngDung = new ArrayList<>();
        String maTheLoai = theLoai.getId();
        List<String> listAnh;
        openDatabase();
        listAnh = new ArrayList<>();
        Cursor cursor;
        if (theLoai.getIcon() == R.drawable.ic_tatca) {
            cursor = mDatabase.rawQuery("SELECT ungdung.id,ungdung.ten,ungdung.installed,ungdung.icon,ungdung.luotcai" +
                    ",ungdung.version,ungdung.des,ungdung.linkcai,ungdung.rating,ungdung.version_code,ungdung.capnhat" +
                    " FROM ungdung", null);
        } else {
            cursor = mDatabase.rawQuery("SELECT ungdung.id,ungdung.ten,ungdung.installed,ungdung.icon,ungdung.luotcai," +
                    "ungdung.version,ungdung.des,ungdung.linkcai ,ungdung.rating,ungdung.version_code,ungdung.capnhat FROM ungdung JOIN theloai_ungdung ON ungdung.id=theloai_ungdung.ungdungid WHERE theloai_ungdung.theloaiid=" + maTheLoai, null);
        }
        if (cursor.moveToFirst()) {
            UngDung ungDung = new UngDung();
            ungDung.setId(cursor.getString(0));
            ungDung.setName(cursor.getString(1));
            if (cursor.getInt(2) == 0) {
                ungDung.setInstalled(false);
            } else {
                ungDung.setInstalled(true);
            }
            ungDung.setIcon(cursor.getString(3));
            ungDung.setLuotCai(cursor.getString(4));
            ungDung.setVersion(cursor.getString(5));
            ungDung.setDes(cursor.getString(6));
            ungDung.setLinkCai(cursor.getString(7));
            ungDung.setRating(cursor.getString(8));
            ungDung.setVersionCode(cursor.getString(9));
            ungDung.setUpdate(cursor.getString(10));
            ungDung.setAnh(listAnh);
            listUngDung.add(ungDung);
        }
        while (cursor.moveToNext()) {
            UngDung ungDung = new UngDung();
            ungDung.setId(cursor.getString(0));
            ungDung.setName(cursor.getString(1));
            if (cursor.getInt(2) == 0) {
                ungDung.setInstalled(false);
            } else {
                ungDung.setInstalled(true);
            }
            ungDung.setIcon(cursor.getString(3));
            ungDung.setLuotCai(cursor.getString(4));
            ungDung.setVersion(cursor.getString(5));
            ungDung.setDes(cursor.getString(6));
            ungDung.setLinkCai(cursor.getString(7));
            ungDung.setRating(cursor.getString(8));
            ungDung.setVersionCode(cursor.getString(9));
            ungDung.setUpdate(cursor.getString(10));
            ungDung.setAnh(listAnh);
            listUngDung.add(ungDung);
        }
        cursor.close();
        closeDatabase();

        return listUngDung;
    }

    //lấy ra thông tin về id địa chỉ thời tiết và tên của tỉnh
    public ThoiTiet getThongTinThoiTiet(String id) {
        ThoiTiet thoiTiet = new ThoiTiet();
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT * FROM thoitiet WHERE thoitiet.id = " + id, null);

        if (cursor.moveToFirst()) {
            thoiTiet.setTen(cursor.getString(1));
            thoiTiet.setMaThoiTiet(cursor.getString(2));
        }
        cursor.close();
        closeDatabase();

        return thoiTiet;
    }

    // lấy danh sách tất cả các điện thoại của thương hiệu xyz
    public List<String> getListAnhChiTietUngDung(UngDung ungDung) {
        List<String> listAnh = new ArrayList<>();
        String ungDungId = "";
        String maUngDung = ungDung.getId();
        openDatabase();
        Cursor cursor;
        // cursor = mDatabase.rawQuery("SELECT * FROM anhchitiet WHERE ungdungid = " + maUngDung, null);
        cursor = mDatabase.rawQuery("SELECT * FROM anhchitiet", null);
        if (cursor.moveToFirst()) {
            ungDungId = cursor.getString(1);
            if (ungDungId.equals(maUngDung)) {
                listAnh.add(cursor.getString(2));
            }
        }
        while (cursor.moveToNext()) {
            ungDungId = cursor.getString(1);
            if (ungDungId.equals(maUngDung)) {
                listAnh.add(cursor.getString(2));
            }
        }

        cursor.close();
        closeDatabase();

        return listAnh;
    }

    //lấy ra thông tin về id cập nhật
    public String getMaxCapNhatId() {
        String id = "0";
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT id FROM capnhat ORDER BY id DESC LIMIT 1", null);

        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
        }
        cursor.close();
        closeDatabase();
        return id;
    }

    public String getMaxQuangCaoId() {
        String id = "0";
        String nd = "";
        int count = 0;
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT id,noidung FROM quangcao WHERE loaiquangcao = 2  ORDER BY id DESC", null);
        count = cursor.getCount();
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
            nd = cursor.getString(1);
        }
        cursor.close();
        closeDatabase();
        return id + "," + nd + "," + count;
    }

    public String getLinkAnhQuangCao() {
        String nd = "";
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT noidung FROM quangcao WHERE loaiquangcao = 2 ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            nd = cursor.getString(0);
        }
        cursor.close();
        closeDatabase();
        return nd;
    }

    public void insertQuangCao(String id, String noiDung, String loaiQuangCaoId) {
        ContentValues values = new ContentValues();
        values.put("id", Integer.parseInt(id));
        values.put("noidung", noiDung);
        values.put("loaiquangcao", Integer.parseInt(loaiQuangCaoId));
        openDatabase();
        mDatabase.insert("quangcao", null, values);
        closeDatabase();
    }
    public String getLinkTextQuangCao() {
        String nd = "";
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT noidung FROM quangcao WHERE loaiquangcao = 3 ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            nd = cursor.getString(0);
        }
        cursor.close();
        closeDatabase();
        return nd;
    }
    public String getLinkVideoQuangCao() {
        String nd = "";
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT noidung FROM quangcao WHERE loaiquangcao = 1 ORDER BY id DESC", null);
        if (cursor.moveToFirst()) {
            nd = cursor.getString(0);
        }
        cursor.close();
        closeDatabase();
        return nd;
    }
    public void deleteQuangCao() {
        openDatabase();
        mDatabase.delete("quangcao", "1", null);
        closeDatabase();
    }

    public List<UngDung> getLissAppName() {
        List<UngDung> ungDungList = new ArrayList<>();
        String id = "0";
        String ten = "0";
        int count = 0;
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT id,ten FROM ungdung", null);
        count = cursor.getCount();
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
            ten = cursor.getString(1);
            UngDung ungDung = new UngDung();
            ungDung.setId(id);
            ungDung.setName(ten);
            ungDungList.add(ungDung);
        }
        while (cursor.moveToNext()){
            id = cursor.getString(0);
            ten = cursor.getString(1);
            UngDung ungDung = new UngDung();
            ungDung.setId(id);
            ungDung.setName(ten);
            ungDungList.add(ungDung);
        }
        cursor.close();
        closeDatabase();
        return ungDungList;
    }

    public void insertApp(String id, String ten, int installed, String icon, String luotcai, String versions, String des,
                          String linkcai,String rating,String versionCode,int update) {
        ContentValues values = new ContentValues();
        values.put("id", Integer.parseInt(id));
        values.put("ten", ten);
        values.put("installed", installed);
        values.put("icon", icon);
        values.put("luotcai", luotcai);
        values.put("version", versions);
        values.put("des", des);
        values.put("linkcai", linkcai);
        values.put("rating", rating);
        values.put("version_code", versionCode);
        values.put("capnhat", update);
        openDatabase();
        //mDatabase.delete("ungdung", "1", null);
        mDatabase.insert("ungdung", null, values);
        closeDatabase();
    }

    public void updateApp(int installed, String id) {
        ContentValues values = new ContentValues();
        values.put("installed", installed);
        openDatabase();
        mDatabase.update("ungdung", values, "id = " + id, null);
        closeDatabase();
    }

    public void deleteListApp() {
        openDatabase();
        mDatabase.delete("ungdung", "1", null);
        closeDatabase();
    }

    // UPDATE "main"."ungdung" SET "installed" = ?1 WHERE  "id" = 3
    public String testInsertApp() {
        String id = "0";
        String ten = "0";
        String in = "3";
        int count = 0;
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT id,ten,installed FROM ungdung ORDER BY id DESC ", null);
        count = cursor.getCount();
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
            ten = cursor.getString(1);
            in = cursor.getString(2);
        }
        cursor.close();
        closeDatabase();
        return id + "," + ten + in + "count" + count;
    }

    public void insertTheLoaiUngDung(String id, String theloaiid, String ungdungid) {
        ContentValues values = new ContentValues();
        values.put("id", Integer.parseInt(id));
        values.put("theloaiid", theloaiid);
        values.put("ungdungid", ungdungid);
        openDatabase();
        //mDatabase.delete("ungdung", "1", null);
        mDatabase.insert("theloai_ungdung", null, values);
        closeDatabase();
    }

    public void deleteTheLoaiUngDung() {
        openDatabase();
        mDatabase.delete("theloai_ungdung", "1", null);
        closeDatabase();
    }

    public String testTheLoaiUngDung() {
        String id = "0";
        int count = 0;
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT id FROM theloai_ungdung ORDER BY id DESC ", null);
        count = cursor.getCount();
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
        }
        cursor.close();
        closeDatabase();
        return id + "," + "count" + count;
    }

    public void insertTheLoai(String id, String ten, String soLuong, String icon) {
        ContentValues values = new ContentValues();
        values.put("id", Integer.parseInt(id));
        values.put("ten", ten);
        values.put("soLuong", Integer.parseInt(soLuong));
        values.put("icon", icon);
        openDatabase();
        //mDatabase.delete("ungdung", "1", null);
        mDatabase.insert("theloai", null, values);
        closeDatabase();
    }

    public void deleteTheLoai() {
        openDatabase();
        mDatabase.delete("theloai", "1", null);
        closeDatabase();
    }

    public String testTheLoai() {
        String id = "0";
        int count = 0;
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT id FROM theloai ORDER BY id DESC ", null);
        count = cursor.getCount();
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
        }
        cursor.close();
        closeDatabase();
        return id + "," + "count" + count;
    }

    public void insertAnhChiTiet(String id, String ungdungid, String ten) {
        ContentValues values = new ContentValues();
        values.put("id", Integer.parseInt(id));
        values.put("ungdungid", ungdungid);
        values.put("ten", ten);
        openDatabase();
        //mDatabase.delete("ungdung", "1", null);
        mDatabase.insert("anhchitiet", null, values);
        closeDatabase();
    }

    public void deleteAnhChiTiet() {
        openDatabase();
        mDatabase.delete("anhchitiet", "1", null);
        closeDatabase();
    }

    public String testAnhChiTiet() {
        String id = "0";
        String ten = "0";
        String ungdungid = "0";
        int count = 0;
        openDatabase();
        Cursor cursor;
        //  cursor = mDatabase.rawQuery("SELECT id,ten,ungdungid FROM anhchitiet WHERE ungdungid = 5", null);
        cursor = mDatabase.query("anhchitiet", new String[]{"id", "ten", "ungdungid"}, null, null, null, null, null);
        count = cursor.getCount();
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
            ten = cursor.getString(1);
            ungdungid = cursor.getString(2);
        }
        cursor.close();
        closeDatabase();
        return id + "," + ten + "," + ungdungid + "," + "count" + count;
    }

    public void insertCapNhat(String id, String value) {
        ContentValues values = new ContentValues();
        values.put("id", Integer.parseInt(id));
        values.put("value", value);
        openDatabase();
        mDatabase.insert("capnhat", null, values);
        closeDatabase();
    }

    public void deleteCapNhat() {
        openDatabase();
        mDatabase.delete("capnhat", "1", null);
        closeDatabase();
    }

    public String getIdCapNhat() {
        String value = "0";
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT value FROM capnhat ORDER BY id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            value = cursor.getString(0);
        }
        cursor.close();
        closeDatabase();
        return value;
    }

    public String testCapNhat() {
        String id = "0";
        String value = "0";
        int count = 0;
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT id,value FROM capnhat ORDER BY id DESC LIMIT 1", null);
        count = cursor.getCount();
        if (cursor.moveToFirst()) {
            id = cursor.getString(0);
            value = cursor.getString(1);
        }
        cursor.close();
        closeDatabase();
        return id + "," + value + "," + "count" + count;
    }
}