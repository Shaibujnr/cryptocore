package com.devlab.cryptocore.models;

import com.devlab.cryptocore.R;

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


    public static int getImageResource(Crypto crypto){
        switch (crypto){
            case BITCOIN:
                return R.drawable.bbtc;
            case ETHERIUM:
                return R.drawable.etth;
            default:
                return 0;
        }
    }
}
