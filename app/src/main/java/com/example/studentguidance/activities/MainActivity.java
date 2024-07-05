package com.example.studentguidance.activities;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.annotation.SuppressLint;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.content.res.ColorStateList;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import android.util.Log;
import android.view.MotionEvent;

import android.widget.Toast;


import com.example.studentguidance.ModelClasses.StringExtract;
import com.example.studentguidance.R;
import com.example.studentguidance.fragments.AboutUsFragment;

import com.example.studentguidance.fragments.FacultyListFragment;
import com.example.studentguidance.fragments.HomeFragment;
import com.example.studentguidance.fragments.LoginFragment;

import com.example.studentguidance.fragments.NavigationFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Locale;



public class MainActivity extends AppCompatActivity {



    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FloatingActionButton voiceIconFAB;
    public static String ROOT_FRAGMENT_TAG="ROOT_FRAGMENT_TAG";

    public static boolean RECORD_AUDIO_PERMISSION=false;
    public final  static int RECORD_AUDIO_REQUEST_CODE=56;
    public static boolean SUCCESSFUL_LOGIN=false;
    SpeechRecognizer speechRecognizer;
    FirebaseAuth mAuth;
    String apiKey;




    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
            SUCCESSFUL_LOGIN=true;
    }








    @SuppressLint({"NonConstantResourceId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        loadFragment(new HomeFragment(),0);
        navigationView.setNavigationItemSelectedListener(menuItem->{
            switch (menuItem.getItemId()){
                case R.id.loginAsAdmin:{
                    drawerLayout.close();
                    loadFragment(new LoginFragment(),1);
                    break;
                }
                case  R.id.logout: {
                    drawerLayout.close();
                    mAuth.signOut();
                    SUCCESSFUL_LOGIN=false;
                    if(getSupportFragmentManager().findFragmentById(R.id.container) instanceof FacultyListFragment){
                        getSupportFragmentManager().popBackStack();
                    }

                    break;
                }
                case R.id.aboutus: drawerLayout.close(); loadFragment(new AboutUsFragment(),1); break;
                default:
                    Toast.makeText(this, "Other", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        voiceIconFAB.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_UP:{
                    speechRecognizer.stopListening();
                    return true;
                }
                case MotionEvent.ACTION_DOWN:{
                    startListen();
                    return true;
                }
                default: return true;
            }

        });

    }













    public void init(){
        drawerLayout=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navigationView);
        voiceIconFAB=findViewById(R.id.voiceIconFAB);
        apiKey="AIzaSyDFqAltC7AvxqdfXKwTefcoLxPyXjoQy40";
        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
        toolbar=findViewById(R.id.toolbar);
        mAuth=FirebaseAuth.getInstance();

    }













    private void startListen(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                voiceIconFAB.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_light)));
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                if(bundle!=null){
                    ArrayList<String> stringRes=bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    ArrayList<String> res=StringExtract.getSourceAndDestination(stringRes.get(0));
                    Toast.makeText(MainActivity.this, stringRes.get(0).concat(String.valueOf(res)), Toast.LENGTH_SHORT).show();

                    if(res.size()==2) {
                        String src = NavigationFragment.getCoordinates(res.get(0));
                        String dest = NavigationFragment.getCoordinates(res.get(1));
                        opeGmaps(src,dest);
                    }else Toast.makeText(MainActivity.this, "Repeat Once again", Toast.LENGTH_SHORT).show();
                }
                voiceIconFAB.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), android.R.color.white));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });
        speechRecognizer.startListening(intent);
    }








    private void loadFragment(Fragment fragment,int flag){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        if(flag==0) {
            ft.add(R.id.container,fragment).commit();
            fm.popBackStack(ROOT_FRAGMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else {
            ft.replace(R.id.container,fragment).commit();
            ft.addToBackStack(null);
        }
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
















    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) drawerLayout.close();
        else super.onBackPressed();
    }
}