package com.devlab.cryptocore.models;

import java.util.Currency;
import java.util.Date;

/**
 * Created by shaibu on 10/24/17.
 */

public class Item {
    private Crypto crypto_type;
    private double price;
    private Date last_synced;
    private Currency currency;


    public Item(Crypto crypto_type, double price, Date last_synced, Currency currency){
        this.crypto_type = crypto_type;
        this.price = price;
        this.last_synced = last_synced;
        this.currency = currency;


    }

    public Item(Crypto crypto_type, String currency_code) {
        this(crypto_type,0,new Date(),Currency.getInstance(currency_code.toUpperCase()));
    }

    public Item(Crypto crypto_type, double price, String currency_code) {
        this(crypto_type,price,new Date(),Currency.getInstance(currency_code.toUpperCase()));
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

    public Date getLast_synced() {
        return last_synced;
    }

    public void setLast_synced(Date last_synced) {
        this.last_synced = last_synced;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }


}
