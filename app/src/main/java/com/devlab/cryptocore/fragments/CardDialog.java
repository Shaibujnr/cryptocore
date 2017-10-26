package com.devlab.cryptocore.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.devlab.cryptocore.activities.MainActivity;
import com.devlab.cryptocore.R;
import com.devlab.cryptocore.adapters.SpinnerAdapter;
import com.devlab.cryptocore.models.Crypto;
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
        public void createCard(CardDialog cardDialog,ProgressBar progressBar,Crypto crypto,String code);
    }
    CardDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initSpinner(getContext());
        callingActivity = (MainActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        setCancelable(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.card_dialog,null);
        final Spinner spinner = v.findViewById(R.id.dialog_spinner);
        final RadioGroup radioGroup = v.findViewById(R.id.dialog_radio_group);
        final ProgressBar progressBar = v.findViewById(R.id.dialog_progress);
        final Button cancel = v.findViewById(R.id.dialog_cancel);
        final Button done = v.findViewById(R.id.dialog_done);
        spinnerAdapter = new SpinnerAdapter(spinnerItems);
        spinner.setAdapter(spinnerAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpinnerItem spinnerItem = (SpinnerItem) spinner.getSelectedItem();
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.dialog_btc:
                        mListener.createCard(CardDialog.this,progressBar,Crypto.BITCOIN,spinnerItem.getItem_text());
                        break;
                    case R.id.dialog_eth:
                        mListener.createCard(CardDialog.this,progressBar,Crypto.ETHERIUM,spinnerItem.getItem_text());dismiss();
                        break;
                    default:
                        Toast.makeText(getContext(),"Card Not Created Select either BTC or ETH",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        builder.setView(v);
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
