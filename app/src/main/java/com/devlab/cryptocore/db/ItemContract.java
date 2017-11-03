package com.devlab.cryptocore.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.devlab.cryptocore.models.Item;

/**
 * Created by shaibu on 11/2/17.
 */

public class ItemContract {

    private ItemContract(){}
    public static final String SQL_CREATE_ENTRIES = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY," +
                    "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT)", ItemEntry.TABLE_NAME,
            ItemEntry._ID,
            ItemEntry.COLUMN_NAME_CRYPTO,
            ItemEntry.COLUMN_NAME_PRICE,
            ItemEntry.COLUMN_NAME_CURRENCY,
            ItemEntry.COLUMN_NAME_CREATED,
            ItemEntry.COLUMN_NAME_UPDATED);

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME;

    public static class ItemEntry implements BaseColumns{
        public static final String TABLE_NAME = "Items";
        public static final String COLUMN_NAME_CRYPTO = "Crypto";
        public static final String COLUMN_NAME_PRICE = "Price";
        public static final String COLUMN_NAME_CURRENCY = "Currency";
        public static final String COLUMN_NAME_CREATED = "Created";
        public static final String COLUMN_NAME_UPDATED = "Updated";


    }




}
