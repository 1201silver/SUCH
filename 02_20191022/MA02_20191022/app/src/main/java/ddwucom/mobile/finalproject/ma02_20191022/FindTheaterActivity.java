package ddwucom.mobile.finalproject.ma02_20191022;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;


public class FindTheaterActivity extends AppCompatActivity implements OnMapReadyCallback {

    final static String TAG = "FindTheaterActivity";
    private final static int PERMISSION_REQ_CODE = 100;

    private LocationManager locManager;
    private String bestProvider;
    private Marker currentMarker;

    private GoogleMap mGoogleMap;
    private MarkerOptions options;
    private PlacesClient placesClient;

    double currentLat;
    double currentLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_theater);

        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        /*Passive 가 아닌 GPS 또는 Network provider 중 선택이 필요할 경우 기준 설정*/
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		criteria.setAltitudeRequired(false);
		criteria.setCostAllowed(false);
		bestProvider = locManager.getBestProvider(criteria, true);

//        bestProvider = LocationManager.GPS_PROVIDER;

        mapLoad();

        Places.initialize(getApplicationContext(), getString(R.string.googleApiKey));
        placesClient = Places.createClient(this);

        if(checkPermission()) {
            locationUpdate();
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFind:
                searchStart(PlaceType.MOVIE_THEATER);
                Toast.makeText(this, "find theater .. ", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void searchStart(String type) {
        new NRPlaces.Builder().listener(placesListener)
                .key(getResources().getString(R.string.googleApiKey))
                .latlng(currentLat, currentLng)
                .radius(2000)
                .type(type) // onClick -> searchStart 매개변수
                .build()
                .execute();
    }

    PlacesListener placesListener = new PlacesListener() {

        @Override
        public void onPlacesSuccess(final List<noman.googleplaces.Place> places) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (noman.googleplaces.Place place : places) {
                        options.title(place.getName());
                        options.position(new LatLng(place.getLatitude(), place.getLongitude()));
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                        Marker theaterMarker = mGoogleMap.addMarker(options);
                        theaterMarker.setTag(place.getPlaceId());

                        Log.d(TAG, place.getPlaceId()+ " : " +place.getName());
                    }
                }
            });
        }

        @Override
        public void onPlacesFailure(PlacesException e) { }

        @Override
        public void onPlacesStart() { }

        @Override
        public void onPlacesFinished() { }

    };

    private void locationUpdate() {
        if (checkPermission()) {
            locManager.requestLocationUpdates(bestProvider, 3000, 0, locListener);
        }
    }

    private Location getLastLocation() {
        Location lastLocation = null;

        if(checkPermission()) {
            lastLocation = locManager.getLastKnownLocation(bestProvider);
        }
        return lastLocation;
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*위치 조사 종료 - 반드시 추가!*/
        locManager.removeUpdates(locListener);
    }

    LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "locationListener: " +location);
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 16));

            currentMarker.setPosition(currentLoc);

            currentLat = location.getLatitude();
            currentLng = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mGoogleMap = googleMap;
        options = new MarkerOptions();

        if(checkPermission()) {
            mGoogleMap.setMyLocationEnabled(true);
        }

        // 기본 위치
        double latitude = Double.parseDouble(getResources().getString(R.string.init_lat));
        double longitude = Double.parseDouble(getResources().getString(R.string.init_lng));


        LatLng currentLoc;
        Location lastLoc = getLastLocation();
        if(lastLoc != null) {
            latitude = lastLoc.getLatitude();
            longitude = lastLoc.getLongitude();
            currentLoc = new LatLng(latitude, longitude);
        }
        else {
            currentLoc = new LatLng(latitude, longitude);
        }

        currentLat = latitude;
        currentLng = longitude;

        if (checkPermission()) {
            locationUpdate();
        }

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

        options.position(currentLoc);
        options.title("현재 위치");
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentMarker = mGoogleMap.addMarker(options);
        currentMarker.showInfoWindow();

        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
//                Toast.makeText(FindTheaterActivity.this, "Click!", Toast.LENGTH_SHORT).show();
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 13));
                return false;
            }
        });

        mGoogleMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                String msg = String.format("현재위치: (%.3f, %.3f)", location.getLatitude(), location.getLongitude());
                Toast.makeText(FindTheaterActivity.this, msg, Toast.LENGTH_SHORT).show();

            }
        });

        mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String placeId = marker.getTag().toString();
                getPlaceDetail(placeId);
            }
        });
    }

    private void getPlaceDetail(String placeId) {
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.ADDRESS, Place.Field.PHONE_NUMBER);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();

        placesClient.fetchPlace(request).addOnSuccessListener(
                new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse response) {
                        Place place = response.getPlace();

                        final ConstraintLayout theaterLayout
                                = (ConstraintLayout) View.inflate(FindTheaterActivity.this, R.layout.theater_diaolg_layout, null);

                        TextView tvAddress = theaterLayout.findViewById(R.id.tvAddress);
                        TextView tvPhoneNum = theaterLayout.findViewById(R.id.tvPhoneNum);

                        if(place.getAddress() == null) {
                            tvAddress.setText("주소 정보가 존재하지 않습니다.");
                        }
                        else {
                            tvAddress.setText(place.getAddress());
                        }

                        if(place.getPhoneNumber() == null) {
                            tvPhoneNum.setText("전화번호가 존재하지 않습니다.");
                        }
                        else {
                            tvPhoneNum.setText(place.getPhoneNumber());
                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(FindTheaterActivity.this);

                        builder.setTitle(place.getName())
                                .setView(theaterLayout)
                                .setIcon(R.mipmap.ic_launcher_movie)
                                .setPositiveButton("확인", null)
                                .setCancelable(false)
                                .show();


                        Log.d(TAG,"Place found: " +place.getName());
                        Log.d(TAG, "Phone Number: " +place.getPhoneNumber());
                        Log.d(TAG, "Address: " +place.getAddress());
                    }
                }
        ).addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();

                            Log.e(TAG, "Place not found: " +statusCode + " " +e.getMessage());
                        }
                    }
                }
        );
    }

    private void mapLoad() {
//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(FindTheaterActivity.this);
    }

    /* 필요 permission 요청 */
    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQ_CODE);
                return false;
            } else
                return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션을 획득하였을 경우 맵 로딩 실행
                mapLoad();
            } else {
                // 퍼미션 미획득 시 액티비티 종료
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}