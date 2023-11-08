package com.example.pinned_location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> {

    private final EditClickListener editClickListener;
    TextView textViewAddress, textViewLatitude, textViewLongitude;
    FloatingActionButton fabRemove, fabEdit;

    // Interface for handling edit click events
    public interface EditClickListener {
        void onEdit(Location location);
    }

    private final DeleteClickListener deleteClickListener;

    // Interface for handling delete click events
    public interface DeleteClickListener {
        void onDelete(Location location);
    }

    // Constructor for the LocationAdapter
    public LocationAdapter(Context context, List<Location> locations, EditClickListener editClickListener, DeleteClickListener deleteClickListener) {
        super(context, 0, locations);
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }

    @Override
    public View getView(int position, View listView, ViewGroup parent) {
        if (listView == null) {
            // Inflate the list item layout if not already created
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Location location = getItem(position);

        // Find and set the address TextView
        textViewAddress = listView.findViewById(R.id.textViewAddress);
        textViewAddress.setText(location.getAddress());

        // Find and set the latitude TextView
        textViewLatitude = listView.findViewById(R.id.textViewLatitude);
        textViewLatitude.setText("Latitude: " + String.valueOf(location.getLatitude()));

        // Find and set the longitude TextView
        textViewLongitude = listView.findViewById(R.id.textViewLongitude);
        textViewLongitude.setText("Longitude: " + String.valueOf(location.getLongitude()));

        // Find and set the remove and edit FABs
        fabRemove = listView.findViewById(R.id.remove_fab);
        fabEdit = listView.findViewById(R.id.edit_fab);

        // Set a click listener for the edit FAB
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editClickListener.onEdit(location);
            }
        });

        // Set a click listener for the remove FAB
        fabRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteClickListener.onDelete(location);
            }
        });

        return listView;
    }
}
