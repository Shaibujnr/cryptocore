package com.devlab.cryptocore.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.devlab.cryptocore.activities.ExchangeActivity;
import com.devlab.cryptocore.activities.MainActivity;
import com.devlab.cryptocore.db.ItemDbHelper;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;
import com.devlab.cryptocore.models.SpinnerItem;
import com.mikhaellopez.hfrecyclerview.HFRecyclerView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shaibu on 11/4/17.
 */

public class HitemAdapter extends HFRecyclerView<Item> {
    ArrayList<Item> items;
    ArrayList<SpinnerItem> spinnerItems;
    Context context;
    NetworkQueue queue;
    ItemDbHelper dbHelper;

    public HitemAdapter(List<Item> data) {
        super(data, false, true);
        items = (ArrayList<Item>) data;

    }

    @Override
    protected RecyclerView.ViewHolder getItemView(LayoutInflater inflater, ViewGroup parent) {
        View v = inflater.inflate(R.layout.item_card,parent,false);
        this.context = parent.getContext();
        dbHelper = new ItemDbHelper(context);
        queue = NetworkQueue.getInstance(context);
        initSpinner(context);
        return new ItemViewHolder(v);
    }

    @Override
    protected RecyclerView.ViewHolder getHeaderView(LayoutInflater inflater, ViewGroup parent) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder getFooterView(LayoutInflater inflater, ViewGroup parent) {
        return new FooterViewHolder(inflater.inflate(R.layout.footer,parent,false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            final Item currentItem = items.get(position);
            final ItemViewHolder ivh = (ItemViewHolder) holder;
            ivh.cryptoImage.setImageResource(Crypto.getImageResource(currentItem.getCrypto_type()));
            ivh.timeStamp.setText(String.format("%s: %S","Updated",new SimpleDateFormat("dd/MM/yyyy hh:mm").
                    format(currentItem.getUpdated())));
            ivh.priceText.setText(String.format("%s%s",currentItem.getCurrency().getSymbol(),
                    ResponseHelper.format(currentItem.getPrice())));
            ivh.crytoCode.setText(currentItem.getCrypto_type().toString());
            ivh.spinner.setCompoundDrawablesWithIntrinsicBounds(Item.getDrawableFromCode(currentItem
                    .getCurrency().getCurrencyCode().toUpperCase(),context),null,null,null);
            ivh.spinner.setText(currentItem.getCurrency().getCurrencyCode().toUpperCase());
            ivh.refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ivh.progressBar.setVisibility(View.VISIBLE);
                    Uri.Builder builder = Uri.parse(NetworkQueue.price_url_endpoint).buildUpon();
                    builder.appendQueryParameter("fsym",currentItem.getCrypto_type().toString());
                    builder.appendQueryParameter("tsyms",currentItem.getCurrency().getCurrencyCode().toUpperCase());
                    String url = builder.build().toString();
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                            url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    ResponseHelper responseHelper = new ResponseHelper(response);
                                    double price = responseHelper.getPrice(currentItem.getCurrency().getCurrencyCode().toUpperCase());
                                    if(price == -1){
                                        //Error Encountered
                                        Log.e("Fetching","Error here in Item adapter");
                                        ivh.progressBar.setVisibility(View.GONE);
                                        Toast.makeText(context,"Error Encountered",Toast.LENGTH_SHORT).show();

                                    }
                                    else{
                                        ivh.progressBar.setVisibility(View.GONE);
                                        Date update_time = new Date();
                                        int update_result =dbHelper.updatePrice(currentItem.get_id(),price,update_time);
                                        Log.e("Updaten",String.format("%s:%s","update_price",String.valueOf(price)));
                                        Log.e("Updaten",String.format("%s:%s","update_result",String.valueOf(update_result)));
                                        if(update_result > 0){
                                            //update was successful
                                            String price_str = currentItem.getCurrency().getSymbol()+ ResponseHelper.format(price);
                                            String updated_txt = String.format("%s: %S","Updated",
                                                    new SimpleDateFormat("dd/MM/yyyy hh:mm").format(update_time));
                                            ivh.priceText.setText(price_str);
                                            currentItem.setPrice(price);
                                            ivh.timeStamp.setText(updated_txt);
                                            currentItem.setUpdated(update_time);
                                        }

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ivh.progressBar.setVisibility(View.GONE);
                            Toast.makeText(context,"Error Encountered",Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.addToRequestQueue(request);
                }
            });
            ivh.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog dialog = new AlertDialog.Builder(context).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            int delete_result = dbHelper.deleteItem(currentItem.get_id());
                            Log.e("Deleten",String.valueOf(delete_result));
                            if(delete_result > 0){
                                items.remove(currentItem);
                                notifyDataSetChanged();
                            }


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
            ivh.self.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ExchangeActivity.class);
                    intent.putExtra("card_id",currentItem.get_id());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        String card_transition = context.getString(R.string.card_transition_string);
                        Pair<View,String> ct = Pair.create(ivh.self,card_transition);
                        String crypto_label_transition = context.getString(R.string.crypto_label_transition_string);
                        Pair<View,String> clt = Pair.create((View)ivh.crytoCode,crypto_label_transition);
                        String currency_code_transition = context.getString(R.string.currency_code_transition_string);
                        Pair<View,String> cct = Pair.create((View)ivh.spinner,currency_code_transition);
                        String price_transition = context.getString(R.string.price_transition_string);
                        Pair<View,String> pt = Pair.create((View)ivh.priceText,price_transition);
                        String refresh_transition = context.getString(R.string.refresh_transition_string);
                        Pair<View,String> rt = Pair.create((View)ivh.refresh,refresh_transition);
                        ActivityOptionsCompat options =
                                ActivityOptionsCompat.makeSceneTransitionAnimation((MainActivity)context,
                                        ct,clt,cct,pt,rt);
                        context.startActivity(intent,options.toBundle());
                    }
                    else{
                        context.startActivity(intent);
                    }
                }
            });

        }

    }
    public  class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView spinner;
        private TextView priceText,timeStamp,crytoCode;
        private ImageView cryptoImage;
        private ImageButton delete,refresh;
        private ProgressBar progressBar;
        private View self;

        public ItemViewHolder(View itemView) {
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

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
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
