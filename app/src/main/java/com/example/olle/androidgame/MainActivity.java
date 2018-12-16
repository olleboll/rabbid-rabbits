package com.example.olle.androidgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.olle.androidgame.game.GameActivity;
import com.example.olle.androidgame.credits.CreditsActivity;
import com.example.olle.androidgame.highscore.HighScoreActivity;
import com.example.olle.androidgame.map.MapActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private TextView buttonPlay;
    private TextView buttonMap;
    private TextView highScore;
    private TextView credits;
    private TextView exit;

    private Context context;
    private SharedPreferences storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        buttonPlay = (TextView) findViewById(R.id.buttonPlay);
        buttonMap = (TextView) findViewById(R.id.buttonMap);
        highScore = (TextView) findViewById(R.id.buttonHighScore);
        credits = (TextView) findViewById(R.id.buttonCredits);
        exit = (TextView) findViewById(R.id.buttonExit);
        //this.localStorage = a.getPreferences(Context.MODE_PRIVATE);
        storage = this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        Log.i("rabbid", "starting");
        //clearPref(storage);

        if (storage.getString("userName", "NoneZipNothingLol") == "NoneZipNothingLol") {
            initNameDialog();
        }
    }

    private void clearPref(SharedPreferences storage){
        storage.edit().clear().commit();
    }

    private void initNameDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your name");
        builder.setMessage(
                "Will be shown in highscore list."
        );

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userName = input.getText().toString();
                postUserName(userName);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void createNewUser(String handle){
        Log.i("rabbid", "creating user" + handle);
    }

    public void onClickStart(View v) {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("mode", "arcade"); //Optional parameters
        startActivity(intent);
    }

    public void onClickMap(View view) {
        startActivity(new Intent(this, MapActivity.class));
    }

    public void onClickHighScore(View v) {
        startActivity(new Intent(this, HighScoreActivity.class));
    }

    public void onClickCredits(View v) {
        startActivity(new Intent(this, CreditsActivity.class));
    }

    public void onClickExit(View v) {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void postUserName(final String handle) {

        RequestQueue queue = Volley.newRequestQueue(this.context);

        String url = "https://rr-api.maidendance.com/api/v1/user";
        JSONObject params = new JSONObject();
        Log.i("rabbid", "screating user?" + handle);

        try {
            params.put("userName", handle);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            SharedPreferences.Editor editor = storage.edit();
                            String userID = response.optString("id");
                            String userName = response.optString("userName");
                            editor.putString("userName", userName);
                            editor.putString("userID", userID);
                            editor.commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("ERROR", error.getMessage());
                VolleyLog.e("Error: ", error);
            }
        });
        queue.add(request);
    }

}