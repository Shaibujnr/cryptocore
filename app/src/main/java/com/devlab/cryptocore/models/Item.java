package com.devlab.cryptocore.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.devlab.cryptocore.R;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;

/**
 * Created by shaibu on 10/24/17.
 */

public class Item implements Serializable{
    private long _id;
    private Crypto crypto_type;
    private double price;
    private Date created;
    private Date updated;
    private Currency currency;



    public Item(long _id, Crypto crypto_type, double price, Currency currency, Date created, Date updated) {
        this._id = _id;
        this.crypto_type = crypto_type;
        this.price = price;
        this.created = created;
        this.updated = updated;
        this.currency = currency;
    }

    public Item(Crypto crypto_type, double price, Currency currency) {
        //constructor for creating item vai mainactivity
        this(-1,crypto_type,price,currency,new Date(),new Date());

    }


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public Crypto getCrypto_type() {
        return crypto_type;
    }

    public void setCrypto_type(Crypto crypto_type) {
        this.crypto_type = crypto_type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;

    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;

    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;

    }


    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public static Drawable getDrawableFromCode(String code,Context context){
        String[] choices = context.getResources().getStringArray(R.array.spinner_choices);
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.spinner_images);
        for(int i=0;i<choices.length;i++){
            if(choices[i].equals(code)){
                return imgs.getDrawable(i);
            }
        }
        return null;
    }




}
