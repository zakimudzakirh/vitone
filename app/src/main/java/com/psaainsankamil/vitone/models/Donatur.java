package com.psaainsankamil.vitone.models;

/**
 * Created by zaki on 24/05/18.
 */

public class Donatur {
    private String id;
    private String nama;
    private String kontak;
    private boolean check;

    public Donatur(){}

    public Donatur(String id, String nama, String kontak){
        this.id = id;
        this.nama = nama;
        this.kontak = kontak;
    }

    public Donatur(String id, String nama, String kontak, boolean check){
        this.id = id;
        this.nama = nama;
        this.kontak = kontak;
        this.check = check;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setNama(String nama){
        this.nama = nama;
    }

    public void setKontak(String kontak){
        this.kontak = kontak;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getId(){
        return this.id;
    }

    public String getNama(){
        return this.nama;
    }

    public String getKontak(){
        return this.kontak;
    }

    public boolean getCheck(){
        return this.check;
    }
}
