package com.example.project1;
import com.google.firebase.auth.FirebaseUser;
public class Login {


    public static boolean isLogin(FirebaseUser user) {
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

}