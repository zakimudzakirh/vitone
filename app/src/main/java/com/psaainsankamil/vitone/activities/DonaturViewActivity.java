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
import com.psaainsankamil.vitone.adapters.DonaturAdapter;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.models.Donatur;
import com.psaainsankamil.vitone.session.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 27/05/18.
 */

public class DonaturViewActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private Button btnBack;

    private SearchView searchView;

    private Member member;
    private DatabaseVitone dbVitone;
    private DonaturAdapter adapter;
    private List<Donatur> listDonatur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donatur_view);
        getSupportActionBar().setTitle("Data Donatur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(DonaturViewActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnBack:
                startActivity(new Intent(this, DonaturMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, DonaturMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
                final List<Donatur> filterDonatur = filter(listDonatur, newText);
                adapter.setFilter(filterDonatur);
                return true;
            }
        });
        return true;
    }

    private List<Donatur> filter(List<Donatur> list, String query){
        query = query.toLowerCase();
        final List<Donatur> filter = new ArrayList<>();

        for(Donatur donatur:list){
            final String text = donatur.getNama().toLowerCase();
            if(text.contains(query)){
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
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        listDonatur = dbVitone.getListDonatur();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DonaturAdapter(listDonatur, this);
        recyclerView.setAdapter(adapter);
    }

    private void initListeners(){
        btnBack.setOnClickListener(this);
    }

    public List<Donatur> getListDonatur(){
        return listDonatur;
    }

    public void done(){
        startActivity(new Intent(this, DonaturViewActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
