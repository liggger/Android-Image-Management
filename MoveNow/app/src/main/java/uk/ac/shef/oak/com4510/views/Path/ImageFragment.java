package uk.ac.shef.oak.com4510.views.Path;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private PathViewModel pathViewModel;
    private ImageViewModel imageViewModel;
    private int path_id;
    private int image_id;
    private List<Double> latitudeList;
    private List<Double> longitudeList;
    private List<LatLng> route = new ArrayList<>();
    private double imageLatitude;
    private double imageLonitude;
    private String temperature;
    private String pressure;
    private Polyline polyline;
    private LatLng latLng;
    private TextView Title, Temp, Pressure;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image, container, false);

        pathViewModel = ViewModelProviders.of(this).get(PathViewModel.class);
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

        Title = v.findViewById(R.id.Date);
        Temp = v.findViewById(R.id.Temp);
        Pressure = v.findViewById(R.id.Pressure);
        imageView = v.findViewById(R.id.imageView);

        path_id = getArguments().getInt("path_id");
        image_id = getArguments().getInt("image_id");

        pathViewModel.findPathById(path_id).observe(this, new Observer<Path>() {
            @Override
            public void onChanged(Path path) {
                if (path != null) {
                    temperature = path.getTemperature();
                    pressure = path.getPressure();
                    Temp.setText("Temp: " + temperature);
                    Pressure.setText("Pressure: " + pressure);
                    Title.setText("Title: " + path.getTitle());
                    latitudeList = path.getLatitudeList();
                    longitudeList = path.getLongitudeList();
                    plotRoute(mMap, latitudeList, longitudeList);
                }
            }
        });

        imageViewModel.findImagesById(path_id).observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                for (int i = 0; i < images.size(); i++) {
                    if (images.get(i).getImage_id() != image_id) {
                        int height = 70;
                        int width = 70;
                        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.blue_marker_40);
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                        latLng = new LatLng(images.get(i).getLatitude(), images.get(i).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .title(images.get(i).getLatitude() + ", " + images.get(i).getLongitude())
                                .icon(smallMarkerIcon));
                    }
                }
            }
        });

        imageViewModel.findImageByImageId(image_id).observe(this, new Observer<Image>() {
            @Override
            public void onChanged(Image image) {
                if (image != null) {
                    imageLatitude = image.getLatitude();
                    imageLonitude = image.getLongitude();
                    int height = 90;
                    int width = 90;
                    Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.red_marker_80);
                    Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                    BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);
                    latLng = new LatLng(imageLatitude, imageLonitude);
                    mMap.addMarker(new MarkerOptions().position(latLng)
                            .title(image.getLatitude() + ", " + image.getLongitude())
                            .icon(smallMarkerIcon));
                    byte[] picture = image.getPicture();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                    bitmap = scaleBitmap(bitmap, 0.4f);
                    imageView.setImageBitmap(bitmap);
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void plotRoute(GoogleMap map, List<Double> latitudeList, List<Double> longitudeList) {
        for (int i = 0; i < latitudeList.size(); i++) {
            route.add(new LatLng(latitudeList.get(i), longitudeList.get(i)));
        }
        map.addPolyline(new PolylineOptions().addAll(route).width(6).color(Color.BLUE));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.get(0), 14.0f));
    }

    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }
}
