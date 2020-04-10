package com.pratik.healthappchemist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DashActivity extends AppCompatActivity {

    //Firebase Auth
    private FirebaseAuth mAuth;

    String Respiratory, Neurological, Infectious, Cardiac, Dermatological, Urological, GeneralSurgical, Gastroenterological, Others;

    TextInputEditText AreaTextInput, MonthTextInput, YearTextInput;
    TextView StatisticsTextView;
    MaterialButton GetStatisticsButton, ActiveOffline, ActiveOnline, CompleteOrders;
    //Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash);

        mAuth = FirebaseAuth.getInstance();

        //Firebase Firestore
        db = FirebaseFirestore.getInstance();

        AreaTextInput = findViewById(R.id.textInputArea);
        MonthTextInput = findViewById(R.id.textInputMonth);
        YearTextInput = findViewById(R.id.textInputYear);

        StatisticsTextView = findViewById(R.id.textViewStatistics);

        GetStatisticsButton = findViewById(R.id.btnGetStats);
        ActiveOffline = findViewById(R.id.btnActiveOfflineOrders);
        ActiveOnline = findViewById(R.id.btnActiveOnlineOrders);
        CompleteOrders = findViewById(R.id.btnCompleteOrders);

        GetStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (AreaTextInput.getText().toString().equals(null)) {
                    AreaTextInput.setError("Enter Area!");
                    AreaTextInput.requestFocus();
                    return;
                }

                if (MonthTextInput.getText().toString().equals(null) || MonthTextInput.getText().toString().equals("00") || MonthTextInput.getText().toString().length() != 2) {
                    MonthTextInput.setError("Enter Valid Month!");
                    MonthTextInput.requestFocus();
                    return;
                }
                if (YearTextInput.getText().toString().equals(null) || YearTextInput.getText().toString().length() != 4) {
                    YearTextInput.setError("Enter Valid Year!");
                    YearTextInput.requestFocus();
                    return;
                }
                Snackbar.make(v, "Getting Statistics...", Snackbar.LENGTH_SHORT).show();

                db.collection("statistics").document(MonthTextInput.getText().toString() + YearTextInput.getText().toString() + AreaTextInput.getText().toString().toLowerCase()).
                        get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("Statistics", "DocumentSnapshot data: " + document.getData());
                                Respiratory = document.get("Respiratory").toString();
                                Neurological = document.get("Neurological").toString();
                                Infectious = document.get("Infectious").toString();
                                Cardiac = document.get("Cardiac").toString();
                                Dermatological = document.get("Dermatological").toString();
                                Urological = document.get("Urological").toString();
                                GeneralSurgical = document.get("GeneralSurgical").toString();
                                Gastroenterological = document.get("Gastroenterological").toString();
                                Others = document.get("Others").toString();

                                String stats = "Statistics:" +
                                        "\n\tRespiratory: " + Respiratory +
                                        "\n\tNeurological: " + Neurological +
                                        "\n\tInfectious: " + Infectious +
                                        "\n\tCardiac: " + Cardiac +
                                        "\n\tDermatological: " + Dermatological +
                                        "\n\tUrological: " + Urological +
                                        "\n\tGeneralSurgical: " + GeneralSurgical +
                                        "\n\tGastroenterological: " + Gastroenterological +
                                        "\n\tOthers: " + Others;
                                StatisticsTextView.setText(stats);
                            } else {
                                Log.d("Statistics", "No such document");
                                Snackbar.make(v, "No Statistics Available!", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("Statistics", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });


        ActiveOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashActivity.this, ActiveOfflineActivity.class);
                startActivity(i);
            }
        });

        ActiveOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashActivity.this, ActiveOnlineActivity.class);
                startActivity(i);
            }
        });

        CompleteOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashActivity.this, CompleteActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.userprofile) {
            startActivity(new Intent(getBaseContext(), ProfileActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }
}
