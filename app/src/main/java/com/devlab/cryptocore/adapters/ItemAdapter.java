package com.devlab.cryptocore.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.devlab.cryptocore.R;
import com.devlab.cryptocore.models.Item;
import com.devlab.cryptocore.models.SpinnerItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by shaibu on 10/24/17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    ArrayList<Item> items;
    ArrayList<SpinnerItem> spinnerItems;


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
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        private Spinner spinner;
        public ViewHolder(View itemView) {

            super(itemView);
            spinner = itemView.findViewById(R.id.item_spinner);
            SpinnerAdapter adapter = new SpinnerAdapter(spinnerItems);
            spinner.setAdapter(adapter);
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
