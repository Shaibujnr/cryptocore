package com.devlab.cryptocore.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.devlab.cryptocore.R;
import com.devlab.cryptocore.models.SpinnerItem;

import java.util.ArrayList;

/**
 * Created by shaibu on 10/24/17.
 */

public class SpinnerAdapter extends BaseAdapter {
    private ArrayList<SpinnerItem> spinnerItems;

    public SpinnerAdapter(ArrayList<SpinnerItem> items){
        this.spinnerItems = items;
    }
    @Override
    public int getCount() {
        return spinnerItems.size();
    }

    @Override
    public Object getItem(int i) {
        return spinnerItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spinner_item,viewGroup,false);
        TextView text = v.findViewById(R.id.spinner_item_code);
        ImageView img = v.findViewById(R.id.spinner_item_image);
        SpinnerItem current_item = spinnerItems.get(i);
        text.setText(current_item.getItem_text());
        img.setImageResource(current_item.getItem_image());
        return v;
    }
}
