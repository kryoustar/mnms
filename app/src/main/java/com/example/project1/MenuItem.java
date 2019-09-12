package com.example.project1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MenuItem {
    private int MenuNumber; //레스토랑 넘버
    private String MenuName;
    private String MenuPrice;
    private String MenuType;
    private String MenuNutrients;
    private int MenuNumber2; //메뉴 고유넘버

    public int getNumber(){
        return MenuNumber;
    }

    public void setNumber(int number) {
        MenuNumber = number;
    }

    public int getNumber2() {
        return MenuNumber2;
    }

    public void setNumber2(int number) { MenuNumber2 = number; }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getMenuPrice() {
        return MenuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        MenuPrice = menuPrice;
    }

    public String getMenuType() {
        return MenuType;
    }

    public void setMenuType(String menuType) {
        MenuType = menuType;
    }

    public String getMenuNutrients() {
        return MenuNutrients;
    }

    public void setMenuNutrients(String menuNutrients) {
        MenuNutrients = menuNutrients;
    }

    public static MenuItem menuItemSearch(Integer keyIndex, Context context) {
        Context myContext = context;
        String sql;
        SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
        Cursor c;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
        db = myContext.openOrCreateDatabase("vRes.db", android.content.Context.MODE_PRIVATE, null);
        sql = "select * from veganRes02 where 메뉴번호 = " + keyIndex;
        c = db.rawQuery(sql, null);
        c.moveToNext();
        int number = c.getInt(0); //레스토랑 넘버
        String name = c.getString(1); //메뉴 이름
        String price = c.getString(2); //메뉴 가격
        String type = c.getString(3); //비건단계
        String nutrients = c.getString(4); //주요 영양소
        int number2 = c.getInt(5); //메뉴 번호

        MenuItem menuItem = new MenuItem(number, name, price, type, nutrients, number2);
        return menuItem;
    }




    public MenuItem(int MenuNumber, String MenuName, String MenuPrice, String MenuType, String MenuNutrients, int MenuNumber2){
        this.MenuNumber = MenuNumber;
        this.MenuName = MenuName;
        this.MenuPrice = MenuPrice;
        this.MenuType = MenuType;
        this.MenuNutrients = MenuNutrients;
        this.MenuNumber2 = MenuNumber2;
    }
}
