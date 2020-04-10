package com.pratik.healthappchemist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.pratik.healthappchemist.R;
import com.pratik.healthappchemist.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {

    String status;
    //For Button Click Interface
    private OnItemClickListener listener;
    private List<Order> orders;

    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.PatientNameTextView.setText(order.getPatientName());
        holder.DoctorNameTextView.setText(order.getDoctorName());
        holder.DateTextView.setText(order.getDate());
        if (order.getStatus() == 1) {
            status = "pending";
        } else if (order.getStatus() == 2) {
            status = "completed";
        }
        holder.StatusTextView.setText(status);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // For Button Click
    public interface OnItemClickListener {
        void onItemClick(Order order, int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView PatientNameTextView, DoctorNameTextView, DateTextView, StatusTextView;
        MaterialButton ViewPrescriptionButton;

        MyViewHolder(View view) {
            super(view);
            PatientNameTextView = view.findViewById(R.id.textViewPatientName);
            DoctorNameTextView = view.findViewById(R.id.textViewDoctorName);
            DateTextView = view.findViewById(R.id.textViewDate);
            StatusTextView = view.findViewById(R.id.textViewStatus);
            ViewPrescriptionButton = view.findViewById(R.id.btnViewPrescription);

            ViewPrescriptionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(orders.get(position), position);
                    }
                }
            });
        }
    }
}