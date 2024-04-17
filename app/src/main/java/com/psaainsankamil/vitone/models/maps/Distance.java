package com.psaainsankamil.vitone.models.maps;

/**
 * Created by zaki on 05/06/18.
 */

public class Distance {

    private String text;
    private String value;

    public Distance(String text, String value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
