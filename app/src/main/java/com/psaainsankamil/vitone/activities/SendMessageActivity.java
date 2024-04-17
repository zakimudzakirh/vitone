package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonWriter;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.models.Pesan;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 24/05/18.
 */

public class SendMessageActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText textNomor;
    private EditText textNama;
    private EditText textPesan;

    private RadioGroup radioGroup;

    private Button btnSend;
    private Button btnBack;
    private FloatingActionButton btnSelect;

    private Member member;
    private DatabaseVitone dbVitone;

    private Pesan pesan;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        getSupportActionBar().setTitle("Kirim Pesan SMS Center");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(SendMessageActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
        if(getIntent().getStringArrayExtra("donatur") != null){
            String[] array = getIntent().getStringArrayExtra("donatur");
            textNomor.setText(array[1]);
            textNama.setText(array[0]);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                //onBackPressed();
                startActivity(new Intent(this, SendMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnSend:
                send();
                break;
            case R.id.btnBack:
                startActivity(new Intent(this, SendMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.btnSelect:
                select();
                break;
        }
    }

    private void initViews(){
        textNomor = (EditText) findViewById(R.id.textNomor);
        textNama = (EditText) findViewById(R.id.textNama);
        textPesan = (EditText) findViewById(R.id.textPesan);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSelect = (FloatingActionButton) findViewById(R.id.btnSelect);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        pesan = dbVitone.getPesan("1");
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
        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnSelect.setOnClickListener(this);
    }

    private void send(){
        String pesan = textPesan.getText().toString().replace("<nama>",textNama.getText().toString().trim());

        class SendMessage extends AsyncTask<Void, Void, String>{

            ProgressDialog pd = new ProgressDialog(SendMessageActivity.this);

            String content;
            String msisdn;

            SendMessage(String content, String msisdn){
                this.content = content;
                this.msisdn = msisdn;
            }

            @Override
            protected String doInBackground(Void...v){
                JSONArray obj = new JSONArray();
                try {
                    //for(int i=0; i<2; i++){
                    JSONObject ob = new JSONObject();
                    ob.put(Configuration.KEY_SEND_NOMOR, msisdn);
                    ob.put(Configuration.KEY_SEND_PESAN, content);
                    obj.put(ob);
                    //}
                }catch (JSONException e){

                }
                HashMap<String,String> params = new HashMap<>();
                //params.put(Configuration.KEY_SEND_PESAN, content);
                //params.put(Configuration.KEY_SEND_NOMOR, msisdn);
                System.out.println(obj.toString());
                params.put("list", obj.toString());
                params.put(Configuration.KEY, member.getSPKey());
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"send", params);
                System.out.println("hasil adalah "+ res);
                return res;
            }

            @Override
            protected void onPreExecute(){
                pd = ProgressDialog.show(SendMessageActivity.this, "Mengirim...", "Tunggu Sebentar...");
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
                AlertDialog.Builder alert = new AlertDialog.Builder(SendMessageActivity.this);
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
        if(pesan.length() <= 160){
            pesan = pesan.replace("\r\n","%0A");
            pesan = pesan.replace("\n","%0A");
            final String content = pesan.trim();
            final String msisdn = textNomor.getText().toString().trim();
            SendMessage sm = new SendMessage(content, msisdn);
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
        }

    }

    private void select(){
        startActivity(new Intent(SendMessageActivity.this, SendSelectActivity.class));
    }
}

