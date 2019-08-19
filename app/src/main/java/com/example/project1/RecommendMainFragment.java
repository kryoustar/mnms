package com.example.project1;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class RecommendMainFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.recommend_main,container,false);
        //구현하고 싶은거
        return view;
    }
}