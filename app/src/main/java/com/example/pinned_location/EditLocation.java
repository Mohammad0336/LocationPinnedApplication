package com.example.pinned_location;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EditLocation extends AppCompatActivity {
    EditText editTextAddress, editTextLatitude, editTextLongitude;
    Button buttonUpdate;
    private final DataBaseHelper databaseHelper = new DataBaseHelper(EditLocation.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_location);

        // initializing address for view data
        editTextAddress = findViewById(R.id.u_ev_addr);
        editTextLatitude = findViewById(R.id.u_ev_lat);
        editTextLongitude = findViewById(R.id.u_ev_long);
        buttonUpdate = findViewById(R.id.update_button);

        // intent passed from MainActivity to parse data into the inputs of the views
        Intent intent = getIntent();

        // Get the location data from the selected location
        int id = intent.getIntExtra("location_id", -1);
        String address = intent.getStringExtra("location_address");
        double latitude = intent.getDoubleExtra("location_latitude", -1);
        double longitude = intent.getDoubleExtra("location_longitude", -1);

        // Set the fields in the edit screen to the selected location
        editTextAddress.setText(address);
        editTextLatitude.setText(String.valueOf(latitude));
        editTextLongitude.setText(String.valueOf(longitude));

        // Event listener for the update button. This will update the location data and return home on completion.
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // parsing inputs into strings
                String address = editTextAddress.getText().toString();
                String latitudeString = editTextLatitude.getText().toString();
                String longitudeString = editTextLongitude.getText().toString();

                Location location;

                // if statement to check validity of strings
                if (!address.isEmpty() && !latitudeString.isEmpty() && !longitudeString.isEmpty()) {
                    double latitude = Double.parseDouble(latitudeString);
                    double longitude = Double.parseDouble(longitudeString);

                    // Input of edit into the database
                    location = new Location(id, address, latitude, longitude);
                    databaseHelper.updateLocation(location);
                    Toast.makeText(EditLocation.this, "Location updated successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditLocation.this, "Location not updated successfully", Toast.LENGTH_SHORT).show();
                }

                // return home
                Intent intent = new Intent(EditLocation.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    // Simple intent used to redirect from the current view to main page
    public void returnHome(View view) {
        Intent intent = new Intent(EditLocation.this, MainActivity.class);
        startActivity(intent);
    }
}
