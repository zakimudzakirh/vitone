package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.psaainsankamil.vitone.adapters.MesjidAdapter;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.models.Lokasi;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 29/05/18.
 */

public class MesjidDataActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnUpdate;
    private Button btnBack;

    private SearchView searchView;

    private RecyclerView recyclerView;
    private MesjidAdapter adapter;
    private Member member;
    private DatabaseVitone dbVitone;
    private List<Lokasi> listMesjid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesjid_data);
        getSupportActionBar().setTitle("Data Mesjid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(MesjidDataActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnUpdate:
                update();
                break;
            case R.id.btnBack:
                back();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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
                final List<Lokasi> filterMesjid = filter(listMesjid, newText);
                adapter.setFilter(filterMesjid);
                return true;
            }
        });
        return true;
    }

    private List<Lokasi> filter(List<Lokasi> listLokasi, String query){
        query = query.toLowerCase();
        final List<Lokasi> filter = new ArrayList<>();

        for(Lokasi lokasi:listLokasi){
            final String text = lokasi.getNama().toLowerCase();
            if(text.contains(query)){
                filter.add(lokasi);
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
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (Button) findViewById(R.id.btnBack);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateMesjid();
    }

    private void initListeners(){
        btnUpdate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void update(){
        class Update extends AsyncTask<Void, Void, String> {

            ProgressDialog pd = new ProgressDialog(MesjidDataActivity.this);

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"all_mesjid", params);
                return res;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(MesjidDataActivity.this, "Update Info...","tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(String result) {
                pd.dismiss();
                int r = 0;
                try{
                    JSONObject res = new JSONObject(result);
                    if(res.getBoolean("status")){

                        JSONArray array = res.getJSONArray("mesjids");
                        List<Lokasi> listLokasi = new ArrayList<>();
                        for(int i=0; i<array.length(); i++){
                            JSONObject objA = new JSONObject(array.get(i).toString());
                            Lokasi lokasi = new Lokasi();
                            lokasi.setId(objA.getString("id"));
                            lokasi.setNama(objA.getString("nama"));
                            lokasi.setAlamat(objA.getString("alamat"));
                            lokasi.setContact(objA.getString("contact"));
                            lokasi.setPetugas(objA.getString("petugas"));
                            lokasi.setLat(objA.getString("lat"));
                            lokasi.setLon(objA.getString("lon"));
                            lokasi.setKategori(objA.getString("kategori"));
                            listLokasi.add(lokasi);
                        }
                        r = dbVitone.updateMesjid(listLokasi);
                    }
                }catch(JSONException er){
                    er.printStackTrace();
                    r = 0;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(MesjidDataActivity.this);
                if(r == 0){
                    alert.setMessage("Update Informasi gagal!\nKoneksi sedang bermasalah!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Update Info")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Update Informasi berhasil!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Update Info")
                            .create();
                    alert.show();
                    updateMesjid();
                }
            }
        }
        Update u = new Update();
        u.execute();
    }

    private void back(){
        startActivity(new Intent(this, MesjidMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void updateMesjid(){
        listMesjid = dbVitone.getListMesjid();
        adapter = new MesjidAdapter(listMesjid,this);
        recyclerView.setAdapter(adapter);
    }


}
