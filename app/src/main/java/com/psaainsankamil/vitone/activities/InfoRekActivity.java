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
import com.psaainsankamil.vitone.models.Rekening;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zaki on 29/05/18.
 */

public class InfoRekActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textNama;
    private TextView textBRI;
    private TextView textBNI;
    private TextView textBCA;
    private TextView textMandiri;
    private TextView textMuamalat;

    private Button btnUpdate;
    private Button btnBack;

    private Member member;
    private DatabaseVitone dbVitone;
    private Rekening rek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_rek);
        getSupportActionBar().setTitle("Info Rekening");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(InfoRekActivity.this, LoginActivity.class)
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

    private void initViews(){
        textNama = (TextView) findViewById(R.id.textNama);
        textBRI = (TextView) findViewById(R.id.textBRI);
        textBNI = (TextView) findViewById(R.id.textBNI);
        textBCA = (TextView) findViewById(R.id.textBCA);
        textMandiri = (TextView) findViewById(R.id.textMandiri);
        textMuamalat = (TextView) findViewById(R.id.textMuamalat);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        rek = dbVitone.getRekening("1");

        updateRek(rek);
    }

    private void initListeners(){
        btnUpdate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void update(){
        class Update extends AsyncTask<Void, Void, String> {
            ProgressDialog pd = new ProgressDialog(InfoRekActivity.this);

            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"info_rek", params);

                return res;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(InfoRekActivity.this,"Update Info...","Tunggu Sebentar");
            }

            @Override
            protected void onPostExecute(String result) {
                pd.dismiss();
                try{
                    JSONObject res = new JSONObject(result);
                    Rekening rek = new Rekening();
                    rek.setId("1");
                    rek.setNama(res.getString("nama"));
                    rek.setBri(res.getString("bri"));
                    rek.setBni(res.getString("bni"));
                    rek.setBca(res.getString("bca"));
                    rek.setMandiri(res.getString("mandiri"));
                    rek.setMuamalat(res.getString("muamalat"));
                    dbVitone.updateRek(rek);
                    updateRek(rek);
                }catch(JSONException er){
                    er.printStackTrace();
                }
            }
        }
        Update u = new Update();
        u.execute();
    }

    private void back(){
        startActivity(new Intent(this, InfoMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

    private void updateRek(Rekening rek){
        textNama.setText(rek.getNama());
        textBRI.setText(rek.getBri());
        textBNI.setText(rek.getBni());
        textBCA.setText(rek.getBca());
        textMandiri.setText(rek.getMandiri());
        textMuamalat.setText(rek.getMuamalat());
    }
}
