package com.example.cardviewgridviewsample.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cardviewgridviewsample.EditPersonActivity;
import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.ViewPersonActivity;
import com.example.cardviewgridviewsample.objects.Person;
import com.example.cardviewgridviewsample.objects.Personlistdata;
import com.example.cardviewgridviewsample.objects.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonlistAdapter extends RecyclerView.Adapter<PersonlistAdapter.ViewHolder> {

    Context context;
    List<Personlistdata> list;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public PersonlistAdapter(Context context, List<Personlistdata> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_personlist_card, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");

        SharedPreferences userPreference = context.getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        final String username = (userPreference.getString("uname",""));

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pName = list.get(viewHolder.getAdapterPosition()).getPerson_name();
                double personBudget = list.get(viewHolder.getAdapterPosition()).getPerson_budget();

                Intent intent = new Intent(v.getContext(), ViewPersonActivity.class); //calling an intent
                intent.putExtra("selected_person", pName);
//                Toast.makeText(context, pName+" is the selected name", Toast.LENGTH_SHORT).show();
                v.getContext().startActivity(intent);
            }
        });

        viewHolder.layout.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

//            String personName = list.get(viewHolder.getAdapterPosition()).getPerson_name();
            @Override
            public void onCreateContextMenu(ContextMenu menu, final View v, ContextMenu.ContextMenuInfo menuInfo) {

                menu.add("Update").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final String personName = list.get(viewHolder.getAdapterPosition()).getPerson_name();

                        Intent intent = new Intent(v.getContext(), EditPersonActivity.class);
                        intent.putExtra("mPerson_name", personName);
                        v.getContext().startActivity(intent);

                        return true;
                    }
                });

                menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final String personName = list.get(viewHolder.getAdapterPosition()).getPerson_name();

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
                                                final int totalQuantity = users.getUser_gift_qty();
                                                final double totalBudget = users.getUser_budget();

                                                databaseReference.child("users/"+usersKey+"/personlist")
                                                        .orderByChild("person_name")
                                                        .equalTo(personName)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()){
                                                                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                                                                        String personlistKey = dataSnapshot2.getKey();
                                                                        Person person = dataSnapshot2.getValue(Person.class);
                                                                        int quantity = person.getPerson_gift_qty();
                                                                        double budget = person.getPerson_budget();

                                                                        String userBudgetStr = String.format("%.2f", totalBudget - budget);
                                                                        double userBudget = Double.parseDouble(userBudgetStr);

                                                                        int userTotalQuantity = totalQuantity - quantity;


                                                                        databaseReference.child("users/"+usersKey+"/user_budget").setValue(userBudget);
                                                                        databaseReference.child("users/"+usersKey+"/person_gift_qty").setValue(userTotalQuantity);

                                                                        databaseReference.child("users/"+usersKey+"/personlist/"+personlistKey).setValue(null);
                                                                    }
                                                                    Snackbar.make(view, "Person has been deleted successfully", Snackbar.LENGTH_LONG)
                                                                            .setAction("Action",null).show();
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

                        return true;
                    }
                });
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Personlistdata data = list.get(i);
        viewHolder.personName.setText(data.getPerson_name());
        viewHolder.personBudget.setText(String.valueOf(data.getPerson_budget()));
        viewHolder.personGiftCount.setText(String.valueOf(data.getPerson_gift_qty()));
        Picasso.with(context).load(data.getPerson_image()).into(viewHolder.personImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView personName, personBudget, personGiftCount;
        ImageView personImage;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            personName = (TextView) itemView.findViewById(R.id.card_person_name);
            personImage = (ImageView) itemView.findViewById(R.id.card_personImage);
            personBudget = (TextView) itemView.findViewById(R.id.card_person_budget);
            layout = (RelativeLayout) itemView.findViewById(R.id.card_layout);
            personGiftCount = (TextView) itemView.findViewById(R.id.card_person_gift_count);
        }
    }
}
