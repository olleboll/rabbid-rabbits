package com.example.olle.androidgame.game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.util.Log;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.olle.androidgame.R;
import com.example.olle.androidgame.game.entities.Apple;
import com.example.olle.androidgame.game.entities.Entity;
import com.example.olle.androidgame.game.entities.Painter;
import com.example.olle.androidgame.game.entities.Player;
import com.example.olle.androidgame.game.entities.ScoreFlash;
import com.example.olle.androidgame.game.entities.Skurk;
import com.example.olle.androidgame.game.entities.Tree;
import com.example.olle.androidgame.game.scene.Level;
import com.example.olle.androidgame.utils.SkurkSpawn;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static android.provider.Settings.Global.getString;

public class GameView extends SurfaceView implements Runnable {

    Context context;

    volatile boolean playing;
    private Thread gameThread = null;

    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    private ScoreFlash flash;

    private int displayScreenY;
    private int displayScreenX;
    private Apple apple;
    private ArrayList<Skurk> skurkar;
    private ArrayList<Entity> entities;
    private ArrayList<Tree> trees;
    private Level level;

    private int points = 0;
    private int lastScore = 0;
    private int highscore = 0;
    private int fps;
    private int time;

    private SharedPreferences storage;
    SharedPreferences.Editor editor;
    private int previousProgress;
    private String mode;

    public GameView(Context context, int screenX, int screenY, int stage) {
        super(context);
        this.context = context;
        skurkar = new ArrayList<Skurk>();
        entities = new ArrayList<Entity>();
        trees = new ArrayList<Tree>();


        displayScreenY = screenY;
        displayScreenX = screenX;
        player = new Player(context, screenX, screenY - 200);
        surfaceHolder = getHolder();
        paint = new Paint();

        flash = new ScoreFlash(context, 0,0);

        level = new Level(context, screenX, screenY, player);
        apple = new Apple(context, 200,500);
        Activity a = (Activity) this.getContext();
        //this.localStorage = a.getPreferences(Context.MODE_PRIVATE);
        this.storage = a.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        this.editor = storage.edit();
        System.out.println("stage is: " + stage);
        if (stage == 0) {
            mode = "arcade";
            setLevel(0);
            highscore = getHighscore();
        } else {
            mode = "campaign";
            setLevel(stage);
            setPoints();
            this.previousProgress = storage.getInt("progress", 1);
        }

    }

    private void setPoints(){
        if (level.getLevelProgress() > 1){
            points = level.getLevelStartScore();
        }
    }

    private void setHighScore(int score){
        //Activity a = (Activity) this.getContext();
        //SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("highscore", score);
        editor.commit();
        postHighScore(score);

    }

    private int getHighscore(){
        //Activity a = (Activity) this.getContext();
        //SharedPreferences sharedPref = a.getPreferences(Context.MODE_PRIVATE);
        int highScore = storage.getInt("highscore", 0);
        return highScore;
    }

    private void updateProgress(int level){
        //Activity a = (Activity) this.getContext();
        //SharedPreferences sharedPref = a.getSharedPreferences("LEVEL_DATA", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("progress", level);
        editor.commit();
    }

