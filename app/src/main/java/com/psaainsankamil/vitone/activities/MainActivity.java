package com.psaainsankamil.vitone.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.session.Member;

/**
 * Created by zaki on 24/05/18.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSend;
    private Button btnInfo;
    private Button btnMosque;
    private Button btnDonatur;
    private Button btnSetting;
    private Button btnLogout;

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(MainActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnSend:
                Intent intentSend = new Intent(MainActivity.this, SendMenuActivity.class);
                startActivity(intentSend);
                break;
            case R.id.btnInfo:
                Intent intentInfo = new Intent(MainActivity.this, InfoMenuActivity.class);
                startActivity(intentInfo);
                break;
            case R.id.btnMosque:
                Intent intentMesjid = new Intent(MainActivity.this, MesjidMenuActivity.class);
                startActivity(intentMesjid);
                break;
            case R.id.btnDonatur:
                Intent intentDonatur = new Intent(MainActivity.this, DonaturMenuActivity.class);
                startActivity(intentDonatur);
                break;
            case R.id.btnSetting:
                Intent intentSetting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intentSetting);
                break;
            case R.id.btnLogout:
                member.saveSPString(Member.SP_USERNAME, "");
                member.saveSPString(member.SP_EMAIL, "");
                member.saveSPString(member.SP_NAMA, "");
                member.saveSPString(member.SP_PASSWORD, "");
                member.saveSPString(member.SP_CATEGORY, "");
                member.saveSPString(member.SP_PHOTO, "");
                member.saveSPString(member.SP_KEY, "");
                member.saveSPBoolean(member.SP_CHECK, false);
                startActivity(new Intent(MainActivity.this, LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                break;
        }
    }

    private void initViews(){
        btnSend = (Button) findViewById(R.id.btnSend);
        btnInfo = (Button) findViewById(R.id.btnInfo);
        btnMosque = (Button) findViewById(R.id.btnMosque);
        btnDonatur = (Button) findViewById(R.id.btnDonatur);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnLogout = (Button) findViewById(R.id.btnLogout);
    }

    private void initObjects(){
        member = new Member(this);
    }

    private void initListeners(){
        btnSend.setOnClickListener(this);
        btnInfo.setOnClickListener(this);
        btnMosque.setOnClickListener(this);
        btnDonatur.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }
}
