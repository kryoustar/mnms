package com.example.project1;

import android.content.Intent;
import android.widget.DatePicker;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.Serializable;
import java.util.Date;
/*
 * https://gakari.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-DatePicker-%EC%9C%84%EC%A0%AF%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EB%82%A0%EC%A7%9C-%EC%9E%85%EB%A0%A5-%EB%B0%9B%EA%B8%B0
 * calendar activity 여기서 고대로 갖고왔고, 바뀐것은 Calendar Activity / calendar_activity.xml
 *
 *
 * */

public class CalendarActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);


        //mTxtDate = (TextView)findViewById(R.id.txtdate);
        //처음 DatePicker를 오늘 날짜로 초기화한다.
        //그리고 리스너를 등록한다.
        /*mDate.init(mDate.getYear(), mDate.getMonth(), mDate.getDayOfMonth(),

                new DatePicker.OnDateChangedListener() {
                    //값이 바뀔때마다 텍스트뷰의 값을 바꿔준다.
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        // TODO Auto-generated method stub
                        //monthOfYear는 0값이 1월을 뜻하므로 1을 더해줌 나머지는 같다.
                       // mTxtDate.setText(String.format("%d/%d/%d", year,monthOfYear + 1, dayOfMonth));
                    }

                });*/

        //선택기로부터 날짜 조사
        findViewById(R.id.btnnow).setOnClickListener(new View.OnClickListener() {
            //버튼 클릭시 DatePicker로부터 값을 읽어와서 Toast메시지로 보여준다.
            @Override
            public void onClick(View v) {
                // FragmentManager fragmentManager = getSupportFragmentManager();
                DatePicker mDate = findViewById(R.id.datepicker);
                Intent intent = new Intent(CalendarActivity.this, BottomActivity.class);

                Bundle bundle = new Bundle();

                // FragmentTransaction transaction = fragmentManager.beginTransaction();
                intent.putExtra("Selected Year",mDate.getYear());
                intent.putExtra("Selected Month", mDate.getMonth() + 1);
                intent.putExtra("Selected Day", mDate.getDayOfMonth());
                intent.putExtras(bundle);

                startActivity(intent);
                // FoodListFragment foodListFragment= new FoodListFragment();
                //foodListFragment.setArguments(bundle);
                // Intent intent = new Intent(getApplicationContext(),BottomActivity.class);
                //startActivity(intent);
                //  transaction.replace(R.id.frame_layout, foodListFragment).commitAllowingStateLoss();

                //replaceFragment()
                // TODO Auto-generated method stub
                //Intent intent = onNewIntent(getApplicationContext(),FoodListFragment.class);
                //Bundle bundle = onNewIntent(getApplicationContext(),FoodListFragment.class);
                //String result = String.format("%d년 %d월 %d일", mDate.getYear(),
                //       mDate.getMonth() + 1, mDate.getDayOfMonth());
                //Toast.makeText(CalendarActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });
    }



}



