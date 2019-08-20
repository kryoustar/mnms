package com.example.project1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FoodListFragment extends Fragment{

    //Intent intent = getIntent();
    public boolean isBreakfast = false;
    public boolean isLunch = false;
    public boolean isDinner = false;
    public boolean isSnack = false;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("UserId");

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.food_list,container,false);
        Button breakfast_add;
        Button lunch_add;
        Button dinner_add;
        Button snack_add;

        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(time);

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
}
