package com.festember.festember16;

import android.app.Activity;
import android.content.Intent;


/**
 * Created by Ravikiran on 9/9/2016.
 */
public class NativeCode implements AndroidCode {

    private Activity context;
    Intent intent;
    String text;


    public NativeCode(Activity context) {
        this.context = context;

    }

    @Override
    public void shareScore(long score) {

        text="Hey friends! Ive got a score of "+score+" in Festember spacefarer game. Beat that?";

        intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,text);
        Intent temp=Intent.createChooser(intent,"Where do you want to brag?");
        context.startActivity(temp);
    }
}
