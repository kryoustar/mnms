package com.example.project1;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SaveDiet {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("server/saving-data/fireblog");

    public String breakfast;
    public String lunch;
    public String dinner;
    public String snack;

    public SaveDiet(String breakfast,String lunch, String dinner, String snack){
        this.breakfast = breakfast;
        this.lunch = lunch;
        this.dinner = dinner;
        this.snack = snack;
    }
    DatabaseReference saveDietRef = ref.child("users");

    HashMap<String,SaveDiet> saveDietHashMap = new HashMap();
  //  saveDietHashMap.put("0810",new SaveDiet("break","lunch","dinner","snack"));

}
