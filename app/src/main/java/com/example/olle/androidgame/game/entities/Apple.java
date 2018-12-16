package com.example.olle.androidgame.game.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.olle.androidgame.R;


/**
 * Created by Olle on 2017-03-20.
 */

public class Apple extends Entity implements Comparable<Entity>{

    private int x,y;
    private Bitmap bitmap;
    private Context context;
    private Rect box;
    private int appleSize = 128;

    public Apple(Context context, int startX, int startY){
        x = startX;
        y = startY;
        this.context = context;

        box = new Rect(x+25, y+25, x+appleSize-25, y+appleSize-25);

        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable._morot), appleSize, appleSize, true);

    }


    public int compareTo(Entity e){
        return this.y - e.getY();
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public Rect getBox(){
        return box;
    }

    @Override
    public int getSize() {
        return appleSize;
    }

}
