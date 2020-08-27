package com.example.cardviewgridviewsample.tabs;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.objects.Gift;
import com.example.cardviewgridviewsample.objects.Person;
import com.example.cardviewgridviewsample.objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabStatFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    TextView txtTotalBudget, txtGiftQty, txtTotalSpent, txtTotalLeftToBuy, txtTotalLeftToWrap,
             txtDays, txtMinutes, txtHours, txtSeconds;
    double personBudget;
    String totalBudgetStr, personBudgetStr;
    CountDownTimer countDownTimer;

    //
    long startTime = 0;
    long milliseconds = 0;
    long diff = 0;


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
        txtTotalSpent = (TextView) view.findViewById(R.id.total_spent_yooo);
        txtTotalLeftToBuy = (TextView)view.findViewById(R.id.left_to_buy);
        txtTotalLeftToWrap = (TextView)view.findViewById(R.id.left_to_wrap);
        txtDays = (TextView) view.findViewById(R.id.countdown_days);
        txtHours = (TextView) view.findViewById(R.id.countdown_hours);
        txtMinutes = (TextView) view.findViewById(R.id.countdown_minutes);
        txtSeconds = (TextView) view.findViewById(R.id.countdown_seconds);


        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        displayDetails();

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy, HH:mm:ss");
        format.setLenient(false);

        String endTime = "25.12.2020, 00:00:00";

        Date endDate;
        try {
            endDate = format.parse(endTime);
            milliseconds = endDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        diff = milliseconds - startTime;

        countDownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                startTime=startTime-1;
                Long serverUptimeSeconds =
                        (millisUntilFinished - startTime) / 1000;

                String daysLeft = String.format("%d", serverUptimeSeconds / 86400);
                String hoursLeft = String.format("%d", (serverUptimeSeconds % 86400) / 3600);
                String minutesLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) / 60);
                String secondsLeft = String.format("%d", ((serverUptimeSeconds % 86400) % 3600) % 60);

//                txtChristmasTimer.setText(daysLeft+":"+hoursLeft+":"+minutesLeft+":"+secondsLeft);
                txtDays.setText(daysLeft);
                txtHours.setText(hoursLeft);
                txtMinutes.setText(minutesLeft);
                txtSeconds.setText(secondsLeft);
            }

            @Override
            public void onFinish() {

            }
        }.start();


//        ----------------

//        Calendar start_timer = Calendar.getInstance();
//        Calendar end_timer = Calendar.getInstance();
//
//        long start_millis = start_timer.getTimeInMillis();
//        long end_millis = end_timer.getTimeInMillis();
//        long total_millis = (end_millis - start_millis);
//
//        start_timer.set(2019, 10, 4);
//        end_timer.set(2019, 12, 25);
//
//        new CountDownTimer(total_millis, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//
//                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
//                millisUntilFinished -= TimeUnit.DAYS.toMillis(days);
//
//                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished);
//                millisUntilFinished -= TimeUnit.HOURS.toMillis(hours);
//
//                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
//                millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes);
//
//                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
//
//                txtChristmasTimer.setText(days + ":" + hours + ":" + minutes + ":" + seconds);
//            }
//
//            public void onFinish() {
//                txtChristmasTimer.setText("done!");
//            }
//        }.start();

    }


    private void displayDetails() {

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

                                        //
                                        databaseReference.child("users/"+userKey+"/personlist/"
                                                +personListKey+"/person_gift").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    int totalBought = 0;
                                                    int partialBought = 0;
                                                    double totalSpent = 0;
                                                    int checkCounter = 0;
                                                    int uncheckCounter = 0;
                                                    int wrapCounter = 0;
                                                    int unWrapCounter = 0;
                                                    for (DataSnapshot dataSnapshot11: dataSnapshot.getChildren()){
                                                        String personGiftKey = dataSnapshot11.getKey();
                                                        Gift gift = dataSnapshot11.getValue(Gift.class);
                                                        String boughtStatus = gift.getGift_bought_status();
                                                        String wrapStatus = gift.getGift_wrap_status();
                                                        double giftPrice = gift.getGift_price();

                                                        //
                                                        if (boughtStatus.equals("checked")){
                                                            checkCounter+=1;
                                                            totalSpent+=giftPrice;
                                                        }else {
                                                            uncheckCounter+=1;
                                                            totalSpent+=0;
                                                        }
                                                        //////////////

                                                        if (wrapStatus.equals("checked")){
                                                            wrapCounter+=1;
                                                        }else {
                                                            unWrapCounter+=1;
                                                        }
                                                    }
                                                    txtTotalLeftToBuy.setText(String.valueOf(uncheckCounter));
                                                    txtTotalLeftToWrap.setText(String.valueOf(unWrapCounter));
                                                    txtTotalSpent.setText(""+totalSpent);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

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
