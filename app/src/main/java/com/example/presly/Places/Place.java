package com.example.presly.Places;

import java.io.Serializable;


public class Place implements Serializable {

    private int placeId;
    private String placeName, placeAddress, placeImage;
    private double latitude, longitude, placeDistance;


    // constructor without id
    public Place(String placeName, String placeAddress, String placeImage, double latitude, double longitude, double placeDistance) {
        setPlaceName(placeName);
        setPlaceAddress(placeAddress);
        setPlaceImage(placeImage);
        setLatitude(latitude);
        setLongitude(longitude);
        setPlaceDistance(placeDistance);
    }

    // constructor with id and without distance
    public Place(int placeId, String placeName, String placeAddress, String placeImage, double latitude, double longitude) {
        setPlaceId(placeId);
        setPlaceName(placeName);
        setPlaceAddress(placeAddress);
        setPlaceImage(placeImage);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    // constructor with distance and id
    public Place(int placeId, String placeName, String placeAddress, String placeImage, double latitude, double longitude, double placeDistance) {
        setPlaceId(placeId);
        setPlaceName(placeName);
        setPlaceAddress(placeAddress);
        setPlaceImage(placeImage);
        setLatitude(latitude);
        setLongitude(longitude);
        setPlaceDistance(placeDistance);
    }

    //geters and seters:
    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public double getPlaceDistance() {
        return placeDistance;
    }

    public void setPlaceDistance(double placeDistance) {
        this.placeDistance = placeDistance;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
