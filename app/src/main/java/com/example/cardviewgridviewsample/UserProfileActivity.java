package com.example.cardviewgridviewsample;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cardviewgridviewsample.objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText name, txtusername, password;
    ImageView imageView;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = (TextInputEditText) findViewById(R.id.person_name);
        txtusername = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);
        imageView = (ImageView) findViewById(R.id.imageViewAdd);

        imageView.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences userPreference = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
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
                                Users users = dataSnapshot1.getValue(Users.class);
                                String usersName = users.getUser_name();
                                String usersUsername = users.getUser_username();
                                String usersPassword = users.getUser_password();

                                name.setText(usersName);
                                txtusername.setText(usersUsername);
                                password.setText(usersPassword);
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
            case R.id.imageViewAdd:
                updateUsersDetails();
                break;
        }
    }

    private void updateUsersDetails() {
        final String mName = name.getText().toString();
        final String mUsername = txtusername.getText().toString();
        final String mPassword = password.getText().toString();

        SharedPreferences userPreference = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
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

                                databaseReference.child("users/"+usersKey+"/user_name").setValue(mName);
                                databaseReference.child("users/"+usersKey+"/user_password").setValue(mPassword);
                                databaseReference.child("users/"+usersKey+"/user_username").setValue(mUsername);
                            }

                            Toast.makeText(UserProfileActivity.this, "User details updated successfully.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
}
