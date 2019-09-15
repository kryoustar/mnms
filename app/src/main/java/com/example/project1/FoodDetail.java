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
        //setContentView(R.layout.food_detail);
        setContentView(R.layout.food_detail);

        TextView amount = (TextView) findViewById(R.id.food_amount);
        TextView kcal =  (TextView) findViewById(R.id.food_kcal);
        TextView protein =  (TextView) findViewById(R.id.food_protein);
        TextView fat =  (TextView) findViewById(R.id.food_fat);
        TextView natrium =  (TextView) findViewById(R.id.food_natrium);
        TextView sugar =  (TextView) findViewById(R.id.food_sugar);
        TextView cholesterol =  (TextView) findViewById(R.id.food_cholesterol);
        TextView saturatedFat =  (TextView) findViewById(R.id.food_saturatedfat);
        TextView transFat =  (TextView) findViewById(R.id.food_transfat);


        //FoodItem foodItem = new FoodItem();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        TextView foodTitle = (TextView) findViewById(R.id.foodname);
        Intent intent = getIntent();
        String foodIndex = intent.getStringExtra("Food number");
        String date = intent.getStringExtra("Date");
        //foodTitle.setText(foodIndex);
        Integer index = Integer.valueOf(foodIndex);
        FoodItem foodItem = FoodItem.FoodItemSearch(index, getApplicationContext());


        foodTitle.setText(foodItem.getFoodName());
        amount.setText(Float.toString(foodItem.getFoodAmount())+" (kcal)");
        kcal.setText(Float.toString(foodItem.getFoodKcal())+" (g)");
        protein.setText(Float.toString(foodItem.getFoodProtein())+" (g)");
        fat.setText(Float.toString(foodItem.getFoodFat())+" (g)");
        natrium.setText("나트륨:\t"+Float.toString(foodItem.getFoodNatrium())+" (mg)");
        sugar.setText("당류:\t"+Float.toString(foodItem.getFoodSugar())+" (g)");
        cholesterol.setText("콜레스테롤:\t"+Float.toString(foodItem.getFoodCholesterol())+" (mg)");
        saturatedFat.setText("포화지방산:\t"+Float.toString(foodItem.getFoodSaturatedfat())+" (g)");
        transFat.setText("트랜스지방산:\t"+Float.toString(foodItem.getFoodTransfat())+" (g)");

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
                           // Toast.makeText(getApplicationContext(), String.valueOf(mealIndex) + " " + foodIndex + " " + deleteKey, Toast.LENGTH_SHORT).show();
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