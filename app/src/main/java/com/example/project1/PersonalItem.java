package com.example.project1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PersonalItem {
    //gender age kcal carbs protein fat natrium
    public String PersonGender;
    public int PersonAge;
    public float PersonKcal;
    public float PersonCarbs;
    public float PersonProtein;
    public float PersonFat;
    public float PersonNatrium;


    public String getPersonGender() {
        return PersonGender;
    }

    public int getPersonAge() {
        return PersonAge;
    }

    public float getPersonKcal() {
        return PersonKcal;
    }

    public float getPersonCarbs() {
        return PersonCarbs;
    }

    public float getPersonProtein() {
        return PersonProtein;
    }

    public float getPersonFat() {
        return PersonFat;
    }

    public float getPersonNatrium() {
        return PersonNatrium;
    }


    public PersonalItem(String PersonGender, int PersonAge, float PersonKcal, float PersonCarbs,
                        float PersonProtein, float PersonFat, float PersonNatrium) {
        this.PersonGender = PersonGender;
        this.PersonAge = PersonAge;
        this.PersonKcal = PersonKcal;
        this.PersonCarbs = PersonCarbs;
        this.PersonProtein = PersonProtein;
        this.PersonFat = PersonFat;
        this.PersonNatrium = PersonNatrium;

    }


    public static PersonalItem PersonalItemSearch(String gender, String age, Context context) {
        Context myContext = context;
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성

        db = myContext.openOrCreateDatabase("nutrientsPerday.db", android.content.Context.MODE_PRIVATE, null);
        sql = "select * from nutrientsPerday_tb where ( gender = '"+gender+"') and ( age = '"+age+"' )";

        c = db.rawQuery(sql, null);
        c.moveToNext();
        //gender age kcal carbs protein fat natrium

        float kcal = c.getFloat(2); // 열량
        float carbs = c.getFloat(3); // 탄수화물
        float protein = c.getFloat(4); // 단백질
        float fat = c.getFloat(5); // 지방
        float natrium = c.getFloat(6); // 나트륨

        PersonalItem personalItem = new PersonalItem(gender, Integer.valueOf(age), kcal, carbs, protein, fat, natrium);
        return personalItem;
    }

}