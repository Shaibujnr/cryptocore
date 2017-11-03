package com.devlab.cryptocore.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devlab.cryptocore.NetworkQueue;
import com.devlab.cryptocore.ResponseHelper;
import com.devlab.cryptocore.R;
import com.devlab.cryptocore.interfaces.ItemUpdatedListener;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Currency;
import java.util.Date;

public class ExchangeActivity extends AppCompatActivity implements ItemUpdatedListener {
    Item item;
    ImageButton refresh;
    EditText crypto_edit,currency_edit;
    TextView crypto_title,
            price_text,
            crypto_label,
            currency_label,
            card_created_month,
            card_created_day,
            card_created_year,
            card_created_time,
            card_updated_month,
            card_updated_day,
            card_updated_year,
            card_updated_time,
            source_updated_month,
            source_updated_day,
            source_updated_year,
            source_updated_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        refresh = (ImageButton) findViewById(R.id.exchange_refresh);
        crypto_edit = (EditText) findViewById(R.id.exchange_card_crypt_edit);
        currency_edit = (EditText) findViewById(R.id.exchange_card_price_edit);
        crypto_title = (TextView) findViewById(R.id.exchange_card_crypt_text);
        price_text = (TextView) findViewById(R.id.exchange_card_price_text);
        crypto_label = (TextView) findViewById(R.id.exchange_card_crypt_label);
        currency_label = (TextView) findViewById(R.id.exchange_card_price_label);
        card_created_month = (TextView) findViewById(R.id.exchange_card_created_month);
        card_created_day = (TextView) findViewById(R.id.exchange_card_created_day);
        card_created_year = (TextView) findViewById(R.id.exchange_card_created_year);
        card_created_time = (TextView) findViewById(R.id.exchange_card_created_time);
        card_updated_day = (TextView) findViewById(R.id.exchange_card_updated_day);
        card_updated_month = (TextView) findViewById(R.id.exchange_card_updated_month);
        card_updated_year = (TextView) findViewById(R.id.exchange_card_updated_year);
        card_updated_time = (TextView) findViewById(R.id.exchange_card_updated_time);
        source_updated_month = (TextView) findViewById(R.id.exchange_source_updated_month);
        source_updated_day = (TextView) findViewById(R.id.exchange_source_updated_day);
        source_updated_year = (TextView) findViewById(R.id.exchange_source_updated_year);
        source_updated_time = (TextView) findViewById(R.id.exchange_source_updated_time);
        item = getItemFromIntent();
        String[] card_created_format = dmyt(item.getCreated());
        String[] card_updated_format = dmyt(item.getUpdated());
//        String[] source_updated_format = dmyt(item.getSource_updated());
        card_created_day.setText(card_created_format[0]);
        card_created_month.setText(card_created_format[1]);
        card_created_year.setText(card_created_format[2]);
        card_created_time.setText(card_created_format[3]);
        card_updated_day.setText(card_updated_format[0]);
        card_updated_month.setText(card_updated_format[1]);
        card_updated_year.setText(card_updated_format[2]);
        card_updated_time.setText(card_updated_format[3]);
//        source_updated_day.setText(source_updated_format[0]);
//        source_updated_month.setText(source_updated_format[1]);
//        source_updated_year.setText(source_updated_format[2]);
//        source_updated_time.setText(source_updated_format[3]);
        price_text.setText(String.format("%s %s",
                ResponseHelper.format(item.getPrice()),
                item.getCurrency().getCurrencyCode()));
        crypto_label.setText(item.getCrypto_type().toString());
        currency_label.setText(item.getCurrency().getCurrencyCode());
        initExchangeTitle();
        initExchange();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Crypto crypto = item.getCrypto_type();
                final String code = item.getCurrency().getCurrencyCode();
                Uri.Builder builder = Uri.parse(NetworkQueue.price_url_endpoint).buildUpon();
                builder.appendQueryParameter("fsym",crypto.toString());
                builder.appendQueryParameter("tsyms",code);
                String url = builder.build().toString();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                ResponseHelper responseHelper = new ResponseHelper(response);
                                double price = responseHelper.getPrice(code.toUpperCase());
                                if(price == -1){
                                    //Error Encountered
                                    Toast.makeText(ExchangeActivity.this,
                                            "Error Encountered try again",Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    item.setPrice(price);

                                }


                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ExchangeActivity.this,
                                "Error Encountered try again",Toast.LENGTH_SHORT).show();

                    }
                });
                NetworkQueue.getInstance(getApplicationContext()).addToRequestQueue(request);
            }
        });
    }

    private Item getItemFromIntent(){
        Intent intent = getIntent();
        Crypto crypto = (Crypto) intent.getSerializableExtra("card_crypto_type");
        String code = intent.getStringExtra("card_currency_code");
        double price = intent.getDoubleExtra("card_price",0);
        Date created = (Date) intent.getSerializableExtra("card_created");
        Date updated = (Date) intent.getSerializableExtra("card_updated");
        Date source_updated = (Date) intent.getSerializableExtra("card_source_updated");
        return new Item(crypto,price,Currency.getInstance(code.toUpperCase()));
    }

    private void initExchange(){
        crypto_edit.setHint("1");
        currency_edit.setHint(String.valueOf(item.getPrice()));
        crypto_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(crypto_edit.isFocused() && charSequence.toString().length()>0){
                    try {
                        Log.e("Seq",charSequence.toString());
                        String bigDecimal = new BigDecimal(charSequence.toString()).toPlainString();
                        Log.e("Deci",bigDecimal);
                        Double crypto_price = Double.parseDouble(bigDecimal);
                        Log.e("Val",String.valueOf(crypto_price));
                        Double base_result = crypto_price * item.getPrice();
                        currency_edit.setText(String.valueOf(base_result));
                    }
                    catch(Exception e){
                        currency_edit.setText("");
                    }

                }
                else if(crypto_edit.isFocused() && charSequence.toString().length()<=0){
                    currency_edit.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        currency_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(currency_edit.isFocused() && charSequence.toString().length()>0){
                    try{
                        Log.e("Seq",charSequence.toString());
                        String bigDecimal = new BigDecimal(charSequence.toString()).toPlainString();
                        Log.e("Deci",bigDecimal);
                        Double base_price = Double.parseDouble(bigDecimal);
                        Log.e("Val",String.valueOf(base_price));
                        Double crypto_result = base_price/item.getPrice();
                        crypto_edit.setText(String.valueOf(crypto_result));
                    }
                    catch (Exception e){
                        crypto_edit.setText("");
                    }

                }
                else if(currency_edit.isFocused() && charSequence.toString().length()<=0){
                    crypto_edit.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initExchangeTitle(){
        Crypto crypto = item.getCrypto_type();
        switch (crypto){
            case BITCOIN:
                crypto_title.setText("1 Bitcoin Equals");
                break;
            case ETHERIUM:
                crypto_title.setText("1 Ethereum Equals");
                break;
        }
    }

    private String[] dmyt(Date date){
        String[] result = new String[4];
        String date_format = DateFormat.getDateTimeInstance(2,3).format(date);
        Log.e("Lerrors",date_format);
        String[] splitted = date_format.split(",");
        String year_time = splitted[1].substring(1);
        String[] ytp = year_time.split(" ");
        String[] dm = splitted[0].split(" ");
        result[0] = dm[1];
        result[1] = dm[0];
        result[2] = ytp[0];
        result[3] = ytp[1];
        return result;

    }

    @Override
    public void onPriceChanged(double old, double recent) {
        price_text.setText(String.format("%s %s",ResponseHelper.format(recent),item.getCurrency().getCurrencyCode()));
    }

    @Override
    public void onDateCreatedChanged(Date old, Date recent) {
        String[] dmyt = dmyt(recent);
        card_created_day.setText(dmyt[0]);
        card_created_month.setText(dmyt[1]);
        card_created_year.setText(dmyt[2]);
        card_created_time.setText(dmyt[3]);
    }

    @Override
    public void onDateUpdatedChanged(Date old, Date recent) {
        String[] dmyt = dmyt(recent);
        card_updated_day.setText(dmyt[0]);
        card_updated_month.setText(dmyt[1]);
        card_updated_year.setText(dmyt[2]);
        card_updated_time.setText(dmyt[3]);
    }

    @Override
    public void onSourceUpdatedChanged(Date old, Date recent) {
        String[] dmyt = dmyt(recent);
        source_updated_day.setText(dmyt[0]);
        source_updated_month.setText(dmyt[1]);
        source_updated_year.setText(dmyt[2]);
        source_updated_time.setText(dmyt[3]);
    }
}
