package com.example.presly.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.presly.Values.Globals;
import com.example.presly.Places.Place;
import com.example.presly.R;
import com.example.presly.Values.Repository;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private SupportMapFragment fragmentMap;

    public MapFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflating the layout
        LinearLayout linearLayoutMap = (LinearLayout) inflater.inflate(R.layout.fragment_map, container, false);

        //fragments map:
        fragmentMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment);
        fragmentMap.getMapAsync(this);

        return linearLayoutMap;
    }

    //when the map is ready to launch:
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (Repository.lastPresentedPlaces != null) {
            showPlaceOnMap(Repository.lastPresentedPlaces);
        }

    }

    public void showPlaceOnMap(Place place) {
        googleMap.clear();

        LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(place.getPlaceName());
        markerOptions.snippet(place.getPlaceAddress());
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        googleMap.addMarker(markerOptions);

        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);

        int zoom = Globals.DEFAULT_ZOOM;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);

        //display the map with animation
        googleMap.animateCamera(cameraUpdate);


    }
}
