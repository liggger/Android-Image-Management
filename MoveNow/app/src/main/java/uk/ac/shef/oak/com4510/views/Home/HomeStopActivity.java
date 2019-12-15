package uk.ac.shef.oak.com4510.views.Home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;

public class HomeStopActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int ACCESS_FINE_LOCATION = 123;
    private static final int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};

    private EasyImage easyImage;
    private ImageViewModel imageViewModel;
    private PathViewModel pathViewModel;

    private GoogleMap mMap;
    private Image image;
    private Path imagePath;
    private int pathId;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private Activity activity;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private ArrayList<LatLng> route = new ArrayList<>();
    private ArrayList<Double> currentLatitudeList = new ArrayList<>();
    private ArrayList<Double> currentLongitudeList = new ArrayList<>();
    private Polyline polyline;
    private Pressure_and_Temperature pressure_and_temperature = new Pressure_and_Temperature();
    private String temperature, pressure;
    private Chronometer chronometer;
    private SimpleDateFormat sdf;
    private Calendar calendar;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stop);
//        getSupportActionBar().hide();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pressure_and_temperature.initPressure_and_Temperature(getApplicationContext());
        pressure_and_temperature.starttemperatureSensor();
        pressure_and_temperature.startpressureSensor();

        activity = this;

        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        pathViewModel = ViewModelProviders.of(this).get(PathViewModel.class);

        pathViewModel.getOnePath().observe(this, new Observer<Path>() {
            @Override
            public void onChanged(Path path) {
                imagePath = path;
                pathId = path.getPath_id();
            }
        });

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        sdf = new SimpleDateFormat("HH:mm:ss");
        chronometer.setFormat("00:%s");
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer ch) {
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                if (elapsedMillis > 3600000L) {
                    chronometer.setFormat("0%s");
                } else {
                    chronometer.setFormat("00:%s");

                }
            }
        });
        chronometer.start();

        if (allPermissionsGranted()) {
            initEasyImage(); //start camera if permission has been granted by user
            startLocationUpdates();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        FloatingActionButton fabGallery = findViewById(R.id.fab_gallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyImage.openGallery(getActivity());
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation
                        .getLongitude()))
                        .title(mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude()));

                pressure = pressure_and_temperature.getPressure();
                temperature = pressure_and_temperature.getTemperature();
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    date = sdf.parse(sdf.format(calendar.getInstance().getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                image = new Image(pathId, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), date);


//                EasyImage.openCamera(getActivity(), 0);
                easyImage.openCameraForImage(getActivity());
            }
        });

        Button stop = (Button) findViewById(R.id.Stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stop.getText().toString().equals("Stop")) {
                    stopLocationUpdates();
                    chronometer.stop();
                    stop.setText("Start");
                } else {
                    startLocationUpdates();
                    chronometer.start();
                    stop.setText("Stop");
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_FINE_LOCATION);
            }

            return;
        }
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(20000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
    }

    /**
     * it stops the location updates
     */
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            mCurrentLocation = locationResult.getLastLocation();
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            Log.i("MAP", "new location " + mCurrentLocation.toString());
            if (mMap != null) {
                currentLatitudeList.add(mCurrentLocation.getLatitude());
                currentLongitudeList.add(mCurrentLocation.getLongitude());
                route.add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
            }
            pressure = pressure_and_temperature.getPressure();
            temperature = pressure_and_temperature.getTemperature();

            imagePath.setLatitudeList(currentLatitudeList);
            imagePath.setLongitudeList(currentLongitudeList);
            imagePath.setPressure(pressure);
            imagePath.setTemperature(temperature);
            pathViewModel.updatePath(imagePath);

            polyline = mMap.addPolyline(new PolylineOptions().addAll(route).width(10).color(Color.BLUE));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 14.0f));

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, null /* Looper */);
                }
                return;
            }

        }
    }

    private void initEasyImage() {
        easyImage = new EasyImage.Builder(this)
                .setChooserTitle("Pick media")
                .setCopyImagesToPublicGalleryFolder(false)
//                .setChooserType(ChooserType.CAMERA_AND_DOCUMENTS)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("EasyImage sample")
                .allowMultiple(true)
                .build();
    }

//    private void initEasyImage() {
//        EasyImage.configuration(this)
//                .setImagesFolderName("EasyImage sample")
//                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
//                .setCopyPickedImagesToPublicGalleryAppFolder(false)
//                .setAllowMultiplePickInGallery(true);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                for (MediaFile imageFile : imageFiles) {
                    Log.d("EasyImage", "Image file returned: " + imageFile.getFile().toString());
                }
                Bitmap bitmap = BitmapFactory.decodeFile(imageFiles[0].getFile().getAbsolutePath());
                byte[] picture = getBitmapAsByteArray(bitmap);
                image.setPicture(picture);

                insertImage(imageViewModel);
            }

            @Override
            public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                //Some error handling
                error.printStackTrace();
            }

            @Override
            public void onCanceled(@NonNull MediaSource source) {
                //Not necessary to remove any files manually anymore
            }
        });
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
//            @Override
//            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
//                //Some error handling
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
//                System.out.println(imageFiles.get(0).getAbsolutePath());
//                Bitmap bitmap= BitmapFactory.decodeFile(imageFiles.get(0).getAbsolutePath());
//                byte[] picture = getBitmapAsByteArray(bitmap);
//                image.setPicture(picture);
//
//                insertImage(imageViewModel);
//            }
//
//            @Override
//            public void onCanceled(EasyImage.ImageSource source, int type) {
//                //Cancel handling, you might wanna remove taken photo if it was canceled
//                if (source == EasyImage.ImageSource.CAMERA) {
//                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(getActivity());
//                    if (photoFile != null) photoFile.delete();
//                }
//            }
//        });
//    }

        public void insertImage (ImageViewModel imageViewModel){
            imageViewModel.insertOneImage(image);
        }

        public static byte[] getBitmapAsByteArray (Bitmap bitmap){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }

        private boolean allPermissionsGranted () {

            for (String permission : REQUIRED_PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }

        public Activity getActivity () {
            return activity;
        }

    }