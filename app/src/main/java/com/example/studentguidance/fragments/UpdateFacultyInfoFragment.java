package com.example.studentguidance.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.example.studentguidance.ModelClasses.Faculty;
import com.example.studentguidance.R;
import com.example.studentguidance.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class UpdateFacultyInfoFragment extends Fragment {

    Button updateBtnClose;
    CircleImageView facultyImage;
    EditText nameET,designationET,departmentET,cabinNumberET,emailET;
    HashMap<String,String> hMapFacultyRecord;

    ProgressDialog progressDialogUploading;
    FirebaseFirestore db;
    ScrollView scrollViewAddFaculty;
    String imageUrl="null";







    public UpdateFacultyInfoFragment() {
        // Required empty public constructor
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_update_faculty_info, container, false);
        init(view);
        assert this.getArguments() != null;
        Faculty faculty= Parcels.unwrap(this.getArguments().getParcelable("FACULTY_OBJ"));


        setData(faculty);
        updateBtnClose.setOnClickListener(v-> updateData(faculty));

        return view;
    }




    public void init(View view){
        updateBtnClose=view.findViewById(R.id.updateBtnClose);
        facultyImage=view.findViewById(R.id.facultyImage);
        nameET=view.findViewById(R.id.nameET);
        designationET=view.findViewById(R.id.designationET);
        departmentET=view.findViewById(R.id.departmentET);
        cabinNumberET=view.findViewById(R.id.cabinNumberET);
        emailET=view.findViewById(R.id.emailET);
        db= FirebaseFirestore.getInstance();
        hMapFacultyRecord=new HashMap<>();
        progressDialogUploading = new ProgressDialog(requireContext());
        scrollViewAddFaculty=view.findViewById(R.id.scrollViewAddFaculty);
    }









    private void setData(Faculty faculty){
        nameET.setText(faculty.getName());
        designationET.setText(faculty.getDesignation());
        departmentET.setText(faculty.getDepartment());
        cabinNumberET.setText(faculty.getCabinnumber());
        emailET.setText(faculty.getEmail());
        imageUrl=faculty.getImagelink();
        Glide.with(requireContext())
                .load(faculty.getImagelink())
                .placeholder(R.drawable.person_image)
                .into(facultyImage);
    }














    private void updateData(Faculty faculty){
        hMapFacultyRecord.clear();
        if(nameET.getText().toString().equals("") || designationET.getText().toString().equals("") ||
                departmentET.getText().toString().equals("") || cabinNumberET.getText().toString().equals("")|| emailET.getText().toString().equals("")){
            Snackbar.make(scrollViewAddFaculty,"Fields should not be empty",Snackbar.LENGTH_SHORT).show();
        }else{
            progressDialogUploading.setTitle("Updating");
            progressDialogUploading.setMessage("wait...wait...wait...");
            progressDialogUploading.setCancelable(false);
            progressDialogUploading.show();


            hMapFacultyRecord.put("imagelink", imageUrl);
            updateDataToFireStore(faculty);


        }
    }
































    private void updateDataToFireStore(Faculty faculty){
        hMapFacultyRecord.put("name",nameET.getText().toString());
        hMapFacultyRecord.put("designation",designationET.getText().toString());
        hMapFacultyRecord.put("department",departmentET.getText().toString());
        hMapFacultyRecord.put("cabinnumber",cabinNumberET.getText().toString());
        hMapFacultyRecord.put("email",emailET.getText().toString());
        db.collection("StudentGuidanceFacultyInfo").document(faculty.getDocumentid())
                .set(hMapFacultyRecord)
                .addOnCompleteListener(task -> {
                    clearAllFields();
                    progressDialogUploading.dismiss();

                    showDialog();
                }).addOnFailureListener(e -> {
                    clearAllFields();
                    progressDialogUploading.dismiss();
                    Snackbar.make(scrollViewAddFaculty,"Update unsuccessful",Snackbar.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                });
    }








    private void showDialog(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button doneBtnClose=dialog.findViewById(R.id.doneBtnClose);
        doneBtnClose.setOnClickListener(v-> {
            dialog.dismiss();
            requireActivity().onBackPressed();
        });
        dialog.setCancelable(false);
        dialog.show();
    }










    private void clearAllFields(){
        nameET.setText("");
        designationET.setText("");
        departmentET.setText("");
        cabinNumberET.setText("");
        emailET.setText("");
        facultyImage.setImageResource(R.drawable.person_image);
    }






















}