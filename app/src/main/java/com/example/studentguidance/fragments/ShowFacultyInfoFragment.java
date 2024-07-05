package com.example.studentguidance.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studentguidance.ModelClasses.Faculty;
import com.example.studentguidance.R;

import org.parceler.Parcels;

import de.hdodenhof.circleimageview.CircleImageView;


public class ShowFacultyInfoFragment extends Fragment {

    CircleImageView circularImageView;
    TextView nameTV,departmentTV,designationTV,cabinNoTV,emailTV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_show_faculty_info, container, false);
        init(view);
        Faculty faculty= Parcels.unwrap(this.getArguments().getParcelable("GET_FACULTY_FROM_ADAPTER"));


        setInfo(faculty);
        return view;
    }






    public void init(View view){
        circularImageView=view.findViewById(R.id.circularImageView);
        nameTV=view.findViewById(R.id.nameTV);
        departmentTV=view.findViewById(R.id.departmentTV);
        designationTV=view.findViewById(R.id.designationTV);
        cabinNoTV=view.findViewById(R.id.cabinNoTV);
        emailTV=view.findViewById(R.id.emailTV);
    }










    public void setInfo(Faculty faculty){
        Glide.with(requireContext())
                .load(faculty.getImagelink())
                .placeholder(R.drawable.person_image)
                .into(circularImageView);
        nameTV.setText(faculty.getName());
        designationTV.setText("Designation: ".concat(faculty.getDesignation()));
        departmentTV.setText("Department: ".concat(faculty.getDepartment()));
        cabinNoTV.setText("Cabin No: ".concat(faculty.getCabinnumber()));
        emailTV.setText("Email: ".concat(faculty.getEmail()));
    }
}