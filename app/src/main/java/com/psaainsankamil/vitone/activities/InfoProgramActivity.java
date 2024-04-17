package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.adapters.ProgramAdapter;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.models.Program;
import com.psaainsankamil.vitone.session.Member;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 27/05/18.
 */

public class InfoProgramActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;

    private Button btnUpdate;
    private Button btnBack;

    private Member member;
    private DatabaseVitone dbVitone;

    private List<Program> listProgram;
    private ProgramAdapter programAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_program);
        getSupportActionBar().setTitle("Info Program");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(InfoProgramActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnUpdate:
                update();
                break;
            case R.id.btnBack:
                startActivity(new Intent(this, InfoMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, InfoMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);

        listProgram = dbVitone.getListProgram();
        updateInfo(listProgram);
    }

    private void initListeners(){
        btnUpdate.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void update(){
        class Update extends AsyncTask<Void, Void, Integer>{
            ProgressDialog pd = new ProgressDialog(InfoProgramActivity.this);

            @Override
            protected Integer doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"info_program", params);
                int result = 0;
                try{
                    JSONObject obj = new JSONObject(res);
                    if(obj.getBoolean("status")){
                        JSONArray array = obj.getJSONArray("result");
                        List<Program> programs = new ArrayList<>();
                        for(int i=0; i<array.length(); i++){
                            JSONObject objA = new JSONObject(array.get(i).toString());
                            Program program = new Program();
                            program.setId(objA.getString("id"));
                            program.setJudul(objA.getString("judul"));
                            program.setIsi(objA.getString("isi"));
                            programs.add(program);
                        }
                        result = dbVitone.updateProgram(programs);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(InfoProgramActivity.this, "Update Info...", "tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(Integer s) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(InfoProgramActivity.this);
                if(s == 0){
                    alert.setMessage("Update Info Program Gagal!")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Update Info")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Update Info Program Berhasil!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Update Info")
                            .create();
                    alert.show();
                }
                listProgram = dbVitone.getListProgram();
                updateInfo(listProgram);
            }
        }
        Update u = new Update();
        u.execute();
    }

    private void updateInfo(List<Program> listProgram){
        programAdapter = new ProgramAdapter(listProgram, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(programAdapter);
        programAdapter.notifyDataSetChanged();
    }

}
