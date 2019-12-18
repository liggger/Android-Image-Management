package uk.ac.shef.oak.com4510.views.Path;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;

/**
 * @description The path image fragment.
 * @author Yichen Niu
 */

public class PathImageFragment extends Fragment {
    private PathImageAdapter pathImageAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PathViewModel pathViewModel;
    private ImageViewModel imageViewModel;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment.
     * @param container This is the parent view that the fragment's
     * UI should be attached to.
     * @param savedInstanceState this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the path image fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the path image fragment.
        View root = inflater.inflate(R.layout.fragment_path_image, container, false);

        // Creates a PathViewModel.
        pathViewModel = ViewModelProviders.of(this).get(PathViewModel.class);
        // Creates a ImageViewModel.
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

        /*
          Get all paths.
          Adds the given observer to the observers list within the lifespan of the given owner.
         */
        pathViewModel.getPaths().observe(this, new Observer<List<Path>>() {
            @Override
            public void onChanged(@Nullable List<Path> paths) {
                recyclerView = root.findViewById(R.id.recycler_view);

                // Initialize the PathAdapter
                pathImageAdapter = new PathImageAdapter(getContext(), paths, imageViewModel, getViewLifecycleOwner());
                // Creates a vertical LinearLayoutManager
                linearLayoutManager = new LinearLayoutManager(getContext());
                // Set the LinearLayoutManager that this RecyclerView will use.
                recyclerView.setLayoutManager(linearLayoutManager);
                // Set the pathImageAdapter to provide child views on demand.
                recyclerView.setAdapter(pathImageAdapter);
                /*
                  Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can
                  affect both measurement and drawing of individual item views.
                 */
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            }
        });

        return root;
    }

    /**
     * Creates the activity
     *
     * @param savedInstanceState State
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Finds the first descendant imageView with the given ID.
        ImageView imageView = getView().findViewById(R.id.imagePathBack);
        // Register a callback to be invoked when this imageView is clicked.
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Find a {@link NavController} given a local {@link View}.
                 */
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_pathImageFragment_to_navigation_paths);
            }
        });
    }

}
