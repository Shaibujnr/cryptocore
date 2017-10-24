package com.devlab.cryptocore.models;

/**
 * Created by shaibu on 10/24/17.
 */

public enum Crypto {
    BITCOIN(1,"BTC"),
    ETHERIUM(2,"ETH");

    private int value;
    private String code;

    Crypto(int value, String code){
        this.value = value;
        this.code = code;
    }

    public int value(){return this.value;}

    @Override
    public String toString() {
        return this.code;
    }

    public static Crypto fromInt(int value){
        switch (value){
            case 1:
                return BITCOIN;
            case 2:
                return ETHERIUM;
            default:
                return null;
        }
    }
}
