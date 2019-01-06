package com.example.jag.projetenseirb.DirectionHelper;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jag.projetenseirb.R;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class  fragmentRoute extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    GoogleMap map;
    Button btnGetDirection;
    MarkerOptions place1, place2;
    Polyline currentPolyline;


    private String getUrl(LatLng origin, LatLng dest, String directionMode){
        //origin of route
        String str_origin = "origin=" + origin.latitude + origin.longitude + " , " + origin.longitude;

        //Destination of route
        String str_dest = "destination=" + origin.latitude + origin.longitude + " , " + origin.longitude;

        //Mode
        String mode = "mode=" + directionMode;

        //Building the parameters to the web service
        String parameters = str_origin + "&" +str_dest + "&" + mode;

        //output format
        String output =" json";

        //Building the url to the web service
        String url =  "https://maps.googleapis.com/maps/api/directions/" + output + "?" +parameters + "&key" + getString( R.string.google_maps_key ) ;
        return url;
    }

    @Override
    public void  onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_fragment_route );
        btnGetDirection = findViewById( R.id.btnGetDirection );
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById( R.id.mapFrag );
        mapFragment.getMapAsync( this );

        place1 = new MarkerOptions().position( new LatLng( 27.658143 , 85.3199503 ) ).title( "Location1" );
        place2 = new MarkerOptions().position( new LatLng( 27.667491, 85.3208583 ) ).title( "Location2" );

        btnGetDirection.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getUrl(place1.getPosition(),place2.getPosition(),"driving");
                new FetchURL(fragmentRoute.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");
            }
        } );


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline( (PolylineOptions) values[0]);
    }
}
