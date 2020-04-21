package com.pranoy.maps.model;

import java.io.Serializable;

public class CityModel implements Serializable {


    public final String country;
    public final String name;
    public final String _id;
    public  final LatLongModel coord;

    public CityModel(String country, String name, String _id, LatLongModel coord) {
        this.country = country;
        this.name = name;
        this._id = _id;
        this.coord =coord;
    }

    public LatLongModel getLatLonModel() {
        return coord;
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }

    @Override
    public String toString() {
        return name;
    }

}
