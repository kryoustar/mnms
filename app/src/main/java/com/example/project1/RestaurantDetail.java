package com.example.project1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RestaurantDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retaurantdetail_view);

        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor cursor;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        ListView listView;   // ListView 객체 생성
        String[] result;   // ArrayAdapter에 넣을 배열 생성

        TextView tvResName = (TextView) findViewById(R.id.resName);
        TextView tvResAdd = (TextView) findViewById(R.id.resAddress);
        TextView tvResPhone = (TextView) findViewById(R.id.resPhoneNumber);
        TextView tvResOpen = (TextView) findViewById(R.id.resOpeningHours);

        Intent intent = getIntent();
        tvResName.setText(intent.getStringExtra("Restaurant Name"));
        tvResAdd.setText(intent.getStringExtra("Restaurant Address"));
        if (intent.getStringExtra(("Restaurant Phone Number"))!=null) {
            tvResPhone.setText("전화번호: " + intent.getStringExtra("Restaurant Phone Number"));
        }
        if (intent.getStringExtra("Restaurant Opening Hours")!=null) {
            tvResOpen.setText("영업시간: " + intent.getStringExtra("Restaurant Opening Hours"));
        }

        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-cecd8.appspot.com").child("image_"+getIntent().getIntExtra("Restaurant Number", 0)+".jpg");
        try { //레스토랑 사진 불러오기
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    ImageView myImage = (ImageView) findViewById(R.id.image);
                    myImage.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }

        catch (IOException e ) {}
        /*
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView myImage = (ImageView) findViewById(R.id.image);
                myImage.setImageBitmap(bitmap);
            }
        });*/



        //메뉴정보
        ArrayList<MenuItem> data = null;
        data = new ArrayList<>();

        db = openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        listView = findViewById(R.id.menulist);
        Integer resNubmer = intent.getIntExtra("Restaurant Number", 0);

        sql = "select * from veganRes02 where 번호 = " + resNubmer;
        cursor = db.rawQuery(sql, null);

        int count = cursor.getCount();   // db에 저장된 행 개수를 읽어온다
        result = new String[count];   // 저장된 행 개수만큼의 배열을 생성

        for (int i = 0; i < count; i++) {
            cursor.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
            int MenuNumber = cursor.getInt(0);
            String MenuName = cursor.getString(1);
            String MenuPrice = cursor.getString(2);
            String MenuType = cursor.getString(3);
            String MenuNutrients = cursor.getString(4);
            MenuItem menuItem = new MenuItem(MenuNumber, MenuName, MenuPrice, MenuType, MenuNutrients);
            result[i] = MenuName + " " + MenuPrice + "원 "
                    + "(" + MenuType + ") (" + MenuNutrients + ")" ; // 각각의 속성값들을 해당 배열의 i번째에 저장
            data.add(menuItem);
            //Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_LONG).show();
        }

        ArrayAdapter adapter = new ArrayAdapter(RestaurantDetail.this, android.R.layout.simple_list_item_1, result);   // ArrayAdapter(this, 출력모양, 배열)
        listView.setAdapter(adapter);
        //Toast.makeText(getApplicationContext(), Integer.toString(count), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), result[1], Toast.LENGTH_LONG).show();




    }

}

