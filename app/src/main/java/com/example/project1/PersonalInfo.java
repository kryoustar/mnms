package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import static com.example.project1.R.layout.personalinfo_edit;

public class PersonalInfo extends AppCompatActivity {
    public EditText nicknameTV, ageTV;
    public Button register_btn;
    public RadioGroup radioGender;
    public FirebaseAuth mAuth;
    //public ProgressBar progressBar;
    public RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(personalinfo_edit);

        mAuth = FirebaseAuth.getInstance();
        initializeUI();

        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInfo();
            }
        });
    }

    private void registerInfo() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            dbRef = database.getReference("/User");
            int selectedId = radioGender.getCheckedRadioButtonId();
            String nickname = nicknameTV.getText().toString();
            String age = ageTV.getText().toString();
            int selectedId2 = radioGroup.getCheckedRadioButtonId();
            //null값 처리
            if (selectedId == -1 || selectedId2 == -1) {
                Toast.makeText(PersonalInfo.this, "Please select one", Toast.LENGTH_SHORT).show();
            } else if (nickname.matches("") || age.matches("")) {
                Toast.makeText(PersonalInfo.this, "입력해주세요.", Toast.LENGTH_LONG).show();
            } else {
                RadioButton radioButton = (RadioButton) radioGender.findViewById(selectedId);
                RadioButton radioButton2
                        = (RadioButton) radioGroup
                        .findViewById(selectedId2);
                dbRef.child(uid).child("Personal Info").child("Nickname").setValue(nickname); //닉네임 저장
                dbRef.child(uid).child("Personal Info").child("Age").setValue(age); //나이 저장
                dbRef.child(uid).child("Personal Info").child("Gender").setValue(radioButton.getText().toString()); //나이 저장
                dbRef.child(uid).child("Personal Info").child("Veganism Type").setValue(radioButton2.getText().toString());
                Toast.makeText(PersonalInfo.this, "개인정보가 수정되었습니다.", Toast.LENGTH_LONG).show();
                Intent gobackIntent = new Intent(PersonalInfo.this, BottomActivity.class);
                startActivity(gobackIntent);

            }

        }
    }


    private void initializeUI() {
        nicknameTV = findViewById(R.id.nickname);
        ageTV = findViewById(R.id.age);
        register_btn = findViewById(R.id.inforegister);
        radioGender = findViewById(R.id.gender);
        //progressBar = findViewById(R.id.progressBar);
        radioGroup = (RadioGroup) findViewById(R.id.groupradio);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference ConditionRef1 = dbRef.child("User").child(uid)
                .child("Personal Info").child("Nickname");

        ConditionRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nickname = dataSnapshot.getValue(String.class);

                    nicknameTV = findViewById(R.id.nickname);
                    nicknameTV.setText(nickname);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ConditionRef2 = dbRef.child("User").child(uid)
                .child("Personal Info").child("Age");

        ConditionRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String age = dataSnapshot.getValue(String.class);

                    ageTV = findViewById(R.id.age);
                    ageTV.setText(age);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ConditionRef3 = dbRef.child("User").child(uid)
                .child("Personal Info").child("Gender");

        ConditionRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String genderType = dataSnapshot.getValue(String.class);

                    if (genderType.matches("여성")) {
                        RadioButton genderFemale = findViewById(R.id.gender_female);
                        genderFemale.setChecked(true);

                    } else if (genderType.matches("남성")) {
                        RadioButton genderMale = findViewById(R.id.gender_male);
                        genderMale.setChecked(true);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        DatabaseReference ConditionRef4 = dbRef.child("User").child(uid)
                .child("Personal Info").child("Veganism Type");

        ConditionRef4.addValueEventListener(new ValueEventListener() {
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

    }


}