package com.example.thilinidineshika.mapdirection;

import com.google.android.gms.common.internal.DowngradeableSafeParcel;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by OshadhaDeemantha on 3/17/2017.
 */

public class LocationDistance implements Comparable<LocationDistance> {
    private Integer distance;
    private LatLng location;

    public LocationDistance(Integer distance, LatLng location) {
        this.distance = distance;
        this.location = location;
    }

    public int compareTo(LocationDistance ld){
        return ld.distance.compareTo(this.distance);
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
