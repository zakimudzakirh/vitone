package com.psaainsankamil.vitone.models;

/**
 * Created by zaki on 27/05/18.
 */

public class Profil {
    private String id;
    private String email;
    private String telp;
    private String alamat;
    private String profil;
    private String visi;
    private String misi;

    public Profil(){}

    public void setId(String id){
        this.id = id;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setTelp(String telp){
        this.telp = telp;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public void setProfil(String profil){
        this.profil = profil;
    }

    public void setVisi(String visi){
        this.visi = visi;
    }

    public void setMisi(String misi){
        this.misi = misi;
    }

    public String getId(){
        return this.id;
    }

    public String getAlamat() {
        return this.alamat;
    }

    public String getEmail() {
        return this.email;
    }

    public String getTelp() {
        return this.telp;
    }

    public String getProfil() {
        return this.profil;
    }

    public String getVisi() {
        return this.visi;
    }

    public String getMisi() {
        return this.misi;
    }
}
