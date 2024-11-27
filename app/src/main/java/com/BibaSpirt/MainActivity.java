package com.BibaSpirt;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.updateLocale(this);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        setContentView(R.layout.activity_main);
        startService(svc);
    }

    public void startGame(View view) {
        playSound();
        startActivity(new Intent(this, GameActivity.class));
    }

    public void viewAchievements(View view) {
        playSound();
        startActivity(new Intent(this, AchievementsActivity.class));
    }

    public void viewStatistics(View view) {
        playSound();
        startActivity(new Intent(this, StatisticsActivity.class));
    }

    public void viewSettings(View view) {
        playSound();
        langChanged.launch(new Intent(this, SettingsActivity.class));
    }

    ActivityResultLauncher langChanged = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), (ActivityResultCallback<androidx.activity.result.ActivityResult>) result -> {
        try {
            if (result.getData().getBooleanExtra("langChanged", false)) {
                recreate();
            } else {
                Log.d("@@@", ": false");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    });

}