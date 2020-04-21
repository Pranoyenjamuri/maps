package com.pranoy.maps;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pranoy.maps.model.CityModel;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivty extends AppCompatActivity implements OnMapReadyCallback {

    public CityModel selectedCityModel;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a landscape mode
     *
     */
    private boolean mTwoPane;
    private GoogleMap mMap;
    private List<CityModel> cityModelList;
    private SimpleItemRecyclerViewAdapter mSimpleItemRecyclerViewAdapter;
    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String query = editable.toString();
            filterResults(query);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        ((EditText) findViewById(R.id.search_edt)).addTextChangedListener(watcher);


        if (findViewById(R.id.landscape_layout) != null) {
            mTwoPane = true;
        } else {
            findViewById(R.id.map_layout).setVisibility(View.GONE);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.setRetainInstance(true);

        mapFragment.getMapAsync(this);
        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void filterResults(String query) {
        if (query.trim().isEmpty()) {
            mSimpleItemRecyclerViewAdapter.setValues(cityModelList);
            mSimpleItemRecyclerViewAdapter.notifyDataSetChanged();
            return;
        }
        ArrayList<CityModel> tempList = new ArrayList<>();

        for (CityModel cityModel : cityModelList) {
            if (cityModel.getName().startsWith(query)) {
                tempList.add(cityModel);
            } else {
                if (tempList.size() > 0) {
                    //this means a new sequence is started
                    break;
                }
            }
        }
        mSimpleItemRecyclerViewAdapter.setValues(tempList);
        mSimpleItemRecyclerViewAdapter.notifyDataSetChanged();

    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<CityModel>>() {
        }.getType();
        cityModelList = gson.fromJson(loadJSONFromAsset(), type);
        Collections.sort(cityModelList, new CustomComparator());
        mSimpleItemRecyclerViewAdapter = new SimpleItemRecyclerViewAdapter(this, cityModelList, mTwoPane);
        recyclerView.setAdapter(mSimpleItemRecyclerViewAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if (selectedCityModel != null) {
            LatLng sydney = new LatLng((selectedCityModel.getLatLonModel().lat), (selectedCityModel.getLatLonModel().lon));

            mMap.addMarker(new MarkerOptions().position(sydney).title(selectedCityModel.name + "," + selectedCityModel.country + selectedCityModel.coord).snippet(

                    selectedCityModel.getLatLonModel().lat + "," + selectedCityModel.getLatLonModel().lon));

            CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
            mMap.setInfoWindowAdapter(customInfoWindow);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));

        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        if (findViewById(R.id.map_layout) != null && findViewById(R.id.map_layout).getVisibility() == View.VISIBLE)
            findViewById(R.id.map_layout).setVisibility(View.GONE);
        else
            super.onBackPressed();

    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final HomeActivty mParentActivity;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CityModel item = (CityModel) view.getTag();
                mParentActivity.selectedCityModel = item;

                if (!mTwoPane) {
                    mParentActivity.findViewById(R.id.map_layout).setVisibility(View.VISIBLE);
                }
                if (mParentActivity.mMap != null) {
                    LatLng sydney = new LatLng((mParentActivity.selectedCityModel.getLatLonModel().lat), (mParentActivity.selectedCityModel.getLatLonModel().lon));
                    mParentActivity.mMap.addMarker(new MarkerOptions().position(sydney).title(mParentActivity.selectedCityModel.name + "," + mParentActivity.selectedCityModel.country).snippet(
                            mParentActivity.selectedCityModel.getLatLonModel().lat + "," + mParentActivity.selectedCityModel.getLatLonModel().lon));

                    CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(mParentActivity);
                    mParentActivity.mMap.setInfoWindowAdapter(customInfoWindow);
                    mParentActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 14));
                }
            }
        };
        private List<CityModel> mValues;

        SimpleItemRecyclerViewAdapter(HomeActivty parent,
                                      List<CityModel> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        public void setValues(List<CityModel> mValues) {
            this.mValues = mValues;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            CityModel value = mValues.get(position);
            holder.mContentView.setText(value.name + " , " + value.country + " \n " + value.getLatLonModel().lat + " , " + value.getLatLonModel().lon);
            holder.itemView.setTag(value);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final TextView mContentView;

            ViewHolder(View view) {
                super(view);

                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    public class CustomComparator implements Comparator<CityModel> {
        @Override
        public int compare(CityModel o1, CityModel o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
