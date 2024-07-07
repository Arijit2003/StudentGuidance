package com.example.studentguidance.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentguidance.R;
import com.example.studentguidance.activities.MainActivity;


public class NavigationFragment extends Fragment {


    EditText startLocationET,destinationET;
    CardView getNavigationCV;
    String source,destination;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_navigation, container, false);
        init(view);

        getNavigationCV.setOnClickListener(v->{
            if(!startLocationET.getText().toString().equals("") &&!destinationET.getText().toString().equals("")){
                source=startLocationET.getText().toString().replaceAll("\\s","").toLowerCase();
                destination=destinationET.getText().toString().replaceAll("\\s","").toLowerCase();
                source=getCoordinates(source);
                destination=getCoordinates(destination);
                opeGmaps(source,destination);
            }else{
                Toast.makeText(requireContext(), "Fill all the fields ", Toast.LENGTH_SHORT).show();
                //show snackbar

            }
        });

        return view;
    }






    private void init(View view){
        startLocationET=view.findViewById(R.id.startLocationET);
        destinationET=view.findViewById(R.id.destinationET);
        getNavigationCV=view.findViewById(R.id.getNavigationCV);
        MainActivity.voiceIconFAB.setVisibility(View.VISIBLE);
    }









    public static String getCoordinates(String str){
        if(str.contains("academic") && str.contains("block")) str= HomeFragment.locationMaps.getOrDefault("academicblock",str);
        else if(str.contains("boys") && str.contains("hostel") && str.contains("1"))str=HomeFragment.locationMaps.getOrDefault("boyshostel1",str);
        else if(str.contains("boys") && str.contains("hostel") && str.contains("2"))str=HomeFragment.locationMaps.getOrDefault("boyshostel2",str);
        else if(str.contains("boys") && str.contains("hostel") && str.contains("3"))str=HomeFragment.locationMaps.getOrDefault("boyshostel3",str);
        else if(str.contains("boys") && str.contains("hostel") && str.contains("4"))str=HomeFragment.locationMaps.getOrDefault("boyshostel4",str);
        else if(str.contains("boys") && str.contains("hostel") && str.contains("5"))str=HomeFragment.locationMaps.getOrDefault("boyshostel5",str);
        else if(str.contains("boys") && str.contains("hostel") && str.contains("6"))str=HomeFragment.locationMaps.getOrDefault("boyshostel6",str);
        else if(str.contains("girls") && str.contains("hostel"))str=HomeFragment.locationMaps.getOrDefault("girlshostel",str);
        else if(str.contains("underbelly"))str=HomeFragment.locationMaps.getOrDefault("underbelly",str);
        else if(str.contains("lab") && str.contains("complex"))str=HomeFragment.locationMaps.getOrDefault("labcomplex",str);
        else if(str.contains("visa") && str.contains("mart"))str=HomeFragment.locationMaps.getOrDefault("visamart",str);
        else if(str.contains("multipurpose") && str.contains("hall"))str=HomeFragment.locationMaps.getOrDefault("multipurposehall",str);
        else if(str.contains("parking") && str.contains("area"))str=HomeFragment.locationMaps.getOrDefault("parkingarea",str);
        else if(str.contains("post") && str.contains("office"))str=HomeFragment.locationMaps.getOrDefault("postoffice",str);
        else if (str.contains("auditorium"))str=HomeFragment.locationMaps.getOrDefault("auditorium",str);
        else if(str.contains("ab"))str=HomeFragment.locationMaps.getOrDefault("ab",str);
        else if(str.contains("block") && str.contains("1"))str=HomeFragment.locationMaps.getOrDefault("block1",str);
        else if(str.contains("block") && str.contains("2"))str=HomeFragment.locationMaps.getOrDefault("block2",str);
        else if(str.contains("block") && str.contains("3"))str=HomeFragment.locationMaps.getOrDefault("block3",str);
        else if(str.contains("block") && str.contains("4"))str=HomeFragment.locationMaps.getOrDefault("block4",str);
        else if(str.contains("block") && str.contains("5"))str=HomeFragment.locationMaps.getOrDefault("block5",str);
        else if(str.contains("block") && str.contains("6"))str=HomeFragment.locationMaps.getOrDefault("block6",str);
        else if(str.contains("ub"))str=HomeFragment.locationMaps.getOrDefault("ub",str);
        else if(str.contains("lc"))str=HomeFragment.locationMaps.getOrDefault("lc",str);
        else if(str.contains("mph"))str=HomeFragment.locationMaps.getOrDefault("mph",str);
        return str;
    }






    public void opeGmaps(String from, String to){
        try {
            Uri uri=Uri.parse("https://www.google.com/maps/dir/"+from+"/"+to);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Uri uri=Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}