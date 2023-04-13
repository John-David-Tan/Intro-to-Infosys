package com.example.loginpage.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.loginpage.Login;
import com.example.loginpage.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment{

    View view;
    Button button;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.admin_fragment_profile, container, false);
        button = view.findViewById(R.id.logout);
        textView = view.findViewById(R.id.user_details);

        // insert fragment logic here
        String email = this.getArguments().getString("email");
        textView.setText(email);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getActivity().getApplicationContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }
}