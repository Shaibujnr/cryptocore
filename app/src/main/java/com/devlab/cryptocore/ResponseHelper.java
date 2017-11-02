package com.devlab.cryptocore;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;

/**
 * Created by shaibu on 10/26/17.
 */

public class ResponseHelper {
    JSONObject response;
    public ResponseHelper(JSONObject response){
        this.response = response;
    }

    public double getPrice(String code){
        try {
            String response_status = response.optString("Response",null);
            if(response_status != null && response_status.equals("Error")){
                Log.e("Fetching",response.getString("Message"));
                return -1;
            }
            return response.getDouble(code);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static String format(double price){
        return NumberFormat.getNumberInstance().format(price);
    }
}
