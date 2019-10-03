package com.example.cardviewgridviewsample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.cardviewgridviewsample.fragments.GiftlistFragment;
import com.example.cardviewgridviewsample.objects.Users;
import com.example.cardviewgridviewsample.tabs.AddPersonActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView txtName, txtUsername;
    ImageView imageView;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        txtName = (TextView) header.findViewById(R.id.user_fullname);
        txtUsername = (TextView) header.findViewById(R.id.user_username);
        imageView = (ImageView) header.findViewById(R.id.user_image);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_giftlist));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(DrawerActivity.this, AddPersonActivity.class);
                startActivity(intent);
            }
        });

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");
    }

    @Override
    protected void onStart() {
        super.onStart();

        //
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
                                Users users = dataSnapshot1.getValue(Users.class);
                                String name = users.getUser_name();
                                String username = users.getUser_username();

                                txtName.setText(name);
                                txtUsername.setText(username);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


    private void dispSelectedCashierScreen(int id){
        Fragment cashfragment = null;

        switch (id){
            case R.id.nav_giftlist:
                cashfragment = new GiftlistFragment();
                break;
            case R.id.nav_settings:
                Intent intent = new Intent(DrawerActivity.this, UserProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                logoutDialog();
                break;
        }

        if(cashfragment!=null){
            FragmentTransaction cashft = getSupportFragmentManager().beginTransaction();
            cashft.replace(R.id.flcontent, cashfragment);
            cashft.commit();
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("LEAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences userPreference = getApplicationContext().getSharedPreferences("UserPref", MODE_PRIVATE);
                SharedPreferences personNamePref = getSharedPreferences("personPref", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = userPreference.edit();
                SharedPreferences.Editor editor1 = personNamePref.edit();

                editor.clear().commit();
                editor1.clear().commit();

                Intent intent = new Intent(DrawerActivity.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(DrawerActivity.this, "You have been logged out.", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        dispSelectedCashierScreen(id);

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
