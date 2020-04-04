package com.pratik.healthappchemist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    TextView PhoneNumberTextView, GenderTextView;
    TextInputEditText NameTextInput, AgeTextInput;
    TextInputEditText StateTextInput, CityTextInput, AreaTextInput, AddressLineTextInput;
    MaterialButton SignOutButton, UpdateInfoButton;
    String phoneno, gender;
    //Firebase Auth
    private FirebaseAuth mAuth;
    //Firebase Firestore
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        //Firebase Firestore
        db = FirebaseFirestore.getInstance();

        PhoneNumberTextView = findViewById(R.id.textViewPhoneNumber);
        NameTextInput = findViewById(R.id.textInputEditTextName);
        AgeTextInput = findViewById(R.id.textInputEditTextAge);

        GenderTextView = findViewById(R.id.textViewGender);

        StateTextInput = findViewById(R.id.textInputEditTextState);
        CityTextInput = findViewById(R.id.textInputEditTextCity);
        AreaTextInput = findViewById(R.id.textInputEditTextArea);
        AddressLineTextInput = findViewById(R.id.textInputEditTextLine);

        UpdateInfoButton = findViewById(R.id.btnUpdateInfo);
        SignOutButton = findViewById(R.id.btnSignOut);

        phoneno = mAuth.getCurrentUser().getPhoneNumber();
        DocumentReference docRef = db.collection("users").document(phoneno + "c");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("UserFetch", "DocumentSnapshot data: " + document.getData());
                        PhoneNumberTextView.setText(mAuth.getCurrentUser().getPhoneNumber());
                        NameTextInput.setText(document.get("name").toString());
                        AgeTextInput.setText(document.get("age").toString());
                        GenderTextView.setText(document.get("gender").toString());
                        StateTextInput.setText(document.get("state").toString());
                        CityTextInput.setText(document.get("city").toString());
                        AreaTextInput.setText(document.get("area").toString());
                        AddressLineTextInput.setText(document.get("addressline").toString());
                        gender = document.get("gender").toString();
                    } else {
                        Log.d("UserFetch", "No such document");
                        Toast.makeText(ProfileActivity.this, "User Not Found!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.d("UserFetch", "get failed with ", task.getException());
                    Toast.makeText(ProfileActivity.this, "Fetch Failed!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        UpdateInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                int checked = GenderRadioGroup.getCheckedRadioButtonId();
//                if (checked == -1) {
//                    Toast.makeText(ProfileActivity.this, "Select User Type!", Toast.LENGTH_SHORT).show();
//                    return;
//                } else if (checked == R.id.radioButtonMale)
//                    gender = "Male";
//                else if (checked == R.id.radioButtonFemale)
//                    gender = "Female";
//                else if (checked == R.id.radioButtonOthers)
//                    gender = "Others";

                final Map<String, Object> user = new HashMap<>();
                user.put("userType", "chemist");
                user.put("name", NameTextInput.getText().toString());
                user.put("age", Integer.parseInt(AgeTextInput.getText().toString()));
                user.put("gender", gender);
                user.put("state", StateTextInput.getText().toString().toLowerCase());
                user.put("city", CityTextInput.getText().toString().toLowerCase());
                user.put("area", AreaTextInput.getText().toString().toLowerCase());
                user.put("addressline", AddressLineTextInput.getText().toString().toLowerCase());

                db.collection("users").document(phoneno + "c")
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("UserAdd", "DocumentSnapshot successfully written!");
                                Toast.makeText(ProfileActivity.this, "Information Updated!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("UserAdd", "Error writing document", e);
                                Toast.makeText(ProfileActivity.this, "Information Not Updated!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
