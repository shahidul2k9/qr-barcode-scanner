package com.shahidul.qr.barcode.scanner.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.security.PublicKey;

/**
 * @author Shahidul Islam
 * @since 2/6/2016.
 */
public class HistoryDatabaseHelper extends SQLiteOpenHelper {
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_BARCODE_FORMAT = "barcode_format";
    public static final String COLUMN_BARCODE_CONTENT = "barcode_content";
    public static final String COLUMN_DATE = "_date";
    public static final String COLUMN_RAW_IMAGE = "raw_image";
    public static final String DATABASE_NAME = "history.bd";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "history";
    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    COLUMN_BARCODE_FORMAT + TEXT_TYPE + COMMA_SEP +
                    COLUMN_BARCODE_CONTENT + TEXT_TYPE + COMMA_SEP +
                    COLUMN_DATE + " INTEGER NOT NULL" + COMMA_SEP +
                    COLUMN_RAW_IMAGE + TEXT_TYPE + " )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static HistoryDatabaseHelper INSTANCE;
    public HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static HistoryDatabaseHelper getInstance(Context context){
        if (INSTANCE == null){
            INSTANCE = new HistoryDatabaseHelper(context);
        }
        return INSTANCE;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public long insertToHistory(String format, String content, long date, String rawImage){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BARCODE_FORMAT,format);
        contentValues.put(COLUMN_BARCODE_CONTENT, content);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_RAW_IMAGE, rawImage);
        SQLiteDatabase db = getWritableDatabase();
        long id =  db.insert(TABLE_NAME,null,contentValues);
        db.close();
        return id;
    }
    public int deleteHistoryById(long id){
        return getWritableDatabase().delete(TABLE_NAME,COLUMN_ID + " = ?",new String[]{String.valueOf(id)});
    }
    public int deleteByFormatAndContent(String format, String content){
        return getWritableDatabase().delete(TABLE_NAME,COLUMN_BARCODE_FORMAT + " = ? AND " + COLUMN_BARCODE_CONTENT + " = ?", new String[]{format,content});
    }
    public Cursor getHistoryById(long id){
        return getReadableDatabase().query(TABLE_NAME, null, COLUMN_ID + " = ?",new String[]{String.valueOf(id)},null,null,null,String.valueOf(id));
    }
    public Cursor getAllHistory(){
        return getReadableDatabase().query(TABLE_NAME, null,null,null, null, null, COLUMN_DATE + " DESC", null);

    }
}
