package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.helpers.InputRegisterValidation;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zaki on 28/05/18.
 */

public class SettingEditActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textLayoutNama;
    private TextInputLayout textLayoutUsername;
    private TextInputLayout textLayoutEmail;
    private TextInputLayout textLayoutPassword;
    private TextInputLayout textLayoutCPassword;

    private TextInputEditText textNama;
    private TextInputEditText textUsername;
    private TextInputEditText textEmail;
    private TextInputEditText textPassword;
    private TextInputEditText textCPassword;

    private Button btnEdit;
    private Button btnBack;

    private InputRegisterValidation inputValidation;
    private Member member;

    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_edit);
        getSupportActionBar().setTitle("Edit Akun");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pass = getIntent().getStringExtra("password");
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(SettingEditActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnEdit:
                edit();
                break;
            case R.id.btnBack:
                startActivity(new Intent(this, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        textLayoutNama = (TextInputLayout) findViewById(R.id.textLayoutNama);
        textLayoutUsername = (TextInputLayout) findViewById(R.id.textLayoutUsername);
        textLayoutEmail = (TextInputLayout) findViewById(R.id.textLayoutEmail);
        textLayoutPassword = (TextInputLayout) findViewById(R.id.textLayoutPassword);
        textLayoutCPassword = (TextInputLayout) findViewById(R.id.textLayoutCPassword);

        textNama = (TextInputEditText) findViewById(R.id.textNama);
        textUsername = (TextInputEditText) findViewById(R.id.textUsername);
        textEmail = (TextInputEditText) findViewById(R.id.textEmail);
        textPassword = (TextInputEditText) findViewById(R.id.textPassword);
        textCPassword = (TextInputEditText) findViewById(R.id.textCPassword);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
        inputValidation = new InputRegisterValidation(this);

        textNama.setText(member.getSPNama());
        textUsername.setText(member.getSPUsername());
        textEmail.setText(member.getSPEmail());
        textPassword.setText(pass);
    }

    private void initListeners(){
        btnEdit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void edit(){
        if (!inputValidation.isInputEditTextFilled(textNama, textLayoutNama, "Harus diisi!")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textUsername, textLayoutNama, "Harus diisi!")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textEmail, textLayoutEmail, "Harus diisi!")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textPassword, textLayoutPassword, "Harus diisi!")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textCPassword, textLayoutCPassword, "Harus diisi!")) {
            return;
        }
        if (!inputValidation.isInputEditTextSmall(textUsername, textLayoutUsername, "Panjang karakter min 8!")) {
            return;
        }
        if (!inputValidation.isInputEditTextBig(textUsername, textLayoutUsername, "Panjang karakter max 20!")) {
            return;
        }
        if (!inputValidation.isInputEditTextSmall(textPassword, textLayoutPassword, "Panjang karakter min 8!")) {
            return;
        }
        if (!inputValidation.isInputEditTextBig(textPassword, textLayoutPassword, "Panjang karakter max 20!")) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textEmail, textLayoutEmail, "Email tidak valid!")) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textPassword, textCPassword,
                textLayoutCPassword, "Confirm Password tidak cocok!")) {
            return;
        }
        final String nama = textNama.getText().toString().trim();
        final String username = textUsername.getText().toString().trim();
        final String email = textEmail.getText().toString().trim();
        final String password = textPassword.getText().toString().trim();

        class Edit extends AsyncTask<Void, Void, String> {

            ProgressDialog pd = new ProgressDialog(SettingEditActivity.this);

            @Override
            protected String doInBackground(Void...v){
                HashMap<String,String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("c_username", member.getSPUsername());
                params.put("username", username);
                params.put("c_email", member.getSPEmail());
                params.put("email", email);
                params.put("password", password);
                params.put("photo", "default.png");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"edit_member", params);

                return res;
            }

            @Override
            protected void onPreExecute(){
                pd = ProgressDialog.show(SettingEditActivity.this, "Edit Akun...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(String result){
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingEditActivity.this);
                String res = "Koneksi sedang bermasalah!";
                try {
                    JSONObject obj = new JSONObject(result);
                    boolean status = obj.getBoolean("status");
                    if(status){
                        alert.setMessage("Edit Berhasil!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        member.saveSPString(member.SP_NAMA, nama);
                                        member.saveSPString(member.SP_USERNAME, username);
                                        member.saveSPString(member.SP_EMAIL, email);
                                        member.saveSPString(member.SP_PASSWORD, getPassword(password));
                                        goBack();
                                    }
                                })
                                .setTitle("Edit Akun")
                                .create();
                        alert.show();
                    }else{
                        String type = obj.getString("type");
                        System.out.println("type = "+type);
                        if(type.equals("username")){
                            alert.setMessage("Edit Gagal! Username sudah ada!")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setTitle("Edit Akun")
                                    .create();
                            alert.show();
                        }else if(type.equals("email")){
                            alert.setMessage("Edit Gagal! Email sudah ada!")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setTitle("Edit Akun")
                                    .create();
                            alert.show();
                        }else{
                            alert.setMessage("Edit Gagal!")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setTitle("Edit Akun")
                                    .create();
                            alert.show();
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                    alert.setMessage(res)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Edit Akun")
                            .create();
                    alert.show();
                }
            }

            private String getPassword(String password){
                String result = "";
                for(int i=0; i<password.length(); i++){
                    result += "*";
                }
                return result;
            }
        }
        Edit e = new Edit();
        e.execute();
    }

    private void goBack(){
        startActivity(new Intent(this, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
