package com.example.cesamockv3;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.cesamockv3.Models.Alert;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;



public class CarAlertsActivity extends AppCompatActivity {

private RecyclerView recyclerView;
private SwipeRefreshLayout swipeContainer;
AlertsAdapter adapter;
DatabaseReference mbase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_alerts);

        // Create a instance of the database and get
        // its reference
        mbase = FirebaseDatabase.getInstance().getReference().child("RPI_01").child("alerts");

        recyclerView = findViewById(R.id.rvAlerts);

        // To display the Recycler view linearly
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<Alert> options = new FirebaseRecyclerOptions.Builder<Alert>().setQuery(mbase, Alert.class).build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new AlertsAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Toast.makeText(getApplicationContext(), "Attemting Refresh",Toast.LENGTH_SHORT).show();
                adapter.startListening();

                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);

                }


            }
        });

        adapter.stopListening();



    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
//    @Override protected void onStart()
//    {
//        super.onStart();
//        adapter.startListening();
//    }

    // Function to tell the app to stop getting
    // data from database on stoping of the activity
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}







//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
// import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.util.Log;
//        import android.view.LayoutInflater;
//        import android.view.View;
//        import android.view.ViewGroup;
//        import android.widget.Button;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//        import com.example.cesamockv3.Models.Alert;
//        import com.google.android.material.card.MaterialCardView;
//        import com.google.firebase.database.DataSnapshot;
//        import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//        import com.google.firebase.database.ValueEventListener;
//
//        import java.io.File;
//        import java.lang.reflect.Array;
//        import java.util.ArrayList;
//        import java.util.List;
//    DatabaseReference reference;
//    TextView tvDetails;
//
//    RecyclerView rvAlerts;
//    public static final String TAG = "CarAlertsActivity";
//    AlertsAdapter adapter;
//    List<Alert> alerts;
//    SwipeRefreshLayout swipeContainer;





            //String a = dataSnapshot.child("Alert1").getValue().toString();
            //tvDetails.setText(a);


            //  }

            //adapter = new AlertsAdapter(CarAlertsActivity.this, alerts);
            //rvAlerts.setAdapter(adapter);

//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                Toast.makeText(CarAlertsActivity.this, "Error Ocurred", Toast.LENGTH_SHORT).show();
//            }
//        });
//                    @Override
//            public void done(List<Post> posts, ParseException e) {
//                if(e != null){
//                    Log.e(TAG, "Issue with getting posts", e);
//                    return;
//                }
//                for(Post post : posts){
//                    Log.i(TAG, "Post:" + post.getDescription() + ", username: " + post.getUser().getUsername());
//                }
//                allPosts.addAll(posts);
//                adapter.notifyDataSetChanged();
//            }
//    }



        //        ParseQuery<Alert> query = ParseQuery.getQuery(Post.class);
//        query.include(Post.KEY_USER);
//        query.setLimit(20);
//        query.addDescendingOrder(Post.KEY_CREATED_KEY);
//        query.findInBackground(new FindCallback<Post>() {
//            @Override
//            public void done(List<Post> posts, ParseException e) {
//                if(e != null){
//                    Log.e(TAG, "Issue with getting posts", e);
//                    return;
//                }
//                for(Post post : posts){
//                    Log.i(TAG, "Post:" + post.getDescription() + ", username: " + post.getUser().getUsername());
//                }
//                allPosts.addAll(posts);
//                adapter.notifyDataSetChanged();
//            }
//        });









//    private void savePost(String description) {
//        Post post = new Post();
//        post.setDescription(description);
//        post.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e != null){
//                    Log.e(TAG, "Error while saving", e);
//                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
//                }
//                Log.i(TAG, "Post save was successful!!");
//                etDescription.setText("");
//                ivPostImage.setImageResource(0);
//            }
//        });
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_car_alerts);
//
//
//        tvDetails = findViewById(R.id.tvDetails);
//        rvAlerts = findViewById(R.id.rvAlerts);
//        rvAlerts.setLayoutManager(new LinearLayoutManager(this));
//        alerts = new ArrayList<Alert>();
//        adapter = new AlertsAdapter(CarAlertsActivity.this, alerts);
//        rvAlerts.setAdapter(adapter);


//        reference = FirebaseDatabase.getInstance().getReference().child("Rafael");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
//                //for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
//                //{
//                    Alert a = dataSnapshot1.getValue(Alert.class);
//                    alerts.add(a);
//
//                    //String a = dataSnapshot.child("Alert1").getValue().toString();
//                    //tvDetails.setText(a);
//
//
//              //  }
//
//                //adapter = new AlertsAdapter(CarAlertsActivity.this, alerts);
//                //rvAlerts.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                Toast.makeText(CarAlertsActivity.this, "Error Ocurred", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//
//        swipeContainer = findViewById(R.id.swipeContainer);
//
//        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//                android.R.color.holo_green_light,
//                android.R.color.holo_orange_light,
//                android.R.color.holo_red_light);
//
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                Toast.makeText(getApplicationContext(), "Attemting Refresh",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
//        swipeContainer.setRefreshing(false);
//
//
//
//
//
//       // rvAlerts.setAdapter(adapter);
//
//        Button back_button = findViewById(R.id.backButton);
//        back_button.setOnClickListener(this::onClick);
//
//
//
//    }
//
//    public void onClick(View v) {
//
//        switch(v.getId()){
//            case R.id.backButton:
//                Intent intent1 = new Intent(CarAlertsActivity.this,MainActivity.class);
//                startActivity(intent1);
//                break;
//        }
//    }
//}





//
//
//    public CarAlertsActivity() {
//
//    }
//
//
//    protected void onCreate(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_car_alerts);
//        rvAlerts = view.findViewById(R.id.rvAlerts);
//
//        swipeContainer = view.findViewById(R.id.swipeContainer);
//        swipeContainer.setColorSchemeColors(
//                getResources().getColor(android.R.color.holo_blue_bright),
//                getResources().getColor(android.R.color.holo_green_light),
//                getResources().getColor(android.R.color.holo_orange_light),
//                getResources().getColor(android.R.color.holo_red_light)
//        );
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                Log.i(TAG, "fetching new data!");
//
//            }
//        });
//
//        alerts = new ArrayList<>();
//        adapter = new AlertsAdapter(this, alerts);
//
//        //Steps to use the recycler view:
//        //0. create the layout for one row in the list
//        //1. create the adapter
//        //2.create the data source
//        //3.set the adapter on the recycler view
//        rvAlerts.setAdapter(adapter);
//
//        //4.set the layout manager on the recycler view
//        rvAlerts.setLayoutManager(new LinearLayoutManager(this));
//        queryPosts();
//    }
//
//
//    protected void queryPosts() {
//        reference = FirebaseDatabase.getInstance().getReference().child("Rafael");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
//                //for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
//                //{
//                Alert a = dataSnapshot1.getValue(Alert.class);
//
//                Log.i(TAG, "Post:" + a.getBody());
//
//                alerts.add(a);
//                adapter.notifyDataSetChanged();
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.i(TAG, String.valueOf(error));
//                Toast.makeText(CarAlertsActivity.this, "Error Ocurred", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }