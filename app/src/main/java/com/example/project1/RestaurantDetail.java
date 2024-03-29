package com.example.project1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

//import com.example.project1.com.like.LikeButton;
//import com.like.LikeButton;

public class RestaurantDetail extends AppCompatActivity {
    int number;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retaurantdetail_view);

        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        ListView listView;   // ListView 객체 생성
        String[] result;   // ArrayAdapter에 넣을 배열 생성

        TextView tvResName = findViewById(R.id.resName);
        TextView tvResAdd = findViewById(R.id.resAddress);
        TextView tvResPhone = findViewById(R.id.resPhoneNumber);
        TextView tvResOpen = findViewById(R.id.resOpeningHours);

        Intent intent = getIntent();
        int restaurantIndex = intent.getIntExtra("Restaurant Number", 0);
        RestaurantItem thisItem = RestaurantItem.restaurantItemSearch(restaurantIndex, getApplication());
        tvResName.setText(thisItem.getRestaurantName());

        tvResAdd.setText(thisItem.getRestaurantPhoneNumber());
        if (thisItem.getRestaurantOpeningHours() != null) {
            tvResPhone.setText(thisItem.getRestaurantOpeningHours());
        }
        if (thisItem.getRestaurantCity() != null) {
            tvResOpen.setText(thisItem.getRestaurantCity());
        }
        int drawable;

        drawable = getResources().
                getIdentifier("image_" + restaurantIndex, "drawable", getPackageName());

        ImageView myImage = findViewById(R.id.image);
        myImage.setImageResource(drawable);


        /*FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-cecd8.appspot.com").child("image_" + (restaurantIndex-111) + ".jpg");
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
        } catch (IOException e) {
        }*/

        //하트 버튼
        RestaurantItem restaurantItem = RestaurantItem.restaurantItemSearch(restaurantIndex, getApplicationContext());

        LikeButton likeButton = findViewById(R.id.heartButton);
        //likeButton.setLiked(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ConditionRef = ref.child("User").child(uid).child("Scrap");
        Integer[] scraps = new Integer[1000];
        ConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Integer temp = snapshot.getValue(Integer.class);
                    if (restaurantIndex == temp) {
                        likeButton.setLiked(true);
                        break;
                    } else {
                        likeButton.setLiked(false);
                    }
                    i++;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                DatabaseManager.ScrapDataAdd(restaurantIndex);
                Toast.makeText(getApplicationContext(), restaurantItem.getRestaurantName() + "을/를 스크랩하였습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                DatabaseManager.ScrapDataDelete(restaurantIndex);
                Toast.makeText(getApplicationContext(), restaurantItem.getRestaurantName() + "을/를 지웠습니다.", Toast.LENGTH_SHORT).show();

            }
        });


        //메뉴정보
        ArrayList<MenuItem> data = null;
        data = new ArrayList<>();
        ArrayList<FoodItem> foodData = null;
        foodData = new ArrayList<>();

        db = openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        listView = findViewById(R.id.menulist);
        Integer resNubmer = restaurantIndex;
        Cursor cursor;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성

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
            int MenuNumber2 = cursor.getInt(5);
            MenuItem menuItem = new MenuItem(MenuNumber, MenuName, MenuPrice, MenuType, MenuNutrients, MenuNumber2);
            if (MenuPrice.equals("미정")) {
                result[i] = MenuName ;
            }
            else
                result[i] = MenuName + "\t \t" + MenuPrice + "원 " ;

            data.add(menuItem);
            foodData.add(FoodItem.FoodItemStringSearch(menuItem.getMenuName(), getApplicationContext()));
        }
        final ArrayList<FoodItem> finalData = foodData;
        ArrayAdapter adapter = new ArrayAdapter(RestaurantDetail.this, R.layout.simpleitemtwo, result);   // ArrayAdapter(this, 출력모양, 배열)
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                number = finalData.get(i).getFoodNumber();
                return false;
            }
        });

    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        menu.add(0, v.getId(), 0, "오늘 먹은 음식에 추가하기");
        // menu.add(0, v.getId(), 0, "Delete");
    }

    public boolean onContextItemSelected(android.view.MenuItem item) {
        if (item.getTitle() == "오늘 먹은 음식에 추가하기") {
            Calendar todayCal = Calendar.getInstance();
            String toYear = Integer.toString(todayCal.get(Calendar.YEAR));
            String toMonth = Integer.toString(todayCal.get(Calendar.MONTH) + 1);
            if ((todayCal.get(Calendar.MONTH) + 1) < 10) {
                toMonth = "0" + toMonth;
            }
            String toDayofMonth = Integer.toString(todayCal.get(Calendar.DATE));
            if (todayCal.get(Calendar.DATE) < 10) {
                toDayofMonth = "0" + toDayofMonth;
            }
            String date = toYear + "-" + toMonth + "-" + toDayofMonth;
            Toast.makeText(getApplicationContext(), FoodItem.FoodItemSearch(number, getApplicationContext()).getFoodName() + "을(를) 추가하였습니다.", Toast.LENGTH_SHORT).show();


            DatabaseManager.MealDataAdd(date, number);


        }
        return true;
    }
}

