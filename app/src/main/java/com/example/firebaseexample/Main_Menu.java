package com.example.firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Main_Menu extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        signOut=findViewById(R.id.buttonSignOut);
        String y=getIntent().getStringExtra("bool_value");
        Log.d("akshay",y);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (y.equals("0")) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(Main_Menu.this,"Logged out with username",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Main_Menu.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();
                    mGoogleSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);
                    mGoogleSignInClient.signOut()
                            .addOnCompleteListener(Main_Menu.this, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(Main_Menu.this,"signed out",Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(Main_Menu.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });
    }
}