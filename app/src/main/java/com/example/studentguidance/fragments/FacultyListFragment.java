package com.example.studentguidance.fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.studentguidance.Adapters.AboutUsRecyclerAdapter;
import com.example.studentguidance.Adapters.CustomRecyclerAdapter;
import com.example.studentguidance.ModelClasses.Faculty;
import com.example.studentguidance.R;
import com.example.studentguidance.activities.MainActivity;

import java.util.ArrayList;


public class FacultyListFragment extends Fragment {


    RecyclerView recyclerView;
    public static EditText searchEditText;
    public static CustomRecyclerAdapter adapter;








    public FacultyListFragment() {
        // Required empty public constructor
    }


















    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(MainActivity.SUCCESSFUL_LOGIN);
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_faculty_list, container, false);
        init(view);


        adapter=new CustomRecyclerAdapter(requireContext(),HomeFragment.FACULTY_LIST_VIT_BPL,getParentFragmentManager());
        recyclerView.setAdapter(adapter);
        recyclerView.smoothScrollToPosition(HomeFragment.FACULTY_LIST_VIT_BPL.size()-1);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchAndFilter(editable.toString());
            }
        });
        return view;


    }












    private void init(View view){
        recyclerView=view.findViewById(R.id.recyclerView);
        searchEditText=view.findViewById(R.id.searchEditText);
    }














    @SuppressLint("NotifyDataSetChanged")
    private void searchAndFilter(String s){
        if(s.isEmpty()) {
            recyclerView.setAdapter(adapter);
//            adapter.notifyDataSetChanged();
            return;
        }
        ArrayList<Faculty> temp=new ArrayList<>();
        for (Faculty faculty :HomeFragment.FACULTY_LIST_VIT_BPL) {
            if(faculty.getName().toLowerCase().contains(s.toLowerCase())) temp.add(faculty);
        }
        CustomRecyclerAdapter customRecyclerAdapter =new CustomRecyclerAdapter(requireContext(),temp,getParentFragmentManager());
        recyclerView.setAdapter(customRecyclerAdapter);
        adapter.notifyDataSetChanged();
    }











    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_faculty_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }















    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addFacultyDetail: loadFragment(new AddFacultyInfoFragment(),1);break;
        }
        return super.onOptionsItemSelected(item);
    }













    private void loadFragment(Fragment fragment,int flag){
        hideKeyboard();
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








    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        View view = requireActivity().getCurrentFocus();
        if (view == null) {
            view = new View(requireActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }











}