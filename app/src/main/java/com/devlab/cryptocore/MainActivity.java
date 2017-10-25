package com.devlab.cryptocore;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;
import android.widget.Toast;

import com.devlab.cryptocore.adapters.ItemAdapter;
import com.devlab.cryptocore.fragments.CardDialog;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CardDialog.CardDialogListener{
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
        items.add(new Item(Crypto.BITCOIN,"NGN"));
        items.add(new Item(Crypto.ETHERIUM,"USD"));
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
    public void addCard(Item card) {
        for(int i=0;i<items.size();i++){
            Item item = items.get(i);
            if(item.getCurrency().getCurrencyCode().equals(card.getCurrency().getCurrencyCode()) &&
                    item.getCrypto_type() == card.getCrypto_type()){
                Toast.makeText(this,"Card exists",Toast.LENGTH_SHORT).show();
                smoothScroller.setTargetPosition(i);
                layoutManager.startSmoothScroll(smoothScroller);
                CardView cardView = (CardView) recyclerView.getLayoutManager().findViewByPosition(i);
                return;
            }
        }
        items.add(card);
        adapter.notifyDataSetChanged();
    }
}
