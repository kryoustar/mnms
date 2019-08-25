package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DatabaseManager extends AppCompatActivity {

    public static void BreakfastDataAdd(String date, int foodNumber){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("/User");
        DatabaseReference tempRef = dbRef.child(uid).child("Meal").child(date).child("Breakfast");

        if(tempRef == null){
            tempRef.setValue(foodNumber);
        }
        else{
            tempRef.push().setValue(foodNumber);
        }

    }
    public static void LunchDataAdd(String date, int foodNumber){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("/User");
        DatabaseReference tempRef = dbRef.child(uid).child("Meal").child(date).child("Lunch");

        if(tempRef == null){
            tempRef.setValue(foodNumber);
        }
        else{
            tempRef.push().setValue(foodNumber);
        }
    }
    public static void DinnerDataAdd(String date, int foodNumber){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("/User");
        DatabaseReference tempRef = dbRef.child(uid).child("Meal").child(date).child("Dinner");

        if(tempRef == null){
            tempRef.setValue(foodNumber);
        }
        else{
            tempRef.push().setValue(foodNumber);
        }
    }
    public static void SnackDataAdd(String date, int foodNumber){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("/User");
        DatabaseReference tempRef = dbRef.child(uid).child("Meal").child(date).child("Snack");

        if(tempRef == null){
            tempRef.setValue(foodNumber);
        }
        else{
            tempRef.push().setValue(foodNumber);
        }
    }
}
