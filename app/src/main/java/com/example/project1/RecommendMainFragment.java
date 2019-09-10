package com.example.project1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
        TextView mLowLabel, mMidLabel, mHighLabel;
        TextView lacknutrients, lacknutrients2;
        BarView mLowBar, mMidBar, mHighBar;

        //Some sample percentage values
        int low;
        int mid;
        int high;
        mLowBar = view.findViewById(R.id.low_bar);
        mMidBar = view.findViewById(R.id.mid_bar);
        mHighBar = view.findViewById(R.id.high_bar);

        mLowLabel = view.findViewById(R.id.low_text);
        mMidLabel = view.findViewById(R.id.mid_text);
        mHighLabel = view.findViewById(R.id.high_text);

        lacknutrients = view.findViewById(R.id.lacknutrients);
        lacknutrients2 = view.findViewById(R.id.lacknutrients2);



        /*
        TextView accumulatedKcalTV = view.findViewById(R.id.accumulated_kcal);
        TextView accumulatedCarbsTV = view.findViewById(R.id.accumulated_carbs);
        TextView accumulatedProteinTV = view.findViewById(R.id.accumulated_protein);
        TextView accumulatedFatTV = view.findViewById(R.id.accumulated_fat);
        TextView accumulatedNatriumTV = view.findViewById(R.id.accumulated_natrium);
        TextView weekKCalTV = view.findViewById(R.id.week_kcal);
        TextView weekCarbsTV = view.findViewById(R.id.week_carbs);
        TextView weekProteinTV = view.findViewById(R.id.week_protein);
        TextView weekFatTV = view.findViewById(R.id.week_fat);
        TextView weekNatriumTV = view.findViewById(R.id.week_natrium);
*/
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
            ConditionRef.child(weekArray[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Integer index = snapshot.getValue(Integer.class);
                        weekMeal.add(index);
                    }

                    //사용자의 gender, age 받아오기
                    String[] result;
                    result = new String[2]; //gender, sex 저장

                    DatabaseReference ConditionRef2 = Database.child("User").child(uid).child("Personal Info");
                    ConditionRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String temp = snapshot.getValue(String.class); //value 받아옴
                                String key = snapshot.getKey(); //key 받아옴

                                if (key.equals("Gender")) {
                                    result[0] = temp;
                                } else if (key.equals("Age")) {
                                    result[1] = temp;
                                }
                            }

                            //띄움
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
                            /*
                            accumulatedKcalTV.setText("열량 : " + Float.toString(food_kcal));
                            accumulatedCarbsTV.setText("탄수화물 : " + Float.toString(food_carbs));
                            accumulatedProteinTV.setText("단백질 : " + Float.toString(food_protein));
                            accumulatedFatTV.setText("지방 : " + Float.toString(food_fat));
                            accumulatedNatriumTV.setText("나트륨 : " + Float.toString(food_natrium));
*/
                            PersonalItem personalItem = PersonalItem.PersonalItemSearch(result[0], result[1], getActivity());
                            Float kcal = personalItem.getPersonKcal() * 7;
                            Float carbs = personalItem.getPersonCarbs() * 7;
                            Float protein = personalItem.getPersonProtein() * 7;
                            Float fat = personalItem.getPersonFat() * 7;
                            Float natrium = personalItem.getPersonNatrium() * 7;

/*
                            weekKCalTV.setText("\n열량: " + Math.round(food_kcal / kcal * 100) + "%");
                            weekCarbsTV.setText("탄수화물 " + Math.round(food_carbs / carbs * 100) + "%");
                            weekProteinTV.setText("단백질 " + Math.round(food_protein / protein * 100) + "%");
                            weekFatTV.setText("지방 " + Math.round(food_fat / fat * 100) + "%");
                            weekNatriumTV.setText("나트륨 " + Math.round(food_natrium / natrium * 100) + "%");
                            */

                            int low = Math.round(food_carbs / carbs * 100);
                            int mid = Math.round(food_protein / protein * 100);
                            int high = Math.round(food_fat / fat * 100);

                            if (low < 30) {
                                mLowBar.set(Color.RED, low);
                            }
                            if (low >= 30 && low < 70) {
                                mLowBar.set(Color.GREEN, low);
                            }
                            if (low >= 70) {
                                mLowBar.set(Color.BLUE, low);
                            }
                            if (mid < 30) {
                                mMidBar.set(Color.RED, mid);

                            }
                            if (mid >= 30 && low < 70) {
                                mMidBar.set(Color.GREEN, mid);

                            }
                            if (mid >= 70) {
                                mMidBar.set(Color.BLUE, mid);

                            }
                            if (high < 30) {
                                mHighBar.set(Color.RED, high);

                            }
                            if (high >= 30 && low < 70) {
                                mHighBar.set(Color.GREEN, high);

                            }
                            if (high >= 70) {
                                mHighBar.set(Color.BLUE, high);

                            }

                            // mLowBar.set(Color.BLUE, low);
                            // mMidBar.set(Color.RED, mid);
                           // mHighBar.set(Color.GREEN, high);

                            mLowLabel.setText(String.valueOf(low) + "%");
                            mMidLabel.setText(String.valueOf(mid) + "%");
                            mHighLabel.setText(String.valueOf(high) + "%");

                            if (low<mid) {
                                if (low<high) {
                                    lacknutrients.setText("'탄수화물'이 가장 부족합니다.");
                                    lacknutrients2.setText("'탄수화물'이 부족할 땐 감자튀김, 쿠키 등을 먹는 것이 좋습니다.");
                                } else {
                                    lacknutrients.setText("'지방'이 가장 부족합니다.");
                                    lacknutrients2.setText("'지방'이 부족할 땐 도넛, 아보카도, 견과류 등을 먹는 것이 좋습니다.");

                                }
                            }
                            else {
                                if (mid<high) {
                                    lacknutrients.setText("'단백질'이 가장 부족합니다.");
                                    lacknutrients2.setText("'단백질'이 부족할 땐 브로콜리, 검정콩, 두부 등을 먹는 것이 좋습니다.");

                                } else {
                                    lacknutrients.setText("'지방'이 가장 부족합니다.");
                                    lacknutrients2.setText("'지방'이 부족할 땐 도넛, 아보카도, 견과류 등을 먹는 것이 좋습니다.");

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        return view;
    }
}