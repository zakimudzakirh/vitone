package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.models.Pesan;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zaki on 26/05/18.
 */

public class SendSettingActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText textThanks;
    private EditText textInformasi;
    private EditText textPromosi;

    private Button btnServerThanks;
    private Button btnServerInfo;
    private Button btnServerPromo;

    private Button btnSaveThanks;
    private Button btnSaveInfo;
    private Button btnSavePromo;

    private Button btnBack;

    private Member member;
    private DatabaseVitone dbVitone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_setting);
        getSupportActionBar().setTitle("Setting Pesan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(SendSettingActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnServerThanks:
                serverThanks();
                break;
            case R.id.btnServerInfo:
                serverInfo();
                break;
            case R.id.btnServerPromo:
                serverPromo();
                break;
            case R.id.btnSaveThanks:
                saveThanks();
                break;
            case R.id.btnSaveInfo:
                saveInfo();
                break;
            case R.id.btnSavePromo:
                savePromo();
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
        textThanks = (EditText) findViewById(R.id.textThanks);
        textInformasi = (EditText) findViewById(R.id.textInformasi);
        textPromosi = (EditText) findViewById(R.id.textPromosi);

        btnServerThanks = (Button) findViewById(R.id.btnServerThanks);
        btnServerInfo = (Button) findViewById(R.id.btnServerInfo);
        btnServerPromo = (Button) findViewById(R.id.btnServerPromo);

        btnSaveThanks = (Button) findViewById(R.id.btnSaveThanks);
        btnSaveInfo = (Button) findViewById(R.id.btnSaveInfo);
        btnSavePromo = (Button) findViewById(R.id.btnSavePromo);

        TextInputLayout layoutPromo = (TextInputLayout) findViewById(R.id.layoutPromo);
        LinearLayoutCompat layoutBtnPromo = (LinearLayoutCompat) findViewById(R.id.layoutBtnPromo);
        layoutPromo.setVisibility(View.GONE);
        layoutBtnPromo.setVisibility(View.GONE);

        btnBack = (Button) findViewById(R.id.btnBack);

    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        initPesan();
    }

    private void initListeners(){

        textThanks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        textInformasi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        textPromosi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        btnServerThanks.setOnClickListener(this);
        btnServerInfo.setOnClickListener(this);
        btnServerPromo.setOnClickListener(this);

        btnSaveThanks.setOnClickListener(this);
        btnSaveInfo.setOnClickListener(this);
        btnSavePromo.setOnClickListener(this);

        btnBack.setOnClickListener(this);
    }

    private void serverThanks(){
        class ServerThanks extends AsyncTask<Void, Void, Integer>{
            ProgressDialog pd = new ProgressDialog(SendSettingActivity.this);

            @Override
            protected Integer doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"pesan_thanks", params);
                int result = 0;

                try {
                    JSONObject obj = new JSONObject(res);
                    boolean status = obj.getBoolean("status");
                    if(status){
                        Pesan pesan = new Pesan();
                        pesan.setId("1");
                        pesan.setThanks(obj.getString("pesan"));
                        result = dbVitone.updatePesanThanks(pesan);
                    }else{
                        result = 0;
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(SendSettingActivity.this,"Pesan dari Server...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(Integer result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(SendSettingActivity.this);
                if(result == 0){
                    alert.setMessage("Mengambil Pesan dari Server Gagal!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pesan Server")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Mengambil Pesan dari Server Berhasil!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pesan Server")
                            .create();
                    alert.show();
                }
                initPesan();
            }
        }
        ServerThanks st = new ServerThanks();
        st.execute();
    }

    private void serverInfo(){
        class ServerInfo extends AsyncTask<Void, Void, Integer>{
            ProgressDialog pd = new ProgressDialog(SendSettingActivity.this);

            @Override
            protected Integer doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"pesan_info", params);
                int result = 0;

                try {
                    JSONObject obj = new JSONObject(res);
                    boolean status = obj.getBoolean("status");
                    if(status){
                        Pesan pesan = new Pesan();
                        pesan.setId("1");
                        pesan.setInfo(obj.getString("pesan"));
                        result = dbVitone.updatePesanInfo(pesan);
                    }else{
                        result = 0;
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(SendSettingActivity.this,"Pesan dari Server...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(Integer result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(SendSettingActivity.this);
                if(result == 0){
                    alert.setMessage("Mengambil Pesan dari Server Gagal!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pesan Server")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Mengambil Pesan dari Server Berhasil!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pesan Server")
                            .create();
                    alert.show();
                }
                initPesan();
            }
        }
        ServerInfo si = new ServerInfo();
        si.execute();
    }

    private void serverPromo(){
        class ServerPromo extends AsyncTask<Void, Void, Integer>{
            ProgressDialog pd = new ProgressDialog(SendSettingActivity.this);

            @Override
            protected Integer doInBackground(Void... voids) {
                HashMap<String,String> params = new HashMap<>();

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"pesan_promo", params);
                int result = 0;

                try {
                    JSONObject obj = new JSONObject(res);
                    boolean status = obj.getBoolean("status");
                    if(status){
                        Pesan pesan = new Pesan();
                        pesan.setId("1");
                        pesan.setPromo(obj.getString("pesan"));
                        result = dbVitone.updatePesanPromo(pesan);
                    }else{
                        result = 0;
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(SendSettingActivity.this,"Pesan dari Server...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(Integer result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(SendSettingActivity.this);
                if(result == 0){
                    alert.setMessage("Mengambil Pesan dari Server Gagal!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pesan Server")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Mengambil Pesan dari Server Berhasil!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Pesan Server")
                            .create();
                    alert.show();
                }
                initPesan();
            }
        }
        ServerPromo sp = new ServerPromo();
        sp.execute();
    }

    private void saveThanks(){
        final String thanks = textThanks.getText().toString().trim();

        class SaveThanks extends AsyncTask<Void, Void, Integer> {

            ProgressDialog pd = new ProgressDialog(SendSettingActivity.this);

            @Override
            protected Integer doInBackground(Void... voids) {
                Pesan pesan = new Pesan();
                pesan.setId("1");
                pesan.setThanks(thanks);

                int result = dbVitone.updatePesanThanks(pesan);
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(SendSettingActivity.this, "Simpan Pesan...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(Integer result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(SendSettingActivity.this);
                if(result == 0){
                    alert.setMessage("Menyimpan Pesan Gagal!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Simpan Pesan")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Menyimpan Pesan Berhasil!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Simpan Pesan")
                            .create();
                    alert.show();
                }
            }
        }
        SaveThanks st = new SaveThanks();
        st.execute();
    }

    private void saveInfo(){
        final String info = textInformasi.getText().toString().trim();

        class SaveInfo extends AsyncTask<Void, Void, Integer> {

            ProgressDialog pd = new ProgressDialog(SendSettingActivity.this);

            @Override
            protected Integer doInBackground(Void... voids) {
                Pesan pesan = new Pesan();
                pesan.setId("1");
                pesan.setInfo(info);

                int result = dbVitone.updatePesanInfo(pesan);
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(SendSettingActivity.this, "Simpan Pesan...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(Integer result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(SendSettingActivity.this);
                if(result == 0){
                    alert.setMessage("Menyimpan Pesan Gagal!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Simpan Pesan")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Menyimpan Pesan Berhasil!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Simpan Pesan")
                            .create();
                    alert.show();
                }
            }
        }
        SaveInfo si = new SaveInfo();
        si.execute();
    }

    private void savePromo(){
        final String promo = textPromosi.getText().toString().trim();

        class SavePromo extends AsyncTask<Void, Void, Integer> {

            ProgressDialog pd = new ProgressDialog(SendSettingActivity.this);

            @Override
            protected Integer doInBackground(Void... voids) {
                Pesan pesan = new Pesan();
                pesan.setId("1");
                pesan.setPromo(promo);

                int result = dbVitone.updatePesanPromo(pesan);
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(SendSettingActivity.this, "Simpan Pesan...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(Integer result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(SendSettingActivity.this);
                if(result == 0){
                    alert.setMessage("Menyimpan Pesan Gagal!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Simpan Pesan")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Menyimpan Pesan Berhasil!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Simpan Pesan")
                            .create();
                    alert.show();
                }
            }
        }
        SavePromo sp = new SavePromo();
        sp.execute();
    }

    private void initPesan(){
        Pesan pesan = dbVitone.getPesan("1");

        textThanks.setText(pesan.getThanks());
        textInformasi.setText(pesan.getInfo());
        textPromosi.setText(pesan.getPromo());
    }
}
