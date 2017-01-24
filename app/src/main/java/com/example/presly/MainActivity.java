package com.example.presly;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.presly.Downloader.TextDownloader;
import com.example.presly.Fragments.FavoriteFragment;
import com.example.presly.Fragments.ListFragment;
import com.example.presly.Fragments.MapFragment;
import com.example.presly.Places.Place;
import com.example.presly.Places.PlaceDatabase;
import com.example.presly.Places.PlaceHolder;
import com.example.presly.Values.Globals;
import com.example.presly.Values.Repository;
import com.example.presly.Values.Settings;
import com.example.presly.Values.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements TextDownloader.Callbacks, LocationListener, PlaceHolder.Callbacks, AlertDialog.OnClickListener {

    private String picture;
    private int preferanceRadius;
    private EditText editTextSearch;
    private double userLatitude, userLongitude;

    private ListFragment listFragment;
    private MapFragment mapFragment;
    private FavoriteFragment favoriteFragment;

    private PlaceDatabase placeDatabase;
    private Place place;
    private ArrayList<Place> allPlaces = new ArrayList<>();
    private ProgressDialog progressDialog;
    public static String unit = Globals.MILES;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar:
        Toolbar toolbarMenu = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarMenu);

        loadSettings();
        placeDatabase = new PlaceDatabase(this);

        //Create and add the place list fragment:
        listFragment = new ListFragment();
        mapFragment = new MapFragment();
        favoriteFragment = new FavoriteFragment();

        if (Repository.isFavorite) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainer, favoriteFragment) // replace instead of add, so if there is already a previous fragment - before orientation changed - replace it and don't add another one on top of it.
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainer, listFragment) // replace instead of add, so if there is already a previous fragment - before orientation changed - replace it and don't add another one on top of it.
                    .commit();
        }

        //Create and add the map fragment if it's landscape mode:
        if (Utilities.isLandscape(this)) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainerMap, mapFragment) // replace instead of add, so if there is already a previous fragment - before orientation changed - replace it and don't add another one on top of it.
                    .commit();
        }


        editTextSearch = (EditText) findViewById(R.id.editTextSearch);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        String provider = LocationManager.NETWORK_PROVIDER;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        locationManager.requestLocationUpdates(provider, Globals.MIN_TIME, Globals.MIN_DISTANCE, this);
    }

    public void loadSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.settings_pref_title), Globals.DEFAULT_NUMBER);

        unit = sharedPreferences.getString(getString(R.string.unit_distance_list_pref_title), getString(R.string.default_value));
        if (unit.equals(getString(R.string.default_value))) {
            unit = getString(R.string.default_kilometers_unit);
        }
        String radius = sharedPreferences.getString(getString(R.string.radius_distance_list_title), getString(R.string.default_radius));

        switch (radius) {
            case "2":
                radius = "2000";
                break;
            case "3":
                radius = "3000";
                break;
            case "5":
                radius = "5000";
                break;
        }
        preferanceRadius = Integer.parseInt(radius);

    }

    // menu = the activity options menu:
    public boolean onCreateOptionsMenu(Menu menu) {

        // Get a component which can inflate a menu:
        MenuInflater menuInflater = getMenuInflater();

        // Inflate the menu_main xml file which we built:
        menuInflater.inflate(R.menu.menu_main, menu);

        // Return:
        return true;
    }

    //which option menu item selected:
    public boolean onOptionsItemSelected(MenuItem item) {
        int res_id = item.getItemId();

        switch (res_id) {
            //open the Favorites fragment:
            case R.id.action_favorite:

                editTextSearch.setVisibility(View.GONE);
                goToFavorites();

                break;

            //open the Settings Activity screen:
            case R.id.action_settings:

                Intent intent = new Intent(this, Settings.class);
                startActivityForResult(intent, Globals.DEFAULT_NUMBER);

                break;

            //User insert searching ward:
            case R.id.action_search:

                loadSettings();

                Toast.makeText(this, getString(R.string.radius_is_title) + "\n" + "" + preferanceRadius, Toast.LENGTH_LONG).show();
                editTextSearch.setVisibility(View.VISIBLE);

                String search = editTextSearch.getText().toString();

                //If user puts space at the end or the start of his searching wards :
                if (search.endsWith(" ") || search.startsWith(" ")) {
                    search = search.trim();
                }
                //If user uses more then one words :
                search = search.replaceAll(" ", "+");

                String url = Globals.GOOGLEMAP_API_SEARCH_BY_TITLE +
                        userLatitude + "," + userLongitude + Globals.SEARCH_BY_RADIUS + preferanceRadius + Globals.SEARCH_BY_NAME + search + Globals.GOOGLEMAP_KEY;
                TextDownloader textDownloader = new TextDownloader(this);

                textDownloader.execute(url);
                Toast.makeText(this, getString(R.string.searching_message), Toast.LENGTH_LONG).show();

                break;

            //Exit from app:
            case R.id.action_exit:

                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Globals.DEFAULT_NUMBER && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            boolean clearFavorite = extras.getBoolean(getString(R.string.clear_favorite_boolean_title));
            if (clearFavorite) {
                favoriteFragment.refreshTheFavoriteList();
            }
        }
    }

    //Shows progressDialog:
    public void onAboutToBegin() {
        allPlaces.clear();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.searching_message);
        progressDialog.setIcon(R.drawable.download_icon);
        progressDialog.show();

    }

    //When thar are result for the user search:
    public void onSuccess(String downloadedText) {
        progressDialog.dismiss();

        try {
            JSONObject jsonSearchObject = new JSONObject(downloadedText);

            JSONArray jsonPlaceArray = jsonSearchObject.getJSONArray("results");

            for (int i = Globals.START_VALUE; i < jsonPlaceArray.length(); i++) {

                JSONObject jsonPlaceObject = jsonPlaceArray.getJSONObject(i);


                JSONObject geometry = jsonPlaceObject.getJSONObject("geometry");
                String placeName = jsonPlaceObject.getString("name");
                String placeAddress = jsonPlaceObject.getString("vicinity");

                JSONObject location = geometry.getJSONObject("location");
                double placeLatitude = location.getDouble("lat");
                double placeLongitude = location.getDouble("lng");

                if (jsonPlaceObject.has("photos")) {

                    JSONArray photosArray = jsonPlaceObject.getJSONArray("photos");

                    for (int j = Globals.START_VALUE; j < photosArray.length(); j++) {

                        JSONObject photoObject = photosArray.getJSONObject(Globals.START_VALUE);

                        picture = photoObject.getString("photo_reference");

                    }

                } else {
                    picture = jsonPlaceObject.getString("icon");
                }

                // function that calculating the distance from the user location to the place:
                Location userLocation = new Location(getString(R.string.user_location_point_a));
                userLocation.setLatitude(userLatitude);
                userLocation.setLongitude(userLongitude);
                Location placeLocation = new Location(getString(R.string.place_location_point_b));
                placeLocation.setLatitude(placeLatitude);
                placeLocation.setLongitude(placeLongitude);
                double distance = userLocation.distanceTo(placeLocation);

                distance = distance / Globals.DISTANCE_DIVIDER;

                distance = Math.floor(distance * Globals.DISTANCE_DIVIDER_MATH_FLO0R) / Globals.DISTANCE_DIVIDER_MATH_FLO0R;

                Place place = new Place(placeName, placeAddress, picture, placeLatitude, placeLongitude, distance);

                allPlaces.add(place);


            }
            Repository.lastSearchedPlaces = allPlaces;
            listFragment.refreshTheList();


        } catch (JSONException ex) {

            Toast.makeText(this, getString(R.string.error_title) + ex.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void onError(int httpStatusCode, String errorMessage) {
        Toast.makeText(this, getString(R.string.error_no_internet_message), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    //switching the recycler view fragment with the favorites fragment.
    public void goToFavorites() {
        Repository.isFavorite = true;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayoutContainer, favoriteFragment)
                .addToBackStack(null)
                .commit();

    }

    //when a place in the list is clicked:
    public void itemPlaceClicked(Place place) {
        Repository.lastPresentedPlaces = place;
        if (Utilities.isPortrait(this)) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutContainer, mapFragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            mapFragment.showPlaceOnMap(place);
        }
    }

    //on a long click on the place:
    public void itemPlaceLongClick(Place place) {
        this.place = place;

        String title;
        String message;
        String buttonOK;
        if (Repository.isFavorite) {
            title = getString(R.string.confirm_delete_title);
            message = getString(R.string.delete_question);
            buttonOK = getString(R.string.ok_option);
        } else {
            title = getString(R.string.add_fav_title);
            message = getString(R.string.add_fav_question);
            buttonOK = getString(R.string.ok_option);
        }

        AlertDialog dialog = new AlertDialog.Builder(this).create();

        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setButton(Dialog.BUTTON_POSITIVE, buttonOK, this);
        dialog.setButton(Dialog.BUTTON_NEGATIVE, getString(R.string.cancel_button), this);

        dialog.setCancelable(false);
        dialog.show();
    }

    //Share function:
    public void sharePlace(Place place) {

        Toast.makeText(this, place.getPlaceName() + "\n" + this.getString(R.string.share_message_head), Toast.LENGTH_LONG).show();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_message_head));
        shareIntent.putExtra(Intent.EXTRA_TEXT, place.getPlaceName() + "\n\n" + place.getPlaceAddress());
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_menu)));


    }

    // After the dialog figure the action -> act accordingly :
    public void onClick(DialogInterface dialog, int which) {

        if (which == Dialog.BUTTON_POSITIVE) {
            if (Repository.isFavorite) {
                placeDatabase.deletePlace(place);
                favoriteFragment.refreshTheFavoriteList();
            } else {
                placeDatabase.addFavoritePlace(place);
            }
        }
    }

    public void onLocationChanged(Location location) {
        userLatitude = location.getLatitude();
        userLongitude = location.getLongitude();

    }
}
