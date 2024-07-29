package com.example.practicev;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // UI elements
    private FrameLayout mapLayout;
    private GoogleMap gMap;
    private Location currentLocation;
    private Marker marker;
    private FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Initialize views
        mapLayout = findViewById(R.id.map);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();

        // Initialize FusedLocationProviderClient
        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        // Set search view listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Get the query from the search view
                String location = searchView.getQuery().toString();
                if (location.isEmpty()) {
                    // Notify the user if the location is not found
                    Toast.makeText(MapsActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    // Use Geocoder to get the location coordinates from the location name
                    Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(location, 1);
                        if (!addressList.isEmpty()) {
                            // Get the first address from the address list
                            Address address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            if (marker != null) {
                                // Remove the previous marker if it exists
                                marker.remove();
                            }
                            // Create a new marker with the location's coordinates
                            MarkerOptions markerOptions = new MarkerOptions()
                                    .position(latLng)
                                    .title(location)
                                    .snippet("Latitude: " + address.getLatitude() + ", Longitude: " + address.getLongitude())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            // Move the camera to the new location and add the marker to the map
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
                            gMap.animateCamera(cameraUpdate);
                            marker = gMap.addMarker(markerOptions);
                        } else {
                            // Notify the user if the location is not found
                            Toast.makeText(MapsActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // Get the current location of the device
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        // Get the last known location using FusedLocationProviderClient
        Task<Location> task = fusedClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Save the current location and initialize the map
                    currentLocation = location;
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    if (supportMapFragment != null) {
                        supportMapFragment.getMapAsync(MapsActivity.this);
                    }
                } else {
                    // Notify the user if the location is not available
                    Toast.makeText(MapsActivity.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Called when the map is ready to be used
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
        if (currentLocation != null) {
            // Set the current location marker
            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .title("My Current Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            // Add the marker to the map and move the camera to the current location
            gMap.addMarker(markerOptions);
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        } else {
            // Notify the user if the location is not available
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the location
                getLocation();
            } else {
                // Notify the user if the permission is denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
