package com.example.cardviewgridviewsample;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cardviewgridviewsample.objects.Person;
import com.example.cardviewgridviewsample.objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EditPersonActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView, updateImage;
    TextInputEditText name, budget;
    String personNameStr;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        imageView = (ImageView) findViewById(R.id.edit_person_image);
        name = (TextInputEditText) findViewById(R.id.edit_person_name);
        budget = (TextInputEditText) findViewById(R.id.edit_person_budget);
        updateImage = (ImageView) findViewById(R.id.updateBtn);

        updateImage.setOnClickListener(this);
        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle!=null){
            personNameStr = bundle.getString("mPerson_name");
        }

        name.setText(personNameStr);

        SharedPreferences userPreference = getSharedPreferences("UserPref", MODE_PRIVATE);
        final String username = (userPreference.getString("uname",""));

        databaseReference.child("users")
                .orderByChild("user_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                String usersKey = dataSnapshot1.getKey();

                                databaseReference.child("users/"+usersKey+"/personlist")
                                        .orderByChild("person_name")
                                        .equalTo(personNameStr)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot11: dataSnapshot.getChildren()){
                                                        String personlistKey = dataSnapshot11.getKey();
                                                        Person person = dataSnapshot11.getValue(Person.class);
                                                        String personName = person.getPerson_name();
                                                        double personBudget = person.getPerson_budget();
                                                        String personImage = person.getPerson_image();

                                                        name.setText(personName);
                                                        budget.setText(String.valueOf(personBudget));
                                                        Picasso.with(EditPersonActivity.this).load(personImage).into(imageView);
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
            case R.id.updateBtn:
                updatePersonDetails();
                break;
        }
    }

    private void updatePersonDetails() {

        SharedPreferences userPreference = getSharedPreferences("UserPref", MODE_PRIVATE);
        final String username = (userPreference.getString("uname",""));

        databaseReference.child("users")
                .orderByChild("user_username")
                .equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                                final String usersKey = dataSnapshot1.getKey();
                                final Users users = dataSnapshot1.getValue(Users.class);
                                final double usersBudget = users.getUser_budget();

                                databaseReference.child("users/"+usersKey+"/personlist")
                                        .orderByChild("person_name")
                                        .equalTo(personNameStr)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot11: dataSnapshot.getChildren()){
                                                        String personlistKey = dataSnapshot11.getKey();

                                                        //
                                                        databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey+"/person_name")
                                                                .setValue(name.getText().toString());
//                                                        databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey+"/person_budget")
//                                                                .setValue(Double.parseDouble(budget.getText().toString()));
//
//                                                        String newBudget = budget.getText().toString();
//                                                        double newBudgetDouble = Double.parseDouble(newBudget);
//
//                                                        double mnewBudget = 0;
//
//                                                        if (newBudgetDouble > usersBudget ){
//                                                            mnewBudget = newBudgetDouble + usersBudget;
//                                                        }else if (ne){
//
//                                                        }
//
//                                                        databaseReference.child("users/"+usersKey+"/user_budget").setValue(Double.parseDouble(String.valueOf(usersBudget - newBudgetDouble)));
                                                    }
                                                    Toast.makeText(EditPersonActivity.this, "Person updated successfully", Toast.LENGTH_SHORT).show();
                                                    finish();
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
