package com.example.project1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
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
import java.util.Calendar;
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
        breakfastView = view.findViewById(R.id.breakfast_list);
        lunchView = view.findViewById(R.id.lunch_list);
        dinnerView = view.findViewById(R.id.dinner_list);
        snackView = view.findViewById(R.id.snack_list);
        //nutrientView = view.findViewById(R.id.n)
        Button selectDateButton = view.findViewById(R.id.btnDate);


        //날짜 지정
        Date time = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(time);

        Calendar selectedCal = Calendar.getInstance();

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int todayYear = calendar.get(Calendar.YEAR);
                int todayMonth = calendar.get(Calendar.MONTH);
                int todayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                //DatePickerDialog dialog = new DatePickerDialog(this,listener,2013,9,22);
                // dialog.show();
                //private DatePickerDialog.OnDateSetListener listener =
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                selectDateButton.setText((month + 1) + "/" + day  + "/" + year);
                               // selectedCal.set(Calendar.YEAR,year);
                                //selectedCal.set(Calendar.MONTH,month);
                               // selectedCal.set(Calendar.DAY_OF_MONTH,day);
                               // selectedCal.toString();
                                //System.out.println(selectedCal);

                            }
                        }, todayYear, todayMonth, todayOfMonth);
                datePickerDialog.show();
            }
        });
        //selectedCal.toString();

        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user !=null) {


            String uid = user.getUid();

            DatabaseReference ConditionRef = Database.child("User")
                    .child(uid).child("Meal")
                    .child(date);


            ConditionRef.child("Breakfast").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Integer breakfastIndex = dataSnapshot.getValue(Integer.class);
                    if (breakfastIndex != null) {
                        FoodItem breakfastItem = FoodItem.FoodItemSearch(breakfastIndex,getActivity());
                        breakfastView.setText(breakfastItem.getFoodName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            ConditionRef.child("Lunch").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Integer lunchIndex = dataSnapshot.getValue(Integer.class);
                    if (lunchIndex != null) {
                        FoodItem lunchItem = FoodItem.FoodItemSearch(lunchIndex,getActivity());
                        lunchView.setText(lunchItem.getFoodName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            ConditionRef.child("Dinner").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Integer dinnerIndex = dataSnapshot.getValue(Integer.class);
                    if (dinnerIndex != null) {
                        FoodItem dinnerItem = FoodItem.FoodItemSearch(dinnerIndex,getActivity());
                        dinnerView.setText(dinnerItem.getFoodName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            ConditionRef.child("Snack").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Integer snackIndex = dataSnapshot.getValue(Integer.class);
                    if (snackIndex != null) {
                        FoodItem snackItem = FoodItem.FoodItemSearch(snackIndex,getActivity());
                        snackView.setText(snackItem.getFoodName());

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



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

        intent.putExtra("Date", date);

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
