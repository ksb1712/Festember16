package com.festember16.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ajay Nataraj on 9/16/2016.
 */
public class CustomList extends ArrayAdapter<String> {



    private final Notification context;
    private List<String> title = new ArrayList<>();
    private List<String> text = new ArrayList<>();
    private List<String> time = new ArrayList<>();


    public CustomList(Notification context, List<String> title, List<String> text, List<String> time) {
        super(context, R.layout.list_single_notification, R.id.title_box,title);
        this.context = context;
        this.title=title;
        this.time= time;
        this.text = text;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single_notification, parent, false);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.title_box);
        TextView txtText = (TextView) rowView.findViewById(R.id.text_box);
        TextView txtTime = (TextView) rowView.findViewById(R.id.time_box);

        txtTitle.setText(title.get(position));
        txtText.setText(text.get(position));
        txtTime.setText(time.get(position));


        return rowView;
    }
}