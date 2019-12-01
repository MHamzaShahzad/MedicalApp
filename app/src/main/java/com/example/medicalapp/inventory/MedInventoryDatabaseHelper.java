package com.example.medicalapp.inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MedInventoryDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Medicine_Inventory";
    public static final int DATABASE_VERSION = 2;

    public static final String TABLE_MEDICINE_DETAILS = "table_medicine_details";

    public static final String MEDICINE_ID = "medicine_id";
    public static final String MEDICINE_NAME = "medicine_name";
    public static final String MEDICINE_DETAIL = "medicine_detail";
    public static final String MEDICINE_TYPE = "medicine_type";
    public static final String MEDICINE_ADDED_AT = "medicine_added_at";
    public static final String MEDICINE_EXPIRY_DATE = "medicine_expire_at";
    public static final String MEDICINE_PER_DAY = "medicine_per_day";
    public static final String MEDICINE_STOCK = "medicine_stock";

    public static final String CREATE_TABLE_MEDICINE_DETAILS_QUERY = "create table if not exists " + TABLE_MEDICINE_DETAILS
            + "(" + MEDICINE_ID + " integer primary key autoincrement, " + MEDICINE_NAME + " text, " + MEDICINE_DETAIL + " text, " + MEDICINE_ADDED_AT + " text," + MEDICINE_EXPIRY_DATE + " text," + MEDICINE_STOCK + " integer, " + MEDICINE_TYPE + " text, " + MEDICINE_PER_DAY + " integer);";

    public MedInventoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MEDICINE_DETAILS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if EXISTS " + TABLE_MEDICINE_DETAILS);
        onCreate(db);
    }
}
