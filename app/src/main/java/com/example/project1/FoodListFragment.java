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

public class FoodListFragment extends Fragment{
    public boolean isBreakfast = false;
    public boolean isLunch = false;
    public boolean isDinner = false;
    public boolean isSnack = false;

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
        //setTitle("식단관리");

        breakfast_add = view.findViewById(R.id.breakfast_add);
        lunch_add = view.findViewById(R.id.lunch_add);
        dinner_add = view.findViewById(R.id.dinner_add);
        snack_add = view.findViewById(R.id.snack_add);

        breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodSearch.class);
                isBreakfast = true;
                intent.putExtra("isBreakfast",isBreakfast);
                startActivity(intent);
            }
        });

        lunch_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodSearch.class);
                isLunch = true;
                intent.putExtra("isLunch",isLunch);
                startActivity(intent);
            }
        });

        dinner_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodSearch.class);
                isDinner = true;
                intent.putExtra("isDinner",isDinner);
                startActivity(intent);
            }
        });

        snack_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodSearch.class);
                isSnack = true;
                intent.putExtra("isSnack",isSnack);

                startActivity(intent);
            }
        });


        return view;
    }
}
