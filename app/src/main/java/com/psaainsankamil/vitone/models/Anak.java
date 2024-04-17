package com.psaainsankamil.vitone.models;

/**
 * Created by zaki on 27/05/18.
 */

public class Anak {
    private String id;
    private String nama;
    private String infomukim;
    private String infoDhuafa;
    private String deksripsi;

    public Anak(){}

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setInfomukim(String infomukim) {
        this.infomukim = infomukim;
    }

    public void setInfoDhuafa(String infoDhuafa) {
        this.infoDhuafa = infoDhuafa;
    }

    public void setDeksripsi(String deksripsi) {
        this.deksripsi = deksripsi;
    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getInfomukim() {
        return infomukim;
    }

    public String getInfoDhuafa() {
        return infoDhuafa;
    }

    public String getDeksripsi() {
        return deksripsi;
    }
}
