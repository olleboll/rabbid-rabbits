package com.example.olle.androidgame.highscore;


import android.app.ListActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.olle.androidgame.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HighScoreActivity extends ListActivity {

    private ListView highscore;

    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        highscore = (ListView) findViewById(android.R.id.list);
        list = new ArrayList<String>();

        adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_layout, R.id.textItem,
                list);
        highscore.setAdapter(adapter);

        getHighScore();

    }

    private void getHighScore(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://rr-api.maidendance.com/api/v1/score/highscore";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setupHighScore(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("highscore request failed :(");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void setupHighScore(String response){
        System.out.println("score obj" + response);
        JSONObject all;
        JSONArray scores;
        try {
            all = new JSONObject(response);
            scores = all.getJSONArray("highscore");
            System.out.println(scores);

            for (int i = 0; i < scores.length(); i++){
                JSONObject s = scores.getJSONObject(i);
                JSONObject u = s.getJSONObject("User");
                System.out.println(s);
                String text = Integer.toString(i+1) + ". " + u.getString("userName");
                text +=" : "+s.getString("score");
                adapter.add(text);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
