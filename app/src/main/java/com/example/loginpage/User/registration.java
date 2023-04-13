package com.example.loginpage.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.loginpage.Login;
import com.example.loginpage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class registration extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword, editTextName, editTextStudentid;
    Button buttonReg, backtologin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore db;
    DocumentReference dbref;
    String IsAdmin = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity_registration);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        backtologin = findViewById(R.id.loginNow);
        editTextName = findViewById(R.id.name);
        editTextStudentid = findViewById(R.id.studentid);
        editTextPassword.setTransformationMethod(new PasswordTransformationMethod());

        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                String name = editTextName.getText().toString();
                String studentid = editTextStudentid.getText().toString();
                db = FirebaseFirestore.getInstance();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(registration.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(registration.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(registration.this, "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(studentid)) {
                    Toast.makeText(registration.this, "Enter studentid", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    String isAdmin = "0";
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(registration.this, "Account created",
                                            Toast.LENGTH_SHORT).show();
                                    dbref = db.collection("Users").document(email);
                                    Map<String, Object> users = new HashMap<>();
                                    users.put("fullname", name);
                                    users.put("studentid", studentid);
                                    users.put("isadmin", isAdmin);
                                    dbref.set(users, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(registration.this, "Registration Successful. Please return to login page", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(registration.this, "Registration failed or user already exists",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(registration.this, "User Already Exists", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}