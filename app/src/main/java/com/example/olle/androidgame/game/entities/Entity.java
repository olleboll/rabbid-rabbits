package com.example.olle.androidgame.game.entities;

import android.graphics.Bitmap;
import android.graphics.Rect;

/**
 * Created by Olle on 2017-03-20.
 */

public abstract class Entity implements Comparable<Entity>{
    protected int x,y;
    protected Bitmap bitmap;
    protected Rect box;

    public abstract Bitmap getBitmap();

    public abstract int getX();

    public abstract int getY();

    public abstract Rect getBox();

    public abstract int getSize();


}
