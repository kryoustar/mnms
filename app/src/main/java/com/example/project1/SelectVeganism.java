package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectVeganism extends AppCompatActivity {
    public RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_veganism);
        Button submit = (Button) findViewById(R.id.submit);
        radioGroup = (RadioGroup) findViewById(R.id.groupradio);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ConditionRef1 = dbRef.child("User").child(uid)
                .child("Personal Info").child("Veganism Type");

        ConditionRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String veganType = dataSnapshot.getValue(String.class);

                    if (veganType.matches("비건")) {
                        RadioButton vegan = findViewById(R.id.vegan);
                        vegan.setChecked(true);

                    } else if (veganType.matches("오보")) {
                        RadioButton ovo = findViewById(R.id.ovo);
                        ovo.setChecked(true);

                    } else if (veganType.matches("락토")) {
                        RadioButton lacto = findViewById(R.id.lacto);
                        lacto.setChecked(true);

                    } else if (veganType.matches("락토 오보")) {
                        RadioButton ovolacto = findViewById(R.id.ovolacto);
                        ovolacto.setChecked(true);

                    } else if (veganType.matches("페스코")) {
                        RadioButton pesco = findViewById(R.id.pesco);
                        pesco.setChecked(true);

                    } else {
                        RadioButton none = findViewById(R.id.none);
                        none.setChecked(true);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton) group
                                .findViewById(checkedId);

                    }
                });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef;//= database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String uid = user.getUid();
                    dbRef = database.getReference("/User");
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if (selectedId == -1) {
                        Toast.makeText(SelectVeganism.this,
                                "Please select one",
                                Toast.LENGTH_SHORT)
                                .show();
                    } else {

                        RadioButton radioButton
                                = (RadioButton) radioGroup
                                .findViewById(selectedId);

                        Toast.makeText(SelectVeganism.this,
                                radioButton.getText(),
                                Toast.LENGTH_SHORT)
                                .show();
                        dbRef.child(uid).child("Personal Info").child("Veganism Type").setValue(radioButton.getText().toString());
                        Intent gobackIntent = new Intent(SelectVeganism.this, BottomActivity.class);

                        startActivity(gobackIntent);
                    }
                }
            }
        });
    }
}