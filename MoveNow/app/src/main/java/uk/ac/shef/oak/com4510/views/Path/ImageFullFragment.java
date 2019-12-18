package uk.ac.shef.oak.com4510.views.Path;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;

/**
 * @description The full size of the image.
 * @author Zhicheng Zhou
 */

public class ImageFullFragment extends Fragment {
    // The image to be displayed.
    private ImageView imageView;
    // The ImageViewModel.
    private ImageViewModel imageViewModel;
    // The path id and the image id.
    private int path_id, image_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the image full layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_full, container, false);

        // Initialize the ImageViewModel.
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

        // Find the ImageView.
        imageView = v.findViewById(R.id.imageView);
        // Get the path id from Bundle.
        path_id = getArguments().getInt("path_id");
        // Get the image id from Bundle.
        image_id = getArguments().getInt("image_id");

        /*
          Find the image according to the image id.
          Adds the given observer to the observers list within the lifespan of the given owner.
         */
        imageViewModel.findImageByImageId(image_id).observe(this, new Observer<Image>() {
            @Override
            public void onChanged(Image image) {
                // Get the picture.
                byte[] picture = image.getPicture();
                // Resize the input image.
                Bitmap bitmap = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                // Update the image at the given position.
                imageView.setImageBitmap(bitmap);
            }
        });

        /*
          Register a callback to be invoked when this imageView is clicked.
         */
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                // Inserts the image id into the mapping of this Bundle.
                bundle.putInt("image_id", image_id);
                // Inserts the path id into the mapping of this Bundle.
                bundle.putInt("path_id", path_id);

                /*
                  Find a {@link NavController} given a local {@link View}.
                 */
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_imageFullFragment_to_imageFragment, bundle);
            }
        });

        return v;
    }

}
