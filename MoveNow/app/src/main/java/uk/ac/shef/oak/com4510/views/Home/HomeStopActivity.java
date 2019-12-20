package uk.ac.shef.oak.com4510.views.Home;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import pl.aprilapps.easyphotopicker.ChooserType;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import uk.ac.shef.oak.com4510.MainActivity;
import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;
import uk.ac.shef.oak.com4510.views.Gallery.GalleryAdapter;
import uk.ac.shef.oak.com4510.views.Home.Service.ProcessMainClass;
import uk.ac.shef.oak.com4510.views.Home.Service.RestartServiceBroadcastReceiver;

/**
 * @description The activity when the tracking is on.
 * @author Legao Dai, Zhicheng Zhou
 */

public class HomeStopActivity extends AppCompatActivity implements OnMapReadyCallback {
    // The request code permissions.
    private static final int ACCESS_FINE_LOCATION = 123;
    private static final int CAMERA_REQUEST_CODE = 124;
    private static final int GALLERY_REQUEST_CODE = 125;
    // The EasyImage.
    private EasyImage easyImage;
    // The ImageViewModel.
    private ImageViewModel imageViewModel;
    // The PathViewModel.
    private PathViewModel pathViewModel;

    // The GoogleMap.
    private static GoogleMap mMap;
    // The image taken by the camera.
    private Image image;
    // The current path.
    private Path imagePath;
    // The current path id.
    private int pathId;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest = new LocationRequest();

    private Activity activity;
    // The current location.
    private Location mCurrentLocation;
    // The LatLng list.
    private ArrayList<LatLng> route = new ArrayList<>();
    // The latitude list.
    private ArrayList<Double> currentLatitudeList = new ArrayList<>();
    // The longitude list.
    private ArrayList<Double> currentLongitudeList = new ArrayList<>();

    // The temperature sensor and the pressure sensor.
    private Pressure_and_Temperature pressure_and_temperature = new Pressure_and_Temperature();
    // The value of the temperature sensor and the pressure sensor.
    private String temperature, pressure;
    // The chronometer.
    private Chronometer chronometer;
    // The date format.
    private SimpleDateFormat sdf;
    // The date.
    private Date date;
    // Store the time when the stop button is clicked.
    private long stopTime;
    // The coordinate TextView.
    private TextView coordinate;

    /**
     * Initialize the activity.
     * @param savedInstanceState The savedInstanceState
     */
    @SuppressLint({"SetTextI18n", "SimpleDateFormat"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_stop);

        // Hide the ActionBar if it is currently showing.
        Objects.requireNonNull(getSupportActionBar()).hide();
        // Get the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Get the coordinate textView.
        coordinate = findViewById(R.id.coordinate);

        // Initialize the sensors.
        pressure_and_temperature.initPressure_and_Temperature(getApplicationContext());
        // Start the temperature sensor.
        pressure_and_temperature.startTemperatureSensor();
        // Start the pressure sensor.
        pressure_and_temperature.startPressureSensor();

        initEasyImage();

        // Get the activity.
        activity = this;

        // Creates a ImageViewModel.
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);
        // Creates a PathViewModel.
        pathViewModel = ViewModelProviders.of(this).get(PathViewModel.class);

        /*
          Get the latest path.
          Adds the given observer to the observers list within the lifespan of the given owner.
         */
        pathViewModel.getOnePath().observe(this, new Observer<Path>() {
            @Override
            public void onChanged(Path path) {
                imagePath = path;
                pathId = path.getPath_id();
            }
        });


        startLocationUpdates();

