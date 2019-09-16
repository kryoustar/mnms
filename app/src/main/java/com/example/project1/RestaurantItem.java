package com.example.project1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class RestaurantItem {
    private int Number;
    private String RestaurantName;
    private String RestaurantAddress;
    private String RestaurantPhoneNumber;
    private String RestaurantOpeningHours;
    private String RestaurantCity;
    private String RestaurantVeganType;

    public int getNumber(){

        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public String getRestaurantName() {
        return RestaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        RestaurantName = restaurantName;
    }

    public String getRestaurantAddress() {
        return RestaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        RestaurantAddress = restaurantAddress;
    }

    public String getRestaurantPhoneNumber() {
        return RestaurantPhoneNumber;
    }

    public void setRestaurantPhoneNumber(String restaurantPhoneNumber) {
        RestaurantPhoneNumber = restaurantPhoneNumber;
    }

    public String getRestaurantOpeningHours() {
        return RestaurantOpeningHours;
    }

    public void setRestaurantOpeningHours(String restaurantOpeningHours) {
        RestaurantOpeningHours = restaurantOpeningHours;
    }

    public String getRestaurantCity() {
        return RestaurantCity;
    }

    public void setRestaurantCity(String restaurantCity) {
        RestaurantCity = restaurantCity;
    }

    public String getRestaurantVeganType() {
        return RestaurantVeganType;
    }

    public void setRestaurantVeganType(String restaurantVeganType) {
        RestaurantVeganType = restaurantVeganType;
    }


    public static RestaurantItem restaurantItemSearch(Integer keyIndex, Context context) {
        Context myContext = context;
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        db = myContext.openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        sql = "select * from veganRes03 where Number = " + keyIndex;
        c = db.rawQuery(sql, null);
        c.moveToNext();
        int number = c.getInt(0); //식당 넘버
        String name = c.getString(1); //식당 이름
        String city = c.getString(2); //식당 지역구
        String address = c.getString(3); //식당 주소
        String pheonnumber = c.getString(4); //식당번호
        String openinghours = c.getString(5); //영업시간
        String vegantype = c.getString(6); //비건타입

        RestaurantItem restaurantItem = new RestaurantItem(number, name, city, address, pheonnumber, openinghours, vegantype);
        return restaurantItem;
    }

    public static ArrayList<RestaurantItem> restaurantItemSQLSearch(Context context,String search) {
        Context myContext = context;
        String sql;
        ArrayList<RestaurantItem> data = null;
        data = new ArrayList<>();
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        db = myContext.openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        sql = search;
        c = db.rawQuery(sql, null);
        int count = c.getCount();
        for (int i = 0; i<count ; i++){
            c.moveToNext();
            int number = c.getInt(0); //식당 넘버
            String name = c.getString(1); //식당 이름
            String city = c.getString(2); //식당 지역구
            String address = c.getString(3); //식당 주소
            String pheonnumber = c.getString(4); //식당번호
            String openinghours = c.getString(5); //영업시간
            String vegantype = c.getString(6); //비건타입
            RestaurantItem restaurantItem = new RestaurantItem(number, name, address, pheonnumber, openinghours, city, vegantype);
            data.add(restaurantItem);
        }

        return data;
    }

    public static String[] returnResult(Context context,String search) {
        Context myContext = context;
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        db = myContext.openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        sql = search;
        c = db.rawQuery(sql, null);
        int count = c.getCount();
        String[] result = new String[count];
        for (int i = 0; i<count ; i++){
            c.moveToNext();
            int number = c.getInt(0); //식당 넘버
            String name = c.getString(1); //식당 이름
            String city = c.getString(2); //식당 지역구
            String address = c.getString(3); //식당 주소
            String pheonnumber = c.getString(4); //식당번호
            String openinghours = c.getString(5); //영업시간
            String vegantype = c.getString(6); //비건타입
            RestaurantItem restaurantItem = new RestaurantItem(number, name, address, pheonnumber, openinghours, city, vegantype);
            result[i] = name;
        }
        return result;
    }

    public static Integer[] returnIntegerResult(Context context,String search) {
        Context myContext = context;
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        db = myContext.openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        sql = search;
        c = db.rawQuery(sql, null);
        int count = c.getCount();
        Integer[] result = new Integer[count];
        for (int i = 0; i<count ; i++){
            c.moveToNext();
            int number = c.getInt(0); //식당 넘버
            String name = c.getString(1); //식당 이름
            String city = c.getString(2); //식당 지역구
            String address = c.getString(3); //식당 주소
            String pheonnumber = c.getString(4); //식당번호
            String openinghours = c.getString(5); //영업시간
            String vegantype = c.getString(6); //비건타입
            RestaurantItem restaurantItem = new RestaurantItem(number, name, address, pheonnumber, openinghours, city, vegantype);
            result[i] = number;
        }
        return result;
    }

    public static RestaurantItem RestaurantItemStringSearch(String search, Context context){
        Context myContext = context;
        String sql;
        SQLiteDatabase db;
        Cursor c;
        db = myContext.openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        //sql = "select * from veganRes03 where name = '" + search + "'" ;
        sql = "select * from veganRes03 where RestaurantName = '" + search + "'";
        c = db.rawQuery(sql,null);
        c.moveToNext();
        int number = c.getInt(0); //식당 넘버
        String name = c.getString(1); //식당 이름
        String city = c.getString(2); //식당 지역구
        String address = c.getString(3); //식당 주소
        String pheonnumber = c.getString(4); //식당번호
        String openinghours = c.getString(5); //영업시간
        String vegantype = c.getString(6); //비건타입
        RestaurantItem restaurantItem = new RestaurantItem(number, name, address, pheonnumber, openinghours, city, vegantype);

        return restaurantItem;
    }
    public RestaurantItem(int Number, String RestaurantName, String RestaurantAddress, String RestaurantPhoneNumber, String RestaurantOpeningHours, String RestaurantCity, String RestaurantVeganType){
        this.Number = Number;
        this.RestaurantName = RestaurantName;
        this.RestaurantAddress = RestaurantAddress;
        this.RestaurantPhoneNumber = RestaurantPhoneNumber;
        this.RestaurantOpeningHours = RestaurantOpeningHours;
        this.RestaurantCity = RestaurantCity;
        this.RestaurantVeganType = RestaurantVeganType;
    }
}
