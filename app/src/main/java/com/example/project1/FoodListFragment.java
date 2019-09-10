package com.example.project1;

import androidx.annotation.NonNull;
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


public class FoodListFragment extends Fragment {
    private static FirebaseDatabase Database; //데이터베이스 연결을 위한 멤버 객체


    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_list, container, false);
        Button selectDateButton = view.findViewById(R.id.btnDate);
        TextView todaykcal = view.findViewById(R.id.todaykcal);
        //TextView nutrientsToday = view.findViewById(R.id.nutrientsToday);
        //TextView nutrientsPerson = view.findViewById(R.id.nutrients_person);
        //TextView percent = view.findViewById(R.id.perday);


        ListView mealListview = view.findViewById(R.id.mealListView); // listview  생성 및 adapter 지정
        ArrayList<String> mealItems = new ArrayList<String>(); // 빈 데이터 리스트 생성


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
/*
        Calendar todayCal = Calendar.getInstance();
        String toYear = Integer.toString(todayCal.get(Calendar.YEAR));
        String toMonth = Integer.toString(todayCal.get(Calendar.MONTH) + 1);
        String toDayofMonth = Integer.toString(todayCal.get(Calendar.DATE));
        date = toYear + "-" + toMonth + "-" + toDayofMonth;
        selectDateButton.setText(date);
*/
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null){
            int yearselect = bundle.getInt("Selected Year");
            selectedYear = Integer.toString(yearselect);
            int monthselect = bundle.getInt("Selected Month");
            selectedMonth = Integer.toString(monthselect);
            int dayselect = bundle.getInt("Selected Day");
            selectedDay = Integer.toString(dayselect);
            date = selectedYear + "-" + selectedMonth + "-" + selectedDay;
            selectDateButton.setText(date);
        }

        String nullcheck = selectDateButton.getText().toString();
        if(nullcheck.equals("0-0-0")){
            SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
            Date today2 = new Date();
            String date2 = formatter.format(today2);
            selectDateButton.setText(date2);

        }


        /*
        if (bundle == null) {
            Calendar todayCal = Calendar.getInstance();
            String toYear = Integer.toString(todayCal.get(Calendar.YEAR));
            String toMonth = Integer.toString(todayCal.get(Calendar.MONTH) + 1);
            String toDayofMonth = Integer.toString(todayCal.get(Calendar.DATE));
            date = toYear + "-" + toMonth + "-" + toDayofMonth;
            selectDateButton.setText(date);
        } else if (bundle != null){
            int yearselect = bundle.getInt("Selected Year");
            selectedYear = Integer.toString(yearselect);
            int monthselect = bundle.getInt("Selected Month");
            selectedMonth = Integer.toString(monthselect);
            int dayselect = bundle.getInt("Selected Day");
            selectedDay = Integer.toString(dayselect);
            date = selectedYear + "-" + selectedMonth + "-" + selectedDay;
            selectDateButton.setText(date);
        }
        else {
            Calendar todayCal = Calendar.getInstance();
            String toYear = Integer.toString(todayCal.get(Calendar.YEAR));
            String toMonth = Integer.toString(todayCal.get(Calendar.MONTH) + 1);
            String toDayofMonth = Integer.toString(todayCal.get(Calendar.DATE));
            date = toYear + "-" + toMonth + "-" + toDayofMonth;
            selectDateButton.setText(date);
        }

         */


        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            gotoLogin();
        }

        String uid = user.getUid();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mealItems);


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
                        mealItems.add(mealItem.getFoodName());
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
                            //food_amount += foodItem.getFoodAmount();
                            //food_sugar += foodItem.getFoodSugar();
                            //food_cholesterol += foodItem.getFoodCholesterol();
                            //food_saturatedfat += foodItem.getFoodSaturatedfat();
                            //food_transfat += foodItem.getFoodTransfat();
                            j++;
                        }

                        PersonalItem personalItem = PersonalItem.PersonalItemSearch(result2[0], result2[1], getActivity());
                        Float kcal = personalItem.getPersonKcal();
                        Float carbs = personalItem.getPersonCarbs();
                        Float protein = personalItem.getPersonProtein();
                        Float fat = personalItem.getPersonFat();
                        Float natrium = personalItem.getPersonNatrium();

                        todaykcal.setText("[오늘 섭취해야 할 칼로리 "+ Math.round(kcal) +
                                "kcal 중 " + Float.toString(food_kcal) + "kcal를 먹었습니다.]" );
/*
                        nutrientsToday.setText("<오늘 섭취한 영양소>\n" + "칼로리: " +
                                Float.toString(food_kcal) +
                                "     탄수화물: " + Float.toString(food_carbs) +
                                "     단백질: " + Float.toString(food_protein) +
                                "\n지방: " + Float.toString(food_fat) +
                                "     나트륨: " + Float.toString(food_natrium));
                        nutrientsPerson.setText("<오늘 섭취해야 할 영양소>\n" +
                                "칼로리: " + Math.round(kcal) +
                                "     탄수화물: " + Math.round(carbs) +
                                "     단백질: " + Math.round(protein) +
                                "\n지방: " + Math.round(fat) +
                                "     나트륨: " + Math.round(natrium));
                        percent.setText("칼로리 섭취량: " + Math.round(food_kcal / kcal * 100) + "%\n" +
                                "탄수화물 섭취량: " + Math.round(food_carbs / carbs * 100) + "%\n" +
                                "단백질 섭취량: " + Math.round(food_protein / protein * 100) + "%\n" +
                                "지방 섭취량: " + Math.round(food_fat / fat * 100) + "%\n" +
                                "나트륨 섭취량 " + Math.round(food_natrium / natrium * 100) + "%");
*/


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
                Utility.setListViewHeightBasedOnChildren(mealListview);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //음식 리스트 클릭
        mealListview.setAdapter(adapter);
        //adapter.notifyAll();
        //mealListview.n
        mealListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), FoodDetail.class);
                String foodnumber = result[i];
                intent.putExtra("Food number", foodnumber);
                intent.putExtra("Date", dateTemp);
                startActivity(intent);
            }

        });


        Button meal_add;
        meal_add = view.findViewById(R.id.meal_add);

        Intent intent = new Intent(getActivity(), FoodSearch.class);
        intent.putExtra("Date", date);

        meal_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) { //if user logged in,
                    startActivity(intent);
                } else {
                    gotoLogin();
                }
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        //    CircleChart realCircleChart = view.findViewById(R.id.circleChart);
        //  realCircleChart.addView

    }


    public void gotoLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getActivity(), "로그인 이후 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
            Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
            startActivity(gotoLogin);
        }
    }
}