package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class FoodDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_detail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        TextView foodTitle = (TextView) findViewById(R.id.foodname);
        Intent intent = getIntent();
        String foodIndex = intent.getStringExtra("Food number");
        String date = intent.getStringExtra("Date");
        foodTitle.setText(foodIndex);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ConditionRef = ref.child("User").child(uid).child("Meal").child(date);

        Button delete_food;
        delete_food = findViewById(R.id.deletefood);

        delete_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Integer mealIndex = snapshot.getValue(Integer.class);
                            String deleteKey = snapshot.getKey();
                            Toast.makeText(getApplicationContext(), String.valueOf(mealIndex) + " " + foodIndex + " " + deleteKey, Toast.LENGTH_SHORT).show();
                            if (Integer.toString(mealIndex).equals(foodIndex) ) {
                                ConditionRef.child(deleteKey).removeValue();
                                break;
                            }
                           // else{}
                        }

                        Intent intent = new Intent(FoodDetail.this, BottomActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
        });
    }
}