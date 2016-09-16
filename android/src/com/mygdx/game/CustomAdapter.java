package com.mygdx.game;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bharath17 on 16/9/16.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {


    int lastPosition = -1;
    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView college;
        TextView score;
        // You need to retrieve the container (ie the root ViewGroup from your custom_item_layout)
        // It's the view that will be animated
        RelativeLayout container;

        public ViewHolder(View itemView)
        {
            super(itemView);

            container = (RelativeLayout) itemView.findViewById(R.id.relative_layout);

            college = (TextView) itemView.findViewById(R.id.college_name);
            score = (TextView)itemView.findViewById(R.id.score);
        }
    }

    private List<Score_class> itemList;
    private Context context;

    public CustomAdapter(Context context, List<Score_class> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.score_file, null);
        CustomAdapter.ViewHolder rcv = new CustomAdapter.ViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.college.setText(itemList.get(position).getCollege());
        holder.score.setText(itemList.get(position).getScore());

        // Here you apply the animation when the view is bound
        setAnimation(holder.container, position);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
