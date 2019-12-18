package uk.ac.shef.oak.com4510.views.User;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.ImageViewModel;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;

public class UserFragment extends Fragment {
    // The PathViewModel.
    private PathViewModel pathViewModel;
    // The ImageViewModel.
    private ImageViewModel imageViewModel;

    private TextView LastPathDate, Pictures, Paths;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user, container, false);

        // Initialize the PathViewModel.
        pathViewModel = ViewModelProviders.of(this).get(PathViewModel.class);
        // Initialize the ImageViewModel.
        imageViewModel = ViewModelProviders.of(this).get(ImageViewModel.class);

        LastPathDate = v.findViewById(R.id.LastPathDate);
        Pictures = v.findViewById(R.id.Pictures);
        Paths = v.findViewById(R.id.Paths);

        pathViewModel.getOnePath().observe(this, new Observer<Path>() {
            @Override
            public void onChanged(Path path) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                LastPathDate.setText(String.format("%s %s", "Last path date: ", format.format(path.getDate())));
            }
        });

        imageViewModel.countImageNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Pictures.setText(String.format("%s %d ", "Pictures: ", integer));
            }
        });

        pathViewModel.countPathNumber().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Paths.setText(String.format("%s %d ", "Paths: ", integer));
            }
        });
        return v;
    }

}
