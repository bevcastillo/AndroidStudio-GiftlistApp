package com.example.cardviewgridviewsample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.cardviewgridviewsample.objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout createAccountLayout;
    Button loginBtn;
    TextInputEditText editTextUsername, editTextPassword;
    TextInputLayout usernameLayout, passwordLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAccountLayout = (LinearLayout) findViewById(R.id.createAcctLayout);
        editTextUsername = (TextInputEditText) findViewById(R.id.loginUsername);
        editTextPassword = (TextInputEditText) findViewById(R.id.loginPassw);
        usernameLayout = (TextInputLayout) findViewById(R.id.loginUsernameLayout);
        passwordLayout= (TextInputLayout) findViewById(R.id.loginPasswLayout);
        loginBtn = (Button) findViewById(R.id.btnLogin);

        loginBtn.setOnClickListener(this);
        createAccountLayout.setOnClickListener(this);

//        getSupportActionBar().hide();

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbreference = firebaseDatabase.getReference("wishes_data");

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.createAcctLayout:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btnLogin:
                loginUser();
                break;
        }
    }

    public void loginUser(){
        SharedPreferences userPreference = getApplicationContext().getSharedPreferences("UserPref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = userPreference.edit();

        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        dbreference.child("users").orderByChild("user_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    editor.putString("uname", username);
                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                        Users user = dataSnapshot2.getValue(Users.class);
                        String mPassw = user.getUser_password();

                        if (mPassw.equals(password)){
                            Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
                            startActivity(intent);
                            editor.commit();
                            Toast.makeText(LoginActivity.this, "Successfully logged in.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
