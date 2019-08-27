package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
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

import java.util.ArrayList;
import java.util.Calendar;


public class FoodListFragment extends Fragment {


    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_list, container, false);
       // TextView mealView = view.findViewById(R.id.mealList);
        Button selectDateButton = view.findViewById(R.id.btnDate);

        ListView mealListview = view.findViewById(R.id.mealListView); // listview  생성 및 adapter 지정
        ArrayList<String> mealItems = new ArrayList<String>(); // 빈 데이터 리스트 생성


        selectDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CalendarActivity.class);
                startActivity(intent);
            }
        });

        String selectedYear, selectedMonth, selectedDay;
        String date = "";

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            Calendar todayCal = Calendar.getInstance();
            String toYear = Integer.toString(todayCal.get(Calendar.YEAR));
            String toMonth = Integer.toString(todayCal.get(Calendar.MONTH) + 1);
            String toDayofMonth = Integer.toString(todayCal.get(Calendar.DATE));
            date = toYear + "-" + toMonth + "-" + toDayofMonth;
            selectDateButton.setText(date);
        } else {
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
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mealItems);

        DatabaseReference ConditionRef = Database.child("User")
                .child(uid).child("Meal")
                .child(date);

        ConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mealItemList = "";
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer mealIndex = snapshot.getValue(Integer.class);
                    if (mealIndex != null) {
                        FoodItem breakfastItem = FoodItem.FoodItemSearch(mealIndex, getActivity());
                        mealItemList = mealItemList + "\n" + breakfastItem.getFoodName();
                        mealItems.add(breakfastItem.getFoodName());
                    }
                }
                //mealView.setText(mealItemList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mealListview.setAdapter(adapter);
        mealListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


        Button meal_add;

        meal_add = view.findViewById(R.id.breakfast_add);
        //lunch_add = view.findViewById(R.id.lunch_add);
        //dinner_add = view.findViewById(R.id.dinner_add);
        //snack_add = view.findViewById(R.id.snack_add);

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

    public void gotoLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getActivity(), "로그인 이후 사용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
            Intent gotoLogin = new Intent(getActivity(), LoginActivity.class);
            startActivity(gotoLogin);
        }

    }

}

