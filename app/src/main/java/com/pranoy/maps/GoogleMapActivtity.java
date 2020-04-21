package com.pranoy.maps;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.pranoy.maps.model.CityModel;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.NavUtils;

import android.view.MenuItem;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item _id are presented side-by-side with a list of items
 * in a {@link HomeActivty}.
 */
public class GoogleMapActivtity extends AppCompatActivity implements OnMapReadyCallback {

    private CityModel selectedCityModel;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            selectedCityModel = (CityModel) getIntent().getSerializableExtra("item");
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, HomeActivty.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        if (selectedCityModel != null) {
            LatLng sydney = new LatLng((selectedCityModel.getLatLonModel().lat), (selectedCityModel.getLatLonModel().lon));
            mMap.addMarker(new MarkerOptions().position(sydney).title(selectedCityModel.name + "," + selectedCityModel.country ).snippet(

                    selectedCityModel.getLatLonModel().lat + "," + selectedCityModel.getLatLonModel().lon));

            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
            mMap.setInfoWindowAdapter(customInfoWindow);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        }
    }
}
