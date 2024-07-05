package com.example.studentguidance.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.studentguidance.Adapters.AboutUsRecyclerAdapter;
import com.example.studentguidance.ModelClasses.Developer;
import com.example.studentguidance.R;

import java.util.ArrayList;


public class AboutUsFragment extends Fragment {


    RecyclerView recyclerViewAboutUs;
    public static AboutUsRecyclerAdapter aboutUsAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_about_us, container, false);
        init(view);

//        ArrayList<Developer> arrayList = new ArrayList<>();
//        arrayList.add(new Developer("Arijit Modak","Android Developer",
//                "jkhuibfiuehiushf hfiherfhiufhuirehfuisdbuiehrjyc gsyfg uewygfuiewr f gweewuduewhwguibsdecji","null",""));
//        arrayList.add(new Developer("Arijit Modak","Android Developer",
//                "jkhuibfiuehiushf hfiherfhiufhuirehfuisdbuiehrjyc gsyfg uewygfuiewr f gweewuduewhwguibsdecji","null",""));

        aboutUsAdapter=new AboutUsRecyclerAdapter(HomeFragment.DEVELOPER_LIST,requireContext());
        recyclerViewAboutUs.setAdapter(aboutUsAdapter);
        recyclerViewAboutUs.smoothScrollToPosition(HomeFragment.DEVELOPER_LIST.size()-1);
        recyclerViewAboutUs.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false));
        return view;
    }



    public void init(View view){
        recyclerViewAboutUs=view.findViewById(R.id.recyclerViewAboutUs);
    }
}