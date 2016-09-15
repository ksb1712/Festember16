package com.festember.festember16;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

/**
 * Created by Ravikiran on 8/10/2016.
 */
public class Rocks {


    private Sprite rock;
    private float size;
    private float posX,posY;
    private float velocity;
    private int appearances;
    private int max_appearance;

    public Rocks() {

        appearances=0;

        Random random=new Random();
        size=(random.nextInt(10)+5)*10;

        Random random2=new Random();
        velocity=-((random2.nextInt(10)+4));

        posX= Gdx.graphics.getWidth();

        Random random3=new Random();
        posY=random3.nextInt(Gdx.graphics.getHeight()-20);

        Random random4=new Random();
        max_appearance=random4.nextInt(3)+1;

    }

    public int getAppearances() {
        return appearances;
    }

    public void setAppearances() {
        this.appearances++;
    }

    public int getMax_appearance() {
        return max_appearance;
    }

    public void setMax_appearance(int max_appearance) {
        this.max_appearance = max_appearance;
    }

    public float getSize() {
        return size;
    }


    public float getVelocity() {
        return velocity;
    }


    public float getPosY() {
        return posY;
    }


    public float getPosX() {
        return posX;
    }


    public Sprite getRock() {
        return rock;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public void setRock(Sprite rock) {
        this.rock = rock;
    }


}
