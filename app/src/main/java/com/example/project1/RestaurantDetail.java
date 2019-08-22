package com.example.project1;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;

public class RestaurantDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retaurantdetail_view);

        TextView tvResName = (TextView) findViewById(R.id.resName);
        TextView tvResAdd = (TextView) findViewById(R.id.resAddress);
        TextView tvResPhone = (TextView) findViewById(R.id.resPhoneNumber);
        TextView tvResOpen = (TextView) findViewById(R.id.resOpeningHours);

        Intent intent = getIntent();
        tvResName.setText(intent.getStringExtra("Restaurant Name"));
        tvResAdd.setText(intent.getStringExtra("Restaurant Address"));
        tvResPhone.setText(intent.getStringExtra("Restaurant Phone Number"));
        tvResOpen.setText(intent.getStringExtra("Restaurant Opening Hours"));
        FirebaseApp.initializeApp(this);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-cecd8.appspot.com").child("image_"+getIntent().getIntExtra("Restaurant Number", 0)+".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    ImageView myImage = (ImageView) findViewById(R.id.image);
                    myImage.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }

        catch (IOException e ) {}
        /*
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView myImage = (ImageView) findViewById(R.id.image);
                myImage.setImageBitmap(bitmap);
            }
        });*/
    }

}

