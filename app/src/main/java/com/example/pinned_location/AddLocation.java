package com.example.pinned_location;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddLocation extends AppCompatActivity {
    EditText editTextAddress, editTextLatitude, editTextLongitude;
    private final DataBaseHelper databaseHelper = new DataBaseHelper(AddLocation.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_location);

        // initializing address for view data
        editTextAddress = findViewById(R.id.ev_addr);
        editTextLatitude = findViewById(R.id.ev_lat);
        editTextLongitude = findViewById(R.id.ev_long);

    }

    public void addLocation(View view) {
        // Parsing user input in the editTexts into strings
        String address = editTextAddress.getText().toString();
        String latitudeString = editTextLatitude.getText().toString();
        String longitudeString = editTextLongitude.getText().toString();

        // if input is valid meaning not null, add to DB
        if (!address.isEmpty() && !latitudeString.isEmpty() && !longitudeString.isEmpty()) {
            double latitude = Double.parseDouble(latitudeString);
            double longitude = Double.parseDouble(longitudeString);
            databaseHelper.addLocation(address, latitude, longitude);
            Toast.makeText(AddLocation.this, "Location added successfully", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(AddLocation.this, "Location not added successfully", Toast.LENGTH_SHORT).show();
        }
        // Return to home screen
        Intent intent = new Intent(AddLocation.this, MainActivity.class);
        startActivity(intent);
    }

    // Return to home screen
    public void returnHome(View view) {
        Intent intent = new Intent(AddLocation.this, MainActivity.class);
        startActivity(intent);
    }
}
