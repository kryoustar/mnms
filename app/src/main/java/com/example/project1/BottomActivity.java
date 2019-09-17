package com.example.project1;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BottomActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FoodListFragment foodListFragment= new FoodListFragment();
    private RecommendMainFragment recommendMainFragment = new RecommendMainFragment();
    private RestaurantListFragment restaurantListFragment = new RestaurantListFragment();
    private MypageMainFragment mypageMainFragment = new MypageMainFragment();
    private MainActivity mainActivity = new MainActivity();

    DatabaseReference Database = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);


        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(Login.isLogin(user)) {
            transaction.replace(R.id.frame_layout, foodListFragment).commitAllowingStateLoss();
        }
        else{
            transaction.replace(R.id.frame_layout, mypageMainFragment).commitAllowingStateLoss();

        }


        bottomNavigationView.setOnNavigationItemSelectedListener((item)->{
            FragmentTransaction transaction2 = fragmentManager.beginTransaction();
            switch (item.getItemId()){
                case R.id.navigation_food:{
                    transaction2.replace(R.id.frame_layout,foodListFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_recommend:{
                    transaction2.replace(R.id.frame_layout,recommendMainFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_restaurant:{
                    transaction2.replace(R.id.frame_layout,restaurantListFragment).commitAllowingStateLoss();
                    break;
                }
                case R.id.navigation_mypage:{
                    transaction2.replace(R.id.frame_layout,mypageMainFragment).commitAllowingStateLoss();
                    break;
                }
            }
            return true;
        });
    }
//
/*
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_food:
                    Intent intentFood = new Intent(getApplicationContext(), FoodList_Main.class);
                    startActivity(intentFood);
                    return true;

                case R.id.navigation_recommend:
                    Intent intentRecommend = new Intent(getApplicationContext(),RecommendMain.class);
                    startActivity(intentRecommend);

                    return true;
                case R.id.navigation_restaurant:
                    Intent intentRestaurant = new Intent(getApplicationContext(),RestaurantList.class);
                    startActivity(intentRestaurant);

                    return true;

                case R.id.navigation_mypage:
                    Intent intentMypage = new Intent(getApplicationContext(),MypageMain.class);
                    startActivity(intentMypage);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    } */

}
