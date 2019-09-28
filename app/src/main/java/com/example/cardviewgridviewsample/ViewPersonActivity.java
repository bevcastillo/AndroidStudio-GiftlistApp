package com.example.cardviewgridviewsample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cardviewgridviewsample.adapters.ViewGiftsAdapter;
import com.example.cardviewgridviewsample.objects.Gift;
import com.example.cardviewgridviewsample.objects.Giftlistdata;
import com.example.cardviewgridviewsample.tabs.AddPersonActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPersonActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView editPerson, addGift, backBtn;
    String personNameStr;
    TextView txtPersonName;
    RecyclerView recyclerView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ProgressBar progressBar;
    LinearLayout emptyLayout;

    List<Giftlistdata> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_person);

        editPerson = (ImageView) findViewById(R.id.edit_person);
        addGift = (ImageView) findViewById(R.id.add_gift);
        backBtn = (ImageView) findViewById(R.id.backbutton_image);
        txtPersonName = (TextView) findViewById(R.id.person_name);
        recyclerView = (RecyclerView) findViewById(R.id.gifts_recyclerview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        emptyLayout = (LinearLayout) findViewById(R.id.layout_noitem_found1);

        editPerson.setOnClickListener(this);
        addGift.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences personNamePref = getSharedPreferences("personPref", Context.MODE_PRIVATE);
        final String personName = (personNamePref.getString("person_name",""));

        txtPersonName.setText(personName);

        displayPersonDetails();
    }

    private void displayPersonDetails() {

        SharedPreferences personNamePref = getSharedPreferences("personPref", Context.MODE_PRIVATE);
        final String personName = (personNamePref.getString("person_name",""));

        SharedPreferences userPreference = getSharedPreferences("UserPref", MODE_PRIVATE);
        final String username = (userPreference.getString("uname",""));

        databaseReference.child("users").orderByChild("user_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String usersKey = dataSnapshot1.getKey();

                                databaseReference.child("users/"+usersKey+"/personlist")
                                        .orderByChild("person_name")
                                        .equalTo(personName)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){
                                            for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                String personlistKey = dataSnapshot2.getKey();

                                                databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey+"/person_gift")
                                                        .addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                list = new ArrayList<>();
                                                                for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                    Gift gift = dataSnapshot3.getValue(Gift.class);
                                                                    Giftlistdata listdata = new Giftlistdata();
                                                                    String giftName = gift.getGift_name();
                                                                    double giftPrice = gift.getGift_price();
                                                                    String giftNote = gift.getGift_note();
                                                                    String giftWhere = gift.getGift_where_to_buy();
                                                                    listdata.setGift_name(giftName);
                                                                    listdata.setGift_price(giftPrice);
                                                                    listdata.setGift_note(giftNote);
                                                                    listdata.setGift_where_to_buy(giftWhere);
                                                                    list.add(listdata);
                                                                }
                                                                ViewGiftsAdapter adapter = new ViewGiftsAdapter(list, getApplicationContext());
                                                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                                                                recyclerView.setLayoutManager(layoutManager);
                                                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
                                                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                                recyclerView.setAdapter(adapter);
                                                                adapter.notifyDataSetChanged();

                                                                progressBar.setVisibility(View.GONE);

                                                                if (list.isEmpty()){
                                                                    emptyLayout.setVisibility(View.VISIBLE);
                                                                }else {
                                                                    emptyLayout.setVisibility(View.GONE);
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
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.edit_person:
                Intent intent = new Intent(ViewPersonActivity.this, AddPersonActivity.class);
                startActivity(intent);
                break;
            case R.id.add_gift:
                Intent intent1 = new Intent(ViewPersonActivity.this, AddGiftsActivity.class);
                startActivity(intent1);
                break;
            case R.id.backbutton_image:
                break;
        }
        
    }
}
