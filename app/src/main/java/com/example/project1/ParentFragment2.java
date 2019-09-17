package com.example.project1;


import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ParentFragment2 extends Fragment {
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent2, container, false);
        TextView mLowLabel, mMidLabel, mHighLabel;
        TextView lacknutrients, lacknutrients2;
        BarView mLowBar, mMidBar, mHighBar;

        mLowBar = view.findViewById(R.id.low_bar);
        mMidBar = view.findViewById(R.id.mid_bar);
        mHighBar = view.findViewById(R.id.high_bar);

        mLowLabel = view.findViewById(R.id.low_text);
        mMidLabel = view.findViewById(R.id.mid_text);
        mHighLabel = view.findViewById(R.id.high_text);

        lacknutrients = view.findViewById(R.id.lacknutrients);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String date = formatter.format(today);

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
            if ((todayCalendar.get(Calendar.MONTH) + 1)<10) {
                toMonth = "0" + toMonth;
            }
            String toDayofMonth = Integer.toString(todayCalendar.get(Calendar.DATE));
            if (todayCalendar.get(Calendar.DATE) < 10) {
                toDayofMonth = "0" + toDayofMonth;
            }
            tempDate = toYear + "-" + toMonth + "-" + toDayofMonth;
            weekArray[i - 1] = tempDate;
            // 일주일 치 날 짜 배열 생성
        }

        TextView week = view.findViewById(R.id.weekdate);
        String weekdate = weekArray[6] + " ~ " + weekArray[0];
        week.setText(weekdate);

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


                            PersonalItem personalItem = PersonalItem.PersonalItemSearch(result[0], result[1], getActivity());
                            Float kcal = personalItem.getPersonKcal() * 7;
                            Float carbs = personalItem.getPersonCarbs() * 7;
                            Float protein = personalItem.getPersonProtein() * 7;
                            Float fat = personalItem.getPersonFat() * 7;
                            Float natrium = personalItem.getPersonNatrium() * 7;


                            int low = Math.round(food_carbs / carbs * 100);
                            int mid = Math.round(food_protein / protein * 100);
                            int high = Math.round(food_fat / fat * 100);

                            if (low>100)
                                low = 100;
                            if (mid>100)
                                mid = 100;
                            if (high>100)
                                high=100;

                            if (low < 30) {
                                mLowBar.set(Color.RED, low);
                            }
                            if (low >= 30 && low < 70) {
                                mLowBar.set(Color.rgb(255, 187, 0), low);
                            }
                            if (low >= 70) {
                                mLowBar.set(Color.rgb(1, 160, 26), low);
                            }
                            if (mid < 30) {
                                mMidBar.set(Color.RED, mid);

                            }
                            if (mid >= 30 && low < 70) {
                                mMidBar.set(Color.rgb(255, 187, 0), mid);

                            }
                            if (mid >= 70) {
                                mMidBar.set(Color.rgb(1, 160, 26), mid);

                            }
                            if (high < 30) {
                                mHighBar.set(Color.RED, high);

                            }
                            if (high >= 30 && low < 70) {
                                mHighBar.set(Color.rgb(255, 187, 0), high);


                            }
                            if (high >= 70) {
                                mHighBar.set(Color.rgb(1, 160, 26), high);

                            }

                            mLowLabel.setText(String.valueOf(low) + "%");
                            mMidLabel.setText(String.valueOf(mid) + "%");
                            mHighLabel.setText(String.valueOf(high) + "%");


                            if (low<mid) {
                                if (low<high) {
                                    lacknutrients.setText("지난 일주일 간 [탄수화물]이 가장 부족합니다.");
                                } else {
                                    lacknutrients.setText("지난 일주일 간 [지방]이 가장 부족합니다.");
                                }
                            }
                            else {
                                if (mid<high) {
                                    lacknutrients.setText("지난 일주일 간 [단백질]이 가장 부족합니다.");
                                } else {
                                    lacknutrients.setText("지난 일주일 간 [지방]이 가장 부족합니다.");
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
        //return inflater.inflate(R.layout.fragment_parent, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Fragment childFragment = new ChildFragment();
        //FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        //transaction.replace(R.id.child_fragment_container, childFragment).commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            // throw new RuntimeException(context.toString()
            //         + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromParentFragment(Uri uri);
    }
}