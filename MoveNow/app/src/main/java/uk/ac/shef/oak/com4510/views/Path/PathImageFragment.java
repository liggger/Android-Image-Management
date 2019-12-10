package uk.ac.shef.oak.com4510.views.Path;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.ImageElement;

/**
 * A simple {@link Fragment} subclass.
 */
public class PathImageFragment extends Fragment {
    private PathImageAdapter pathImageAdapter;
    private List<ImageElement> list;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_path_image, container, false);

        initData();
        recyclerView = root.findViewById(R.id.pathImage);
        pathImageAdapter = new PathImageAdapter(list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pathImageAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));

        return root;
    }

    private void initData() {
        list = new ArrayList<>();

        list.add(new ImageElement(R.drawable.p1));
        list.add(new ImageElement(R.drawable.p1));
        list.add(new ImageElement(R.drawable.p1));
    }
}
