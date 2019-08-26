package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SelectVeganism extends AppCompatActivity {
    public RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_veganism);
        SharedPreferences pref = getSharedPreferences("preferencesName", MODE_PRIVATE);
        final SharedPreferences.Editor prefEdit = pref.edit();
        Button submit = (Button) findViewById(R.id.submit);
        //clear = (Button) findViewById(R.id.clear);
        radioGroup = (RadioGroup) findViewById(R.id.groupradio);

        // Uncheck or reset the radio buttons initially
        radioGroup.clearCheck();

        // Add the Listener to the RadioGroup
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override

                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {

                        // Get the selected Radio Button
                        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
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
                        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    radioButton.setChecked(true);
                                } else {
                                    radioButton.setChecked(false);
                                }
                                //prefEdit.putBoolean("booleanValue", true).commit();
                            }
                        });

                        Toast.makeText(SelectVeganism.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                        dbRef.child(uid).child("Personal Info").child("Veganism Type").setValue(radioButton.getText().toString());
                        onBackPressed();
                    }
                }
            }
        });
/*
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                radioGroup.clearCheck();
            }
        });*/
    }
}