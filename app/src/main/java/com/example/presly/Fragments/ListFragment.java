package com.example.presly.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.presly.Values.Globals;
import com.example.presly.Places.Place;
import com.example.presly.Downloader.PlaceAdapter;
import com.example.presly.Places.PlaceDatabase;
import com.example.presly.R;
import com.example.presly.Values.Repository;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    private PlaceAdapter placeAdapter;
    private ArrayList<Place> allPlaces = new ArrayList<>();
    private RecyclerView recyclerViewPlaces;
    private LinearLayout linearLayout;
    private PlaceDatabase placeDatabase;


    public ListFragment() {
        // Required empty public constructor
    }


    //an on create method that creates this fragment and shows the layout
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the fragment's layout:
        linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_list, container, false);

        placeDatabase = new PlaceDatabase(getActivity());

        if (Repository.lastSearchedPlaces != null) {
            allPlaces.clear();
            allPlaces.addAll(Repository.lastSearchedPlaces);
        } else if (!placeDatabase.getAllLastSearchedPlaces().isEmpty()) {
            allPlaces.clear();
            allPlaces.addAll(placeDatabase.getAllLastSearchedPlaces());
        }

        // Fill the list view:
        recyclerViewPlaces = (RecyclerView) linearLayout.findViewById(R.id.recyclerViewPlaces);


        placeAdapter = new PlaceAdapter(this.getActivity(), allPlaces);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        recyclerViewPlaces.setLayoutManager(layoutManager);

        recyclerViewPlaces.setHasFixedSize(true);

        recyclerViewPlaces.setItemViewCacheSize(Globals.SIZE_OF_ITEMS);

        recyclerViewPlaces.setDrawingCacheEnabled(true);

        recyclerViewPlaces.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recyclerViewPlaces.setAdapter(placeAdapter);

        // Return the inflated layout:
        return linearLayout;
    }

    //set and refresh the search list:
    public void refreshTheList() {
        if (Repository.lastSearchedPlaces != null) {
            allPlaces.clear();
            allPlaces.addAll(Repository.lastSearchedPlaces);
            placeAdapter.notifyDataSetChanged();
        } else if (!placeDatabase.getAllLastSearchedPlaces().isEmpty()) {
            allPlaces.clear();
            allPlaces.addAll(placeDatabase.getAllLastSearchedPlaces());
            placeAdapter.notifyDataSetChanged();
        }
    }

    //on pause method - we save the state :
    public void onPause() {
        placeDatabase.deleteAllSearchedPlaces();
        saveToDB();
        super.onPause();
    }

    //function to get list of places and save in Database:
    public void saveToDB() {
        if (!allPlaces.isEmpty()) {
            for (int i = Globals.START_VALUE; i < allPlaces.size(); i++) {
                placeDatabase.addPlace(allPlaces.get(i));
            }
        }

    }

}