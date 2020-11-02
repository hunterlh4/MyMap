package com.miempresa.mymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.icu.text.DecimalFormat;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ResourceBusyException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.internal.IStreetViewPanoramaFragmentDelegate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "Estilo del mapa";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private LatLng japon, alemania, italia, francia;
    double distancia;
    private double miLatitud = 0, miLongitud = 0;
    private double Latitud2 = 0, Longitud2 = 0;
    private LocationManager mylocationManager;
    private Location milocacion;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        japon = new LatLng(35.680513, 139.769051);
        alemania = new LatLng(52.516934, 13.403190);
        italia = new LatLng(41.902609, 12.494847);
        francia = new LatLng(48.843489, 2.355331);





        String distancia;

        switch (item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.addMarker(new MarkerOptions().position(japon).title("Japon"));

                 distancia = CalcularDistancia("japon",miLatitud,miLongitud,japon.latitude,japon.longitude);


                Toast.makeText(getApplicationContext(), distancia, Toast.LENGTH_LONG).show();
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.addMarker(new MarkerOptions().position(italia).title("Italia"));
                 distancia = CalcularDistancia("italia",miLatitud,miLongitud,italia.latitude,italia.longitude);
                Toast.makeText(getApplicationContext(), distancia, Toast.LENGTH_LONG).show();
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mMap.addMarker(new MarkerOptions().position(alemania).title("Alemania"));
                distancia = CalcularDistancia("alemania",miLatitud,miLongitud,alemania.latitude,alemania.longitude);
                Toast.makeText(getApplicationContext(), distancia, Toast.LENGTH_LONG).show();
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mMap.addMarker(new MarkerOptions().position(francia).title("Francia"));
                distancia = CalcularDistancia("francia",miLatitud,miLongitud,francia.latitude,francia.longitude);
                Toast.makeText(getApplicationContext(), distancia, Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String CalcularDistancia(String pais,double lat1, double long1,double lat2,double long2) {
        int Radius=6371;//radius of earth in Km
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(long2-long1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        int km=(int) valueResult;
        double mt = (long)valueResult;
        String DistanciaEntre = "La distancia hasta " + pais+ " es de "+km +"Kilometros y "+mt+" metros ";
        return DistanciaEntre;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        mylocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        milocacion = mylocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        miLatitud = milocacion.getLatitude();
        miLongitud = milocacion.getLongitude();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoom = 16;

        try{
            boolean success = mMap.setMapStyle(MapStyleOptions
                    .loadRawResourceStyle(getApplicationContext(),R.raw.map_style));
            if(!success){
                Log.e(TAG,"Fallo al cargar estilo del mapa");
            }
        } catch (Resources.NotFoundException exception) {
            Log.e(TAG,"No es posible hallar el estilo. Error: ",exception);
        }

        // Add a marker in Sydney and move the camera
        LatLng tacna = new LatLng(miLatitud,miLongitud);
        mMap.addMarker(new MarkerOptions().position(tacna).title("Marker in Tacna"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tacna,zoom));
        GroundOverlayOptions iconOverlay = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.home))
                .position(tacna,100);
        mMap.addGroundOverlay(iconOverlay);
        setMapLongClick(mMap);
        setPoiClick(mMap);
        enableMyLocation();
        setInfoWindowClickToPanorama(mMap);
    }

    private void setMapLongClick(final GoogleMap map){
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault()
                        ,"Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);
                map.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .position(latLng)
                        .title(getString(R.string.app_name))
                        .snippet(snippet));



            }
        });
    }

    private void setPoiClick(final GoogleMap map){
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {
                Marker poiMarker = map.addMarker(new MarkerOptions()
                        .position(pointOfInterest.latLng)
                        .title(pointOfInterest.name));
                poiMarker.setTag("poi");
                poiMarker.showInfoWindow();
            }
        });
    }

    private void enableMyLocation(){
        if(ContextCompat.checkSelfPermission(getBaseContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this
                    ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                    ,REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode
            , @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_LOCATION_PERMISSION:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    enableMyLocation();
                }
                break;
        }
    }

    private void setInfoWindowClickToPanorama(GoogleMap map){
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker.getTag() == "poi"){
                    StreetViewPanoramaOptions options = new StreetViewPanoramaOptions()
                            .position(marker.getPosition());
                    SupportStreetViewPanoramaFragment streetViewPanoramaFragment =
                            SupportStreetViewPanoramaFragment.newInstance(options);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, streetViewPanoramaFragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
    }
}