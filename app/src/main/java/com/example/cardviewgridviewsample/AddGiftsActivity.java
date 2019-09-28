package com.example.cardviewgridviewsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.cardviewgridviewsample.objects.Gift;
import com.example.cardviewgridviewsample.objects.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddGiftsActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    LinearLayout giftImageLayout;
    ImageView backButton, addGiftImage;
    Button addPictureBtn;
    String GiftId;
    TextInputEditText editGiftName, editGiftValue, editGiftWhere, editGiftNote;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gifts);

        giftImageLayout = (LinearLayout) findViewById(R.id.gift_image_layout);
        backButton = (ImageView) findViewById(R.id.backbutton_image);
        addGiftImage = (ImageView) findViewById(R.id.imageViewAdd);
        addPictureBtn = (Button) findViewById(R.id.btn_add_picture);
        editGiftName = (TextInputEditText) findViewById(R.id.gift_name);
        editGiftValue = (TextInputEditText) findViewById(R.id.gift_value);
        editGiftWhere = (TextInputEditText) findViewById(R.id.gift_wheretobuy);
        editGiftNote = (TextInputEditText) findViewById(R.id.gift_note);

        backButton.setOnClickListener(this);
        addGiftImage.setOnClickListener(this);
        addPictureBtn.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");
        GiftId = databaseReference.push().getKey();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.backbutton_image:
                break;
            case R.id.imageViewAdd:
                insertGift();
                break;
            case R.id.btn_add_picture:
                break;
        }

    }


    private void addGift(final String gift_name, String gift_where_to_buy, String gift_note, double gift_price) {
        final Gift gift = new Gift(gift_name, gift_where_to_buy, gift_note, gift_price);

        SharedPreferences userPreference = getSharedPreferences("UserPref", MODE_PRIVATE);
        final String username = (userPreference.getString("uname",""));

        //
        SharedPreferences personNamePref = getSharedPreferences("personPref", Context.MODE_PRIVATE);
        final String personName = (personNamePref.getString("person_name",""));


        databaseReference.child("users").orderByChild("user_username")
                .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                        final String usersKey = dataSnapshot2.getKey();

                        databaseReference.child("users/"+usersKey+"/personlist")
                                .orderByChild("person_name")
                                .equalTo(personName)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot21: dataSnapshot.getChildren()){
                                                String personlistKey = dataSnapshot21.getKey();
                                                Person person = dataSnapshot21.getValue(Person.class);

                                                databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey+"/person_gift")
                                                        .child(GiftId).setValue(gift);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void insertGift() {
        String giftName = editGiftName.getText().toString();
        double giftPrice = Double.parseDouble(editGiftValue.getText().toString());
        String giftNote = editGiftNote.getText().toString();
        String giftWhere = editGiftWhere.getText().toString();

        addGift(giftName, giftWhere, giftNote, giftPrice);
        Toast.makeText(this, "Gift successfully added!", Toast.LENGTH_SHORT).show();
        finish();
    }

}
