package com.example.cesamockv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseReference reff;

    private FirebaseAuth mAuth;
    private static final String TAG = "LOGTOKEN:";
    private String token;
    private String uid;
    String vin;
    String newString;

    TextView tvName;
    private TextView tvCarInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);


        // Notification Setup
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference(); // setting database
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        Log.d(TAG, "uid = " + uid );

        //mDatabase = mDatabase.child(uid); // Accessing user database



        tvName = findViewById(R.id.tvName);
        MaterialCardView carinfo_button = findViewById(R.id.btnCarInfo);
        MaterialCardView carlocation_button = findViewById(R.id.btnCarLocation);
        MaterialCardView carcheckengine_button = findViewById(R.id.btnCheckEngine);
        MaterialCardView caralerts_button = findViewById(R.id.btnAlerts);

        ImageView ivSignout = findViewById(R.id.ivSignOut);
        ImageView profile = findViewById(R.id.ivProfile);

        carinfo_button.setOnClickListener(this);
        carlocation_button.setOnClickListener(this);
        carcheckengine_button.setOnClickListener(this);
        caralerts_button.setOnClickListener(this);
        ivSignout.setOnClickListener(this);
        profile.setOnClickListener(this);

        reff = FirebaseDatabase.getInstance().getReference().child(uid);

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Name").getValue().toString();
                tvName.setText(name);

                vin = snapshot.child("Vin").getValue().toString();

//                //        Retrofit car api
//                tvCarInfo = findViewById(R.id.tvCarInfo);
//
//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("http://api.carmd.com/v3.0/")
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                CarMDApi carMDApi = retrofit.create(CarMDApi.class);
//                Log.e(TAG, " VIN = " + vin);
//                Call<VinDecode> call = carMDApi.getDecode(vin);
//
//                call.enqueue(new Callback<VinDecode>() {
//                    @Override
//                    public void onResponse(Call<VinDecode> call, Response<VinDecode> response) {
//
//                        Log.i(TAG, "This is a message:" + response.message());
////                Log.i(TAG, "This is a message:" + response.body());
////                Log.i(TAG, "This is a message:" + response.toString());
//
//                        if(!response.isSuccessful()){
//                            Toast.makeText(getApplicationContext(), "Code:" +response.code(),Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        VinDecode decode = response.body();
////                        Log.e(TAG, "This is a message:" + decode.getMessage().getMessage());
////                        Log.e(TAG, "This is a code:" + decode.getMessage().getCode());
////                        Log.e(TAG, "This is a credentials:" + decode.getMessage().getCredentials());
////                        Log.e(TAG, "This is a version:" + decode.getMessage().getVersion());
////                        Log.e(TAG, "This is a endpoint:" + decode.getMessage().getEndpoint());
////                        Log.e(TAG, "This is a count:" + decode.getMessage().getEndpoint());
//                        String content = "";
////                        content += decode.getData().getYear() + " ";
////                        content += decode.getData().getMake() + " ";
////                        content += decode.getData().getModel() + " ";
//
//                        tvCarInfo.append(content);
//                    }
//
//                    @Override
//                    public void onFailure(Call<VinDecode> call, Throwable t) {
//                        Log.e(TAG, "API call failed", t);
//                    }
//                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                newString= null;
//            } else {
//                newString= extras.getString("name");
//            }
//        } else {
//            newString= (String) savedInstanceState.getSerializable("name");
//        }
//
//        tvName.setText(newString);



        // Notification token
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.d(TAG, "Fetching FCM registration token failed", task.getException());
                    return;
                }

                // Get FCM token
                token = task.getResult();

                // Log and toast
                //String msg = getString(R.string.msg_token_ftm, token);
                Log.d(TAG, token);
                mDatabase.child(uid).child("FCM").setValue(token);
                // Consider later on getting user input of device id in settings
                mDatabase.child("RPI_02").child("DeviceInfo").child("FCM").setValue(token);
            }
        });
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnCarInfo:
                Intent intent1 = new Intent(MainActivity.this, CarInfoActivity.class);
//                        new Intent(this, CarInfoActivity.class);
                startActivity(intent1);
                break;
            case R.id.btnCheckEngine:
                Intent intent2 = new Intent(MainActivity.this, CheckEngineActivity.class);
//                        new Intent(this, CarInfoActivity.class);
                startActivity(intent2);
                break;
            case R.id.btnCarLocation:
                Intent intent3 = new Intent(MainActivity.this, CarMapsActivity.class);
//                        new Intent(this, CarInfoActivity.class);
                startActivity(intent3);
                break;
            case R.id.btnAlerts:
                Intent intent4 = new Intent(MainActivity.this, CarAlertsActivity.class);
//                        new Intent(this, CarInfoActivity.class);
                startActivity(intent4);
                break;
            case R.id.ivSignOut:
                FirebaseAuth.getInstance().signOut();
                Intent intent5 = new Intent(MainActivity.this, signin.class);
//                        new Intent(this, CarInfoActivity.class);
                startActivity(intent5);
                finish();
                break;
            case R.id.ivProfile:
                Intent intent6 = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent6);
                break;

        }
    }
}