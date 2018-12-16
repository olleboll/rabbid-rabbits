package com.example.olle.androidgame.utils;

/**
 * Created by Sara on 2017-11-28.
 */

public class SkurkSpawn {

    int x, y, dir;

    public SkurkSpawn(int x, int y, int dir){
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDir() {
        return dir;
    }
}
