package com.psaainsankamil.vitone.models.maps;

import java.util.List;

/**
 * Created by zaki on 05/06/18.
 */

public class Route {

    private List<Legs> legs;

    public List<Legs> getLegs() {
        return legs;
    }

    public void setLegs(List<Legs> legs) {
        this.legs = legs;
    }
}
