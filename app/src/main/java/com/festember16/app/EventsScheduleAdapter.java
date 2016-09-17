package com.festember16.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class EventsScheduleAdapter extends ArrayAdapter
{
    private Context context;
    List<String> eventNames , eventVenues, startTime, endTime;

    public EventsScheduleAdapter(Context context, List<String> eventNames, List<String> eventVenue, List<String> startTime , List<String> endTime)
    {
        super(context, R.layout.event_schedule_template , R.id.eventNameTextBlock , eventNames );
        this.context = context;
        this.eventNames = eventNames;
        this.eventVenues = eventVenue;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.event_schedule_template, parent, false);
        TextView name = (TextView) row.findViewById(R.id.eventNameTextBlock);
        TextView venue = (TextView) row.findViewById(R.id.eventVenueTextBlock);
        TextView start = (TextView) row.findViewById(R.id.startTimeTextBlock);
        TextView end = (TextView) row.findViewById(R.id.endTimeTextBlock);

        Log.wtf("wtf",  eventNames.size() + "size");
        name.setText( eventNames.get(position));
        venue.setText( eventVenues.get(position));
        start.setText( "Start Time: " + startTime.get(position));
        end.setText( "End Time: " + endTime.get(position));

        return row;
    }
}
