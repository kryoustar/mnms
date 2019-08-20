package com.example.project1;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import java.util.ArrayList;
import java.util.HashMap;
import android.util.Log;

public class FoodSearch extends AppCompatActivity implements View.OnClickListener{

    Button addBtn;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_search);

        searchText=findViewById(R.id.search_nutrients);
        addBtn=findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String search = searchText.getText().toString(); //검색 받아옴

        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        ListView listView;   // ListView 객체 생성
        String[] result;   // ArrayAdapter에 넣을 배열 생성
        ArrayList<String> data = null;
        data = new ArrayList<>();


        db = openOrCreateDatabase("nutrients.db", MODE_PRIVATE, null);
        listView = (ListView)findViewById(R.id.listView);

        sql = "select * from tb_nutrients where name like '%"+ search + "%'";
        //SELECT [컬럼명] FROM [테이블명] WHERE [컬럼명] LIKE '%특정문자열%'
        c = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)

        int count = c.getCount();   // db에 저장된 행 개수를 읽어온다
        result = new String[count];   // 저장된 행 개수만큼의 배열을 생성

        for (int i = 0; i < count; i++) {
            c.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
            String food_name = c.getString(2);
            data.add(food_name);
            result[i] = food_name; // 각각의 속성값들을 해당 배열의 i번째에 저장
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
        listView.setAdapter(adapter);   // listView 객체에 Adapter를 붙인다
        final ArrayList<String> finalData = data;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
              //  Intent intent = new Intent(getActivity(),FoodListFragment.class);

               //intent.putExtra("Restaurant Name", finalData.get(position).getRestaurantName());
                //intent.putExtra("Restaurant Address", finalData.get(position).getRestaurantAddress());
                //intent.putExtra("Restaurant Phone Number", finalData.get(position).getRestaurantPhoneNumber());
                //intent.putExtra("Restaurant Opening Hours", finalData.get(position).getRestaurantOpeningHours());
              //  startActivity(intent);
            }


        });
    }
}

