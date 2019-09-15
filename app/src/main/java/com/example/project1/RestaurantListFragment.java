package com.example.project1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantListFragment extends Fragment {
    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    Button searchBtn;
    EditText searchText;
    Button citySearchBtn;
    Button gpslocation;

    String sql;
    SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
    Cursor cursor;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
    ListView listView;   // ListView 객체 생성
    String[] result;   // ArrayAdapter에 넣을 배열 생성

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.restaurantlist_view, container, false);
        searchText = view.findViewById(R.id.search_restaurant);
        searchBtn = (Button) view.findViewById(R.id.search_btn);
        gpslocation = (Button) view.findViewById(R.id.gps_location);
        citySearchBtn = (Button) view.findViewById(R.id.citysearch_btn);
        Button tvVeganismType = (Button) view.findViewById(R.id.veganismType);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ConditionRef2 = dbRef.child("User").child(uid)
                .child("Personal Info").child("Veganism Type");

        ConditionRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String veganType = dataSnapshot.getValue(String.class);
                tvVeganismType.setText(veganType);

                ArrayList<RestaurantItem> data = null;

                listView = view.findViewById(R.id.listView);
                if (veganType == "지향 없음" || veganType == "페스코")
                    sql = "select * from veganRes03 where RestaurantCity like '%" + "용산구" + "%'";
                else if (veganType == "락토 오보")
                    sql = "select * from veganRes03 where RestaurantVeganType like '%" + "비건" + "%' OR RestaurantVeganType like '%" + "락토" + "%' OR RestaurantVeganType like '%" + "오보" + "%' OR RestaurantVeganType like '%" + "락토 오보" + "%' AND RestaurantCity like '%" + "용산구" + "%'";
                else if (veganType== "락토")
                    sql = "select * from veganRes03 where RestaurantVeganType like '%" + "비건" + "%' OR RestaurantVeganType like '%" + "락토" + "%' AND RestaurantCity like '%" + "용산구" + "%'";
                else if (veganType== "오보")
                    sql = "select * from veganRes03 where RestaurantVeganType like '%" + "비건" + "%' OR RestaurantVeganType like '%" + "오보" + "%' AND RestaurantCity like '%" + "용산구" + "%'";
                else
                    sql = "select * from veganRes03 where RestaurantVeganType like '%" + "비건" + "%' AND RestaurantCity like '%" + "용산구" + "%'";


                result = new String[1000];   // 저장된 행 개수만큼의 배열을 생성

                result = RestaurantItem.returnResult(getActivity(),sql);
                data = RestaurantItem.restaurantItemSQLSearch(getActivity(),sql);

                CustomAdapter adapter = new CustomAdapter(getActivity(), result);
                listView.setAdapter(adapter);

                //클릭 시 다음페이지
                final ArrayList<RestaurantItem> finalData = data;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent intent = new Intent(getActivity(), RestaurantDetail.class);
                        intent.putExtra("Restaurant Number", finalData.get(position).getNumber());
                        startActivity(intent);
                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        citySearchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityList.class);
                startActivity(intent);
            }
        });

        gpslocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetCurrentGPSLocation.class);
                startActivity(intent);
            }
        });

        //검색기능
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = searchText.getText().toString(); //검색 받아옴

                ArrayList<RestaurantItem> data2 = null;
                data2 = new ArrayList<>();

                sql = "select * from veganRes03 where RestaurantName like '%" + search + "%'";

                result = new String[1000];   // 저장된 행 개수만큼의 배열을 생성

                result = RestaurantItem.returnResult(getActivity(),sql);
                data2 = RestaurantItem.restaurantItemSQLSearch(getActivity(),sql);
                CustomAdapter adapter = new CustomAdapter(getActivity(), result);
                final ArrayList<RestaurantItem> finalData2 = data2;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView parent, View v, int position, long id) {
                        Intent intent = new Intent(getActivity(), RestaurantDetail.class);
                        intent.putExtra("Restaurant Number", finalData2.get(position).getNumber());
                        startActivity(intent);
                    }
                });

            }

        });
        return view;
    }
}