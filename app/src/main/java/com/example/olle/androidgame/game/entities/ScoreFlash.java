package com.example.olle.androidgame.game.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.olle.androidgame.R;

/**
 * Created by Sara on 2017-11-28.
 */

public class ScoreFlash {

    //private Bitmap bitmap;

    private Context context;
    private int x, y, size, speed, duration;

    private boolean visible = false;

    private String text;

    public ScoreFlash(Context context, int startX, int startY){
        x = startX;
        y = startY;
        this.context = context;
        //bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.blomma), appleSize, appleSize, true);
    }

    public void setText(String text){
        this.text = text;
    }

    public float getSize() {
        return size;
    }

    public String[] getText() {
        String s[] = new String[2];
        if (text.length() > 15) {
            s[0] = text.substring(0,7);
            s[1] = text.substring(7);
            return s;
        }
        s[0] = text;
        s[1] = "";
        return s;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getString() {
        return "hej";
    }

    public void init(int x, int y, String text, int speed, int duration){

        visible = true;
        this.x = x;
        this.y = y;
        this.text = text;
        this.size = 30;
        this.speed = speed;
        this.duration = duration;
    }

    public void update(){
        if (size > duration & duration > 0) {
            visible = false;
        }
        if (size < 100) {
            size+=speed;
        }
    }

    public boolean visible() {
        return visible;
    }

    public void remove(){
        visible = false;
    }
}
