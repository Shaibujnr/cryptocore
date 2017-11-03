package com.devlab.cryptocore.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;

import static com.devlab.cryptocore.db.ItemContract.SQL_CREATE_ENTRIES;
import static com.devlab.cryptocore.db.ItemContract.SQL_DELETE_ENTRIES;

/**
 * Created by shaibu on 11/2/17.
 */

public class ItemDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ItemStore.db";
    private SQLiteDatabase db;

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insertItem(Item item){
        db = getWritableDatabase();
        long created_updated = new Date().getTime();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemContract.ItemEntry.COLUMN_NAME_CRYPTO,item.getCrypto_type().toString());
        contentValues.put(ItemContract.ItemEntry.COLUMN_NAME_PRICE,String.valueOf(item.getPrice()));
        contentValues.put(ItemContract.ItemEntry.COLUMN_NAME_CURRENCY,item.getCurrency().getCurrencyCode());
        contentValues.put(ItemContract.ItemEntry.COLUMN_NAME_CREATED,String.valueOf(created_updated));
        contentValues.put(ItemContract.ItemEntry.COLUMN_NAME_UPDATED,String.valueOf(created_updated));
        return db.insert(ItemContract.ItemEntry.TABLE_NAME,null,contentValues);
    }

    public ArrayList<Item> fetchItems(){
        ArrayList<Item> items = new ArrayList<>();
        db = getReadableDatabase();
        String query = String.format("SELECT * FROM %s ORDER BY %s DESC",
                ItemContract.ItemEntry.TABLE_NAME,
                ItemContract.ItemEntry._ID);
        Cursor cursor = db.rawQuery(query,null);
        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(ItemContract.ItemEntry._ID));
            Crypto crypto = Crypto.fromSym(cursor.getString(cursor.getColumnIndex(
                    ItemContract.ItemEntry.COLUMN_NAME_CRYPTO)));
            double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex(
                    ItemContract.ItemEntry.COLUMN_NAME_PRICE)));
            Currency currency = Currency.getInstance(cursor.getString(cursor.getColumnIndex(
                    ItemContract.ItemEntry.COLUMN_NAME_CURRENCY)));
            Date created = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(
                    ItemContract.ItemEntry.COLUMN_NAME_CREATED))));
            Date updated = new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(
                    ItemContract.ItemEntry.COLUMN_NAME_UPDATED))));
            items.add(new Item(id,crypto,price,currency,created,updated));

        }
        return items;

    }

    public int updatePrice(long id,double price,Date ut){
        db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ItemContract.ItemEntry.COLUMN_NAME_PRICE,String.valueOf(price));
        contentValues.put(ItemContract.ItemEntry.COLUMN_NAME_UPDATED,String.valueOf(ut.getTime()));
        return db.update(ItemContract.ItemEntry.TABLE_NAME,contentValues,
                String.format("%s=%s", ItemContract.ItemEntry._ID,String.valueOf(id)),null);
    }

    public int deleteItem(long itemId){
        db = getWritableDatabase();
        return db.delete(ItemContract.ItemEntry.TABLE_NAME,
                String.format("%s=%s", ItemContract.ItemEntry._ID,String.valueOf(itemId)),null);
    }

}