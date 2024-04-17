package com.psaainsankamil.vitone.models;

/**
 * Created by zaki on 26/05/18.
 */

public class Pesan {
    private String id;
    private String thanks;
    private String info;
    private String promo;

    public Pesan(){}

    public Pesan(String id, String thanks, String info, String promo){
        this.id = id;
        this.thanks = thanks;
        this.info = info;
        this.promo = promo;
    }

    public void setId(String id){
        this.id = id;
    }

    public void setThanks(String thanks){
        this.thanks = thanks;
    }

    public void setInfo(String info){
        this.info = info;
    }

    public void setPromo(String promo){
        this.promo = promo;
    }

    public String getId(){
        return this.id;
    }

    public String getThanks(){
        return this.thanks;
    }

    public String getInfo(){
        return this.info;
    }

    public String getPromo(){
        return this.promo;
    }
}
