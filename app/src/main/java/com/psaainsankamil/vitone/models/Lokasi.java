package com.psaainsankamil.vitone.models;

import android.util.Base64;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by zaki on 29/05/18.
 */

public class Lokasi {
    private String id;
    private String nama;
    private String alamat;
    private String contact;
    private String petugas;
    private String lat;
    private String lon;
    private String kategori;
    private MarkerOptions marker;

    public void setId(String id) {
        this.id = id;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setPetugas(String petugas) {
        this.petugas = petugas;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setKategori(String karegori) {
        this.kategori = karegori;
    }

    public void setMarker(){
        if(kategori.equals("panti")) {
            marker = new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
                    .title(nama)
                    .icon(BitmapDescriptorFactory.defaultMarker(123.0f));
        }else{
            marker = new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon)))
                    .title(nama)
                    .snippet("CP: "+contact+"\n"+"Petugas: "+petugas);
        }

    }

    public String getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getContact() {
        return contact;
    }

    public String getPetugas() {
        return petugas;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getKategori() {
        return kategori;
    }

    public MarkerOptions getMarker() {
        return marker;
    }
}
