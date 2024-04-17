package com.psaainsankamil.vitone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.session.Member;

/**
 * Created by zaki on 26/05/18.
 */

public class SendMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSend;
    private Button btnSendContact;
    private Button btnSetting;
    private Button btnBack;

    private Member member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_menu);
        getSupportActionBar().setTitle("Pesan SMS Center");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(SendMenuActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSend:
                Intent intentSend = new Intent(SendMenuActivity.this, SendMessageActivity.class);
                startActivity(intentSend);
                break;
            case R.id.btnSendContact:
                Intent intentSendContact = new Intent(SendMenuActivity.this, SendMessageContactActivity.class);
                startActivity(intentSendContact);
                break;
            case R.id.btnSetting:
                Intent intentSetting = new Intent(SendMenuActivity.this, SendSettingActivity.class);
                startActivity(intentSetting);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSendContact = (Button) findViewById(R.id.btnSendContact);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
    }

    private void initListeners(){
        btnSend.setOnClickListener(this);
        btnSendContact.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }
}
