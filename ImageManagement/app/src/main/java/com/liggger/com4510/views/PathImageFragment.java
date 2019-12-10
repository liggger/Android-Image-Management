package com.liggger.com4510.views;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liggger.imagemanagement.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PathImageFragment extends Fragment {


    public PathImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_path_image, container, false);
    }

}
