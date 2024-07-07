package com.example.studentguidance.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ScrollView;


import com.example.studentguidance.R;

import com.example.studentguidance.activities.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class AddFacultyInfoFragment extends Fragment {

    Button submitBtnClose;
    CircleImageView facultyImage;
    Uri imageUri=null;
    EditText nameET,designationET,departmentET,cabinNumberET,emailET;
    HashMap<String,String> hMapFacultyRecord;
    static final int IMAGE_REQUEST_CODE=100;
    StorageReference storageReference;
    ProgressDialog progressDialogUploading;
    FirebaseFirestore db;
    ScrollView scrollViewAddFaculty;



    public AddFacultyInfoFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_add_faculty_info, container, false);
        init(view);

        submitBtnClose.setOnClickListener(v-> uploadData());
        facultyImage.setOnClickListener(v-> fetchFromImageChooser());
        facultyImage.setOnLongClickListener(view1 -> {
            imageUri=null;
            facultyImage.setImageResource(R.drawable.person_image);
            return true;
        });




        return view;
    }







    public void init(View view){
        submitBtnClose=view.findViewById(R.id.submitBtnClose);
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
        MainActivity.voiceIconFAB.setVisibility(View.INVISIBLE);
    }













    private void uploadData(){
        hMapFacultyRecord.clear();
        if(nameET.getText().toString().equals("") || designationET.getText().toString().equals("") ||
        departmentET.getText().toString().equals("") || cabinNumberET.getText().toString().equals("")|| emailET.getText().toString().equals("")){
            Snackbar.make(scrollViewAddFaculty,"Fields should not be empty",Snackbar.LENGTH_SHORT).show();
        }else{
            progressDialogUploading.setTitle("uploading");
            progressDialogUploading.setMessage("wait...wait...wait...");
            progressDialogUploading.setCancelable(false);
            progressDialogUploading.show();

            if(imageUri!=null) uploadImageToFirebaseStorageAndData();
            else {
                hMapFacultyRecord.put("imagelink", "null");
                uploadDataToFireStore();
            }

        }
    }














     private void uploadImageToFirebaseStorageAndData(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.ENGLISH);
        Date now = new Date();
        String fileName = dateFormat.format(now);

        storageReference= FirebaseStorage.getInstance().getReference("images/"+fileName);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                hMapFacultyRecord.put("imagelink",uri.toString());
                                uploadDataToFireStore();
                            }).addOnFailureListener(e -> {
                                hMapFacultyRecord.put("imagelink","null");
                                uploadDataToFireStore();
                            });
                }).addOnFailureListener(e -> {
                    hMapFacultyRecord.put("imagelink","null");
                    uploadDataToFireStore();
                });

    }

















    private void uploadDataToFireStore(){
        hMapFacultyRecord.put("name",nameET.getText().toString());
        hMapFacultyRecord.put("designation",designationET.getText().toString());
        hMapFacultyRecord.put("department",departmentET.getText().toString());
        hMapFacultyRecord.put("cabinnumber",cabinNumberET.getText().toString());
        hMapFacultyRecord.put("email",emailET.getText().toString());
        db.collection("StudentGuidanceFacultyInfo").add(hMapFacultyRecord)
                .addOnCompleteListener(task -> {
                    clearAllFields();
                    progressDialogUploading.dismiss();
                    showDialog();
                }).addOnFailureListener(e -> {
                    clearAllFields();
                    progressDialogUploading.dismiss();
                    Snackbar.make(scrollViewAddFaculty,"Upload unsuccessful",Snackbar.LENGTH_SHORT).show();

                });
    }













    private void showDialog(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.success_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button doneBtnClose=dialog.findViewById(R.id.doneBtnClose);
        doneBtnClose.setOnClickListener(v-> dialog.dismiss());
        dialog.setCancelable(false);
        dialog.show();
    }














    private void clearAllFields(){
        nameET.setText("");
        designationET.setText("");
        departmentET.setText("");
        cabinNumberET.setText("");
        emailET.setText("");
        imageUri=null;
        facultyImage.setImageResource(R.drawable.person_image);
    }







    private void fetchFromImageChooser(){
        if(HomeFragment.READ_EXTERNAL_STORAGE_GRANTED){
            Intent intent=new Intent(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,IMAGE_REQUEST_CODE);
        }else{
            Snackbar.make(scrollViewAddFaculty,"READ EXTERNAL STORAGE permission not granted",Snackbar.LENGTH_SHORT).show();

        }
    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== RESULT_OK){
            if(data!=null && requestCode==IMAGE_REQUEST_CODE){
                imageUri=data.getData();
                facultyImage.setImageURI(imageUri);
            }
        }
    }
}