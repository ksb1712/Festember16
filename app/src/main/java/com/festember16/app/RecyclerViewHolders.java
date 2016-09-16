package com.festember16.app;

/**
 * Created by bharath17 on 14/9/16.
 * Holder for recycleview adpater
 */
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name;


    public RecyclerViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        name = (TextView)itemView.findViewById(R.id.name);

    }

    @Override
    public void onClick(View view) {
        Intent i;
        Toast.makeText(view.getContext(),name.getText().toString(),Toast.LENGTH_SHORT).show();
        switch (getPosition())
        {
            case 0:
                i = new Intent(view.getContext(),Events_list.class);
                String s[] = {"event1","event2"};
                i.putExtra("events",s);
                view.getContext().startActivity(i);

                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}