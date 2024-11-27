package com.plink6746;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.SwitchCompat;

public class SettingsActivity extends BaseActivity {
    private String currentLang;
    private SwitchCompat switchMusic;
    private SwitchCompat switchLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
    }

    private void initViews() {
        currentLang = sharedPreferences.getString(LocaleHelper.SELECTED_LANGUAGE, "en");
        
        switchMusic = findViewById(R.id.switchMusic);
        switchLanguage = findViewById(R.id.switchLanguage);

        // Set initial states
        switchMusic.setChecked(isMyServiceRunning(BackgroundSoundService.class));
        switchLanguage.setChecked(currentLang.equals("en"));

        // Music switch listener
        switchMusic.setOnCheckedChangeListener((buttonView, isChecked) -> {
            playSound();
            if (isChecked) {
                startService(svc);
            } else {
                stopService(svc);
            }
        });

        // Language switch listener
        switchLanguage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String newLang = isChecked ? "en" : "ru";
            if (!currentLang.equals(newLang)) {
                playSound();
                LocaleHelper.setLocale(SettingsActivity.this, newLang);
                Intent intent = new Intent();
                intent.putExtra("langChanged", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void returnToMenu(View view) {
        playSound();
        finish();
    }
}