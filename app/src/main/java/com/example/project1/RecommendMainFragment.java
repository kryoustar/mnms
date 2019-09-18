package com.example.project1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RecommendMainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recommend_main, container, false);
        TextView mLowLabel, mMidLabel, mHighLabel;
        TextView lacknutrients, lacknutrients2;
        BarView mLowBar, mMidBar, mHighBar;
        ImageView recommendImage1, recommendImage2;
        TextView recommendTitle1, recommendTitle2, recommendMenu, recommendMenu1, recommendMenu2;

        mLowBar = view.findViewById(R.id.low_bar);
        mMidBar = view.findViewById(R.id.mid_bar);
        mHighBar = view.findViewById(R.id.high_bar);

        mLowLabel = view.findViewById(R.id.low_text);
        mMidLabel = view.findViewById(R.id.mid_text);
        mHighLabel = view.findViewById(R.id.high_text);

        lacknutrients = view.findViewById(R.id.lacknutrients);
        lacknutrients2 = view.findViewById(R.id.lacknutrients2);


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String date = formatter.format(today);

        DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ConditionRef = Database.child("User")
                .child(uid).child("Meal");

        String[] weekArray = new String[7];
        for (int i = 1; i < 8; i++) {
            Calendar todayCalendar = new GregorianCalendar();
            todayCalendar.add(Calendar.DATE, -i);
            String tempDate;
            String toYear = Integer.toString(todayCalendar.get(Calendar.YEAR));
            String toMonth = Integer.toString(todayCalendar.get(Calendar.MONTH) + 1);
            if ((todayCalendar.get(Calendar.MONTH) + 1)<10) {
                toMonth = "0" + toMonth;
            }
            String toDayofMonth = Integer.toString(todayCalendar.get(Calendar.DATE));
            if (todayCalendar.get(Calendar.DATE) < 10) {
                toDayofMonth = "0" + toDayofMonth;
            }
            tempDate = toYear + "-" + toMonth + "-" + toDayofMonth;
            weekArray[i - 1] = tempDate;
            // 일주일 치 날 짜 배열 생성
        }

        TextView week = view.findViewById(R.id.weekdate);
        String weekdate = weekArray[6] + " ~ " + weekArray[0];
        week.setText(weekdate);

        ArrayList<Integer> weekMeal = new ArrayList<Integer>(); // 빈 데이터 리스트 생성
        for (int i = 0; i < 7; i++) {
            ConditionRef.child(weekArray[i]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Integer index = snapshot.getValue(Integer.class);
                        weekMeal.add(index);
                    }

                    //사용자의 gender, age 받아오기
                    String[] result;
                    result = new String[2]; //gender, sex 저장

                    DatabaseReference ConditionRef2 = Database.child("User").child(uid).child("Personal Info");
                    ConditionRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String temp = snapshot.getValue(String.class); //value 받아옴
                                String key = snapshot.getKey(); //key 받아옴

                                if (key.equals("Gender")) {
                                    result[0] = temp;
                                } else if (key.equals("Age")) {
                                    result[1] = temp;
                                }
                            }

                            //띄움
                            float food_kcal = 0, food_carbs = 0,
                                    food_protein = 0, food_fat = 0, food_natrium = 0, food_sugar = 0,
                                      food_cholesterol = 0 , food_saturatedFat = 0, food_transFat = 0;

                            for (int i = 0; i < weekMeal.size(); i++) {
                                Integer findIndex = weekMeal.get(i);
                                FoodItem foodItem = FoodItem.FoodItemSearch(findIndex, getActivity());
                                food_kcal += foodItem.getFoodKcal();
                                food_carbs += foodItem.getFoodCarbs();
                                food_protein += foodItem.getFoodProtein();
                                food_fat += foodItem.getFoodFat();
                                food_natrium += foodItem.getFoodNatrium();
                                food_sugar += foodItem.getFoodSugar();
                                food_cholesterol += foodItem.getFoodCholesterol();
                                food_saturatedFat += foodItem.getFoodSaturatedfat();
                                food_transFat += foodItem.getFoodTransfat();

                            }
                            int show_natrium = (int)food_natrium ;
                            int show_sugar = (int)food_sugar;
                            int show_cholesterol = (int)food_cholesterol ;
                            int show_saturatedFat = (int)food_saturatedFat ;
                            int show_transFat = (int)food_transFat ;


                            TextView forSugar = view.findViewById(R.id.sugar);
                            forSugar.setText("\n당류: \n"+show_sugar + "(g)");
                            TextView forNatrium = view.findViewById(R.id.natrium);
                            forNatrium.setText("나트륨: \n" + show_natrium + "(mg)");
                            TextView forCholesterol = view.findViewById(R.id.cholestrol);
                            forCholesterol.setText("\n콜레스테롤: \n"+ show_cholesterol + "(mg)");
                            TextView forSatFat = view.findViewById(R.id.saturatedfat);
                            forSatFat.setText("\n포화지방산: \n"+ show_saturatedFat + "(g)");
                            TextView forTransFat = view.findViewById(R.id.transfat);
                            forTransFat.setText("\n트랜스지방산: \n"+ show_transFat + "(g)");

                            PersonalItem personalItem = PersonalItem.PersonalItemSearch(result[0], result[1], getActivity());
                            Float kcal = personalItem.getPersonKcal() * 7;
                            Float carbs = personalItem.getPersonCarbs() * 7;
                            Float protein = personalItem.getPersonProtein() * 7;
                            Float fat = personalItem.getPersonFat() * 7;
                            Float natrium = personalItem.getPersonNatrium() * 7;


                            int low = Math.round(food_carbs / carbs * 100);
                            int mid = Math.round(food_protein / protein * 100);
                            int high = Math.round(food_fat / fat * 100);

                            if (low>100)
                                low = 100;
                            if (mid>100)
                                mid = 100;
                            if (high>100)
                                high=100;

                            if (low < 30) {
                                mLowBar.set(Color.RED, low);
                            }
                            if (low >= 30 && low < 70) {
                                mLowBar.set(Color.rgb(255, 187, 0), low);
                            }
                            if (low >= 70) {
                                mLowBar.set(Color.rgb(1, 160, 26), low);
                            }
                            if (mid < 30) {
                                mMidBar.set(Color.RED, mid);

                            }
                            if (mid >= 30 && low < 70) {
                                mMidBar.set(Color.rgb(255, 187, 0), mid);

                            }
                            if (mid >= 70) {
                                mMidBar.set(Color.rgb(1, 160, 26), mid);

                            }
                            if (high < 30) {
                                mHighBar.set(Color.RED, high);

                            }
                            if (high >= 30 && low < 70) {
                                mHighBar.set(Color.rgb(255, 187, 0), high);


                            }
                            if (high >= 70) {
                                mHighBar.set(Color.rgb(1, 160, 26), high);

                            }

                            mLowLabel.setText(low + "%");
                            mMidLabel.setText(mid + "%");
                            mHighLabel.setText(high + "%");


                            int minNut = 0; //가장 적은 영양소: 탄수화물_1, 단백질_2, 지방_3

                            if (low<mid) {
                                if (low<high) {
                                    lacknutrients.setText("지난 일주일 간 [탄수화물]이 가장 부족합니다.");
                                    lacknutrients2.setText("[탄수화물]이 부족할 땐 감자튀김, 쿠키 등을 먹는 것이 좋습니다.");
                                    //recommendMenu.setText("[탄수화물] 섭취를 위한 내 주변 추천 메뉴");
                                    minNut = 1;
                                } else {
                                    lacknutrients.setText("지난 일주일 간 [지방]이 가장 부족합니다.");
                                    lacknutrients2.setText("[지방]이 부족할 땐 도넛, 아보카도, 견과류 등을 먹는 것이 좋습니다.");
                                    //recommendMenu.setText("[지방] 섭취를 위한 내 주변 추천 메뉴");
                                    minNut = 3;

                                }
                            }
                            else {
                                if (mid<high) {
                                    lacknutrients.setText("지난 일주일 간 [단백질]이 가장 부족합니다.");
                                    lacknutrients2.setText("[단백질]이 부족할 땐 브로콜리, 검정콩, 두부 등을 먹는 것이 좋습니다.");
                                   // recommendMenu.setText("[단백질] 섭취를 위한 내 주변 추천 메뉴");

                                    minNut = 2;

                                } else {
                                    lacknutrients.setText("지난 일주일 간 [지방]이 가장 부족합니다.");
                                    lacknutrients2.setText("[지방]이 부족할 땐 도넛, 아보카도, 견과류 등을 먹는 것이 좋습니다.");
                                    //recommendMenu.setText("[지방] 섭취를 위한 내 주변 추천 메뉴");
                                    minNut = 3;

                                }
                            }

                            /*
                            //부족한 영양소에 따른 메뉴 추천(용산구 내 식당 메뉴 임의로 10개씩 선택)
                            Integer [] temp;
                            Integer [] carbsRec = {233, 236, 241, 257, 268, 272, 320, 365, 384, 400};
                            Integer [] proteinRec = {206, 217, 273, 321, 325, 392, 417, 423, 436, 444};
                            Integer [] fatRec = {194, 195, 201, 212, 244, 306, 357, 426, 333, 364};

                            if (minNut == 1) {
                                temp = carbsRec;
                            }
                            else if (minNut == 2) {
                                temp = proteinRec;
                            }
                            else {
                                temp = fatRec;
                            }
*/
                            /*
                            int ranNum1 = new Random().nextInt(10);
                            int ranNum2 = new Random().nextInt(10);
                            while (ranNum1 == ranNum2) {
                                ranNum2 = new Random().nextInt(10);
                            }

                            MenuItem menuItem1 = MenuItem.menuItemSearch(temp[ranNum1], getActivity());
                            MenuItem menuItem2 = MenuItem.menuItemSearch(temp[ranNum2], getActivity());
                            String Menu1 = menuItem1.getMenuName();
                            String Menu2 = menuItem2.getMenuName();

                            int ResNum1 = menuItem1.getNumber(); //레스토랑 넘버
                            int ResNum2 = menuItem2.getNumber();

                            RestaurantItem restaurantItem1 = RestaurantItem.restaurantItemSearch(ResNum1, getActivity());
                            RestaurantItem restaurantItem2 = RestaurantItem.restaurantItemSearch(ResNum2, getActivity());
                            String resName1 = restaurantItem1.getRestaurantName();
                            String resName2 = restaurantItem2.getRestaurantName();

                            recommendMenu1.setText(Menu1);
                            recommendMenu2.setText(Menu2);
                            recommendTitle1.setText(resName1);
                            recommendTitle2.setText(resName2);
*/
/*
                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-cecd8.appspot.com").child("image_" + 1 + ".jpg");
                            //StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-cecd8.appspot.com").child("image_" + resNum1 + ".jpg");

                            try {
                                final File localFile = File.createTempFile("images", "jpg");
                                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        recommendImage1.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                });
                            } catch (IOException e) {
                            }
*/
/*


                            StorageReference storageRef2 = storage.getReferenceFromUrl("gs://project1-cecd8.appspot.com").child("image_" + 1 + ".jpg");
                            //StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-cecd8.appspot.com").child("image_" + resNum2 + ".jpg");

                            try {
                                final File localFile = File.createTempFile("images", "jpg");
                                storageRef2.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                        recommendImage2.setImageBitmap(bitmap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                });
                            } catch (IOException e) {
                            }
*/
/*
                            //첫 번째 추천메뉴 사진 클릭했을 때
                            recommendImage1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), RestaurantDetail.class);

                                    intent.putExtra("Restaurant Number", restaurantItem1.getNumber());
                                    startActivity(intent);
                                }
                            });
*/
/*

                            //두 번째 추천메뉴 사진 클릭했을 때
                            recommendImage2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), RestaurantDetail.class);

                                    intent.putExtra("Restaurant Number", restaurantItem2.getNumber());
                                    startActivity(intent);
                                }
                            });
*/


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return view;
    }
}