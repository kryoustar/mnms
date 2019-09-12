package com.example.project1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReferenceFromUrl("https://project1-cecd8.firebaseio.com/");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) { //로그인 되어있을 경우
            View view = inflater.inflate(R.layout.activity_mypage, container, false);
            String email = user.getEmail(); //get user email
            String uid = user.getUid();

            ArrayList<String> items = new ArrayList<String>(); // 빈 데이터 리스트 생성
            items.add("");
            items.add("");
            items.add("");
            items.add("");
            items.add("");

            //array adapter 생성 아이템 뷰를 선택 가능하도록 만듦
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);

            //items.set(0, "User Email: " + email);

            DatabaseReference ConditionRef1 = dbRef.child("User").child(uid)
                    .child("Personal Info").child("Nickname");

            ConditionRef1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Nickname = dataSnapshot.getValue(String.class);
                    //Toast.makeText(getActivity(), Nickname + "", Toast.LENGTH_SHORT).show();
                    if (Nickname != null) {
                        items.set(0, "닉네임: " + Nickname);
                    } else {
                        items.set(0, "닉네임 ");
                    }
                    adapter.notifyDataSetChanged();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                }
         });


            DatabaseReference ConditionRef2 = dbRef.child("User").child(uid)
                    .child("Personal Info").child("Veganism Type");

            ConditionRef2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String veganType = dataSnapshot.getValue(String.class);
                    //Toast.makeText(getActivity(), veganType + "", Toast.LENGTH_SHORT).show();
                    if (veganType != null) {
                        items.set(1, "채식타입: " + veganType);
                    } else {
                        items.set(1, "채식타입 ");

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


            items.set(2, "개인정보");
            items.set(3, "스크랩");
            items.set(4, "로그아웃");


            ListView listview = view.findViewById(R.id.list); // listview  생성 및 adapter 지정
            listview.setAdapter(adapter);


            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0: //user email
                            //Toast.makeText(getContext(), items.get(position) + "", Toast.LENGTH_SHORT).show();
                            break;

                        case 1: //채식타입
                            //Toast.makeText(getContext(), items.get(position) + "", Toast.LENGTH_SHORT).show();

                            Intent intent1 = new Intent(getActivity(), SelectVeganism.class);
                            startActivity(intent1);
                            break;

                        case 2: //개인정보
                            //Toast.makeText(getContext(), items.get(position) + "", Toast.LENGTH_SHORT).show();
                            Intent Intent = new Intent(getActivity(), PersonalInfo.class);
                            startActivity(Intent);
                            break;

                        case 3:

                        case 4: //로그아웃
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getContext(), "로그아웃 되었습니다." + "", Toast.LENGTH_SHORT).show();
                            Intent intent2 = new Intent(getActivity(), BottomActivity.class);
                            startActivity(intent2);
                            break;
                    }
                }
            });
            return view;
        } else { //로그인 되어있지 않을 경우
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            return null;
        }
    }
}