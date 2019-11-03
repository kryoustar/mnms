package com.example.project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;


public class CalendarActivityTwo extends AppCompatActivity {


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
                Intent intent = new Intent(CalendarActivityTwo.this, BottomActivity.class);

                Bundle bundle = new Bundle();

                // FragmentTransaction transaction = fragmentManager.beginTransaction();
                intent.putExtra("Selected Year",mDate.getYear());
                intent.putExtra("Selected Month", mDate.getMonth() + 1);
                intent.putExtra("Selected Day", mDate.getDayOfMonth());
                intent.putExtras(bundle);

                setResult(RESULT_OK,intent);

                startActivity(intent);
                finish();
            }
        });
    }



}



