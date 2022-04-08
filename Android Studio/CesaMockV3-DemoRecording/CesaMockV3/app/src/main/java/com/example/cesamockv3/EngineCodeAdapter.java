package com.example.cesamockv3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cesamockv3.Models.EngineCode;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class EngineCodeAdapter extends FirebaseRecyclerAdapter<EngineCode, EngineCodeAdapter.CodeViewholder> {

    public EngineCodeAdapter(@NonNull FirebaseRecyclerOptions<EngineCode> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void onBindViewHolder(@NonNull EngineCodeAdapter.CodeViewholder holder,
                                    int position, @NonNull EngineCode model)
    {

        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.ecode.setText(model.getCode());

    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public EngineCodeAdapter.CodeViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_code, parent, false);
        return new EngineCodeAdapter.CodeViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class CodeViewholder extends RecyclerView.ViewHolder {
        TextView ecode;

        public CodeViewholder(@NonNull View itemView)
        {
            super(itemView);

            ecode = itemView.findViewById(R.id.ecode);

        }
    }
}
