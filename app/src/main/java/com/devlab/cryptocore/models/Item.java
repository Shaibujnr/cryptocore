package com.devlab.cryptocore.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.devlab.cryptocore.R;
import com.devlab.cryptocore.interfaces.ItemUpdatedListener;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;

/**
 * Created by shaibu on 10/24/17.
 */

public class Item implements Serializable{
    private Crypto crypto_type;
    private double price;
    private Date created;
    private Date updated;
    private Date source_updated;
    private Currency currency;
    private ItemUpdatedListener updatedListener;

    public Item(Context context,Crypto crypto_type, double price, Date created, Date updated,
                Date source_updated, Currency currency) {
        try{
            updatedListener = (ItemUpdatedListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement ItemUpdatedListener");
        }

        this.crypto_type = crypto_type;
        this.price = price;
        this.created = created;
        this.updated = updated;
        this.source_updated = source_updated;
        this.currency = currency;
    }

    public Item(Crypto crypto_type, double price, Currency currency){
        this(null,crypto_type,
                price,
                new Date(),
                new Date(),
                new Date(),
                currency);


    }


    public Item(Crypto crypto_type, double price, String currency_code) {
        this(crypto_type,price,Currency.getInstance(currency_code.toUpperCase()));

    }

    public Item(Context context,Crypto crypto_type,double price,String currency_code){
        this(context,
                crypto_type,
                price,
                new Date(),new Date(),new Date(),Currency.getInstance(currency_code));
    }

    public Item(Crypto crypto,double price,String currency_code,Date source_updated){
        this(null,
                crypto,
                price,
                new Date(),
                new Date(),
                source_updated,
                Currency.getInstance(currency_code.toUpperCase()));
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
        double old_price = this.price;
        this.price = price;
        updatedListener.onPriceChanged(old_price,price);
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        Date old_date = this.created;
        this.created = created;
        updatedListener.onDateCreatedChanged(old_date,created);
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        Date old = this.updated;
        this.updated = updated;
        updatedListener.onDateUpdatedChanged(old,updated);
    }

    public Date getSource_updated() {
        return source_updated;
    }

    public void setSource_updated(Date source_updated) {
        Date old = this.source_updated;
        this.source_updated = source_updated;
        updatedListener.onSourceUpdatedChanged(old,source_updated);
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
                Toast.makeText(context,"found it",Toast.LENGTH_SHORT).show();
                return imgs.getDrawable(i);
            }
        }
        return null;
    }




}
