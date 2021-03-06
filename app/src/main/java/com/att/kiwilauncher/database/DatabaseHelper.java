package com.att.kiwilauncher.database;

/**
 * Created by mac on 5/5/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.UngDung;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.model.QuangCao;
import com.att.kiwilauncher.model.TheLoai;
import com.att.kiwilauncher.model.TheLoaiUngDung;
import com.att.kiwilauncher.model.ThoiTiet;
import com.att.kiwilauncher.model.UngDungNew;
import com.att.kiwilauncher.xuly.DuLieu;

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

    public HashMap<String, List> getAllList() {
        HashMap<String, List> finalListMap = new HashMap<>();
        List<ChuDe> chuDeList = new ArrayList<>();
        List<UngDung> ungDungList = new ArrayList<>();
        List<QuangCao> quangCaoList = new ArrayList<>();
        List<TheLoaiUngDung> theLoaiUngDungList = new ArrayList<>();
        List<String> capNhatList = new ArrayList<>();
        openDatabase();
        //get List Category
        ChuDe chuDe;
        int loopTurn = 1;
        Cursor cursor = mDatabase.rawQuery("SELECT id,ten,soluong,icon FROM theloai", null);
        while (cursor.moveToNext()) {
            if (cursor.getString(3).equals("ic_tatca")) {
                if (cursor.moveToNext()) {
                    chuDe = new ChuDe();
                    if (loopTurn == 1) {
                        chuDe.setCheckedCate(true);
                    } else {
                        chuDe.setCheckedCate(false);
                    }
                    chuDe.setIndexCate(cursor.getInt(0));
                    chuDe.setDrawCate(listIcon.get(cursor.getString(3)));
                    chuDe.setNameCate(cursor.getString(1));
                    chuDeList.add(chuDe);
                }
            } else {
                chuDe = new ChuDe();
                if (loopTurn == 1) {
                    chuDe.setCheckedCate(true);
                } else {
                    chuDe.setCheckedCate(false);
                }
                chuDe.setIndexCate(cursor.getInt(0));
                chuDe.setDrawCate(listIcon.get(cursor.getString(3)));
                chuDe.setNameCate(cursor.getString(1));
                chuDeList.add(chuDe);
            }
            loopTurn++;
        }
        // get quang cao
        cursor = mDatabase.rawQuery("SELECT noidung,loaiquangcao FROM quangcao", null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals("1")) {
                QuangCao quangCao = new QuangCao();
                quangCao.setNoiDung(cursor.getString(0));
                quangCao.setLoaiQuangCao(cursor.getString(1));
                quangCao.setLinkVideo(quangCao.getNoiDung().split(";")[0]);
                quangCao.setLinkWeb(quangCao.getNoiDung().split(";")[1]);
                quangCaoList.add(quangCao);
            } else if (cursor.getString(1).equals("3")) {
                QuangCao quangCao = new QuangCao();
                quangCao.setNoiDung(cursor.getString(0));
                quangCao.setLoaiQuangCao(cursor.getString(1));
                quangCao.setText(cursor.getString(0));
                quangCaoList.add(quangCao);
            } else if (cursor.getString(1).equals("4")) {
                QuangCao quangCao = new QuangCao();
                quangCao.setNoiDung(cursor.getString(0));
                quangCao.setLoaiQuangCao(cursor.getString(1));
                quangCao.setLinkImage(quangCao.getNoiDung().split(";")[0]);
                quangCao.setLinkWeb(quangCao.getNoiDung().split(";")[1]);
                quangCao.setTime(quangCao.getNoiDung().split(";")[2]);
                quangCaoList.add(quangCao);
            }
        }
//get app list
        cursor = mDatabase.rawQuery("SELECT ungdung.id,ungdung.ten,ungdung.icon FROM ungdung", null);
        while (cursor.moveToNext()) {
            UngDung ungDung = new UngDung();
            ungDung.setId(cursor.getString(0));
            ungDung.setNameApp(cursor.getString(1));
            ungDung.setIcon(cursor.getString(2));
            ungDungList.add(ungDung);
        }
// get table theloai_ungdung list
        cursor = mDatabase.rawQuery("SELECT theloaiid,ungdungid FROM theloai_ungdung", null);
        while (cursor.moveToNext()) {
            TheLoaiUngDung theLoaiUngDung = new TheLoaiUngDung();
            theLoaiUngDung.setIdTheLoai(cursor.getString(0));
            theLoaiUngDung.setIdUngDung(cursor.getString(1));
            theLoaiUngDungList.add(theLoaiUngDung);
        }
// get id cap nhat
        cursor = mDatabase.rawQuery("SELECT value FROM capnhat ORDER BY id DESC LIMIT 1", null);
        if (cursor.moveToFirst()) {
            String value = cursor.getString(0);
            capNhatList.add(value);
        }
        cursor.close();
        closeDatabase();
        finalListMap.put("theloai", chuDeList);
        finalListMap.put("ungdung", ungDungList);
        finalListMap.put("quangcao", quangCaoList);
        finalListMap.put("theloaiungdung", theLoaiUngDungList);
        finalListMap.put("capnhat", capNhatList);

        return finalListMap;
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

    // lấy danh sách tất cả các thể loại cửa ứng dụng
    public List<ChuDe> getListChuDe() {
        ChuDe chuDe;
        List<ChuDe> chuDeList = new ArrayList<>();
        int id = 0;
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT id,ten,soluong,icon FROM theloai", null);
        if (cursor.moveToFirst()) {
            if (cursor.getString(0).equals("1")) {
                if (cursor.moveToNext()) {
                    chuDe = new ChuDe();
                    chuDe.setCheckedCate(true);
                    chuDe.setIndexCate(cursor.getInt(0));
                    //chuDe.setDrawCate(listIcon.get(cursor.getString(3)));
                    chuDe.setIconLink(cursor.getString(3));
                    chuDe.setNameCate(cursor.getString(1));
                    chuDeList.add(chuDe);
                }
            } else {
                chuDe = new ChuDe();
                chuDe.setCheckedCate(true);
                chuDe.setIndexCate(cursor.getInt(0));
                //chuDe.setDrawCate(listIcon.get(cursor.getString(3)));
                chuDe.setIconLink(cursor.getString(3));
                chuDe.setNameCate(cursor.getString(1));
                chuDeList.add(chuDe);
            }
        }
        while (cursor.moveToNext()) {
            chuDe = new ChuDe();
            chuDe.setCheckedCate(false);
            id = cursor.getInt(0);
            chuDe.setIndexCate(id);
            //chuDe.setDrawCate(listIcon.get(cursor.getString(3)));
            chuDe.setIconLink(cursor.getString(3));
            chuDe.setNameCate(cursor.getString(1));
            chuDeList.add(chuDe);
            // không lấy thể loại truyền hình, phim ,etc ...
            if (id >= 6) {
                cursor.moveToLast();
            }
        }
        cursor.close();
        closeDatabase();
        return chuDeList;
    }

    // lấy danh sách tất cả các ứng dụng
    public List<UngDung> getListUngDungV2() {
        List<UngDung> listUngDung = new ArrayList<>();
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT ungdung.id,ungdung.ten,ungdung.icon,ungdung.packageName FROM ungdung", null);

        if (cursor.moveToFirst()) {
            UngDung ungDungNew = new UngDung();
            ungDungNew.setId(cursor.getString(0));
            ungDungNew.setNameApp(cursor.getString(1));
            ungDungNew.setIcon(cursor.getString(2));
            ungDungNew.setPackageName(cursor.getString(3));
            Log.e("db",cursor.getString(0)+"="+cursor.getString(1)+"="+cursor.getString(2)+"="+cursor.getString(3));
            listUngDung.add(ungDungNew);
        }
        while (cursor.moveToNext()) {
            UngDung ungDungNew = new UngDung();
            ungDungNew.setId(cursor.getString(0));
            ungDungNew.setNameApp(cursor.getString(1));
            ungDungNew.setIcon(cursor.getString(2));
            ungDungNew.setPackageName(cursor.getString(3));
 //           Log.e("db",cursor.getString(0)+"="+cursor.getString(1)+"="+cursor.getString(2)+"="+cursor.getString(3));
            listUngDung.add(ungDungNew);
        }
        cursor.close();
        closeDatabase();
        return listUngDung;
    }

    // lấy danh sách bảng thể loại ứng dụng
    public List<TheLoaiUngDung> getListTheLoaiUngDung() {
        List<TheLoaiUngDung> theLoaiUngDungList = new ArrayList<>();
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT theloaiid,ungdungid FROM theloai_ungdung", null);
        while (cursor.moveToNext()) {
            TheLoaiUngDung theLoaiUngDung = new TheLoaiUngDung();
            theLoaiUngDung.setIdTheLoai(cursor.getString(0));
            theLoaiUngDung.setIdUngDung(cursor.getString(1));
            theLoaiUngDungList.add(theLoaiUngDung);
        }
        cursor.close();
        closeDatabase();
        return theLoaiUngDungList;
    }

    // lấy danh sách bảng thể loại ứng dụng
    public List<QuangCao> getListQuangCao() {
        List<QuangCao> quangCaoList = new ArrayList<>();
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT noidung,loaiquangcao FROM quangcao", null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals("1")) {
                QuangCao quangCao = new QuangCao();
                quangCao.setNoiDung(cursor.getString(0));
                quangCao.setLoaiQuangCao(cursor.getString(1));
                quangCao.setLinkVideo(quangCao.getNoiDung().split(";")[0]);
                quangCao.setLinkWeb(quangCao.getNoiDung().split(";")[1]);
                quangCaoList.add(quangCao);
            } else if (cursor.getString(1).equals("3")) {
                QuangCao quangCao = new QuangCao();
                quangCao.setNoiDung(cursor.getString(0));
                quangCao.setLoaiQuangCao(cursor.getString(1));
                quangCao.setText(cursor.getString(0));
                quangCaoList.add(quangCao);
            } else if (cursor.getString(1).equals("4")) {
                QuangCao quangCao = new QuangCao();
                quangCao.setNoiDung(cursor.getString(0));
                quangCao.setLoaiQuangCao(cursor.getString(1));
                quangCao.setLinkImage(DuLieu.URL_IMAGE+"/"+quangCao.getNoiDung().split(";")[0]);
                quangCao.setLinkWeb(quangCao.getNoiDung().split(";")[1]);
                quangCao.setTime(quangCao.getNoiDung().split(";")[2]);
                quangCaoList.add(quangCao);
            }
        }
        cursor.close();
        closeDatabase();
        return quangCaoList;
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

    //get Id CapNhat
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
    // lấy danh sách tất cả các ứng dụng của thẻ loại xyz
    public List<UngDung> getListUngDung(ChuDe chuDe) {
        List<UngDung> listUngDung = new ArrayList<>();
        String maTheLoai = chuDe.getIndexCate() + "";
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT ungdung.id,ungdung.ten,ungdung.icon,ungdung.packageName FROM ungdung JOIN theloai_ungdung ON ungdung.id=theloai_ungdung.ungdungid WHERE theloai_ungdung.theloaiid=" + maTheLoai, null);

        if (cursor.moveToFirst()) {
            UngDung ungDungNew = new UngDung();
            ungDungNew.setId(cursor.getString(0));
            ungDungNew.setNameApp(cursor.getString(1));
            ungDungNew.setIcon(cursor.getString(2));
            ungDungNew.setPackageName(cursor.getString(3));
            listUngDung.add(ungDungNew);
        }
        while (cursor.moveToNext()) {
            UngDung ungDungNew = new UngDung();
            ungDungNew.setId(cursor.getString(0));
            ungDungNew.setNameApp(cursor.getString(1));
            ungDungNew.setIcon(cursor.getString(2));
            ungDungNew.setPackageName(cursor.getString(3));
            listUngDung.add(ungDungNew);
        }
        cursor.close();
        closeDatabase();

        return listUngDung;
    }

    // lấy danh sách tất cả các ứng dụng của thẻ loại xyz
    public List<UngDung> getListUngDungById(int idChuDe) {
        List<UngDung> listUngDung = new ArrayList<>();
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT ungdung.id,ungdung.ten,ungdung.icon,ungdung.packageName FROM ungdung JOIN theloai_ungdung ON ungdung.id=theloai_ungdung.ungdungid WHERE theloai_ungdung.theloaiid=" + idChuDe, null);

        if (cursor.moveToFirst()) {
            UngDung ungDungNew = new UngDung();
            ungDungNew.setId(cursor.getString(0));
            ungDungNew.setNameApp(cursor.getString(1));
            ungDungNew.setIcon(cursor.getString(2));
            ungDungNew.setPackageName(cursor.getString(3));
            listUngDung.add(ungDungNew);
        }
        while (cursor.moveToNext()) {
            UngDung ungDungNew = new UngDung();
            ungDungNew.setId(cursor.getString(0));
            ungDungNew.setNameApp(cursor.getString(1));
            ungDungNew.setIcon(cursor.getString(2));
            ungDungNew.setPackageName(cursor.getString(3));
            listUngDung.add(ungDungNew);
        }
        cursor.close();
        closeDatabase();

        return listUngDung;
    }
    public void deleteQuangCao() {
        openDatabase();
        mDatabase.delete("quangcao", "1", null);
        closeDatabase();
    }

    public void deleteListApp() {
        openDatabase();
        mDatabase.delete("ungdung", "1", null);
        closeDatabase();
    }
    public void insertApp(String id, String ten, int installed, String icon, String luotcai, String versions, String des,
                          String linkcai, String rating, String versionCode, int update,String packageName) {
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
        values.put("packageName", packageName);
        openDatabase();
        //mDatabase.delete("ungdung", "1", null);
        mDatabase.insert("ungdung", null, values);
        closeDatabase();
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

    // lấy danh sách tất cả các ứng dụng của thẻ loại xyz
    public List<UngDungNew> getListUngDungNew(TheLoai theLoai) {
        List<UngDungNew> listUngDungNew = new ArrayList<>();
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
            UngDungNew ungDungNew = new UngDungNew();
            ungDungNew.setId(cursor.getString(0));
            ungDungNew.setName(cursor.getString(1));
            if (cursor.getInt(2) == 0) {
                ungDungNew.setInstalled(false);
            } else {
                ungDungNew.setInstalled(true);
            }
            ungDungNew.setIcon(cursor.getString(3));
            ungDungNew.setLuotCai(cursor.getString(4));
            ungDungNew.setVersion(cursor.getString(5));
            ungDungNew.setDes(cursor.getString(6));
            ungDungNew.setLinkCai(cursor.getString(7));
            ungDungNew.setRating(cursor.getString(8));
            ungDungNew.setVersionCode(cursor.getString(9));
            ungDungNew.setUpdate(cursor.getString(10));
            ungDungNew.setAnh(listAnh);
            listUngDungNew.add(ungDungNew);
        }
        while (cursor.moveToNext()) {
            UngDungNew ungDungNew = new UngDungNew();
            ungDungNew.setId(cursor.getString(0));
            ungDungNew.setName(cursor.getString(1));
            if (cursor.getInt(2) == 0) {
                ungDungNew.setInstalled(false);
            } else {
                ungDungNew.setInstalled(true);
            }
            ungDungNew.setIcon(cursor.getString(3));
            ungDungNew.setLuotCai(cursor.getString(4));
            ungDungNew.setVersion(cursor.getString(5));
            ungDungNew.setDes(cursor.getString(6));
            ungDungNew.setLinkCai(cursor.getString(7));
            ungDungNew.setRating(cursor.getString(8));
            ungDungNew.setVersionCode(cursor.getString(9));
            ungDungNew.setUpdate(cursor.getString(10));
            ungDungNew.setAnh(listAnh);
            listUngDungNew.add(ungDungNew);
        }
        cursor.close();
        closeDatabase();

        return listUngDungNew;
    }

    // lấy danh sách tất cả các điện thoại của thương hiệu xyz
    public List<String> getListAnhChiTietUngDung(UngDungNew ungDungNew) {
        List<String> listAnh = new ArrayList<>();
        String ungDungId = "";
        String maUngDung = ungDungNew.getId();
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

    public ArrayList<String> getListVideoQuangCao() {
        ArrayList<String> listVideo = new ArrayList<>();
        String nd = "";
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT noidung FROM quangcao WHERE loaiquangcao = 1 ORDER BY id DESC LIMIT 3", null);

        if (cursor.moveToFirst()) {
            nd = DuLieu.splitLinkVideoWeb(cursor.getString(0))[0];
            listVideo.add(nd);
        }

        while (cursor.moveToNext()) {
            nd = DuLieu.splitLinkVideoWeb(cursor.getString(0))[0];
            listVideo.add(nd);
        }

        cursor.close();
        closeDatabase();
        return listVideo;
    }

    public ArrayList<String> getListLinkWebQuangCao() {
        ArrayList<String> listLinkWeb = new ArrayList<>();
        String nd = "";
        openDatabase();
        Cursor cursor;
        cursor = mDatabase.rawQuery("SELECT noidung FROM quangcao WHERE loaiquangcao = 1 ORDER BY id DESC LIMIT 3", null);

        if (cursor.moveToFirst()) {
            nd = DuLieu.splitLinkVideoWeb(cursor.getString(0))[1];
            listLinkWeb.add(nd);
        }

        while (cursor.moveToNext()) {
            nd = DuLieu.splitLinkVideoWeb(cursor.getString(0))[1];
            listLinkWeb.add(nd);
        }

        cursor.close();
        closeDatabase();
        return listLinkWeb;
    }



    public List<UngDungNew> getLissAppName() {
        List<UngDungNew> ungDungNewList = new ArrayList<>();
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
            UngDungNew ungDungNew = new UngDungNew();
            ungDungNew.setId(id);
            ungDungNew.setName(ten);
            ungDungNewList.add(ungDungNew);
        }
        while (cursor.moveToNext()) {
            id = cursor.getString(0);
            ten = cursor.getString(1);
            UngDungNew ungDungNew = new UngDungNew();
            ungDungNew.setId(id);
            ungDungNew.setName(ten);
            ungDungNewList.add(ungDungNew);
        }
        cursor.close();
        closeDatabase();
        return ungDungNewList;
    }


    public void updateApp(int installed, String id) {
        ContentValues values = new ContentValues();
        values.put("installed", installed);
        openDatabase();
        mDatabase.update("ungdung", values, "id = " + id, null);
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