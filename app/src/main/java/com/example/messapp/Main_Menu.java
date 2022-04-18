package com.example.messapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main_Menu extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    Button signOut;
    TextView name,email;
    ListView listView;
    String dishes[];
    ArrayAdapter<String> adapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    //ImageView signInPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        signOut=findViewById(R.id.sign_out);
        name=findViewById(R.id.name_signedin);
        listView=findViewById(R.id.list);
        dishes=getResources().getStringArray(R.array.menu_card);

        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,dishes);
        listView.setAdapter(adapter);











        //email=findViewById(R.id.email_signedin);
        //signInPhoto=findViewById(R.id.signed_in_photo);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signout();
            }
        });
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            //String personGivenName = acct.getGivenName();
            //String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            if (!personEmail.contains("@iitbhilai.ac.in")){
                Toast.makeText(Main_Menu.this,"Please use iit bhilai mail id",Toast.LENGTH_SHORT).show();
                signout();
            }
            //String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            name.setText("Hello "+personName);
            //email.setText(personEmail);
            //Glide.with(this).load(String.valueOf(personPhoto)).into(signInPhoto);
            DatabaseReference reference=database.getReference().child(personName);
            AlertDialog.Builder alertDialogue=new AlertDialog.Builder(this);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String dish_name=adapterView.getItemAtPosition(i).toString();
                    alertDialogue.setTitle("Confirmation")
                            .setMessage("Do you want to order"+dish_name)
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    reference.child(dish_name).setValue(1);
                                }
                            }).show();
                    alertDialogue.create();
                    //reference.child(dish_name).setValue(1);
                }
            });
        }
    }

    private void signout(){
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












//AlertDialog.Builder alertDialogue=new AlertDialog.Builder(this);
//                    alertDialogue.setTitle("Confirmation")
//                            .setMessage("Do you want to order"+dish_name)
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.cancel();
//                                }
//                            })
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    reference.child(dish_name).setValue(1);
//                                }
//                            }).show();
//                    alertDialogue.create();

//                    reference.child(dish_name).setValue(1);