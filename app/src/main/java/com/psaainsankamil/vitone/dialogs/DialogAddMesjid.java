package com.psaainsankamil.vitone.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.activities.MesjidMapActivity;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.libraries.Configuration;
import com.psaainsankamil.vitone.libraries.RequestHandler;
import com.psaainsankamil.vitone.models.Lokasi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zaki on 30/05/18.
 */

public class DialogAddMesjid extends Dialog implements View.OnClickListener{

    private MesjidMapActivity activity;
    private DatabaseVitone dbVitone;
    private Location location;

    private EditText textNama;
    private EditText textAlamat;

    private Button btn_yes;
    private Button btn_no;

    private Lokasi lokasia;

    public DialogAddMesjid(MesjidMapActivity activity, Location location){
        super(activity);
        this.activity = activity;
        this.location = location;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_mesjid);
        getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        getWindow().setTitle("Tambah Mesjid");
        getWindow().setTitleColor(Color.WHITE);
        initViews();
        initObjects();
        initListeners();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_yes:
                add();
                break;
            case R.id.btn_no:
                dismiss();
                break;
        }
    }

    private void initViews(){
        textNama = (EditText) findViewById(R.id.textNama);
        textAlamat = (EditText) findViewById(R.id.textAlamat);
        btn_yes = (Button) findViewById(R.id.btn_yes);
        btn_no = (Button) findViewById(R.id.btn_no);
    }

    private void initObjects(){
        dbVitone = new DatabaseVitone(activity);
    }

    private void initListeners(){
        btn_yes.setOnClickListener(this);
        btn_no.setOnClickListener(this);
    }

    private void add(){
        class Add extends AsyncTask<Void, Void, String>{
            ProgressDialog pd = new ProgressDialog(activity);
            @Override
            protected String doInBackground(Void... voids) {
                HashMap<String, String> params = new HashMap<>();
                params.put("nama", textNama.getText().toString().trim());
                params.put("alamat", textAlamat.getText().toString().trim());
                params.put("contact", "contact");
                params.put("petugas", "petugas");
                params.put("lat", String.valueOf(location.getLatitude()));
                params.put("lon", String.valueOf(location.getLongitude()));
                params.put("kategori", "mesjid");

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(Configuration.URL+"add_mesjid", params);
                boolean result = false;
                try{
                    JSONObject obj = new JSONObject(res);
                    result = obj.getBoolean("status");
                }catch (JSONException er){
                    er.printStackTrace();
                }
                if(result){
                    params = new HashMap<>();
                    res = rh.sendPostRequest(Configuration.URL+"all_mesjid", params);
                }
                return res;
            }

            @Override
            protected void onPreExecute() {
                //super.onPreExecute();
                pd = ProgressDialog.show(activity, "Tambah Mesjid...", "tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(String res) {
                pd.dismiss();
                boolean result = false;
                try{
                    JSONObject obj = new JSONObject(res);
                    result = obj.getBoolean("status");
                    if(result){
                        JSONArray array = obj.getJSONArray("mesjids");
                        List<Lokasi> listLokasi = new ArrayList<>();
                        for(int i=0; i<array.length(); i++){
                            JSONObject objA = new JSONObject(array.get(i).toString());
                            Lokasi lokasi = new Lokasi();
                            lokasi.setId(objA.getString("id"));
                            lokasi.setNama(objA.getString("nama"));
                            lokasi.setAlamat(objA.getString("alamat"));
                            lokasi.setLat(objA.getString("lat"));
                            lokasi.setLon(objA.getString("lon"));
                            lokasi.setKategori(objA.getString("kategori"));
                            listLokasi.add(lokasi);
                        }
                        lokasia = listLokasi.get(listLokasi.size()-1);
                        dbVitone.updateMesjid(listLokasi);
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                if(result){
                    alert.setMessage("Tambah Mesjid Berhasil! :)")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    lokasia.setMarker();
                                    activity.done(lokasia);
                                    dismiss();
                                }
                            })
                            .setTitle("Tambah Mesjid")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Tambah Mesjid Gagal! :(")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    dismiss();
                                }
                            })
                            .setTitle("Tambah Mesjid")
                            .create();
                    alert.show();
                }
            }
        }
        Add a = new Add();
        a.execute();
    }
}
