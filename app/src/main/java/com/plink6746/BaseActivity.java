package com.plink6746;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class BaseActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    public static MediaPlayer mp;
    Intent svc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        mp = MediaPlayer.create(this, R.raw.button_pressed);
        mp.setLooping(false);
        svc = new Intent(this, BackgroundSoundService.class);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.updateLocale(newBase));
    }

    public void playSound() {
        try {
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("@@@", "startAudio: " + e.getMessage());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
