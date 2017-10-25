package com.devlab.cryptocore.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.devlab.cryptocore.R;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;
import com.devlab.cryptocore.models.SpinnerItem;

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
    SpinnerAdapter spinnerAdapter;


    public ItemAdapter(ArrayList<Item> items){
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card,parent,false);
        initSpinner(parent.getContext());
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Item currentItem = items.get(position);
        holder.cryptoImage.setImageResource(Crypto.getImageResource(currentItem.getCrypto_type()));
        holder.timeStamp.setText("Updated: "+
                new SimpleDateFormat("dd/MM/yyyy hh:mm").format(currentItem.getLast_synced()));
        holder.priceText.setText(currentItem.getCurrency().getSymbol()+"1,234,567,890");
        holder.crytoCode.setText(currentItem.getCrypto_type().toString());
        int spinner_pos = spinnerAdapter.getPosbyCode(currentItem.getCurrency().getCurrencyCode().toUpperCase());
        holder.spinner.setSelection(spinner_pos);
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SpinnerItem spinnerItem = spinnerItems.get(i);
                currentItem.setCurrency(Currency.getInstance(spinnerItem.getItem_text().toUpperCase()));
                String price = currentItem.getCurrency().getSymbol()+"1,234,567,890";
                holder.priceText.setText(price);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        private Spinner spinner;
        private TextView priceText,timeStamp,crytoCode;
        private ImageView cryptoImage;
        private ImageButton delete,refresh;
        public ViewHolder(View itemView) {

            super(itemView);
            spinner = itemView.findViewById(R.id.item_spinner);
            priceText = itemView.findViewById(R.id.item_price);
            timeStamp = itemView.findViewById(R.id.item_time_stamp);
            crytoCode = itemView.findViewById(R.id.item_crypto_code);
            cryptoImage = itemView.findViewById(R.id.item_image);
            delete = itemView.findViewById(R.id.item_delete);
            refresh = itemView.findViewById(R.id.item_refresh);
            spinnerAdapter = new SpinnerAdapter(spinnerItems);
            spinner.setAdapter(spinnerAdapter);

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
