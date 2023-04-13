package com.example.loginpage.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.loginpage.Login;
import com.example.loginpage.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AdminLogin extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword;
    String uid;
    Button return_to_login,Administrator_login;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore db;
    DocumentReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_login);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        return_to_login=findViewById(R.id.return_to_user_login);
        Administrator_login=findViewById(R.id.administrator);
        progressBar=findViewById(R.id.progressBar);

        editTextPassword.setTransformationMethod(new PasswordTransformationMethod());

        Administrator_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                db = FirebaseFirestore.getInstance();
                dbref = db.collection("Users").document(editTextEmail.getText().toString());
                progressBar.setVisibility(View.VISIBLE);
                String email =editTextEmail.getText().toString();
                String password =editTextPassword.getText().toString();
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(AdminLogin.this,"Enter email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(AdminLogin.this,"Enter password",Toast.LENGTH_SHORT).show();
                    return;
                }


                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                //    checkUserLevel(authResult.getUser.getUid);
                                    dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            Log.d("TAG","onSuccess" + documentSnapshot.getString("isadmin"));
                                            if(Objects.equals(documentSnapshot.getString("isadmin"), "1")){
                                                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                                finish();
                                            }else{
                                                Toast.makeText(AdminLogin.this,"You are not an admin",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText( AdminLogin.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        return_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}