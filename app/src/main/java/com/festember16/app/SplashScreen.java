package com.festember16.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int numberOfClouds = 5;

        Animation downAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.panda_fall);
        Animation upLongAnimation = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.object_rise);
        final Animation logoEntrance = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.logo_entrance);

        ImageView polo = (ImageView) findViewById(R.id.polo);
        final ImageView plane = (ImageView) findViewById(R.id.fPlaneImageView);
        ImageView clouds[] = new ImageView[numberOfClouds];
        for (int i = 0; i < numberOfClouds; i++) {
            clouds[i] = (ImageView) findViewById(getResources().getIdentifier("cloud" + i, "id", getPackageName()));
        }
        final ImageView festemberLogo = (ImageView) findViewById(R.id.festember_logo);

        polo.startAnimation(downAnimation);
        downAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                festemberLogo.setVisibility(View.VISIBLE);
                festemberLogo.setAnimation(logoEntrance);
                logoEntrance.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        for (int i = 0; i < numberOfClouds; i++) clouds[i].startAnimation(upLongAnimation);
        plane.startAnimation(upLongAnimation);

    }

}
