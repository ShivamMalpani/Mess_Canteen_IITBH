package com.example.messapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chef_Activity extends AppCompatActivity {
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference reference=database.getReference();
    GoogleSignInClient mGoogleSignInClient;
    Button SignOut;
    RecyclerView ParentRecyclerViewItem;
    LinearLayoutManager layoutManager;

    String user_dishes[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef);
        ParentRecyclerViewItem=findViewById(R.id.parent_recyclerview);
        layoutManager=new LinearLayoutManager(Chef_Activity.this);
        SignOut=findViewById(R.id.chef_sign_out);
        //Log.d("Akshay","Message");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                List<ParentItem> itemList=new ArrayList<>();
                for(DataSnapshot snapshot:datasnapshot.getChildren()){
                    String ma=snapshot.getValue().toString();
                    String[] child = ma.split(",");
                    child[0] = child[0].substring(1);
                    String pa = child[child.length - 1];
                    child[child.length - 1] = child[child.length - 1].substring(0,pa.length()-1);
                    Log.d("Akshay",snapshot.getKey()+""+snapshot.getValue());
                    List<ChildItem> ChildItemList=new ArrayList<>();
                    for(String p: child) {
                        Log.d("Akshay", "onDataChange: " + p);

                        ChildItemList.add(new ChildItem(p));
                    }
                    ParentItem item=new ParentItem(snapshot.getKey(),ChildItemList);
                    itemList.add(item);
                }
                ParentItemAdapter parentItemAdapter=new ParentItemAdapter(itemList);
                ParentRecyclerViewItem.setAdapter(parentItemAdapter);
                ParentRecyclerViewItem.setLayoutManager(layoutManager);
//                ParentRecyclerViewItem.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        itemList.remove(0);
//                    }
//                });


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signout();
            }
        });

    }

    private void signout(){
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(Chef_Activity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Chef_Activity.this,"signed out",Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Chef_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


}