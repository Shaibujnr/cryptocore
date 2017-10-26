package com.devlab.cryptocore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by shaibu on 10/26/17.
 */

public class PriceHelper {
    JSONObject response;
    public PriceHelper(JSONObject response){
        this.response = response;
    }

    public double getPrice(){
        try {
            int response_type = response.getInt("Type");
            JSONArray array = response.getJSONArray("Data");
            if(response_type != 100){
                return -1;
            }
            JSONObject result = array.getJSONObject(0);
            double price = result.getDouble("Price");
            return price;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
