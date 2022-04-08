package com.example.cesamockv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarInfoActivity extends AppCompatActivity {

//    TextView tvVin;
    TextView tvAmbient;
    TextView tvCooltemp;
    TextView tvDist;
    TextView tvFuel;
    TextView tvRPM;
    TextView tvThrottle;
    TextView tvSpeed;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_car_info);
//        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setLogo(R.mipmap.ic_launcher_foreground);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setTitle("Car Diagnostics"); // set the top title
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#FC6161"));
//
//        // Set BackgroundDrawable
//        actionBar.setBackgroundDrawable(colorDrawable);

//        tvVin = findViewById(R.id.vin);
        tvAmbient = findViewById(R.id.ambDegrees);
        tvCooltemp =  findViewById(R.id.tvCooltemp);
        tvDist = findViewById(R.id.tvDist);
        tvFuel = findViewById(R.id.tvFuellevel);
        tvRPM = findViewById(R.id.rpm);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvThrottle = findViewById(R.id.throttle);

        reff = FirebaseDatabase.getInstance().getReference().child("RPI_01").child("01-setTest");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String vin = snapshot.child("VIN").getValue().toString();
//                VinNumber.setText(vin);

                String ambient = snapshot.child("ambient temp").getValue().toString();
                tvAmbient.setText(ambient);

                String cooltemp = snapshot.child("coolant temp").getValue().toString();
                tvCooltemp.setText(cooltemp);

                String distance = snapshot.child("distance").getValue().toString();
                tvDist.setText(distance);

                String fuel = snapshot.child("fuel").getValue().toString();
                tvFuel.setText(fuel);

                String rpm = snapshot.child("rpm").getValue().toString();
                tvRPM.setText(rpm);

                String speed = snapshot.child("speed").getValue().toString();
                tvSpeed.setText(speed);

                String throttle = snapshot.child("throttle").getValue().toString();
                tvThrottle.setText(throttle);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        String title = actionBar.getTitle().toString(); // get the title
//        actionBar.hide(); // or even hide the actionbar
    }
}




//
//    private FirebaseAuth mAuth;
//
//    private String uid;
//
//    TextView tvVin;
//    TextView tvAmbient;
//    TextView tvCooltemp;
//    TextView tvDist;
//    TextView tvFuel;
//    TextView tvRPM;
//    TextView tvThrottle;
//    TextView tvSpeed;
//    DatabaseReference reff;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_car_info);
//
//        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
//        actionBar.setDisplayShowHomeEnabled(true);
//        actionBar.setLogo(R.mipmap.ic_launcher_foreground);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setTitle("Car Diagnostics"); // set the top title
//        ColorDrawable colorDrawable
//                = new ColorDrawable(Color.parseColor("#FC6161"));
//
//        // Set BackgroundDrawable
//        actionBar.setBackgroundDrawable(colorDrawable);
//
//        tvVin = findViewById(R.id.vin);
//        tvAmbient = findViewById(R.id.ambDegrees);
//        tvCooltemp =  findViewById(R.id.tvCooltemp);
//        tvDist = findViewById(R.id.tvDist);
//        tvFuel = findViewById(R.id.tvFuellevel);
//        tvRPM = findViewById(R.id.rpm);
//        tvSpeed = findViewById(R.id.tvSpeed);
//        tvThrottle = findViewById(R.id.throttle);
//
//        mAuth = FirebaseAuth.getInstance();
//        uid = mAuth.getUid();
//
////        reff = FirebaseDatabase.getInstance().getReference().child("RPI_02").child("01-setTest");
//
//        reff = FirebaseDatabase.getInstance().getReference().child("User1");
//
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String vin = snapshot.child("vhuo9HqaYXTAksRxhWGrKVGnnj53").child("VIN").getValue().toString();
//                tvVin.setText(vin);
//
//                String ambient = snapshot.child("RPI_03").child("01-setTest").child("ambient temp").getValue().toString();
//                tvAmbient.setText(ambient);
//                String cooltemp = snapshot.child("RPI_03").child("01-setTest").child("coolant temp").getValue().toString();
//                tvCooltemp.setText(cooltemp);
//
//                String distance = snapshot.child("RPI_03").child("01-setTest").child("distance").getValue().toString();
//                tvDist.setText(distance);
//
//                String fuel = snapshot.child("RPI_03").child("01-setTest").child("fuel").getValue().toString();
//                tvFuel.setText(fuel);
//
//                String rpm = snapshot.child("RPI_03").child("01-setTest").child("rpm").getValue().toString();
//                tvRPM.setText(rpm);
//
//                String speed = snapshot.child("RPI_03").child("01-setTest").child("speed").getValue().toString();
//                tvSpeed.setText(speed);
//
//                String throttle = snapshot.child("RPI_03").child("01-setTest").child("throttle").getValue().toString();
//                tvThrottle.setText(throttle);