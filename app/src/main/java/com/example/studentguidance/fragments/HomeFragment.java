package com.example.studentguidance.fragments;

import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.studentguidance.ModelClasses.Developer;
import com.example.studentguidance.ModelClasses.Faculty;
import com.example.studentguidance.R;
import com.example.studentguidance.activities.MainActivity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }
    ImageSlider imageSlider;
    ArrayList<SlideModel> imageList=new ArrayList<>();
    CardView navigationCardView,facultyInfoCardView;
    FirebaseFirestore db;

    public static ArrayList<Faculty> FACULTY_LIST_VIT_BPL;
    public static ArrayList<Developer> DEVELOPER_LIST;

    public static boolean READ_EXTERNAL_STORAGE_GRANTED=false;
    static final int  READ_EXTERNAL_STORAGE_REQUEST_CODE=0;
    public static HashMap<String,String> locationMaps=new HashMap<>();












    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        imageSlider.setImageList(imageList);

        eventChangeListener();
        loadFacultyData();
        loadDeveloperData();
        navigationCardView.setOnClickListener(v-> loadFragment(new NavigationFragment(),1));
        facultyInfoCardView.setOnClickListener(view1 -> loadFragment(new FacultyListFragment(),1));


        return view;
    }










    private void init(View view){
        MainActivity.voiceIconFAB.setVisibility(View.INVISIBLE);

        imageSlider=view.findViewById(R.id.imageSlider);
        navigationCardView=view.findViewById(R.id.navigationCardView);
        facultyInfoCardView=view.findViewById(R.id.facultyInfoCardView);
        db=FirebaseFirestore.getInstance();
        FACULTY_LIST_VIT_BPL=new ArrayList<>();
        DEVELOPER_LIST=new ArrayList<>();
        //adding images to the list
        imageList.clear();
        imageList.add(new SlideModel(R.drawable.img1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.img2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.img3, ScaleTypes.FIT));
        getPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE_REQUEST_CODE);
        getPermission(Manifest.permission.RECORD_AUDIO,MainActivity.RECORD_AUDIO_REQUEST_CODE);

    }













    private void eventChangeListener(){
        locationMaps.clear();
        db.collection("StudentGuidance")
                .addSnapshotListener((value,error)->{
                    if(error!=null){
                        Log.e("FirestoreError",error.getMessage());
                        return ;
                    }
                    assert value != null;
                    DocumentChange dc=value.getDocumentChanges().get(0);
                    locationMaps.clear();
                    for(Map.Entry<String, Object> entry: dc.getDocument().getData().entrySet()){
                        //Log.d("FirestoreData",entry.getKey().concat(String.valueOf(entry.getValue())));
                        locationMaps.put(entry.getKey(),String.valueOf(entry.getValue()));
                    }
                });
    }














    private void loadFacultyData(){
        FACULTY_LIST_VIT_BPL.clear();
        db.collection("StudentGuidanceFacultyInfo")
                .addSnapshotListener((value, error) -> {
                    if(error!=null){
                        Log.e("FirestoreError",error.getMessage());
                        return ;
                    }
                    assert value != null;
                    Faculty temp;
                    int pos;
                    for (DocumentChange dc:value.getDocumentChanges()) {
                        if(dc.getType()== DocumentChange.Type.ADDED) {
                            temp = createFacultyFromFireStoreData(dc);
                            if (temp.getName() != null && temp.getDesignation() != null && temp.getDesignation() != null &&
                                    temp.getCabinnumber() != null && temp.getEmail() != null && temp.getImagelink() != null) {
                                FACULTY_LIST_VIT_BPL.add(temp);
                                if(FacultyListFragment.adapter!=null)
                                    FacultyListFragment.adapter.notifyItemInserted(FACULTY_LIST_VIT_BPL.size()-1);
                            }

                        }
                        else if(dc.getType()==DocumentChange.Type.MODIFIED){
                            pos=getPositionFromFacultyList(dc.getDocument().getId());
                            temp = createFacultyFromFireStoreData(dc);
                            if (pos!=-1 && temp.getName() != null && temp.getDesignation() != null && temp.getDesignation() != null &&
                                    temp.getCabinnumber() != null && temp.getEmail() != null && temp.getImagelink() != null) {
                                FACULTY_LIST_VIT_BPL.set(pos, temp);
                                if (FacultyListFragment.adapter != null) FacultyListFragment.adapter.notifyItemChanged(pos);
                            }
                        }
                        else{//Type.DELETED
                            pos=getPositionFromFacultyList(dc.getDocument().getId());
                            if(pos!=-1){
                                FACULTY_LIST_VIT_BPL.remove(pos);
                                if(FacultyListFragment.adapter!=null)FacultyListFragment.adapter.notifyItemRemoved(pos);
                            }
                        }
                    }
                });
    }

















    private Faculty createFacultyFromFireStoreData(DocumentChange dc){
        Faculty temp=new Faculty();
        temp.setDocumentid(dc.getDocument().getId());
        for (Map.Entry<String,Object> entry: dc.getDocument().getData().entrySet()) {
            switch (entry.getKey()){
                case "name": temp.setName(String.valueOf(entry.getValue()));break;
                case "department":temp.setDepartment(String.valueOf(entry.getValue()));break;
                case "designation":temp.setDesignation(String.valueOf(entry.getValue()));break;
                case "cabinnumber":temp.setCabinnumber(String.valueOf(entry.getValue()));break;
                case "email":temp.setEmail(String.valueOf(entry.getValue()));break;
                case "imagelink":temp.setImagelink(String.valueOf(entry.getValue()));break;
            }
        }
        return temp;
    }















    private int getPositionFromFacultyList(String docId){
        for(int i=0;i<FACULTY_LIST_VIT_BPL.size();i++){
            if(docId.equals(FACULTY_LIST_VIT_BPL.get(i).getDocumentid())) return i;
        }
        return -1;
    }












    private void loadDeveloperData(){
        DEVELOPER_LIST.clear();
        db.collection("StudentGuidanceDeveloper")
                .addSnapshotListener((value, error) -> {
                    if(error!=null){
                        Log.e("FirestoreError",error.getMessage());
                        return ;
                    }
                    assert value != null;
                    Developer temp;
                    int pos;
                    for (DocumentChange dc:value.getDocumentChanges()) {
                        if(dc.getType()== DocumentChange.Type.ADDED) {
                            temp = createDeveloperFromFireStoreData(dc);
                            if (temp.getName() != null && temp.getRole() != null && temp.getDetails() != null &&
                                    temp.getUrl() != null) {
                                DEVELOPER_LIST.add(temp);
                                if(AboutUsFragment.aboutUsAdapter!=null)
                                    AboutUsFragment.aboutUsAdapter.notifyItemInserted(DEVELOPER_LIST.size()-1);
                            }

                        }
                        else if(dc.getType()==DocumentChange.Type.MODIFIED){
                            pos=getPositionFromDeveloperList(dc.getDocument().getId());
                            temp = createDeveloperFromFireStoreData(dc);
                            if (temp.getName() != null && temp.getRole() != null && temp.getDetails() != null &&
                                    temp.getUrl() != null) {
                                DEVELOPER_LIST.set(pos, temp);
                                if (AboutUsFragment.aboutUsAdapter != null) AboutUsFragment.aboutUsAdapter.notifyItemChanged(pos);
                            }
                        }
                        else{//Type.DELETED
                            pos=getPositionFromDeveloperList(dc.getDocument().getId());
                            if(pos!=-1){
                                DEVELOPER_LIST.remove(pos);
                                if(AboutUsFragment.aboutUsAdapter!=null)AboutUsFragment.aboutUsAdapter.notifyItemRemoved(pos);
                            }
                        }
                    }
                });
    }















    private Developer createDeveloperFromFireStoreData(DocumentChange dc){
        Developer temp=new Developer();
        temp.setDocId(dc.getDocument().getId());
        for (Map.Entry<String,Object> entry: dc.getDocument().getData().entrySet()) {
            switch (entry.getKey()){
                case "name": temp.setName(String.valueOf(entry.getValue()));break;
                case "role":temp.setRole(String.valueOf(entry.getValue()));break;
                case "details":temp.setDetails(String.valueOf(entry.getValue()));break;
                case "url":temp.setUrl(String.valueOf(entry.getValue()));break;
            }
        }
        return temp;
    }













    private int getPositionFromDeveloperList(String docId){
        for(int i=0;i<DEVELOPER_LIST.size();i++){
            if(docId.equals(DEVELOPER_LIST.get(i).getDocId())) return i;
        }
        return -1;
    }












    private void loadFragment(Fragment fragment,int flag){
        FragmentManager fm=getParentFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(flag==0) {
            ft.add(R.id.container,fragment).commit();
            fm.popBackStack(MainActivity.ROOT_FRAGMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else {
            ft.replace(R.id.container,fragment).commit();
            ft.addToBackStack(null);
        }
    }












    private void getPermission(String permission,int requestCode){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.Q ){
            if(checkSelfPermission(requireContext(),permission)!= PackageManager.PERMISSION_GRANTED){
                if(requestCode==MainActivity.RECORD_AUDIO_REQUEST_CODE  && Build.VERSION.SDK_INT<=Build.VERSION_CODES.O ){
                    MainActivity.RECORD_AUDIO_PERMISSION=true;
                }
                else requestPermissions(new String[]{permission},requestCode);
            }else{
                switch (requestCode){
                    case READ_EXTERNAL_STORAGE_REQUEST_CODE:
                        READ_EXTERNAL_STORAGE_GRANTED = true;
                        break;
                    case MainActivity.RECORD_AUDIO_REQUEST_CODE:
                        MainActivity.RECORD_AUDIO_PERMISSION=true;
                        break;

                }
            }
        }else {
            switch (requestCode){
                case READ_EXTERNAL_STORAGE_REQUEST_CODE:
                    READ_EXTERNAL_STORAGE_GRANTED=true;
                    break;
                case MainActivity.RECORD_AUDIO_REQUEST_CODE:
                    if(checkSelfPermission(requireContext(),permission)!= PackageManager.PERMISSION_GRANTED){
                        requestPermissions(new String[]{permission},requestCode);
                    }else{
                        MainActivity.RECORD_AUDIO_PERMISSION=true;
                    }
                    break;
            }

        }

    }













    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==READ_EXTERNAL_STORAGE_REQUEST_CODE){
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED) READ_EXTERNAL_STORAGE_GRANTED=false;
            else READ_EXTERNAL_STORAGE_GRANTED=true;
        }
        if(requestCode==MainActivity.RECORD_AUDIO_REQUEST_CODE){
            if(grantResults[0]!=PackageManager.PERMISSION_GRANTED) MainActivity.RECORD_AUDIO_PERMISSION=false;
            else MainActivity.RECORD_AUDIO_PERMISSION=true;
        }
    }





}