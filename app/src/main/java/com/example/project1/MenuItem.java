package com.example.project1;

public class MenuItem {
    private int MenuNumber;
    private String MenuName;
    private String MenuPrice;
    private String MenuType;
    private String MenuNutrients;

    public int getNumber(){
        return MenuNumber;
    }

    public void setNumber(int number) {
        MenuNumber = number;
    }

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

    public MenuItem(int MenuNumber, String MenuName, String MenuPrice, String MenuType, String MenuNutrients){
        this.MenuNumber = MenuNumber;
        this.MenuName = MenuName;
        this.MenuPrice = MenuPrice;
        this.MenuType = MenuType;
        this.MenuNutrients = MenuNutrients;
    }
}
