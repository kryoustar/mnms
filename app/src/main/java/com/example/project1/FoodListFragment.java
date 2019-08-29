package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.text.TextUtils;
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
        Button selectDateButton = view.findViewById(R.id.btnDate);
        TextView nutrientsToday = view.findViewById(R.id.nutrientsToday);

        ListView mealListview = view.findViewById(R.id.mealListView); // listview  생성 및 adapter 지정
        ArrayList<String> mealItems = new ArrayList<String>(); // 빈 데이터 리스트 생성


        selectDateButton.setOnClickListener(new View.OnClickListener() { //날짜 클릭할 때

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

        String[] result;
        result = new String[100];
        final String dateTemp = date;

        ConditionRef.addValueEventListener(new ValueEventListener() { //먹은 음식 띄우기
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        mealListview.setAdapter(adapter); //음식 리스트 클릭할 때
        mealListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
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


        //하루 먹은 양 누적
        SQLiteDatabase db = getActivity().openOrCreateDatabase("nutrients.db", android.content.Context.MODE_PRIVATE, null);
        //String food_name = "";
        int food_amount = 0;
        float food_kcal = 0, food_carbs = 0, food_protein = 0, food_fat = 0, food_sugar = 0,
                food_natrium = 0, food_cholesterol = 0, food_saturatedfat = 0, food_transfat = 0;



        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), result[0], Toast.LENGTH_SHORT).show();
            }
        }, 10000);



        Integer j = 0;
        while (!TextUtils.isEmpty(result[j]))
        {
            String sql = "select * from tb_nutrients where number = " + result[j];
            Cursor cursor = db.rawQuery(sql, null);

           //if (cursor !=null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            Toast.makeText(getActivity(), result[0], Toast.LENGTH_SHORT).show();

            food_amount += cursor.getInt(3);
            food_kcal += cursor.getFloat(4);
            food_carbs += cursor.getFloat(5);
            food_protein += cursor.getFloat(6);
            food_fat += cursor.getFloat(7);
            food_sugar += cursor.getFloat(8);
            food_natrium += cursor.getFloat(9);
            food_cholesterol += cursor.getFloat(10);
            food_saturatedfat += cursor.getFloat(11);
            food_transfat += cursor.getFloat(12);
            j++;
        }

        nutrientsToday.setText("칼로리: " + String.valueOf(food_amount) +"\n탄수화물: " + Float.toString(food_carbs) +
                "\n단백질: " +  Float.toString(food_protein) + "\n지방: " +  Float.toString(food_fat) +
                "\n당류: " +  Float.toString(food_sugar) + "\n나트륨: " +  Float.toString(food_natrium) +
                "\n콜레스테롤: " +  Float.toString(food_cholesterol) + "\n포화지방산: " +  Float.toString(food_saturatedfat) + "" +
                "\n트랜스지방산: " +  Float.toString(food_transfat));
        //Toast.makeText(getActivity(), String.valueOf(j), Toast.LENGTH_SHORT).show();


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