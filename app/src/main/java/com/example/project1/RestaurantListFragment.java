package com.example.project1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;
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
        citySearchBtn = (Button) view.findViewById(R.id.citysearch_btn);

        ArrayList<RestaurantItem> data = null;
        data = new ArrayList<>();

        db = getActivity().openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        listView = view.findViewById(R.id.listView);

        sql = "select * from veganRes03";
        cursor = db.rawQuery(sql, null);

        int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다
        result = new String[count];   // 저장된 행 개수만큼의 배열을 생성

        for (int i = 0; i < count; i++) {
            cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
            int Number = cursor.getInt(0);
            String RestaurantName = cursor.getString(1);
            String RestaurantAddress = cursor.getString(3);
            String RestaurantPhoneNumber = cursor.getString(4);
            String RestaurantOpeningHours = cursor.getString(5);
            String RestaurantCity = cursor.getString(2);
            String RestaurantVeganType = cursor.getString(6);
            RestaurantItem restaurantItem = new RestaurantItem(Number, RestaurantName, RestaurantAddress, RestaurantPhoneNumber, RestaurantOpeningHours, RestaurantCity, RestaurantVeganType);
            result[i] = RestaurantName; // 각각의 속성값들을 해당 배열의 i번째에 저장
            data.add(restaurantItem);
        }

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
        listView.setAdapter(adapter);

        //클릭 시 다음페이지
        final ArrayList<RestaurantItem> finalData = data;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(getActivity(), RestaurantDetail.class);
                intent.putExtra("Restaurant Number", finalData.get(position).getNumber());
                intent.putExtra("Restaurant Name", finalData.get(position).getRestaurantName());
                intent.putExtra("Restaurant Address", finalData.get(position).getRestaurantAddress());
                intent.putExtra("Restaurant Phone Number", finalData.get(position).getRestaurantPhoneNumber());
                intent.putExtra("Restaurant Opening Hours", finalData.get(position).getRestaurantOpeningHours());
                intent.putExtra("Restaurant City", finalData.get(position).getRestaurantCity());
                intent.putExtra("Restaurant Vegan Type", finalData.get(position).getRestaurantVeganType());
                startActivity(intent);
            }
        });
        citySearchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CityList.class);
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
                cursor = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)

                int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다
                result = new String[count];   // 저장된 행 개수만큼의 배열을 생성

                for (int i = 0; i < count; i++) {
                    cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
                    int Number = cursor.getInt(0);
                    String RestaurantName = cursor.getString(1);
                    String RestaurantAddress = cursor.getString(3);
                    String RestaurantPhoneNumber = cursor.getString(4);
                    String RestaurantOpeningHours = cursor.getString(5);
                    String RestaurantCity = cursor.getString(2);
                    String RestaurantVeganType = cursor.getString(6);
                    RestaurantItem restaurantItem = new RestaurantItem(Number, RestaurantName, RestaurantAddress, RestaurantPhoneNumber, RestaurantOpeningHours, RestaurantCity, RestaurantVeganType);

                    result[i] = RestaurantName; // 각각의 속성값들을 해당 배열의 i번째에 저장
                    data2.add(restaurantItem);

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
                    listView.setAdapter(adapter);
                    final ArrayList<RestaurantItem> finalData2 = data2;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView parent, View v, int position, long id) {
                            Intent intent = new Intent(getActivity(), RestaurantDetail.class);
                            intent.putExtra("Restaurant Number", finalData2.get(position).getNumber());
                            intent.putExtra("Restaurant Name", finalData2.get(position).getRestaurantName());
                            intent.putExtra("Restaurant Address", finalData2.get(position).getRestaurantAddress());
                            intent.putExtra("Restaurant Phone Number", finalData2.get(position).getRestaurantPhoneNumber());
                            intent.putExtra("Restaurant Opening Hours", finalData2.get(position).getRestaurantOpeningHours());
                            intent.putExtra("Restaurant City", finalData2.get(position).getRestaurantCity());
                            intent.putExtra("Restaurant Vegan Type", finalData2.get(position).getRestaurantVeganType());
                            startActivity(intent);
                        }
                    });
                }
            }

        });
        return view;
    }
}