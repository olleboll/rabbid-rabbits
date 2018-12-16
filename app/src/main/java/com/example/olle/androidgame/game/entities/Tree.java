package com.example.olle.androidgame.game.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.olle.androidgame.R;

/**
 * Created by Olle on 2017-04-03.
 */

public class Tree extends Entity implements Comparable<Entity>{

    private int x, y, treeSize;
    private Bitmap bitmap;
    private Rect box;


    public Tree(Context context, int x, int y){

        this.x = x;
        this.y = y;
        treeSize = 256;
        box = new Rect(x+(treeSize/4), y+treeSize/2, x+((3*treeSize)/4), y+treeSize);

        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.trad), treeSize, treeSize, true);


    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public Rect getBox() {
        return box;
    }

    @Override
    public int compareTo(Entity e) {
        return this.y+this.treeSize - (e.getY()-e.getSize());
    }

    @Override
    public int getSize() {
        return treeSize;
    }
}
