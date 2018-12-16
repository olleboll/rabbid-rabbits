package com.example.olle.androidgame.game.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.olle.androidgame.R;
import com.example.olle.androidgame.game.entities.Entity;
import com.example.olle.androidgame.game.scene.Level;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Created by Olle on 2017-03-16.
 */

public class Skurk extends Entity implements Comparable<Entity> {

    //private Bitmap bitmap;
    private int skurkSize = 128;

    private Rect box;

    private int x;
    private int y;
    private int direction;
    private Bitmap[] moveForward = new Bitmap[2];
    private Bitmap[] moveLeft = new Bitmap[2];
    private Bitmap[] moveRight = new Bitmap[2];
    private Bitmap[] moveBack = new Bitmap[2];

    private Bitmap[] currentAnimation;

    private int maxY;
    private int minY;
    private int maxX;
    private int minX;

    private Level level;
    private ArrayList<Tree> trees;

    private double speed = 5;

    private boolean isMoving = false;

    public Skurk(Context context, int startX, int startY, int screenX, int screenY, int startDir, double speed, Level level){
        x = startX;
        y = startY;
        this.speed = speed;
        this.level = level;
        this.trees = level.getTrees();

        initBitmaps(context);
        initAnimations();
        bitmap = front_still;

        box = new Rect(x+(skurkSize/4), y+skurkSize/2, x+((3*skurkSize)/4), y+skurkSize);

        minX = 0;
        minY = 0;
        maxX = screenX - bitmap.getWidth();
        maxY = screenY;

        currentAnimation = moveForward;
        direction = startDir;
    }

    private void setAnimation(int dir){

        if(dir == 1){
            currentAnimation = moveForward;
        }
        if(dir == 2){
            currentAnimation = moveBack;
        }
        if(dir == 3){
            currentAnimation = moveRight;
        }
        if(dir == 4){
            currentAnimation = moveLeft;
        }
    }

    private int animationCounter = 0;
    private int runSpeed = 5;
    private void animate(){
        animationCounter++;
        if(animationCounter >= runSpeed){
            bitmap = currentAnimation[1];
        }else {
            bitmap = currentAnimation[0];
        }
        if(animationCounter >= runSpeed * 2){
            animationCounter = 0;
        }
    }

    public void update(){
        boolean collision = false;
        if(checkCollission()){
            collision = true;
            speed = speed *2;
            if (direction == 1) {
                direction = 2;
            } else if (direction == 2) {
                direction = 1;
            } else if (direction == 3) {
                direction = 4;
            } else {
                direction = 3;
            }
        }

        if(direction == 1){
            //forward
            y += speed;
        }else if(direction == 2){
            //back
            y -= speed;
        }else if(direction == 3){
            //right
            x += speed;
        }else {
            //left
            x -= speed;
        }
        if (collision){
            speed = speed / 2;
        }
        box.offsetTo(x+(skurkSize/4),y+skurkSize/2);

        animate();
        updateDirection();
    }

    private boolean checkCollission() {
        for (Tree tree : trees){
            if(Rect.intersects(this.getBox(), tree.getBox())){
                return true;
            }
        }
        return false;
    }

    private void updateDirection(){

        if(x >= maxX){
            direction = 4;
        }
        if(x <= minX){
            direction = 3;
        }
        if(y >= maxY){
            direction = 2;
        }
        if(y <= minY){
            direction = 1;
        }
        setAnimation(direction);
    }

    public Rect getBox(){
        return box;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    @Override
    public int getSize() {
        return skurkSize;
    }

    @Override
    public int compareTo(Entity e) {
        return this.y+this.skurkSize - (e.getY()-e.getSize());
    }

    public double getSpeed(){
        return speed;
    }

    private void initAnimations(){
        moveForward[0] = front_move_1;
        moveForward[1] = front_still;
        moveBack[0] = back_move_1;
        moveBack[1] = back_still;
        moveLeft[0] = left_move_1;
        moveLeft[1] = left_still;
        moveRight[0] = right_move_1;
        moveRight[1] = right_still;
    }

    private void initBitmaps(Context context){
        front_still = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.skurk_spel_fram), skurkSize, skurkSize, true);
        front_move_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.skurk_spel_fram_1),skurkSize, skurkSize, true);
        left_still = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.skurk_spel_vanster),skurkSize, skurkSize, true);
        left_move_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.skurk_spel_vanster_1),skurkSize, skurkSize, true);
        right_still = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.skurk_spel_hoger),skurkSize, skurkSize, true);
        right_move_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.skurk_spel_hoger_1),skurkSize, skurkSize, true);
        back_still = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.skurk_spel_bakifran),skurkSize, skurkSize, true);
        back_move_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.skurk_spel_bakifran_1),skurkSize, skurkSize, true);
    }

    Bitmap front_still;
    Bitmap front_move_1;
    Bitmap left_still ;
    Bitmap left_move_1;
    Bitmap right_still;
    Bitmap right_move_1;
    Bitmap back_still ;
    Bitmap back_move_1;

}
