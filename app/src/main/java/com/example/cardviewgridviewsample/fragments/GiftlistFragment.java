package com.example.cardviewgridviewsample.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.adapters.PagerAdapter;
import com.example.cardviewgridviewsample.tabs.AddPersonActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class GiftlistFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private ImageView addImage;

    public GiftlistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_giftlist, container, false);

        //

//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        //initializing the tablayout
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        addImage = (ImageView) view.findViewById(R.id.imageViewAdd);

        addImage.setOnClickListener(this);
//
        //adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Giftlist"));
        tabLayout.addTab(tabLayout.newTab().setText("Stats"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
        //initializing the viewpager
        viewPager = (ViewPager) view.findViewById(R.id.pager);
//
        //creating our pager adapter
        PagerAdapter adapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount());

        //adding adapter to the pager
        viewPager.setAdapter(adapter);

        //adding onTabSelectedListener to swipe views
//        tabLayout.addOnTabSelectedListener((TabLayout.BaseOnTabSelectedListener) getContext());

//        //adding adapter to th/stener((TabLayout.BaseOnTabSelectedListener) getContext());

        return view;

    }


    //tabs
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.imageViewAdd:
                Intent intent = new Intent(getActivity(), AddPersonActivity.class);
                startActivity(intent);
                break;
        }
    }
}
