package com.psaainsankamil.vitone.models.maps;

import com.psaainsankamil.vitone.models.Lokasi;

/**
 * Created by zaki on 31/05/18.
 */

public class Legs {

    private Distance distance;
    private Duration duration;
    private String end_address;
    private Lokasi end_location;
    private String start_address;
    private Lokasi start_location;

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEnd_address() {
        return end_address;
    }

    public void setEnd_address(String end_address) {
        this.end_address = end_address;
    }

    public Lokasi getEnd_location() {
        return end_location;
    }

    public void setEnd_location(Lokasi end_location) {
        this.end_location = end_location;
    }

    public String getStart_address() {
        return start_address;
    }

    public void setStart_address(String start_address) {
        this.start_address = start_address;
    }

    public Lokasi getStart_location() {
        return start_location;
    }

    public void setStart_location(Lokasi start_location) {
        this.start_location = start_location;
    }
}