package com.ndu.sanghiang.kners.smartqap.inline.sqlite.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_BARCODE;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_CAP_BOX;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_CAP_RASA;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_GRAMASI_PERPCS;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_IS_KWG;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_ITEM_CODE;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_ITEM_DESC;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_JUMLAH_DOOS_PERBOX;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_JUMLAH_PCS_PERBOX;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_KET;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_KODE_PABRIK;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_KODE_SACHET;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_LOKAL_EXPORT;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_SINGKATAN;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_TANGGAL_PIC_PENGUPDATE;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_TIMESTAMP;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.COLUMN_UMUR_PRODUK;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.CREATE_TABLE;
import static com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model.ItemCode.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "itemCode_db";
    private String LOG = "database";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // create assets table
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    // Upgrading db
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public long createItemCode(String itemCode) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_ITEM_CODE, itemCode);

        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public void dropTable() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ItemCode getItemCode(long code) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{
                        COLUMN_ITEM_DESC,
                        COLUMN_SINGKATAN,
                        COLUMN_KODE_SACHET,
                        COLUMN_CAP_RASA,
                        COLUMN_CAP_BOX,
                        COLUMN_IS_KWG,
                        COLUMN_UMUR_PRODUK,
                        COLUMN_KODE_PABRIK,
                        COLUMN_KET,
                        COLUMN_LOKAL_EXPORT,
                        COLUMN_GRAMASI_PERPCS,
                        COLUMN_JUMLAH_DOOS_PERBOX,
                        COLUMN_JUMLAH_PCS_PERBOX,
                        COLUMN_TANGGAL_PIC_PENGUPDATE,
                        COLUMN_BARCODE,
                        COLUMN_TIMESTAMP,
                },
                /*COLUMN_ID + "=?",*/
                COLUMN_ITEM_CODE,
                new String[]{String.valueOf(code)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare asset object
        ItemCode itemCode = new ItemCode(
                Objects.requireNonNull(cursor).getString(cursor.getColumnIndex(COLUMN_ITEM_CODE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESC)),
                cursor.getString(cursor.getColumnIndex(COLUMN_SINGKATAN)),
                cursor.getString(cursor.getColumnIndex(COLUMN_KODE_SACHET)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CAP_RASA)),
                cursor.getString(cursor.getColumnIndex(COLUMN_CAP_BOX)),
                cursor.getString(cursor.getColumnIndex(COLUMN_IS_KWG)),
                cursor.getString(cursor.getColumnIndex(COLUMN_UMUR_PRODUK)),
                cursor.getString(cursor.getColumnIndex(COLUMN_KODE_PABRIK)),
                cursor.getString(cursor.getColumnIndex(COLUMN_KET)),
                cursor.getString(cursor.getColumnIndex(COLUMN_LOKAL_EXPORT)),
                cursor.getString(cursor.getColumnIndex(COLUMN_GRAMASI_PERPCS)),
                cursor.getString(cursor.getColumnIndex(COLUMN_JUMLAH_DOOS_PERBOX)),
                cursor.getString(cursor.getColumnIndex(COLUMN_JUMLAH_PCS_PERBOX)),
                cursor.getString(cursor.getColumnIndex(COLUMN_TANGGAL_PIC_PENGUPDATE)),
                cursor.getString(cursor.getColumnIndex(COLUMN_BARCODE)));

        // close the db connection
        cursor.close();

        return itemCode;
    }

    public List<ItemCode> getAllItemCode() {
        List<ItemCode> itemCodes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
//                ItemCode.COLUMN_TIMESTAMP + " DESC";
                /*COLUMN_ID*/COLUMN_ITEM_CODE + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemCode itemCode = new ItemCode();
                itemCode.setItem_code(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CODE)));
                itemCode.setItem_desc(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESC)));
                itemCode.setSingkatan(cursor.getString(cursor.getColumnIndex(COLUMN_SINGKATAN)));
                itemCode.setKode_sachet(cursor.getString(cursor.getColumnIndex(COLUMN_KODE_SACHET)));
                itemCode.setCap_rasa(cursor.getString(cursor.getColumnIndex(COLUMN_CAP_RASA)));
                itemCode.setCap_box(cursor.getString(cursor.getColumnIndex(COLUMN_CAP_BOX)));
                itemCode.setIs_kwg(cursor.getString(cursor.getColumnIndex(COLUMN_IS_KWG)));
                itemCode.setUmur_produk(cursor.getString(cursor.getColumnIndex(COLUMN_UMUR_PRODUK)));
                itemCode.setKode_pabrik(cursor.getString(cursor.getColumnIndex(COLUMN_KODE_PABRIK)));
                itemCode.setKet(cursor.getString(cursor.getColumnIndex(COLUMN_KET)));
                itemCode.setLokal_export(cursor.getString(cursor.getColumnIndex(COLUMN_LOKAL_EXPORT)));
                itemCode.setGramasi_perpcs(cursor.getString(cursor.getColumnIndex(COLUMN_GRAMASI_PERPCS)));
                itemCode.setJumlah_doos_perbox(cursor.getString(cursor.getColumnIndex(COLUMN_JUMLAH_DOOS_PERBOX)));
                itemCode.setJumlah_pcs_perbox(cursor.getString(cursor.getColumnIndex(COLUMN_JUMLAH_PCS_PERBOX)));
                itemCode.setTanggal_pic_pengupdate(cursor.getString(cursor.getColumnIndex(COLUMN_TANGGAL_PIC_PENGUPDATE)));
                itemCode.setBarcode(cursor.getString(cursor.getColumnIndex(COLUMN_BARCODE)));

                itemCodes.add(itemCode);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return assets list
        return itemCodes;
    }


    public int getItemCodeCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

