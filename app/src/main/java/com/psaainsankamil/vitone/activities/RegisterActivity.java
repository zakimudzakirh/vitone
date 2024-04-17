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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.psaainsankamil.vitone.helpers.InputRegisterValidation;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zaki on 24/05/18.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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

    private Button btnRegister;
    private TextView textLogin;

    private InputRegisterValidation inputValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        initViews();
        initObjects();
        initListeners();
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnRegister:
                register();
                break;
            case R.id.textLogin:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
        }
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

        btnRegister = (Button) findViewById(R.id.btnRegister);
        textLogin = (TextView) findViewById(R.id.textLogin);
    }

    private void initObjects(){
        inputValidation = new InputRegisterValidation(this);
    }

    private void initListeners(){
        btnRegister.setOnClickListener(this);
        textLogin.setOnClickListener(this);
    }

    private void register(){
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
        if(!inputValidation.isInputEditTextChar(textUsername, textLayoutUsername, "Username tidak boleh ada spasi")){
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

        class Register extends AsyncTask<Void, Void, String>{

            ProgressDialog pd = new ProgressDialog(RegisterActivity.this);

            @Override
            protected String doInBackground(Void...v){
                HashMap<String,String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("photo", "default.png");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"register_member", params);
                String result = "Koneksi sedang bermasalah!";
                try {
                    JSONObject obj = new JSONObject(res);
                    boolean status = obj.getBoolean("status");
                    if(status){
                        result = "Registrasi Berhasil!\nSilahkan Cek Email untuk verifikasi!";
                    }else{
                        String type = obj.getString("type");
                        System.out.println("type = "+type);
                        if(type.equals("username")){
                            result = "Registrasi Gagal! Username sudah ada!";
                        }else if(type.equals("email")){
                            result = "Registrasi Gagal! Email sudah ada!";
                        }else{
                            result = "Registrasi Gagal!";
                        }
                    }
                }catch(JSONException e){
                    e.printStackTrace();
                }

                return result;
            }

            @Override
            protected void onPreExecute(){
                pd = ProgressDialog.show(RegisterActivity.this, "Registrasi...", "Tunggu Sebentar...");
            }

            @Override
            protected void onPostExecute(String result){
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                alert.setMessage(result)
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setTitle("Registrasi")
                        .create();
                alert.show();
            }
        }
        Register r = new Register();
        r.execute();
    }
}
