package com.example.cardviewgridviewsample.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.objects.Giftlistdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ViewGiftsAdapter extends RecyclerView.Adapter<ViewGiftsAdapter.ViewHolder> {
    List<Giftlistdata> list;
    Context context;

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    //
    String wrapped, bought;

    CheckBox chkWrapped, chkBought;

    public ViewGiftsAdapter(List<Giftlistdata> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_card_person_giftlist, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");

        viewHolder.wrappedChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final String name = list.get(viewHolder.getAdapterPosition()).getGift_name();

                if (isChecked){
                    wrapped = "checked";
                }else {
                    wrapped = "unchecked";
//                            viewHolder.wrappedChecked.setChecked(false);
                }

                SharedPreferences userPreference = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE);
                final String username = (userPreference.getString("uname",""));

                SharedPreferences personNamePref = context.getSharedPreferences("PersonPref", Context.MODE_PRIVATE);
                final String personName = (personNamePref.getString("shared_person_name",""));


                databaseReference.child("users").orderByChild("user_username")
                        .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                        final String personlistKey = dataSnapshot2.getKey();

                                                        databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey+"/person_gift")
                                                                .orderByChild("gift_name")
                                                                .equalTo(name)
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()){
                                                                            for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                                String persongiftKey = dataSnapshot3.getKey();

                                                                                databaseReference.child("users/"+usersKey
                                                                                        +"/personlist/"+personlistKey
                                                                                        +"/person_gift/"+persongiftKey)
                                                                                        .child("/gift_wrap_status").setValue(wrapped);
                                                                            }
                                                                            Snackbar.make(view, "Selection is saved", Snackbar.LENGTH_LONG)
                                                                                    .setAction("Action", null).show();
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
        });

        viewHolder.boughtChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final String name = list.get(viewHolder.getAdapterPosition()).getGift_name();
                if (isChecked){
                    bought = "checked";
                    viewHolder.boughtChecked.setChecked(true);
                }else {
                    bought = "unchecked";
                    viewHolder.boughtChecked.setChecked(false);
                }


                SharedPreferences userPreference = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE);
                final String username = (userPreference.getString("uname",""));

                //
                SharedPreferences personNamePref = context.getSharedPreferences("personPref", Context.MODE_PRIVATE);
                final String personName = (personNamePref.getString("person_name",""));

                databaseReference.child("users").orderByChild("user_username")
                        .equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                                        final String personlistKey = dataSnapshot2.getKey();

                                                        databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey+"/person_gift")
                                                                .orderByChild("gift_name")
                                                                .equalTo(name)
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists()){
                                                                            for (DataSnapshot dataSnapshot3: dataSnapshot.getChildren()){
                                                                                String persongiftKey = dataSnapshot3.getKey();

                                                                                databaseReference.child("users/"+usersKey
                                                                                        +"/personlist/"+personlistKey
                                                                                        +"/person_gift/"+persongiftKey)
                                                                                        .child("/gift_wrap_status").setValue(wrapped);
                                                                            }
                                                                            Snackbar.make(view,"Selection is saved", Snackbar.LENGTH_LONG)
                                                                                    .setAction("Action", null).show();
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
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Giftlistdata data = list.get(i);

        viewHolder.gift_name.setText(data.getGift_name());
        viewHolder.gift_price.setText("₱ "+data.getGift_price());

        String status = data.getGift_bought_status();
        String wrapStat = data.getGift_wrap_status();

        viewHolder.boughtChecked.setChecked(false);
        viewHolder.wrappedChecked.setChecked(false);

        if (data.getGift_bought_status().equals("checked")){
            viewHolder.boughtChecked.setChecked(true);

        }

        if (data.getGift_bought_status().equals("unchecked")){
            viewHolder.boughtChecked.setChecked(false);
        }

        if (data.getGift_wrap_status().equals("checked")){
            viewHolder.wrappedChecked.setChecked(true);
        }

        if (data.getGift_wrap_status().equals("unchecked")){
            viewHolder.wrappedChecked.setChecked(false);
        }


//        if (data.getGift_bought_status() == "checked"){
//            viewHolder.boughtChecked.setChecked(true);
//        }
//
//
//        if (data.getGift_bought_status() == "unchecked"){
//            viewHolder.boughtChecked.setChecked(false);
//        }
//
//        if (data.getGift_wrap_status() == "checked"){
//            viewHolder.wrappedChecked.setChecked(true);
//        }
//
//        if (data.getGift_wrap_status() == "unchecked"){
//            viewHolder.wrappedChecked.setChecked(false);
//        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView gift_name, gift_price;
        CheckBox wrappedChecked, boughtChecked;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gift_name = (TextView) itemView.findViewById(R.id.gift_name);
            gift_price = (TextView) itemView.findViewById(R.id.gift_price);
            wrappedChecked = (CheckBox) itemView.findViewById(R.id.packed_checkBox);
            boughtChecked = (CheckBox) itemView.findViewById(R.id.bought_checkBox);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }
}
