package uk.ac.shef.oak.com4510.views.Path;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;

/**
 * @description The image fragment.
 * @author Yichen Niu, Zhicheng Zhou
 */

public class ImageFragment extends Fragment implements OnMapReadyCallback {

    // The google map.
    private GoogleMap mMap;
    // The PathViewModel.
    private PathViewModel pathViewModel;
    // The ImageViewModel.
    private ImageViewModel imageViewModel;
    // The path id and the image id.
    private int path_id, image_id;
    // The latitude list and the longitude list.
    private List<Double> latitudeList, longitudeList;
    // The LatLng list.
    private List<LatLng> route = new ArrayList<>();
    // The image latitude and the longitude.
    private double imageLatitude, imageLongitude;
    // The value of the temperature sensor and the pressure sensor.
    private String temperature, pressure;
    // The LatLng.
    private LatLng latLng;
    // The title, temperature, pressure TextView.
    private TextView Title, Temp, Pressure;
    // The imageView.
    private ImageView imageView;
    private Bitmap newBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the image layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image, container, false);

        // Initialize the PathViewModel.
        pathViewModel = ViewModelProviders.of(this).get(PathViewModel.class);
        // Initialize the ImageViewModel.
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

        /*
          Find the title, temp, pressure TextView and the imageView.
         */
        Title = v.findViewById(R.id.Date);
        Temp = v.findViewById(R.id.Temp);
        Pressure = v.findViewById(R.id.Pressure);
        imageView = v.findViewById(R.id.imageView);

        /*
          Get the path id and the image id from the Bundle.
         */
        path_id = getArguments().getInt("path_id");
        image_id = getArguments().getInt("image_id");

        /*
          Find the path according to the path id.
         */
        pathViewModel.findPathById(path_id).observe(this, new Observer<Path>() {
            @Override
            public void onChanged(@NonNull Path path) {
                /*
                  Get the value of temperature and pressure.
                 */
                temperature = path.getTemperature();
                pressure = path.getPressure();

                /*
                  Set the temperature, pressure, title TextView.
                 */
                Temp.setText(String.format("Temp: %s C", temperature));
                Pressure.setText(String.format("Pressure: %s mbars", pressure));
                Title.setText(String.format("Title: %s", path.getTitle()));

                /*
                  Get the whole path, and plot it.
                 */
                latitudeList = path.getLatitudeList();
                longitudeList = path.getLongitudeList();
                plotRoute(mMap, latitudeList, longitudeList);
            }
        });

        /*
          Find the images according to the path id,
          add the markers of these images' location.
         */
        imageViewModel.findImagesByPathId(path_id).observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(@NonNull List<Image> images) {
                /*
                  Add the small blue markers except the current image.
                 */
                for (int i = 0; i < images.size(); i++) {
                    if (images.get(i).getImage_id() != image_id) {
                        /*
                          Set the size of the height and width for the blue marker,
                          get the blue marker
                         */
                        int height = 70;
                        int width = 70;
                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.blue_marker_40);
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

                        /*
                          Get the LatLng and add the marker
                         */
                        latLng = new LatLng(images.get(i).getLatitude(), images.get(i).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .title(images.get(i).getLatitude() + ", " + images.get(i).getLongitude())
                                .icon(smallMarkerIcon));
                    }
                }
            }
        });

        /*
          Find the image according to the image id.
         */

        imageViewModel.findImageByImageId(image_id).observe(this, new Observer<Image>() {
            @Override
            public void onChanged(@NonNull Image image) {
                /*
                  Set the size of the height and width for the red marker,
                  get the red marker
                */
                imageLatitude = image.getLatitude();
                imageLongitude = image.getLongitude();
                int height = 90;
                int width = 90;
                Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.red_marker_80);
                Bitmap bigMarker = Bitmap.createScaledBitmap(b, width, height, false);
                BitmapDescriptor bigMarkerIcon = BitmapDescriptorFactory.fromBitmap(bigMarker);

                /*
                  Get the LatLng and add the marker.
                 */
                latLng = new LatLng(imageLatitude, imageLongitude);
                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title(image.getLatitude() + ", " + image.getLongitude())
                        .icon(bigMarkerIcon));

                /*
                  Get the picture, resize the picture and set the imageView.
                 */
                byte[] picture = image.getPicture();
                Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                newBitmap = scaleBitmap(bitmap, 0.4f);
                imageView.setImageBitmap(newBitmap);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        // Inserts the image id into the mapping of this Bundle.
                        bundle.putInt("image_id", image_id);
                        // Inserts the path id into the mapping of this Bundle.
                        bundle.putInt("path_id", path_id);
                        NavController controller = Navigation.findNavController(v);
                        controller.navigate(R.id.action_imageFragment_to_imageFullFragment, bundle);
                    }
                });
            }
        });

        return v;
    }

    /**
     * Get the map.
     * @param savedInstanceState The savedInstanceState.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
     * Plot the PolyLine for the whole path.
     * @param map The GoogleMap.
     * @param latitudeList The latitude list of the path.
     * @param longitudeList The longitude list of the path.
     */
    public void plotRoute(GoogleMap map, List<Double> latitudeList, List<Double> longitudeList) {
        for (int i = 0; i < latitudeList.size(); i++) {
            route.add(new LatLng(latitudeList.get(i), longitudeList.get(i)));
        }
        map.addPolyline(new PolylineOptions().addAll(route).width(6).color(Color.BLUE));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.get(0), 14.0f));
    }

    /**
     * Resize the input image.
     * @param origin The input image.
     * @param ratio The ratio of the image.
     * @return The new image.
     */
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        // The width of the input image.
        int width = origin.getWidth();
        // The height of the input image.
        int height = origin.getHeight();

        // Optional matrix to be applied to the pixels.
        Matrix matrix = new Matrix();
        matrix.preScale(1, ratio);

        // Get the bitmap that represents the specified subset of source.
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
}
