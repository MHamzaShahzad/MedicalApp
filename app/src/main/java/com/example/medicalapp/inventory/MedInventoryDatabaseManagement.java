package com.example.medicalapp.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.medicalapp.reminder.Class.Medicine;
import com.example.medicalapp.reminder.Class.MedicinePerRow;
import com.example.medicalapp.reminder.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedInventoryDatabaseManagement {

    private static final String TAG = MedInventoryDatabaseManagement.class.getName();
    private MedInventoryDatabaseHelper medInventoryDatabaseHelper;

    public MedInventoryDatabaseManagement(Context context) {
        medInventoryDatabaseHelper = new MedInventoryDatabaseHelper(context);
    }

    public boolean addMedicineDetails(String TABLE_NAME, MedInventoryModel medicine) {

        SQLiteDatabase sqLiteDatabase = medInventoryDatabaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MedInventoryDatabaseHelper.MEDICINE_NAME, medicine.getMedName());
        contentValues.put(MedInventoryDatabaseHelper.MEDICINE_DETAIL, medicine.getMedDetail());
        contentValues.put(MedInventoryDatabaseHelper.MEDICINE_STOCK, medicine.getMedStock());
        contentValues.put(MedInventoryDatabaseHelper.MEDICINE_TYPE, medicine.getMedType());
        contentValues.put(MedInventoryDatabaseHelper.MEDICINE_PER_DAY, medicine.getMedPerDay());
        contentValues.put(MedInventoryDatabaseHelper.MEDICINE_ADDED_AT, medicine.getMedAddedAt());
        contentValues.put(MedInventoryDatabaseHelper.MEDICINE_EXPIRY_DATE, medicine.getMedExpiryDate());

        long row = -1;
        HashMap<String, Object> map = new HashMap<>();
        map.put(MedInventoryDatabaseHelper.MEDICINE_NAME, medicine.getMedName());
        if (!searchMedicine(TABLE_NAME, map))
            row = sqLiteDatabase.insert(MedInventoryDatabaseHelper.TABLE_MEDICINE_DETAILS, null, contentValues);
        else
            row = sqLiteDatabase.update(TABLE_NAME, contentValues, MedInventoryDatabaseHelper.MEDICINE_NAME + " LIKE '%" + medicine.getMedName() + "%'", null);
        return row != -1;
    }

    public ArrayList<MedInventoryModel> retrieveMedicineFromInventory() {



        ArrayList<MedInventoryModel> list = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = medInventoryDatabaseHelper.getReadableDatabase();
        String selectQuery = "Select * from " + MedInventoryDatabaseHelper.TABLE_MEDICINE_DETAILS;

        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
        Log.e(TAG, "retrieveMedicineFromInventory: " + cursor.moveToFirst());


        if (cursor.moveToFirst()) {

            do {

                int mId = cursor.getInt(cursor.getColumnIndex(MedInventoryDatabaseHelper.MEDICINE_ID));
                String mName = cursor.getString(cursor.getColumnIndex(MedInventoryDatabaseHelper.MEDICINE_NAME));
                String mType = cursor.getString(cursor.getColumnIndex(MedInventoryDatabaseHelper.MEDICINE_TYPE));
                String mDetail = cursor.getString(cursor.getColumnIndex(MedInventoryDatabaseHelper.MEDICINE_DETAIL));
                String mTimeAdded = cursor.getString(cursor.getColumnIndex(MedInventoryDatabaseHelper.MEDICINE_ADDED_AT));
                String mTimeExpire = cursor.getString(cursor.getColumnIndex(MedInventoryDatabaseHelper.MEDICINE_EXPIRY_DATE));
                int mPerDay = cursor.getInt(cursor.getColumnIndex(MedInventoryDatabaseHelper.MEDICINE_PER_DAY));
                int mStock = cursor.getInt(cursor.getColumnIndex(MedInventoryDatabaseHelper.MEDICINE_STOCK));


                list.add(
                        new MedInventoryModel(
                                mId,
                                mName,
                                mDetail,
                                mTimeAdded,
                                mTimeExpire,
                                mType,
                                mStock,
                                mPerDay
                        )
                );

            } while (cursor.moveToNext());

        }

        cursor.close();
        return list;

    }

    public boolean searchMedicine(String TABLE_NAME, HashMap<String, Object> listParams) {

        SQLiteDatabase sqLiteDatabase = medInventoryDatabaseHelper.getReadableDatabase();
        StringBuilder where_arguments = new StringBuilder();
        if (listParams != null && listParams.size() > 0) {
            int counter = 0;
            for (Map.Entry<String, Object> entry : listParams.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (counter == listParams.size() - 1)
                    where_arguments.append(key).append(" LIKE '%").append(value).append("%' ");
                else
                    where_arguments.append(key).append(" LIKE '%").append(value).append("%' AND ");
                counter++;
            }
        }
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + where_arguments.toString(), null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        Log.e(TAG, "searchMedicine: " + result );
        return result;

    }

    public boolean deleteData(String TABLE_NAME, String key, Object value) {
        SQLiteDatabase db = medInventoryDatabaseHelper.getWritableDatabase();
        int count = db.delete(TABLE_NAME, key + "=" + value, null);
        return count > 0;
    }

}
