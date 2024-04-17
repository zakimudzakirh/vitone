package com.psaainsankamil.vitone.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.adapters.SelectAdapter;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.models.Donatur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 28/05/18.
 */

public class SendSelectActivity extends AppCompatActivity implements View.OnClickListener{

    SearchView searchView;

    private Button btnBack;

    private RecyclerView recyclerView;

    private SelectAdapter adapter;
    private List<Donatur> listDonatur;
    private DatabaseVitone dbVitone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_select);
        getSupportActionBar().setTitle("Pilih Donatur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBack:
                back();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                back();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(getResources().getColor(android.R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Donatur> filterModelList = filter(listDonatur, newText);
                adapter.setFilter(filterModelList);
                return true;
            }
        });
        return true;
    }

    private List<Donatur> filter(List<Donatur> listDonatur, String query){
        query = query.toLowerCase();
        final List<Donatur> filter = new ArrayList<>();

        for(Donatur donatur:listDonatur){
            final String text = donatur.getNama().toLowerCase();
            if(text.startsWith(query)){
                filter.add(donatur);
            }
        }
        return filter;
    }

    private void changeSearchViewTextColor(View view){
        if(view != null){
            if(view instanceof TextView){
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            }else if(view instanceof ViewGroup){
                ViewGroup viewGroup = (ViewGroup) view;
                for(int i=0; i<viewGroup.getChildCount(); i++){
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    private void initViews(){
        btnBack = (Button) findViewById(R.id.btnBack);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void initObjects(){
        dbVitone = new DatabaseVitone(this);
        //listDonatur = new ArrayList<>();
        listDonatur = dbVitone.getListDonatur();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SelectAdapter(listDonatur,this, this);
        recyclerView.setAdapter(adapter);
    }

    private void initListeners(){
        btnBack.setOnClickListener(this);
    }

    private void back(){
        startActivity(new Intent(this, SendMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    public void selected(Donatur donatur){
        Intent intent = new Intent(this, SendMessageActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("donatur", new String[]{donatur.getNama(), donatur.getKontak()});
        startActivity(intent);
        //startActivity(new Intent(this, DonaturMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
