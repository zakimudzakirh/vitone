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
 * Created by zaki on 27/05/18.
 */

public class InfoMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnInfoProfil;
    private Button btnInfoAnak;
    private Button btnInfoProgram;
    private Button btnInfoRek;
    private Button btnBack;

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_menu);
        getSupportActionBar().setTitle("Info PSAA Insan Kamil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(InfoMenuActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnInfoProfil:
                Intent intentProfil = new Intent(InfoMenuActivity.this, InfoProfilActivity.class);
                startActivity(intentProfil);
                break;
            case R.id.btnInfoAnak:
                Intent intentAnak = new Intent(InfoMenuActivity.this, InfoAnakActivity.class);
                startActivity(intentAnak);
                break;
            case R.id.btnInfoProgram:
                Intent intentProgram = new Intent(InfoMenuActivity.this, InfoProgramActivity.class);
                startActivity(intentProgram);
                break;
            case R.id.btnInfoRek:
                Intent intentRek = new Intent(InfoMenuActivity.this, InfoRekActivity.class);
                startActivity(intentRek);
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
        btnInfoProfil = (Button) findViewById(R.id.btnInfoProfil);
        btnInfoAnak = (Button) findViewById(R.id.btnInfoAnak);
        btnInfoProgram = (Button) findViewById(R.id.btnInfoProgram);
        btnInfoRek = (Button) findViewById(R.id.btnInfoRek);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
    }

    private void initListeners(){
        btnInfoProfil.setOnClickListener(this);
        btnInfoAnak.setOnClickListener(this);
        btnInfoProgram.setOnClickListener(this);
        btnInfoRek.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

}
