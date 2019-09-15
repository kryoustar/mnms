package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class ScrapList extends AppCompatActivity {
    ListView restaurantListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scrap_list);
        ListView scrapListview = findViewById(R.id.restaurantListView); // listview  생성 및 adapter 지정
        ArrayList<String> restaurantNames = new ArrayList<String>(); // 빈 데이터 리스트 생성
        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(ScrapList.this, android.R.layout.simple_list_item_1, restaurantNames);


        DatabaseReference ConditionRef = Database.child("User")
                .child(uid).child("Scrap");

        String[] result;
        result = new String[100];


        //스크랩한 restaurant get
        ConditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer resIndex = snapshot.getValue(Integer.class);
                    if (resIndex != null) {
                        RestaurantItem restaurantItem= RestaurantItem.restaurantItemSearch(resIndex, getApplication());
                        restaurantNames.add(restaurantItem.getRestaurantName());
                        //Utility.setListViewHeightBasedOnChildren(restaurantListview);
                    }

                }
                adapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        final ArrayList<String> finalData = restaurantNames;

        scrapListview.setAdapter(adapter);
        scrapListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplication(),RestaurantDetail.class);
                String temp = finalData.get(i);
                int number = RestaurantItem.RestaurantItemStringSearch(temp,getApplication()).getNumber();
                intent.putExtra("Restaurant Number",number);
                startActivity(intent);
            }
        });
    }
}