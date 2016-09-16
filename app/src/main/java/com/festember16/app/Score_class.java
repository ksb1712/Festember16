package com.festember16.app;

/**
 * Created by bharath17 on 16/9/16.
 */
public class Score_class {

    private String college,score;

    public Score_class( String college, String score) {

        this.college = college;
        this.score = score;
    }


    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
