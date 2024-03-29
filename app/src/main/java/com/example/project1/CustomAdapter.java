package com.example.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private String[] Title;
    private Integer[] integers;
    ImageView i1;

    public CustomAdapter(Context context, String[] text1,Integer[] int1) {
        mContext = context;
        Title = text1;
        integers = int1;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return Title.length;
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row;
        row = inflater.inflate(R.layout.row, parent, false);
        TextView title;


        title = row.findViewById(R.id.txtTitle);
        title.setText(Title[position]);


        //FirebaseStorage storage = FirebaseStorage.getInstance();
        //StorageReference storageRef = storage.getReferenceFromUrl("gs://project1-cecd8.appspot.com").child("image_" + (position+1) + ".jpg");

        int drawable = mContext.getResources().
                getIdentifier("image_" + integers[position],"drawable",mContext.getPackageName());
        i1 = row.findViewById(R.id.imgIcon);
        i1.setImageResource(drawable);
        /*
        try {

            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    i1 = (ImageView) row.findViewById(R.id.imgIcon);
                    i1.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }

        catch (IOException e ) {}
*/
        return (row);
    }
}