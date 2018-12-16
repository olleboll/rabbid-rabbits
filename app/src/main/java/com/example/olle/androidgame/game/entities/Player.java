package com.example.olle.androidgame.game.entities;

/**
 * Created by Olle on 2017-03-14.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.olle.androidgame.R;
import com.example.olle.androidgame.game.entities.Entity;

import java.util.ArrayList;
import java.util.Vector;

import static java.lang.Math.abs;

public class Player extends Entity implements Comparable<Entity>{

    //private Bitmap bitmap;
    private int playerSize = 128;

    private Rect box;
    private Rect safeSpace;

    private int safeWidth = playerSize*2;
    private int safeheight = playerSize*2;

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

    private int startPosX, startPosY;

    private double speedModifier = 2;

    private Vector<Point> pathPoints;
    private boolean isMoving = false;


    public Player(Context context, int screenX, int screenY){
        this.startPosX = screenX - playerSize/1;
        this.startPosY = screenY/2;
        x = startPosX;
        y = startPosY;

        box = new Rect(x+(playerSize/4), y+playerSize/3, x+((3*playerSize)/4), y+playerSize - 25);
        safeSpace = new Rect(box.left - safeWidth, box.top - safeheight , box.right + safeWidth, box.bottom + safeheight);


        pathPoints = new Vector<Point>();

        initBitmaps(context);
        initAnimations();
        currentAnimation = moveForward;

        bitmap = front_still;
        minX = -playerSize/4;
        minY = 0;
        maxX = screenX - playerSize * 3 / 4;
        maxY = screenY;
    }

    public void moveToStart(){
        x = startPosX;
        y = startPosY;
    }

    @Override
    public int compareTo(Entity e) {
        return this.y+this.playerSize - (e.getY()-e.getSize());
    }

    private boolean colliding = false;
    private int cleanX, cleanY;

    public int getCleanX(){
        return cleanX;
    }

    public int getCleanY(){
        return cleanY;
    }

    public void setColliding(boolean colliding){

        this.colliding = colliding;
        if(!colliding){
            cleanX=x;
            cleanY=y;
        }
    }

    public void ensurePlayerSafety(ArrayList<Tree> trees) {
        for(Tree t : trees) {
            if (Rect.intersects(this.getBox(), t.getBox())){
                if (this.getBox().centerX() > maxX/2) {
                    x -= playerSize;
                } else {
                    x += playerSize;
                }

                box.offsetTo(x + playerSize / 4, y + playerSize / 2);
                offsetSafeSpace(x, y);
                offsetSafeSpace(x, y);
            }
        }
    }

    public void setMoving(boolean moving){
        isMoving = moving;
        if(!moving){
            pathPoints.clear();
        }
    }

    private float lastMoveX;
    private float lastMoveY;

    public void setStart(float startX, float startY){
        lastMoveX = startX;
        lastMoveY = startY;
    }

    public void pushMove(float newX, float newY){
        float dx = newX - lastMoveX;
        float dy = newY - lastMoveY;
        if(abs(dx) < 10 && abs(dy) < 10){
            return;
        }
        setDirection(dx, dy);
        lastMoveY = newY;
        lastMoveX = newX;
        pathPoints.add(new Point((int)dx,(int)dy));
    }

    private void setDirection(float dx, float dy){
        int newDirection = 0;
        if(abs(dx) > abs(dy)){
            if(dx > 0){
                // Moving to the right;
                newDirection = 3;
            }else {
                //moving to the left
                newDirection = 4;
            }
        }else {
            if(dy > 0){
                // Moving down/forward;
                newDirection = 1;
            }else {
                //moving up/back
                newDirection = 2;
            }
        }
        if(newDirection != direction){
            setAnimation(newDirection);
        }
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

    public void modifySpeed() {
        speedModifier -=0.025;
    }

    public void setInitialSpeed(){
        speedModifier = 2;
    }

    public Vector<Point> getPathPoints(){
        return pathPoints;
    }

    public void update(){
        if( pathPoints.size() > 0 && isMoving) {
            Point point = pathPoints.firstElement();
            setDirection(point.x, point.y);
            double dx = point.x/speedModifier;
            double dy = point.y/speedModifier;
            if(!colliding) {
                x += dx;
                y += dy;

                if (x >= maxX || x <= minX) {
                    x -= dx;
                    box.offset(-(int) dx, 0);
                }
                if (y >= maxY || y <= minY) {
                    y -= dy;
                    box.offset(0, -(int) dy);
                }
                box.offsetTo(x + playerSize / 4, y + playerSize / 2);
                offsetSafeSpace(x, y);
                pathPoints.remove(point);
            }else{
                x = cleanX;
                y = cleanY;
                box.offsetTo(x + playerSize / 4, y + playerSize / 2);
                offsetSafeSpace(x, y);
                //setColliding(false);
                pathPoints.clear();
            }
            animate();
        }

    }

    private void offsetSafeSpace(int x, int y){
        safeSpace.offsetTo(box.left - safeWidth, box.top - safeheight);
    }

    public Rect getSafeSpace(){
        return safeSpace;
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
        return playerSize;
    }

    public double getSpeed(){
        return speedModifier;
    }

    private void initAnimations(){
        moveForward[0] = front_move_1;
        moveForward[1] = front_move_2;
        moveBack[0] = back_move_1;
        moveBack[1] = back_move_2;
        moveLeft[0] = left_move_1;
        moveLeft[1] = left_move_2;
        moveRight[0] = right_move_1;
        moveRight[1] = right_move_2;
    }

    private void initBitmaps(Context context){
        front_still = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_fram), playerSize,playerSize, true);
        front_move_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_fram_1),playerSize,playerSize, true);
        front_move_2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_fram_2),playerSize,playerSize, true);
        left_still = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_vanster),playerSize,playerSize, true);
        left_move_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_vanster_1),playerSize,playerSize, true);
        left_move_2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_vanster_2),playerSize,playerSize, true);
        right_still = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_hoger),playerSize,playerSize, true);
        right_move_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_hoger_1),playerSize,playerSize, true);
        right_move_2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_hoger_2),playerSize,playerSize, true);
        back_still = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_bakifran),playerSize,playerSize, true);
        back_move_1 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_bakifran_1),playerSize,playerSize, true);
        back_move_2 = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.spel_bakifran_2),playerSize,playerSize, true);
    }

    Bitmap front_still;
    Bitmap front_move_1;
    Bitmap front_move_2;
    Bitmap left_still ;
    Bitmap left_move_1;
    Bitmap left_move_2;
    Bitmap right_still;
    Bitmap right_move_1;
    Bitmap right_move_2;
    Bitmap back_still ;
    Bitmap back_move_1;
    Bitmap back_move_2;


    public int getSkurkSpawnSafeDistance() {
        return playerSize * 4;
    }
}
