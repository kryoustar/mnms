package com.example.project1;

public class RestaurantItem {
    private int Number;
    private String RestaurantName;
    private String RestaurantAddress;
    private String RestaurantPhoneNumber;
    private String RestaurantOpeningHours;


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

    public RestaurantItem(int Number, String RestaurantName, String RestaurantAddress, String RestaurantPhoneNumber, String RestaurantOpeningHours){
        this.Number = Number;
        this.RestaurantName = RestaurantName;
        this.RestaurantAddress = RestaurantAddress;
        this.RestaurantPhoneNumber = RestaurantPhoneNumber;
        this.RestaurantOpeningHours = RestaurantOpeningHours;
    }
}
