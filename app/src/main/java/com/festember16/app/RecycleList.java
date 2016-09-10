package com.festember16.app;


import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.support.v7.widget.RecyclerView;

public class RecycleList extends RecyclerView.Adapter<RecycleList.CustomViewHolder> {
    public final Context context;
    public final String[][] present;

    public final int[][] time;

    public final String[] Number;
    public final String[] cates;
    public final int co2;
    Calendar timenow=new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));


    int n=3,a,b,c,d;

    public RecycleList(Context context, String[][] present, int[][] time, int o,String[] Number,String[] cates,int co2) {

        this.context = context;
        this.present = present;
        this.time=time;
        this.Number=Number;
        this.cates=cates;
        this.co2=co2;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView Event,Time,Location,Cate,text;
        public RelativeLayout lay;
        public ImageView line;

        public CustomViewHolder(View view) {
            super(view);

            this.Event = (TextView) view.findViewById(R.id.Event);
            this.Time = (TextView) view.findViewById(R.id.Time);
            this.Location=(TextView) view.findViewById(R.id.Location);
            this.Cate = (TextView) view.findViewById(R.id.Cate);
            this.lay=(RelativeLayout)itemView.findViewById(R.id.singlelistlayout);
            this.line=(ImageView)itemView.findViewById(R.id.line);
            //this.text=(TextView)view.findViewById(R.id.textView);

        }
    }

    //function to remove underscores from the strings
    public String propergram(String word){
        // word.toLowerCase();
        String[] tempstr =word.split("_");
        String tempstr2;

        for(int i=0;i<tempstr.length;i++){
            if((tempstr[i].charAt(0))>='A'&&(tempstr[i].charAt(0))<='Z') {
                tempstr2 = (String.valueOf(tempstr[i].charAt(0)));
            }
            else {
                tempstr2 = (String.valueOf(tempstr[i].charAt(0))).toUpperCase();
            }
            tempstr[i] = tempstr2.concat(tempstr[i].substring(1));
            if (i != 0) {
                word=word.concat(" ").concat(tempstr[i]);
            } else {
                word = tempstr[i];
            }

        }
        return word;


    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_single, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        int position=i;
        String[] temp=present[position];

        Calendar time5 = new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));
        time5.set(timenow.get(Calendar.YEAR), timenow.get(Calendar.MONTH), time[position][0], time[position][1], time[position][2]);

        Calendar time6 = new GregorianCalendar(TimeZone.getTimeZone("GMT+5:30"));
        time6.set(timenow.get(Calendar.YEAR), timenow.get(Calendar.MONTH), time[position][3], time[position][4], time[position][5]);

        customViewHolder.Event.setText(propergram(temp[0]));
        customViewHolder.Location.setText(propergram(temp[1]));
       customViewHolder.Cate.setText(propergram(temp[2]));
        int colt[]={0Xffd41414,0Xffe32b2b,0Xffe36f2b,0Xffe3a62b,0Xffe3d72b,0Xffd1e32b,0Xffb2e32b,0Xff62e32b,0Xff2be353,0Xff2be3b8,0Xff2b68e3,0Xffa62be3,0Xffe32bbf,0Xffe32b65};
        int p=0;
        for(;p<co2;p++){
            if(temp[2].equals(cates[p])){
                break;
            }
        }
        customViewHolder.Cate.getBackground().setColorFilter(colt[p], PorterDuff.Mode.MULTIPLY );
        customViewHolder.line.getBackground().setColorFilter(colt[p], PorterDuff.Mode.MULTIPLY );

      /*  customViewHolder.Event.setText(temp[0]);
        customViewHolder.Location.setText(temp[1]);
        customViewHolder.Cate.setText(temp[2]);
        */
        //calculates time left for begining an event
        if (time5.after(timenow)) {

            a =((int)((time5.getTimeInMillis() - timenow.getTimeInMillis())/3600000));
            b =((int)((time5.getTimeInMillis() - timenow.getTimeInMillis())/60000))%60;
            if(a!=0)
                customViewHolder.Time.setText("begins in " + a + " hours " + b + " mins ");
            else
                customViewHolder.Time.setText("begins in "  + b + " mins ");
        }
        //calculates time left for end of an ongoing event
        else if (time6.before(timenow)) {

            customViewHolder.Time.setText("ends in 0 mins ");
        }
        else {

            c = ((int)((time6.getTimeInMillis() - timenow.getTimeInMillis())/3600000));
            d =((int)((time6.getTimeInMillis() - timenow.getTimeInMillis())/60000))%60;
            if(c!=0)
                customViewHolder.Time.setText("ends in " + c + " hours " + d + " mins ");
            else
                customViewHolder.Time.setText("ends in "  + d + " mins ");
        }
    }

    @Override
    public int getItemCount() {
        return (null != Number ? Number.length : 0);
    }

}