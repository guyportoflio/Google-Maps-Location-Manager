package com.example.presly.Places;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class PlaceDatabase extends SQLiteOpenHelper {

    private SQLiteDatabase sqLiteDatabase;
    private ContentValues contentValues;

    public PlaceDatabase(Context context) {
        super(context, "PlaceDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE Places(id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT NOT NULL," +
                "address TEXT NOT NULL, latitude DOUBLE NOT NULL, longitude DOUBLE NOT NULL," +
                "image TEXT NOT NULL, distance DOUBLE NOT NULL)";
        db.execSQL(sql);

        String sqli = "CREATE TABLE Favorites(id INTEGER PRIMARY KEY AUTOINCREMENT , name TEXT NOT NULL," +
                "address TEXT NOT NULL, latitude DOUBLE NOT NULL, longitude DOUBLE NOT NULL," +
                "image TEXT NOT NULL)";
        db.execSQL(sqli);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS Places";
        db.execSQL(sql);
        onCreate(db);

        String sqli = "DROP TABLE IF EXISTS Favorites";
        db.execSQL(sqli);
        onCreate(db);

    }

    public void open() {
        sqLiteDatabase = getWritableDatabase();
    }

    public void close() {
        super.close();
    }

    //Add place
    public void addPlace(Place place) {
        open();

        //Get the place details
        contentValues = new ContentValues();
        contentValues.put("name", place.getPlaceName());
        contentValues.put("address", place.getPlaceAddress());
        contentValues.put("image", place.getPlaceImage());
        contentValues.put("latitude", place.getLatitude());
        contentValues.put("longitude", place.getLongitude());
        contentValues.put("distance", place.getPlaceDistance());

        long id = sqLiteDatabase.insert("Places", null, contentValues);

        place.setPlaceId((int) id);

        close();
    }

    public void addFavoritePlace(Place place) {

        open();

        contentValues = new ContentValues();
        contentValues.put("name", place.getPlaceName());
        contentValues.put("address", place.getPlaceAddress());
        contentValues.put("latitude", place.getLatitude());
        contentValues.put("longitude", place.getLongitude());
        contentValues.put("image", place.getPlaceImage());

        long id = sqLiteDatabase.insert("Favorites", null, contentValues);

        place.setPlaceId((int) id);

        close();

    }

    public void deletePlace(Place place) {
        @SuppressLint("DefaultLocale") String sql = String.format("DELETE FROM Favorites WHERE id=%d", place.getPlaceId());
        open();
        sqLiteDatabase.execSQL(sql);
        close();
    }

    public void deleteAllSearchedPlaces() {
        String sql = "DELETE FROM Places";
        open();
        sqLiteDatabase.execSQL(sql);
        close();
    }

    //Delete all favorites places from the list in the database:
    public void deleteAllFavoritePlaces() {
        String sql = "DELETE FROM Favorites";
        open();
        sqLiteDatabase.execSQL(sql);
        close();
    }

    //making an array
    public ArrayList<Place> getAllLastSearchedPlaces() {
        ArrayList<Place> lastSearchedPlaces = new ArrayList<>();
        open();
        Cursor cursor = sqLiteDatabase.query("Places", null, null, null, null, null, null);

        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int addressIndex = cursor.getColumnIndex("address");
        int imageIndex = cursor.getColumnIndex("image");
        int latitudeIndex = cursor.getColumnIndex("latitude");
        int longitudeIndex = cursor.getColumnIndex("longitude");
        int distanceIndex = cursor.getColumnIndex("distance");

        while (cursor.moveToNext()) {

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String address = cursor.getString(addressIndex);
            String image = cursor.getString(imageIndex);
            double latitude = cursor.getDouble(latitudeIndex);
            double longitude = cursor.getDouble(longitudeIndex);
            double distance = cursor.getDouble(distanceIndex);

            Place place = new Place(id, name, address, image, latitude, longitude, distance);

            lastSearchedPlaces.add(place);
        }
        cursor.close();

        close();

        return lastSearchedPlaces;

    }


    //Create array
    public ArrayList<Place> getAllFavoritesPlaces() {
        ArrayList<Place> allFavoritesPlaces = new ArrayList<>();
        open();
        Cursor cursor = sqLiteDatabase.query("Favorites", null, null, null, null, null, null);

        int idIndex = cursor.getColumnIndex("id");
        int nameIndex = cursor.getColumnIndex("name");
        int addressIndex = cursor.getColumnIndex("address");
        int imageIndex = cursor.getColumnIndex("image");
        int latitudeIndex = cursor.getColumnIndex("latitude");
        int longitudeIndex = cursor.getColumnIndex("longitude");


        while (cursor.moveToNext()) {

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String address = cursor.getString(addressIndex);
            String image = cursor.getString(imageIndex);
            double latitude = cursor.getDouble(latitudeIndex);
            double longitude = cursor.getDouble(longitudeIndex);

            Place place = new Place(id, name, address, image, latitude, longitude);

            allFavoritesPlaces.add(place);
        }
        cursor.close();

        close();

        return allFavoritesPlaces;

    }

}
