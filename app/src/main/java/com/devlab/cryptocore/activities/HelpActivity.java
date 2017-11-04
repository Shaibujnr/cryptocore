package com.devlab.cryptocore.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devlab.cryptocore.NetworkQueue;
import com.devlab.cryptocore.R;
import com.devlab.cryptocore.ResponseHelper;
import com.devlab.cryptocore.db.ItemDbHelper;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;

import org.json.JSONObject;

import java.util.Currency;

public class HelpActivity extends AppCompatActivity {
    Button done;
    ProgressBar progressBar;
    TextView progressText;
    ItemDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        done = (Button) findViewById(R.id.help_done);
        progressBar = (ProgressBar) findViewById(R.id.help_progress);
        progressText = (TextView) findViewById(R.id.help_progress_text);
        dbHelper = new ItemDbHelper(this);
        Intent intent = getIntent();
        boolean sync = intent.getBooleanExtra("sync",false);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(HelpActivity.this,MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
            }
        });
        if(sync){
            displaySyncControls(true);
            Uri.Builder builder = Uri.parse(NetworkQueue.price_url_endpoint).buildUpon();
            builder.appendQueryParameter("fsym", Crypto.BITCOIN.toString());
            builder.appendQueryParameter("tsyms","NGN");
            String url = builder.build().toString();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            ResponseHelper responseHelper = new ResponseHelper(response);
                            double price = responseHelper.getPrice("NGN");
                            if(price == -1){
                                //Error Encountered
                                displaySyncControls(false);
                                displayMessage("Error Encountered Sync not successful");


                            }
                            else{

                                Item new_item = new Item(Crypto.BITCOIN,price, Currency.getInstance("NGN"));
                                long inserted_id = dbHelper.insertItem(new_item);
                                if(inserted_id > 0){
                                    //item successfully added into database
                                    //now update ui
                                    new_item.set_id(inserted_id);
                                    displaySyncControls(false);
                                }
                                else{
                                    displayMessage("Error Encountered, Sync not successful");
                                }

                            }
                            SharedPreferences sp = getSharedPreferences(getString(R.string.sp_name),MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("new",false);
                            editor.apply();


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    displaySyncControls(false);
                    displayMessage("Error Encountered Sync not successful");


                }
            });
            NetworkQueue.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
        else{
            displaySyncControls(false);
        }
    }

    private void displayMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    private void displaySyncControls(boolean bool){
        done.setEnabled(!bool);
        if(bool){
            progressText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        else{
            progressText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }

    }
}
