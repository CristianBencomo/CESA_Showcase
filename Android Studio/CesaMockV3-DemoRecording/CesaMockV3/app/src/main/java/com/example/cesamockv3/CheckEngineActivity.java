package com.example.cesamockv3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.cesamockv3.Models.EngineCode;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CheckEngineActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
//    private SwipeRefreshLayout swipeContainer;
    EngineCodeAdapter adapter;
    DatabaseReference mdatabase;

    DatabaseReference reff;
    private FirebaseAuth mAuth;
    private String uid;
    String vin;

    TextView tvVinNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_check_engine);

        // Create a instance of the database and get
        // its reference
        mdatabase = FirebaseDatabase.getInstance().getReference().child("RPI_01").child("codes");

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();

        tvVinNumber = findViewById(R.id.tvVinNumber);

        reff = FirebaseDatabase.getInstance().getReference().child(uid);

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                vin = snapshot.child("Vin").getValue().toString();
                tvVinNumber.setText(vin);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView = findViewById(R.id.rvCodes);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<EngineCode> options = new FirebaseRecyclerOptions.Builder<EngineCode>().setQuery(mdatabase, EngineCode.class).build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new EngineCodeAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);
//        Button back_button = findViewById(R.id.backButton2);
//        back_button.setOnClickListener(this::onClick);
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }

//    public void onClick(View v) {

//        switch(v.getId()){
//            case R.id.backButton2:
//                Intent intent1 = new Intent(CheckEngineActivity.this,MainActivity.class);
//                startActivity(intent1);
//                break;
//        }
//    }

}