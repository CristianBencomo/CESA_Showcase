package com.example.cesamockv3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Sign_up extends AppCompatActivity {

    private static final String TAG = "LOGTOKEN:";

    private EditText email_field;
    private EditText password_field;
    private EditText fullname_field;
    private EditText vin_number;
    private EditText mile_age;
    private EditText passwordConfirm_field;
    private Button register_button;
    private Button noAccount;
    private FirebaseAuth mAuth;
    private String uid;
    private DatabaseReference mDatabase;
    private boolean isDataValid, isEmailValid;

    private String email, password, fullName, vin, miles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        isDataValid = true;
        email_field = (EditText) findViewById(R.id.email_field);
        password_field = (EditText) findViewById(R.id.password_field);
        fullname_field = (EditText) findViewById(R.id.fullname_field);
        vin_number = (EditText) findViewById(R.id.vinNumber);
        mile_age = (EditText) findViewById(R.id.mileage);
        passwordConfirm_field = (EditText) findViewById(R.id.passwordConf_field);
        register_button = (Button) findViewById(R.id.signin_button);
        noAccount = (Button) findViewById(R.id.signup_button);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating
                //Validate the data
                checkData(password_field);
                checkData(fullname_field);
                checkData(vin_number);
                checkData(mile_age);
                checkEmail(email_field);
                checkData(passwordConfirm_field);

                //check if email and all fields are valid
                if(!password_field.getText().toString().equals(passwordConfirm_field.getText().toString())) {
                    isDataValid = false;
                    passwordConfirm_field.setError("Passwords do not match");
                }
                if(!isDataValid || !isEmailValid) {
                    Toast.makeText(getApplicationContext(), "Invalid Input", Toast.LENGTH_LONG).show();
                }
                else {
                    isDataValid = true;

                    email = email_field.getText().toString();
                    password = password_field.getText().toString();
                    fullName = fullname_field.getText().toString();
                    vin = vin_number.getText().toString();
                    miles = mile_age.getText().toString();


                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(fullName)
                                        .build();

                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                currentUser.updateProfile(profileUpdate);



                                uid = mAuth.getUid();
                                Log.d(TAG, "uid = " + uid );

                                mDatabase.child(uid).child("Vin").setValue(vin);
                                mDatabase.child(uid).child("Mileage").setValue(miles);
                                mDatabase.child(uid).child("Email").setValue(email);
                                mDatabase.child(uid).child("Name").setValue(fullName);
                                mDatabase.child(uid).child("Linked Device").setValue("RPI_01");
//                                Log.d(TAG, "Check: " + uid);
                                Toast.makeText(getApplicationContext(), "Successfully Registered", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(Sign_up.this, MainActivity.class);
                                intent.putExtra("user", currentUser); // passing user, will need to retrieve on main activity
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }


                        }
                    });
                }
            }
        });
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_up.this, signin.class);
                startActivity(intent);
            }
        });
    }
    public void OnStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            Intent intent = new Intent(Sign_up.this, Sign_up.class);
            startActivity(intent);
        }
    }


    public void checkData(EditText field)
    {
        if(field.getText().toString().isEmpty()){
            isDataValid = false;
            field.setError("Required Field.");
        }
        else if(isDataValid){
            isDataValid = true;
        }
        else {
            isDataValid = false;
        }
    }

    public void checkEmail(EditText email){
        if(android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            isEmailValid = true;
        }
        else{
            isEmailValid = false;
            email.setError("Enter a valid email.");
        }
    }
}