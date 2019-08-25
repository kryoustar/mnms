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


    boolean isBreakfast;
    boolean isLunch;
    boolean isDinner;
    boolean isSnack;

    // DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //DatabaseReference conditionRef = mRootRef.child("UserId");
    TextView breakfastView, lunchView, dinnerView, snackView;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if(getArguments() !=null){

        //}
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

        String selectedYear, selectedMonth, selectedDay;
        String date = "";

        Bundle bundle1 = getActivity().getIntent().getExtras();
        if (bundle1 == null) {
            Calendar todayCal = Calendar.getInstance();
            String toYear = Integer.toString(todayCal.get(Calendar.YEAR));
            String toMonth = Integer.toString(todayCal.get(Calendar.MONTH) + 1);
            String toDayofMonth = Integer.toString(todayCal.get(Calendar.DATE));
            date = toYear + "-" + toMonth + "-" + toDayofMonth;
            selectDateButton.setText(date);
        } else {
            int yearselect = bundle1.getInt("Selected Year");
            selectedYear = Integer.toString(yearselect);
            int monthselect = bundle1.getInt("Selected Month");
            selectedMonth = Integer.toString(monthselect);
            int dayselect = bundle1.getInt("Selected Day");
            selectedDay = Integer.toString(dayselect);
            date = selectedYear + "-" + selectedMonth + "-" + selectedDay;
            selectDateButton.setText(date);
        }

        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();

        DatabaseReference ConditionRef = Database.child("User")
                .child(uid).child("Meal")
                .child("tryTemp");


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
                    Integer lunchIndex = snapshot.getValue(Integer.class);
                    if (lunchIndex != null) {
                        FoodItem lunchItem = FoodItem.FoodItemSearch(lunchIndex, getActivity());
                        lunchItemList = lunchItemList + "\n" + lunchItem.getFoodName();
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
                    Integer dinnerIndex = snapshot.getValue(Integer.class);
                    if (dinnerIndex != null) {
                        FoodItem dinnerItem = FoodItem.FoodItemSearch(dinnerIndex, getActivity());
                        dinnerItemList = dinnerItemList + "\n" + dinnerItem.getFoodName();
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
                    Integer snackIndex = snapshot.getValue(Integer.class);
                    if (snackIndex != null) {
                        FoodItem snackItem = FoodItem.FoodItemSearch(snackIndex, getActivity());
                        snackItemList = snackItemList + "\n" + snackItem.getFoodName();
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

        breakfast_add = view.findViewById(R.id.breakfast_add);
        lunch_add = view.findViewById(R.id.lunch_add);
        dinner_add = view.findViewById(R.id.dinner_add);
        snack_add = view.findViewById(R.id.snack_add);

        Bundle b = new Bundle();
        b.putString("Date",date);
        FoodSearchFragment fSearchFrag = new FoodSearchFragment();
        fSearchFrag.setArguments(b);


        breakfast_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) {
                    isBreakfast = true;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isBreakfast", isBreakfast);
                    FoodSearchFragment foodSearchFragment = new FoodSearchFragment();
                    foodSearchFragment.setArguments(bundle);
                    ((BottomActivity) getActivity()).replaceFragment(FoodSearchFragment.newInstance(bundle));


                } else {
                    gotoLogin();
                }
            }
        });
        // isLunch = false;
        lunch_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) {
                    isLunch = true;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isLunch", isLunch);
                    FoodSearchFragment foodSearchFragment = new FoodSearchFragment();
                    foodSearchFragment.setArguments(bundle);

                    ((BottomActivity) getActivity()).replaceFragment(FoodSearchFragment.newInstance(bundle));

                    // intent.putExtra("isLunch", isLunch);
                    //   startActivity(intent);
                } else {
                    gotoLogin();
                }
            }
        });
        //isDinner = false;
        dinner_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) {
                    isDinner = true;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isLunch", isLunch);
                    FoodSearchFragment foodSearchFragment = new FoodSearchFragment();
                    foodSearchFragment.setArguments(bundle);

                    ((BottomActivity) getActivity()).replaceFragment(FoodSearchFragment.newInstance(bundle));

                    //intent.putExtra("isDinner", isDinner);
                    //startActivity(intent);
                } else {
                    gotoLogin();

                }
            }
        });
        //isSnack = false;
        snack_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Login.isLogin(user)) {
                    isSnack = true;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isLunch", isLunch);
                    FoodSearchFragment foodSearchFragment = new FoodSearchFragment();
                    foodSearchFragment.setArguments(bundle);

                    ((BottomActivity) getActivity()).replaceFragment(FoodSearchFragment.newInstance(bundle));

                    //  intent.putExtra("isSnack", isSnack);
                    //  startActivity(intent);

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

    public static FoodListFragment newInstance(Bundle bundle) {
        FoodListFragment fragment = new FoodListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}

