package com.psaainsankamil.vitone.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.psaainsankamil.vitone.R;
import com.psaainsankamil.vitone.adapters.ContactAdapter;
import com.psaainsankamil.vitone.database.DatabaseVitone;
import com.psaainsankamil.vitone.models.Donatur;
import com.psaainsankamil.vitone.session.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaki on 27/05/18.
 */

public class DonaturAddContactActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;

    private Button btnAdd;
    private Button btnBack;

    private Member member;
    private DatabaseVitone dbVitone;
    private ContactAdapter adapter;
    private List<Donatur> listDonatur;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donatur_add_contact);
        getSupportActionBar().setTitle("Donatur Dari Kontak");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        initObjects();
        initListeners();
        if(!member.getSPCheck()){
            startActivity(new Intent(DonaturAddContactActivity.this, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnAdd:
                addContact();
                break;
            case R.id.btnBack:
                startActivity(new Intent(this, DonaturMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this, DonaturMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem menuItem = menu.findItem(R.id.search);
        searchView = (SearchView) menuItem.getActionView();
        changeSearchViewTextColor(searchView);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text))
                .setHintTextColor(getResources().getColor(android.R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                menuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Donatur> filterDonatur = filter(listDonatur, newText);
                adapter.setFilter(filterDonatur);
                return true;
            }
        });
        return true;
    }

    private List<Donatur> filter(List<Donatur> list, String query){
        query = query.toLowerCase();
        final List<Donatur> filter = new ArrayList<>();

        for(Donatur donatur:list){
            final String text = donatur.getNama().toLowerCase();
            if(text.contains(query)){
                filter.add(donatur);
            }
        }
        return filter;
    }

    private void changeSearchViewTextColor(View view){
        if(view != null){
            if(view instanceof TextView){
                ((TextView) view).setTextColor(Color.WHITE);
                return;
            }else if(view instanceof ViewGroup){
                ViewGroup viewGroup = (ViewGroup) view;
                for(int i=0; i<viewGroup.getChildCount(); i++){
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    private void initViews(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnBack = (Button) findViewById(R.id.btnBack);
    }

    private void initObjects(){
        member = new Member(this);
        dbVitone = new DatabaseVitone(this);
        listDonatur = new ArrayList<>();
        getContactList();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ContactAdapter(listDonatur, this);
        recyclerView.setAdapter(adapter);

    }

    private void initListeners(){
        btnAdd.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    private void getContactList() {
        class ContactList extends AsyncTask<Void, Void, String>{

            ProgressDialog pd = new ProgressDialog(DonaturAddContactActivity.this);

            @Override
            protected String doInBackground(Void... voids) {
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                if ((cur != null ? cur.getCount() : 0) > 0) {
                    while (cur != null && cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        //String photo = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));

                        if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                if(!dbVitone.checkDonatur(phoneNo)){
                                    listDonatur.add(new Donatur(phoneNo, name, phoneNo, false));
                                }
                            }
                            pCur.close();
                        }
                    }
                }
                if(cur!=null){
                    cur.close();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(DonaturAddContactActivity.this, "Cek Data", "tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(String s) {
                pd.dismiss();
            }
        }
        ContactList cl = new ContactList();
        cl.execute();
    }

    private void addContact(){
        class AddContact extends AsyncTask<Void, Void, Long>{

            ProgressDialog pd = new ProgressDialog(DonaturAddContactActivity.this);

            @Override
            protected Long doInBackground(Void... voids) {
                long result = dbVitone.addDonaturContact(listDonatur);
                return result;
            }

            @Override
            protected void onPreExecute() {
                pd = ProgressDialog.show(DonaturAddContactActivity.this, "Tambah Donatur", "tunggu sebentar...");
            }

            @Override
            protected void onPostExecute(Long result) {
                pd.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(DonaturAddContactActivity.this);
                if(result <= 0){
                    alert.setMessage("Menambah Donatur Gagal! :(")
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .setTitle("Tambah Donatur")
                            .create();
                    alert.show();
                }else{
                    alert.setMessage("Menambah Donatur Berhasil! :)")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    startActivity(new Intent(DonaturAddContactActivity.this,
                                            DonaturMenuActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                }
                            })
                            .setTitle("Tambah Donatur")
                            .create();
                    alert.show();
                }
            }
        }
        AddContact ac = new AddContact();
        ac.execute();
    }
}
