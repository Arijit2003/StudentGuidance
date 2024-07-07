package com.example.studentguidance.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentguidance.R;
import com.example.studentguidance.activities.MainActivity;


public class QNAFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        MainActivity.voiceIconFAB.setVisibility(View.VISIBLE);
        return inflater.inflate(R.layout.fragment_q_n_a, container, false);
    }
}