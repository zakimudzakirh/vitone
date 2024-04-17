package com.psaainsankamil.vitone.models;

/**
 * Created by zaki on 27/05/18.
 */

public class Program {
    private String id;
    private String judul;
    private String isi;

    public void setId(String id) {
        this.id = id;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public String getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getIsi() {
        return isi;
    }
}
