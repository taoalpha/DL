package com.example.android.location;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private Button mSubmitButton;
    private SQLiteLocation mSQLiteLocation;
    private SQLiteDatabase mSQLDB;
    private SimpleCursorAdapter mSQLCurSorAdapter;
    private Cursor mSQLCursor;
    private static final String tag = "SQLactivity";

    private GoogleApiClient mGoogleApiClient;
    private static final int LOCATION_PERMISSION_RESULT = 23;
    private Location mLocation;
    private LocationRequest mLocationRequest;
    private LocationListener mLocationListener;
    private String mLong = "-123.2";
    private String mLat = "44.5";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //construct SQLite database
        mSQLiteLocation = new SQLiteLocation(this);
        mSQLDB = mSQLiteLocation.getWritableDatabase();

        //build a client
        setUpClient();
        mLocationRequest = LocationRequest.create();
        //set up accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // if location is not null, then change the value of mLong and mLat
                if (location != null) {
                    mLong = String.valueOf(location.getLongitude());
                    mLat = String.valueOf(location.getLatitude());
                }
            }
        };
        mSubmitButton = (Button) findViewById(R.id.submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // insert value into table
                if (mSQLDB != null) {
                    ContentValues testValues = new ContentValues();
                    testValues.put(DBcontract.locationTable.COLUMN_NAME_NAME, ((EditText) findViewById(R.id.inputname)).getText().toString());
                    testValues.put(DBcontract.locationTable.COLUMN_NAME_latitude, mLat);
                    testValues.put(DBcontract.locationTable.COLUMN_NAME_longitude, mLong);
                    mSQLDB.insert(DBcontract.locationTable.TABLE_NAME, null, testValues);
                    populateTable();

                } else {
                    Log.d(tag, "unable to access database for writing");
                }
            }

        });
        populateTable();
    }



    private void populateTable() {
        if (mSQLDB != null) {
            try {
                if (mSQLCurSorAdapter != null && mSQLCurSorAdapter.getCursor() != null) {
                    if (!mSQLCurSorAdapter.getCursor().isClosed()) {
                        mSQLCurSorAdapter.getCursor().close();
                    }
                }
                //query data from table
                mSQLCursor = mSQLDB.query(DBcontract.locationTable.TABLE_NAME,
                        new String[]{DBcontract.locationTable._ID, DBcontract.locationTable.COLUMN_NAME_NAME,
                                DBcontract.locationTable.COLUMN_NAME_latitude, DBcontract.locationTable.COLUMN_NAME_longitude},
                        null,
                        null,
                        null,
                        null,
                        null);
                ListView locationLS = (ListView) findViewById(R.id.ls);
                // construct an adapter to provide data and set how to display data into specific locations.
                mSQLCurSorAdapter = new SimpleCursorAdapter(this,
                        R.layout.listitem,
                        mSQLCursor,
                        new String[]{DBcontract.locationTable.COLUMN_NAME_NAME, DBcontract.locationTable.COLUMN_NAME_latitude, DBcontract.locationTable.COLUMN_NAME_longitude},
                        new int[]{R.id.displayname, R.id.latitude, R.id.longitude},
                        0);
                locationLS.setAdapter(mSQLCurSorAdapter);
            } catch (Exception e) {
                Log.d(tag, "Error loading data from database");
            }
        }
    }

    // set up a googleapi client and connect it to service.
    public void setUpClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_RESULT) {
            if (grantResults.length > 0) {
                updateLocation();
            }
        }
    }

    public void updateLocation() {
        // if no permission, just return
        if (!checkPermission()) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // if have permission to location, request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
    }

    // this function is used to check whether there is a permission to location
    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onConnected(@NonNull Bundle connectionHint) {
        // if no permission, request for a permission
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_RESULT);
            return;
        }
        // if there ia a permission, then update location.
        updateLocation();
    }

    @Override
    public void onConnectionSuspended(int cause) {

    }

    @Override
    public void onConnectionFailed(@NonNull  ConnectionResult result) {

    }
}



