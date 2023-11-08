package com.example.pinned_location;
public class Location {
    private int id;
    private String address;
    private double latitude, longitude;
    // Constructor to initialize Location object with id, address, latitude, and longitude values
    public Location(int id, String address, double latitude, double longitude) {
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    @Override
    public String toString() {
        // Return a string representation of the Location object
        return "Location{" + "id=" + id + ", address='" + address + '\''
                + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }
    public int getId() {return id;}
    public String getAddress() {return address;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
}

