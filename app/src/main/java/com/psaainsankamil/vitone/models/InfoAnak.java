package com.psaainsankamil.vitone.models;

/**
 * Created by zaki on 27/05/18.
 */

public class InfoAnak {
    private String id;
    private int jmlh;
    private int mukim;
    private int nonMukim;
    private int yatim;
    private int dhuafa;

    public void setId(String id) {
        this.id = id;
    }

    public void setJmlh(int jmlh) {
        this.jmlh = jmlh;
    }

    public void setMukim(int mukim) {
        this.mukim = mukim;
        this.nonMukim = this.jmlh - mukim;
    }

    public void setDhuafa(int dhuafa) {
        this.dhuafa = dhuafa;
        this.yatim = this.jmlh - dhuafa;
    }

    public String getId() {
        return id;
    }

    public int getJmlh() {
        return jmlh;
    }

    public int getMukim() {
        return mukim;
    }

    public int getNonMukim() {
        return nonMukim;
    }

    public int getYatim() {
        return yatim;
    }

    public int getDhuafa() {
        return dhuafa;
    }
}
