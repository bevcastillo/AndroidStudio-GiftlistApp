package com.example.cardviewgridviewsample.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.objects.Person;
import com.example.cardviewgridviewsample.objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabStatFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView txtTotalBudget, txtGiftQty;
    double personBudget;
    String totalBudgetStr, personBudgetStr;


    public TabStatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_stat, container, false);

        ///
        txtTotalBudget = (TextView) view.findViewById(R.id.total_budget);
        txtGiftQty = (TextView) view.findViewById(R.id.gift_count);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        displayDetails();
    }

    private void displayDetails() {

        SharedPreferences personNamePref = getActivity().getSharedPreferences("personPref", Context.MODE_PRIVATE);
        final String personName = (personNamePref.getString("person_name",""));

        SharedPreferences userPreference = getActivity().getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        final String username = (userPreference.getString("uname",""));

        databaseReference.child("users").orderByChild("user_username")
                .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        final String userKey = dataSnapshot1.getKey();
                        Users users = dataSnapshot1.getValue(Users.class);
                        final double currentTotalBudget = users.getUser_budget();

                        databaseReference.child("users/"+userKey+"/personlist").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    double mTotal = 0;
                                    int mTotQuantity = 0;
                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                        String personListKey = dataSnapshot2.getKey();
                                        Person person = dataSnapshot2.getValue(Person.class);
                                        double number = person.getPerson_budget();
                                        int quantity = person.getPerson_gift_qty();
                                        mTotal = mTotal+number;
                                        mTotQuantity = mTotQuantity+quantity;

                                    }
                                    databaseReference.child("users/"+userKey+"/user_budget").setValue(mTotal);
                                    txtTotalBudget.setText(String.valueOf(mTotal));

                                    databaseReference.child("users/"+userKey+"/person_gift_qty").setValue(mTotQuantity);
                                    txtGiftQty.setText(String.valueOf(mTotQuantity));

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
