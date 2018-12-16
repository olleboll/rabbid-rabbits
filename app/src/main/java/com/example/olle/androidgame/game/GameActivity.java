package com.example.olle.androidgame.game;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);
        //Initializing game view object
        //this time we are also passing the screen size to the GameView constructor
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        System.out.println("mode gotten from previois activity: " + mode);
        if (mode.equals("arcade")){
            System.out.println("creating arcade game");
            gameView = new GameView(this, size.x, size.y, 0);
        } else {
            int level = intent.getIntExtra("level", 1);
            gameView = new GameView(this, size.x, size.y, level);
        }


        //adding it to contentview
        setContentView(gameView);
    }

    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();
    }
}
