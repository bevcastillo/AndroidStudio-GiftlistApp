package com.example.cardviewgridviewsample;

import android.content.Intent;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout createAccountLayout;
    Button btnCreateAcct;
    TextInputEditText editTextName, editTextUsername, editTextPassword;
    TextInputLayout nameLayout, usernameLayout, passwordLayout;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        //
        createAccountLayout = (LinearLayout) findViewById(R.id.regLoginLayout);
        editTextName = (TextInputEditText) findViewById(R.id.regName);
        editTextUsername = (TextInputEditText) findViewById(R.id.regUsername);
        editTextPassword = (TextInputEditText) findViewById(R.id.regPassword);
        btnCreateAcct = (Button) findViewById(R.id.regCreateBtn);
        nameLayout = (TextInputLayout) findViewById(R.id.regNameLayout);
        usernameLayout = (TextInputLayout) findViewById(R.id.regUsernameLayout);
        passwordLayout = (TextInputLayout) findViewById(R.id.regPasswordLayout);

        createAccountLayout.setOnClickListener(this);
        btnCreateAcct.setOnClickListener(this);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");
        UserId = databaseReference.push().getKey();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.regLoginLayout:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.regCreateBtn:
                if (validateFields()){
                    if (checkDuplicates()){
                        insertUsers();
                    }
                }
                break;
        }
    }

    private void createUser(String name, String username, String password) {

        Users user = new Users(name, username, password);
        databaseReference.child("users").child(UserId).setValue(user);
    }

    private void insertUsers(){
        String name = editTextName.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        createUser(name,username,password);
        Intent intent = new Intent(RegisterActivity.this, DrawerActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Successfully registered.", Toast.LENGTH_SHORT).show();

    }

    private boolean validateFields(){
        String name = editTextName.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        boolean ok = true;

        if (name.isEmpty()){
            nameLayout.setError("Fields cannot be empty.");
            ok = false;
        }

        if (username.isEmpty()){
            usernameLayout.setError("Fields cannot be empty.");
            ok = false;
        }

        if (password.isEmpty()){
            passwordLayout.setError("Fields cannot be empty.");
            ok = false;
        }

        return ok;
    }

    private boolean checkDuplicates(){
        String username = editTextUsername.getText().toString();
        final boolean[] ok = {true};

        databaseReference.child("users").orderByChild("user_name").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    usernameLayout.setError("Username already exists.");
                    ok[0] = false;
                }else {
                    ok[0] = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return ok[0];
    }
}

