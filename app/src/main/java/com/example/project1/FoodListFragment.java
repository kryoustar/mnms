package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static android.content.Intent.getIntent;

public class FoodListFragment extends Fragment {

    TextView breakfastView, lunchView, dinnerView, snackView;
    boolean isBreakfast;
    boolean isLunch;
    boolean isDinner;
    boolean isSnack;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_list, container, false);
        breakfastView = view.findViewById(R.id.breakfast_list);
        lunchView = view.findViewById(R.id.lunch_list);
        dinnerView = view.findViewById(R.id.dinner_list);
        snackView = view.findViewById(R.id.snack_list);
        Button selectDateButton = view.findViewById(R.id.btnDate);

        selectDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });
/*
        //날짜 지정
        //Date time = new Date();
       // SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //String date = format.format(time);
        Calendar selectedCal = Calendar.getInstance();
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int todayYear = calendar.get(Calendar.YEAR);
                int todayMonth = calendar.get(Calendar.MONTH);
                int todayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                selectDateButton.setText((month + 1) + "/" + day  + "/" + year);
                                selectedCal.set(Calendar.YEAR,year);
                                selectedCal.set(Calendar.MONTH,month);
                                selectedCal.set(Calendar.DAY_OF_MONTH,day);

                            }
                            public void onOkay(DialogInterface dialog){

                            }
                        }, todayYear, todayMonth, todayOfMonth);

                datePickerDialog.show();

            }
        });


        String date; //선택한 날짜
        String year = Integer.toString(selectedCal.get(Calendar.YEAR));
        String month = Integer.toString(selectedCal.get(Calendar.MONTH)+1);
        String day = Integer.toString(selectedCal.get(Calendar.DATE));
        date = year + "-" + month + "-" + day;

        String today;  //오늘
        Calendar todayCal = Calendar.getInstance();
        String toYear = Integer.toString(todayCal.get(Calendar.YEAR));
        String toMonth = Integer.toString(todayCal.get(Calendar.MONTH)+1);
        String toDayofMonth = Integer.toString(todayCal.get(Calendar.DATE));
        today = toYear + "-" + toMonth + "-" + toDayofMonth;

        if(date.equals(today)){}
        else if(!date.equals(today)){}

        Date time = new Date();  //임시
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(time);*/
        // isBreakfast = bundle.getBoolean("BreakfastFlag",false);

        String selectedYear, selectedMonth,selectedDay;
        String date="";

        Bundle bundle = getActivity().getIntent().getExtras();
        if(bundle == null){
            Calendar todayCal = Calendar.getInstance();
            String toYear = Integer.toString(todayCal.get(Calendar.YEAR));
            String toMonth = Integer.toString(todayCal.get(Calendar.MONTH)+1);
            String toDayofMonth = Integer.toString(todayCal.get(Calendar.DATE));
            date = toYear + "-" + toMonth + "-" + toDayofMonth;
            selectDateButton.setText(date);
        }

        else{
            int yearselect = bundle.getInt("Selected Year");
            selectedYear = Integer.toString(yearselect);
            int monthselect = bundle.getInt("Selected Month");
            selectedMonth = Integer.toString(monthselect);
            int dayselect = bundle.getInt("Selected Day");
            selectedDay = Integer.toString(dayselect);
            date = selectedYear + "-" + selectedMonth + "-" + selectedDay;
            selectDateButton.setText(date);
        }

        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            gotoLogin();
        }

        String uid = user.getUid();

        DatabaseReference ConditionRef = Database.child("User")
                .child(uid).child("Meal")
                .child(date);

        ConditionRef.child("Breakfast").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String breakfastItemList = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer breakfastIndex = snapshot.getValue(Integer.class);
                    if (breakfastIndex != null) {
                        FoodItem breakfastItem = FoodItem.FoodItemSearch(breakfastIndex, getActivity());
                        breakfastItemList = breakfastItemList + "\n" + breakfastItem.getFoodName();
                    }
                }
                breakfastView.setText(breakfastItemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ConditionRef.child("Lunch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lunchItemList = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer breakfastIndex = snapshot.getValue(Integer.class);
                    if (breakfastIndex != null) {
                        FoodItem breakfastItem = FoodItem.FoodItemSearch(breakfastIndex, getActivity());
                        lunchItemList = lunchItemList + "\n" + breakfastItem.getFoodName();
                    }
                }
                lunchView.setText(lunchItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ConditionRef.child("Dinner").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dinnerItemList = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer breakfastIndex = snapshot.getValue(Integer.class);
                    if (breakfastIndex != null) {
                        FoodItem breakfastItem = FoodItem.FoodItemSearch(breakfastIndex, getActivity());
                        dinnerItemList = dinnerItemList + "\n" + breakfastItem.getFoodName();
                    }
                }
                dinnerView.setText(dinnerItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ConditionRef.child("Snack").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String snackItemList = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer breakfastIndex = snapshot.getValue(Integer.class);
                    if (breakfastIndex != null) {
                        FoodItem breakfastItem = FoodItem.FoodItemSearch(breakfastIndex, getActivity());
                        snackItemList = snackItemList + "\n" + breakfastItem.getFoodName();
                    }
                }
                snackView.setText(snackItemList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button breakfast_add;
        Button lunch_add;
        Button dinner_add;
        Button snack_add;

        //if (bundle ==null){
        isBreakfast = false;
        isLunch = false;
        isDinner = false;
        isSnack = false;
        // }
        //else{
        //   isBreakfast = bundle.getBoolean("BreakfastFlag",false);
        //   isLunch = bundle.getBoolean("LunchFlag",false);
        //  isDinner = bundle.getBoolean("DinnerFlag",false);
        //   isSnack = bundle.getBoolean("SnackFlag", false);
        // }


        //setTitle("식단관리");


        breakfast_add = view.findViewById(R.id.breakfast_add);
        lunch_add = view.findViewById(R.id.lunch_add);
        dinner_add = view.findViewById(R.id.dinner_add);
        snack_add = view.findViewById(R.id.snack_add);

        Intent intent = new Intent(getActivity(), FoodSearch.class);
        intent.putExtra("Date", date);

        breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) {

                    //Bundle b = getActivity().getIntent().getExtras();
                    //isBreakfast = b.getBoolean("BreakfastFlag");
                    //isLunch = b.getBoolean("LunchFlag");
                    //isDinner = b.getBoolean("DinnerFlag");
                    //isSnack = b.getBoolean("SnackFlag");
                    isBreakfast = true;
                    intent.putExtra("isBreakfast", isBreakfast);
                    startActivity(intent);
                } else {
                    gotoLogin();
                }
            }
        });

        lunch_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) {

                    //Bundle b = getActivity().getIntent().getExtras();
                    //isBreakfast = b.getBoolean("BreakfastFlag");
                    //isLunch = b.getBoolean("LunchFlag");
                    //isDinner = b.getBoolean("DinnerFlag");
                    //isSnack = b.getBoolean("SnackFlag");
                    isLunch = true;
                    intent.putExtra("isLunch", isLunch);
                    startActivity(intent);
                } else {
                    gotoLogin();
                }
            }
        });

        dinner_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) {

                    //Bundle b = getActivity().getIntent().getExtras();
                    //isBreakfast = b.getBoolean("BreakfastFlag");
                    //isLunch = b.getBoolean("LunchFlag");
                    //isDinner = b.getBoolean("DinnerFlag");
                    //isSnack = b.getBoolean("SnackFlag");
                    isDinner = true;
                    intent.putExtra("isDinner", isDinner);
                    startActivity(intent);
                } else {
                    gotoLogin();

                }
            }
        });

        snack_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) {

                    //Bundle b = getActivity().getIntent().getExtras();
                    //isBreakfast = b.getBoolean("BreakfastFlag");
                    //isLunch = b.getBoolean("LunchFlag");
                    //isDinner = b.getBoolean("DinnerFlag");
                    //isSnack = b.getBoolean("SnackFlag");
                    isSnack = true;
                    intent.putExtra("isSnack", isSnack);
                    startActivity(intent);
                } else {
                    gotoLogin();
                }
            }
        });


        return view;
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
