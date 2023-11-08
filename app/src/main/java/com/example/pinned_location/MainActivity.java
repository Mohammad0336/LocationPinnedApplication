package com.example.pinned_location;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listViewLocations; // Simple ListView

    // Data
    private List<Location> listLocations;
    private List<Location> filteredLocations; // List to store filtered locations
    private LocationAdapter adapter;
    private final DataBaseHelper databaseHelper = new DataBaseHelper(MainActivity.this); // Database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        SearchView searchView = findViewById(R.id.SearchBar);
        FloatingActionButton fabAdd = findViewById(R.id.floatingActionButton);
        listViewLocations = findViewById(R.id.listView);

        // Load all locations from the database
        listLocations = databaseHelper.getTableData();

        // Initialize the LocationAdapter with all locations
        initializeLocationAdapter(listLocations);

        // Set the adapter for the list view
        listViewLocations.setAdapter(adapter);

        // Add Location button click listener
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddLocation();
            }
        });

        // SearchView text change listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLocations(newText);
                return false;
            }
        });
    }

    // Initialize the LocationAdapter with a list of locations
    private void initializeLocationAdapter(List<Location> locations) {
        adapter = new LocationAdapter(this, locations, this::navigateToEditLocation, this::deleteLocation);
    }

    // Navigate to the AddLocation activity
    private void navigateToAddLocation() {
        Intent intent = new Intent(MainActivity.this, AddLocation.class);
        startActivity(intent);
    }

    // Navigate to the EditLocation activity with location details
    private void navigateToEditLocation(Location location) {
        Intent intent = new Intent(MainActivity.this, EditLocation.class);
        intent.putExtra("location_id", location.getId());
        intent.putExtra("location_address", location.getAddress());
        intent.putExtra("location_latitude", location.getLatitude());
        intent.putExtra("location_longitude", location.getLongitude());
        startActivity(intent);
    }

    // Delete a location and refresh the adapter and list
    private void deleteLocation(Location location) {
        int id = location.getId();

        if (id != -1) {
            // Remove the deleted location from the database
            databaseHelper.deleteLocation(id);

            // Remove the deleted location from the original list
            listLocations.remove(location);
            Toast.makeText(MainActivity.this, "Location removed successfully", Toast.LENGTH_SHORT).show();

            // Remove the deleted location from the filtered list
            if (filteredLocations != null) {
                filteredLocations.remove(location);
                Toast.makeText(MainActivity.this, "Location removed successfully", Toast.LENGTH_SHORT).show();
            }

            // Notify the adapter with the filtered or unfiltered data, depending on the state
            if (filteredLocations == null || filteredLocations.isEmpty()) {
                listViewLocations.setAdapter(adapter);
            } else {
                adapter = new LocationAdapter(this, filteredLocations, this::navigateToEditLocation, this::deleteLocation);
                listViewLocations.setAdapter(adapter);
            }
        }
    }

    // Filter locations based on the query and refresh the adapter
    private void filterLocations(String query) {
        filteredLocations = new ArrayList<>();

        for (Location location : listLocations) {
            if (location.getAddress().toLowerCase().contains(query.toLowerCase())) {
                filteredLocations.add(location);
            }
        }

        // Notify the adapter with the filtered data
        adapter = new LocationAdapter(this, filteredLocations, this::navigateToEditLocation, this::deleteLocation);
        listViewLocations.setAdapter(adapter);
    }
}
