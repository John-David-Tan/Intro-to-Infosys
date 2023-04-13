package com.example.loginpage.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.loginpage.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminActivity extends AppCompatActivity {

    private BottomNavigationView botNav;

    private Bundle profileBundle;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
    DocumentReference dbref;
    static String studentID = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_main);
        // TODO db not working for now
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        dbref = db.collection("Users").document(user.getEmail());
        dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    studentID = documentSnapshot.getString("studentid");
                }
            }
        });
        // info to send to the fragments. assumes that after logging out and logging back in, mainactivity oncreate is called again
        profileBundle = new Bundle();
        // TODO db not working for now
        profileBundle.putString("email", user.getEmail());
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(profileBundle);

        botNav = findViewById(R.id.bottomNavigationView);
        botNav.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bookings:
                    break;
                case R.id.spaces:
                    replaceFragment(new SpacesFragment());
                    break;
                case R.id.profile:
                    replaceFragment(profileFragment);
                    break;
            }
            return true;
        });
    }

    // replaces the fragment inside framelayout view
    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view_tag, fragment);
        fragmentTransaction.commit();
    }


}