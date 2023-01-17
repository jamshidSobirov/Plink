package com.BibaSpirt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class StatisticsActivity extends BaseActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        initViews();
    }

    private void initViews() {
        sharedPreferences = this.getSharedPreferences("MySharedPref", MODE_PRIVATE);

        ((TextView) findViewById(R.id.tvBestScore)).setText(String.valueOf(sharedPreferences.getInt("bestScore", 0)));
        ((TextView) findViewById(R.id.tvLastGameResult)).setText(String.valueOf(sharedPreferences.getInt("lastGameResult", 0)));
        ((TextView) findViewById(R.id.tvGamesPlayed)).setText(String.valueOf(sharedPreferences.getInt("gamesPlayed", 0)));
    }

    public void returnToMenu(View view) {
        playSound();
        finish();
    }
}