package com.example.taxiappdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class OrderFragment extends Fragment {

    LatLng newLang;
    GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        EditText getLocation = view.findViewById(R.id.location);
        TextView greet = view.findViewById(R.id.greet);
        ImageView forward = view.findViewById(R.id.btnForward);
        getLocation.clearFocus();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);


        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }

                        googleMap.setMyLocationEnabled(true);

                        MarkerOptions markerOptions = new MarkerOptions();


                        markerOptions.position(latLng);

                        markerOptions.title(latLng.latitude + ";" +latLng.longitude);



                        googleMap.clear();

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));

                        googleMap.addMarker(markerOptions);
                        
                    }
                });
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getLocation.getText().toString().toLowerCase().equals("sofia center")){
                    getLocation.setText("София");
                    newLang = new LatLng(42.698334,23.319941);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLang,10));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(newLang);
                    mMap.addMarker(markerOptions);
                }
                if(getLocation.getText().toString().toLowerCase().equals("nevski")){
                    getLocation.setText("Александър Невски");
                    newLang = new LatLng(42.695808,23.332794);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLang,100));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(newLang);
                    mMap.addMarker(markerOptions);
                }
            }
        });

        return view;
    }





    }
