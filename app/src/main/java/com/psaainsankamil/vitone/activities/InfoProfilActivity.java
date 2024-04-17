package com.psaainsankamil.vitone.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.models.Profil;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zaki on 27/05/18.
 */

public class InfoProfilActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textEmail;
    private TextView textTelp;
    private TextView textAlamat;
    private TextView textProfil;
    private TextView textVisi;
    private TextView textMisi;

    private Button btnUpdate;
    private Button btnBack;

    private Member member;
    private DatabaseVitone dbVitone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_profil);
        getSupportActionBar().setTitle("Info Profil Panti");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(InfoProfilActivity.this, LoginActivity.class)
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
        textEmail = (TextView) findViewById(R.id.textEmail);
        textTelp = (TextView) findViewById(R.id.textTelp);
        textAlamat = (TextView) findViewById(R.id.textAlamat);
        textProfil = (TextView) findViewById(R.id.textProfil);
        textVisi = (TextView) findViewById(R.id.textVisi);
        textMisi = (TextView) findViewById(R.id.textMisi);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        Profil profil = dbVitone.getProfil("1");
        updateInfo(profil);
    }

    private void initListeners(){
        btnUpdate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void update(){
        class Update extends AsyncTask<Void, Void, String>{
            ProgressDialog pd = new ProgressDialog(InfoProfilActivity.this);

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"info_profil", params);

                return res;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(InfoProfilActivity.this,"Update Info...","Tunggu Sebentar");
            }

            @Override
            protected void onPostExecute(String result) {
                pd.dismiss();
                try{
                    JSONObject res = new JSONObject(result);
                    Profil profil = new Profil();
                    profil.setId("1");
                    profil.setEmail(res.getString("email"));
                    profil.setTelp(res.getString("telp"));
                    profil.setAlamat(res.getString("alamat"));
                    profil.setProfil(res.getString("profil"));
                    profil.setVisi(res.getString("visi"));
                    profil.setMisi(res.getString("misi"));
                    dbVitone.updateProfil(profil);
                    updateInfo(profil);
                }catch(JSONException er){
                    er.printStackTrace();
                }
            }
        }
        Update u = new Update();
        u.execute();
    }

    private void updateInfo(Profil profil){
        textEmail.setText(profil.getEmail());
        textTelp.setText(profil.getTelp());
        textAlamat.setText(profil.getAlamat());
        textProfil.setText(profil.getProfil());
        textVisi.setText(profil.getVisi());
        textMisi.setText(profil.getMisi());
    }
}
