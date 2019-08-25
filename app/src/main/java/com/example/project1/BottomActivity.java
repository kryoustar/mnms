package com.example.project1;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

public class BottomActivity extends AppCompatActivity {
   private TextView mTextMessage;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FoodListFragment foodListFragment= new FoodListFragment();
    private RecommendMainFragment recommendMainFragment = new RecommendMainFragment();
    private RestaurantListFragment restaurantListFragment = new RestaurantListFragment();
    private MypageMainFragment mypageMainFragment = new MypageMainFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, foodListFragment).commitAllowingStateLoss();

                //(getApplicationContext(),foodList_main).commitAllowingStateLoss()
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
    }*/
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment).commit();
    }
}
