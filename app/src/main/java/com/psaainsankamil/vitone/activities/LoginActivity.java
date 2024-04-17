package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zaki on 24/05/18.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textUsername;
    private EditText textPassword;

    private Button btnLogin;
    private TextView textRegister;

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initViews();
        initObjects();
        initListeners();
        if(member.getSPCheck()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.textRegister:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
        }
    }

    private void initViews(){
        textUsername = (EditText) findViewById(R.id.textUsername);
        textPassword = (EditText) findViewById(R.id.textPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        textRegister = (TextView) findViewById(R.id.textRegister);
    }

    private void initObjects(){
        member = new Member(this);
    }

    private void initListeners(){
        btnLogin.setOnClickListener(this);
        textRegister.setOnClickListener(this);
    }

    private void login(){

        final String username = textUsername.getText().toString().trim();
        final String password = textPassword.getText().toString().trim();

        class Login extends AsyncTask<Void, Void, Object[]> {

            ProgressDialog pd = new ProgressDialog(LoginActivity.this);

            @Override
            protected Object[] doInBackground(Void...v){
                HashMap<String,String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"login", params);
                Object[] object = new Object[2];
                object[0] = "FAILED";
                object[1] = "wrong";
                try {
                    JSONObject obj = new JSONObject(res);
                    boolean status = obj.getBoolean("status");
                    if(status){
                        member.saveSPString(member.SP_USERNAME, obj.getString("username"));
                        member.saveSPString(member.SP_PASSWORD, obj.getString("password"));
                        member.saveSPString(member.SP_EMAIL, obj.getString("email"));
                        member.saveSPString(member.SP_NAMA, obj.getString("nama"));
                        member.saveSPString(member.SP_PHOTO, obj.getString("photo"));
                        member.saveSPString(member.SP_CATEGORY, obj.getString("category"));
                        member.saveSPString(member.SP_KEY, obj.getString("member_key"));
                        member.saveSPBoolean(member.SP_CHECK, status);
                        object[0] = "SUCCESS";
                        object[1] = "verify";
                    }else{
                        if(obj.getBoolean("check_verify")) {
                            member.saveSPString(member.SP_USERNAME, obj.getString("username"));
                            member.saveSPString(member.SP_EMAIL, obj.getString("email"));
                            member.saveSPString(member.SP_NAMA, obj.getString("nama"));
                            member.saveSPString(member.SP_KEY, obj.getString("member_key"));
                        }
                        object[0] = "WRONG";
                        object[1] = obj.getString("verify");
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

                return object;
            }

            @Override
            protected void onPreExecute(){
                pd = ProgressDialog.show(LoginActivity.this, "Login...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(Object[] result){
                pd.dismiss();
                if(result[0].equals("SUCCESS")){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }else if(result[0].equals("WRONG")){
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    if(result[1].equals("no")){
                        alert.setMessage("Akun anda belum diverifikasi!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setNeutralButton("Kirim kode", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        class SendCode extends AsyncTask<Void, Void, Boolean> {

                                            ProgressDialog pdsc = new ProgressDialog(LoginActivity.this);
                                            @Override
                                            protected Boolean doInBackground(Void... voids) {
                                                HashMap<String,String> params = new HashMap<>();
                                                params.put("username", member.getSPUsername());
                                                params.put("email", member.getSPEmail());
                                                params.put("nama", member.getSPNama());
                                                params.put("member_key", member.getSPKey());

                                                RequestHandler rh = new RequestHandler();
                                                String res = rh.sendPostRequest(Configuration.URL+"email_send_again", params);
                                                boolean result = false;
                                                try {
                                                    JSONObject obj = new JSONObject(res);
                                                    boolean status = obj.getBoolean("status");
                                                    result = status;
                                                }catch(JSONException e){
                                                    e.printStackTrace();
                                                }

                                                return result;
                                            }

                                            @Override
                                            protected void onPreExecute() {
                                                pdsc = ProgressDialog.show(LoginActivity.this,
                                                        "Mengirim kode...", "Tunggu Sebentar...");
                                            }

                                            @Override
                                            protected void onPostExecute(Boolean objects) {
                                                pdsc.dismiss();
                                                if(objects) {
                                                    AlertDialog.Builder alertsc = new AlertDialog.Builder(LoginActivity.this);
                                                    alertsc.setMessage("Kode berhasil dikirim ke email anda!")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                }
                                                            })
                                                            .setTitle("Pengiriman kode")
                                                            .create();
                                                    alertsc.show();
                                                }else{
                                                    AlertDialog.Builder alertsc = new AlertDialog.Builder(LoginActivity.this);
                                                    alertsc.setMessage("Kode gagal dikirim ke email anda!")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                }
                                                            })
                                                            .setTitle("Pengiriman kode")
                                                            .create();
                                                    alertsc.show();
                                                }
                                            }
                                        }
                                        SendCode sc = new SendCode();
                                        sc.execute();
                                    }
                                })
                                .create();
                        alert.show();
                    }else{
                        alert.setMessage("Username atau Password salah!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .create();
                        alert.show();
                    }

                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setMessage("Koneksi sedang bermasalah!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .create();
                    alert.show();
                }

            }

        }
        Login l = new Login();
        l.execute();
    }


}
