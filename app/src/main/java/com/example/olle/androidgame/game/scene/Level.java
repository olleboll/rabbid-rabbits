package com.example.olle.androidgame.game.scene;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.olle.androidgame.R;
import com.example.olle.androidgame.game.entities.Player;
import com.example.olle.androidgame.game.entities.Tree;
import com.example.olle.androidgame.utils.SkurkSpawn;

import java.util.ArrayList;

/**
 * Created by Olle on 2017-05-23.
 */

public class Level {

    private Context context;
    private int screenX, screenY;
    private int levelProgress;

    private int[] goal = {5, 12, 16, 25, 32, 40, 1};

    private Bitmap bg;

    private double minSkurkSpeed, maxSkurkSpeed;

    private ArrayList<Tree> trees;

    private Player player;

    public Level(Context context, int screenX, int screenY, Player player){
        this.trees = new ArrayList<Tree>();
        this.context = context;
        this.screenX = screenX;
        this.screenY = screenY;
        this.player = player;
        this.bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gras), screenX,screenY, true);
    }

    public void setUpLevel(int level){
        levelProgress = level;
        trees.clear();
        System.out.println("LEVEL: " + level);
        switch(level){
            case 0:
                minSkurkSpeed = 3;
                maxSkurkSpeed = 7;
                trees.add(new Tree(this.context, 250, 450));
                trees.add(new Tree(this.context, 650, 750));
                break;
            case 1:
                minSkurkSpeed = 3;
                maxSkurkSpeed = 3;
                break;
            case 2:
                //this.bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gras), screenX,screenY, true);
                minSkurkSpeed = 3;
                maxSkurkSpeed = 4;
                trees.add(new Tree(this.context, 250, 450));
                break;
            case 3:
                //this.bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gras), screenX,screenY, true);
                minSkurkSpeed = 3;
                maxSkurkSpeed = 4;
                trees.add(new Tree(this.context, 250, 450));
                trees.add(new Tree(this.context, 650, 750));
                break;
            case 4:
                //this.bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gras), screenX,screenY, true);
                minSkurkSpeed = 4;
                maxSkurkSpeed = 4;
                trees.add(new Tree(this.context, 250, 450));
                trees.add(new Tree(this.context, 650, 750));
                break;
            case 5:
                //this.bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gras), screenX,screenY, true);
                minSkurkSpeed = 4;
                maxSkurkSpeed = 5;
                trees.add(new Tree(this.context, 250, 450));
                trees.add(new Tree(this.context, 650, 750));
                break;
            case 6:
                //this.bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gras), screenX,screenY, true);
                minSkurkSpeed = 3;
                maxSkurkSpeed = 6;
                trees.add(new Tree(this.context, 250, 450));
                trees.add(new Tree(this.context, 650, 750));
                break;
            case 7:
                //this.bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.gras), screenX,screenY, true);
                minSkurkSpeed = 7;
                maxSkurkSpeed = 7;
                trees.add(new Tree(this.context, 250, 450));
                trees.add(new Tree(this.context, 650, 750));
                break;
        }
        player.ensurePlayerSafety(trees);
    }

    public int getLevelStartScore(){
        if (levelProgress == 1) {
            return 0;
        }
        return goal[levelProgress-2];
    }

    public int[] getGoal(){
        return goal;
    }

    public int getLevelProgress(){
        return levelProgress;
    }

    public Bitmap getBG(){
        return bg;
    }

    public ArrayList<Tree> getTrees(){
        return trees;
    }

    public double getMinSkurkSpeed(){
        return minSkurkSpeed;
    }

    public double getMaxSkurkSpeed(){
        return maxSkurkSpeed;
    }

    public SkurkSpawn generateSkurkSpawn(Player player, boolean panic) {
        Point center = new Point(player.getBox().centerX(), player.getBox().centerY());
        int skurkX, skurkY;
        int safeD = player.getSkurkSpawnSafeDistance();
        if (panic){
            safeD += trees.get(0).getSize();
        }
        //safeD = 200;
        int dirX, dirY;
        if (center.x > screenX/2) {
            skurkX = center.x - safeD;
            dirX = 3;
        } else {
            skurkX = center.x + safeD;
            dirX = 4;
        }
        if (center.y > screenY/2) {
            skurkY = center.y - safeD;
            dirY = 1;
        } else {
            skurkY = center.y + safeD;
            dirY = 2;
        }
        int dir;
        if (Math.random() > 0.5) {
            dir = dirX;
            skurkY = center.y;
        } else {
            dir = dirY;
            skurkX = center.x;
        }

        SkurkSpawn spawn = new SkurkSpawn(skurkX, skurkY, dir);

        return spawn;
    }

    private int safeSpaceSize = 128;
    private boolean treeInTheWay(int x, int y) {
        Rect safeSpace = new Rect(x-safeSpaceSize, y - safeSpaceSize, x + safeSpaceSize, y + safeSpaceSize);
        for (Tree t : trees){
            if (Rect.intersects(safeSpace, t.getBox())){
                return true;
            }
        }
        return false;
    }
    /*
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
    */
}
