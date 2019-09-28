package com.example.cardviewgridviewsample.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cardviewgridviewsample.R;
import com.example.cardviewgridviewsample.objects.Giftlistdata;

import java.util.List;

public class ViewGiftsAdapter extends RecyclerView.Adapter<ViewGiftsAdapter.ViewHolder> {
    List<Giftlistdata> list;
    Context context;

    public ViewGiftsAdapter(List<Giftlistdata> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_card_person_giftlist, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Giftlistdata data = list.get(i);

        viewHolder.gift_name.setText(data.getGift_name());
        viewHolder.gift_price.setText("₱ "+data.getGift_price());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView gift_name, gift_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gift_name = (TextView) itemView.findViewById(R.id.gift_name);
            gift_price = (TextView) itemView.findViewById(R.id.gift_price);
        }
    }
}
