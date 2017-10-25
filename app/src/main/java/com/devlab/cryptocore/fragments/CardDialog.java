package com.devlab.cryptocore.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.devlab.cryptocore.MainActivity;
import com.devlab.cryptocore.R;
import com.devlab.cryptocore.adapters.SpinnerAdapter;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;
import com.devlab.cryptocore.models.SpinnerItem;

import java.util.ArrayList;

/**
 * Created by shaibu on 10/25/17.
 */

public class CardDialog extends DialogFragment {
    ArrayList<SpinnerItem> spinnerItems;
    SpinnerAdapter spinnerAdapter;
    MainActivity callingActivity;


    public interface CardDialogListener{
        public void addCard(Item card);
    }
    CardDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initSpinner(getContext());
        callingActivity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.card_dialog,null);
        final Spinner spinner = v.findViewById(R.id.dialog_spinner);
        final RadioGroup radioGroup = v.findViewById(R.id.dialog_radio_group);
        spinnerAdapter = new SpinnerAdapter(spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        builder.setView(v)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SpinnerItem spinnerItem = (SpinnerItem) spinner.getSelectedItem();
                        switch (radioGroup.getCheckedRadioButtonId()){
                            case R.id.dialog_btc:
                                mListener.addCard(new Item(Crypto.BITCOIN,spinnerItem.getItem_text()));
                                dismiss();
                                break;
                            case R.id.dialog_eth:
                                mListener.addCard(new Item(Crypto.ETHERIUM,spinnerItem.getItem_text()));
                                dismiss();
                                break;
                            default:
                                Toast.makeText(getContext(),"Card Not Created Select either BTC or ETH",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener = (CardDialogListener) context;
        }catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
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
