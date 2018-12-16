package com.example.olle.androidgame.map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.example.olle.androidgame.R;
import com.example.olle.androidgame.game.GameActivity;

public class MapActivity extends AppCompatActivity {

    private TextView buttonPlay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        buildLevelButtons();
    }
    @Override
    protected void onResume() {
        super.onResume();
        LinearLayout oldList = (LinearLayout) findViewById(R.id.buttonList);
        oldList.removeAllViews();
        buildLevelButtons();
    }

    public void onClickStart(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("level", 3); //Optional parameters

        startActivity(intent);
    }

    private void buildLevelButtons(){
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttonList);
        layout.setOrientation(LinearLayout.VERTICAL);  //Can also be done in xml by android:orientation="vertical"
        int maxLevel = getMaxLevel();
        int currentLevel = 1;
        int rows = (int) Math.ceil(maxLevel / 4.0);
        System.out.println("maxlevel: "+maxLevel);
        System.out.println("rows: "+rows);

        for (int i = 0; i < rows ; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

            for (int j = 0; j < 4; j++) {
                if (currentLevel-1 == maxLevel) {
                    break;
                }
                Button btn = new Button(this);
                btn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
                final int thisLevel = currentLevel;
                btn.setText("Level " + thisLevel);
                btn.setId(thisLevel);
                btn.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MapActivity.this, GameActivity.class);
                        intent.putExtra("level", thisLevel); //Optional parameters
                        intent.putExtra("mode", "campaign");

                        startActivity(intent);
                    }
                });
                currentLevel++;

                row.addView(btn);
            }
            layout.addView(row);
        }
    }


    private int getMaxLevel(){
        SharedPreferences sharedPref = this.getSharedPreferences("LEVEL_DATA", Context.MODE_PRIVATE);
        int reachedLevel = sharedPref.getInt("progress", 1);
        return reachedLevel;
    }

}
