package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FoodSearchFragment extends Fragment {
    Button searchButton;
    EditText searchText;

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.food_search, container, false);
        boolean isBreakfast, isLunch, isDinner, isSnack;
        //Intent intent = getIntent();
       // String date = intent.getStringExtra("Date");
        String date = "tryTemp";// getArguments().getString("Date");

        Bundle bundle = new Bundle();
        FoodListFragment foodListFragment = new FoodListFragment();

        EditText searchText = view.findViewById(R.id.search_nutrients);
        Button searchButton = view.findViewById(R.id.add_btn);
        //searchButton.setOnClickListener(getView());
        Bundle args = getArguments();
        if(args ==null){
            Toast.makeText(getActivity(), "arguments is null " , Toast.LENGTH_LONG).show();
            isBreakfast = false;
            isLunch = true;
            isDinner = false;
            isSnack = false;
        }
        else {
             isBreakfast = (boolean) getArguments().getBoolean("isBreakfast");
             isLunch = (boolean) getArguments().getBoolean("isLunch");
             isDinner = (boolean) getArguments().getBoolean("isDinner");
             isSnack = (boolean) getArguments().getBoolean("isSnack");
        }
        TextView breakfastView;
        String search = searchText.getText().toString(); //검색 받아옴
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        ListView listView;   // ListView 객체 생성
        String[] result;   // ArrayAdapter에 넣을 배열 생성
        Integer[] foodnumber;
        db = getContext().openOrCreateDatabase("nutrients.db", Context.MODE_PRIVATE, null);
        listView = (ListView) view.findViewById(R.id.listView);

        sql = "select * from tb_nutrients where name like '%" + search + "%'";
        c = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)

        int count = c.getCount();   // db에 저장된 행 개수를 읽어온다
        result = new String[count];// 저장된 행 개수만큼의 배열을 생성
        foodnumber = new Integer[count];

        for (int i = 0; i < count; i++) {
            c.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
            int food_number = c.getInt(0);
            String str_name = c.getString(2);
            result[i] = str_name;
            foodnumber[i] = food_number;
        }
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String uid = user.getUid();
                dbRef = database.getReference("/User");



                if (isBreakfast) {
                    DatabaseManager.BreakfastDataAdd(date, foodnumber[position]);
                    boolean BreakfastFlag = false;
                    bundle.putBoolean("BreakfastFlag", BreakfastFlag);
                    foodListFragment.setArguments(bundle);
                    ((BottomActivity) getActivity()).replaceFragment(FoodListFragment.newInstance(bundle));
                   // onBackPressed();
                }
                if (isLunch) {
                    DatabaseManager.LunchDataAdd(date, foodnumber[position]);
                    boolean LunchFlag = false;
                    bundle.putBoolean("LunchFlag", LunchFlag);
                    foodListFragment.setArguments(bundle);
                    ((BottomActivity) getActivity()).replaceFragment(FoodListFragment.newInstance(bundle));

                    // onBackPressed();
                }
                if (isDinner) {
                    DatabaseManager.DinnerDataAdd(date, foodnumber[position]);
                    boolean DinnerFlag = false;
                    bundle.putBoolean("DinnerFlag", DinnerFlag);
                    foodListFragment.setArguments(bundle);
                    ((BottomActivity) getActivity()).replaceFragment(FoodListFragment.newInstance(bundle));

                    //BottomActivity.replaceFragment(FoodListFragment.newInstance());
                   // onBackPressed();
                }
                if (isSnack) {
                    DatabaseManager.SnackDataAdd(date, foodnumber[position]);
                    boolean SnackFlag = false;
                    bundle.putBoolean("SnackFlag", SnackFlag);
                    foodListFragment.setArguments(bundle);
                    ((BottomActivity) getActivity()).replaceFragment(FoodListFragment.newInstance(bundle));


                    //  onBackPressed();
                } /*else { //error ?

                    onBackPressed();
                }*/
            }
        });
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
        listView.setAdapter(adapter);   // listView 객체에 Adapter를 붙인다





        return view;
    }


    public static FoodSearchFragment newInstance(Bundle bundle){
        FoodSearchFragment fragment = new FoodSearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
  /*  public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();
    }*/

}
