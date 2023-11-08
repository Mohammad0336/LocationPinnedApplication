package com.example.pinned_location;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DataBaseHelper";
    private static final String TABLE_NAME = "Location"; // Name of table
    private static final String COLUMN_ID = "id"; // Column name for ID
    public static final String COLUMN_ADDRESS = "address"; //Column name for address
    public static final String COLUMN_LATITUDE = "latitude"; //Column name for latitude
    public static final String COLUMN_LONGITUDE = "longitude"; //Column name for longitude

    private Context context;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "location_table.db", null, 2);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL);";

        db.execSQL(createTableSQL);

        // Open and read the text file containing 50 latitude and longitude pairs
        try {
            InputStream inputStream = context.getResources().openRawResource(R.raw.data);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            GeocodingHelper geocodingHelper = new GeocodingHelper(context);

            while ((line = reader.readLine()) != null) {
                String[] coordinates = line.split(",");
                if (coordinates.length == 2) {
                    double latitude = Double.parseDouble(coordinates[0].trim());
                    double longitude = Double.parseDouble(coordinates[1].trim());

                    // Get the address for the coordinates
                    String address = geocodingHelper.getAddressFromCoordinates(latitude, longitude);

                    if (address != null) {
                        // Insert the data into the database
                        ContentValues values = new ContentValues();
                        values.put(COLUMN_ADDRESS, address);
                        values.put(COLUMN_LATITUDE, latitude);
                        values.put(COLUMN_LONGITUDE, longitude);

                        long newRowId = db.insert(TABLE_NAME, null, values);

                        if (newRowId == -1) {
                            Log.e(TAG, "Error inserting data into the database.");
                        }
                    } else {
                        Log.e(TAG, "Geocoding failed for latitude " + latitude + " and longitude " + longitude);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database schema updates when the version number changes
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public List<Location> getTableData() {
        List<Location> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String address = cursor.getString(1);
                double latitude = cursor.getDouble(2);
                double longitude = cursor.getDouble(3);
                Location newLocation = new Location(id, address, latitude, longitude);
                returnList.add(newLocation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public void addLocation(String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_LATITUDE, latitude);
        contentValues.put(COLUMN_LONGITUDE, longitude);

        long newRowId = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        if (newRowId != -1) {
            Log.e(TAG, "Adding location successfully.");
        } else {
            Log.e(TAG, "Error adding location.");
        }
    }

    public void deleteLocation(int locationId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = {String.valueOf(locationId)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void updateLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ADDRESS, location.getAddress());
        contentValues.put(COLUMN_LATITUDE, location.getLatitude());
        contentValues.put(COLUMN_LONGITUDE, location.getLongitude());

        // Update the database with the new values for the specified location
        int rowsAffected = db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?",
                new String[]{String.valueOf(location.getId())});

        db.close();

        if (rowsAffected > 0) {
            Log.d(TAG, "Location updated successfully.");
        } else {
            Log.e(TAG, "Error updating location.");
        }
    }


}
