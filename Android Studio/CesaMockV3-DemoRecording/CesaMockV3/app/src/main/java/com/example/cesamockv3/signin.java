package com.example.cesamockv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signin extends AppCompatActivity {


    public static final String TAG = "CustomAuthActivity";
    private EditText email_field;
    private EditText password_field;
    private Button login_button, register_button;
    boolean isDataValid;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener AuthListener;
    private String password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("log", "Check here");
        isDataValid = true;
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_signin);
        password_field = (EditText) findViewById(R.id.password_field);
        email_field = (EditText) findViewById(R.id.email_field);
        login_button = (Button) findViewById(R.id.signin_button);
        register_button = (Button) findViewById(R.id.signup_button);

        mAuth = FirebaseAuth.getInstance();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData(password_field);
                checkData(email_field);

                if(!isDataValid) {
                    Toast.makeText(getApplicationContext(), "Missing fields",Toast.LENGTH_SHORT).show();
                }
                else {
                    email = email_field.getText().toString();
                    password = password_field.getText().toString();

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
//                                updateUI(user);
                                //New activity
                                Intent intent = new Intent(signin.this, MainActivity.class);
                                intent.putExtra("user", user); // passing user, will need to retrieve on main activity
                                startActivity(intent);
                                finish();
                            }
                            else { // Sign in fails
                                Toast.makeText(getApplicationContext(), "Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signin.this, Sign_up.class);
                startActivity(intent);
            }
        });

    }
    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            Log.d(TAG, "SignInWithPresistance");
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
        }
        Log.w(TAG, "User not logged in");
    }
    // [END on_start_check_user]

    public void checkData(EditText field)
    {
        if(field.getText().toString().isEmpty()){
            isDataValid = false;
            field.setError("Required Field.");
        }
        else if (isDataValid) {
            isDataValid = true;
        }
        else {
            isDataValid = false;
        }
    }
}