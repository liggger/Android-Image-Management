package uk.ac.shef.oak.com4510.views.Path;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;

/**
 * @description The path detail fragment.
 * @author Yichen Niu
 */

public class PathDetailFragment extends Fragment {

    private ImageViewModel imageViewModel;
    private PathDetailAdapter pathDetailAdapter;
    private RecyclerView recyclerView;
    private TextView textView;
    // The title of the path.
    private String title;
    // The path id.
    private int path_id;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment.
     * @param container This is the parent view that the fragment's
     * UI should be attached to.
     * @param savedInstanceState this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the path detail fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the path detail fragment.
        View v = inflater.inflate(R.layout.fragment_path_detail, container, false);

        // Creates an ImageViewModel.
        imageViewModel =
                ViewModelProviders.of(this).get(ImageViewModel.class);

        // Returns the value associated with the key title from Bundle.
        title = getArguments().getString("title");
        // Returns the value associated with the key path id from Bundle.
        path_id = Integer.parseInt(getArguments().getString("path_id"));

        textView = v.findViewById(R.id.Title);
        // Sets the text to be displayed.
        textView.setText(title);

        /**
         * Find the target path according to the path id.
         * Adds the given observer to the observers list within the lifespan of the given owner.
         */
        imageViewModel.findImagesByPathId(path_id).observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(@Nullable List<Image> images) {
                recyclerView = v.findViewById(R.id.recycler_view);

                // Initialize the PathDetailAdapter
                pathDetailAdapter = new PathDetailAdapter(images, false);

                // The number of columns in the grid.
                int numberOfColumns = 4;
                // Set the GridLayoutManager that this RecyclerView will use.
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
                // Set the path detail adapter to provide child views on demand.
                recyclerView.setAdapter(pathDetailAdapter);
            }
        });
        return v;
    }

}
