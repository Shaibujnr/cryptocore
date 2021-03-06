package com.devlab.cryptocore.models;

import com.devlab.cryptocore.R;

import java.io.Serializable;

/**
 * Created by shaibu on 10/24/17.
 */

public enum Crypto implements Serializable{
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

    public static Crypto fromSym(String sym){
        if(sym.equals("BTC")){
            return BITCOIN;
        }
        else if(sym.equals("ETH")){
            return ETHERIUM;
        }
        else{
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
