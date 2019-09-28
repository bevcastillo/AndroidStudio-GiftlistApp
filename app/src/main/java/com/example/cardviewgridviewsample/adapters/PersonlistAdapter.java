package com.example.cardviewgridviewsample.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.ViewPersonActivity;
import com.example.cardviewgridviewsample.objects.Personlistdata;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PersonlistAdapter extends RecyclerView.Adapter<PersonlistAdapter.ViewHolder> {

    Context context;
    List<Personlistdata> list;

    public PersonlistAdapter(Context context, List<Personlistdata> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_personlist_card, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personName = list.get(viewHolder.getAdapterPosition()).getPerson_name();
                double personBudget = list.get(viewHolder.getAdapterPosition()).getPerson_budget();

                Intent intent = new Intent(v.getContext(), ViewPersonActivity.class); //calling an intent
                v.getContext().startActivity(intent);

                //save the data to shared preference
                SharedPreferences personNamePref = v.getContext().getSharedPreferences("personPref",Context.MODE_PRIVATE);
                SharedPreferences.Editor personEditor = personNamePref.edit();

                personEditor.putString("person_name", personName);
                personEditor.commit();
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Personlistdata data = list.get(i);
        viewHolder.personName.setText("Name: "+data.getPerson_name());
        viewHolder.personBudget.setText("Budget: ₱"+data.getPerson_budget());
        Picasso.with(context).load(data.getPerson_image()).into(viewHolder.personImage);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView personName, personBudget;
        ImageView personImage;
        RelativeLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            personName = (TextView) itemView.findViewById(R.id.card_person_name);
            personImage = (ImageView) itemView.findViewById(R.id.card_personImage);
            personBudget = (TextView) itemView.findViewById(R.id.card_person_budget);
            layout = (RelativeLayout) itemView.findViewById(R.id.card_layout);
        }
    }
}
