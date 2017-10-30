package com.devlab.cryptocore.activities;

import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devlab.cryptocore.NetworkQueue;
import com.devlab.cryptocore.PriceHelper;
import com.devlab.cryptocore.R;
import com.devlab.cryptocore.adapters.ItemAdapter;
import com.devlab.cryptocore.fragments.CardDialog;
import com.devlab.cryptocore.interfaces.ItemUpdatedListener;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CardDialog.CardDialogListener,ItemUpdatedListener{
    ItemAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Item> items;
    FloatingActionButton floatingActionButton;
    RecyclerView.SmoothScroller smoothScroller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        items = new ArrayList<Item>();
        items.add(new Item(this,Crypto.BITCOIN,12345.4,"NGN"));
        items.add(new Item(this,Crypto.ETHERIUM,4567.8,"USD"));
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CardDialog().show(getSupportFragmentManager(),"create");
            }
        });

        smoothScroller = new LinearSmoothScroller(this) {
            @Override protected int getVerticalSnapPreference() {
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
    }


    @Override
    public void createCard(final CardDialog dialog, final ProgressBar progressBar, final Crypto crypto, final String code) {
        for(int i=0;i<items.size();i++){
            Item item = items.get(i);
            if(item.getCurrency().getCurrencyCode().equals(code) &&
                    item.getCrypto_type() == crypto){
                dialog.dismiss();
                Toast.makeText(this,"Card exists",Toast.LENGTH_SHORT).show();
                smoothScroller.setTargetPosition(i);
                layoutManager.startSmoothScroll(smoothScroller);
                CardView cardView = (CardView) recyclerView.getLayoutManager().findViewByPosition(i);
                return;
            }
        }
        progressBar.setVisibility(View.VISIBLE);
        Uri.Builder builder = Uri.parse(NetworkQueue.url_endpoint).buildUpon();
        builder.appendQueryParameter("fsym",crypto.toString());
        builder.appendQueryParameter("tsyms",code);
        String url = builder.build().toString();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PriceHelper priceHelper = new PriceHelper(response);
                        double price = priceHelper.getPrice(code.toUpperCase());
                        if(price == -1){
                            //Error Encountered
                            progressBar.setVisibility(View.GONE);
                            displayMessage("Error Encountered try again");
                            dialog.dismiss();

                        }
                        else{
                            progressBar.setVisibility(View.GONE);
                            items.add(0,new Item(MainActivity.this,crypto,price,code));
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                displayMessage("Error Encountered try again");
                displayMessage(error.toString());
                dialog.dismiss();

            }
        });
        NetworkQueue.getInstance(getApplicationContext()).addToRequestQueue(request);



    }

    private void displayMessage(String msg,int length){
        Toast.makeText(this,msg,length).show();
    }

    private void displayMessage(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPriceChanged(double old, double recent) {

    }

    @Override
    public void onDateCreatedChanged(Date old, Date recent) {

    }

    @Override
    public void onDateUpdatedChanged(Date old, Date recent) {

    }

    @Override
    public void onSourceUpdatedChanged(Date old, Date recent) {

    }
}
