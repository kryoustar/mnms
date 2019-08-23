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

public class PersonalInfo extends AppCompatActivity {
    private EditText nicknameTV, ageTV;
    private Button register_btn;
    private RadioGroup radioGender;
    private RadioButton gender_female_btn, gender_male_btn, gender_non_btn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personalinfo_edit);

        mAuth = FirebaseAuth.getInstance();
        initializeUI();


        /*
        //좃나 안되는 라디오버튼^^ 누가 해줘
        radioGender = (RadioGroup) findViewById(R.id.gender);
        radioGender.clearCheck();
        radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Get the selected Radio Button
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            }
        });

         */


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInfo();
            }
        });
    }

    private void registerInfo() {
        String nickname, age;
        int gender;

        nickname = nicknameTV.getText().toString();
        age = ageTV.getText().toString();

        //gender = radioGender.getCheckedRadioButtonId();
        //int selectedId = radioGender.getCheckedRadioButtonId();
        //RadioButton radioButton = (RadioButton) radioGender.findViewById(selectedId);
        //RadioButton rb = (RadioButton) findViewById(gender);
        //rb.getText().toString();


        /*
        //null값 처리
        if (nickname==null) {
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(Integer.toString(age))) {
            Toast.makeText(getApplicationContext(), "나이를 입력해주세요.", Toast.LENGTH_LONG).show();
            return;
        }
        */


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        dbRef = database.getReference("/User");
        dbRef.child(uid).child("Personal Info").child("Nickname").setValue(nickname); //닉네임 저장
        dbRef.child(uid).child("Personal Info").child("Age").setValue(age); //나이 저장
        //dbRef.child(uid).child("Personal Info").child("Gender").setValue(rb.getText().toString()); //나이 저장
        //dbRef.child(uid).child("Personal Info").child("Gender").setValue(radioButton.getText().toString());

        Toast.makeText(getApplicationContext(), "개인정보가 수정되었습니다.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(PersonalInfo.this, BottomActivity.class);
        startActivity(intent);
        //progressBar.setVisibility(View.GONE);
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