package com.example.presly.Places;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.presly.Downloader.ImageDownloader;
import com.example.presly.MainActivity;
import com.example.presly.R;
import com.example.presly.Values.Globals;


public class PlaceHolder extends RecyclerView.ViewHolder {

    private Activity activity;
    private ImageView imageViewPlaceImage, imageViewPlaceShare;
    private TextView textViewPlaceName, textViewPlaceAddress, textViewPlaceDistance;
    private Place place;


    public PlaceHolder(final Activity activity, final View itemView, final Callbacks callbacks) {
        super(itemView);

        this.activity = activity;

        //callbacks interface :
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (callbacks != null) {
                    callbacks.itemPlaceClicked(place);
                }
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View view) {
                if (callbacks != null) {
                    callbacks.itemPlaceLongClick(place);
                }
                return true;
            }
        });

        //initializing
        imageViewPlaceImage = (ImageView) itemView.findViewById(R.id.imageViewPlaceImage);
        imageViewPlaceShare = (ImageView) itemView.findViewById(R.id.imageViewPlaceShare);
        textViewPlaceName = (TextView) itemView.findViewById(R.id.textViewPlaceName);
        textViewPlaceAddress = (TextView) itemView.findViewById(R.id.textViewPlaceAddres);
        textViewPlaceDistance = (TextView) itemView.findViewById(R.id.textViewPlaceDistance);

        //Share callbacks interface:
        imageViewPlaceShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (callbacks != null) {
                    callbacks.sharePlace(place);
                }
            }
        });

    }

    //recyclerView binding
    @SuppressLint("DefaultLocale")
    public void bindPlace(Place place) {
        this.place = place;

        ImageDownloader imageDownloader = new ImageDownloader(new ImageDownloader.Callbacks() {

            @Override
            public void onAboutToBegin() {                     //before we start

            }

            public void onSuccess(Bitmap downloadedBitmap) {           //if we got result
                imageViewPlaceImage.setImageBitmap(downloadedBitmap);

            }

            public void onError(int httpStatusCode, String errorMessage) {   //something bad...
                imageViewPlaceImage.setImageResource(R.drawable.coffee_icon);
                Toast.makeText(activity, R.string.error_title + errorMessage, Toast.LENGTH_LONG).show();

            }
        });

       //default icon
        String image;
        if (place.getPlaceImage().contains(Globals.SEARCH_IMAGE_CONTAIN)) {
            image = place.getPlaceImage();
        } else {
            image = Globals.GOOGLE_API_IMAGE_SEARCH +
                    place.getPlaceImage() + Globals.GOOGLEMAP_KEY;
        }

        imageDownloader.execute(image);

        textViewPlaceName.setText(place.getPlaceName());
        textViewPlaceAddress.setText(place.getPlaceAddress());

        double measuring;
        if (place.getPlaceDistance() != Globals.DEFAULT_PLACE_DISTANCE) {
            if (MainActivity.unit.equals("Miles")) {
                measuring = Globals.MILES_CALCULATOR_MUL;
            } else measuring = Globals.KILOMETERS_CALCULATOR_MUL;
            double distance = place.getPlaceDistance() * measuring;
            distance = Math.floor(distance * Globals.DISTANCE_DIVIDER_MATH_FLO0R) / Globals.DISTANCE_DIVIDER_MATH_FLO0R;
            distance = Double.valueOf(String.format("%2f", distance));
            textViewPlaceDistance.setVisibility(View.VISIBLE);
            textViewPlaceDistance.setText(distance + " " + MainActivity.unit);
        }
    }

    //callbacks interface:
    public interface Callbacks {
        void itemPlaceClicked(Place place);

        void itemPlaceLongClick(Place place);

        void sharePlace(Place place);
    }

}
