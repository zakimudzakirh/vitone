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

public class DonaturMenuActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnAdd;
    private Button btnAddContact;
    private Button btnView;
    private Button btnBack;

    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donatur_menu);
        getSupportActionBar().setTitle("Donatur");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(DonaturMenuActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                Intent intentAdd = new Intent(DonaturMenuActivity.this, DonaturAddActivity.class);
                startActivity(intentAdd);
                break;
            case R.id.btnAddContact:
                Intent intentAddContact = new Intent(DonaturMenuActivity.this, DonaturAddContactActivity.class);
                startActivity(intentAddContact);
                break;
            case R.id.btnView:
                Intent intentView = new Intent(DonaturMenuActivity.this, DonaturViewActivity.class);
                startActivity(intentView);
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
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnView = (Button) findViewById(R.id.btnView);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
    }

    private void initListeners(){
        btnAdd.setOnClickListener(this);
        btnAddContact.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

}
