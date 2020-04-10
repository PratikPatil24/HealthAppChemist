package com.pratik.healthappchemist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pratik.healthappchemist.adapters.OrderAdapter;
import com.pratik.healthappchemist.models.Order;

import java.util.ArrayList;

public class CompleteActivity extends AppCompatActivity {

    //Recycler
    OrderAdapter madapter;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    ArrayList<Order> orders = new ArrayList<>();
    //Firebase Auth
    private FirebaseAuth mAuth;
    //Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);

        mAuth = FirebaseAuth.getInstance();

        //Firebase Firestore
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerComplete);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        getData();
    }

    void getData() {
        Log.d("Orders", "Fetching Complete Orders...");
        db.collection("chemists").document(mAuth.getCurrentUser().getPhoneNumber())
                .collection("completeorders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order order = new Order(document.getId(), document.get("chemistID").toString(), document.get("patientID").toString(),
                                        document.get("doctorName").toString(), document.get("patientName").toString(), document.get("doctorID").toString(),
                                        document.get("doctorSpeciality").toString(), document.get("doctorDegree").toString(), document.get("date").toString(),
                                        document.get("medicines").toString(), document.get("orderType").toString(), document.get("type").toString(), Integer.parseInt(document.get("status").toString()));
                                orders.add(order);

                                Log.d("Document Fetch", document.getId() + " => " + document.getData());
                            }
                            madapter = new OrderAdapter(orders);
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(madapter);
                            madapter.notifyDataSetChanged();

                            //For Button Click
                            madapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(Order order, int position) {
                                    //Toast.makeText(BookAppointmentActivity.this, doctor.getPhoneno(), Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getApplicationContext(), OrderActivity.class);
                                    i.putExtra("Order", order);
                                    startActivity(i);
                                }
                            });

                        } else {
                            Log.d("Document Fetch", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
