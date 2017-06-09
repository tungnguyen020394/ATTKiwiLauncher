package com.att.kiwilauncher.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by mac on 6/2/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper{
    public static final String DB_NAME = "KiwiLauncherData.sqlite";
    public static final String DB_FOLDER_PATH = "data/data/com.att.attkiwilauncher/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public SQLiteDatabase getmDatabase() {
        return mDatabase;
    }

    public DatabaseHelper(Context context) {
        super(context,DB_NAME,null,1);
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
        mDatabase = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    // check database exist
    public void checkDatabase(Context context) {
        File database = context.getApplicationContext().getDatabasePath(DatabaseHelper.DB_NAME);
        if (database.exists() == false) {
            this.getReadableDatabase();
        }
    }
}
