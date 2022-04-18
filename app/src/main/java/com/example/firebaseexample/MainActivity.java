package com.example.firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button signInbtn;
    Button signUp;
    Button forgot;
    Button googleSignInbtn;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    String y="0"; // If we login through sigin page y will be send as 0 else y will be send as 1 if we login through google
    private static final int RC_SIGN_IN=100;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.editTextEmail);
        password=findViewById(R.id.editTextPassword);
        signInbtn=findViewById(R.id.buttonSignIn);
        signUp=findViewById(R.id.buttonSignUp);
        forgot=findViewById(R.id.buttonforgotmypassword);
        googleSignInbtn=findViewById(R.id.buttonGoogleSignIn);
        Log.d("akshay",y+"main");
        //Simple Sign In Options
        signInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail=email.getText().toString();
                String userPassword=password.getText().toString();
                if (userEmail.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please fill in the credentials",Toast.LENGTH_SHORT).show();
                }
                else{
                    signInFirebase(userEmail,userPassword);
                }
            }
        });

        //To Sign Up if you are a new user
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);

            }
        });

        //If you have forgotten the password
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ForgetActivity.class);
                startActivity(intent);
            }
        });

        //Using Google Email Id for SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        googleSignInbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }
    public void signInFirebase(String userEmail,String userPassword){
        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(MainActivity.this,Main_Menu.class);
                            intent.putExtra("bool_value",y);
                            startActivity(intent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"Invalid Credentials",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user= auth.getCurrentUser();
        if (user!=null){
            Toast.makeText(MainActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,Main_Menu.class);
            intent.putExtra("bool_value",y);
            startActivity(intent);
            finish();
        }
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account!=null){
            Toast.makeText(MainActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,Main_Menu.class);
            y="1";
            intent.putExtra("bool_value",y);
            startActivity(intent);
            finish();
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
            }
            Toast.makeText(MainActivity.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,Main_Menu.class);
            y="1";
            intent.putExtra("bool_value",y);
            startActivity(intent);
            finish();
            // Signed in successfully, show authenticated UI
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("akshay", "signInResult:failed code=" + e.getStatusCode());

        }
    }
}