package com.psaainsankamil.vitone.models;

/**
 * Created by zaki on 29/05/18.
 */

public class Rekening {
    private String id;
    private String nama;
    private String bri;
    private String bni;
    private String bca;
    private String mandiri;
    private String muamalat;

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setBri(String bri) {
        this.bri = bri;
    }

    public void setBni(String bni) {
        this.bni = bni;
    }

    public void setBca(String bca) {
        this.bca = bca;
    }

    public void setMandiri(String mandiri) {
        this.mandiri = mandiri;
    }

    public void setMuamalat(String muamalat) {
        this.muamalat = muamalat;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getBri() {
        return bri;
    }

    public String getBni() {
        return bni;
    }

    public String getBca() {
        return bca;
    }

    public String getMandiri() {
        return mandiri;
    }

    public String getMuamalat() {
        return muamalat;
    }
}
