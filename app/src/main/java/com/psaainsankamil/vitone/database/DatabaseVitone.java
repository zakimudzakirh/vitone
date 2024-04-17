package com.psaainsankamil.vitone.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.psaainsankamil.vitone.models.Anak;
import com.psaainsankamil.vitone.models.Donatur;
import com.psaainsankamil.vitone.models.InfoAnak;
import com.psaainsankamil.vitone.models.Lokasi;
import com.psaainsankamil.vitone.models.Pesan;
import com.psaainsankamil.vitone.models.Profil;
import com.psaainsankamil.vitone.models.Program;
import com.psaainsankamil.vitone.models.Rekening;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static com.psaainsankamil.vitone.database.PropertiesDB.*;

/**
 * Created by zaki on 26/05/18.
 */

public class DatabaseVitone extends SQLiteOpenHelper {

    //tables
    private static final String TABLE_DONATUR = "donatur";
    private static final String TABLE_PESAN  = "pesan";
    private static final String TABLE_PROFIL = "profil";
    private static final String TABLE_PROGRAM = "program";
    private static final String TABLE_ANAK = "anak";
    private static final String TABLE_INFOANAK = "info_anak";
    private static final String TABLE_LOKASI = "lokasi";
    private static final String TABLE_REK = "rekening";

    //table donatur
    private static final String COLUMN_DONATUR_ID = "id_donatur";
    private static final String COLUMN_DONATUR_NAMA = "nama";
    private static final String COLUMN_DONATUR_KONTAK = "kontak";

    //table pesan
    private static final String COLUMN_PESAN_ID = "id_pesan";
    private static final String COLUMN_PESAN_THANKS = "thanks";
    private static final String COLUMN_PESAN_INFO = "info";
    private static final String COLUMN_PESAN_PROMO = "promo";

    //table profil
    private static final String COLUMN_PROFIL_ID = "id_profil";
    private static final String COLUMN_PROFIL_EMAIL = "email";
    private static final String COLUMN_PROFIL_TELP = "telp";
    private static final String COLUMN_PROFIL_ALAMAT = "alamat";
    private static final String COLUMN_PROFIL_PROFIL = "profil";
    private static final String COLUMN_PROFIL_VISI = "visi";
    private static final String COLUMN_PROFIL_MISI = "misi";

    //table program
    private static final String COLUMN_PROGRAM_ID = "id_program";
    private static final String COLUMN_PROGRAM_JUDUL = "judul";
    private static final String COLUMN_PROGRAM_ISI = "isi";

    //table anak
    private static final String COLUMN_ANAK_ID = "id_anak";
    private static final String COLUMN_ANAK_NAMA = "nama";
    private static final String COLUMN_ANAK_INFOMUKIM = "info_mukim";
    private static final String COLUMN_ANAK_INFODHUAFA = "info_dhuafa";
    private static final String COLUMN_ANAK_DESKRIPSI = "deskripsi";

    //table info_anak
    private static final String COLUMN_INFOANAK_ID = "id_info_anak";
    private static final String COLUMN_INFOANAK_JMLH = "jmlh";
    private static final String COLUMN_INFOANAK_MUKIM = "mukim";
    private static final String COLUMN_INFOANAK_DHUAFA = "dhuafa";

    //table lokasi
    private static final String COLUMN_LOKASI_ID = "id_lokasi";
    private static final String COLUMN_LOKASI_NAMA = "nama";
    private static final String COLUMN_LOKASI_ALAMAT = "alamat";
    private static final String COLUMN_LOKASI_CONTACT = "contact";
    private static final String COLUMN_LOKASI_PETUGAS = "petugas";
    private static final String COLUMN_LOKASI_LAT = "lat";
    private static final String COLUMN_LOKASI_LON = "lon";
    private static final String COLUMN_LOKASI_KATEGORI = "kategori";

