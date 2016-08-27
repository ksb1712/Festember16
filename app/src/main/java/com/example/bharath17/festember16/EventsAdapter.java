package com.example.bharath17.festember16;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by vishnu on 3/8/16.
 */
public class EventsAdapter extends ArrayAdapter<Event>{

    Context context;
    List<Event> events;


    public EventsAdapter(Context context, int resource, List<Event> objects) {
        super(context, resource, objects);

        this.context = context;
        this.events = objects;

    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        View view = convertView;



        if(view!=null){
            holder = (ViewHolder) view.getTag();
        }

        else{

            view = inflater.inflate(
                    R.layout.event_list_item, null, false
            );

            holder = new ViewHolder(view);
            view.setTag(holder);

            holder.textView.setText(
                    (position+1) + ") " + parseEventName(events.get(position).getEventName()));
            holder.textView1.setText(
                    "Starts at: " + events.get(position).getEventStartTime()

                            + " on " +

                            parseEventDate(events.get(position).getEventDate())
            );

            holder.textView2.setText(events.get(position).getEventVenue());

        }



        return view;

    }

    public static String parseEventDate(String eventDate){

        String [] strings = eventDate.split("-");

        if(strings.length==3){
            StringBuilder builder = new StringBuilder();

            builder.append(strings[2] + "/ " + strings[1] + "/ " + strings[0]);

            return builder.toString();
        }

        return eventDate;
    }

    public static String parseEventName(String eventName){

        StringBuilder builder = new StringBuilder();

        String [] strings = eventName.split("_");



        for(String string: strings)
        {
            builder.append(string.toUpperCase() + " ");
        }

        return builder.toString();
    }

    static class ViewHolder{
        @InjectView(R.id.eventName)
        TextView textView;
        @InjectView(R.id.startTime)
        TextView textView1;
        @InjectView(R.id.venue)
        TextView textView2;

        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }
}
