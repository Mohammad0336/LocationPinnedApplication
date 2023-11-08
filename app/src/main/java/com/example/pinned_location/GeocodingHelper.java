package com.example.pinned_location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
public class GeocodingHelper {
    private final Geocoder geocoder;
    public GeocodingHelper (Context context) {
        geocoder = new Geocoder(context, Locale.getDefault());
    }
    // Function to read input file and use Geocoding to find 50 addresses input latitude and longitude pairs
    public String getAddressFromCoordinates(double latitude, double longitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0); // Get the first address line
            }
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }
}
