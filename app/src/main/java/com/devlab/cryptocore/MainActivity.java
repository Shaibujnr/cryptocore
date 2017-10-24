package com.devlab.cryptocore;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;

import com.devlab.cryptocore.adapters.ItemAdapter;
import com.devlab.cryptocore.models.Crypto;
import com.devlab.cryptocore.models.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        items = new ArrayList<Item>();
        items.add(new Item(Crypto.BITCOIN,"NGN"));
        items.add(new Item(Crypto.ETHERIUM,"USD"));
        recyclerView.setLayoutManager(layoutManager);
        ItemAdapter adapter = new ItemAdapter(items);
        recyclerView.setAdapter(adapter);
    }
}
