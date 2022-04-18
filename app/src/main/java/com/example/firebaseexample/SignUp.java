package com.example.firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity {

    EditText emailUp;
    EditText passwordUp;
    Button enter;

    FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        emailUp=findViewById(R.id.editTextSignUpEmail);
        passwordUp=findViewById(R.id.editTextSignUpPassword);
        enter=findViewById(R.id.buttonEnter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=emailUp.getText().toString();
                String userPassword=passwordUp.getText().toString();
                if (userEmail.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(SignUp.this,"Please fill in the credentials",Toast.LENGTH_SHORT).show();
                }
                else {
                    signupFirebase(userEmail, userPassword);
                }
            }
        });
    }
    public void signupFirebase(String userEmail,String userPassword){
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignUp.this,"Your Account is Created",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(SignUp.this,MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(SignUp.this,"SignUp Activity Failed",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}