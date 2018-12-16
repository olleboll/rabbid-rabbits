package com.example.olle.androidgame.game.entities;

import com.example.olle.androidgame.game.entities.Entity;

import java.util.Comparator;

/**
 * Created by Olle on 2017-04-11.
 */

public class Painter implements Comparator<Entity> {

    @Override
    public int compare(Entity o1, Entity o2) {
        return o1.getY()+o1.getSize() - (o2.getY()+o2.getSize());
    }
}
