package uk.ac.shef.oak.com4510.views.Path;


import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Image;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PathDetailFragment extends Fragment {
    private PathViewModel pathViewModel;
    private ImageViewModel imageViewModel;
    private PathDetailAdapter pathDetailAdapter;
    private RecyclerView recyclerView;
    private TextView textView;
    private String title;
    private int path_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_path_detail, container, false);

        imageViewModel =
                ViewModelProviders.of(this).get(ImageViewModel.class);

        title = getArguments().getString("title");
        path_id = Integer.parseInt(getArguments().getString("path_id"));
        textView = v.findViewById(R.id.Title);
        textView.setText(title);
        imageViewModel.findImagesById(path_id).observe(this, new Observer<List<Image>>() {
            @Override
            public void onChanged(List<Image> images) {
                if (images != null) {
                    pathDetailAdapter = new PathDetailAdapter(images, false);
                    recyclerView = v.findViewById(R.id.recycler_view);
                    int numberOfColumns = 3;
                    recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
                    recyclerView.setAdapter(pathDetailAdapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
                }
            }
        });
        System.out.println(path_id);
        return v;
    }

}
