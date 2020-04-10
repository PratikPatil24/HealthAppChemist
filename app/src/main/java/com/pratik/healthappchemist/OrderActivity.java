package com.pratik.healthappchemist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pratik.healthappchemist.models.Order;

import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    Order order;
    TextView PatientNameTextView, DoctorNameTextView, DateTextView, OrderTypeTextView, StatusTextView, MedicinesTextView;
    MaterialButton CompleteOrderButton;
    String status;
    //Firebase Auth
    private FirebaseAuth mAuth;
    //Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        mAuth = FirebaseAuth.getInstance();

        //Firebase Firestore
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();
        order = (Order) i.getSerializableExtra("Order");

        Log.d("Order", order.getChemistID() + order.getDoctorName());

        PatientNameTextView = findViewById(R.id.textViewPatientName);
        DoctorNameTextView = findViewById(R.id.textViewDoctorName);
        DateTextView = findViewById(R.id.textViewDate);
        OrderTypeTextView = findViewById(R.id.textViewOrderType);
        StatusTextView = findViewById(R.id.textViewStatus);
        MedicinesTextView = findViewById(R.id.textViewMedicine);

        CompleteOrderButton = findViewById(R.id.btnComplete);

        PatientNameTextView.setText(order.getPatientName());
        DoctorNameTextView.setText(order.getDoctorName());
        DateTextView.setText(order.getDate());
        OrderTypeTextView.setText(order.getOrderType());

        if (order.getStatus() == 1) {
            status = "Pending";
        } else if (order.getStatus() == 2) {
            status = "Completed";
        }
        StatusTextView.setText(status);
        MedicinesTextView.setText(order.getMedicines());

        if (order.getStatus() == 2) {
            CompleteOrderButton.setVisibility(View.GONE);
        }

        CompleteOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setStatus(2);
                addtocomplete();
                Intent i = new Intent(OrderActivity.this, CompleteActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    void addtocomplete() {

        final Map<String, Object> completeOrder = new HashMap<>();
        completeOrder.put("chemistID", order.getChemistID());
        completeOrder.put("patientID", order.getPatientID());
        completeOrder.put("doctorID", order.getDoctorID());
        completeOrder.put("doctorName", order.getDoctorName());
        completeOrder.put("doctorSpeciality", order.getDoctorSpeciality());
        completeOrder.put("doctorDegree", order.getDoctorDegree());
        completeOrder.put("patientName", order.getPatientName());
        completeOrder.put("medicines", order.getMedicines());
        completeOrder.put("type", order.getType());
        completeOrder.put("date", order.getDate());
        completeOrder.put("orderType", order.getOrderType());
        completeOrder.put("status", 2);

        String day = order.getDate().substring(0, 2);
        String month = order.getDate().substring(3, 5);
        String year = order.getDate().substring(6, 9);

        db.collection("chemists").document(order.getChemistID())
                .collection("completeorders")
                .document(order.getId())
                .set(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Order", "DocumentSnapshot successfully written!");
                        Toast.makeText(OrderActivity.this, "Order Completed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Order", "Error writing document", e);
                        Toast.makeText(OrderActivity.this, "Prescription Not Added!", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection("patients").document(order.getPatientID())
                .collection("orders").document(order.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Order", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Order", "Error deleting document", e);
                    }
                });

        db.collection("chemists").document(order.getChemistID())
                .collection("activeorders").document(order.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Order", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Order", "Error deleting document", e);
                    }
                });

    }
}
