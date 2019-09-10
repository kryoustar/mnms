package com.example.project1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class FoodItem {
    //탄수화물 지방 단백질 나트륨
    //number type name amount kcal carbs protein fat sugar natrium cholesterol saturatedfat transfat year
    public int FoodNumber;
    public String FoodName;
    public int FoodAmount;
    public float FoodKcal;
    public float FoodCarbs;
    public float FoodProtein;
    public float FoodFat;
    public float FoodSugar;
    public float FoodNatrium;
    public float FoodCholesterol;
    public float FoodSaturatedfat;
    public float FoodTransfat;

    /*public static void main(String[] args){
        FoodItem foodItem = new FoodItem();
        foodItem.FoodItemSearch(Integer index);
    }*/
    public int getFoodNumber() {
        return FoodNumber;
    }

    public String getFoodName() {
        return FoodName;
    }

    public int getFoodAmount() {
        return FoodAmount;
    }

    public float getFoodKcal() {
        return FoodKcal;
    }

    public float getFoodCarbs() {
        return FoodCarbs;
    }

    public float getFoodProtein() {
        return FoodProtein;
    }

    public float getFoodFat() {
        return FoodFat;
    }

    public float getFoodSugar() {
        return FoodSugar;
    }

    public float getFoodNatrium() {
        return FoodNatrium;
    }

    public float getFoodCholesterol() {
        return FoodCholesterol;
    }

    public float getFoodSaturatedfat() {
        return FoodSaturatedfat;
    }

    public float getFoodTransfat() {
        return FoodTransfat;
    }


    public FoodItem(int FoodNumber, String FoodName, int FoodAmount, float FoodKcal, float FoodCarbs,
                    float FoodProtein, float FoodFat, float FoodSugar, float FoodNatrium,
                    float FoodCholesterol, float FoodSaturatedfat, float FoodTransfat) {
        this.FoodNumber = FoodNumber;
        this.FoodName = FoodName;
        this.FoodAmount = FoodAmount;
        this.FoodKcal = FoodKcal;
        this.FoodCarbs = FoodCarbs;
        this.FoodProtein = FoodProtein;
        this.FoodFat = FoodFat;
        this.FoodSugar = FoodSugar;
        this.FoodNatrium = FoodNatrium;
        this.FoodCholesterol = FoodCholesterol;
        this.FoodSaturatedfat = FoodSaturatedfat;
        this.FoodTransfat = FoodTransfat;
    }


    public static FoodItem FoodItemSearch(Integer keyIndex, Context context) {
        Context myContext = context;
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        db = myContext.openOrCreateDatabase("nutrients.db", android.content.Context.MODE_PRIVATE, null);
        sql = "select * from tb_nutrients where number = " + keyIndex;
        c = db.rawQuery(sql, null);
        c.moveToNext();
        int number = c.getInt(0);
        String name = c.getString(2); //식품이름
        int amount = c.getInt(3);  // 1회제공량
        float kcal = c.getFloat(4); // 열량
        float carbs = c.getFloat(5); // 탄수화물
        float protein = c.getFloat(6); // 단백질
        float fat = c.getFloat(7); // 지방
        float sugar = c.getFloat(8); // 당류
        float natrium = c.getFloat(9); //나트륨
        float cholesterol = c.getFloat(10); // 콜레스테롤
        float saturatedfat = c.getFloat(11);// 포화지방산
        float transfat = c.getFloat(12); //트랜스지방산

        FoodItem foodItem = new FoodItem(number, name, amount, kcal, carbs, protein,
                fat, sugar, natrium, cholesterol, saturatedfat, transfat);
        return foodItem;
    }

    public static FoodItem FoodItemStringSearch(String search, Context context) {
        Context myContext = context;
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성

        db = myContext.openOrCreateDatabase("nutrients.db", android.content.Context.MODE_PRIVATE, null);
        sql = "select * from tb_nutrients where name = '" + search + "'";
        c = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)
        c.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
        int number = c.getInt(0);
        String name = c.getString(2); //식품이름
        int amount = c.getInt(3);  // 1회제공량
        float kcal = c.getFloat(4); // 열량
        float carbs = c.getFloat(5); // 탄수화물
        float protein = c.getFloat(6); // 단백질
        float fat = c.getFloat(7); // 지방
        float sugar = c.getFloat(8); // 당류
        float natrium = c.getFloat(9); //나트륨
        float cholesterol = c.getFloat(10); // 콜레스테롤
        float saturatedfat = c.getFloat(11);// 포화지방산
        float transfat = c.getFloat(12); //트랜스지방산

        FoodItem foodItem = new FoodItem(number, name, amount, kcal, carbs, protein,
                fat, sugar, natrium, cholesterol, saturatedfat, transfat);
        return foodItem;
    }


}