package com.example.presly.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.presly.Places.Place;
import com.example.presly.Downloader.PlaceAdapter;
import com.example.presly.Places.PlaceDatabase;
import com.example.presly.R;

import java.util.ArrayList;


public class FavoriteFragment extends Fragment {

    private ArrayList<Place> allFavoritesPlaces = new ArrayList<>();
    private PlaceDatabase placeDatabase;
    private PlaceAdapter placeAdapter;


    public FavoriteFragment() {
        // Required empty public constructor
    }


    //an on create method that creates this fragment and shows the layout
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment's layout:
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_favorites_list, container, false);

        //check if the favorites list is empty:
        placeDatabase = new PlaceDatabase(getActivity());
        allFavoritesPlaces.clear();
        allFavoritesPlaces.addAll(placeDatabase.getAllFavoritesPlaces());

        // Fill the list view:
        RecyclerView recyclerViewPlaces = (RecyclerView) linearLayout.findViewById(R.id.recyclerViewPlaces);

        placeAdapter = new PlaceAdapter(this.getActivity(), allFavoritesPlaces);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerViewPlaces.setLayoutManager(layoutManager);

        recyclerViewPlaces.setHasFixedSize(true);

        recyclerViewPlaces.setItemViewCacheSize(20);

        recyclerViewPlaces.setDrawingCacheEnabled(true);

        recyclerViewPlaces.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recyclerViewPlaces.setAdapter(placeAdapter);

        // Return the inflated layout:
        return linearLayout;
    }

    //Function to refresh the favorit list if there is a change:
    public void refreshTheFavoriteList() {
        if(allFavoritesPlaces != null){
            allFavoritesPlaces.clear();
            allFavoritesPlaces.addAll(placeDatabase.getAllFavoritesPlaces());
            placeAdapter.notifyDataSetChanged();
        }
           else {

        }

    }
}
