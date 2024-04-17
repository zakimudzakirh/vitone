package com.psaainsankamil.vitone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.dialogs.DialogSetting;
import com.psaainsankamil.vitone.session.Member;

/**
 * Created by zaki on 28/05/18.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textNama;
    private TextView textUsername;
    private TextView textEmail;
    private TextView textPassword;

    private Button btnEdit;
    private Button btnBack;

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Setting Akun");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(SettingActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnEdit:
                editMember();
                break;
            case R.id.btnBack:
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        textNama = (TextView) findViewById(R.id.textNama);
        textUsername = (TextView) findViewById(R.id.textUsername);
        textEmail = (TextView) findViewById(R.id.textEmail);
        textPassword = (TextView) findViewById(R.id.textPassword);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);

        textNama.setText(member.getSPNama());
        textUsername.setText(member.getSPUsername());
        textEmail.setText(member.getSPEmail());
        textPassword.setText(member.getSPPassword());
    }

    private void initListeners(){
        btnEdit.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void editMember(){
        DialogSetting dialog = new DialogSetting(this);
        dialog.show();
    }

    public Member getMember(){
        return member;
    }

    public void done(String pass){
        Intent intentEdit = new Intent(SettingActivity.this, SettingEditActivity.class);
        intentEdit.putExtra("password", pass);
        startActivity(intentEdit);
    }
}
