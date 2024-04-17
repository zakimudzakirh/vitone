package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.models.Donatur;
import com.psaainsankamil.vitone.session.Member;

/**
 * Created by zaki on 27/05/18.
 */

public class DonaturAddActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textNama;
    private EditText textKontak;

    private Button btnAdd;
    private Button btnBack;

    private Member member;
    private DatabaseVitone dbVitone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donatur_add);
        getSupportActionBar().setTitle("Tambah Donatur Manual");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(DonaturAddActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                addDonatur();
                break;
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

    private void initViews(){
        textNama = (EditText) findViewById(R.id.textNama);
        textKontak = (EditText) findViewById(R.id.textKontak);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        this.member = new Member(this);
        this.dbVitone = new DatabaseVitone(this);
    }

    private void initListeners(){
        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void addDonatur(){

        final String nama = textNama.getText().toString().trim();
        final String kontak = textKontak.getText().toString().trim();

        class AddDonatur extends AsyncTask<Void, Void, Long>{

            ProgressDialog pd = new ProgressDialog(DonaturAddActivity.this);

            @Override
            protected Long doInBackground(Void... voids) {
                Donatur donatur = new Donatur();
                donatur.setNama(nama);
                donatur.setKontak(kontak);
                long result = dbVitone.addDonaturManual(donatur);
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(DonaturAddActivity.this,"Tambah Donatur...","tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(Long result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(DonaturAddActivity.this);
                if(result <= 0){
                    alert.setMessage("Data Donatur sudah ada!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Donatur")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Menambahkan donatur berhasil!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Donatur")
                            .create();
                    alert.show();
                }

            }
        }

        AddDonatur ad = new AddDonatur();
        ad.execute();
    }
}
