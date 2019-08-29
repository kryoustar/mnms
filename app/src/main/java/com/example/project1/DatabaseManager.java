package com.example.project1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatabaseManager extends AppCompatActivity {

    public static void MealDataAdd(String date, int foodNumber) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("/User");  //FromUrl("https://project1-cecd8.firebaseio.com/");
        DatabaseReference tempRef = dbRef.child(uid).child("Meal").child(date);

        if (tempRef == null) {
            tempRef.setValue(foodNumber);
        } else {
            tempRef.push().setValue(foodNumber);
        }

    }

    public static void mealDateDelete(String date, int foodNumber) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("/User");
        DatabaseReference tempRef = dbRef.child(uid).child("Meal").child(date);


    }
    
}
