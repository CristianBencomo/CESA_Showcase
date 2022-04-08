package com.example.cesamockv3;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cesamockv3.Models.Alert;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class AlertsAdapter extends FirebaseRecyclerAdapter<Alert, AlertsAdapter.AlertsViewholder> {

    public AlertsAdapter(
            @NonNull FirebaseRecyclerOptions<Alert> options)
    {
        super(options);
    }

    // Function to bind the view in Card view(here
    // "person.xml") iwth data in
    // model class(here "person.class")
    @Override
    protected void onBindViewHolder(@NonNull AlertsViewholder holder,
                     int position, @NonNull Alert model)
    {

        // Add firstname from model class (here
        // "person.class")to appropriate view in Card
        // view (here "person.xml")
        holder.tvDetails.setText(model.getAlert());

    }

    // Function to tell the class about the Card view (here
    // "person.xml")in
    // which the data will be shown
    @NonNull
    @Override
    public AlertsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert, parent, false);
        return new AlertsAdapter.AlertsViewholder(view);
    }

    // Sub Class to create references of the views in Crad
    // view (here "person.xml")
    class AlertsViewholder extends RecyclerView.ViewHolder {
        TextView tvDetails;

        public AlertsViewholder(@NonNull View itemView)
        {
            super(itemView);

            tvDetails = itemView.findViewById(R.id.tvDetails);

        }
    }
}














//
//public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.ViewHolder> {
//
//    private Context context;
//    private List<Alert> alerts;
////            = new ArrayList<Alert>();
//
//    public AlertsAdapter(Context context, List<Alert> alerts){
//        this.context = context;
//        this.alerts = alerts;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_alert, parent, false);
//
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Alert alert = alerts.get(position);
//        holder.bind(alert);
////        holder.tvDetails.setText(alerts.get(position).getBody());
////        Alert alert = alerts.get(position);
////
////        holder.bind(alert);
//
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return alerts.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
////        ImageView ivAlarm;
//        TextView tvDetails;
//
//        public ViewHolder (View itemView) {
//            super(itemView);
//
////            ivAlarm = itemView.findViewById(R.id.ivAlarm);
//            tvDetails = itemView.findViewById(R.id.tvDetails);
//
//        }
//
//        public void bind(Alert alert) {
//            tvDetails.setText(alert.getBody());
//
//        }
//
//
//    }
//}




//    public void clear(){
//        alerts.clear();
//        notifyDataSetChanged();
//    }
//
//    public void addAll(List<Alert> alertList){
//        alerts.addAll(alertList);
//        notifyDataSetChanged();
//    }











