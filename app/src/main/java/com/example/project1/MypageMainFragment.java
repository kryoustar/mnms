package com.example.project1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MypageMainFragment extends Fragment {
    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) { //로그인 되어있을 경우
            View view = inflater.inflate(R.layout.activity_mypage, container, false);
            return view;
        }
        else { //로그인 되어있지 않을 경우
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            //View view = inflater.inflate(R.layout.activity_login, container, false);
            return null;
        }
    }
}