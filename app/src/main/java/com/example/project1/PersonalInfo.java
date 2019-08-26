package com.example.project1;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.EOFException;

public class PersonalInfo extends AppCompatActivity {
    public EditText nicknameTV, ageTV;
    public Button register_btn;
    public RadioGroup radioGender;
    public RadioButton gender_female_btn, gender_male_btn, gender_non_btn;
    public FirebaseAuth mAuth;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinfo_edit);

        mAuth = FirebaseAuth.getInstance();
        initializeUI();

        radioGender.setOnCheckedChangeListener(
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
            //null값 처리
            if (selectedId == -1) {
                Toast.makeText(PersonalInfo.this, "Please select one", Toast.LENGTH_SHORT).show();
            }

            else if (nickname.matches("")||age.matches("")) {
                Toast.makeText(PersonalInfo.this, "입력해주세요.", Toast.LENGTH_LONG).show();
            }
            else{
                RadioButton radioButton
                        = (RadioButton) radioGender
                        .findViewById(selectedId);
                dbRef.child(uid).child("Personal Info").child("Nickname").setValue(nickname); //닉네임 저장
                dbRef.child(uid).child("Personal Info").child("Age").setValue(age); //나이 저장
                dbRef.child(uid).child("Personal Info").child("Gender").setValue(radioButton.getText().toString()); //나이 저장

                Toast.makeText(PersonalInfo.this, "개인정보가 수정되었습니다.", Toast.LENGTH_LONG).show();
                Intent gobackIntent = new Intent(PersonalInfo.this, BottomActivity.class);
                startActivity(gobackIntent);
                //progressBar.setVisibility(View.GONE);
            }
        }
    }


    private void initializeUI() {
        nicknameTV = findViewById(R.id.nickname);

        ageTV = findViewById(R.id.age);

        register_btn = findViewById(R.id.inforegister);
        radioGender = findViewById(R.id.gender);
        //gender_female_btn = findViewById(R.id.gender_female);
        //gender_male_btn = findViewById(R.id.gender_male);
        //gender_non_btn = findViewById(R.id.gender_non);
        progressBar = findViewById(R.id.progressBar);
    }
}