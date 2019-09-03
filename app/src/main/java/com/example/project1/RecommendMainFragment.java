package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RecommendMainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_main, container, false);
        TextView accumulatedKcalTV = view.findViewById(R.id.accumulated_kcal);
        TextView accumulatedCarbsTV = view.findViewById(R.id.accumulated_carbs);
        TextView accumulatedProteinTV = view.findViewById(R.id.accumulated_protein);
        TextView accumulatedFatTV = view.findViewById(R.id.accumulated_fat);
        TextView accumulatedNatriumTV = view.findViewById(R.id.accumulated_natrium);
        TextView todayTextView = view.findViewById(R.id.dateTV);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String date = formatter.format(today);
        todayTextView.setText(date);  // textview 에 오늘 날짜 설정

        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ConditionRef = Database.child("User")
                .child(uid).child("Meal");


        String weekArray[] = new String[7];
        for (int i = 1; i < 8; i++) {
            Calendar todayCalendar = new GregorianCalendar();
            todayCalendar.add(Calendar.DATE, -i);
            String tempDate;
            String toYear = Integer.toString(todayCalendar.get(Calendar.YEAR));
            String toMonth = Integer.toString(todayCalendar.get(Calendar.MONTH) + 1);
            String toDayofMonth = Integer.toString(todayCalendar.get(Calendar.DATE));
            tempDate = toYear + "-" + toMonth + "-" + toDayofMonth;
            weekArray[i - 1] = tempDate;
            // 일주일 치 날 짜 배열 생성
        }

        ArrayList<Integer> weekMeal = new ArrayList<Integer>(); // 빈 데이터 리스트 생성
        for (int i = 0; i < 7; i++) {
            ConditionRef.child(weekArray[i]).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Integer index = snapshot.getValue(Integer.class);
                        weekMeal.add(index);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                float food_kcal = 0, food_carbs = 0,
                        food_protein = 0, food_fat = 0, food_natrium = 0;

                for (int i = 0; i < weekMeal.size(); i++) {
                    Integer findIndex = weekMeal.get(i);
                    FoodItem foodItem = FoodItem.FoodItemSearch(findIndex, getActivity());
                    food_kcal += foodItem.getFoodKcal();
                    food_carbs += foodItem.getFoodCarbs();
                    food_protein += foodItem.getFoodProtein();
                    food_fat += foodItem.getFoodFat();
                    food_natrium += foodItem.getFoodNatrium();


                }
                accumulatedKcalTV.setText("열량 : "+Float.toString(food_kcal));
                accumulatedCarbsTV.setText("탄수화물 : "+Float.toString(food_carbs));
                accumulatedProteinTV.setText("단백질 : "+Float.toString(food_protein));
                accumulatedFatTV.setText("지방 : "+Float.toString(food_fat));
                accumulatedNatriumTV.setText("나트륨 : " + Float.toString(food_natrium));

            }
        }, 100000);

        return view;
    }
}