package com.psaainsankamil.vitone.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zaki on 24/05/18.
 */

public class Member {

    public static final String SP_USERNAME = "username";
    public static final String SP_PASSWORD = "password";
    public static final String SP_EMAIL = "email";
    public static final String SP_NAMA = "nama";
    public static final String SP_PHOTO = "photo";
    public static final String SP_CATEGORY = "category";

    public static final String SP_KEY = "member_key";
    public static final String SP_CHECK = "check";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public Member(Context context){
        sp = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPUsername(){
        return sp.getString(SP_USERNAME, "");
    }

    public String getSPPassword(){
        return sp.getString(SP_PASSWORD, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPPhoto(){
        return sp.getString(SP_PHOTO, "");
    }

    public String getSPCategory(){
        return sp.getString(SP_CATEGORY, "");
    }

    public String getSPKey(){
        return sp.getString(SP_KEY, "");
    }

    public Boolean getSPCheck(){
        return sp.getBoolean(SP_CHECK, false);
    }
}
