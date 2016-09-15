package com.festember16.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.InjectView;

/**
 * Created by vishnu on 8/9/16.
 */
public class RulesFragment extends Fragment {

    public static final String ID = "ID";

    Events events;

    @InjectView(R.id.rules)
    TextView rules;

    public static RulesFragment getInstance(int id){
        RulesFragment fragment = new RulesFragment();
        Bundle args = new Bundle();
        args.putInt(ID, id);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rules_layout, container, false);

        Bundle bundle = getArguments();

        if(bundle!=null) {
            int id = bundle.getInt(ID);

            //Todo: get event rules from db.

            events = DetailsActivity.detailedEvent;
            //Just in case
            if(events == null){
               // events = DetailsActivity.db.getEvent(id);
            }

            //Todo: set rules from events

        }


        return view;
    }
}
