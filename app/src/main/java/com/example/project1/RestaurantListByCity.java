package com.example.project1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantListByCity extends AppCompatActivity {

    Button searchBtn;
    EditText searchText;

    String sql;
    SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
    Cursor cursor;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
    ListView listView;   // ListView 객체 생성
    String[] result;   // ArrayAdapter에 넣을 배열 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurantlist_bycity);
        searchText = findViewById(R.id.search_restaurant);
        searchBtn = (Button) findViewById(R.id.search_btn);
        Button tvVeganismType = (Button) findViewById(R.id.veganismType2);

        ArrayList<RestaurantItem> data = null;
        data = new ArrayList<>();


        listView = findViewById(R.id.listView);

        Intent intent = getIntent();
        String clickedRestaurantCity = intent.getStringExtra("Restaurant City"); // 이건 어쩔 수 없이 받아오는 코드

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ConditionRef2 = dbRef.child("User").child(uid)
                .child("Personal Info").child("Veganism Type");

        ConditionRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String veganType = dataSnapshot.getValue(String.class);
                tvVeganismType.setText(veganType);

                ArrayList<RestaurantItem> data = null;
                data = new ArrayList<>();

                listView = findViewById(R.id.listView);

                Intent intent = getIntent();
                String clickedRestaurantCity = intent.getStringExtra("Restaurant City");

                if (veganType == "지향 없음" || veganType == "페스코")
                    sql = "select * from veganRes03 where RestaurantCity like '%" + clickedRestaurantCity + "%'";
                else if (veganType == "락토 오보")
                    sql = "select * from veganRes03 where RestaurantCity like '%" + clickedRestaurantCity + "%' AND (RestaurantVeganType like '%" + "비건" + "%' OR RestaurantVeganType like '%" + "락토" + "%' OR RestaurantVeganType like '%" + "오보" + "%' OR RestaurantVeganType like '%" + "락토 오보" + "%')";
                else if (veganType == "락토")
                    sql = "select * from veganRes03 where RestaurantCity like '%" + clickedRestaurantCity + "%' AND (RestaurantVeganType like '%" + "비건" + "%' OR RestaurantVeganType like '%" + "락토" + "%')";
                else if (veganType == "오보")
                    sql = "select * from veganRes03 where RestaurantCity like '%" + clickedRestaurantCity + "%' AND (RestaurantVeganType like '%" + "비건" + "%' OR RestaurantVeganType like '%" + "오보" + "%')";
                else
                    sql = "select * from veganRes03 where RestaurantCity like '%" + clickedRestaurantCity + "%' AND RestaurantVeganType like '%" + "비건" + "%'";


                data = RestaurantItem.restaurantItemSQLSearch(getApplication(), sql);
                result = RestaurantItem.returnResult(getApplication(), sql);
                CustomAdapter adapter = new CustomAdapter(RestaurantListByCity.this, result);
                listView.setAdapter(adapter);

                //클릭 시 다음페이지
                final ArrayList<RestaurantItem> finalData = data;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent intent = new Intent(RestaurantListByCity.this, RestaurantDetail.class);
                        intent.putExtra("Restaurant Number", finalData.get(position).getNumber());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        //검색기능
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString(); //검색 받아옴

                ArrayList<RestaurantItem> data2 = null;
                sql = "select * from veganRes03 where RestaurantName like '%" + search
                        + "%' AND RestaurantCity like '%" + clickedRestaurantCity + "%'";

                result = RestaurantItem.returnResult(getApplication(), sql);
                data2 = RestaurantItem.restaurantItemSQLSearch(getApplication(), sql);

                CustomAdapter adapter = new CustomAdapter(RestaurantListByCity.this, result);
                listView.setAdapter(adapter);


                final ArrayList<RestaurantItem> finalData2 = data2;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent intent = new Intent(RestaurantListByCity.this, RestaurantDetail.class);
                        intent.putExtra("Restaurant Number", finalData2.get(position).getNumber());
                        startActivity(intent);
                    }
                });

            }

        });
    }
}
