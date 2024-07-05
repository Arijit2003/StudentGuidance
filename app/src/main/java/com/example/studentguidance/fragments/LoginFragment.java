package com.example.studentguidance.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentguidance.R;
import com.example.studentguidance.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;


public class LoginFragment extends Fragment {


    EditText usernameET, passwordET;
    Button buttonLogin;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);

        buttonLogin.setOnClickListener(v->{
            if(!usernameET.getText().toString().equals("") && !passwordET.getText().toString().equals("")){
                login(usernameET.getText().toString(),passwordET.getText().toString());
            }else Toast.makeText(requireContext(), "Try to fill all the fields", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void init(View view){
        usernameET=view.findViewById(R.id.usernameET);
        passwordET=view.findViewById(R.id.passwordET);
        buttonLogin=view.findViewById(R.id.buttonLogin);
        mAuth = FirebaseAuth.getInstance();
    }
    private  void login(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        MainActivity.SUCCESSFUL_LOGIN=true;
                        requireActivity().onBackPressed();
                    } else {
                        MainActivity.SUCCESSFUL_LOGIN=false;
                        Toast.makeText(requireContext(), "Login unsuccessful", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}