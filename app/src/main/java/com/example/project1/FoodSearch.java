package com.example.project1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FoodSearch extends AppCompatActivity implements View.OnClickListener {
    ImageButton searchButton;
    EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_search);
        searchText = findViewById(R.id.search_nutrients);
        searchButton = findViewById(R.id.add_btn);
        searchButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = getIntent();
        String date = intent.getStringExtra("Date");


        String search = searchText.getText().toString(); //검색 받아옴
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        ListView listView;   // ListView 객체 생성
        String[] result;   // ArrayAdapter에 넣을 배열 생성
        Integer[] foodnumber;

        db = openOrCreateDatabase("nutrients.db", MODE_PRIVATE, null);
        listView = (ListView) findViewById(R.id.listView);

        sql = "select * from tb_nutrients where name like '%" + search + "%'";
        c = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)

        int count = c.getCount();   // db에 저장된 행 개수를 읽어온다
        result = new String[count];// 저장된 행 개수만큼의 배열을 생성
        foodnumber = new Integer[count];

        for (int i = 0; i < count; i++) {
            c.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
            int food_number = c.getInt(0);
            String str_name = c.getString(2);
            Integer food_amount = c.getInt(3);
            result[i] = str_name + " (" + Integer.toString(food_amount) + "g)" ;
            foodnumber[i] = food_number;
        }

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), ((TextView) view).getText() +"을(를) 추가하였습니다.", Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                String uid = user.getUid();
                dbRef = database.getReference("/User");

                DatabaseManager.MealDataAdd(date, foodnumber[position]);

                Intent gobackIntent = new Intent(FoodSearch.this, BottomActivity.class);
                //onBackPressed();
                Bundle bundle = new Bundle();

                // FragmentTransaction transaction = fragmentManager.beginTransaction();
                String year = date.substring(0, 4);
                String month = date.substring(5, 7);
                String day = date.substring(8, 10);

                intent.putExtra("Selected Year", Integer.parseInt(year));
                intent.putExtra("Selected Month", Integer.parseInt(month));
                intent.putExtra("Selected Day", Integer.parseInt(day));

                //Toast.makeText(getApplicationContext(), year + "-" + month + "-" + day, Toast.LENGTH_SHORT).show();
                intent.putExtras(bundle);
                startActivity(gobackIntent);

            }
        });
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.simpleitemtwo, result);   // ArrayAdapter(this, 출력모양, 배열)
        listView.setAdapter(adapter);   // listView 객체에 Adapter를 붙인다
    }


}
