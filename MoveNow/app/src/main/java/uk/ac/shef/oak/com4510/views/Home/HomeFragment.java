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

/**
 * @description The HomeFragment.
 * @author Zhicheng Zhou
 */

public class HomeFragment extends Fragment implements View.OnFocusChangeListener {

    private PathViewModel pathViewModel;
    private EditText title;
    private Button start;
    private Date date;

    /**
     * Called to have the fragment instantiate its user interface view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment.
     * @param container This is the parent view that the fragment's
     * UI should be attached to.
     * @param savedInstanceState this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return Return the View for the Home fragment's UI.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the home fragment.
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        /*
          Creates a PathViewModel, which retains ViewModels while a scope of given
          {@code fragment} is alive.
         */
        pathViewModel = ViewModelProviders.of(this).get(PathViewModel.class);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Finds the first descendant view with the editText.
        title = getView().findViewById(R.id.editText);
        // Register a callback to be invoked when focus of the title editText changed.
        title.setOnFocusChangeListener(this);
        // Finds the first descendant view with the Start.
        start = getView().findViewById(R.id.Start);

        // Register a callback to be invoked when the start button is clicked
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                  Constructs a <code>SimpleDateFormat</code> using the given pattern and
                  the default date format symbols.
                 */
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                try {
                    // Parses text from the given string date to produce a date
                    date = sdf.parse(sdf.format(Calendar.getInstance().getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                /*
                  If the editText is null,
                  sets the right-hand compound drawable of the TextView to the "error".
                 */
                if (title.getText().toString().trim().equalsIgnoreCase("")) {
                    title.setError("This field can not be blank");
                } else {
                    // Construct a new path.
                    Path path = new Path(title.getText().toString(), date,
                            null,null, null, null);
                    // Insert the path.
                    insertPath(pathViewModel, path);
                    // Create a new intent.
                    Intent intent = new Intent(getActivity(), HomeStopActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    /**
     * Change the hint of the editText when the focus state of a view has changed.
     * @param v The view whose state has changed.
     * @param hasFocus The new focus state of v.
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        title.setHint("");
    }

    /**
     * Insert the target path.
     * @param pathViewModel The pathViewModel.
     * @param path The path to be inserted.
     */
    private void insertPath(PathViewModel pathViewModel, Path path) {
        pathViewModel.insertPath(path);
    }
}