package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

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
            String email = user.getEmail(); //get user email

            ListView list;
            ArrayList<String> items;
            list = view.findViewById(R.id.list);
            items = new ArrayList<String>();
            items.add("User Email: "+email);
            items.add("Types of Veganism");
            items.add("Personal Information");
            items.add("Logout");

            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);

            list.setAdapter(itemsAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0: //user email
                            Toast.makeText(getContext(), items.get(position)+"", Toast.LENGTH_SHORT).show();
                            break;

                        case 1: //채식타입
                            Toast.makeText(getContext(), items.get(position)+"", Toast.LENGTH_SHORT).show();
                            break;

                        case 2: //개인정보
                            Intent Intent = new Intent(getActivity(), PersonalInfo.class);
                            startActivity(Intent);
                            //Toast.makeText(getContext(), items.get(position)+"", Toast.LENGTH_SHORT).show();
                            break;

                        case 3: //로그아웃
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getContext(), "로그아웃 되었습니다."+"", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), BottomActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            });
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