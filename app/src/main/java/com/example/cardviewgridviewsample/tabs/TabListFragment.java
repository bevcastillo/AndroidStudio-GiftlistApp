package com.example.cardviewgridviewsample.tabs;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.adapters.PersonlistAdapter;
import com.example.cardviewgridviewsample.objects.Person;
import com.example.cardviewgridviewsample.objects.Personlistdata;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabListFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    List<Personlistdata> list;
    LinearLayout noItemLayout;
    ProgressBar progressBar;


    public TabListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_list, container, false);

        //
        recyclerView = (RecyclerView) view.findViewById(R.id.personlist_recyclerview);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        noItemLayout = (LinearLayout) view.findViewById(R.id.layout_noitem_found);


        //
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("wishes_data");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        displayAllPersonlist();
    }

    private void displayAllPersonlist() {
        SharedPreferences userPreference = getActivity().getSharedPreferences("UserPref", MODE_PRIVATE);
        final String username = (userPreference.getString("uname", ""));

        databaseReference.child("users").orderByChild("user_username").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot2: dataSnapshot.getChildren()){
                        String usersKey = dataSnapshot2.getKey();

                        databaseReference.child("users/"+usersKey+"/personlist").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list = new ArrayList<>();
                                for (DataSnapshot dataSnapshot21: dataSnapshot.getChildren()){
                                    Person person = dataSnapshot21.getValue(Person.class);
                                    Personlistdata listdata = new Personlistdata();
                                    String person_name = person.getPerson_name();
                                    String person_image = person.getPerson_image();
                                    double person_budget = person.getPerson_budget();
                                    listdata.setPerson_name(person_name);
                                    listdata.setPerson_budget(person_budget);
                                    listdata.setPerson_image(person_image);
                                    list.add(listdata);
                                }
                                PersonlistAdapter adapter = new PersonlistAdapter(getContext(), list);
                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(gridLayoutManager);
                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);

                                if (list.isEmpty()){
                                    noItemLayout.setVisibility(View.VISIBLE);
                                }else {
                                    noItemLayout.setVisibility(View.GONE);
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
