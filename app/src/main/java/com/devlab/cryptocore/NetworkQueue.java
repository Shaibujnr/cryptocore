package com.devlab.cryptocore;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by shaibu on 10/26/17.
 */

public class NetworkQueue {
    private static NetworkQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;
    public static final String url_endpoint = "https://min-api.cryptocompare.com/data/price";

    private NetworkQueue(Context context){
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkQueue(context);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }

}