    //table rekening
    private static final String COLUMN_REK_ID = "id_rek";
    private static final String COLUMN_REK_NAMA = "nama";
    private static final String COLUMN_REK_BRI = "bri";
    private static final String COLUMN_REK_BNI = "bni";
    private static final String COLUMN_REK_BCA = "bca";
    private static final String COLUMN_REK_MANDIRI = "mandiri";
    private static final String COLUMN_REK_MUAMALAT = "muamalat";

    //create table donatur
    private String CREATE_TABLE_DONATUR = "CREATE TABLE IF NOT EXISTS " + TABLE_DONATUR + "("
            + COLUMN_DONATUR_ID + " VARCHAR(20) PRIMARY KEY," + COLUMN_DONATUR_NAMA + " TEXT,"
            + COLUMN_DONATUR_KONTAK + " VARCHAR(20) )";

    //create table pesan
    private String CREATE_TABLE_PESAN = "CREATE TABLE IF NOT EXISTS " + TABLE_PESAN + "("
            + COLUMN_PESAN_ID + " INT(1) PRIMARY KEY," + COLUMN_PESAN_THANKS + " TEXT,"
            + COLUMN_PESAN_INFO + " TEXT," + COLUMN_PESAN_PROMO + " TEXT )";

    //create table profil
    private String CREATE_TABLE_PROFIL = "CREATE TABLE IF NOT EXISTS " + TABLE_PROFIL + "("
            + COLUMN_PROFIL_ID + " INT(1) PRIMARY KEY," + COLUMN_PROFIL_EMAIL + " TEXT,"
            + COLUMN_PROFIL_TELP + " VARCHAR(15)," + COLUMN_PROFIL_ALAMAT + " TEXT,"
            + COLUMN_PROFIL_PROFIL + " TEXT," + COLUMN_PROFIL_VISI + " TEXT,"
            + COLUMN_PROFIL_MISI + " TEXT)";

    //create table program
    private String CREATE_TABLE_PROGRAM = "CREATE TABLE IF NOT EXISTS " + TABLE_PROGRAM + "("
            + COLUMN_PROGRAM_ID + " INT(2) PRIMARY KEY," + COLUMN_PROGRAM_JUDUL + " TEXT,"
            + COLUMN_PROGRAM_ISI + " TEXT)";

    //create table anak
    private String CREATE_TABLE_ANAK = "CREATE TABLE IF NOT EXISTS " + TABLE_ANAK + "("
            + COLUMN_ANAK_ID + " INT(4) PRIMARY KEY," + COLUMN_ANAK_NAMA + " TEXT,"
            + COLUMN_ANAK_INFOMUKIM + " VARCHAR(10),"
            + COLUMN_ANAK_INFODHUAFA + " VARCHAR(10),"
            + COLUMN_ANAK_DESKRIPSI + " TEXT)";

    //create table anak
    private String CREATE_TABLE_INFOANAK = "CREATE TABLE IF NOT EXISTS " + TABLE_INFOANAK + "("
            + COLUMN_INFOANAK_ID + " INT(1) PRIMARY KEY," + COLUMN_INFOANAK_JMLH + " INT(6),"
            + COLUMN_INFOANAK_MUKIM + " INT(6)," + COLUMN_INFOANAK_DHUAFA + " INT(6))";

    //create table lokasi
    private String CREATE_TABLE_LOKASI = "CREATE TABLE IF NOT EXISTS " + TABLE_LOKASI + "("
            + COLUMN_LOKASI_ID + " INT(20) PRIMARY KEY," + COLUMN_LOKASI_NAMA + " VARCHAR(200),"
            + COLUMN_LOKASI_ALAMAT + " TEXT," + COLUMN_LOKASI_LAT + " VARCHAR(50),"
            + COLUMN_LOKASI_CONTACT + " VARCHAR(50)," + COLUMN_LOKASI_PETUGAS + " VARCHAR(50),"
            + COLUMN_LOKASI_LON +" VARCHAR(50)," + COLUMN_LOKASI_KATEGORI +" VARCHAR(20))";

