package com.festember16.app;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecyclerViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name;


    public RecyclerViewHolder2(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        name = (TextView)itemView.findViewById(R.id.name);

    }

    @Override
    public void onClick(View view) {
       Intent intent = new Intent(view.getContext(),DetailsActivity.class);
        int id = 0;
       String s = name.getText().toString();
        Log.e("name ",s);
        for(int i = 0; i < Utilities.map_events.length;i++)
        {
            if(Utilities.map_events[i].equals(s)){
                id = Integer.parseInt(Utilities.map_events[i+1]);
                break;
            }
        }
        intent.putExtra("event_id",id);
        view.getContext().startActivity(intent);
    }
}