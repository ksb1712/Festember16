package com.festember.festember16;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

/**
 * Created by Ravikiran on 8/27/2016.
 */
public class Bonus {

    Sprite bonusSprite;

    private int bonusNumber;

    private boolean speedBoost;
    private boolean fullHealth;
    private boolean noDamage;
    private boolean scoreBoost;

    private boolean isBonusSprite;
    private boolean isBonusCollected;

    int frames;

    private float size;
    private float posX,posY;
    private float velocity;

    public Bonus() {

        isBonusSprite=false;

        bonusNumber=0;
        frames=500;

        speedBoost=false;
        scoreBoost=false;
        fullHealth=false;
        noDamage=true;


        size=100;

        Random random2=new Random();
        velocity=-((random2.nextInt(8)+2));

        posX= Gdx.graphics.getWidth();

        Random random3=new Random();
        posY=random3.nextInt(Gdx.graphics.getHeight()-10)+50;
    }

    public void resetValues(){
        isBonusSprite=false;

        speedBoost=false;
        scoreBoost=false;
        fullHealth=false;
        noDamage=false;


        if(bonusNumber==3){
            bonusNumber=0;
        }
        else{
            bonusNumber++;
        }
        setBonus();

        Random random2=new Random();
        velocity=-((random2.nextInt(8)+2));

        posX= Gdx.graphics.getWidth();

        Random random3=new Random();
        posY=random3.nextInt(Gdx.graphics.getHeight()-100)+50;


    }

    private void setBonus(){
        switch(bonusNumber){
            case 0:
                noDamage=true;
                break;
            case 1:
                scoreBoost=true;
                break;
            case 2:
                fullHealth=true;
                break;
            case 3:
                speedBoost=true;
                break;
            default:
                noDamage=true;
                break;
        }
    }


    public Sprite getBonusSprite() {
        return bonusSprite;
    }

    public boolean isSpeedBoost() {
        return speedBoost;
    }

    public boolean isFullHealth() {
        return fullHealth;
    }

    public boolean isNoDamage() {
        return noDamage;
    }


    public boolean isScoreBoost() {
        return scoreBoost;
    }

    public boolean isBonusSprite() {
        return isBonusSprite;
    }

    public int getFrames() {
        return frames;
    }

    public float getSize() {
        return size;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getVelocity() {
        return velocity;
    }

    public boolean isBonusCollected() {
        return isBonusCollected;
    }






    public void setIsBonusSprite(boolean isBonusSprite) {
        this.isBonusSprite = isBonusSprite;
    }

    public void setBonusSprite(Sprite bonusSprite) {
        this.bonusSprite = bonusSprite;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setIsBonusCollected(boolean isBonusCollected) {
        this.isBonusCollected = isBonusCollected;
    }
}
