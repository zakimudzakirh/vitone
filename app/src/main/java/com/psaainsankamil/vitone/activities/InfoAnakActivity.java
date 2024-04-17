package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.adapters.AnakAdapter;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.models.Anak;
import com.psaainsankamil.vitone.models.InfoAnak;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 27/05/18.
 */

public class InfoAnakActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textTotal;
    private TextView textTotMukim;
    private TextView textTotNonMukim;
    private TextView textTotYatim;
    private TextView textTotDhuafa;

    private RecyclerView recyclerView;
    private AnakAdapter adapter;
    private List<Anak> listAnak;

    private Button btnUpdate;
    private Button btnBack;

    private Member member;
    private DatabaseVitone dbVitone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_anak);
        getSupportActionBar().setTitle("Info Anak Panti");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(InfoAnakActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUpdate:
                update();
                break;
            case R.id.btnBack:
                startActivity(new Intent(this, InfoMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, InfoMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        textTotal = (TextView) findViewById(R.id.textTotal);
        textTotMukim = (TextView) findViewById(R.id.textTotMukim);
        textTotNonMukim = (TextView) findViewById(R.id.textTotNonMukim);
        textTotYatim = (TextView) findViewById(R.id.textTotYatim);
        textTotDhuafa = (TextView) findViewById(R.id.textTotDhuafa);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        InfoAnak info = dbVitone.getInfoAnak("1");
        updateInfo(info);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setRecyclerViewData(); // call adding data to array list method
        listAnak = dbVitone.getListAnak();
        adapter = new AnakAdapter(listAnak,this);
        recyclerView.setAdapter(adapter);
    }

    private void initListeners(){
        btnUpdate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void update(){
        class Update extends AsyncTask<Void, Void, String>{

            ProgressDialog pd = new ProgressDialog(InfoAnakActivity.this);

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"info_anak", params);
                return res;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(InfoAnakActivity.this, "Update Info...","tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(String result) {
                pd.dismiss();
                int r = 0;
                try{
                    JSONObject res = new JSONObject(result);
                    if(res.getBoolean("status")){
                        InfoAnak info = new InfoAnak();
                        info.setId("1");
                        info.setJmlh(res.getInt("jmlh"));
                        info.setMukim(res.getInt("mukim"));
                        info.setDhuafa(res.getInt("dhuafa"));
                        r = dbVitone.updateInfoAnak(info);
                        updateInfo(info);

                        JSONArray array = res.getJSONArray("anaks");
                        List<Anak> anaks = new ArrayList<>();
                        for(int i=0; i<array.length(); i++){
                            JSONObject objA = new JSONObject(array.get(i).toString());
                            Anak anak = new Anak();
                            anak.setId(objA.getString("id"));
                            anak.setNama(objA.getString("nama"));
                            anak.setInfomukim(objA.getString("mukim"));
                            anak.setInfoDhuafa(objA.getString("yatim"));
                            anaks.add(anak);
                        }
                        r = dbVitone.updateAnak(anaks);
                    }
                }catch(JSONException er){
                    er.printStackTrace();
                    r = 0;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(InfoAnakActivity.this);
                if(r == 0){
                    alert.setMessage("Update Informasi gagal!")
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
                    updateAnak();
                }
            }
        }
        Update u = new Update();
        u.execute();
    }

    private void updateInfo(InfoAnak info){
        textTotal.setText(String.valueOf(info.getJmlh()));
        textTotMukim.setText(String.valueOf(info.getMukim()));
        textTotNonMukim.setText(String.valueOf(info.getNonMukim()));
        textTotYatim.setText(String.valueOf(info.getYatim()));
        textTotDhuafa.setText(String.valueOf(info.getDhuafa()));
    }

    private void updateAnak(){
        listAnak = dbVitone.getListAnak();
        adapter = new AnakAdapter(listAnak,this);
        recyclerView.setAdapter(adapter);
    }

}
