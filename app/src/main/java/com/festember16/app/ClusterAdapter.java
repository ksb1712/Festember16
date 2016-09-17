package com.festember16.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santa on 17/9/16.
 */

public class ClusterAdapter extends ArrayAdapter
{
    private Context context;
    List<String> clusterList = new ArrayList<>();

    public ClusterAdapter(Context context, List<String> cList)
    {
        super(context, R.layout.clusters_layout_template , R.id.clusterText , cList);
        this.context = context;
        clusterList = cList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.clusters_layout_template, parent, false);
        TextView clusterText = (TextView) row.findViewById(R.id.clusterText);
        clusterText.setText(clusterList.get(position));
        return row;
    }
}
