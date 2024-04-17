package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.models.Donatur;
import com.psaainsankamil.vitone.models.Pesan;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 28/05/18.
 */

public class SendMessageContactActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textNama;
    private EditText textPesan;

    private RadioGroup radioGroup;

    private FloatingActionButton btnSelect;
    private Button btnSend;
    private Button btnBack;

    private Member member;
    private DatabaseVitone dbVitone;

    private Pesan pesan;

    private List<Donatur> listDonatur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_contact);
        getSupportActionBar().setTitle("Kirim Pesan SMS Center");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(SendMessageContactActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        if(getIntent().getStringArrayExtra("kontak") != null){
            String[] nama = getIntent().getStringArrayExtra("nama");
            String[] kontak = getIntent().getStringArrayExtra("kontak");
            String[] nama_kontak = getIntent().getStringArrayExtra("nama_kontak");
            String res = "";
            for(int i=0; i<kontak.length; i++){
                listDonatur.add(new Donatur("1", nama[i], kontak[i]));
                res += nama_kontak[i]+"; ";
            }
            textNama.setText(res);
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSelect:
                select();
                break;
            case R.id.btnSend:
                send();
                break;
            case R.id.btnBack:
                startActivity(new Intent(this, SendMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, SendMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        textNama = (EditText) findViewById(R.id.textNama);
        textPesan = (EditText) findViewById(R.id.textPesan);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnSelect = (FloatingActionButton) findViewById(R.id.btnSelect);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnBack = (Button) findViewById(R.id.btnBack);

        RadioButton radioPromo = (RadioButton) findViewById(R.id.radioPromo);
        radioPromo.setVisibility(View.GONE);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        pesan = dbVitone.getPesan("1");

        listDonatur = new ArrayList<>();
    }

    private void initListeners(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch(id){
                    case R.id.radioManual:
                        textPesan.setText("");
                        break;
                    case R.id.radioThanks:
                        textPesan.setText(pesan.getThanks());
                        break;
                    case R.id.radioInfo:
                        textPesan.setText(pesan.getInfo());
                        break;
                    case R.id.radioPromo:
                        textPesan.setText(pesan.getPromo());
                        break;
                }
            }
        });
        btnSelect.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void select(){
        startActivity(new Intent(SendMessageContactActivity.this, SendSelectDonaturActivity.class));
    }

    private void send(){

        class SendMessage extends AsyncTask<Void, Void, String> {

            ProgressDialog pd = new ProgressDialog(SendMessageContactActivity.this);

            @Override
            protected String doInBackground(Void...v){
                JSONArray obj = new JSONArray();
                try {
                    for(int i=0; i<listDonatur.size(); i++){
                        JSONObject ob = new JSONObject();
                        ob.put(Configuration.KEY_SEND_PESAN, listDonatur.get(i).getNama());
                        ob.put(Configuration.KEY_SEND_NOMOR, listDonatur.get(i).getKontak());
                        obj.put(ob);
                    }
                }catch (JSONException e){

                }
                HashMap<String,String> params = new HashMap<>();
                params.put("list", obj.toString());
                params.put(Configuration.KEY, member.getSPKey());

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"send", params);
                return res;
            }

            @Override
            protected void onPreExecute(){
                pd = ProgressDialog.show(SendMessageContactActivity.this, "Mengirim...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(String result){
                pd.dismiss();
                if(result.equals("SUCCESS")){
                    showAlert(true);
                }else{
                    showAlert(false);
                }
            }

            protected void showAlert(boolean info){
                AlertDialog.Builder alert = new AlertDialog.Builder(SendMessageContactActivity.this);
                if(info){
                    alert.setMessage("Kirim Pesan Berhasil!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pengiriman")
                            .create();
                }else{
                    alert.setMessage("Kirim Pesan Gagal!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pengiriman")
                            .create();
                }
                alert.show();
            }
        }

        String pesan = textPesan.getText().toString().trim();
        if(pesan.length() <= 140){
            for(int i=0; i<listDonatur.size(); i++){
                pesan = pesan.replace("<nama>", listDonatur.get(i).getNama());
                pesan = pesan.replace("\r\n","%0A");
                pesan = pesan.replace("\n","%0A");
                pesan.trim();
                listDonatur.get(i).setNama(pesan);
            }
            SendMessage sm = new SendMessage();
            sm.execute();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Jumlah karakter SMS terlalu banyak! :(")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setTitle("Kirim Pesan")
                    .create();
            alert.show();
            return;
        }

    }
}