        /*
          Find the FloatingActionButton gallery,
          if clicked, open the gallery.
         */
        FloatingActionButton fabGallery = findViewById(R.id.fab_gallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] necessaryPermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openGallery(HomeStopActivity.this.getActivity());
                } else {
                    requestPermissionsCompat(necessaryPermissions, GALLERY_REQUEST_CODE);
                }
            }
        });

        /*
          Find the FloatingActionButton camera,
          if clicked, open the camera.
         */
        FloatingActionButton fab = findViewById(R.id.fab_camera);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] necessaryPermissions = new String[]{Manifest.permission.CAMERA};
                if (arePermissionsGranted(necessaryPermissions)) {
                    easyImage.openCameraForImage(HomeStopActivity.this.getActivity());
                } else {
                    requestPermissionsCompat(necessaryPermissions, CAMERA_REQUEST_CODE);
                }
            }
        });

        /*
          Find the textView chronometer, sets the format string used for display.
          Sets the listener to be called when the chronometer changes.
         */
        chronometer = findViewById(R.id.chronometer);
        sdf = new SimpleDateFormat("HH:mm:ss");
        chronometer.setFormat("00:%s");
        chronometer.setOnChronometerTickListener(ch -> {
            long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
            if (elapsedMillis > 3600000L) {
                chronometer.setFormat("0%s");
            } else {
                chronometer.setFormat("00:%s");
            }
        });
        chronometer.start();

        /*
          Find the button stop, if the button is clicked,
          if the text is stop, stop the chronometer, stop the service and set the text 'Restart',
          else, start the chronometer, start the service and set the text 'Stop'.
         */
        Button stop = findViewById(R.id.Stop);
        stop.setOnClickListener(v -> {
            if (stop.getText().toString().equals("Stop")) {
                stopTime = chronometer.getBase() - SystemClock.elapsedRealtime();
                stopLocationUpdates();
                chronometer.stop();
                stop.setText("Restart");
            } else {
                chronometer.setBase(SystemClock.elapsedRealtime() + stopTime);
                startLocationUpdates();
                chronometer.start();
                stop.setText("Stop");
            }

        });

        /*
          Find the button finish, if the button is clicked,
          stop the chronometer, stop the service and start the intent to the MainActivity.
         */
        Button finish = findViewById(R.id.Finish);
        finish.setOnClickListener(v -> {
            stopLocationUpdates();
            chronometer.stop();
            pressure_and_temperature.stopPressureSensor();
            pressure_and_temperature.stopTemperatureSensor();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Initialize the EasyImage.
     */
    private void initEasyImage() {
        easyImage = new EasyImage.Builder(this)
                .setChooserTitle("Pick the image")
                .setCopyImagesToPublicGalleryFolder(false)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("Images")
                .allowMultiple(true)
                .build();
    }

    /**
     * Start updating the location for every 20 seconds.
     */
    private void startLocationUpdates() {
        /*
          Create a new LocationRequest,
          set the interval,
          set the priority.
         */
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(20000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the FusedLocationProviderClient.
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Request for a update.
        String[] necessaryPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

        if (arePermissionsGranted(necessaryPermissions)) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
        } else {
            requestPermissionsCompat(necessaryPermissions, ACCESS_FINE_LOCATION);
        }

        /*
          If the version is Android 7 onwards, use Job Services,
          else, use the ProcessMainClass.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            RestartServiceBroadcastReceiver.scheduleJob(getApplicationContext());
        } else {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(getApplicationContext());
        }
    }

    /**
     * it stops the location updates
     */
    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    /**
     * When the location is called back, update the sensors and the current locations.
     */
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            // Get the latest location.
            mCurrentLocation = locationResult.getLastLocation();
            // Print the new location, current pressure and temperature.
            Log.i("MAP", "New location: (" + mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() + ")");
            Log.i("MAP", "Current pressure: " + pressure_and_temperature.getPressure());
            Log.i("MAP", "Current temperature: " + pressure_and_temperature.getTemperature());

            if (mMap != null) {
                // Add the latitude to the list.
                currentLatitudeList.add(mCurrentLocation.getLatitude());
                // Add the longitude to the list.
                currentLongitudeList.add(mCurrentLocation.getLongitude());
                // Add the LatLng to the route.
                route.add(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
            }

            // Set the text of the coordinate textView.
            coordinate.setText(String.format("(%s, %s)", mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
            // Get the pressure.
            pressure = pressure_and_temperature.getPressure();
            // Get the temperature.
            temperature = pressure_and_temperature.getTemperature();

            /*
              Update the imagePath's LatitudeList, LongitudeList, pressure and temperature.
             */
            imagePath.setLatitudeList(currentLatitudeList);
            imagePath.setLongitudeList(currentLongitudeList);
            imagePath.setPressure(pressure);
            imagePath.setTemperature(temperature);
            pathViewModel.updatePath(imagePath);

            // AddPolyline at the GoogleMap.
            mMap.addPolyline(new PolylineOptions().addAll(route).width(10).color(Color.BLUE));
            // Move to the new LatLng.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 14.0f));

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Called when an activity launched exits
     * @param requestCode  The integer request code.
     * @param resultCode The integer result code returned
     *                   by the child activity through its setResult().
     * @param data An Intent, which can return result data to the caller.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {

            /**
             * When the image is taken by the camera or is chosen in the gallery,
             * add a marker in the map, and insert the image.
             * @param imageFiles The imageFiles taken by the camera.
             * @param source The source
             */
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onMediaFilesPicked(@NonNull MediaFile[] imageFiles, @NonNull MediaSource source) {
                // Print the image information.
                for (MediaFile imageFile : imageFiles) {
                    Log.d("EasyImage", "Image file returned: " + imageFile.getFile().toString());
                }

                // Add a marker according to the current latitude and longitude.
                mMap.addMarker(new MarkerOptions().position(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation
                        .getLongitude()))
                        .title(String.format("%s, %s", mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude())));

                // Get the current pressure.
                pressure = pressure_and_temperature.getPressure();
                // Get the current temperature.
                temperature = pressure_and_temperature.getTemperature();

                /*
                  Constructs a <code>SimpleDateFormat</code> using the given pattern and
                  the default date format symbols.
                 */
                sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    // Parses text from the given string to produce a date.
                    date = sdf.parse(sdf.format(Calendar.getInstance().getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Initialize the image, and converter the imageFile to the byte array.
                image = new Image(pathId, mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), date);
                Bitmap bitmap = BitmapFactory.decodeFile(imageFiles[0].getFile().getAbsolutePath());
                byte[] picture = getBitmapAsByteArray(bitmap);

                // Set the picture.
                image.setPicture(picture);

                // Insert the image.
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

    /**
     * Insert the image taken by the camera.
     * @param imageViewModel The ImageViewModel.
     */
    public void insertImage (ImageViewModel imageViewModel){
        imageViewModel.insertOneImage(image);
    }

    /**
     * Write a compressed version of the bitmap to the outPutStream.
     * @param bitmap The bitmap of the image taken by the camera.
     * @return The newly allocated byte array.
     */
    public static byte[] getBitmapAsByteArray (Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * Callback for the result from requesting permissions.
     * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback, null /* Looper */);
                }
            }
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    easyImage.openCameraForImage(HomeStopActivity.this);
                }
            }
            case GALLERY_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    easyImage.openGallery(HomeStopActivity.this);
                }
            }
        }
    }

    /**
     * Initialize the GoogleMap.
     * @param googleMap The GoogleMap.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    /**
     * Get the current activity.
     * @return The current activity.
     */
    public Activity getActivity () {
        return activity;
    }

    /**
     * Check whether the permissions are granted.
     * @param permissions The permission.
     * @return True if the permissions are granted, else, false.
     */
    private boolean arePermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    /**
     * Request the permissions according to the permissions.
     * @param permissions The permissions.
     * @param requestCode The requestCode.
     */
    private void requestPermissionsCompat(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
    }
}