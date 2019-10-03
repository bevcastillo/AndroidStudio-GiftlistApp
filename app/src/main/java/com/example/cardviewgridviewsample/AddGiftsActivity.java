package com.example.cardviewgridviewsample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cardviewgridviewsample.objects.Gift;
import com.example.cardviewgridviewsample.objects.Person;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddGiftsActivity extends AppCompatActivity implements View.OnClickListener {

//    Toolbar toolbar;
    LinearLayout giftImageLayout;
    ImageView backButton, addGiftImage;
    Uri imageUri;
    Button addPictureBtn;
    String GiftId, personNameStr;
    TextInputEditText editGiftName, editGiftValue, editGiftWhere, editGiftNote;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private static final int PICK_IMAGE = 100;


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
//        toolbar = (Toolbar) findViewById(R.id.addgift_toolbar);

        backButton.setOnClickListener(this);
        addGiftImage.setOnClickListener(this);
        addPictureBtn.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");
        GiftId = databaseReference.push().getKey();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null){
            personNameStr = bundle.getString("mPerson_name");
        }
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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();


        }
    }

    private void addGift(final String gift_name, String gift_where_to_buy, String gift_note, double gift_price) {
        final Gift gift = new Gift(gift_name, gift_where_to_buy, gift_note, gift_price);

        gift.setGift_wrap_status("unchecked");
        gift.setGift_bought_status("unchecked");

        SharedPreferences userPreference = getSharedPreferences("UserPref", MODE_PRIVATE);
        final String username = (userPreference.getString("uname",""));


        databaseReference.child("users").orderByChild("user_username")
                .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                        final String usersKey = dataSnapshot2.getKey();

                        databaseReference.child("users/"+usersKey+"/personlist")
                                .orderByChild("person_name")
                                .equalTo(personNameStr)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            int giftCount = 0;
                                            for (DataSnapshot dataSnapshot21: dataSnapshot.getChildren()){
                                                String personlistKey = dataSnapshot21.getKey();
                                                Person person = dataSnapshot21.getValue(Person.class);
                                                int quantity = person.getPerson_gift_qty();

                                                databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey+"/person_gift")
                                                        .child(GiftId).setValue(gift);
                                                databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey+"/person_gift_qty")
                                                        .setValue(quantity+1);
                                                //
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