    private void setLevel(int levelNumber){
        level.setUpLevel(levelNumber);
        entities.add(apple);
        entities.add(player);
        if(level.getTrees().size() > 0){
            trees.addAll(level.getTrees());
            entities.addAll(level.getTrees());
        }

    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0/60.0;
        double delta = 0;
        int frames = 0;
        while(playing){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                update();
                delta--;
            }
            draw();
            frames++;
            if(System.currentTimeMillis() - timer > 1000){
                timer += 1000;
                fps = frames;
                frames = 0;
            }
        }

    }

    private void update(){

        player.update();
        boolean col = false;
        for(Tree tree : trees) {
            if (checkCollission(player, tree)) {
                col = true;
            }
        }
        if(col){
            player.setColliding(true);
        }else{
            player.setColliding(false);
        }
        if(checkCollission(player,apple)){
            int appleX = randomWithRange(100, displayScreenX - 100);
            int appleY = randomWithRange(25, displayScreenY - 220);

            while(player.getSafeSpace().contains(appleX,appleY) |
                    treeInTheWay(appleX, appleY)) {
                appleX = randomWithRange(100, displayScreenX - 100);
                appleY = randomWithRange(25, displayScreenY - 220);
            }
            boolean panic = false;
            SkurkSpawn p = level.generateSkurkSpawn(player, panic);
            int tries = 1;
            while (treeInTheWay(p.getX(), p.getY())){
                if (tries > 4) {
                    panic = true;
                }
                p = level.generateSkurkSpawn(player, panic);
                System.out.println(p.getX() + " : " + p.getY());
                tries++;
            }
            entities.remove(apple);
            apple = new Apple(super.getContext(), appleX, appleY);
            int dir = p.getDir();
            double speed = randomWithRangeD(level.getMinSkurkSpeed(), level.getMaxSkurkSpeed());
            Skurk s = new Skurk(super.getContext(), p.getX(), p.getY(), displayScreenX, displayScreenY - 200, dir, speed, this.level);
            skurkar.add(s);
            entities.add(s);
            entities.add(apple);

            player.modifySpeed();

            points++;
            flash.init(player.getX(), player.getY(), "Well done!", 2, 75);
            if (mode.equals("campaign")){
                if(points == level.getGoal()[level.getLevelProgress()-1]){
                    entities.clear();
                    skurkar.clear();
                    trees.clear();
                    setLevel(level.getLevelProgress()+1);

                    if (level.getLevelProgress() > previousProgress){
                        updateProgress(level.getLevelProgress());
                    }
                }
            }
        }

        for(Skurk skurk : skurkar){
            skurk.update();
            if(checkCollission(player, skurk)){
                System.out.println(displayScreenX/2);
                flash.init(0, player.getY(), "Oh no.. the bunnies got you!", 1, 0);
                skurkar.clear();
                entities.clear();
                trees.clear();
                lastScore = points;
                if (mode.equals("arcade")) {
                    //highscore = lastScore;

                    if(lastScore > highscore){
                        highscore = lastScore;
                        setHighScore(highscore);
                    }

                    setLevel(0);
                } else {
                    setLevel(1);
                }

                points = 0;
                player.setInitialSpeed();
                break;
            }
        }
        if (flash.visible()){
            flash.update();
        }
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

    private int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    private double randomWithRangeD(double min, double max) {
        double range = (max - min) + 1;
        return (Math.random() * range) + min;
    }

    private boolean checkCollission(Entity player, Entity e) {
        if(Rect.intersects(player.getBox(), e.getBox())){
            return true;
        }else{
            return false;
        }
    }
    private void drawBoxes(Paint paint, Canvas canvas) {
        paint.setColor(Color.WHITE);
        canvas.drawRect(apple.getBox(), paint);
        canvas.drawRect(player.getBox(), paint);
    }
    int size = 1;
    private void draw(){

        Collections.sort(entities, new Painter());

        if(surfaceHolder.getSurface().isValid()){
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            canvas.drawRect(10, 10, displayScreenX, displayScreenY, paint);

            drawLevel(canvas);

            //canvas.drawRect(player.getSafeSpace(), paint);
            // For debugging
            //drawBoxes(paint, canvas);

            for(Entity e : entities){
                Bitmap b = e.getBitmap();

                canvas.drawBitmap(
                        e.getBitmap(),
                        e.getX(),
                        e.getY(),
                        paint);
            }



            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            String s;
            if (mode.equals("arcade")) {
                s =  "FPS: " + fps + ", Points: " + points + ", Last score: "+ lastScore + ", highscore: "+ highscore;
            } else {
                s = "FPS: " + fps + ", Points: " + points + ", Last score: "+ lastScore + ", highscore: "+ highscore + ", Level: " + level.getLevelProgress();
            }

            canvas.drawText(s, 40, displayScreenY - 150, paint);

            if (flash.visible()) {
                paint.setColor(Color.CYAN);
                paint.setTextSize(flash.getSize());
                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                String flashString[] = flash.getText();
                for (int i = 0 ; i < flashString.length; i++) {
                    canvas.drawText(flashString[i], flash.getX() + i * 50, flash.getY() + i * 100, paint);
                }
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawLevel(Canvas canvas) {
        canvas.drawBitmap(
                level.getBG(),
                0,
                0,
                paint);
    }

    public void pause(){
        playing = false;
        try {
            gameThread.join();
        }catch(InterruptedException e){
        }
    }

    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.setMoving(false);
                break;
            case MotionEvent.ACTION_DOWN:
                player.setMoving(true);
                if (flash.visible()){
                    flash.remove();
                }
                player.setStart(motionEvent.getX(), motionEvent.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                player.pushMove(motionEvent.getX(), motionEvent.getY());
                break;
        }
        return true;
    }

    private void postHighScore(final int score) {

        RequestQueue queue = Volley.newRequestQueue(this.context);

        String url = "https://rr-api.maidendance.com/api/v1/score";
        JSONObject params = new JSONObject();

        String userID = storage.getString("userID", "lol");
        String userName = storage.getString("userName", "nope");

        if (userName.equals("nope") || userID.equals("lol")){
            return;
        }

        try {
            params.put("score", Integer.toString(score));
            params.put("user_id", userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });
        queue.add(request);
    }

}
