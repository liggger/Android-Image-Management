package uk.ac.shef.oak.com4510.views.Home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.shef.oak.com4510.R;
import uk.ac.shef.oak.com4510.model.Path;
import uk.ac.shef.oak.com4510.viewmodels.PathViewModel;


public class HomeFragment extends Fragment {

    private PathViewModel pathViewModel;
    private EditText title;
    private Button start;
    private Date date;
    private Calendar calendar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pathViewModel =
                ViewModelProviders.of(this).get(PathViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title = getView().findViewById(R.id.editText);
        start = getView().findViewById(R.id.Start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    date = sdf.parse(sdf.format(calendar.getInstance().getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (title.getText().toString().trim().equalsIgnoreCase("")) {
                    title.setError("This field can not be blank");
                } else {
                    Path path = new Path(title.getText().toString(), date, null,null, null, null);
                    insertPath(pathViewModel, path);
                    Intent intent = new Intent(getActivity(), HomeStopActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void insertPath(PathViewModel pathViewModel, Path path) {
        pathViewModel.insertPath(path);
    }
}