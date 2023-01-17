package com.BibaSpirt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AchievementsActivity extends BaseActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        initViews();


    }

    private void initViews() {
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        int gamesPlayed = sharedPreferences.getInt("gamesPlayed", 0);
        int bestScore = sharedPreferences.getInt("bestScore", 0);

        if (gamesPlayed <= 5) {
            ((ProgressBar) findViewById(R.id.progressBar1)).setProgress(gamesPlayed * 20);
            ((TextView) findViewById(R.id.progress1Desc)).setText(gamesPlayed + "/5");

            ((ProgressBar) findViewById(R.id.progressBar2)).setProgress(gamesPlayed * 10);
            ((TextView) findViewById(R.id.progress2Desc)).setText(gamesPlayed + "/10");

            ((ProgressBar) findViewById(R.id.progressBar3)).setProgress(gamesPlayed * 5);
            ((TextView) findViewById(R.id.progress3Desc)).setText(gamesPlayed + "/20");
        } else {
            ((ProgressBar) findViewById(R.id.progressBar1)).setProgress(100);
            ((TextView) findViewById(R.id.progress1Desc)).setText("5/5");
        }

        if (gamesPlayed > 5 && gamesPlayed <= 10) {
            ((ProgressBar) findViewById(R.id.progressBar2)).setProgress(gamesPlayed * 10);
            ((TextView) findViewById(R.id.progress2Desc)).setText(gamesPlayed + "/10");

            ((ProgressBar) findViewById(R.id.progressBar3)).setProgress(gamesPlayed * 5);
            ((TextView) findViewById(R.id.progress3Desc)).setText(gamesPlayed + "/20");
        } else if (gamesPlayed > 10) {
            ((ProgressBar) findViewById(R.id.progressBar2)).setProgress(100);
            ((TextView) findViewById(R.id.progress2Desc)).setText("10/10");
        }

        if (gamesPlayed > 10 && gamesPlayed <= 20) {
            ((ProgressBar) findViewById(R.id.progressBar3)).setProgress(gamesPlayed * 5);
            ((TextView) findViewById(R.id.progress3Desc)).setText(gamesPlayed + "/20");
        } else if (gamesPlayed > 20) {
            ((ProgressBar) findViewById(R.id.progressBar3)).setProgress(100);
            ((TextView) findViewById(R.id.progress3Desc)).setText("20/20");
        }

        if (bestScore <= 500) {
            ((ProgressBar) findViewById(R.id.progressBar4)).setProgress(bestScore / 5);
            ((TextView) findViewById(R.id.progress4Desc)).setText(bestScore + "/500");
            ((TextView) findViewById(R.id.progress5Desc)).setText(bestScore + "/1500");
            ((TextView) findViewById(R.id.progress6Desc)).setText(bestScore + "/2000");
            ((ProgressBar) findViewById(R.id.progressBar6)).setProgress(bestScore / 20);
            ((ProgressBar) findViewById(R.id.progressBar5)).setProgress(bestScore / 15);
        } else {
            ((ProgressBar) findViewById(R.id.progressBar4)).setProgress(100);
            ((TextView) findViewById(R.id.progress4Desc)).setText("500/500");
        }

        if (bestScore > 500 && bestScore <= 1500) {
            ((ProgressBar) findViewById(R.id.progressBar5)).setProgress(bestScore / 15);
            ((TextView) findViewById(R.id.progress5Desc)).setText(bestScore + "/1500");
            ((TextView) findViewById(R.id.progress6Desc)).setText(bestScore + "/2000");
            ((ProgressBar) findViewById(R.id.progressBar6)).setProgress(bestScore / 20);

        } else if (bestScore > 1500) {
            ((ProgressBar) findViewById(R.id.progressBar5)).setProgress(100);
            ((TextView) findViewById(R.id.progress5Desc)).setText("1500/1500");
        }

        if (bestScore > 1500 && bestScore <= 2000) {
            ((ProgressBar) findViewById(R.id.progressBar6)).setProgress(bestScore / 20);
            ((TextView) findViewById(R.id.progress6Desc)).setText(bestScore + "/2000");
        } else if (bestScore > 2000) {
            ((ProgressBar) findViewById(R.id.progressBar6)).setProgress(100);
            ((TextView) findViewById(R.id.progress6Desc)).setText("2000/2000");
        }


    }

    public void returnToMenu(View view) {
        playSound();
        finish();
    }
}