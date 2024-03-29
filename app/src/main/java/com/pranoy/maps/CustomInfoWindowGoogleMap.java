package com.pranoy.maps;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Context context;

    public CustomInfoWindowGoogleMap(Context ctx){
        context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.marker_info_window, null);

        TextView name_tv = view.findViewById(R.id.city_country);
        TextView details_tv = view.findViewById(R.id.lat_long);


        name_tv.setText(marker.getTitle());
        details_tv.setText(marker.getSnippet());




        return view;
    }
}
