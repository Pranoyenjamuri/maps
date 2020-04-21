package com.pranoy.maps.model;

import java.io.Serializable;

public class LatLongModel implements Serializable {

    public final double lon;
    public final double lat;

    public LatLongModel(long lon, long lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
