package com.festember16.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by bharath17 on 16/9/16.
 */
public class RecyclerViewAdapter2 extends RecyclerView.Adapter<RecyclerViewHolder2> {

    private List<ItemObject> itemList;
    private Context context;

    public RecyclerViewAdapter2(Context context, List<ItemObject> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, null);
        RecyclerViewHolder2 rcv = new RecyclerViewHolder2(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder2 holder, int position) {
        holder.name.setText(itemList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}