package com.devlab.cryptocore.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devlab.cryptocore.NetworkQueue;
import com.devlab.cryptocore.PriceHelper;
import com.devlab.cryptocore.R;
import com.devlab.cryptocore.activities.ExchangeActivity;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;
import com.devlab.cryptocore.models.SpinnerItem;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

/**
 * Created by shaibu on 10/24/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    ArrayList<Item> items;
    ArrayList<SpinnerItem> spinnerItems;
    Context context;
    NetworkQueue queue;


    public ItemAdapter(ArrayList<Item> items){
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
        this.context = parent.getContext();
        queue = NetworkQueue.getInstance(context);
        initSpinner(context);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item currentItem = items.get(position);
        holder.cryptoImage.setImageResource(Crypto.getImageResource(currentItem.getCrypto_type()));
        holder.timeStamp.setText(String.format("%s: %S","Updated",new SimpleDateFormat("dd/MM/yyyy hh:mm").
                        format(currentItem.getUpdated())));
        holder.priceText.setText(String.format("%s%s",currentItem.getCurrency().getSymbol(),
                PriceHelper.format(currentItem.getPrice())));
        holder.crytoCode.setText(currentItem.getCrypto_type().toString());
        holder.spinner.setCompoundDrawablesWithIntrinsicBounds(Item.getDrawableFromCode(currentItem
        .getCurrency().getCurrencyCode().toUpperCase(),context),null,null,null);
        holder.spinner.setText(currentItem.getCurrency().getCurrencyCode().toUpperCase());
        holder.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
                Uri.Builder builder = Uri.parse(NetworkQueue.url_endpoint).buildUpon();
                builder.appendQueryParameter("fsym",currentItem.getCrypto_type().toString());
                builder.appendQueryParameter("tsyms",currentItem.getCurrency().getCurrencyCode().toUpperCase());
                String url = builder.build().toString();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                PriceHelper priceHelper = new PriceHelper(response);
                                double price = priceHelper.getPrice(currentItem.getCurrency().getCurrencyCode().toUpperCase());
                                if(price == -1){
                                    //Error Encountered
                                    Log.e("Fetching","Error here in Item adapter");
                                    holder.progressBar.setVisibility(View.GONE);;
                                    Toast.makeText(context,"Error Encountered",Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    holder.progressBar.setVisibility(View.GONE);
                                    currentItem.setPrice(price);
                                    String price_str = currentItem.getCurrency().getSymbol()+PriceHelper.format(currentItem.getPrice());
                                    holder.priceText.setText(price_str);
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        holder.progressBar.setVisibility(View.GONE);
                        Toast.makeText(context,"Error Encountered",Toast.LENGTH_SHORT).show();
                    }
                });
                queue.addToRequestQueue(request);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(context).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        items.remove(currentItem);
                        notifyDataSetChanged();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setTitle("Delete?")
                        .create();
                dialog.show();

            }
        });
        holder.self.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Card Clicked",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ExchangeActivity.class);
                intent.putExtra("card_crypto_type",currentItem.getCrypto_type());
                intent.putExtra("card_currency_code",currentItem.getCurrency().getCurrencyCode());
                intent.putExtra("card_price",currentItem.getPrice());
                intent.putExtra("card_created",currentItem.getCreated());
                intent.putExtra("card_updated",currentItem.getUpdated());
                intent.putExtra("card_source_updated",currentItem.getSource_updated());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        private TextView spinner;
        private TextView priceText,timeStamp,crytoCode;
        private ImageView cryptoImage;
        private ImageButton delete,refresh;
        private ProgressBar progressBar;
        private View self;

        public ViewHolder(View itemView) {

            super(itemView);
            self = itemView;
            spinner = itemView.findViewById(R.id.item_spinner);
            priceText = itemView.findViewById(R.id.item_price);
            timeStamp = itemView.findViewById(R.id.item_time_stamp);
            crytoCode = itemView.findViewById(R.id.item_crypto_code);
            cryptoImage = itemView.findViewById(R.id.item_image);
            delete = itemView.findViewById(R.id.item_delete);
            refresh = itemView.findViewById(R.id.item_refresh);
            progressBar = itemView.findViewById(R.id.item_progress);

        }
    }


    public void initSpinner(Context context){
        spinnerItems = new ArrayList<SpinnerItem>();
        TypedArray spinner_images = context.getResources().obtainTypedArray(R.array.spinner_images);
        String[] spinner_choices = context.getResources().getStringArray(R.array.spinner_choices);
        for(int i=0;i<spinner_choices.length;i++){
            spinnerItems.add(new SpinnerItem(spinner_choices[i],spinner_images.getResourceId(i,0)));
        }
    }

}