    //create table rekening
    private String CREATE_TABLE_REK = "CREATE TABLE IF NOT EXISTS " + TABLE_REK + "("
            + COLUMN_REK_ID + " INT(1) PRIMARY KEY," + COLUMN_REK_NAMA + " TEXT,"
            + COLUMN_REK_BRI + " VARCHAR(50)," + COLUMN_REK_BNI + " VARCHAR(50)," + COLUMN_REK_BCA +" VARCHAR(50),"
            + COLUMN_REK_MANDIRI +" VARCHAR(50)," + COLUMN_REK_MUAMALAT +" VARCHAR(50))";

    public DatabaseVitone(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_DONATUR);
        db.execSQL(CREATE_TABLE_PESAN);
        db.execSQL("INSERT OR IGNORE INTO "+TABLE_PESAN+" VALUES (1, '', '', '');");
        db.execSQL(CREATE_TABLE_PROFIL);
        db.execSQL("INSERT OR IGNORE INTO "+TABLE_PROFIL+" VALUES (1, '-', '-', '-', '-', '-', '-');");
        db.execSQL(CREATE_TABLE_PROGRAM);
        db.execSQL(CREATE_TABLE_ANAK);
        db.execSQL(CREATE_TABLE_INFOANAK);
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_INFOANAK + " VALUES (1, 0, 0, 0);");
        db.execSQL(CREATE_TABLE_LOKASI);
        db.execSQL(CREATE_TABLE_REK);
        db.execSQL("INSERT OR IGNORE INTO " + TABLE_REK + " VALUES (1, '-', '-', '-', '-', '-', '-');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }


    //query donatur
    public long addDonaturManual(Donatur donatur){
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DONATUR_ID, donatur.getKontak());
        values.put(COLUMN_DONATUR_NAMA, donatur.getNama());
        values.put(COLUMN_DONATUR_KONTAK, donatur.getKontak());

        result = db.insert(TABLE_DONATUR, null, values);
        db.close();

        return result;
    }

    public long addDonaturContact(List<Donatur> donaturs){
        long result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=0; i<donaturs.size(); i++){
            if(donaturs.get(i).getCheck()){
                ContentValues values = new ContentValues();
                values.put(COLUMN_DONATUR_ID, donaturs.get(i).getId());
                values.put(COLUMN_DONATUR_NAMA, donaturs.get(i).getNama());
                values.put(COLUMN_DONATUR_KONTAK, donaturs.get(i).getKontak());

                result = db.insert(TABLE_DONATUR, null, values);
            }
        }
        db.close();
        return result;
    }

    public List<Donatur> getListDonatur(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_DONATUR_ID,
                COLUMN_DONATUR_NAMA,
                COLUMN_DONATUR_KONTAK
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_DONATUR_ID + " LIKE ?";

        // selection arguments
        String[] selectionArgs = {"%%"};

        Cursor cursor = db.query(TABLE_DONATUR, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        List<Donatur> listDonatur = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Donatur donatur = new Donatur();
                donatur.setId(cursor.getString(cursor.getColumnIndex(COLUMN_DONATUR_ID)));
                donatur.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_DONATUR_NAMA)));
                donatur.setKontak(cursor.getString(cursor.getColumnIndex(COLUMN_DONATUR_KONTAK)));
                listDonatur.add(donatur);
            }
        }
        cursor.close();
        db.close();

        return listDonatur;
    }

    public boolean checkDonatur(String id){
        boolean result = false;
        // array of columns to fetch
        String[] columns = {
                COLUMN_DONATUR_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_DONATUR_ID + " = ?";

        // selection arguments
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TABLE_DONATUR, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        result = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return result;
    }

    public void deleteDonatur(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_DONATUR, COLUMN_DONATUR_ID + " = ?",
                new String[]{id});
        db.close();
    }

    //query pesan
    public int updatePesanThanks(Pesan pesan){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PESAN_ID, pesan.getId());
        values.put(COLUMN_PESAN_THANKS, pesan.getThanks());

        // updating row
        result = db.update(TABLE_PESAN, values, COLUMN_PESAN_ID + " = ?",
                new String[]{pesan.getId()});
        db.close();

        return result;
    }

    public int updatePesanInfo(Pesan pesan){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PESAN_ID, pesan.getId());
        values.put(COLUMN_PESAN_INFO, pesan.getInfo());

        // updating row
        result = db.update(TABLE_PESAN, values, COLUMN_PESAN_ID + " = ?",
                new String[]{pesan.getId()});
        db.close();

        return result;
    }

    public int updatePesanPromo(Pesan pesan){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PESAN_ID, pesan.getId());
        values.put(COLUMN_PESAN_PROMO, pesan.getPromo());

        // updating row
        result = db.update(TABLE_PESAN, values, COLUMN_PESAN_ID + " = ?",
                new String[]{pesan.getId()});
        db.close();

        return result;
    }

    public Pesan getPesan(String id){
        // array of columns to fetch
        String[] columns = {
                COLUMN_PESAN_ID,
                COLUMN_PESAN_THANKS,
                COLUMN_PESAN_INFO,
                COLUMN_PESAN_PROMO
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_PESAN_ID + " = ?";

        // selection arguments
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TABLE_PESAN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        Pesan pesan = new Pesan();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                pesan.setId(cursor.getString(cursor.getColumnIndex(COLUMN_PESAN_ID)));
                pesan.setThanks(cursor.getString(cursor.getColumnIndex(COLUMN_PESAN_THANKS)));
                pesan.setInfo(cursor.getString(cursor.getColumnIndex(COLUMN_PESAN_INFO)));
                pesan.setPromo(cursor.getString(cursor.getColumnIndex(COLUMN_PESAN_PROMO)));
            }
        }
        cursor.close();
        db.close();
        return pesan;
    }

    //query profil
    public void updateProfil(Profil profil){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PROFIL_ID, profil.getId());
        values.put(COLUMN_PROFIL_TELP, profil.getTelp());
        values.put(COLUMN_PROFIL_ALAMAT, profil.getAlamat());
        values.put(COLUMN_PROFIL_EMAIL, profil.getEmail());
        values.put(COLUMN_PROFIL_PROFIL, profil.getProfil());
        values.put(COLUMN_PROFIL_VISI, profil.getVisi());
        values.put(COLUMN_PROFIL_MISI, profil.getMisi());

        // updating row
        db.update(TABLE_PROFIL, values, COLUMN_PROFIL_ID + " = ?",
                new String[]{profil.getId()});
        db.close();
    }

    public Profil getProfil(String id){
        // array of columns to fetch
        String[] columns = {
                COLUMN_PROFIL_ID,
                COLUMN_PROFIL_EMAIL,
                COLUMN_PROFIL_TELP,
                COLUMN_PROFIL_ALAMAT,
                COLUMN_PROFIL_PROFIL,
                COLUMN_PROFIL_VISI,
                COLUMN_PROFIL_MISI
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_PROFIL_ID + " = ?";

        // selection arguments
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TABLE_PROFIL, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        Profil profil = new Profil();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                profil.setId(cursor.getString(cursor.getColumnIndex(COLUMN_PROFIL_ID)));
                profil.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_PROFIL_EMAIL)));
                profil.setTelp(cursor.getString(cursor.getColumnIndex(COLUMN_PROFIL_TELP)));
                profil.setAlamat(cursor.getString(cursor.getColumnIndex(COLUMN_PROFIL_ALAMAT)));
                profil.setProfil(cursor.getString(cursor.getColumnIndex(COLUMN_PROFIL_PROFIL)));
                profil.setVisi(cursor.getString(cursor.getColumnIndex(COLUMN_PROFIL_VISI)));
                profil.setMisi(cursor.getString(cursor.getColumnIndex(COLUMN_PROFIL_MISI)));
            }
        }
        cursor.close();
        db.close();

        return profil;
    }

    //query anak
    public int updateAnak(List<Anak> anaks){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=0; i < anaks.size(); i++){
            ContentValues values = new ContentValues();
            values.put(COLUMN_ANAK_ID, anaks.get(i).getId());
            values.put(COLUMN_ANAK_NAMA, anaks.get(i).getNama());
            values.put(COLUMN_ANAK_INFOMUKIM, anaks.get(i).getInfomukim());
            values.put(COLUMN_ANAK_INFODHUAFA, anaks.get(i).getInfoDhuafa());

            //inserting row
            db.execSQL("INSERT OR IGNORE INTO "+TABLE_ANAK+
                    "("+COLUMN_ANAK_ID+","+COLUMN_ANAK_NAMA+","+COLUMN_ANAK_INFOMUKIM+","+COLUMN_ANAK_INFODHUAFA+") VALUES(?,?,?,?)",
                    new Object[]{values.get(COLUMN_ANAK_ID), values.get(COLUMN_ANAK_NAMA), values.get(COLUMN_ANAK_INFOMUKIM),
                    values.get(COLUMN_ANAK_INFODHUAFA)});

            // updating row
            result = db.update(TABLE_ANAK, values, COLUMN_ANAK_ID + " = ?",
                    new String[]{anaks.get(i).getId()});
        }

        db.close();
        return result;
    }

    public List<Anak> getListAnak(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_ANAK_ID,
                COLUMN_ANAK_NAMA,
                COLUMN_ANAK_INFOMUKIM,
                COLUMN_ANAK_INFODHUAFA,
                COLUMN_ANAK_DESKRIPSI
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_ANAK_ID + " LIKE ?";

        // selection arguments
        String[] selectionArgs = {"%%"};

        Cursor cursor = db.query(TABLE_ANAK, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        List<Anak> listAnak = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Anak anak = new Anak();
                anak.setId(cursor.getString(cursor.getColumnIndex(COLUMN_ANAK_ID)));
                anak.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_ANAK_NAMA)));
                anak.setInfomukim(cursor.getString(cursor.getColumnIndex(COLUMN_ANAK_INFOMUKIM)));
                anak.setInfoDhuafa(cursor.getString(cursor.getColumnIndex(COLUMN_ANAK_INFODHUAFA)));
                anak.setDeksripsi(cursor.getString(cursor.getColumnIndex(COLUMN_ANAK_DESKRIPSI)));
                listAnak.add(anak);
            }
        }
        cursor.close();
        db.close();

        return listAnak;
    }

    //query info_program
    public int updateProgram(List<Program> programs){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=0; i < programs.size(); i++){
            ContentValues values = new ContentValues();
            values.put(COLUMN_PROGRAM_ID, programs.get(i).getId());
            values.put(COLUMN_PROGRAM_JUDUL, programs.get(i).getJudul());
            values.put(COLUMN_PROGRAM_ISI, programs.get(i).getIsi());


            //inserting row
            db.execSQL("INSERT OR IGNORE INTO "+TABLE_PROGRAM+" VALUES(?,?,?)",
                    new Object[]{values.get(COLUMN_PROGRAM_ID), values.get(COLUMN_PROGRAM_JUDUL), values.get(COLUMN_PROGRAM_ISI)});

            // updating row
            result = db.update(TABLE_PROGRAM, values, COLUMN_PROGRAM_ID + " = ?",
                    new String[]{programs.get(i).getId()});
        }

        db.close();
        return result;
    }

    public List<Program> getListProgram(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_PROGRAM_ID,
                COLUMN_PROGRAM_JUDUL,
                COLUMN_PROGRAM_ISI
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_PROGRAM_ID + " LIKE ?";

        // selection arguments
        String[] selectionArgs = {"%%"};

        Cursor cursor = db.query(TABLE_PROGRAM, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        List<Program> listProgram = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Program program = new Program();
                program.setId(cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID)));
                program.setJudul(cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_JUDUL)));
                program.setIsi(cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ISI)));
                listProgram.add(program);
            }
        }
        cursor.close();
        db.close();

        return listProgram;
    }

    //query info_anak
    public int updateInfoAnak(InfoAnak info){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_INFOANAK_ID, info.getId());
        values.put(COLUMN_INFOANAK_JMLH, info.getJmlh());
        values.put(COLUMN_INFOANAK_MUKIM, info.getMukim());
        values.put(COLUMN_INFOANAK_DHUAFA, info.getDhuafa());

        // updating row
        result = db.update(TABLE_INFOANAK, values, COLUMN_INFOANAK_ID + " = ?",
                new String[]{info.getId()});
        db.close();
        return result;
    }

    public InfoAnak getInfoAnak(String id){
        // array of columns to fetch
        String[] columns = {
                COLUMN_INFOANAK_ID,
                COLUMN_INFOANAK_JMLH,
                COLUMN_INFOANAK_MUKIM,
                COLUMN_INFOANAK_DHUAFA
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_INFOANAK_ID + " = ?";

        // selection arguments
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TABLE_INFOANAK, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        InfoAnak info = new InfoAnak();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                info.setId(cursor.getString(cursor.getColumnIndex(COLUMN_INFOANAK_ID)));
                info.setJmlh(cursor.getInt(cursor.getColumnIndex(COLUMN_INFOANAK_JMLH)));
                info.setMukim(cursor.getInt(cursor.getColumnIndex(COLUMN_INFOANAK_MUKIM)));
                info.setDhuafa(cursor.getInt(cursor.getColumnIndex(COLUMN_INFOANAK_DHUAFA)));
            }
        }
        cursor.close();
        db.close();

        return info;
    }

    //query lokasi
    public int updateMesjid(List<Lokasi> listLokasi){
        int result = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        for(int i=0; i < listLokasi.size(); i++){
            ContentValues values = new ContentValues();
            values.put(COLUMN_LOKASI_ID, listLokasi.get(i).getId());
            values.put(COLUMN_LOKASI_NAMA, listLokasi.get(i).getNama());
            values.put(COLUMN_LOKASI_ALAMAT, listLokasi.get(i).getAlamat());
            values.put(COLUMN_LOKASI_CONTACT, listLokasi.get(i).getContact());
            values.put(COLUMN_LOKASI_PETUGAS, listLokasi.get(i).getPetugas());
            values.put(COLUMN_LOKASI_LAT, listLokasi.get(i).getLat());
            values.put(COLUMN_LOKASI_LON, listLokasi.get(i).getLon());
            values.put(COLUMN_LOKASI_KATEGORI, listLokasi.get(i).getKategori());

            //inserting row
            db.execSQL("INSERT OR IGNORE INTO "+TABLE_LOKASI+
                            " VALUES(?,?,?,?,?,?,?,?)",
                    new Object[]{values.get(COLUMN_LOKASI_ID), values.get(COLUMN_LOKASI_ALAMAT), values.get(COLUMN_LOKASI_ALAMAT),
                            values.get(COLUMN_LOKASI_CONTACT), values.get(COLUMN_LOKASI_PETUGAS),
                            values.get(COLUMN_LOKASI_LAT),values.get(COLUMN_LOKASI_LON),values.get(COLUMN_LOKASI_KATEGORI)});

            // updating row
            result = db.update(TABLE_LOKASI, values, COLUMN_LOKASI_ID + " = ?",
                    new String[]{listLokasi.get(i).getId()});
        }

        db.close();
        return result;
    }

    public List<Lokasi> getListMesjid(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_LOKASI_ID,
                COLUMN_LOKASI_NAMA,
                COLUMN_LOKASI_ALAMAT,
                COLUMN_LOKASI_CONTACT,
                COLUMN_LOKASI_PETUGAS,
                COLUMN_LOKASI_LAT,
                COLUMN_LOKASI_LON,
                COLUMN_LOKASI_KATEGORI
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_LOKASI_KATEGORI + " = ?";

        // selection arguments
        String[] selectionArgs = {"mesjid"};

        Cursor cursor = db.query(TABLE_LOKASI, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        List<Lokasi> listMesjid = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Lokasi lokasi = new Lokasi();
                lokasi.setId(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_ID)));
                lokasi.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_NAMA)));
                lokasi.setAlamat(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_ALAMAT)));
                lokasi.setContact(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_CONTACT)));
                lokasi.setPetugas(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_PETUGAS)));
                lokasi.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_LAT)));
                lokasi.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_LON)));
                lokasi.setKategori(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_KATEGORI)));
                lokasi.setMarker();
                listMesjid.add(lokasi);
            }
        }
        cursor.close();
        db.close();

        return listMesjid;
    }

    public Lokasi getPanti(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_LOKASI_ID,
                COLUMN_LOKASI_NAMA,
                COLUMN_LOKASI_ALAMAT,
                COLUMN_LOKASI_CONTACT,
                COLUMN_LOKASI_PETUGAS,
                COLUMN_LOKASI_LAT,
                COLUMN_LOKASI_LON,
                COLUMN_LOKASI_KATEGORI
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_LOKASI_KATEGORI + " = ?";

        // selection arguments
        String[] selectionArgs = {"panti"};

        Cursor cursor = db.query(TABLE_LOKASI, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        Lokasi lokasi = new Lokasi();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                lokasi.setId(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_ID)));
                lokasi.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_NAMA)));
                lokasi.setAlamat(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_ALAMAT)));
                lokasi.setContact(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_CONTACT)));
                lokasi.setPetugas(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_PETUGAS)));
                lokasi.setLat(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_LAT)));
                lokasi.setLon(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_LON)));
                lokasi.setKategori(cursor.getString(cursor.getColumnIndex(COLUMN_LOKASI_KATEGORI)));
                lokasi.setMarker();
            }
        }
        cursor.close();
        db.close();

        return lokasi;
    }

    //query rek
    public void updateRek(Rekening rek){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_REK_ID, rek.getId());
        values.put(COLUMN_REK_NAMA, rek.getNama());
        values.put(COLUMN_REK_BRI, rek.getBri());
        values.put(COLUMN_REK_BNI, rek.getBni());
        values.put(COLUMN_REK_BCA, rek.getBca());
        values.put(COLUMN_REK_MANDIRI, rek.getMandiri());
        values.put(COLUMN_REK_MUAMALAT, rek.getMuamalat());

        // updating row
        db.update(TABLE_REK, values, COLUMN_REK_ID + " = ?",
                new String[]{rek.getId()});
        db.close();
    }

    public Rekening getRekening(String id){
        // array of columns to fetch
        String[] columns = {
                COLUMN_REK_ID,
                COLUMN_REK_NAMA,
                COLUMN_REK_BRI,
                COLUMN_REK_BNI,
                COLUMN_REK_BCA,
                COLUMN_REK_MANDIRI,
                COLUMN_REK_MUAMALAT
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_REK_ID + " = ?";

        // selection arguments
        String[] selectionArgs = {id};

        Cursor cursor = db.query(TABLE_REK, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        Rekening rek = new Rekening();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                rek.setId(cursor.getString(cursor.getColumnIndex(COLUMN_REK_ID)));
                rek.setNama(cursor.getString(cursor.getColumnIndex(COLUMN_REK_NAMA)));
                rek.setBri(cursor.getString(cursor.getColumnIndex(COLUMN_REK_BRI)));
                rek.setBni(cursor.getString(cursor.getColumnIndex(COLUMN_REK_BNI)));
                rek.setBca(cursor.getString(cursor.getColumnIndex(COLUMN_REK_BCA)));
                rek.setMandiri(cursor.getString(cursor.getColumnIndex(COLUMN_REK_MANDIRI)));
                rek.setMuamalat(cursor.getString(cursor.getColumnIndex(COLUMN_REK_MUAMALAT)));
            }
        }
        cursor.close();
        db.close();

        return rek;
    }
}
