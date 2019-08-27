package com.example.project1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CityList extends AppCompatActivity {

    ListView listView;   // ListView 객체 생성
    String[] result;   // ArrayAdapter에 넣을 배열 생성

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_list);
        ArrayList<String> data = null;
        data = new ArrayList<String>();

        String city1 = "강남구";
        data.add(city1);

        String city2 = "강동구";
        data.add(city2);

        String city3 = "강북구";
        data.add(city3);

        String city4 = "강서구";
        data.add(city4);

        String city5 = "관악구";
        data.add(city5);

        String city6 = "광진구";
        data.add(city6);

        String city7 = "구로구";
        data.add(city7);

        String city8 = "금천구";
        data.add(city8);

        String city9 = "노원구";
        data.add(city9);

        String city10 = "도봉구";
        data.add(city10);

        String city11 = "동대문구";
        data.add(city11);

        String city12 = "동작구";
        data.add(city12);

        String city13 = "마포구";
        data.add(city13);

        String city14 = "서대문구";
        data.add(city14);

        String city15 = "서초구";
        data.add(city15);

        String city16 = "성동구";
        data.add(city16);

        String city17 = "성북구";
        data.add(city17);

        String city18 = "송파구";
        data.add(city18);

        String city19 = "양천구";
        data.add(city19);

        String city20 = "영등포구";
        data.add(city20);

        String city21 = "용산구";
        data.add(city21);

        String city22 = "은평구";
        data.add(city22);

        String city23 = "종로구";
        data.add(city23);

        String city24 = "중구";
        data.add(city24);

        String city25 = "중랑구";
        data.add(city25);


        listView = findViewById(R.id.listView);


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, data);   // ArrayAdapter(this, 출력모양, 배열)
        listView.setAdapter(adapter);

        //클릭 시 다음페이지
        final ArrayList<String> finalData = data;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(CityList.this, RestaurantListByCity.class);
                intent.putExtra("Restaurant City", finalData.get(position));
                startActivity(intent);
            }
        });
    }
}