package com.liggger.com4510.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.liggger.imagemanagement.R;
import com.liggger.com4510.model.Path;
import com.liggger.com4510.viewmodels.PathViewModel;

import java.util.ArrayList;
import java.util.List;

public class PathFragment extends Fragment {

    private PathViewModel pathViewModel;
    private List<Path> list;
    private PathAdapter pathAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pathViewModel =
                ViewModelProviders.of(this).get(PathViewModel.class);
        View root = inflater.inflate(R.layout.fragment_path, container, false);
        initData();

        recyclerView = root.findViewById(R.id.recycler_view);
        pathAdapter = new PathAdapter(list);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(pathAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView imageView = getView().findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_navigation_paths_to_pathImageFragment);
            }
        });
    }

    private void initData() {
        list = new ArrayList<>();
        Path path1 = new Path("23/10/2019 12:24", "Walk to the 7 bridges");
        Path path2 = new Path("23/10/2019 12:24", "Walk to the 7 bridges");
        list.add(path1);
        list.add(path2);

    }
}