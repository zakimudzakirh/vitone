package com.psaainsankamil.vitone.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.activities.SettingActivity;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zaki on 28/05/18.
 */

public class DialogSetting extends Dialog implements View.OnClickListener {
    private EditText textInputConfirm;

    private Button btn_yes;
    private Button btn_no;

    private SettingActivity activity;

    public DialogSetting(SettingActivity activity){
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_setting);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        getWindow().setTitle("Setting Konfirmasi");
        getWindow().setTitleColor(Color.WHITE);
        initViews();
        initListeners();
        initObjects();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_yes:
                checkPassword();
                break;
            case R.id.btn_no:
                dismiss();
                break;
        }
    }

    private void initViews(){
        this.textInputConfirm = (EditText) findViewById(R.id.textInputConfirm);

        this.btn_yes = (Button) findViewById(R.id.btn_yes);
        this.btn_no = (Button) findViewById(R.id.btn_no);
    }

    private void initListeners(){
        this.btn_yes.setOnClickListener(this);
        this.btn_no.setOnClickListener(this);
    }

    private void initObjects(){

    }

    private void checkPassword(){
        final String password = textInputConfirm.getText().toString().trim();
        class Check extends AsyncTask<Void, Void, String>{
            ProgressDialog pd = new ProgressDialog(activity);
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put("username", activity.getMember().getSPUsername());
                params.put("password", password);
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"check_password", params);
                //int result = 0;
                return res;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(activity, "Check Password...", "tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(String result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                try{
                    JSONObject obj = new JSONObject(result);
                    if(obj.getBoolean("status")){
                        activity.done(textInputConfirm.getText().toString().trim());
                    }else{
                        alert.setMessage("Password tidak cocok!")
                                .setNeutralButton("OK", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .setTitle("Warning")
                                .create();
                        alert.show();
                    }
                }catch (JSONException e){
                    alert.setMessage("Server sedang bermasalah!")
                            .setNeutralButton("OK", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Warning")
                            .create();
                    alert.show();
                }
            }
        }
        Check c = new Check();
        c.execute();
    }
}
