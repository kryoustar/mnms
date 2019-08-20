package com.example.project1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodListFragment extends Fragment{

    //Intent intent = getIntent();
    Bundle bundle = getArguments();

    boolean isBreakfast;
    boolean isLunch;
    boolean isDinner;
    boolean isSnack;


   // DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference conditionRef = mRootRef.child("UserId");
    TextView breakfastView, lunchView, dinnerView, snackView;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.food_list,container,false);
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(time);
        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        DatabaseReference ConditionRef = Database.child("User")
                .child(uid).child("Meal")
                .child(date);



        breakfastView = view.findViewById(R.id.breakfast_list);

        ConditionRef.child("Breakfast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer breakfastIndex = dataSnapshot.getValue(Integer.class);
                if(breakfastIndex !=null){
                    FoodItem breakfastItem = FoodItemSearch(breakfastIndex);
                    breakfastView.setText(breakfastItem.getFoodName());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        lunchView = view.findViewById(R.id.lunch_list);
        ConditionRef.child("Lunch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer lunchIndex = dataSnapshot.getValue(Integer.class);
                if(lunchIndex !=null){
                    FoodItem lunchItem = FoodItemSearch(lunchIndex);
                    lunchView.setText(lunchItem.getFoodName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dinnerView = view.findViewById(R.id.dinner_list);
        ConditionRef.child("Dinner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer dinnerIndex = dataSnapshot.getValue(Integer.class);
                if(dinnerIndex !=null){
                    FoodItem dinnerItem = FoodItemSearch(dinnerIndex);
                    dinnerView.setText(dinnerItem.getFoodName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        snackView = view.findViewById(R.id.snack_list);
        ConditionRef.child("Snack").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer snackIndex = dataSnapshot.getValue(Integer.class);
                if(snackIndex != null){
                    FoodItem snackItem = FoodItemSearch(snackIndex);
                    snackView.setText(snackItem.getFoodName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        Button breakfast_add;
        Button lunch_add;
        Button dinner_add;
        Button snack_add;
        if (bundle ==null){
            isBreakfast = false;
            isLunch = false;
            isDinner = false;
            isSnack = false;
        }
        else{
            isBreakfast = bundle.getBoolean("BreakfastFlag",false);
            isLunch = bundle.getBoolean("LunchFlag",false);
            isDinner = bundle.getBoolean("DinnerFlag",false);
            isSnack = bundle.getBoolean("SnackFlag", false);
        }


        //setTitle("식단관리");


        breakfast_add = view.findViewById(R.id.breakfast_add);
        lunch_add = view.findViewById(R.id.lunch_add);
        dinner_add = view.findViewById(R.id.dinner_add);
        snack_add = view.findViewById(R.id.snack_add);
        Intent intent = new Intent(getActivity(), FoodSearch.class);
        intent.putExtra("Date",date);

        breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBreakfast = true;
                intent.putExtra("isBreakfast",isBreakfast);
                startActivity(intent);
            }
        });

        lunch_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLunch = true;
                intent.putExtra("isLunch",isLunch);
                startActivity(intent);
            }
        });

        dinner_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDinner = true;
                intent.putExtra("isDinner",isDinner);
                startActivity(intent);
            }
        });

        snack_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSnack = true;
                intent.putExtra("isSnack",isSnack);
                startActivity(intent);
            }
        });


        return view;
    }
    public FoodItem FoodItemSearch(Integer keyIndex){
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        db = getActivity().openOrCreateDatabase("nutrients.db",android.content.Context.MODE_PRIVATE,null);
        sql = "select * from tb_nutrients where number = " +keyIndex;
        c = db.rawQuery(sql, null);
        c.moveToNext();
        FoodItem foodItem = new FoodItem(c.getString(2));
        return foodItem;
    }

}
