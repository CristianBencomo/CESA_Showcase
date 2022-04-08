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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {

    public static final String TAG = "UserActivity";
    EditText ivUsername;
    EditText ivEmail;
    EditText ivPhone;
    EditText ivVIN;
    Button buttonChanges;

    private String uid;
    private FirebaseAuth mAuth;
    DatabaseReference reff;
    DatabaseReference usernameRef;
    DatabaseReference emailRef;
    DatabaseReference phoneRef;

    String vin, username, mail, nuUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_user);

        ivUsername = findViewById(R.id.ivUsername);
        ivEmail = findViewById(R.id.ivEmail);
       // ivPhone = findViewById(R.id.ivPhone);
        ivVIN = findViewById(R.id.ivRegisteredCar);
        buttonChanges = findViewById(R.id.buttonChanges);

        buttonChanges.setOnClickListener(this::onClick);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        reff = FirebaseDatabase.getInstance().getReference().child(uid);
//        usernameRef = FirebaseDatabase.getInstance().getReference().child("l3FTJaoRhTcchJfpWnB8BjJZUah2").child("Name");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                vin = snapshot.child("Vin").getValue().toString();
                ivVIN.setText(vin);

                username = snapshot.child("Name").getValue().toString();
                ivUsername.setText(username);
                //ivUsername.setText("");
                //nuUsername = ivUsername.getText().toString();

                mail = snapshot.child("Email").getValue().toString();
                ivEmail.setText(mail);

               // String oilChange = snapshot.child("Phone").getValue().toString();
                //ivPhone.setText(oilChange);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.buttonChanges:
               //  usernameRef.child("l3FTJaoRhTcchJfpWnB8BjJZUah2").child("Name").setValue(mail);
                String nuUsername = ivUsername.getText().toString();
                Log.d(TAG, "TestChanges" + nuUsername);
                String nuEmail = ivEmail.getText().toString();
                String nuVin = ivVIN.getText().toString();
                FirebaseDatabase.getInstance().getReference().child(uid).child("Name").setValue(nuUsername);
                FirebaseDatabase.getInstance().getReference().child(uid).child("Vin").setValue(nuVin);
                FirebaseDatabase.getInstance().getReference().child(uid).child("Email").setValue(nuEmail);

                break;
        }
    }






}