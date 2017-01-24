package com.example.presly.Downloader;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.presly.Places.Place;
import com.example.presly.Places.PlaceHolder;
import com.example.presly.R;

import java.util.ArrayList;


public class PlaceAdapter extends RecyclerView.Adapter<PlaceHolder> implements PlaceHolder.Callbacks {

    private Activity activity;
    private ArrayList<Place> allPlaces;
    private LayoutInflater layoutInflater;
    private PlaceHolder.Callbacks callbacks;

    public PlaceAdapter(Activity activity, ArrayList<Place> allPlaces) {
        this.activity = activity;
        this.allPlaces = allPlaces;
        this.callbacks = (PlaceHolder.Callbacks) activity;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.item_place, parent, false);

        //create holder
        PlaceHolder placeHolder = new PlaceHolder(activity, linearLayout, this);

        return placeHolder;
    }

    public void itemPlaceLongClick(Place place) {
        if (callbacks != null) {
            callbacks.itemPlaceLongClick(place);
        }
    }

    //get the bind where the holder "holds" the data of one item on the list:
    public void onBindViewHolder(PlaceHolder holder, int position) {
        Place place = allPlaces.get(position);
        holder.bindPlace(place);
    }

    public void sharePlace(Place place) {
        if (callbacks != null) {
            callbacks.sharePlace(place);
        }
    }

    //get the amount of items in the recycler view:
    public int getItemCount() {
        return allPlaces.size();
    }

    //callbacks interface:
    public void itemPlaceClicked(Place place) {
        if (callbacks != null) {
            callbacks.itemPlaceClicked(place);
        }
    }

}
