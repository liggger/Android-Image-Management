package uk.ac.shef.oak.com4510.views.Gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;

/**
 * @description The GalleryFragment.
 * @author Zhicheng Zhou
 */

public class GalleryFragment extends Fragment {
    // The imageViewModel.
    private ImageViewModel imageViewModel;
    // The adapter of the gallery.
    private GalleryAdapter galleryAdapter;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the gallery fragment.
        View v = inflater.inflate(R.layout.fragment_gallery, container, false);

        /*
          Creates a ImageViewModel, which retains ViewModels while a scope of given
          {@code fragment} is alive.
         */
        imageViewModel =
                ViewModelProviders.of(this).get(ImageViewModel.class);

        /*
          Get the whole images by date in ascending order.
          For every 20 pages, it will load more pages.
          Adds the given observer to the observers list within the lifespan of the given owner.
         */
        imageViewModel.getImagesByDate().observe(this, new Observer<PagedList<Image>>() {
            @Override
            public void onChanged(@Nullable PagedList<Image> images) {
                galleryAdapter = new GalleryAdapter();
                // Set the new images list to be displayed.
                galleryAdapter.submitList(images);
                recyclerView = v.findViewById(R.id.recycler_view);

                // The number of columns in the grid.
                int numberOfColumns = 4;
                // Set the GridLayoutManager that this RecyclerView will use.
                recyclerView.setLayoutManager(new GridLayoutManager(GalleryFragment.this.getContext(), numberOfColumns));
                // Set the gallery adapter to provide child views on demand.
                recyclerView.setAdapter(galleryAdapter);
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}