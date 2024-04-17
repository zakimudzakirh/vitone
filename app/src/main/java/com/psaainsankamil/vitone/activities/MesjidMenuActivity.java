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
 * Created by zaki on 28/05/18.
 */

public class MesjidMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnMap;
    private Button btnData;
    private Button btnBack;

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesjid_menu);
        getSupportActionBar().setTitle("Mesjid Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(MesjidMenuActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnMap:
                Intent intentMap = new Intent(MesjidMenuActivity.this, MesjidMapActivity.class);
                startActivity(intentMap);
                break;
            case R.id.btnData:
                Intent intentData = new Intent(MesjidMenuActivity.this, MesjidDataActivity.class);
                startActivity(intentData);
                break;
            case R.id.btnBack:
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        btnMap = (Button) findViewById(R.id.btnMap);
        btnData = (Button) findViewById(R.id.btnData);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
    }

    private void initListeners(){
        btnMap.setOnClickListener(this);
        btnData.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }
}
