package uk.ac.shef.oak.com4510.views.Path;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;

/**
 * @description The path fragment.
 * @author Yichen Niu
 */
public class PathFragment extends Fragment {

    private PathViewModel pathViewModel;
    private PathAdapter pathAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment.
     * @param container This is the parent view that the fragment's
     * UI should be attached to.
     * @param savedInstanceState this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the gallery fragment's UI.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the path fragment.
        View root = inflater.inflate(R.layout.fragment_path, container, false);

        /**
         * Creates a PathViewModel, which retains ViewModels while a scope of given
         * {@code fragment} is alive.
         */
        pathViewModel =
                ViewModelProviders.of(this).get(PathViewModel.class);

        /**
         * Get all paths.
         * Adds the given observer to the observers list within the lifespan of the given owner.
         */
        pathViewModel.getPaths().observe(this, new Observer<List<Path>>() {
            @Override
            public void onChanged(@Nullable List<Path> paths) {
                recyclerView = root.findViewById(R.id.recycler_view);

                // Initialize the PathAdapter
                pathAdapter = new PathAdapter(paths);
                // Creates a vertical LinearLayoutManager
                linearLayoutManager = new LinearLayoutManager(getContext());
                // Set the LinearLayoutManager that this RecyclerView will use.
                recyclerView.setLayoutManager(linearLayoutManager);
                // Set the pathAdapter to provide child views on demand.
                recyclerView.setAdapter(pathAdapter);
            }
        });

        return root;
    }

    /**
     * Create the activity
     *
     * @param savedInstanceState State
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Finds the first descendant imageView with the given ID.
        ImageView imageView = getView().findViewById(R.id.imageView);
        // Register a callback to be invoked when this imageView is clicked.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Find a {@link NavController} given a local {@link View}.
                 */
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_paths_to_pathImageFragment);
            }
        });
    }

}