//    SELECT COUNT(*) FROM ItemCode WHERE asset_rfid IS NOT NULL and asset_status = 'ItemCode Ada';

    public int updateItemCode(ItemCode itemCode) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_CODE, itemCode.getItem_code());

        // updating row
        return db.update(TABLE_NAME, values, /*COLUMN_ID*/COLUMN_ITEM_CODE + " = ?",
                /*new String[]{String.valueOf(asset.getId())});*/
                new String[]{itemCode.getItem_code()});
    }

    public void deleteItemCode(ItemCode itemCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, /*COLUMN_ID*/COLUMN_ITEM_CODE + " = ?",
//                new String[]{String.valueOf(asset.getId())});
                new String[]{itemCode.getItem_code()});
        db.close();
    }

    public void getItemDescByItemCode(String itemCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM_CODE + " = '" + itemCode + "' ";

        Log.d(LOG, selectQuery);

        @SuppressLint("Recycle") Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        ItemCode itemCodeUnit = new ItemCode();
        itemCodeUnit.setItem_desc(c.getString(c.getColumnIndex(COLUMN_ITEM_DESC)));//KEY_ID key for fetching id
        //itemCodeUnit.setBreakfast((c.getInt(c.getColumnIndex(KEY_BREAKFAST))));//KEY_BREAKFAST key for fetching isBreakfast
        //itemCodeUnit.setLunch((c.getInt(c.getColumnIndex(KEY_LUNCH))));//KEY_LUNCH key for fetching isLunch
        //itemCodeUnit.setVegetables((c.getFloat(c.getColumnIndex(KEY_VEGETABLE))));//KEY_VEGETABLE key for fetching vegetables

    }

    public Cursor getData(String itemCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM_CODE + " = '" + itemCode + "' ", null );
        return res;
    }

    public long insertItemCode(String itemCode) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(COLUMN_ITEM_CODE, itemCode);

        // insert row
        long id = db.insert(TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    /*https://stackoverflow.com/questions/20415309/android-sqlite-how-to-check-if-a-record-exists*/
    /*https://stackoverflow.com/questions/20838233/sqliteexception-unrecognized-token-when-reading-from-database*/
    public boolean checkIsItemCodeInDB(String itemCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COLUMN_ITEM_CODE + " = " + "'" + itemCode + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void inputDataFromDom(HashMap<String, String> Vi) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ITEM_CODE, Vi.get(COLUMN_ITEM_CODE));
        values.put(COLUMN_ITEM_DESC, Vi.get(COLUMN_ITEM_DESC));
        values.put(COLUMN_SINGKATAN, Vi.get(COLUMN_SINGKATAN));
        values.put(COLUMN_KODE_SACHET, Vi.get(COLUMN_KODE_SACHET));
        values.put(COLUMN_CAP_RASA, Vi.get(COLUMN_CAP_RASA));
        values.put(COLUMN_CAP_BOX, Vi.get(COLUMN_CAP_BOX));
        values.put(COLUMN_IS_KWG, Vi.get(COLUMN_IS_KWG));
        values.put(COLUMN_UMUR_PRODUK, Vi.get(COLUMN_UMUR_PRODUK));
        values.put(COLUMN_KODE_PABRIK, Vi.get(COLUMN_KODE_PABRIK));
        values.put(COLUMN_KET, Vi.get(COLUMN_KET));
        values.put(COLUMN_LOKAL_EXPORT, Vi.get(COLUMN_LOKAL_EXPORT));
        values.put(COLUMN_GRAMASI_PERPCS, Vi.get(COLUMN_GRAMASI_PERPCS));
        values.put(COLUMN_JUMLAH_DOOS_PERBOX, Vi.get(COLUMN_JUMLAH_DOOS_PERBOX));
        values.put(COLUMN_JUMLAH_PCS_PERBOX, Vi.get(COLUMN_JUMLAH_PCS_PERBOX));
        values.put(COLUMN_TANGGAL_PIC_PENGUPDATE, Vi.get(COLUMN_TANGGAL_PIC_PENGUPDATE));
        values.put(COLUMN_BARCODE, Vi.get(COLUMN_BARCODE));
        database.insert(TABLE_NAME, null, values);
        database.close();
    }

    public void createTable() {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        onCreate(db);
    }
}
