package uk.ac.shef.oak.com4510.views.Path;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.ImageElement;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PathImageFragment extends Fragment {
    private PathImageAdapter pathImageAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PathViewModel pathViewModel;
    private ImageViewModel imageViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_path_image, container, false);

        pathViewModel = ViewModelProviders.of(this).get(PathViewModel.class);

        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

        pathViewModel.getPaths().observe(this, new Observer<List<Path>>() {
            @Override
            public void onChanged(List<Path> paths) {
                if (paths != null) {
                    recyclerView = root.findViewById(R.id.recycler_view);
                    pathImageAdapter = new PathImageAdapter(getContext(), paths, imageViewModel, getViewLifecycleOwner());
                    linearLayoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(pathImageAdapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
                }
            }
        });

        return root;
    }

}
