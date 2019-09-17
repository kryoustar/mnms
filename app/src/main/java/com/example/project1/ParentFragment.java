package com.example.project1;

import android.content.Context;
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

public class ParentFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent, container, false);
        Button selectDateButton = view.findViewById(R.id.btnDate);
        TextView todaykcal = view.findViewById(R.id.todaykcal);

        //날짜 선택
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        String selectedYear, selectedMonth, selectedDay;
        String date = "";

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        date = formatter.format(today);
        selectDateButton.setText(date);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null){
            int yearselect = bundle.getInt("Selected Year");
            selectedYear = Integer.toString(yearselect);
            int monthselect = bundle.getInt("Selected Month");
            selectedMonth = Integer.toString(monthselect);
            if (monthselect<10) {
                selectedMonth = "0" + Integer.toString(monthselect);
            }
            int dayselect = bundle.getInt("Selected Day");
            selectedDay = Integer.toString(dayselect);
            if (dayselect<10) {
                selectedDay = "0" + Integer.toString(dayselect);
            }
            date = selectedYear + "-" + selectedMonth + "-" + selectedDay;
            selectDateButton.setText(date);
        }

        String nullcheck = selectDateButton.getText().toString();
        if(nullcheck.equals("0-0-0")){
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            Date today2 = new Date();
            String date2 = formatter2.format(today2);
            selectDateButton.setText(date2);

        }


        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        String uid = user.getUid();

        DatabaseReference ConditionRef = Database.child("User")
                .child(uid).child("Meal")
                .child(date);

        String[] result;
        result = new String[100];
        final String dateTemp = date;
        String[] result2;
        result2 = new String[2];

        //먹은 음식 get
        ConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer mealIndex = snapshot.getValue(Integer.class);
                    result[i] = Integer.toString(mealIndex); //food number 저장
                    i++;
                    if (mealIndex != null) {
                        FoodItem mealItem = FoodItem.FoodItemSearch(mealIndex, getActivity());
                        //mealItems.add(mealItem.getFoodName());
                    }
                }

                //gender, age
                DatabaseReference ConditionRef2 = Database.child("User").child(uid).child("Personal Info");
                ConditionRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String temp = snapshot.getValue(String.class); //value 받아옴
                            String key = snapshot.getKey(); //key 받아옴

                            if (key.equals("Gender")) {
                                result2[0] = temp;
                            } else if (key.equals("Age")) {
                                result2[1] = temp;
                            }
                        }

                        int food_amount = 0;
                        float food_kcal = 0, food_carbs = 0, food_protein = 0, food_fat = 0, food_sugar = 0,
                                food_natrium = 0, food_cholesterol = 0, food_saturatedfat = 0, food_transfat = 0;

                        Integer j = 0;
                        while (!TextUtils.isEmpty(result[j])) {
                            FoodItem foodItem = FoodItem.FoodItemSearch(Integer.valueOf(result[j]), getActivity());


                            food_kcal += foodItem.getFoodKcal();
                            food_carbs += foodItem.getFoodCarbs();
                            food_protein += foodItem.getFoodProtein();
                            food_fat += foodItem.getFoodFat();
                            food_natrium += foodItem.getFoodNatrium();
                            j++;
                        }

                        PersonalItem personalItem = PersonalItem.PersonalItemSearch(result2[0], result2[1], getActivity());
                        Float kcal = personalItem.getPersonKcal();
                        Float carbs = personalItem.getPersonCarbs();
                        Float protein = personalItem.getPersonProtein();
                        Float fat = personalItem.getPersonFat();


                        if (kcal <= food_kcal) {
                            todaykcal.setText("[오늘 섭취해야 할 칼로리 "+ Math.round(kcal) +
                                    "kcal를 모두 섭취하였습니다.]");
                        }

                        else if (food_kcal == 0) {
                            todaykcal.setText("[오늘 먹은 음식이 없습니다.]");
                        }

                        else {
                            todaykcal.setText("[오늘 섭취해야 할 칼로리 "+ Math.round(kcal) +
                                    "kcal 중 " + Float.toString(food_kcal) + "kcal를 먹었습니다.]" );
                        }


                        LinearLayout pongField = (LinearLayout) view.findViewById(R.id.field1);
                        ArrayList<WritingVO> writing = new ArrayList<WritingVO>();

                        Canvas canvas = new Canvas();
                        WritingVO wVO1 = new WritingVO((float) Math.round(food_carbs / carbs * 100) , (float) 100);

                        writing.add(wVO1);
                        CircleChart circleChart = new CircleChart(getActivity(),null,writing,60,300);

                        pongField.addView(circleChart);


                        LinearLayout pongField2 = (LinearLayout) view.findViewById(R.id.field2);
                        ArrayList<WritingVO> writing2 = new ArrayList<WritingVO>();

                        Canvas canvas2 = new Canvas();
                        WritingVO wVO1_2 = new WritingVO((float) Math.round(food_protein / protein * 100) , (float) 100);

                        writing2.add(wVO1_2);
                        CircleChart circleChart2 = new CircleChart(getActivity(),null,writing2,60,300);

                        pongField2.addView(circleChart2);


                        LinearLayout pongField3 = (LinearLayout) view.findViewById(R.id.field3);
                        ArrayList<WritingVO> writing3 = new ArrayList<WritingVO>();

                        Canvas canvas3 = new Canvas();
                        WritingVO wVO1_3 = new WritingVO((float) Math.round(food_fat / fat * 100) , (float) 100);

                        writing3.add(wVO1_3);
                        CircleChart circleChart3 = new CircleChart(getActivity(),null,writing3,60,300);

                        pongField3.addView(circleChart3);
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

        ImageButton meal_add;
        meal_add = view.findViewById(R.id.meal_add);

        Intent intent = new Intent(getActivity(), FoodSearch.class);
        intent.putExtra("Date", date);

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